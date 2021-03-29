/*
 * ================================================================================
 * Copyright (c) 2019 IBM Intellectual Property. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.TcaAppProperties.Tca;
import org.springframework.messaging.MessageHeaders;

public class TcaPublisherResponseHandlerTest {

    static MessageHeaders messageHeaders;

    @BeforeAll
    static void initialize() {
        Map headers = new HashMap<>();
        headers.put("X-ECOMP-RequestID", "TestRequestID");
        headers.put("X-ECOMP-TransactionID", "TestTransactionID");
        headers.put("X-ECOMP-FromAppID", "TestFromAppID");
        messageHeaders = new MessageHeaders(headers);

    }

    @Test
    void getHandleLoggingEnabledTest() throws Exception {

        TcaAppProperties tcaAppProperties = Mockito.mock(TcaAppProperties.class);
        Tca tca = Mockito.mock(Tca.class);
        Mockito.when(tcaAppProperties.getTca()).thenReturn(tca);
        Mockito.when(tcaAppProperties.getTca().getEnableEcompLogging()).thenReturn(true);

        TcaPublisherResponseHandler responseHandler = new TcaPublisherResponseHandler(tcaAppProperties);
        responseHandler.handle("testpayload", messageHeaders);

    }

    @Test
    void getHandleLoggingNotEnabledTest() throws Exception {

        TcaAppProperties tcaAppProperties = Mockito.mock(TcaAppProperties.class);
        Tca tca = Mockito.mock(Tca.class);
        Mockito.when(tcaAppProperties.getTca()).thenReturn(tca);
        Mockito.when(tcaAppProperties.getTca().getEnableEcompLogging()).thenReturn(false);

        TcaPublisherResponseHandler responseHandler = new TcaPublisherResponseHandler(tcaAppProperties);
        responseHandler.handle("testpayload", messageHeaders);

    }

}

