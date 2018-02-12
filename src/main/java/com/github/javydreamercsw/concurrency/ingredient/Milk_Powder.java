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
package com.github.javydreamercsw.concurrency.ingredient;

import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.AbstractIngredient;
import com.github.javydreamercsw.concurrency.Ingredient;
import com.github.javydreamercsw.concurrency.UNIT;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = Ingredient.class)
public class Milk_Powder extends AbstractIngredient
{

    @Override
    public UNIT getUnits()
    {
        return UNIT.OUNCE;
    }
}
