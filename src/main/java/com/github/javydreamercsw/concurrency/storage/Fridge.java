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
package com.github.javydreamercsw.concurrency.storage;

import org.openide.util.lookup.ServiceProvider;

import com.github.javydreamercsw.concurrency.AbstractIngredientProvider;
import com.github.javydreamercsw.concurrency.IngredientProvider;

/**
 *
 * @author Javier Ortiz Bultron <javierortiz@pingidentity.com>
 */
@ServiceProvider(service = IngredientProvider.class)
public class Fridge extends AbstractIngredientProvider
{

    @Override
    public boolean isRefrigerated()
    {
        return true;
    }
}
