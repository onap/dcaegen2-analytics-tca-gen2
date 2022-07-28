/*
 * ============LICENSE_START=======================================================
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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Generic Event Format
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EventV7 extends BaseCEFModel {


    private static final long serialVersionUID = 1L;

    /**
     * Fields common to all Events
     */
    private CommonEventHeaderV7 commonEventHeader;

    /**
     * Measurements for Vf scaling fields
     */
    private MeasurementFields measurementFields;

    /**
     * Threshold crossing alert Fields.
     */
    private ThresholdCrossingAlertFields thresholdCrossingAlertFields;
}
