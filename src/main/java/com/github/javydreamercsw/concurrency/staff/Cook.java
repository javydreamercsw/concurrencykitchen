/*
 * Copyright 2018 Javier Ortiz Bultron <javierortiz@pingidentity.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.javydreamercsw.concurrency.staff;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.javydreamercsw.concurrency.Equipment;
import com.github.javydreamercsw.concurrency.IRecipeStep;
import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.ProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.Util;
import com.github.javydreamercsw.concurrency.exception.MissingStorageException;
import com.github.javydreamercsw.concurrency.exception.NotEnoughEquipmentException;
import com.github.javydreamercsw.concurrency.exception.NotEnoughIngredientException;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class Cook extends Thread
{

    private final ConcurrentLinkedQueue<Recipe> recipes
            = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<EmployeeListener> listeners
            = new ConcurrentLinkedQueue<>();
    private final String name;
    private static final Logger LOG
            = Logger.getLogger(Cook.class.getName());
    private SousChef manager;
    private boolean cook = true;

    public Cook(String name)
    {
        this.name = name;
    }

    public void setManager(SousChef m)
    {
        this.manager = m;
    }

    private long cook(Recipe recipe) throws MissingStorageException,
            InterruptedException, NotEnoughIngredientException,
            NotEnoughEquipmentException
    {
        long timeElapsed = 0;
        speakout("Preparing " + recipe.getName());
        int count = 1;
        for (IRecipeStep rs : recipe.getSteps())
        {
            if (rs.getRequiredEquipment() != null
                    && !rs.getRequiredEquipment().isEmpty())
            {
                //Need to retrieve equipment for this step.
                for (Class<? extends Equipment> e : rs.getRequiredEquipment())
                {
                    try
                    {
                        Util.getEquipment(e, 1);
                    } catch (NotEnoughEquipmentException ex)
                    {
                        speakout("Missing equipment: " + e.getSimpleName());
                        throw ex;
                    }
                }
            }
            speakout("Performing step #" + (count++)
                    + "\n" + rs.getDescription());
            if (rs.getTime() > 0)
            {
                speakout("Working...");
                Thread.sleep(rs.getTime() / 1000);
                timeElapsed += rs.getTime();
            } else
            {
                //Add 10 seconds to the elapsed time.
                Thread.sleep(10);
                timeElapsed += 10000;
            }
            if (rs.getOutput() != null)
            {
                //Store output in storage
                for (Entry<Class<? extends ProcessedIngredient>, Float> entry
                        : rs.getOutput().entrySet())
                {
                    Float amountMade = entry.getValue();
                    Float leftover = 0f;
                    if (manager != null)
                    {
                        if (manager.isWaiting(entry.getKey()))
                        {
                            leftover = amountMade
                                    - manager.getAmountWaiting(entry.getKey());
                            if (leftover < 0)
                            {
                                //We need to make more
                                manager.notifyNeed(entry.getKey(), leftover * -1);
                            }
                        }
                    } else
                    {
                        //Everything is surplus
                        leftover = amountMade;
                    }
                    if (leftover > 0)
                    {
                        //Storing surplus
                        Util.storeIngredient(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        speakout("Time elapsed: "
                + Util.getTimeReadable(timeElapsed));
        return timeElapsed;
    }

    @Override
    public void run()
    {
        long timeElapsed = 0;
        while (shouldCook() && !recipes.isEmpty())
        {
            try
            {
                Recipe current = recipes.remove();
                analyzeIngredients(current, false);
                //If we are here we have all the ingredients.
                //Start cooking. Only you cooking so not much planning.
                timeElapsed += cook(current);
            } catch (NotEnoughIngredientException | InterruptedException | MissingStorageException | NotEnoughEquipmentException ex)
            {
                manager.notifyException(ex);
                break;
            }
        }
        speakout("Total time elapsed: "
                + Util.getTimeReadable(timeElapsed));
        cleanup();
    }

    /**
     * @return the name
     */
    public String getCookName()
    {
        return name;
    }

    /**
     * Analyze the needed ingredients.
     *
     * @param r Recipe to analyze.
     * @param check True if you only want to check the list and not retrieve the
     * ingredients.
     * @return List of missing recipes.
     * @throws NotEnoughIngredientException
     */
    protected List<Recipe> analyzeIngredients(Recipe r, boolean check) throws NotEnoughIngredientException
    {
        List<Recipe> missing = new ArrayList<>();
        speakout("Analyzing recipe: " + r.getName());
        //Sort through the ingredients and see if some require preparation
        if (!check)
        {
            speakout("Gathering ingredients...");
        }
        for (Entry<Class<? extends Ingredient>, Float> entry
                : r.getIngredients().entrySet())
        {
            try
            {
                Class<? extends Ingredient> i = entry.getKey();
                Ingredient ingredient = i.newInstance();
                float need = entry.getValue();
                //Check if there's some in storage
                need -= check ? Util.hasIngredient(i, need) : Util.getIngredient(i, need);
                if (need > 0)
                {
                    //Not in the storage, if it's a processed ingredient we might have some chance
                    if (ingredient instanceof ProcessedIngredient)
                    {
                        speakout("Someone needs to prepare some "
                                + ingredient.getName());
                        ProcessedIngredient pi = (ProcessedIngredient) ingredient;
                        missing.add(pi.getRecipe());
                        if (manager != null)
                        {
                            //Let the manager know
                            manager.notifyNeed(pi.getClass(), need);
                        } else
                        {
                            //I have to do it myself
                            addRecipe(pi.getRecipe());
                        }
                    } else
                    {
                        if (manager != null)
                        {
                            //Let the manager know
                            manager.notifyNeed(entry.getKey(), need);
                        } else
                        {
                            throw new NotEnoughIngredientException(ingredient);
                        }
                    }
                }
            } catch (InstantiationException | IllegalAccessException ex)
            {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (!check)
        {
            if (missing.isEmpty())
            {
                speakout("Able to gather all ingredients!");
            } else
            {
                speakout("Waiting for ingredients!");
            }
        }
        return missing;
    }

    public synchronized void addRecipe(Recipe r)
    {
        recipes.add(r);
    }

    public void speakout(String s)
    {
        System.out.println(getCookName() + ": " + s);
    }

    public synchronized void stopCooking()
    {
        cook = false;
    }

    public synchronized boolean shouldCook()
    {
        return cook;
    }

    public void addListener(EmployeeListener listener)
    {
        listeners.add(listener);
    }

    protected void cleanup()
    {
        listeners.forEach(l ->
        {
            l.taskDone(this);
        });
    }
}
