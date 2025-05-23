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

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluator;
import ch.qos.logback.core.status.ErrorStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Add a stack trace in case the event contains a Throwable. This is used to customize error message. <br>
 * Customizations include: <br>
 * 1. removing of "at" from stack trace. <br>
 * 2. printing full stack-trace in single line separated by comma(,) <br>
 *
 * @author vishnu.k
 */
public class IgniteThrowableProxyConverter extends ThrowableHandlingConverter {

    /**
     * The default capacity for the builder.
     */
    protected static final int BUILDER_CAPACITY = 2048;

    int lengthOption;
    List<EventEvaluator<ILoggingEvent>> evaluatorList = null;
    List<String> ignoredStackTraceLines = null;

    int errorCount = 0;


    /**
     * This method is used to start the converter.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void start() {

        String lengthStr = getFirstOption();
        createLengthOption(lengthStr);

        final List<String> optionList = getOptionList();
        if (optionList != null && optionList.size() > 1) {
            final int optionListSize = optionList.size();
            for (int i = 1; i < optionListSize; i++) {
                String evaluatorOrIgnoredStackTraceLine = (String) optionList.get(i);
                Context context = getContext();
                Map<String, EventEvaluator<?>> evaluatorMap = (Map<String, EventEvaluator<?>>) context
                        .getObject(CoreConstants.EVALUATOR_MAP);
                EventEvaluator<ILoggingEvent> ee = (EventEvaluator<ILoggingEvent>) evaluatorMap
                        .get(evaluatorOrIgnoredStackTraceLine);
                if (ee != null) {
                    addEvaluator(ee);
                } else {
                    addIgnoreStackTraceLine(evaluatorOrIgnoredStackTraceLine);
                }
            }
        }
        super.start();
    }

    private void createLengthOption(String lengthStr) {
        if (lengthStr == null) {
            lengthOption = Integer.MAX_VALUE;
        } else {
            lengthStr = lengthStr.toLowerCase();
            if ("full".equals(lengthStr)) {
                lengthOption = Integer.MAX_VALUE;
            } else if ("short".equals(lengthStr)) {
                lengthOption = 1;
            } else {
                try {
                    lengthOption = Integer.parseInt(lengthStr);
                } catch (NumberFormatException nfe) {
                    addError("Could not parse [" + lengthStr + "] as an integer");
                    lengthOption = Integer.MAX_VALUE;
                }
            }
        }
    }

    private void addEvaluator(EventEvaluator<ILoggingEvent> ee) {
        if (evaluatorList == null) {
            evaluatorList = new ArrayList<>();
        }
        evaluatorList.add(ee);
    }

    private void addIgnoreStackTraceLine(String ignoredStackTraceLine) {
        if (ignoredStackTraceLines == null) {
            ignoredStackTraceLines = new ArrayList<>();
        }
        ignoredStackTraceLines.add(ignoredStackTraceLine);
    }

    /**
     * Stops the converter and clears the evaluator list.
     * This method is called to release any resources held by the converter.
     */
    @Override
    public void stop() {
        evaluatorList = null;
        super.stop();
    }

    /**
     * Add extra data to the log event.
     *
     * @param builder the string builder
     * @param step the stack trace element proxy
     */
    protected void extraData(StringBuilder builder, StackTraceElementProxy step) {
        // nop
    }

    /**
     * Convert the event to a string.
     *
     * @param event the log event
     * @return the string representation of the event
     */
    public String convert(ILoggingEvent event) {

        IThrowableProxy tp = event.getThrowableProxy();
        if (tp == null) {
            return CoreConstants.EMPTY_STRING;
        }
        // an evaluator match will cause stack printing to be skipped
        if (evaluatorList != null) {
            boolean printStack = true;
            for (EventEvaluator<ILoggingEvent> ee : evaluatorList) {
                try {
                    if (ee.evaluate(event)) {
                        printStack = false;
                        break;
                    }
                } catch (EvaluationException eex) {
                    errorCount++;
                    processErrorCount(ee, eex);
                }
            }

            if (!printStack) {
                return CoreConstants.EMPTY_STRING;
            }
        }

        return throwableProxyToString(tp);
    }

    /**
     * Convert the throwable proxy to a string.
     *
     * @param tp the throwable proxy
     * @return the string representation of the throwable proxy
     */
    protected String throwableProxyToString(IThrowableProxy tp) {
        StringBuilder sb = new StringBuilder(BUILDER_CAPACITY);

        recursiveAppend(sb, null, ThrowableProxyUtil.REGULAR_EXCEPTION_INDENT, tp);
        return sb.toString().replace("\t", ",  ");
    }

    private void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
        if (tp == null) {
            return;
        }
        subjoinFirstLine(sb, prefix, indent, tp);
        subjoinStepArray(sb, indent, tp);
        IThrowableProxy[] suppressed = tp.getSuppressed();
        if (suppressed != null) {
            for (IThrowableProxy current : suppressed) {
                recursiveAppend(sb,
                        CoreConstants.SUPPRESSED,
                        indent + ThrowableProxyUtil.SUPPRESSED_EXCEPTION_INDENT,
                        current);
            }
        }
        recursiveAppend(sb, CoreConstants.CAUSED_BY, indent, tp.getCause());
    }

    private void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
        ThrowableProxyUtil.indent(buf, indent - 1);
        if (prefix != null) {
            buf.append(prefix);
        }
        subjoinExceptionMessage(buf, tp);
    }

    private void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
        buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
    }

    /**
     * Appends the stack trace elements of the given throwable proxy to the provided StringBuilder.
     *
     * @param buf the StringBuilder to append the stack trace elements to
     * @param indent the indentation level for the stack trace elements
     * @param tp the throwable proxy containing the stack trace elements
     */
    protected void subjoinStepArray(StringBuilder buf, int indent, IThrowableProxy tp) {
        StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        int commonFrames = tp.getCommonFrames();

        boolean unrestrictedPrinting = lengthOption > stepArray.length;

        int maxIndex = (unrestrictedPrinting) ? stepArray.length : lengthOption;
        if (commonFrames > 0 && unrestrictedPrinting) {
            maxIndex -= commonFrames;
        }

        int ignoredCount = 0;
        for (int i = 0; i < maxIndex; i++) {
            StackTraceElementProxy element = stepArray[i];
            if (!isIgnoredStackTraceLine(element.toString())) {
                ThrowableProxyUtil.indent(buf, indent);
                printStackLine(buf, ignoredCount, element);
                ignoredCount = 0;
                buf.append(CoreConstants.LINE_SEPARATOR);
            } else {
                ++ignoredCount;
                if (maxIndex < stepArray.length) {
                    ++maxIndex;
                }
            }
        }
        if (ignoredCount > 0) {
            printIgnoredCount(buf, ignoredCount);
            buf.append(CoreConstants.LINE_SEPARATOR);
        }

        if (commonFrames > 0 && unrestrictedPrinting) {
            ThrowableProxyUtil.indent(buf, indent);
            buf
                    .append("... ")
                    .append(tp.getCommonFrames())
                    .append(" common frames omitted")
                    .append(CoreConstants.LINE_SEPARATOR);
        }
    }

    private void printStackLine(StringBuilder buf, int ignoredCount, StackTraceElementProxy element) {
        buf.append(element);
        extraData(buf, element); // allow other data to be added
        if (ignoredCount > 0) {
            printIgnoredCount(buf, ignoredCount);
        }
    }

    private void printIgnoredCount(StringBuilder buf, int ignoredCount) {
        buf.append(" [").append(ignoredCount).append(" skipped]");
    }

    private boolean isIgnoredStackTraceLine(String line) {
        if (ignoredStackTraceLines != null) {
            for (String ignoredStackTraceLine : ignoredStackTraceLines) {
                if (line.contains(ignoredStackTraceLine)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void processErrorCount(EventEvaluator<ILoggingEvent> ee, EvaluationException eex) {
        if (errorCount < CoreConstants.MAX_ERROR_COUNT) {
            addError("Exception thrown for evaluator named [" + ee.getName() + "]", eex);
        } else if (errorCount == CoreConstants.MAX_ERROR_COUNT) {
            ErrorStatus errorStatus = new ErrorStatus(
                    "Exception thrown for evaluator named [" + ee.getName() + "].",
                    this,
                    eex);
            errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors."
                    + "We don't want the StatusManager to get flooded.", this));
            addStatus(errorStatus);
        }
    }

}
