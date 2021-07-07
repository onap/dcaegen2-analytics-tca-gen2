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

package org.onap.dcae.analytics.model;

/**
 * Captures various standard Analytics HTTP Request Headers
 *
 * @author Rajiv Singla
 */
public abstract class AnalyticsHttpConstants {

    public static final String REQUEST_ID_HEADER_KEY = "X-RequestID";
    public static final String REQUEST_TRANSACTION_ID_HEADER_KEY = "X-TransactionID";
    public static final String REQUEST_APP_NAME_HEADER_KEY = "X-FromAppID";
    public static final String REQUEST_BEGIN_TS_HEADER_KEY = "X-Begin-Timestamp";
    public static final String REQUEST_END_TS_HEADER_KEY = "X-End-Timestamp";

    public static final String REQUEST_APP_NAME_HEADER_DEFAULT_VALUE = "DCAE-Analytics";

    // DEFAULT HTTP CLIENT ID
    public static final String DEFAULT_HTTP_CLIENT_ID_PREFIX = "DCAE-Analytics-Client-";

    public static final String HTTP_STATUS_CODE_HEADER_KEY = "http_statusCode";

    private AnalyticsHttpConstants() {
        // private constructor
    }
}
