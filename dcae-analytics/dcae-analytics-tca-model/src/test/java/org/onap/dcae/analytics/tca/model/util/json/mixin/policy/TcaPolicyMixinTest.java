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

package org.onap.dcae.analytics.tca.model.util.json.mixin.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.tca.model.BaseTcaModelTest;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion;
import java.util.List;

/**
 * @author Rajiv Singla
 */
class TcaPolicyMixinTest extends BaseTcaModelTest {

    @Test
    @DisplayName("Test TCA Policy JSON Conversions")
    void testTCAPolicyJsonConversions() throws Exception {

        final List<TcaPolicy> tcaPolicy =
                assertJsonConversions(TestFileLocation.TCA_POLICY_JSON, TcaModelJsonConversion
                        .TCA_POLICY_JSON_FUNCTION, TcaModelJsonConversion.TCA_OBJECT_MAPPER);

        assertThat(tcaPolicy).isNotNull();
        TcaPolicy tcaPol = tcaPolicy.get(0);
        assertThat(tcaPol.getMetricsPerEventName().size())
                .as("TCA Policy Metrics Per Event Name must be 3").isEqualTo(3);

        assertThat(tcaPol.getMetricsPerEventName().get(0).getThresholds().size())
                .as("TCA Policy Thresholds for first event name must be 3").isEqualTo(3);

        // test tca policy serialization
        testSerialization(tcaPolicy, getClass());
    }

}
