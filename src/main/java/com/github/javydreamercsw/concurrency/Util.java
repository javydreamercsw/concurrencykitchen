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

import com.github.javydreamercsw.concurrency.exception.MissingStorageException;
import com.github.javydreamercsw.concurrency.exception.NotEnoughEquipmentException;
import com.github.javydreamercsw.concurrency.exception.NotEnoughIngredientException;

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

    public static void storeIngredient(Class<? extends Ingredient> i, float amount)
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

    public static Float hasIngredient(Class<? extends Ingredient> i, float need)
            throws NotEnoughIngredientException
    {
        return getIngredient(i, need, false);
    }

    public static Float getIngredient(Class<? extends Ingredient> i, float need)
            throws NotEnoughIngredientException
    {
        return getIngredient(i, need, true);
    }

    public static Float getIngredient(Class<? extends Ingredient> i,
            final float need,
            boolean get) throws NotEnoughIngredientException
    {
        try
        {
            Ingredient ingredient = i.newInstance();
            float obtained = 0f;
            for (IngredientProvider ip
                    : Lookup.getDefault().lookupAll(IngredientProvider.class))
            {
                obtained = get ? ip.getIngredient(i, need)
                        : ip.hasIngredient(i, need);
                if (get && obtained > 0)
                {
                    LOG.log(Level.INFO, "Obtained {0} {1} of {2} from {3}",
                            new Object[]
                            {
                                obtained,
                                ingredient.getUnits().getName(),
                                ingredient.getName(), ip.getName()
                            });
                }
                if (obtained >= need)
                {
                    break;
                }
            }
            if (obtained < need)
            {
                throw new NotEnoughIngredientException(ingredient);

            }
            return obtained;
        } catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
        return 0f;
    }

    public static int getEquipment(Class<? extends Equipment> e, final int need)
            throws NotEnoughEquipmentException
    {
        try
        {
            Equipment equipment = e.newInstance();
            int obtained = 0;
            for (EquipmentProvider ip
                    : Lookup.getDefault().lookupAll(EquipmentProvider.class))
            {
                obtained += ip.getEquipment(e, need);
                if (obtained > 0)
                {
                    LOG.log(Level.INFO, "Obtained {0} {1} from {2}",
                            new Object[]
                            {
                                obtained,
                                equipment.getName(), ip.getName()
                            });
                }
                if (obtained >= need)
                {
                    break;
                }
            }
            if (obtained < need)
            {
                throw new NotEnoughEquipmentException(equipment);
            }
            return obtained;
        } catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static void storeEquipment(Class<? extends Equipment> i, int amount)
            throws MissingStorageException
    {
        try
        {
            Equipment equip = i.newInstance();
            LOG.log(Level.FINE, "Storing: {0} of {1}",
                    new Object[]
                    {
                        amount, equip.getName()
                    });
            boolean stored = false;
            for (EquipmentProvider ip
                    : Lookup.getDefault().lookupAll(EquipmentProvider.class))
            {
                if (ip.getEmptySpace() > 0)
                {
                    //Enough space, stoare all of it.
                    ip.addEquipment(i, amount);
                    stored = true;
                    break;
                }
            }
            if (!stored)
            {
                throw new MissingStorageException("Found no storage for "
                        + equip.getName());
            }
        } catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}
