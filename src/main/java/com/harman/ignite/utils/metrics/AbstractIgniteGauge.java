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
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

/**
 * Abstract class for creating prometheus gauge metric.
 */
public abstract class AbstractIgniteGauge {

    /**
     * Logger instance for logging.
     */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteGauge.class);

    /**
     * Prometheus gauge instance.
     */
    private Gauge gauge;

    /**
     * Flag to check if the gauge is initialized.
     */
    private boolean isInitialized;

    /**
     * Creates a Prometheus gauge with the specified name, help description, and labels.
     *
     * @param name the name of the gauge
     * @param help the help description of the gauge
     * @param labels the labels for the gauge
     */
    protected void createGauge(String name, String help, String... labels) {
        if (null == gauge) {
            synchronized (this) {
                gauge = Gauge.build(name, name)
                        .labelNames(labels)
                        .help(help)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite gauge with name: {} and labels: {}", name, labels);
            }
        }

        if (null != gauge) {
            isInitialized = true;
            LOGGER.info("Created Prometheus gauge metric with name: {}", name);
        } else {
            LOGGER.warn("Error creating Prometheus gauge metric with name: {}", name);
        }
    }

    /**
     * Increments the gauge by 1.
     *
     * @param labelValues the label values
     */
    public void inc(String... labelValues) {
        if (isInitialized) {
            synchronized (gauge) {
                gauge.labels(labelValues).inc();
            }
        }
    }

    /**
     * Increments the gauge by the given amount.
     *
     * @param value the value to increment the gauge by
     * @param labelValues the label values
     */
    public void inc(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (gauge) {
                gauge.labels(labelValues).inc(value);
            }
        }
    }

    /**
     * Decrements the gauge by 1.
     *
     * @param labelValues the label values
     */
    public void dec(String... labelValues) {
        if (isInitialized) {
            synchronized (gauge) {
                gauge.labels(labelValues).dec();
            }
        }
    }

    /**
     * Decrements the gauge by the given amount.
     *
     * @param value the value to decrement the gauge by
     * @param labelValues the label values
     */
    public void dec(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (gauge) {
                gauge.labels(labelValues).dec(value);
            }
        }
    }

    /**
     * Gets the value of the gauge.
     *
     * @param labelValues the label values
     * @return the value of the gauge
     */
    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
            synchronized (gauge) {
                val = gauge.labels(labelValues).get();
            }
        }
        return val;
    }

    /**
     * Sets the gauge to the given value.
     *
     * @param value the value to set the gauge to
     * @param labelValues the label values
     */
    public void set(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (gauge) {
                gauge.labels(labelValues).set(value);
            }
        }
    }

    /**
     * Removes the gauge from the registry it was registered with.
     */
    public void clear() {
        if (isInitialized) {
            synchronized (gauge) {
                gauge.clear();
            }
        }
    }

    /**
     * Gets the gauge instance.
     *
     * @return the gauge instance
     */
    Gauge getGauge() {
        return this.gauge;
    }

    /**
     * Checks if the gauge is initialized.
     *
     * @return true if the gauge is initialized, false otherwise
     */
    boolean getIsInitialized() {
        return this.isInitialized;
    }
}