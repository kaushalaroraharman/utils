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

package org.eclipse.ecsp.utils.logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/** Test class for IgniteLogger for error.
 *
 * @author AKumar
 */

public class TestIgniteLoggerError {
    private static IgniteLoggerImpl igniteLogger = IgniteLoggerImpl.getIgniteLoggerImplInstance(EventLogger2.class);
    Logger logger;

    @Before
    public void setup() {
        logger = Mockito.mock(Logger.class);
        igniteLogger.setLogger(logger);
    }

    @Test
    public void testIgniteLoggersWhenErrorEnabled() {
        when(logger.isErrorEnabled()).thenReturn(true);
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        igniteLogger.error("ErrorWithMsg");
        Mockito.verify(logger).error(arg.capture());
        assertEquals("ErrorWithMsg", arg.getValue());
    }

    @Test
    public void testIgniteLoggersWhenErrorDisabled() {
        when(logger.isErrorEnabled()).thenReturn(false);
        igniteLogger.error("ErrorWithMsg");
        Mockito.verify(logger, new Times(1)).error("ErrorWithMsg");
    }
}
