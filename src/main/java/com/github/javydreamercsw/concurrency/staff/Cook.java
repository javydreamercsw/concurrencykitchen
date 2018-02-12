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

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.Lookup;

import com.github.javydreamercsw.concurrency.IRecipeStep;
import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.IngredientProvider;
import com.github.javydreamercsw.concurrency.MissingStorageException;
import com.github.javydreamercsw.concurrency.NotEnoughIngredientException;
import com.github.javydreamercsw.concurrency.ProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.Util;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class Cook extends Thread
{

    private final ConcurrentLinkedQueue<Recipe> recipes
            = new ConcurrentLinkedQueue<>();
    private final String name;
    private static final Logger LOG
            = Logger.getLogger(Cook.class.getName());
    private SousChef manager;

    public Cook(String name)
    {
        this.name = name;
    }

    public void setManager(SousChef m)
    {
        this.manager = m;
    }

    private long cook(Recipe recipe) throws MissingStorageException,
            InterruptedException, NotEnoughIngredientException
    {
        long timeElapsed = 0;
        speakout("Preparing " + recipe.getName());
        int count = 1;
        for (IRecipeStep rs : recipe.getSteps())
        {
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
                        Util.store(entry.getKey(), entry.getValue());
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
        while (!recipes.isEmpty())
        {
            try
            {
                Recipe current = recipes.remove();
                analyzeIngredients(current);
                //If we are here we have all the ingredients.
                //Start cooking. Only you cooking so not much planning.
                for (Recipe r : recipes)
                {
                    timeElapsed += cook(r);
                }
            } catch (NotEnoughIngredientException | InterruptedException | MissingStorageException ex)
            {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        speakout("Total time elapsed: "
                + Util.getTimeReadable(timeElapsed));
        if (manager != null)
        {
            //I'm done, let my manager know
            manager.notifyDone(this);
        }
    }

    /**
     * @return the name
     */
    public String getCookName()
    {
        return name;
    }

    private void analyzeIngredients(Recipe r) throws NotEnoughIngredientException
    {
        speakout("Analyzing recipe: " + r.getName());
        //Sort through the ingredients and see if some require preparation
        speakout("Gathering ingredients...");
        for (Entry<Class<? extends Ingredient>, Float> entry
                : r.getIngredients().entrySet())
        {
            try
            {
                Class<? extends Ingredient> i = entry.getKey();
                Ingredient ingredient = i.newInstance();
                float need = entry.getValue();
                //Check if there's some in storage
                for (IngredientProvider ip
                        : Lookup.getDefault().lookupAll(IngredientProvider.class))
                {
                    float obtained = ip.getIngredient(i, entry.getValue());
                    if (obtained > 0)
                    {
                        LOG.log(Level.FINE, "Obtained {0} {1} of {2} from {3}",
                                new Object[]
                                {
                                    obtained,
                                    ingredient.getUnits().getName(),
                                    ingredient.getName(), ip.getName()
                                });
                    }
                    need -= obtained;
                    if (need <= 0)
                    {
                        break;
                    }
                }
                if (need > 0)
                {
                    //Not in the storage, if it's a processed ingredient we might have some chance
                    if (ingredient instanceof ProcessedIngredient)
                    {
                        speakout("Need to prepare some " + ingredient.getName());
                        ProcessedIngredient pi = (ProcessedIngredient) ingredient;
                        analyzeIngredients(pi.getRecipe());
                        addRecipe(pi.getRecipe());
                    } else
                    {
                        throw new NotEnoughIngredientException(ingredient);
                    }
                }
            } catch (InstantiationException | IllegalAccessException ex)
            {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        speakout("Able to gather all ingredients!");
    }

    public synchronized void addRecipe(Recipe r)
    {
        recipes.add(r);
    }

    public void speakout(String s)
    {
        System.out.println(getCookName() + ": " + s);
    }
}
