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

import java.util.Date;

/**
 * TCA Abatement Persistence Entity
 *
 * @author Rajiv Singla
 */
public interface TcaAbatementEntity {

    /**
     * Last Modified Date when entity was modified
     *
     * @return last modified date
     */
    Date getLastModificationDate();

    /**
     * Lookup Key
     *
     * @return provides Lookup Key
     */
    String getLookupKey();


    /**
     * Provides Request Id
     *
     * @return request id
     */
    String getRequestId();


    /**
     * Return true if alert was already sent for this Entity else false
     *
     * @return true if alert was already sent for this Entity else false
     */
    boolean isAbatementAlertSent();

}
