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

package org.onap.dcae.analytics.tca.web.aai;

import java.net.URL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.onap.dcae.analytics.web.http.BaseHttpClientPreferences;
import org.springframework.http.HttpHeaders;

/**
 * @author Rajiv Singla
 */
public class TcaAaiRestClientPreferences extends BaseHttpClientPreferences {

    private static final long serialVersionUID = 1L;

    public TcaAaiRestClientPreferences(@Nonnull final String requestURL,
                                       @Nullable final String httpClientId,
                                       @Nullable final HttpHeaders httpHeaders,
                                       @Nullable final String username,
                                       @Nullable final String password,
                                       @Nullable final URL proxyURL,
                                       @Nullable final Boolean ignoreSSLValidation,
                                       @Nullable final Boolean enableEcompAuditLogging) {
        super(requestURL, httpClientId, httpHeaders, username, password, proxyURL, ignoreSSLValidation,
                enableEcompAuditLogging);
    }
}
