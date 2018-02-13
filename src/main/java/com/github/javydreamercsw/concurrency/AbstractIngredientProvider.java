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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class AbstractIngredientProvider implements IngredientProvider
{

    private final Map<Class<? extends Ingredient>, Float> storage
            = new HashMap<>();
    private static final Logger LOG
            = Logger.getLogger(AbstractIngredientProvider.class.getName());

    @Override
    public String getName()
    {
        return getClass().getSimpleName().replaceAll("_", " ");
    }

    @Override
    public synchronized float getIngredient(Class<? extends Ingredient> clazz,
            float amount)
    {
        if (storage.containsKey(clazz) && amount <= storage.get(clazz))
        {
            storage.put(clazz, storage.get(clazz) - amount);
            return amount;
        } else if (storage.containsKey(clazz))
        {
            return storage.put(clazz, 0f);
        } else
        {
            return 0;
        }
    }

    @Override
    public synchronized float hasIngredient(Class<? extends Ingredient> clazz,
            float amount)
    {
        if (storage.containsKey(clazz) && amount <= storage.get(clazz))
        {
            return amount;
        } else
        {
            return 0;
        }
    }

    /**
     * @return the storage
     */
    @Override
    public Map<Class<? extends Ingredient>, Float> getStorage()
    {
        return storage;
    }

    @Override
    public void addIngredient(Class<? extends Ingredient> i, float amount)
    {
        if (storage.containsKey(i))
        {
            storage.put(i, storage.get(i) + amount);
        } else
        {
            storage.put(i, amount);
        }
    }
}
