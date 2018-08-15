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

import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;

/**
 * TCA Processing Result Context which captures outputs of TCA Execution
 *
 * @author Rajiv Singla
 */
public interface TcaResultContext {

    /**
     * Returns true if Threshold violations are present
     *
     * @return true if Threshold violations are present
     */
    boolean isThresholdViolationsPresent();


    /**
     * Returns TCA policy's violated metrics per event name that was violated by the incoming message
     *
     * @return TCA policy's violated metrics per event name that was violated by the incoming message
     */
    MetricsPerEventName getViolatedMetricsPerEventName();


    /**
     * Sets new value for violated metrics per event name that was violated by the incoming message
     *
     * @param violatedMetricsPerEventName new value for violated metrics per event name that was violated by the
     * incoming message
     */
    void setViolatedMetricsPerEventName(MetricsPerEventName violatedMetricsPerEventName);


    /**
     * Provides TCA Alert message that is generated if there is any TCA policy's threshold violation
     *
     * @return TCA Alert message that is generated if there is any TCA policy's threshold violation
     */
    TcaAlert getTcaAlert();


    /**
     * Sets new value for TCA Alert
     *
     * @param tcaAlert new value for TCA Alert
     */
    void setTcaAlert(TcaAlert tcaAlert);

    /**
     * Provides previous request id for abated threshold violations if present or null
     *
     * @return previous request id for abated threshold violations if present or null
     */
    String getPreviousRequestId();


    /**
     * Sets previous request id for abated threshold violations
     *
     * @param previousRequestId new value of request id for abated threshold violations
     */
    void setPreviousRequestId(String previousRequestId);


}
