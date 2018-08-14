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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.AnalyticsModelConstants;
import org.onap.dcae.analytics.model.util.supplier.RandomIdSupplier;
import org.springframework.http.HttpHeaders;

/**
 * Base implementation for {@link HttpClientPreferences}
 *
 * @author Rajiv Singla
 */
@Getter
@ToString(exclude = "password")
@EqualsAndHashCode
public abstract class BaseHttpClientPreferences implements HttpClientPreferences {

    protected String requestURL;
    protected String httpClientId;
    protected HttpHeaders requestHeaders;
    protected String username;
    protected String password;
    protected URL proxyURL;
    protected Boolean ignoreSSLValidation;
    protected Boolean enableEcompAuditLogging;

    public BaseHttpClientPreferences(@Nonnull final String requestURL) {
        this.requestURL = requestURL;
    }

    public BaseHttpClientPreferences(@Nonnull final String requestURL,
                                     @Nullable final String httpClientId,
                                     @Nullable final HttpHeaders httpHeaders,
                                     @Nullable final String username,
                                     @Nullable final String password,
                                     @Nullable final URL proxyURL,
                                     @Nullable final Boolean ignoreSSLValidation,
                                     @Nullable final Boolean enableEcompAuditLogging) {
        this.requestURL = requestURL;
        // create http client id if not present
        this.httpClientId = httpClientId != null ? httpClientId :
                AnalyticsHttpConstants.DEFAULT_HTTP_CLIENT_ID_PREFIX +
                        new RandomIdSupplier(AnalyticsModelConstants.DEFAULT_RANDOM_ID_LENGTH);
        this.requestHeaders = httpHeaders;
        this.username = username;
        this.password = password;
        this.proxyURL = proxyURL;
        this.ignoreSSLValidation = ignoreSSLValidation != null && ignoreSSLValidation;
        this.enableEcompAuditLogging = enableEcompAuditLogging != null && enableEcompAuditLogging;
    }

}
