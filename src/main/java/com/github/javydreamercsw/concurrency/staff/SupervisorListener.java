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
package com.github.javydreamercsw.concurrency.staff;

import com.github.javydreamercsw.concurrency.Ingredient;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public interface SupervisorListener
{

    /**
     * Supervisee needs something to finish the recipe.
     *
     * @param i ingredient needed.
     * @param need amount needed.
     */
    public void notifyNeed(Class<? extends Ingredient> i,
            float need);

    /**
     * Notify listener of exception.
     *
     * @param ex Exception to notify.
     */
    public void notifyException(Exception ex);
}
