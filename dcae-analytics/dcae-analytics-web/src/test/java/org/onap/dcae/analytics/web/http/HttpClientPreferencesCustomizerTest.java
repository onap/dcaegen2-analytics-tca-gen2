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

package org.onap.dcae.analytics.web.http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.web.client.RestTemplate;

class HttpClientPreferencesCustomizerTest extends BaseAnalyticsWebTest {

    @Test
    public void customize() {

        MrSubscriberPreferences mrSubscriberPreferences = new MrSubscriberPreferences("http://tst:123");
        mrSubscriberPreferences.enableEcompAuditLogging = false;

        HttpClientPreferencesCustomizer<MrSubscriberPreferences> subscriberPreferencesHttpClientPreferencesCustomizer
                = new HttpClientPreferencesCustomizer<>(mrSubscriberPreferences);

        final RestTemplate restTemplate = new RestTemplate();
        subscriberPreferencesHttpClientPreferencesCustomizer.customize(restTemplate);

        Assertions.assertThat(restTemplate).isNotNull();

    }
}
