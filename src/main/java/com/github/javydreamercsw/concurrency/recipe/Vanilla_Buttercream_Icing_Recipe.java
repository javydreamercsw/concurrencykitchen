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
import com.github.javydreamercsw.concurrency.ingredient.Butter_Emulsion;
import com.github.javydreamercsw.concurrency.ingredient.Confectioner_Sugar;
import com.github.javydreamercsw.concurrency.ingredient.Sea_Salt;
import com.github.javydreamercsw.concurrency.ingredient.Shortening;
import com.github.javydreamercsw.concurrency.ingredient.Vanilla;
import com.github.javydreamercsw.concurrency.ingredient.processed.Vanilla_Buttercream_Icing;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Recipe.class)
public class Vanilla_Buttercream_Icing_Recipe extends AbstractRecipe
{

    public Vanilla_Buttercream_Icing_Recipe()
    {
        //Ingredients
        getIngredients().put(Shortening.class, 2.5f);
        getIngredients().put(Confectioner_Sugar.class, 5f);
        getIngredients().put(Sea_Salt.class, 0.5f);
        getIngredients().put(Vanilla.class, 0.5f);
        getIngredients().put(Butter_Emulsion.class, 1f);

        //Output
        getResultingIngredients().put(Vanilla_Buttercream_Icing.class, 1f);

        //Steps
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Bowl.class), null,
                "In the bowl of an electric mixer fitted with the paddle "
                + "attachment, cream the shortening until smooth.",
                5000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                null, null,
                "Add the confectioners' sugar, sea salt, vanilla and butter emulsion."));
        getSteps().add(new RecipeStep(getIngredients(),
                null, getResultingIngredients(),
                "Mix it all on medium speed for 8 to 10 minutes.", 10000 * 60l));
    }
}
