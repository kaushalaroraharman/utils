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

package com.harman.ignite.utils.logger;

import com.harman.ignite.entities.IgniteEvent;

/**
 * Interface for IgniteLogger.
 *
 * <p>This interface defines methods for logging at various levels (trace, debug, info, warn, error)
 * with support for IgniteEvent and message formatting.</p>
 *
 * @since 1.0
 * @version 1.0
 *
 * @see com.harman.ignite.entities.IgniteEvent
 */
public interface IgniteLogger {

    /**
     * Method to know if Trace level log is enabled or not.
     *
     * @return true if trace level is enabled, false otherwise.
     */
    public boolean isTraceEnabled();

    /**
     * Method to know if Debug level log is enabled or not.
     *
     * @return true if debug level is enabled, false otherwise.
     */
    public boolean isDebugEnabled();

    /**
     * Method to know if Info level log is enabled or not.
     *
     * @return true if info level is enabled, false otherwise.
     */
    public boolean isInfoEnabled();

    /**
     * Method to know if Warn level log is enabled or not.
     *
     * @return true if warn level is enabled, false otherwise.
     */
    public boolean isWarnEnabled();

    /**
     * Method to know if Error level log is enabled or not.
     *
     * @return true if error level is enabled, false otherwise.
     */
    public boolean isErrorEnabled();

    /**
     * Logs a trace level message with an IgniteEvent.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     */
    public void trace(IgniteEvent event, String msg);

    /**
     * Logs a trace level message with an IgniteEvent and arguments.
     *
     * @param event the IgniteEvent associated with the log message
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void trace(IgniteEvent event, String format, Object... arguments);

    /**
     * Logs a trace level message with an IgniteEvent and a throwable.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void trace(IgniteEvent event, String msg, Throwable t);

    /**
     * Logs a trace level message.
     *
     * @param msg the message to log
     */
    public void trace(String msg);

    /**
     * Logs a trace level message with arguments.
     *
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void trace(String format, Object... arguments);

    /**
     * Logs a trace level message with a throwable.
     *
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void trace(String msg, Throwable t);

    /**
     * Logs a debug level message with an IgniteEvent.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     */
    public void debug(IgniteEvent event, String msg);

    /**
     * Logs a debug level message with an IgniteEvent and arguments.
     *
     * @param event the IgniteEvent associated with the log message
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void debug(IgniteEvent event, String format, Object... arguments);

    /**
     * Logs a debug level message with an IgniteEvent and a throwable.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void debug(IgniteEvent event, String msg, Throwable t);

    /**
     * Logs a debug level message.
     *
     * @param msg the message to log
     */
    public void debug(String msg);

    /**
     * Logs a debug level message with arguments.
     *
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void debug(String format, Object... arguments);

    /**
     * Logs a debug level message with a throwable.
     *
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void debug(String msg, Throwable t);

    /**
     * Logs an info level message with an IgniteEvent.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     */
    public void info(IgniteEvent event, String msg);

    /**
     * Logs an info level message with an IgniteEvent and arguments.
     *
     * @param event the IgniteEvent associated with the log message
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void info(IgniteEvent event, String format, Object... arguments);

    /**
     * Logs an info level message with an IgniteEvent and a throwable.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void info(IgniteEvent event, String msg, Throwable t);

    /**
     * Logs an info level message.
     *
     * @param msg the message to log
     */
    public void info(String msg);

    /**
     * Logs an info level message with arguments.
     *
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void info(String format, Object... arguments);

    /**
     * Logs an info level message with a throwable.
     *
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void info(String msg, Throwable t);

    /**
     * Logs a warn level message with an IgniteEvent.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     */
    public void warn(IgniteEvent event, String msg);

    /**
     * Logs a warn level message with an IgniteEvent and arguments.
     *
     * @param event the IgniteEvent associated with the log message
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void warn(IgniteEvent event, String format, Object... arguments);

    /**
     * Logs a warn level message with an IgniteEvent and a throwable.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void warn(IgniteEvent event, String msg, Throwable t);

    /**
     * Logs a warn level message.
     *
     * @param msg the message to log
     */
    public void warn(String msg);

    /**
     * Logs a warn level message with arguments.
     *
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void warn(String format, Object... arguments);

    /**
     * Logs a warn level message with a throwable.
     *
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void warn(String msg, Throwable t);

    /**
     * Logs an error level message with an IgniteEvent.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     */
    public void error(IgniteEvent event, String msg);

    /**
     * Logs an error level message with an IgniteEvent and arguments.
     *
     * @param event the IgniteEvent associated with the log message
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void error(IgniteEvent event, String format, Object... arguments);

    /**
     * Logs an error level message with an IgniteEvent and a throwable.
     *
     * @param event the IgniteEvent associated with the log message
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void error(IgniteEvent event, String msg, Throwable t);

    /**
     * Logs an error level message.
     *
     * @param msg the message to log
     */
    public void error(String msg);

    /**
     * Logs an error level message with arguments.
     *
     * @param format the message format string
     * @param arguments the arguments to be used in the message format
     */
    public void error(String format, Object... arguments);

    /**
     * Logs an error level message with a throwable.
     *
     * @param msg the message to log
     * @param t the throwable to log
     */
    public void error(String msg, Throwable t);

}