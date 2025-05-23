/*
 * *******************************************************************************
 *
 *  Copyright (c) 2023-24 Harman International
 *
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *
 *  you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *       
 *
 *  Unless required by applicable law or agreed to in writing, software
 *
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *
 *  limitations under the License.
 *
 *
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  *******************************************************************************
 */

package org.eclipse.ecsp.utils.metrics;

/**
 * The default Ignite Counter implementation.
 *
 * @author avadakkootko
 */
public class GenericIgniteCounter extends AbstractIgniteCounter {

    /**
     * Constructor to create a counter with the given name and help string.
     *
     * @param name   The name of the counter.
     * @param help   The help string of the counter.
     * @param labels The labels of the counter.
     */
    public GenericIgniteCounter(String name, String help, String... labels) {
        createCounter(name, help, labels);
    }

}
