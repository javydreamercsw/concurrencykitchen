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

import java.util.List;

import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.exception.MissingStorageException;
import com.github.javydreamercsw.concurrency.exception.NotEnoughEquipmentException;
import com.github.javydreamercsw.concurrency.exception.NotEnoughIngredientException;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public interface ICook extends Runnable
{

    /**
     * Add EmployeeListener
     *
     * @param listener listener to add
     */
    void addListener(EmployeeListener listener);

    /**
     * Add SupervisorListener.
     *
     * @param listener listener to add
     */
    void addListener(SupervisorListener listener);

    /**
     * Add a recipe to this cook's queue.
     *
     * @param r REcipe to add.
     */
    void addRecipe(Recipe r);

    /**
     * Analyze the needed ingredients.
     *
     * @param r Recipe to analyze.
     * @param check True if you only want to check the list and not retrieve the
     * ingredients.
     * @return List of missing recipes.
     * @throws NotEnoughIngredientException
     */
    List<Recipe> analyzeIngredients(Recipe r, boolean check) throws NotEnoughIngredientException;

    /**
     * CLeanup your station after being done working.
     *
     * @param totalTime
     */
    void cleanup(long totalTime);

    /**
     * Cook a recipe.
     *
     * @param recipe Recipe to cook.
     * @return Time it took to complete.
     * @throws MissingStorageException
     * @throws InterruptedException
     * @throws NotEnoughIngredientException
     * @throws NotEnoughEquipmentException
     */
    long cook(Recipe recipe) throws MissingStorageException,
            InterruptedException, NotEnoughIngredientException,
            NotEnoughEquipmentException;

    /**
     * @return the name
     */
    String getCookName();

    /**
     * Notify supervisors of an exception.
     *
     * @param ex Exception to notify.
     * @return true if it was notified to someone.
     */
    boolean notifyExceptionToSupervisors(Exception ex);

    /**
     * Notify supervisor of the need of an ingredient.
     *
     * @param i Ingredient needed.
     * @param need Amount needed.
     * @throws NotEnoughIngredientException
     */
    void notifyNeedToSupervisors(Class<? extends Ingredient> i, float need)
            throws NotEnoughIngredientException;

    /**
     * Flag that determines if I should keep on cooking.
     *
     * @return if cooking should continue.
     */
    boolean shouldCook();

    /**
     * Say something out loud.
     *
     * @param s String to "say".
     */
    void speakout(String s);

    /**
     * Don't start a new recipe.
     */
    void stopCooking();
}
