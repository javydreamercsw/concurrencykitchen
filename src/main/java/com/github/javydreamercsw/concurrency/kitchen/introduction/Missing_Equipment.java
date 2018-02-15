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
package com.github.javydreamercsw.concurrency.kitchen.introduction;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.AbstractScenario;
import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.Scenario;
import com.github.javydreamercsw.concurrency.Util;
import com.github.javydreamercsw.concurrency.exception.MissingStorageException;
import com.github.javydreamercsw.concurrency.recipe.Separated_Egg_Recipe;
import com.github.javydreamercsw.concurrency.staff.Cook;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Scenario.class, position = 2)
public class Missing_Equipment extends AbstractScenario
{

    private static final Logger LOG
            = Logger.getLogger(Missing_Equipment.class.getName());

    @Override
    public void cook()
    {
        //Add enough ingredients to the pantry.
        Lookup.getDefault().lookupAll(Ingredient.class).forEach(i ->
        {
            try
            {
                Util.storeIngredient(i.getClass(), 100);
            } catch (MissingStorageException ex)
            {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        );
        addRecipe(new Separated_Egg_Recipe());
        addCook(new Cook("Roberto"));
        super.cook();
    }

    @Override
    public int getChapter()
    {
        return 1;
    }
}
