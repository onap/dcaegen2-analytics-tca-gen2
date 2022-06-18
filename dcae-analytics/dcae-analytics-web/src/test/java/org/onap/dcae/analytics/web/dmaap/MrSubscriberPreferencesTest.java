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

package org.onap.dcae.analytics.web.dmaap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;

import java.net.URL;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MrSubscriberPreferencesTest {

    @Test
    public void mrSubscriberPollingPreferencesTest () throws Exception {
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

        MrSubscriberPollingPreferences mrSubscriberPollingPreferences = subscriberPreferences.getPollingPreferences();
        assertNotNull(mrSubscriberPollingPreferences);
    }

    @Test
    public void mrSubscriberPollingPreferences1Test () throws Exception {
        URL proxyURL = new URL("http://localhost");
        MrSubscriberPollingPreferences pollingPreferences = Mockito.mock(MrSubscriberPollingPreferences.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        MrSubscriberPreferences subscriberPreferences =
                new MrSubscriberPreferences("http://localhost:8080",
                        "TestClientId", headers,
                        "TestUserName", "TestPassword",
                        proxyURL, true, false, "TestGroup",
                        Arrays.asList("TestId1", "TestId2"),
                        new Integer(4), new Integer(3), null);

        MrSubscriberPollingPreferences mrSubscriberPollingPreferences = subscriberPreferences.getPollingPreferences();
        assertNotNull(mrSubscriberPollingPreferences);
    }
}
