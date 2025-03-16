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
 * A class representing a generic Ignite histogram.
 * This class extends the AbstractIgniteHistogram and provides
 * functionality to create a histogram with specified parameters.
 */
public class GenericIgniteHistogram extends AbstractIgniteHistogram {

    /**
     * Constructor to create histogram.
     *
     * @param name       Name of the histogram.
     * @param help       Help message for the histogram.
     * @param buckets    Buckets for the histogram.
     * @param labelNames Label names for the histogram.
     */
    public GenericIgniteHistogram(String name, String help, double[] buckets, String... labelNames) {
        createHistogram(name, help, buckets, labelNames);
    }

}
