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

package org.onap.dcae.analytics.tca.model.policy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import org.onap.dcae.analytics.model.cef.EventSeverity;

/**
 * TCA Policy Threshold
 *
 * @author Rajiv Singla
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Threshold extends BaseTcaPolicyModel {

    private static final long serialVersionUID = 1L;

    /**
     * Closed Loop Control Name
     */
    private String closedLoopControlName;


    /**
     * Closed Loop Event Status
     */
    private ClosedLoopEventStatus closedLoopEventStatus;

    /**
     * Threshold Version
     */
    private String version;

    /**
     * Path of the field inside Common Event Format which needs to be monitored by TCA App
     * for threshold crossing
     */
    private String fieldPath;

    /**
     * Threshold Value
     */
    private Long thresholdValue;

    /**
     * Direction of threshold
     */
    private Direction direction;

    /**
     * Severity of Event based on CEF Convention
     */
    private EventSeverity severity;


    /**
     * Actual Field value that caused the threshold violation. Note: Ignored for serialization / deserialization
     */
    private BigDecimal actualFieldValue;


}
