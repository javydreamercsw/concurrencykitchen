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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.ProcessedIngredient;
import com.github.javydreamercsw.concurrency.Recipe;
import com.github.javydreamercsw.concurrency.Util;
import com.github.javydreamercsw.concurrency.exception.NotEnoughIngredientException;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
//@ServiceProvider(service = SousChef.class)
public class Junior_Sous_Chef extends Rookie_Sous_Chef
{

  private final LinkedBlockingDeque<Recipe> recipes
          = new LinkedBlockingDeque<>();
  private final ConcurrentLinkedQueue<ICook> busyChefs
          = new ConcurrentLinkedQueue<>();
  private final ConcurrentLinkedQueue<ICook> idleChefs
          = new ConcurrentLinkedQueue<>();

  private static final Logger LOG
          = Logger.getLogger(Junior_Sous_Chef.class.getName());

  public Junior_Sous_Chef()
  {
    super("Junior");
  }

  /**
   * Supervisee needs something to finish the recipe.
   *
   * @param i ingredient needed.
   * @param need amount needed.
   */
  @Override
  public synchronized void notifyNeed(Class<? extends Ingredient> i,
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
  public void addStaff(ICook cook)
  {
    idleChefs.add(cook);
    cook.addListener((EmployeeListener) this);
  }

  @Override
  public void cook()
  {
    Recipe next = recipes.peek();
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
        recipes.addFirst(m);
      }
      while (!recipes.isEmpty())
      {
        assignToCook(recipes.removeFirst());
      }
    }
  }

  private synchronized void assignToCook(Recipe r)
  {
    if (!idleChefs.isEmpty())
    {
      //Assign a cook
      ICook chef = idleChefs.remove();
      busyChefs.add(chef);
      chef.addRecipe(r);
      chef.run();
    }
    else
    {
      recipes.addFirst(r);
    }
  }

  @Override
  public void addRecipe(Recipe r)
  {
    recipes.add(r);
  }

  @Override
  public void notifyException(Exception ex)
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
}
