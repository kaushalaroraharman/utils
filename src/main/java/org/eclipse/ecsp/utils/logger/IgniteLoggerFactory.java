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

package org.eclipse.ecsp.utils.logger;

/**
 * Factory class which gives a logger instance for the requested type.
 *
 * <p>This class provides a static method to obtain an instance of {@link IgniteLogger}
 * for a specified class type. It uses the {@link IgniteLoggerImpl} to create the logger instance.</p>
 *
 * @since 1.0
 * @version 1.0
 *
 */
public class IgniteLoggerFactory {

    /**
     * Private constructor to prevent instantiation.
     */
    private IgniteLoggerFactory() {
    }

    /**
     * Returns an instance of {@link IgniteLogger} for the specified class type.
     *
     * @param <T> the type of the class for which the logger is requested
     * @param clazz the class for which the logger is requested
     * @return an instance of {@link IgniteLogger} for the specified class type
     */
    public static <T> IgniteLogger getLogger(Class<T> clazz) {
        return IgniteLoggerImpl.getIgniteLoggerInstance(clazz);
    }
}
