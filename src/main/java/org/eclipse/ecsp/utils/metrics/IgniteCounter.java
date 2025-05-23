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

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;

import java.util.Objects;

/**
 * Wrapper around Prometheus Counter.
 *
 * @author sanketadhikari
 */
public abstract class IgniteCounter {


    /**
     * Logger instance for logging.
     */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteCounter.class);
    

    /**
     * Prometheus counter instance.
     */
    private  Counter counter;
    
    /**
     * Increment the counter metric's value by 1 for given labels.
     *
     * @param labelValues Values of the labels
     */
    public void inc(String... labelValues) {
        Objects.requireNonNull(counter, "IgniteCounter is not initialized");
        synchronized (this.counter) {
            counter.labels(labelValues).inc();
        }
    }

    /**
     * Get the metric's value for given labels.
     *
     * @param labelValues label values
     *
     * @return double
     */
    public double get(String... labelValues) {
        double value = 0;
        Objects.requireNonNull(counter, "IgniteCounter is not initialized");
        value = counter.labels(labelValues).get();
        return value;
    }

    /**
     * Creates a Prometheus counter with the specified name and labels.
     *
     * @param name the name of the counter
     * @param labels the labels for the counter
     */
    protected void createCounter(String name, String... labels) {
        if (null == counter) {
            synchronized (this) {
                counter = Counter.build(name, name).labelNames(labels).register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite counter with name : {} and labels {}", name, labels);
            }
        } else {
            LOGGER.warn("Ignite counter with name : {} and labels {}, already created", name, labels);
        }
    }

    /**
     * This method is a getter for counter.
     *
     * @return Counter
     */
    Counter getCounter() { 
        return this.counter;
    }
}