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

import com.github.javydreamercsw.concurrency.staff.Cook;
import com.github.javydreamercsw.concurrency.staff.SousChef;

/**
 * This represents a scenario for the different chapters in the book.
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public interface Scenario
{

    /**
     * Scenario name.
     *
     * @return name
     */
    String getName();

    /**
     * Get chapter this scenario is for.
     *
     * @return chapter number
     */
    int getChapter();

    /**
     * Run the scenario.
     */
    void cook();

    /**
     * Add a recipe for this scenario.
     *
     * @param r recipe
     */
    void addRecipe(Recipe r);

    /**
     * Add cook to scenario.
     *
     * @param chef cook to add.
     */
    public void addCook(Cook chef);

    /**
     * Add listener to the scenario
     *
     * @param listener
     */
    public void addListener(ScenarioListener listener);

    /**
     * Set the sous chef for the scenario.
     *
     * @param chef
     */
    public abstract void setChef(SousChef chef);
}
