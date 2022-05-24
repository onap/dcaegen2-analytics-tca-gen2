/*
 * ================================================================================
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

package org.onap.dcae.analytics.web.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.MessageHeaders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsHttpUtilsTest {

    @Test
    public void createDefaultHttpHeadersTest () throws Exception {
        HttpHeaders httpHeaders = AnalyticsHttpUtils.createDefaultHttpHeaders();
        String transactionId = AnalyticsHttpUtils.getTransactionId(httpHeaders);
    }

    @Test
    public void createDefaultHttpHeadersRequestIDTest () throws Exception {
        HttpHeaders httpHeaders = AnalyticsHttpUtils.createDefaultHttpHeaders("lklkjl");
        String requestId = AnalyticsHttpUtils.getRequestId(httpHeaders);
    }

    @Test
    public void getTransactionIdTest () throws Exception {
        MessageHeaders httpHeaders = new MessageHeaders(null);
        String requestId = AnalyticsHttpUtils.getTransactionId(httpHeaders);
    }

    @Test
    public void getRequestIdTest () throws Exception {
        MessageHeaders httpHeaders = new MessageHeaders(null);
        String requestId = AnalyticsHttpUtils.getRequestId(httpHeaders);
    }

    @Test
    public void getTimestampFromHeadersTest () throws Exception {
        Map<String, Object> headers = new HashMap<String, Object>();
        Date dt = AnalyticsHttpUtils.getTimestampFromHeaders(headers, null);
    }
}
