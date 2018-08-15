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

package org.onap.dcae.analytics.model.util.json.mixin.cef;

import static org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion.ANALYTICS_MODEL_OBJECT_MAPPER;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.model.BaseAnalyticsModelTest;
import org.onap.dcae.analytics.model.cef.AlertType;


/**
 * Test Alert Type Mixin JSON conversions.
 *
 * @author Rajiv Singla
 */
class AlertTypeMixinTest extends BaseAnalyticsModelTest {

    // NOTE: Alert type enum has some special customizations in AlertTypeMixin class
    // as Java enum names does not allow for "-" so actual values are coded as enum names
    @Test
    @DisplayName("Test Alert Type Json Conversions")
    public void testAlertTypeJsonConversions() throws Exception {

        final String alertTypeJson = serializeModelToJson(AlertType.CARD_ANOMALY, ANALYTICS_MODEL_OBJECT_MAPPER);
        Assertions.assertThat(alertTypeJson)
                .as("Alert Type Json for CARD ANOMALY must have hyphen in it").isEqualTo("\"CARD-ANOMALY\"");
        // convert parsed alert type back to enum
        final AlertType alertType = ANALYTICS_MODEL_OBJECT_MAPPER.readValue(alertTypeJson, AlertType.class);
        Assertions.assertThat(alertType)
                .as("Json String for CARD ANOMALY with hyphen can be converted back to Alert Type")
                .isEqualTo(AlertType.CARD_ANOMALY);
    }

}
