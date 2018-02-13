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

/**
 * This interface are for object that provide equipment like a a closet or
 * cabinet.
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public interface EquipmentProvider
{

    /**
     * How many equipment does it fit.
     *
     * @return amount of equipment it can hold.
     */
    int getcapacity();

    /**
     * How many slots it has to store stuff.
     *
     * @return amount of empty spaces.
     */
    int getEmptySpace();

    /**
     * Get provider name.
     *
     * @return provider name.
     */
    String getName();

    /**
     * Request amount of equipment from provider.
     *
     * @param clazz Type of equipment
     * @param amount Amount requested
     * @return The amount of equipment obtained.
     */
    int getEquipment(Class<? extends Equipment> clazz, int amount);

    /**
     * Add equipment to this storage.
     *
     * @param clazz Type of equipment to store.
     * @param amount amount to store.
     */
    void addEquipment(Class<? extends Equipment> clazz, int amount);
}
