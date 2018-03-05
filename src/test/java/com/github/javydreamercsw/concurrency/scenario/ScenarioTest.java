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
package com.github.javydreamercsw.concurrency.scenario;

import static org.testng.Assert.assertTrue;

import java.util.Collection;

import org.openide.util.Lookup;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.github.javydreamercsw.concurrency.Cancellable;
import com.github.javydreamercsw.concurrency.Scenario;
import com.github.javydreamercsw.concurrency.exception.MissingChefException;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public class ScenarioTest
{
  /**
   * Test of cook method, of class Missing_Equipment.
   *
   * @param s Scenario to test
   * @throws
   * com.github.javydreamercsw.concurrency.exception.MissingChefException
   */
  @Test(dataProvider = "scenarios", timeOut = 10 * 1000L)
  public void testCook(Scenario s) throws MissingChefException
  {
    System.out.println("Testing scenario: " + s);
    assertTrue(s.getBookPart() > 0);
    s.cook();
    if (s instanceof Cancellable)
    {
      ((Cancellable) s).cancel();
    }
  }

  @DataProvider()
  public Object[][] scenarios()
  {
    Collection<? extends Scenario> scenarios
            = Lookup.getDefault().lookupAll(Scenario.class);
    Object[][] result = new Object[scenarios.size()][];
    int i = 0;
    for (Scenario s : scenarios)
    {
      result[i] = new Object[]
      {
        s
      };
      i++;
    }
    return result;
  }
}
