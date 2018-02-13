/*
 * Copyright 2018 Javier A. Ortiz Bultron <javierortiz@pingidentity.com>.
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEquipmentProvider implements EquipmentProvider
{

    private final Map<Class<? extends Equipment>, Integer> storage
            = new ConcurrentHashMap<>();

    public AbstractEquipmentProvider()
    {
    }

    @Override
    public String getName()
    {
        return getClass().getSimpleName().replaceAll("_", " ");
    }

    @Override
    public synchronized int getEquipment(Class<? extends Equipment> clazz,
            final int amount)
    {
        if (storage.containsKey(clazz))
        {
            if (amount > storage.get(clazz))
            {
                return storage.put(clazz, 0);
            } else
            {
                return storage.put(clazz, storage.get(clazz) - amount);
            }
        } else
        {
            return 0;
        }
    }

    @Override
    public synchronized final void addEquipment(Class<? extends Equipment> clazz,
            int amount)
    {
        if (storage.containsKey(clazz))
        {
            storage.put(clazz, amount + storage.get(clazz));
        } else
        {
            storage.put(clazz, amount);
        }
    }

    @Override
    public synchronized int getEmptySpace()
    {
        return getcapacity()
                - storage.values().stream().mapToInt(Number::intValue).sum();
    }
}
