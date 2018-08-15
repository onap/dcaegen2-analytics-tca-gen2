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
import static org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion.EVENT_LISTENER_JSON_FUNCTION;
import static org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion.EVENT_LISTENER_LIST_JSON_FUNCTION;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.model.BaseAnalyticsModelTest;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.model.cef.Field;
import org.onap.dcae.analytics.model.cef.NamedArrayOfFields;

/**
 * Contains test for CEF Event Listener JSON Conversions.
 *
 * @author Rajiv Singla
 */
class EventListenerMixinTest extends BaseAnalyticsModelTest {

    @Test
    @DisplayName("Test single CEF JSON conversions")
    void testSingleEventListenerJsonConversion() {

        final EventListener eventListener = assertJsonConversions(TestFileLocation.CEF_JSON_MESSAGE,
                EVENT_LISTENER_JSON_FUNCTION, ANALYTICS_MODEL_OBJECT_MAPPER);

        Assertions.assertThat(eventListener).isNotNull();
        Assertions.assertThat(eventListener.getEvent()).isNotNull();
        final List<Field> additionalFields =
                eventListener.getEvent().getMeasurementsForVfScalingFields().getAdditionalFields();

        Assertions.assertThat(additionalFields.size()).as("Additional Fields size is 2").isEqualTo(2);

        final List<NamedArrayOfFields> additionalMeasurements =
                eventListener.getEvent().getMeasurementsForVfScalingFields().getAdditionalMeasurements();

        Assertions.assertThat(additionalMeasurements.size()).as("Additional Measurements size must be 1").isEqualTo(1);

        final List<Field> arrayOfFields = additionalMeasurements.get(0).getArrayOfFields();

        Assertions.assertThat(arrayOfFields.size()).as("Array Of Field size must be 6").isEqualTo(6);
    }

    @Test
    @DisplayName("Test collection of CEF JSON messages conversions")
    void testCollectionOfEventListenersJsonConversion() {

        List<EventListener> eventListeners =
                assertJsonConversions(TestFileLocation.CEF_JSON_MESSAGES,
                        EVENT_LISTENER_LIST_JSON_FUNCTION, ANALYTICS_MODEL_OBJECT_MAPPER);
        Assertions.assertThat(eventListeners).isNotNull();
        Assertions.assertThat(eventListeners.size()).as("Event Listeners size must be 31").isEqualTo(31);

        // Checks serialization
        testSerialization(eventListeners, getClass());

    }
}
