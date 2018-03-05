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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.Lookup;

import com.github.javydreamercsw.concurrency.exception.MissingChefException;
import com.github.javydreamercsw.concurrency.exception.MissingStorageException;
import com.github.javydreamercsw.concurrency.staff.Cook;
import com.github.javydreamercsw.concurrency.staff.EmployeeListener;
import com.github.javydreamercsw.concurrency.staff.Rookie_Sous_Chef;
import com.github.javydreamercsw.concurrency.staff.SousChef;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public abstract class AbstractScenario implements Scenario, EmployeeListener
{

  private static final Logger LOG
          = Logger.getLogger(AbstractScenario.class.getName());
  private SousChef chef;
  private final ConcurrentLinkedQueue<Recipe> recipes
          = new ConcurrentLinkedQueue<>();
  private final ConcurrentLinkedQueue<ScenarioListener> listeners
          = new ConcurrentLinkedQueue<>();

  @Override
  public final void addRecipe(Recipe r)
  {
    recipes.add(r);
  }

  @Override
  public void cook() throws MissingChefException
  {
    getChef().addListener(this);
    while (!recipes.isEmpty())
    {
      getChef().addRecipe(recipes.remove());
    }
    getChef().cook();
  }

  @Override
  public final synchronized void addCook(Cook cook) throws MissingChefException
  {
    if (chef == null)
    {
      //Set a default chef
      setChef(new Rookie_Sous_Chef());
    }
    getChef().addStaff(cook);
  }

  @Override
  public void addListener(ScenarioListener listener)
  {
    listeners.add(listener);
  }

  @Override
  public void taskDone(Cook c, long time)
  {
    listeners.forEach(l ->
    {
      l.scenarioDone();
    });
  }

  @Override
  public String getName()
  {
    return getClass().getSimpleName().replaceAll("_", " ");
  }

  @Override
  public final SousChef getChef() throws MissingChefException
  {
    if (chef == null)
    {
      throw new MissingChefException();
    }
    return chef;
  }

  /**
   * @param chef the chef to set
   */
  @Override
  public final void setChef(SousChef chef)
  {
    this.chef = chef;
  }

  /**
   * Fill the storages with the amount of ingredients for each.
   *
   * @param amount amount to store of each
   */
  protected void loadIngredients(int amount)
  {
    //Add enough ingredients to the pantry.
    Lookup.getDefault().lookupAll(Ingredient.class).forEach(i ->
    {
      try
      {
        Util.storeIngredient(i.getClass(), amount);
      }
      catch (MissingStorageException ex)
      {
        LOG.log(Level.SEVERE, null, ex);
      }
    }
    );
  }

  /**
   * Fill the storages with the amount of equipment for each.
   *
   * @param amount amount to store of each
   */
  protected void loadEquipment(int amount)
  {
    Lookup.getDefault().lookupAll(Equipment.class).forEach(i ->
    {
      try
      {
        Util.storeEquipment(i.getClass(), amount);
      }
      catch (MissingStorageException ex)
      {
        LOG.log(Level.SEVERE, null, ex);
      }
    });
  }

  @Override
  public String toString()
  {
    return "Scenario: " + getName() + " (Part " + getBookPart() + ")";
  }
}
