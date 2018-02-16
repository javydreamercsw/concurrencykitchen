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

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
public abstract class SousChef extends Cook implements EmployeeListener, SupervisorListener, ICook
{

    /**
     * Variable to store elapsed time.
     */
    protected long totalTime = 0;

    public SousChef(String name)
    {
        super(name);
    }

    /**
     * Add a cook to your staff with you as supervisor.
     *
     * @param cook Cook to add.
     */
    public abstract void addStaff(ICook cook);

    /**
     * Manage your staff to prepare the dishes you have to do.
     */
    public abstract void cook();

    public String getLevel()
    {
        return getClass().getSimpleName().replaceAll("_", " ");
    }

    @Override
    public String toString()
    {
        return getLevel() + ": " + getCookName();
    }
}
