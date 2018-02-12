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
import com.github.javydreamercsw.concurrency.equipment.Microwave;
import com.github.javydreamercsw.concurrency.equipment.Spatula;
import com.github.javydreamercsw.concurrency.ingredient.Dark_Chocolate;
import com.github.javydreamercsw.concurrency.ingredient.Heavy_Cream;
import com.github.javydreamercsw.concurrency.ingredient.processed.Ganache;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Recipe.class)
public class Ganache_Recipe extends AbstractRecipe
{

    public Ganache_Recipe()
    {
        //Ingredients
        getIngredients().put(Dark_Chocolate.class, 16f);
        getIngredients().put(Heavy_Cream.class, 1f);

        //Output
        getResultingIngredients().put(Ganache.class, 1f);

        //Steps
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Microwave.class), null,
                "In a microwave-safe glass bowl, heat the chocolate in the "
                + "microwave in 30-second intervals until melted, stirring "
                + "between each intervals.", 5000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Spatula.class), null,
                "Add the cream and mix with a spatula until smooth."));
        getSteps().add(new RecipeStep(getIngredients(),
                null, getResultingIngredients(),
                "Let it cool.", 5000 * 60l));
    }
}
