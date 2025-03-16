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

import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Creates and registers Guage metric in Prometheus for each one of the property-based metrics in RocksDB.
 * with labels = serviceName
 * and metricName(the actual name of the RocksDB metric property)
 *
 * @author hbadshah
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Component
public class IgniteRocksDBGuage extends IgniteGuage {

    /**
     * LOGGER.
     */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteRocksDBGuage.class);

    /**
     * Create Gauges for RocksDB metrics.
     */
    public void setup() {
        createGuage("rocksdb_metric", "metric_name", "svc", "node");
        LOGGER.debug("rocksdb_metric guage successfully created.");
    }
}