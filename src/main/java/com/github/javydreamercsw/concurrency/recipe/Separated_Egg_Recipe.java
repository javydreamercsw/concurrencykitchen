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
package com.github.javydreamercsw.concurrency.recipe;

import java.util.Arrays;

import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.AbstractRecipe;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.RecipeStep;
import com.github.javydreamercsw.concurrency.equipment.Bowl;
import com.github.javydreamercsw.concurrency.ingredient.Egg;
import com.github.javydreamercsw.concurrency.ingredient.processed.Egg_White;
import com.github.javydreamercsw.concurrency.ingredient.processed.Egg_Yolk;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Recipe.class)
public class Separated_Egg_Recipe extends AbstractRecipe
{

    public Separated_Egg_Recipe()
    {
        //Ingredients
        getIngredients().put(Egg.class, 1f);

        //Output
        getResultingIngredients().put(Egg_White.class, 1f);
        getResultingIngredients().put(Egg_Yolk.class, 1f);

        //Steps
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Bowl.class), null,
                "In a bowl crack the egg open."));
        getSteps().add(new RecipeStep(getIngredients(),
                null, getResultingIngredients(),
                "Slowly separate the yolk from the white."));
    }
}
