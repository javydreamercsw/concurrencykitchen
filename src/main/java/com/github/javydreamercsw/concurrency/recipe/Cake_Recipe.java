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
import com.github.javydreamercsw.concurrency.equipment.Cake_Pan;
import com.github.javydreamercsw.concurrency.equipment.Mixer;
import com.github.javydreamercsw.concurrency.equipment.Oven;
import com.github.javydreamercsw.concurrency.ingredient.Almond_Extract;
import com.github.javydreamercsw.concurrency.ingredient.Baking_Powder;
import com.github.javydreamercsw.concurrency.ingredient.Cake_Flour;
import com.github.javydreamercsw.concurrency.ingredient.Cream_of_Tartar;
import com.github.javydreamercsw.concurrency.ingredient.processed.Egg_White;
import com.github.javydreamercsw.concurrency.ingredient.Granulated_Sugar;
import com.github.javydreamercsw.concurrency.ingredient.Inverted_Sugar;
import com.github.javydreamercsw.concurrency.ingredient.Milk_Powder;
import com.github.javydreamercsw.concurrency.ingredient.Sea_Salt;
import com.github.javydreamercsw.concurrency.ingredient.Shortening;
import com.github.javydreamercsw.concurrency.ingredient.Strawberry;
import com.github.javydreamercsw.concurrency.ingredient.Vanilla_Extract;
import com.github.javydreamercsw.concurrency.ingredient.Water;
import com.github.javydreamercsw.concurrency.ingredient.Whole_Egg;
import com.github.javydreamercsw.concurrency.ingredient.processed.Cake;
import com.github.javydreamercsw.concurrency.ingredient.processed.Cream_Cheese_Filling;
import com.github.javydreamercsw.concurrency.ingredient.processed.Ganache;
import com.github.javydreamercsw.concurrency.ingredient.processed.Pink_Poured_Fondant;
import com.github.javydreamercsw.concurrency.ingredient.processed.Vanilla_Buttercream_Icing;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Recipe.class)
public class Cake_Recipe extends AbstractRecipe
{

    public Cake_Recipe()
    {
        //Ingredients
        getIngredients().put(Shortening.class, 2f);
        getIngredients().put(Inverted_Sugar.class, 0.5f);
        getIngredients().put(Vanilla_Extract.class, 0.25f);
        getIngredients().put(Cake_Flour.class, 2.5f);
        getIngredients().put(Granulated_Sugar.class, 2.25f);
        getIngredients().put(Milk_Powder.class, 4f);
        getIngredients().put(Baking_Powder.class, 1.5f);
        getIngredients().put(Sea_Salt.class, 1.25f);
        getIngredients().put(Cream_of_Tartar.class, 0.25f);
        getIngredients().put(Egg_White.class, 3f);
        getIngredients().put(Whole_Egg.class, 1f);
        getIngredients().put(Water.class, 4f);
        getIngredients().put(Almond_Extract.class, 1.5f);
        getIngredients().put(Ganache.class, 1f);
        getIngredients().put(Cream_Cheese_Filling.class, 1f);
        getIngredients().put(Strawberry.class, 10f);
        getIngredients().put(Vanilla_Buttercream_Icing.class, 1f);
        getIngredients().put(Pink_Poured_Fondant.class, 1f);

        //Output
        getResultingIngredients().put(Cake.class, 1f);

        //Steps
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Cake_Pan.class), null,
                "Prepare two 16-inch cake pans by greasing them and lining the "
                + "bottoms with parchment paper."));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Bowl.class), null,
                "In the bowl of an electric mixer fixed with the paddle "
                + "attachment, add the liquid shortening, "
                + "inverted sugar and vanilla."));
        getSteps().add(new RecipeStep(getIngredients(),
                null, null,
                "Add the cake flour, granulated sugar, milk powder, "
                + "baking powder, sea salt and cream of tartar.", 3000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Mix on speed 1 for 1 minute.", 1000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Then add half of the egg whites and mix on speed 1 for "
                + "3 minutes.", 3000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Add the remaining egg whites and mix on speed 1 for 1 minute.",
                1000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Then mix on speed 3 for 3 minutes.",
                3000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                null, null,
                "Stop the mixer, scrape the bowl, and add the whole eggs, "
                + "water and almond extract."));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Mix on speed 1 for 1 minute.",
                1000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Then mix on speed 3 for 3 minutes.",
                3000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Mixer.class), null,
                "Lastly, mix on speed 1 for 10 minutes.",
                10000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(),
                Arrays.asList(Oven.class), getResultingIngredients(),
                "Fill the cake pans two-thirds full and bake until golden and "
                + "a tester such as a toothpick comes out clean, 35 to 45 "
                + "minutes. Let the cakes cool completely.",
                45000 * 60l));
        getSteps().add(new RecipeStep(getIngredients(), null, null,
                "To assemble the cake: Torte the cooled cakes (level and "
                + "split them horizontally into 2 layers each). "
                + "Generously spread a layer of the Ganache on the "
                + "first layer of cake, then a generous layer of "
                + "Cream Cheese Filling. Layer some fresh strawberries "
                + "on top of the Cream Cheese Filling. Repeat with the "
                + "next 2 cake layers. Top with the last cake layer "
                + "and ice the cake smooth with the Vanilla Buttercream "
                + "Icing. Lastly, pour a thin layer of Pink Poured "
                + "Fondant over the top of the cake, letting it drip "
                + "down the sides."));
    }
}
