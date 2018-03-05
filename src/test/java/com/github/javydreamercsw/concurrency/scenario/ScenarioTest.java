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

import org.openide.util.Lookup;

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
   * @throws
   * com.github.javydreamercsw.concurrency.exception.MissingChefException
   */
  @org.testng.annotations.Test
  public void testCook() throws MissingChefException
  {
    System.out.println("cook");
    for (Scenario s : Lookup.getDefault().lookupAll(Scenario.class))
    {
      System.out.println("Testing scenario: " + s);
      assertTrue(s.getBookPart() > 0);
      s.cook();
    }
  }
}
