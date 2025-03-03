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

import com.harman.ignite.utils.logger.IgniteLogger;
import com.harman.ignite.utils.logger.IgniteLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * Performs the validation of the values of the properties configured against the expected diagnostic value provided.
 *
 * <p>Example usage:</p>
 * <pre>
 *   PropertyDiagnosticReporterImpl reporter = new PropertyDiagnosticReporterImpl();
 *   DiagnosticData data = reporter.getDiagnosticData();
 * </pre>
 *
 * @since 1.0
 * @version 1.0
 *
 * @see com.harman.ignite.diagnostic.DiagnosticReporter
 * @see com.harman.ignite.diagnostic.DiagnosticData
 * @see com.harman.ignite.diagnostic.DiagnosticResult
 * @see org.springframework.beans.factory.annotation.Autowired
 * @see org.springframework.beans.factory.annotation.Value
 * @see org.springframework.stereotype.Component
 *
 */
@Component("propertyDiagnostic")
public class PropertyDiagnosticReporterImpl implements DiagnosticReporter {

    /** Logger instance for logging. */
    private static IgniteLogger igniteLogger = IgniteLoggerFactory.getLogger(PropertyDiagnosticReporterImpl.class);

    /** Metric name for diagnostic properties. */
    private static final String DIAGNOSTIC_PROPERTY_METRIC_NAME = "DIAGNOSTIC_PROPERTY_METRIC";

    /** Reporter name for diagnostic properties. */
    private static final String DIAGNOSTIC_PROPERTY_REPORTER_NAME = "DIAGNOSTIC_PROPERTY_REPORTER";

    /** Configuration map for storing property values. */
    HashMap<String, Object> configurationMap = new HashMap<>();

    /** Application context for accessing environment properties. */
    @Autowired
    ApplicationContext ctx;

    /** Flag to check if the diagnostic property reporter is enabled. */
    @Value("${" + DiagnosticConstants.PROPERTY_DIAGNOSTIC_REPORTER_ENABLED + ": true }")
    private boolean diagnosticPropertyReporterEnabled;

    /**
     * Gets the diagnostic data by validating the properties against the expected values.
     *
     * @return the diagnostic data
     */
    @Override
    public DiagnosticData getDiagnosticData() {
        StringBuilder passedParms = new StringBuilder().append('\n');
        StringBuilder failedParms = new StringBuilder().append('\n');
        DiagnosticData diagnosticData = new DiagnosticData();
        MutablePropertySources propSources = ((AbstractEnvironment) ctx.getEnvironment()).getPropertySources();
        StreamSupport.stream(propSources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .filter(diagnosticPropertyName ->
                        diagnosticPropertyName.startsWith(DiagnosticConstants.DIAGNOSTIC_KEYWORD))
                .forEach(diagnosticPropertyName -> {
                    String propertyToBeValidated = diagnosticPropertyName
                            .substring(DiagnosticConstants.DIAGNOSTIC_KEYWORD.length());
                    // Check if the property to be validated is present in the environment
                    if (Objects.equals(((AbstractEnvironment) ctx.getEnvironment()).getProperty(diagnosticPropertyName),
                            ((AbstractEnvironment) ctx.getEnvironment())
                                    .getProperty(propertyToBeValidated))) {
                        diagnosticData.put(propertyToBeValidated, DiagnosticResult.PASS);
                        passedParms.append(propertyToBeValidated).append('\n');
                    } else {
                        diagnosticData.put(propertyToBeValidated, DiagnosticResult.FAIL);
                        failedParms.append(propertyToBeValidated).append('\n');
                    }
                });
        igniteLogger.debug("Properties that matches diagnostic criteria are : {}", passedParms.toString());
        igniteLogger.warn("Properties that don't matches diagnostic criteria are : {}", failedParms.toString());
        return diagnosticData;
    }

    /**
     * Checks if the diagnostic reporter is enabled.
     *
     * @return true if the diagnostic reporter is enabled, false otherwise
     */
    @Override
    public boolean isDiagnosticReporterEnabled() {
        return diagnosticPropertyReporterEnabled;
    }

    /**
     * Gets the name of the diagnostic reporter.
     *
     * @return the name of the diagnostic reporter
     */
    @Override
    public String getDiagnosticReporterName() {
        return DIAGNOSTIC_PROPERTY_REPORTER_NAME;
    }

    /**
     * Gets the metric name for the diagnostic properties.
     *
     * @return the metric name for the diagnostic properties
     */
    @Override
    public String getDiagnosticMetricName() {
        return DIAGNOSTIC_PROPERTY_METRIC_NAME;
    }
}
