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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.Lookup;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class Util
{

    private static final Logger LOG
            = Logger.getLogger(Util.class.getName());

    private Util()
    {
    }

    public static String getTimeReadable(long time)
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

    public static void store(Class<? extends Ingredient> i, float amount)
            throws MissingStorageException
    {
        try
        {
            Ingredient ing = i.newInstance();
            LOG.log(Level.FINE, "Storing: {0} {1} of {2}",
                    new Object[]
                    {
                        amount, ing.getUnits(), ing.getName()
                    });
            boolean stored = false;
            for (IngredientProvider ip
                    : Lookup.getDefault().lookupAll(IngredientProvider.class))
            {
                if (ing.requiresRefrigeration() && ip.isRefrigerated())
                {
                    ip.addIngredient(i, amount);
                    stored = true;
                    break;
                } else if (!ing.requiresRefrigeration() && !ip.isRefrigerated())
                {
                    ip.addIngredient(i, amount);
                    stored = true;
                    break;
                }
            }
            if (!stored)
            {
                throw new MissingStorageException("Found no storage for "
                        + ing.getName());
            }
        } catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
