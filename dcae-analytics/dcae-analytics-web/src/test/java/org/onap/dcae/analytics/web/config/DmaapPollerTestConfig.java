/*
 * ============LICENSE_START=======================================================
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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingAdvice;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.util.DynamicPeriodicTrigger;

import java.net.URL;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DmaapPollerTestConfig {

    @Test
    public void mrSubscriberPollingAdviceTest () throws Exception {
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

        DmaapPollerConfig dmaapPollerConfig = new DmaapPollerConfig();
        DynamicPeriodicTrigger dynamicPeriodicTrigger = new DynamicPeriodicTrigger(1L);
        MrSubscriberPollingAdvice mrSubscriberPollingAdvice = dmaapPollerConfig.mrSubscriberPollingAdvice(
                dynamicPeriodicTrigger, subscriberPreferences);
        assertNotNull(mrSubscriberPollingAdvice);
    }

    @Test
    public void dynamicPeriodicTriggerTest () throws Exception {
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

        DmaapPollerConfig dmaapPollerConfig = new DmaapPollerConfig();
        DynamicPeriodicTrigger dynamicPeriodicTrigger = dmaapPollerConfig.dynamicPeriodicTrigger(subscriberPreferences);
        assertTrue(dynamicPeriodicTrigger.isFixedRate());
    }

    @Test
    public void pollerMetadataTest () throws Exception {
        DmaapPollerConfig dmaapPollerConfig = new DmaapPollerConfig();
        DynamicPeriodicTrigger dynamicPeriodicTrigger = new DynamicPeriodicTrigger(60000);
        PollerMetadata pollerMetadata = dmaapPollerConfig.pollerMetadata(dynamicPeriodicTrigger);
        assertNotNull(pollerMetadata);
    }

    @Test
    public void defaultPollerTest () throws Exception {
        DmaapPollerConfig dmaapPollerConfig = new DmaapPollerConfig();
        PollerMetadata pollerMetadata = dmaapPollerConfig.defaultPoller();
        assertNotNull(pollerMetadata);
    }
}
