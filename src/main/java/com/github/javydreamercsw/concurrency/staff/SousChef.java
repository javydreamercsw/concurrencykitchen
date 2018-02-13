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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.ProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.exception.NotEnoughIngredientException;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class SousChef extends Cook implements EmployeeListener
{

    private final Map<Class<? extends Ingredient>, Float> WAITING
            = new HashMap<>();
    private final List<Recipe> recipes
            = Collections.synchronizedList(new ArrayList());
    private final ConcurrentLinkedQueue<Cook> cooks
            = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Cook> busyChefs
            = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Cook> idleChefs
            = new ConcurrentLinkedQueue<>();

    private static final Logger LOG
            = Logger.getLogger(SousChef.class.getName());

    public SousChef(String name)
    {
        super(name);
    }

    public boolean isWaiting(Class<? extends ProcessedIngredient> i)
    {
        return WAITING.containsKey(i);
    }

    public float getAmountWaiting(Class<? extends ProcessedIngredient> i)
    {
        return WAITING.get(i);
    }

    /**
     * Supervisee needs something to finish the recipe.
     *
     * @param i ingredient needed.
     * @param need amount needed.
     */
    public synchronized void notifyNeed(Class<? extends Ingredient> i,
            float need)
    {
        try
        {
            WAITING.put(i, need);
            if (i.isAssignableFrom(ProcessedIngredient.class))
            {
                ProcessedIngredient pi = (ProcessedIngredient) i.newInstance();
                addRecipe(pi.getRecipe());
            }
        } catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void addStaff(Cook cook)
    {
        cook.setManager(this);
        cooks.add(cook);
        cook.addListener(this);
    }

    public void cook()
    {
        try
        {
            //Al chefs are set to idle
            while (!cooks.isEmpty())
            {
                idleChefs.add(cooks.remove());
            }
            Recipe next = recipes.get(0);
            //Check if there are other recipes that needs to be done before
            List<Recipe> missing = analyzeIngredients(next, true);
            while (!missing.isEmpty())
            {
                //Insert them prior the real one.
                Recipe m = missing.remove(0);
                speakout("Need to prepare: " + m.getName());
                recipes.add(0, m);
            }
            while (!recipes.isEmpty())
            {
                if (!idleChefs.isEmpty())
                {
                    //Assign a cook
                    Cook chef = idleChefs.remove();
                    busyChefs.add(chef);
                    chef.addRecipe(recipes.remove(0));
                    chef.start();
                } else
                {
                    try
                    {
                        speakout("Waiting for a free cook...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex)
                    {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (NotEnoughIngredientException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addRecipe(Recipe r)
    {
        recipes.add(r);
    }

    public void notifyException(Exception ex)
    {
        //Got an exception, stop execution.
        LOG.log(Level.SEVERE, "Unable to prepare the recipe(s)", ex);
        //Stop all cooks
        speakout("Stopping the kitchen...");
        busyChefs.forEach(cook ->
        {
            cook.stopCooking();
        });
        speakout("Done!");
        System.exit(0);
    }

    @Override
    public void taskDone(Cook c)
    {
        busyChefs.remove(c);
        //Create a new one with the same name
        idleChefs.add(new Cook(c.getCookName()));
        if (busyChefs.isEmpty())
        {
            cleanup();
        }
    }
}
