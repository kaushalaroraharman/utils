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

import ch.qos.logback.classic.pattern.CallerDataConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.status.ErrorStatus;

import java.util.List;

/**
 * This is used to customize log messages.
 * This class overrides methods from CallerDataConverter and customize log messages.<br>
 * Customizations include:<br>
 * 1. removing of "caller" from log message.<br>
 * 2. Printing short-form of fully qualified package <br>
 * name(eg:org.eclipse.ecsp.utils.logger.IgniteCallerDataConverter
 * will be replaced as c.h.i.u.l.IgniteCallerDataConverter)
 *
 * @author vishnu.k;
 */
public class IgniteCallerDataConverter extends CallerDataConverter {


    /**
     * Variable to track the start of depth.
     */
    private int depthStart = 1;

    /**
     * Maximum error count before logging stops.
     */
    static final int MAX_ERR_COUNT = 4;

    /**
     * Variable to track count of error.
     */
    private int errCount = 0;

    /**
     * List of event evaluators.
     */
    List<EventEvaluator<ILoggingEvent>> eventEvaluatorList = null;

    /**
     * Converts the logging event to a string representation.
     *
     * @param logEvent : the logging event
     * @return the string representation of the logging event
     */
    @Override
    public String convert(ILoggingEvent logEvent) {
        if (eventEvaluatorList != null) {
            boolean printCallerData = false;

            for (int i = 0; i < eventEvaluatorList.size(); i++) {
                EventEvaluator<ILoggingEvent> eventEvaluator = eventEvaluatorList.get(i);
                if (canEvaluatorProcessLogEvent(logEvent, eventEvaluator)) {
                    printCallerData = true;
                    break;
                }
            }

            // no evaluator can process the logging event
            if (!printCallerData) {
                return CoreConstants.EMPTY_STRING;
            }
        }

        return convertToCallerData(logEvent);
    }

    /**
     * Converts the logging event to caller data.
     *
     * @param le : the logging event
     * @return the caller data as a string
     */
    private String convertToCallerData(ILoggingEvent le) {
        StringBuilder buf = new StringBuilder();
        StackTraceElement[] cda = le.getCallerData();
        if (cda != null && cda.length > depthStart) {
            buf.append(getCallerLinePrefix());
            buf.append(cda[depthStart]);
            buf.append(" ");

            return buf.toString();
        } else {
            return CallerData.CALLER_DATA_NA;
        }
    }

    /**
     * This method is a getter for callerLinePrefix.
     *
     * @return String
     */
    @Override
    protected String getCallerLinePrefix() {
        return "";
    }

    /**
     * Checks if the event evaluator can process the logging event.
     *
     * @param logEvent : the logging event
     * @param eventEvaluator : the event evaluator
     *
     * @return true if the event evaluator can process the logging event, false otherwise
     */
    private boolean canEvaluatorProcessLogEvent(ILoggingEvent logEvent, EventEvaluator<ILoggingEvent> eventEvaluator) {
        try {
            if (eventEvaluator.evaluate(logEvent)) {
                return true;
            }
        } catch (EvaluationException eex) {
            errCount++;
            processErrorCount(eventEvaluator, eex);
        }
        return false;
    }

    /**
     * Processes the error count and logs errors if necessary.
     *
     * @param ee : the event evaluator
     * @param eex : the evaluation exception
     */
    private void processErrorCount(EventEvaluator<ILoggingEvent> ee, EvaluationException eex) {
        if (errCount < MAX_ERR_COUNT) {
            addError("Exception thrown for evaluator named [" + ee.getName() + "]", eex);
        } else if (errCount == MAX_ERR_COUNT) {
            ErrorStatus errorStatus = new ErrorStatus(
                    "Exception thrown for evaluator named [" + ee.getName() + "].",
                    this, eex);
            errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors."
                    + "We don't want the StatusManager to get flooded.", this));
            addStatus(errorStatus);
        }
    }

}