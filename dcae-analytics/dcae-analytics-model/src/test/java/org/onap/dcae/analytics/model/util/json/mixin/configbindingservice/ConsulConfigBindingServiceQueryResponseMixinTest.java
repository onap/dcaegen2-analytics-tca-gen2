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

package org.onap.dcae.analytics.model.util.json.mixin.configbindingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion.ANALYTICS_MODEL_OBJECT_MAPPER;
import static org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion
        .CONFIG_BINDING_SERVICE_LIST_JSON_FUNCTION;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.model.BaseAnalyticsModelTest;
import org.onap.dcae.analytics.model.configbindingservice.ConsulConfigBindingServiceQueryResponse;

/**
 * Consul Config Binding Service Query Response Mixin Test.
 *
 * @author Rajiv Singla
 */
class ConsulConfigBindingServiceQueryResponseMixinTest extends BaseAnalyticsModelTest {


    @Test
    @DisplayName("Test Config Service Bindings JSON conversions")
    void testConfigServiceBindingsConversions() throws Exception {

        List<ConsulConfigBindingServiceQueryResponse> configServiceBindings =
                assertJsonConversions(TestFileLocation.CONFIG_SERVICE_BINDINGS_JSON,
                        CONFIG_BINDING_SERVICE_LIST_JSON_FUNCTION, ANALYTICS_MODEL_OBJECT_MAPPER);

        assertThat(configServiceBindings).isNotNull();
        assertThat(configServiceBindings.size()).as("There should be only 1 binding config").isEqualTo(1);

        final ConsulConfigBindingServiceQueryResponse configBindingServiceQueryResponse = configServiceBindings.get(0);
        assertThat(configBindingServiceQueryResponse.getAddress())
                .as("Config Service Binding ServiceAddress must be 135.25.108.161").isEqualTo("135.25.108.161");
        assertThat(configBindingServiceQueryResponse.getServicePort())
                .as("Config Service Binding ServicePort must be 32769").isEqualTo(32769);
        assertThat(configBindingServiceQueryResponse.getDynamicProperties())
                .as("Dynamic Properties must be empty if all bindings are successful").isEmpty();
    }

}
