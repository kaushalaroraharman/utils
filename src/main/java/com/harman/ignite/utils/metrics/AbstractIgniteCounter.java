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

package com.harman.ignite.utils.metrics;

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import io.prometheus.client.Counter;

/**
 * Abstract class for creating Prometheus counter metric.
 */
public abstract class AbstractIgniteCounter {

    /**
     * Logger instance for logging.
     */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteCounter.class);

    /**
     * Prometheus counter instance.
     */
    private Counter counter;

    /**
     * Flag to check if the counter is initialized.
     */
    private boolean isInitialized;

    /**
     * Creates a Prometheus counter with the specified name, help description, and labels.
     *
     * @param name the name of the counter
     * @param help the help description of the counter
     * @param labels the labels for the counter
     */
    protected void createCounter(String name, String help, String... labels) {
        if (null == counter) {
            synchronized (this) {
                counter = Counter.build().name(name).help(help).labelNames(labels).register();
            }
        }

        if (null != counter) {
            isInitialized = true;
            LOGGER.info("Created Prometheus counter metric with name: {}", name);
        } else {
            LOGGER.warn("Error creating Prometheus counter metric with name: {}", name);
        }
    }

    /**
     * Increments the counter by 1.
     *
     * @param labelValues the label values
     */
    public void inc(String... labelValues) {
        if (isInitialized) {
            synchronized (counter) {
                counter.labels(labelValues).inc();
            }
        }
    }

    /**
     * Increments the counter by the given amount.
     *
     * @param value the value to increment the counter by
     * @param label the label values
     */
    public void inc(double value, String... label) {
        if (isInitialized) {
            synchronized (counter) {
                counter.labels(label).inc(value);
            }
        }
    }

    /**
     * Gets the value of the counter.
     *
     * @param labelValues the label values
     * @return the value of the counter
     */
    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
            synchronized (counter) {
                val = counter.labels(labelValues).get();
            }
        }
        return val;
    }

    /**
     * Removes the counter from the registry it was registered with.
     */
    public void clear() {
        if (isInitialized) {
            synchronized (counter) {
                counter.clear();
            }
        }
    }

    /**
     * Gets the counter instance.
     *
     * @return the counter instance
     */
    public Counter getCounter() {
        return this.counter;
    }

    /**
     * Checks if the counter is initialized.
     *
     * @return true if the counter is initialized, false otherwise
     */
    public boolean isInitialized() {
        return isInitialized;
    }
}