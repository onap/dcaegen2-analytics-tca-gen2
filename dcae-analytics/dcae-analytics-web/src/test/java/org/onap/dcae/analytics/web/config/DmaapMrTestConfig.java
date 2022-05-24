/*
 * ================================================================================
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
import org.onap.dcae.analytics.web.dmaap.*;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Arrays;

public class DmaapMrTestConfig {

    @Test
    public void mrPublisherInputChannelTest () throws Exception {
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        dmaapMrConfig.mrPublisherInputChannel();
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
                        Arrays.asList("TestId1", "TestId2"),
                        new Integer(4), new Integer(3), pollingPreferences);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        MrTriggerMessageProvider mrTriggerMessageProvider = dmaapMrConfig.mrTriggerMessageProvider(subscriberPreferences);
        System.out.println(mrTriggerMessageProvider.getTriggerMessage());
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
        System.out.println(messageSource.getIntegrationPatternType());
    }

    @Test
    public void mrSubscriberOutputChannelTest () throws Exception {
        BasicMessageGroupStore basicMessageGroupStore = Mockito.mock(BasicMessageGroupStore.class);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        QueueChannel queueChannel = dmaapMrConfig.mrSubscriberOutputChannel(basicMessageGroupStore);
        System.out.println(queueChannel.getQueueSize());
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
        dmaapMrConfig.mrSubscriberFlow(pollerMetadata,restTemplate,messageSource,queueChannel, mrMessageSplitter, mrSubscriberPollingAdvice);
    }

    @Test
    public void mrPublisherFlowTest () throws Exception {
        RequestHandlerRetryAdvice requestHandlerRetryAdvice = Mockito.mock(RequestHandlerRetryAdvice.class);
        DirectChannel directChannel = Mockito.mock(DirectChannel.class);
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        MrPublisherPreferences mrPublisherPreferences = Mockito.mock(MrPublisherPreferences.class);
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        dmaapMrConfig.mrPublisherFlow(mrPublisherPreferences,restTemplate,directChannel,requestHandlerRetryAdvice);
    }

    @Test
    public void mrMessageSplitterTest () throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        int processingBatchSize = 100;
        DmaapMrConfig dmaapMrConfig = new DmaapMrConfig();
        dmaapMrConfig.mrMessageSplitter(objectMapper, processingBatchSize);
    }
}
