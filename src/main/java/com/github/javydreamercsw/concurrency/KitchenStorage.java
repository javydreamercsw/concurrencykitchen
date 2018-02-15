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
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 * @param <T> Type of items it contains.
 */
public interface KitchenStorage<T>
{

    /**
     * How many slots it has to store stuff.
     *
     * @return amount of empty spaces.
     */
    double getEmptySpace();

    /**
     * Get provider name.
     *
     * @return provider name.
     */
    String getName();

    /**
     * How many equipment does it fit.
     *
     * @return amount of equipment it can hold.
     */
    int getcapacity();

    /**
     * Request amount of item from storage.
     *
     * @param clazz Type of item
     * @param amount Amount requested
     * @return The amount of items obtained.
     */
    double getItem(Class<? extends T> clazz, double amount);

    /**
     * Add item to this storage.
     *
     * @param clazz Type of item to store.
     * @param amount amount to store.
     */
    void addItem(Class<? extends T> clazz, double amount);

    /**
     * Checks if an amount of items is available from storage.
     *
     * @param clazz Type of item
     * @param amount Amount requested
     * @return the amount available in this storage
     */
    double hasItem(Class<? extends T> clazz, double amount);

    /**
     * Remove everything from storage.
     */
    void clean();
}
