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

package com.harman.ignite.healthcheck;

/**
 * This class is used to store the health service state.
 *
 * @since 1.0
 * @version 1.0
 *
 * @see java.lang.Object
 */
public class HealthServiceState {

    private static final int BITS = 32;

    /** The state of the health service. */
    private double state;

    /** The message associated with the health service state. */
    private String message;

    /**
     * Gets the state of the health service.
     *
     * @return the state of the health service
     */
    public double getState() {
        return state;
    }

    /**
     * Sets the state of the health service.
     *
     * @param state the state to set
     */
    public void setState(double state) {
        this.state = state;
    }

    /**
     * Gets the message associated with the health service state.
     *
     * @return the message associated with the health service state
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the health service state.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Default constructor.
     */
    public HealthServiceState() {
        // default constructor
    }

    /**
     * Computes the hash code for this health service state.
     *
     * @return the hash code for this health service state
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message != null) ? message.hashCode() : 0);
        long temp;
        temp = Double.doubleToLongBits(state);
        result = prime * result + (int) (temp ^ (temp >>> BITS));
        return result;
    }

    /**
     * Compares this health service state to the specified object.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HealthServiceState other = (HealthServiceState) obj;
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        return (Double.doubleToLongBits(state) == Double.doubleToLongBits(other.state));
    }
}