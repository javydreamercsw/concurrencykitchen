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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openide.util.Lookup;

import com.github.javydreamercsw.concurrency.exception.MissingChefException;
import com.github.javydreamercsw.concurrency.staff.SousChef;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class Main
{

  private final static Logger LOG = Logger.getLogger(Main.class.getName());
  private boolean ready = false;
  private final static Collection<? extends SousChef> chefs
          = Lookup.getDefault().lookupAll(SousChef.class);

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    new Menu().start();
  }

  /**
   * @return the ready
   */
  public synchronized boolean isReady()
  {
    return ready;
  }

  /**
   * @param ready the ready to set
   */
  public synchronized void setReady(boolean ready)
  {
    this.ready = ready;
  }

  private static class Menu extends Thread implements ScenarioListener
  {

    @Override
    public void run()
    {
      try
      {
        showMenu();
      }
      catch (Error ex)
      {
        LOG.log(Level.SEVERE, null, ex);
      }
    }

    @Override
    public synchronized void scenarioDone()
    {
      showMenu();
    }

    private void showMenu()
    {
      TreeMap<Integer, ArrayList<Scenario>> options = new TreeMap<>();
      Lookup.getDefault().lookupAll(Scenario.class).forEach(s ->
      {
        if (!options.containsKey(s.getBookPart()))
        {
          options.put(s.getBookPart(), new ArrayList<>());
        }
        options.get(s.getBookPart()).add(s);
      });
      System.out.println("Available Chapters:");
      options.entrySet().forEach((entry) ->
      {
        System.out.println("\t" + entry.getKey() + ") Chapter "
                + entry.getKey() + ": "
                + entry.getValue().size() + " scenario(s)");
      });

      System.out.println("\tq) quit");

      Scanner scanner = new Scanner(System.in);
      String selection = scanner.next().trim().toLowerCase();
      if (selection.equals("q"))
      {
        System.out.println("Exiting");
        System.exit(0);
      }
      else
      {
        int count = 1;
        System.out.println("Select a scenario:");
        List<Scenario> group = options.get(Integer.valueOf(selection));
        if (group == null)
        {//Wrong selection
          showMenu();
        }
        else
        {
          for (Scenario s : group)
          {
            System.out.println("\t" + count + ") "
                    + s.getName());
            count++;
          }
          System.out.println("\tb) back");
          System.out.println("\tq) quit");
          String subselection = scanner.next().trim().toLowerCase();
          switch (subselection)
          {
            case "b":
              showMenu();
              break;
            case "q":
              System.out.println("Exiting");
              System.exit(0);
            default:
              Integer option = Integer.valueOf(subselection);
              //Wipe out storage
              Util.cleanStorage();
              if (option - 1 >= group.size())
              {
                showMenu();
              }
              else
              {
                Scenario s = group.get(option - 1);
                s.addListener(this);
                int scCount = 1;
                if (chefs.size() > 1)
                {
                  System.out.println("Select a sous chef:");
                  for (SousChef sc : chefs)
                  {
                    System.out.println("\t" + scCount + ") "
                            + sc.toString());
                    scCount++;
                  }
                  System.out.println("\tb) back");
                  System.out.println("\tq) quit");
                  String chefselection = scanner.next().trim().toLowerCase();
                  switch (chefselection)
                  {
                    case "b":
                      showMenu();
                      break;
                    case "q":
                      System.out.println("Exiting");
                      System.exit(0);
                    default:
                  }
                  Integer coption = Integer.valueOf(chefselection);
                  if (coption > chefs.size())
                  {
                    showMenu();
                  }
                  else
                  {
                    s.setChef((SousChef) chefs.toArray()[coption - 1]);

                  }
                }
                else
                {
                  s.setChef((SousChef) chefs.toArray()[0]);
                }
                try
                {
                  startCooking(s);
                }
                catch (MissingChefException ex)
                {
                  LOG.log(Level.SEVERE, null, ex);
                  showMenu();
                }
                catch (Exception ex)
                {
                  LOG.log(Level.SEVERE, null, ex);
                  showMenu();
                }
              }
              break;
          }
        }
      }
    }

    private void startCooking(Scenario s) throws MissingChefException, Exception
    {
      Thread t = new Thread(() ->
      {
        try
        {
          s.cook();
        }
        catch (MissingChefException ex)
        {
          LOG.log(Level.SEVERE, null, ex);
        }
      });
      t.start();
      Scanner scanner = new Scanner(System.in);
      if (s instanceof Cancellable)
      {
        System.out.println("Press any key to cancel this process!");
        scanner.nextLine();
        ((Cancellable) s).cancel();
        if (t.isAlive())
        {
          throw new Exception(s.getChef() + " didn't stop working!");
        }
      }
    }
  }
}
