/*
 * ============LICENSE_START=======================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * Copyright (c) 2022 Huawei. All rights reserved.
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

package org.onap.dcae.analytics.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.onap.dcae.analytics.web.dmaap.MrTriggerMessageProvider;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingAdvice;
import org.onap.dcae.analytics.web.dmaap.MrMessageSplitter;
import org.onap.dcae.analytics.web.dmaap.MrPublisherPreferences;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DmaapMrTestConfig {

    @Test
    public void mrPublisherInputChannelTest () throws Exception {
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        DirectChannel directChannel = dmaapMrConfig.mrPublisherInputChannel();
        assertNotNull(directChannel);
    }

    @Test
    public void mrTriggerMessageProviderTest () throws Exception {
        URL proxyURL = new URL("http://localhost");
        MrSubscriberPollingPreferences pollingPreferences = Mockito.mock(MrSubscriberPollingPreferences.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        MrSubscriberPreferences subscriberPreferences =
                new MrSubscriberPreferences("http://localhost:8080",
                        "TestClientId", headers,
                        "TestUserName", "TestPassword",
                        proxyURL, true, false, "TestGroup",
                        Arrays.asList("TestId1"),
                        new Integer(4), new Integer(3), pollingPreferences);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        MrTriggerMessageProvider mrTriggerMessageProvider = dmaapMrConfig.mrTriggerMessageProvider(subscriberPreferences);
        assertEquals("getTriggerMessage", mrTriggerMessageProvider.TRIGGER_METHOD_NAME);
        assertEquals("http://localhost:8080/TestGroup/TestId1?limit=4&timeout=3",
                mrTriggerMessageProvider.getTriggerMessage().getPayload());
    }

    @Test
    public void mrMessageSourceTest () throws Exception {
        URL proxyURL = new URL("http://localhost");
        MrSubscriberPollingPreferences pollingPreferences = Mockito.mock(MrSubscriberPollingPreferences.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        MrSubscriberPreferences subscriberPreferences =
                new MrSubscriberPreferences("http://localhost:8080",
                        "TestClientId", headers,
                        "TestUserName", "TestPassword",
                        proxyURL, true, false, "TestGroup",
                        Arrays.asList("TestId1", "TestId2"),
                        new Integer(4), new Integer(3), pollingPreferences);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        MrTriggerMessageProvider mrTriggerMessageProvider = dmaapMrConfig.mrTriggerMessageProvider(subscriberPreferences);
        MessageSource messageSource = dmaapMrConfig.mrMessageSource(mrTriggerMessageProvider);
        assertEquals("inbound_channel_adapter", messageSource.getIntegrationPatternType().name());
    }

    @Test
    public void mrSubscriberOutputChannelTest () throws Exception {
        BasicMessageGroupStore basicMessageGroupStore = Mockito.mock(BasicMessageGroupStore.class);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        QueueChannel queueChannel = dmaapMrConfig.mrSubscriberOutputChannel(basicMessageGroupStore);
        assertTrue(queueChannel.getRemainingCapacity() > 0);
    }

    @Test
    public void mrSubscriberFlowTest () throws Exception {
        PollerMetadata pollerMetadata = Mockito.mock(PollerMetadata.class);
        QueueChannel queueChannel = Mockito.mock(QueueChannel.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        MrMessageSplitter mrMessageSplitter = Mockito.mock(MrMessageSplitter.class);
        MrSubscriberPollingAdvice mrSubscriberPollingAdvice = Mockito.mock(MrSubscriberPollingAdvice.class);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        IntegrationFlow integratedFlow = dmaapMrConfig.mrSubscriberFlow(pollerMetadata,restTemplate,messageSource,queueChannel, mrMessageSplitter, mrSubscriberPollingAdvice);
        assertNotNull(integratedFlow.getInputChannel());
    }

    @Test
    public void mrPublisherFlowTest () throws Exception {
        RequestHandlerRetryAdvice requestHandlerRetryAdvice = Mockito.mock(RequestHandlerRetryAdvice.class);
        DirectChannel directChannel = Mockito.mock(DirectChannel.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        MrPublisherPreferences mrPublisherPreferences = Mockito.mock(MrPublisherPreferences.class);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        IntegrationFlow integratedFlow = dmaapMrConfig.mrPublisherFlow(mrPublisherPreferences,restTemplate,directChannel,requestHandlerRetryAdvice);
        assertNotNull(integratedFlow.getInputChannel());
    }

    @Test
    public void mrMessageSplitterTest () throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int processingBatchSize = 100;
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        MrMessageSplitter mrMessageSplitter = dmaapMrConfig.mrMessageSplitter(objectMapper, processingBatchSize);
        assertNotNull(mrMessageSplitter);
    }
}
