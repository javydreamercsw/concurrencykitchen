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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.javydreamercsw.concurrency.ProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class SousChef extends Cook
{

    private final Map<Class<? extends ProcessedIngredient>, Float> WAITING
            = new HashMap<>();
    private final ConcurrentLinkedQueue<Recipe> recipes
            = new ConcurrentLinkedQueue<>();
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
    public synchronized void notifyNeed(Class<? extends ProcessedIngredient> i,
            float need)
    {
        try
        {
            WAITING.put(i, need);
            ProcessedIngredient pi = i.newInstance();
            addRecipe(pi.getRecipe());
        } catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void addStaff(Cook cook)
    {
        cook.setManager(this);
        cooks.add(cook);
    }

    public void cook()
    {
        //Al chefs are set to idle
        while (!cooks.isEmpty())
        {
            idleChefs.add(cooks.remove());
        }
        while (!recipes.isEmpty())
        {
            if (!idleChefs.isEmpty())
            {
                //Assign a cook
                Cook chef = idleChefs.remove();
                busyChefs.add(chef);
                chef.addRecipe(recipes.remove());
                chef.run();
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
    }

    @Override
    public void addRecipe(Recipe r)
    {
        recipes.add(r);
    }

    void notifyDone(Cook c)
    {
        busyChefs.remove(c);
        idleChefs.add(c);
    }
}
