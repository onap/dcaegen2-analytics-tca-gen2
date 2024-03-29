/*
 * ============LICENSE_START=======================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * Copyright (c) 2022 Wipro Limited Intellectual Property. All rights reserved.
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

package org.onap.dcae.analytics.model.cef;

/**
 * Eventing domain associated with the event
 *
 * @author Rajiv Singla
 */
public enum Domain implements CEFModel {

    fault,
    heartbeat,
    measurementsForVfScaling,
    mobileFlow,
    other,
    measurement,
    sipSignaling,
    stateChange,
    syslog,
    thresholdCrossingAlert,
    voiceQuality;

}
