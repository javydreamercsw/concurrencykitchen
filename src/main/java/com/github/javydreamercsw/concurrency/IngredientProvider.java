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

import java.util.List;

/**
 * This interface are for object that provide ingredients like a pantry or
 * fridge.
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public interface IngredientProvider
{

    /**
     * Get provider name.
     *
     * @return provider name.
     */
    String getName();

    /**
     * Request amount of ingredients from provider.
     *
     * @param clazz Type of ingredient
     * @param amount Amount requested
     * @return A list of the requested ingredients. Might be less than requested
     * if there's no more available.
     */
    List<Ingredient> getIngredient(Class<? extends Ingredient> clazz, int amount);
}
