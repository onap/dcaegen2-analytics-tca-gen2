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

package org.onap.dcae.analytics.tca.model.util.json.mixin.facade;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.tca.model.BaseTcaModelTest;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion;

/**
 * @author Rajiv Singla
 */
public class TcaAlertMixinTest extends BaseTcaModelTest {

    @Test
    @DisplayName("Confirms TCA Alert JSON Conversions")
    public void testTCAAlertConversion() throws Exception {
        final TcaAlert tcaAlert =
                assertJsonConversions(TestFileLocation.TCA_ALERT_JSON, TcaModelJsonConversion
                        .TCA_ALERT_JSON_FUNCTION, TcaModelJsonConversion.TCA_OBJECT_MAPPER);

        assertThat(tcaAlert).isNotNull();
        assertThat(tcaAlert.getAai().getGenericVNFName()).isEqualTo("vpp-test(?)");

        assertThat(tcaAlert.getTargetType()).isEqualTo("VNF");
        assertThat(tcaAlert.getRequestId()).isEqualTo("0138afac-b032-4e4b-bd30-88260f444888");

        assertThat(tcaAlert.getClosedLoopControlName())
                .isEqualTo("CL-FRWL-LOW-TRAFFIC-SIG-d925ed73-8231-4d02-9545-db4e101f88f8");

        assertThat(tcaAlert.getVersion()).isEqualTo("1.0.2");

        assertThat(tcaAlert.getClosedLoopAlarmStart()).isEqualTo(1478189220547L);

        assertThat(tcaAlert.getClosedLoopEventStatus()).isEqualTo("ONSET");

    }
}
