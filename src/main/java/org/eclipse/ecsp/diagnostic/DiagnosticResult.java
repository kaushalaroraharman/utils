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

package org.eclipse.ecsp.diagnostic;

/**
 * This enum is used to report the diagnostic result.
 * It provides two possible results: PASS and FAIL, each with a corresponding value.
 * The value can be retrieved using the {@link #getValue()} method.
 *
 * <p>Example usage:</p>
 * <pre>
 *   DiagnosticResult result = DiagnosticResult.PASS;
 *   double value = result.getValue();
 * </pre>
 *
 * @since 1.0
 * @version 1.0
 *
 * @see DiagnosticReporter
 * @see DiagnosticData
 * @see DiagnosticConstants
 *
 * @author avadakkootko
 */
public enum DiagnosticResult {

    /**
     * Used by diagnostic metric to report success.
     * Returns a value of 1.0.
     */
    PASS {
        @Override
        public double getValue() {
            return 1.0;
        }
    },
    /**
     * Used by diagnostic metric to report an issue.
     * Returns a value of 0.0.
     */
    FAIL {
        @Override
        public double getValue() {
            return 0.0;
        }
    };

    /**
     * Gets the value associated with the diagnostic result.
     *
     * @return the value of the diagnostic result
     */
    public abstract double getValue();
}
