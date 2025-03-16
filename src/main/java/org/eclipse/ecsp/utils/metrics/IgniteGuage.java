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
import io.prometheus.client.Gauge;
import org.eclipse.ecsp.utils.logger.IgniteLogger;
import org.eclipse.ecsp.utils.logger.IgniteLoggerFactory;

import java.util.Objects;

/**
 * Wrapper around Prometheus Guage.
 *
 * @author avadakkootko
 */
public abstract class IgniteGuage {

    /**
     * LOGGER.
     */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(IgniteGuage.class);

    /**
     * igniteGuageMetric.
     */
    private Gauge igniteGuageMetric;

    /**
     * Set the metric's value.

     * @param value The value to set.
     * @param labelValues Label values to set the value for.
     */
    public void set(double value, String... labelValues) {
        Objects.requireNonNull(igniteGuageMetric, "IgniteGuage is not initialized");
        synchronized (this.igniteGuageMetric) {
            igniteGuageMetric.labels(labelValues).set(value);
        }
    }

    /**
     * Get the value of the guage.
     *
     * @param labelValues label values
     * @return The value of the guage.
     */
    public double get(String... labelValues) {
        double value = 0;
        Objects.requireNonNull(igniteGuageMetric, "IgniteGuage is not initialized");
        value = igniteGuageMetric.labels(labelValues).get();
        return value;
    }

    /**
     * Create a guage metric.
     *
     * @param name   The name of the guage.
     * @param labels The labels for the guage.
     */
    protected void createGuage(String name, String... labels) {
        if (null == igniteGuageMetric) {
            synchronized (this) {
                igniteGuageMetric = Gauge.build(name, name)
                        .labelNames(labels)
                        .register(CollectorRegistry.defaultRegistry);
                LOGGER.info("Created ignite guage with name : {} and labels {}", name, labels);
            }
        } else {
            LOGGER.warn("Ignite guage with name : {} and labels {}, already created", name, labels);
        }
    }

    /**
     * This method is a getter for igniteGuageMetric.
     *
     * @return Gauge
     */
    Gauge getIgniteGuageMetric() {
        return this.igniteGuageMetric;
    }

    /**
     * This method is a setter for igniteGuageMetric.
     *
     * @param igniteGuageMetric : Gauge
     */
    
    void setIgniteGuageMetric(Gauge igniteGuageMetric) {
        this.igniteGuageMetric = igniteGuageMetric;
    }
}