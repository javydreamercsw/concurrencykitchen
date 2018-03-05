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
package com.github.javydreamercsw.concurrency.scenario.part2;

import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.AbstractScenario;
import com.github.javydreamercsw.concurrency.Cancellable;
import com.github.javydreamercsw.concurrency.Scenario;
import com.github.javydreamercsw.concurrency.exception.MissingChefException;

/**
 * Implements this scenario so it can be cancellable.
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Scenario.class, position = 4)
public class Cancellation_Scenario extends AbstractScenario
        implements Cancellable
{
  @Override
  public void cook() throws MissingChefException
  {
    while (true)
    {
      //Do nothing, waiting to be cancelled.
    }
  }

  @Override
  public int getBookPart()
  {
    return 2;
  }

  @Override
  public void cancel()
  {

  }
}
