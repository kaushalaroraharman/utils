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

package com.harman.ignite.diagnostic;

import com.harman.ignite.healthcheck.InvalidMetricNamingException;
import com.harman.ignite.healthcheck.InvalidMetricNamingException;
import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import com.harman.ignite.utils.metrics.IgniteDiagnosticGuage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The objective of DiagnosticService is to do reporting in Graylog,
 * and publish metrics in Prometheus about diagnostic data of a service.
 * It collects diagnostic data from various reporters and updates the Prometheus gauge metrics.
 * It also logs the diagnostic results.
 *
 * <p>Example usage:</p>
 * <pre>
 *   DiagnosticService diagnosticService = new DiagnosticService();
 *   diagnosticService.triggerDiagnosis();
 * </pre>
 *
 * @since 1.0
 * @version 1.0
 *
 * @see com.harman.ignite.diagnostic.DiagnosticReporter
 * @see com.harman.ignite.diagnostic.DiagnosticData
 * @see com.harman.ignite.diagnostic.DiagnosticResult
 * @see com.harman.ignite.utils.metrics.IgniteDiagnosticGuage
 * @see com.harman.ignite.healthcheck.InvalidMetricNamingException
 * @see org.springframework.beans.factory.annotation.Autowired
 * @see org.springframework.beans.factory.annotation.Value
 * @see org.springframework.stereotype.Component
 *
 */
@Component
public class DiagnosticService {
    /** Logger instance for logging. */
    private static final IgniteLogger LOGGER = IgniteLoggerFactory.getLogger(DiagnosticService.class);

    /** List of diagnostic reporters. */
    @Autowired(required = false)
    private List<DiagnosticReporter> reporters;

    /** Node name for the service. */
    @Value("${NODE_NAME:localhost}")
    private String nodeName;

    /** Prometheus gauge for service diagnostics. */
    @Autowired
    private IgniteDiagnosticGuage serviceDiagnosticGuage;

    /**
     * Triggers the diagnosis process.
     * It collects diagnostic data from enabled reporters and updates the Prometheus gauge metrics.
     * It also logs the diagnostic results.
     */
    public void triggerDiagnosis() {
        List<DiagnosticReporter> enabledDiagnosticReporters = getEnabledReporters();
        for (DiagnosticReporter reporter : enabledDiagnosticReporters) {
            String metricName = reporter.getDiagnosticMetricName();
            DiagnosticData data = reporter.getDiagnosticData();
            for (Map.Entry<String, DiagnosticResult> entry : data.entrySet()) {
                String key = entry.getKey();
                DiagnosticResult result = data.get(key);
                serviceDiagnosticGuage.set(result.getValue(), nodeName, metricName, key);
                if (result == DiagnosticResult.PASS) {
                    LOGGER.info("Diagnosis for metric name {} and key {} is {}", metricName, key, result);
                } else {
                    LOGGER.warn("Diagnosis for metric name {} and key {} is {}", metricName, key, result);
                }
            }
        }
    }

    /**
     * Gets the list of enabled diagnostic reporters.
     *
     * @return the list of enabled diagnostic reporters
     */
    protected List<DiagnosticReporter> getEnabledReporters() {
        Map<String, String> metricToReporterMapping = new HashMap<>();
        List<DiagnosticReporter> enabledDiagnosticReporters = new ArrayList<>();
        if (reporters != null) {
            for (DiagnosticReporter reporter : reporters) {
                if (reporter.isDiagnosticReporterEnabled()) {
                    String reporterName = reporter.getDiagnosticReporterName();
                    String metricName = reporter.getDiagnosticMetricName();
                    // If two reporters have metrics with the same name, throw an exception
                    if (metricToReporterMapping.containsKey(metricName)) {
                        LOGGER.error("Two diagnostic reporters {} and {} cannot have the same MetricName: {}",
                                reporterName, metricToReporterMapping.get(metricName), metricName);
                        throw new InvalidMetricNamingException("Two diagnostic reporters "
                                + reporterName + " and " + metricToReporterMapping.get(metricName)
                                + " cannot have the same MetricName " + metricName);
                    }
                    LOGGER.info("Diagnostic reporter {}, is enabled.", reporterName);
                    metricToReporterMapping.put(metricName, reporterName);
                    enabledDiagnosticReporters.add(reporter);
                } else {
                    LOGGER.info("Diagnostic reporter {}, is disabled.", reporter.getDiagnosticReporterName());
                }
            }
        }
        return enabledDiagnosticReporters;
    }

    /**
     * Sets the list of diagnostic reporters. This method is used for testing.
     *
     * @param reporters the list of diagnostic reporters
     */
    void setReporters(List<DiagnosticReporter> reporters) {
        this.reporters = reporters;
    }

    /**
     * Sets the node name. This method is used for testing.
     *
     * @param nodeName the node name
     */
    void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Sets the Prometheus gauge for service diagnostics. This method is used for testing.
     *
     * @param serviceDiagnosticGuage the Prometheus gauge for service diagnostics
     */
    void setServiceDiagnosticGuage(IgniteDiagnosticGuage serviceDiagnosticGuage) {
        this.serviceDiagnosticGuage = serviceDiagnosticGuage;
    }
}
