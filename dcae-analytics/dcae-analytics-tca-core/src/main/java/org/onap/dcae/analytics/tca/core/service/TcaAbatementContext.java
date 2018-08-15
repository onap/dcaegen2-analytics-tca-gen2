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

/**
 * Provides abstractions for TCA Abatement processing calculations
 *
 * @author Rajiv Singla
 */
public interface TcaAbatementContext {

    /**
     * Returns true if TCA Abatement calculations are enabled
     *
     * @return true if TCA Abatement calculations are enabled
     */
    boolean isAbatementEnabled();


    /**
     * Provides abstractions for Tca Abatement Persistence
     *
     * @return abstractions for Tca Abatement Persistence
     */
    TcaAbatementRepository getTcaAbatementRepository();


    /**
     * Creates new instance of TCA Abatement persistence Entity
     *
     * @param lookupKey lookup key
     * @param requestId request id
     * @param isAbatementAlertSent true if abatement alert was already sent
     *
     * @return new instance of TCA Abatement Persistence Entity
     */
    TcaAbatementEntity create(String lookupKey, String requestId, boolean isAbatementAlertSent);


}
