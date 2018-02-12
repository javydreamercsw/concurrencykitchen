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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCookingProcess implements CookingProcess
{

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final List<Equipment> equipments = new ArrayList<>();

    @Override
    public abstract void gatherIngredients();

    @Override
    public abstract void processRecipe();

    @Override
    public abstract void gatherEquipment();

    public AbstractCookingProcess(List<Ingredient> ingredients, List<Equipment> equipments)
    {
    }

    /**
     * @return the ingredients
     */
    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }

    /**
     * @return the equipments
     */
    public List<Equipment> getEquipments()
    {
        return equipments;
    }
}
