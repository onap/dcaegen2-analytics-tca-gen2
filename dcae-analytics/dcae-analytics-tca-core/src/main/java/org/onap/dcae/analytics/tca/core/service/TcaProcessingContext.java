/*
 * ================================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 *
 */

package org.onap.dcae.analytics.tca.core.service;

import org.onap.dcae.analytics.model.cef.EventListener;

/**
 * TCA Processing Context captures various mutable fields that are computed during TCA execution
 *
 * @author Rajiv Singla
 */
public interface TcaProcessingContext {


    /**
     * Provides common event format message as JAVA object that is being analyzed
     *
     * @return common event format message as JAVA object that is being analyzed
     */
    EventListener getEventListener();


    /**
     * Sets new values for common event format message as JAVA object that is being analyzed
     *
     * @param eventListener new values for common event format message as JAVA object that is being analyzed
     */
    void setEventListener(EventListener eventListener);


    /**
     * Provides flag which is false if TCA processing cannot continue to next stage due to some prior condition
     *
     * @return false if TCA processing cannot continue to next stage due to some condition
     */
    boolean isContinueProcessing();

    /**
     * Sets TCA processing continue flag
     *
     * @param isContinueProcessing - TCA processing continue flag
     */
    void setContinueProcessing(boolean isContinueProcessing);


    /**
     * Provides early termination message message if present or null
     *
     * @return early termination message if present or null
     */
    String getEarlyTerminationMessage();

    /**
     * Sets early termination message
     *
     * @param earlyTerminationMessage sets a new value for early termination message
     */
    void setEarlyTerminationMessage(String earlyTerminationMessage);


    /**
     * Returns error message if present or null
     *
     * @return error message if present or null
     */
    String getErrorMessage();

    /**
     * Sets new value for error message
     *
     * @param errorMessage new value for error message
     */
    void setErrorMessage(final String errorMessage);

}
