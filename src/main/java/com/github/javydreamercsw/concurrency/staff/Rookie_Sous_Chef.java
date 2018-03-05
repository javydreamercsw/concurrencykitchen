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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.ProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.Util;
import com.github.javydreamercsw.concurrency.exception.NotEnoughIngredientException;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = SousChef.class)
public class Rookie_Sous_Chef extends SousChef
{

  private final List<Recipe> recipes = new ArrayList<>();
  private final List<ICook> busyChefs = new ArrayList<>();
  private final List<ICook> idleChefs = new ArrayList<>();
  private static final Logger LOG
          = Logger.getLogger(Rookie_Sous_Chef.class.getName());

  public Rookie_Sous_Chef(String name)
  {
    super(name);
  }

  public Rookie_Sous_Chef()
  {
    super();
  }

  @Override
  public void addStaff(ICook cook)
  {
    idleChefs.add(cook);
    cook.addListener((EmployeeListener) this);
  }

  /**
   * Supervisee needs something to finish the recipe.
   *
   * @param i ingredient needed.
   * @param need amount needed.
   */
  @Override
  public synchronized void notifyNeedToSupervisors(Class<? extends Ingredient> i,
          float need)
  {
    try
    {
      if (i.isAssignableFrom(ProcessedIngredient.class))
      {
        ProcessedIngredient pi = (ProcessedIngredient) i.newInstance();
        addRecipe(pi.getRecipe());
      }
    }
    catch (InstantiationException | IllegalAccessException ex)
    {
      LOG.log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void cook()
  {
    if (!recipes.isEmpty())
    {
      Recipe next = recipes.get(0);
      if (next != null)
      {
        //Check if there are other recipes that needs to be done before
        List<Recipe> missing = new ArrayList<>();
        try
        {
          missing.addAll(analyzeIngredients(next, true));
        }
        catch (NotEnoughIngredientException ex)
        {
          speakout("Not enough ingredients!");
        }
        while (!missing.isEmpty())
        {
          //Insert them prior the real one.
          Recipe m = missing.remove(0);
          speakout("Need to prepare: " + m.getName());
          recipes.add(0, m);
        }
        while (!recipes.isEmpty())
        {
          assignToCook(recipes.remove(0));
        }
      }
    }
  }

  private synchronized void assignToCook(Recipe r)
  {
    if (!idleChefs.isEmpty())
    {
      //Assign a cook
      ICook chef = idleChefs.get(0);
      busyChefs.add(chef);
      chef.addRecipe(r);
      chef.run();
    }
    else
    {
      recipes.add(0, r);
    }
  }

  @Override
  public void addRecipe(Recipe r)
  {
    recipes.add(r);
  }

  @Override
  public boolean notifyExceptionToSupervisors(Exception ex)
  {
    //Got an exception, stop execution.
    LOG.log(Level.SEVERE, "Unable to prepare the recipe(s)", ex);
    //Stop all cooks
    speakout("Stopping the kitchen...");
    busyChefs.forEach(cook ->
    {
      cook.stopCooking();
    });
    speakout("Done!");
    return true;
  }

  @Override
  public void taskDone(Cook c, long time)
  {
    speakout(c.getCookName() + " is done with his task.");
    totalTime += time;
    busyChefs.remove(c);
    //Create a new one with the same name
    addStaff(new Cook(c.getCookName()));
    cook();
    if (busyChefs.isEmpty())
    {
      speakout("Total time elapsed: "
              + Util.getTimeReadable(totalTime));
      cleanup(totalTime);
    }
  }

  @Override
  public void notifyNeed(Class<? extends Ingredient> i, float need)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void notifyException(Exception ex)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
