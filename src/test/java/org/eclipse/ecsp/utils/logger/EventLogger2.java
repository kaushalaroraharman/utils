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

/** Thread class for EventLogger2.
 *
 * @author AKumar
 */
public class EventLogger2 implements Runnable {

    private static IgniteLogger igniteLogger = IgniteLoggerFactory.getLogger(EventLogger2.class);

    @Override
    public void run() {
        igniteLogger.info("Info message from EventLogger2");
        igniteLogger.trace("Info message from EventLogger2");
        igniteLogger.debug("Info message from EventLogger2");
        igniteLogger.error("Error message from EventLogger2", new Exception("exception occurred"));
    }

}
