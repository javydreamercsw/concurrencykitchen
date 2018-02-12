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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractRecipe implements Recipe
{

    /**
     * Steps of the recipe.
     */
    private final List<IRecipeStep> STEPS = new ArrayList<>();
    /**
     * A map of ingredients for this recipe and the required amount.
     */
    private final Map<Class<? extends Ingredient>, Float> INGREDIENTS
            = new HashMap<>();
    /**
     * A map with the results of the recipe.
     */
    private final Map<Class<? extends ProcessedIngredient>, Float> RESULTS
            = new HashMap<>();

    private final static Logger LOG
            = Logger.getLogger(AbstractRecipe.class.getName());

    public AbstractRecipe()
    {
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().replaceAll("_", " ");
    }

    @Override
    public final List<IRecipeStep> getSteps()
    {
        return STEPS;
    }

    @Override
    public final Map<Class<? extends Ingredient>, Float> getIngredients()
    {
        return INGREDIENTS;
    }

    @Override
    public final Map<Class<? extends ProcessedIngredient>, Float> getResultingIngredients()
    {
        return RESULTS;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(":").append("\n");
        sb.append("Ingredients").append(":").append("\n");
        INGREDIENTS.entrySet().forEach(entry ->
        {
            try
            {
                Ingredient i = entry.getKey().newInstance();
                sb.append("\t").append(entry.getValue()).append(" ")
                        .append(i.getUnits().getName())
                        .append(" ").append(i.getName()).append("\n");
            } catch (InstantiationException | IllegalAccessException ex)
            {
                LOG.log(Level.SEVERE, null, ex);
            }
        });
        sb.append("Directions").append(":").append("\n");
        int count = 0;
        for (IRecipeStep step : STEPS)
        {
            sb.append("\t").append(++count).append(") ")
                    .append(step.getDescription());
            if (step.getTime() > 0)
            {
                sb.append(" Takes approx. ")
                        .append(getTimeReadable(step.getTime()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    protected String getTimeReadable(long time)
    {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = time / daysInMilli;
        time = time % daysInMilli;

        long elapsedHours = time / hoursInMilli;
        time = time % hoursInMilli;

        long elapsedMinutes = time / minutesInMilli;
        time = time % minutesInMilli;

        long elapsedSeconds = time / secondsInMilli;

        return String.format(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);
    }
}
