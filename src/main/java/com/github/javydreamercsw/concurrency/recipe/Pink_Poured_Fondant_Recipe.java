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
import com.github.javydreamercsw.concurrency.equipment.Spatula;
import com.github.javydreamercsw.concurrency.ingredient.Almond_Extract;
import com.github.javydreamercsw.concurrency.ingredient.Confectioner_Sugar;
import com.github.javydreamercsw.concurrency.ingredient.Corn_Syrup;
import com.github.javydreamercsw.concurrency.ingredient.Food_Coloring_Pink;
import com.github.javydreamercsw.concurrency.ingredient.Liquid_Whitener_for_Icing;
import com.github.javydreamercsw.concurrency.ingredient.Vanilla_Extract;
import com.github.javydreamercsw.concurrency.ingredient.Water;
import com.github.javydreamercsw.concurrency.ingredient.processed.Pink_Poured_Fondant;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Recipe.class)
public class Pink_Poured_Fondant_Recipe extends AbstractRecipe
{

    public Pink_Poured_Fondant_Recipe()
    {
        //Ingredients
        getIngredients().put(Confectioner_Sugar.class, 5f);
        getIngredients().put(Water.class, 8f);
        getIngredients().put(Corn_Syrup.class, 5f);
        getIngredients().put(Almond_Extract.class, 1f);
        getIngredients().put(Vanilla_Extract.class, 1f);
        getIngredients().put(Liquid_Whitener_for_Icing.class, 2f);
        getIngredients().put(Food_Coloring_Pink.class, 0.5f);

        //Output
        getResultingIngredients().put(Pink_Poured_Fondant.class, 1f);

        //Steps
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Bowl.class), null,
                "In the bowl of an electric mixer fitted with the paddle "
                + "attachment, add the confectioners' sugar, then "
                + "the water, corn syrup, almond extract, vanilla, "
                + "liquid whitener and food coloring."));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Spatula.class), null,
                "Mix it all on medium speed until smooth, about 10 minutes.",
                10000 * 60l));
    }
}
