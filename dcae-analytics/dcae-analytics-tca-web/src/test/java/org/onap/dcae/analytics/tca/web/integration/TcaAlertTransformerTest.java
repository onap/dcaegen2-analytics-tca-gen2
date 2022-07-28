/*
 * =============LICENSE_START======================================================
 * Copyright (c) 2020 ChinaMobile. All rights reserved.
 * Copyright (c) 2022 Wipro Limited Intellectual Property. All rights reserved.
 * ================================================================================
 * Copyright (c) 2019 IBM
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
package org.onap.dcae.analytics.tca.web.integration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.tca.core.service.GenericTcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaExecutionContext.GenericTcaExecutionContextBuilder;
import org.onap.dcae.analytics.tca.core.service.GenericTcaProcessingContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaResultContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaProcessingContext;
import org.onap.dcae.analytics.tca.core.service.TcaResultContext;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion;
import org.onap.dcae.analytics.tca.web.BaseTcaWebTest;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.domain.TestTcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.web.domain.TestTcaAbatementContext;
import org.onap.dcae.analytics.test.BaseAnalyticsSpringBootIT;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class TcaAlertTransformerTest extends BaseAnalyticsSpringBootIT {

    @Autowired
    Environment environment;

    protected static final String TEST_POLICY_JSON_STRING;
    protected static final String TEST_REQUEST_ID = "testRequestId";
    protected static final List<TcaPolicy> TEST_TCA_POLICY;

    static {

        TEST_POLICY_JSON_STRING = fromStream(TestFileLocation.TCA_POLICY_JSON);
        TEST_TCA_POLICY = TcaModelJsonConversion.TCA_POLICY_JSON_FUNCTION.apply(TEST_POLICY_JSON_STRING)
                .orElseThrow(() -> new IllegalStateException("Unable to get Test TcaPolicy"));
    }
  
    @Test
    void TestTcaAppPropertiesValidator() throws Exception {
        TcaAppProperties properties = new TcaAppProperties(environment);

        final Message message = Mockito.mock(Message.class);
        MessageHeaders header = Mockito.mock(MessageHeaders.class);


        final TcaExecutionContext testExecutionContext =
                  getTestExecutionContext(BaseTcaWebTest.fromStream("data/json/cef/cef_message.json"));

        List<TcaExecutionContext> messagePayload = new ArrayList<TcaExecutionContext>();
        messagePayload.add(testExecutionContext);

        Mockito.when(message.getPayload()).thenReturn(messagePayload);
        Mockito.when(message.getHeaders()).thenReturn(header);
        Mockito.when(AnalyticsHttpUtils.getRequestId(header)).thenReturn("resuestId-1");
        Mockito.when(AnalyticsHttpUtils.getTransactionId(header)).thenReturn("transactionId-1");
        Mockito.when(header.get(AnalyticsHttpConstants.REQUEST_BEGIN_TS_HEADER_KEY)).thenReturn(null);

        TcaAlertTransformer tcaAlertTransformer = new TcaAlertTransformer(properties);
        tcaAlertTransformer.doTransform(message);
        assertThat(message).isNotNull();
        assertThat(tcaAlertTransformer.doTransform(message)).getClass().getName().startsWith("TcaAlert");
    
    }

    protected TcaExecutionContext getTestExecutionContext(final String cefMessage) {
        final TestTcaAbatementContext testTcaAbatementContext = new TestTcaAbatementContext();
        return getTestExecutionContextBuilder(cefMessage, TEST_TCA_POLICY, testTcaAbatementContext).build();
    }

    protected GenericTcaExecutionContextBuilder getTestExecutionContextBuilder(
            final String cefMessage, final List<TcaPolicy> tcaPolicy, final TcaAbatementContext tcaAbatementContext) {

        final TcaProcessingContext tcaProcessingContext = new GenericTcaProcessingContext();
        final TcaResultContext tcaResultContext = new GenericTcaResultContext();
        final TestTcaAaiEnrichmentContext testTcaAaiEnrichmentContext = new TestTcaAaiEnrichmentContext();

        return GenericTcaExecutionContext.builder()
                .requestId(TEST_REQUEST_ID)
                .cefMessage(cefMessage)
                .tcaPolicy(tcaPolicy)
                .tcaProcessingContext(tcaProcessingContext)
                .tcaResultContext(tcaResultContext)
                .tcaAbatementContext(tcaAbatementContext)
                .tcaAaiEnrichmentContext(testTcaAaiEnrichmentContext);
    }

}
