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
package com.github.javydreamercsw.concurrency.ingredient.processed;

import com.github.javydreamercsw.concurrency.AbstractProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.UNIT;
import com.github.javydreamercsw.concurrency.recipe.Cream_Cheese_Filling_Recipe;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class Cream_Cheese_Filling extends AbstractProcessedIngredient
{

    @Override
    public Recipe getRecipe()
    {
        return new Cream_Cheese_Filling_Recipe();
    }

    @Override
    public UNIT getUnits()
    {
        return UNIT.EACH;
    }
}
