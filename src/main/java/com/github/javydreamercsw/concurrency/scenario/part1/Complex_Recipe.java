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
package com.github.javydreamercsw.concurrency.scenario.part1;

import java.util.logging.Logger;

import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.AbstractScenario;
import com.github.javydreamercsw.concurrency.Scenario;
import com.github.javydreamercsw.concurrency.exception.MissingChefException;
import com.github.javydreamercsw.concurrency.recipe.Cake_Recipe;
import com.github.javydreamercsw.concurrency.staff.Cook;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Scenario.class, position = 3)
public class Complex_Recipe extends AbstractScenario
{

  private static final Logger LOG
          = Logger.getLogger(Complex_Recipe.class.getName());

  @Override
  public void cook() throws MissingChefException
  {
    //Add enough ingredients to the pantry.
    loadIngredients(100);

    //Add enough equipment to the pantry.
    loadEquipment(1);
    addRecipe(new Cake_Recipe());
    addCook(new Cook());
    super.cook();
  }

  @Override
  public int getBookPart()
  {
    return 1;
  }
}
