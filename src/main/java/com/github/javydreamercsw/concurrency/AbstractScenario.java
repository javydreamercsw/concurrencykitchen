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
package com.github.javydreamercsw.concurrency;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import com.github.javydreamercsw.concurrency.staff.Cook;
import com.github.javydreamercsw.concurrency.staff.EmployeeListener;
import com.github.javydreamercsw.concurrency.staff.SousChef;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public abstract class AbstractScenario implements Scenario, EmployeeListener
{

    private static final Logger LOG
            = Logger.getLogger(AbstractScenario.class.getName());
    private SousChef chef;
    private final ConcurrentLinkedQueue<Recipe> recipes
            = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ScenarioListener> listeners
            = new ConcurrentLinkedQueue<>();

    @Override
    public final void addRecipe(Recipe r)
    {
        recipes.add(r);
    }

    @Override
    public void cook()
    {
        getChef().addListener(this);
        while (!recipes.isEmpty())
        {
            getChef().addRecipe(recipes.remove());
        }
        getChef().cook();
    }

    @Override
    public final synchronized void addCook(Cook cook)
    {
        if (getChef() != null)
        {
            getChef().addStaff(cook);
        }
    }

    @Override
    public void addListener(ScenarioListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void taskDone(Cook c, long time)
    {
        listeners.forEach(l ->
        {
            l.scenarioDone();
        });
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().replaceAll("_", " ");
    }

    /**
     * @return the chef
     */
    public final SousChef getChef()
    {
        return chef;
    }

    /**
     * @param chef the chef to set
     */
    public final void setChef(SousChef chef)
    {
        this.chef = chef;
    }
}
