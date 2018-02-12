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

import java.util.List;
import java.util.Map;

public class RecipeStep implements IRecipeStep
{

    private final Map<Class<? extends Ingredient>, Float> ingredients;
    private final List<Class<? extends Equipment>> equipment;
    private final Map<Class<? extends ProcessedIngredient>, Float> output;
    private final String description;
    //Time in seconds
    private final Long time;

    public RecipeStep(Map<Class<? extends Ingredient>, Float> ingredients,
            List<Class<? extends Equipment>> equipment,
            Map<Class<? extends ProcessedIngredient>, Float> output,
            String description)
    {
        this.ingredients = ingredients;
        this.equipment = equipment;
        this.output = output;
        this.description = description;
        time = 0l;
    }

    public RecipeStep(Map<Class<? extends Ingredient>, Float> ingredients,
            List<Class<? extends Equipment>> equipment,
            Map<Class<? extends ProcessedIngredient>, Float> output,
            String description, Long time)
    {
        this.ingredients = ingredients;
        this.equipment = equipment;
        this.output = output;
        this.description = description;
        this.time = time;
    }

    @Override
    public Map<Class<? extends Ingredient>, Float> getRequiredIngredients()
    {
        return ingredients;
    }

    @Override
    public List<Class<? extends Equipment>> getRequiredEquipment()
    {
        return equipment;
    }

    @Override
    public Map<Class<? extends ProcessedIngredient>, Float> getOutput()
    {
        return output;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    /**
     * @return the time
     */
    public Long getTime()
    {
        return time;
    }
}
