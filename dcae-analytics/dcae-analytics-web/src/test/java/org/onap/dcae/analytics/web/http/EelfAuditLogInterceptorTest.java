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

import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;

class EelfAuditLogInterceptorTest extends BaseAnalyticsWebTest {

    @Test
    void intercept() throws Exception {
        MrSubscriberPreferences mrSubscriberPreferences = new MrSubscriberPreferences("http://tst:123");
        mrSubscriberPreferences.enableEcompAuditLogging = true;

        final EelfAuditLogInterceptor eelfAuditLogInterceptor = new EelfAuditLogInterceptor(mrSubscriberPreferences);

        HttpRequest httpRequest = new MockClientHttpRequest();
        eelfAuditLogInterceptor.intercept(httpRequest, "test".getBytes(), new ClientHttpRequestExecution() {
            @Override
            public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
                return new MockClientHttpResponse("OK".getBytes(), HttpStatus.OK);
            }
        });

    }
}
