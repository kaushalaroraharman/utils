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

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Base histogram for Ignite.
 *
 * @author ssasidharan
 */
public abstract class AbstractIgniteHistogram {

    /**
     * Timer divisor.
     */
    private static final double TIMER_DIVISOR = 1E9D;

    /**
     * Prometheus histogram instance.
     */
    private Histogram histogram;

    /**
     * Method to create a histogram.
     *
     * @param name       name of the histogram
     * @param help       help text for the histogram
     * @param buckets    buckets for the histogram
     * @param labelNames labels for the histogram
     */
    protected void createHistogram(String name, String help, double[] buckets, String... labelNames) {
        histogram = Histogram
                .build(name, help)
                .labelNames(labelNames)
                .buckets(buckets)
                .register(CollectorRegistry.defaultRegistry);
    }

    /**
     * Start the timer.
     *
     * @return  IgniteTimer instance
     */
    public IgniteTimer start() {
        return new IgniteTimer(this);
    }

    /**
     * Observe the time taken for a function to execute.
     *
     * @param amt   time taken
     * @param labels    labels to be observed
     */
    public void observe(double amt, String... labels) {
        histogram.labels(labels).observe(amt);
    }

    /**
     * Observe the time taken for a function to execute.
     *
     * @param f    supplier function to execute
     * @param labels    labels to be observed
     * @param <T>   type of the result
     * @return  result of the function
     */
    public <T> T observe(Supplier<T> f, String... labels) {
        IgniteTimer timer = start();
        try {
            return f.get();
        } finally {
            timer.observe(labels);
        }
    }

    /**
     * Observe the time taken for a function to execute.
     *
     * @param f    runnable function to execute
     * @param labels    labels to be observed
     */
    public void observe(Runnable f, String... labels) {
        IgniteTimer timer = start();
        try {
            f.run();
        } finally {
            timer.observe(labels);
        }
    }

    /**
     * Observe the time taken for a callable function to execute.
     *
     * @param f       callable function to execute
     * @param labels  labels to be observed
     * @param <V>     type of the result
     * @return        result of the callable function
     * @throws Exception if the callable function throws an exception
     */
    public <V> V observeExtended(Callable<V> f, String... labels) throws Exception {
        IgniteTimer timer = start();
        try {
            return f.call();
        } finally {
            timer.observe(labels);
        }
    }

    /**
     * Timer class for Ignite.
     */
    public static class IgniteTimer {

        /**
         * Timer divisor.
         */
        private AbstractIgniteHistogram histo = null;

        /**
         * Start time.
         */
        private long start = 0L;

        /**
         * Constructor for IgniteTimer.
         *
         * @param histo histogram instance
         */
        public IgniteTimer(AbstractIgniteHistogram histo) {
            this.histo = histo;
            this.start = System.nanoTime();
        }

        /**
         * Observe the time taken for the function to execute.
         *
         * @param labels    labels to be observed
         * @return  time taken
         */
        public double observe(String... labels) {
            double amt = (System.nanoTime() - start) / TIMER_DIVISOR;
            histo.observe(amt, labels);
            return amt;
        }
    }

    /**
     * This method is a getter for histogram.
     *
     * @return Histogram
     */
    Histogram getHistogram() {
        return histogram;
    }
}