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

package org.onap.dcae.analytics.model.cef;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Common Event Format - Fields Specific to threshold crossing alert events
 *
 * @author Rajiv Singla
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ThresholdCrossingAlertFields extends BaseCEFModel {

    private static final long serialVersionUID = 1L;

    /**
     * Additional Performance counters parameters.
     */
    private List<PerformanceCounter> additionalParameters;

    /**
     * Event alert action
     */
    private AlertAction alertAction;

    /**
     * Unique short alert description such as IF-SHUB-ERRDROP
     */
    private String alertDescription;

    /**
     * Alert type
     */
    private AlertType alertType;

    /**
     * Calculated API value (if applicable)
     */
    private String alertValue;

    /**
     * List of eventIds associated with the event being reported
     */
    private List<String> associatedAlertIdList;

    /**
     * Time when the performance collector picked up the data; with RFC 2822 compliant format:
     * ‘Sat, 13 Mar 2010 11:29:05 -0800’
     */
    private String collectionTimestamp;

    /**
     * Specific performance collector instance used
     */
    private String dataCollector;

    /**
     * Type of network element
     */
    private String elementType;

    /**
     * Event severity or priority
     */
    private EventSeverity eventSeverity;

    /**
     * Time closest to when the measurement was made; with RFC 2822 compliant format: ‘Sat, 13 Mar 2010 11:29:05 -0800’
     */
    private String eventStartTimestamp;

    /**
     * Physical or logical port or card (if applicable)
     */
    private String interfaceName;

    /**
     * Network name
     */
    private String networkService;

    /**
     * Possible Root Cause (reserved for future use)
     */
    private String possibleRootCause;

    /**
     * Version of the thresholdCrossingAlertFields block
     */
    private Integer thresholdCrossingFieldsVersion;
}
