/*
 * ================================================================================
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

package org.onap.dcae.analytics.tca.model.policy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * TCA Metrics that need to applied to each Event Name
 *
 * @author Rajiv Singla
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetricsPerEventName extends BaseTcaPolicyModel {


    private static final long serialVersionUID = 1L;

    /**
     * Event Name to which TCA Policy needs to applied.
     */
    private String eventName;

    /**
     * Control Loop Schema Type
     */
    private ControlLoopSchemaType controlLoopSchemaType;

    /**
     * Policy Scope
     */
    private String policyScope;

    /**
     * Policy Name
     */
    private String policyName;

    /**
     * Policy Version
     */
    private String policyVersion;

    /**
     * Policy Thresholds
     */
    private List<Threshold> thresholds;
    
    public MetricsPerEventName() { }

}
