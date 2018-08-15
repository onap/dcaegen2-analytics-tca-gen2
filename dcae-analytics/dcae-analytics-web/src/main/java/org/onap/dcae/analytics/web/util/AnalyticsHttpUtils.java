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

package org.onap.dcae.analytics.web.util;

/**
 * @author Rajiv Singla
 */

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.AnalyticsModelConstants;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.util.supplier.CreationTimestampSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;

/**
 * Provides utility methods for Analytics HTTP Operations
 *
 * @author Rajiv Singla
 */
public abstract class AnalyticsHttpUtils {

    /**
     * Creates default http headers for analytics http requests to other services like DMaaP, AAI etc with randomly
     * generated request id header
     *
     * @return default analytics http headers
     */
    public static HttpHeaders createDefaultHttpHeaders() {
        return createDefaultHttpHeaders(null);
    }

    /**
     * Creates default http headers for analytics http requests to other services like DMaaP, AAI etc
     *
     * @param requestId request id
     *
     * @return default analytics http headers
     */
    public static HttpHeaders createDefaultHttpHeaders(@Nullable final String requestId) {

        final HttpHeaders httpHeaders = new HttpHeaders();

        // set analytics from app name header.
        // Look up service name set by config service "SERVICE_NAME" environment variable or assign default value
        httpHeaders.add(
                AnalyticsHttpConstants.REQUEST_APP_NAME_HEADER_KEY,
                ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE != null ?
                        ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE :
                        AnalyticsHttpConstants.REQUEST_APP_NAME_HEADER_DEFAULT_VALUE);

        // if request id is not present create random UUID
        httpHeaders.add(AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY, requestId != null ?
                requestId : AnalyticsWebUtils.REQUEST_ID_SUPPLIER.get());

        // sub transaction id is created randomly for each http request
        httpHeaders.add(AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY,
                AnalyticsWebUtils.RANDOM_ID_SUPPLIER.get());

        // by default analytics will accept only json
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        httpHeaders.setAcceptCharset(
                Collections.singletonList(Charset.forName(AnalyticsModelConstants.UTF8_CHARSET_NAME)));

        return httpHeaders;
    }


    public static String getRequestId(final MessageHeaders messageHeaders) {
        return Optional.ofNullable(messageHeaders.get(AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY))
                .map(requestId -> (String) requestId).orElse("UNKNOWN-REQUEST_ID");
    }

    public static String getTransactionId(final MessageHeaders messageHeaders) {
        return Optional.ofNullable(messageHeaders.get(AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY))
                .map(transactionId -> (String) transactionId).orElse("UNKNOWN-TRANSACTION_ID");
    }

    public static Date getTimestampFromHeaders(final Map<String, Object> headers, final String headerTsKey) {
        return Optional.ofNullable(headers.get(headerTsKey))
                .map(ts -> CreationTimestampSupplier.getParsedDate((String) ts)).orElse(new Date());
    }

    public static String getRequestId(final HttpHeaders httpHeaders) {
        return Optional.ofNullable(
                httpHeaders.get(AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY)).map(headerList ->
                headerList.get(0)).orElse(AnalyticsWebUtils.REQUEST_ID_SUPPLIER.get());
    }

    public static String getTransactionId(final HttpHeaders httpHeaders) {
        return Optional.ofNullable(
                httpHeaders.get(AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY)).map(headerList ->
                headerList.get(0)).orElse(AnalyticsWebUtils.RANDOM_ID_SUPPLIER.get());
    }


}
