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
 * Abstract class for creating and managing Prometheus gauge metrics in Ignite.
 *
 * <p>This class provides methods to create, increment, decrement, get, set, and clear
 * Prometheus gauge metrics. It ensures thread-safe operations on the gauge metrics.</p>
 *
 * @since 1.0
 * @version 1.0
 */
public abstract class AbstractIgniteGauge {

    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(AbstractIgniteGauge.class);

    private Gauge guage;

    private boolean isInitialized;

    /**
     * Creates a Prometheus gauge metric with the specified name, help description, and labels.
     *
     * @param name the name of the gauge metric
     * @param help the help description of the gauge metric
     * @param labels the labels for the gauge metric
     */
    protected void createGauge(String name, String help, String... labels) {
        if (null == guage) {
            synchronized (this) {
                guage = Gauge.build(name, name)
                        .labelNames(labels)
                        .help(help)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite guage with name : {} and labels {}", name, labels);
            }
        }

        if (null != guage) {
            isInitialized = true;
            LOGGER.info("Created prometheus gauge metric with name : {}", name);
        } else {
            LOGGER.warn("Error creating prometheus guage metric with name : {}", name);
        }
    }

    /**
     * Increments the gauge metric by 1 for the specified label values.
     *
     * @param labelValues the label values for the gauge metric
     */
    public void inc(String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).inc();
            }
        }
    }

    /**
     * Increments the gauge metric by the specified value for the specified label values.
     *
     * @param value the value to increment the gauge metric by
     * @param labelValues the label values for the gauge metric
     */
    public void inc(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).inc(value);
            }
        }
    }

    /**
     * Decrements the gauge metric by 1 for the specified label values.
     *
     * @param labelValues the label values for the gauge metric
     */
    public void dec(String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).dec();
            }
        }
    }

    /**
     * Decrements the gauge metric by the specified value for the specified label values.
     *
     * @param value the value to decrement the gauge metric by
     * @param labelValues the label values for the gauge metric
     */
    public void dec(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).dec(value);
            }
        }
    }

    /**
     * Returns the current value of the gauge metric for the specified label values.
     *
     * @param labelValues the label values for the gauge metric
     * @return the current value of the gauge metric
     */
    public double get(String... labelValues) {
        double val = 0;
        if (isInitialized) {
            synchronized (guage) {
                val = guage.labels(labelValues).get();
            }
        }
        return val;
    }

    /**
     * Sets the gauge metric to the specified value for the specified label values.
     *
     * @param value the value to set the gauge metric to
     * @param labelValues the label values for the gauge metric
     */
    public void set(double value, String... labelValues) {
        if (isInitialized) {
            synchronized (guage) {
                guage.labels(labelValues).set(value);
            }
        }
    }

    /**
     * Clears the gauge metric.
     */
    public void clear() {
        if (isInitialized) {
            synchronized (guage) {
                guage.clear();
            }
        }
    }

    /**
     * Returns the gauge metric instance.
     *
     * @return the gauge metric instance
     */
    Gauge getGuage() {
        return this.guage;
    }

    /**
     * Returns whether the gauge metric is initialized.
     *
     * @return true if the gauge metric is initialized, false otherwise
     */
    boolean getIsInitialized() {
        return this.isInitialized;
    }
}