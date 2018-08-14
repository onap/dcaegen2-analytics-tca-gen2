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

package org.onap.dcae.analytics.tca.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.web.BaseTcaWebSpringBootIT;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rajiv Singla
 */
class TcaProcessingServiceImplTest extends BaseTcaWebSpringBootIT {

    @Autowired
    private TcaProcessingService tcaProcessingService;

    @Autowired
    private TcaPolicyWrapper tcaPolicyWrapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTcaExecutionResults() throws Exception {

        final List<TcaExecutionContext> tcaExecutionResults =
                tcaProcessingService.getTcaExecutionResults
                        (TEST_REQUEST_ID, TEST_TRANSACTION_ID, tcaPolicyWrapper.getTcaPolicy(),
                                Arrays.asList(TEST_CEF_EVENT_LISTENER_STRING,
                                        TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING,
                                        TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING,
                                        TEST_CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME));


        for (TcaExecutionContext tcaExecutionResult : tcaExecutionResults) {
            final TcaAlert tcaAlert = tcaExecutionResult.getTcaResultContext().getTcaAlert();
            String tcaAlertString = "";
            if (tcaAlert != null) {
                tcaAlertString = objectMapper.writeValueAsString(tcaAlert);
            }
            LOGGER.debug("{} -> {}", tcaExecutionResult.getRequestId(), tcaAlertString);

        }

    }

}
