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

import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentService;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.util.function.TcaAppPropsToAaiRestClientPrefsFunction;
import org.onap.dcae.analytics.web.http.HttpClientPreferencesCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * @author Rajiv Singla
 */
public class TcaAaiEnrichmentContextImpl implements TcaAaiEnrichmentContext {

    private final TcaAppProperties tcaAppProperties;
    private final RestTemplateBuilder restTemplateBuilder;

    public TcaAaiEnrichmentContextImpl(final TcaAppProperties tcaAppProperties,
            final RestTemplateBuilder restTemplateBuilder) {
        this.tcaAppProperties = tcaAppProperties;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public boolean isAaiEnrichmentEnabled() {
        return tcaAppProperties.getTca().getAai().getEnableEnrichment();
    }

    @Override
    public TcaAaiEnrichmentService getAaiEnrichmentService() {
        TcaAaiRestClientPreferences aaiRestClientPreferences = new TcaAppPropsToAaiRestClientPrefsFunction()
                .apply(tcaAppProperties);
        if (aaiRestClientPreferences == null) {
            return null;
        }
        RestTemplate aaiRestTemplate = restTemplateBuilder
                .additionalCustomizers(new HttpClientPreferencesCustomizer<>(aaiRestClientPreferences)).build();
        return new TcaAaiEnrichmentServiceImpl(tcaAppProperties, aaiRestTemplate);
    }
}
