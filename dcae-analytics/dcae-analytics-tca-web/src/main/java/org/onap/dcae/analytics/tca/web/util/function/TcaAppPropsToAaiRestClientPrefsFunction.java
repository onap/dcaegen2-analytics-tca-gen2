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

package org.onap.dcae.analytics.tca.web.util.function;

import java.util.Optional;
import java.util.function.Function;

import org.onap.dcae.analytics.model.util.function.StringToURLFunction;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.aai.TcaAaiRestClientPreferences;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.springframework.http.HttpHeaders;

/**
 * @author Rajiv Singla
 */
public class TcaAppPropsToAaiRestClientPrefsFunction implements Function<TcaAppProperties,
        TcaAaiRestClientPreferences> {

    @Override
    public TcaAaiRestClientPreferences apply(final TcaAppProperties tcaAppProperties) {

        final TcaAppProperties.Aai aai = tcaAppProperties.getTca().getAai();

        // if aai enrichment is not enabled no need to configure aai Rest client template
//        if (!tcaAppProperties.getTca().getAai().getEnableEnrichment()) {
//            return null;
//        }

        final HttpHeaders aaiHeaders = AnalyticsHttpUtils.createDefaultHttpHeaders();
        aaiHeaders.add("Real-Time", "true");

        return new TcaAaiRestClientPreferences(
                tcaAppProperties.getTca().getAai().getUrl(),
                "aai-rest-client",
                aaiHeaders,
                aai.getUsername(),
                aai.getPassword(),
                Optional.ofNullable(aai.getProxyUrl()).flatMap(new StringToURLFunction()).orElse(null),
                aai.getIgnoreSSLValidation(),
                tcaAppProperties.getTca().getEnableEcompLogging()
        );
    }
}
