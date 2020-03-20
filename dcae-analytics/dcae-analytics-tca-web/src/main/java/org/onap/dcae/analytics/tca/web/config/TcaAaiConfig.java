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

package org.onap.dcae.analytics.tca.web.config;

import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentService;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.aai.TcaAaiEnrichmentContextImpl;
import org.onap.dcae.analytics.tca.web.aai.TcaAaiEnrichmentServiceImpl;
import org.onap.dcae.analytics.tca.web.aai.TcaAaiRestClientPreferences;
import org.onap.dcae.analytics.tca.web.util.function.TcaAppPropsToAaiRestClientPrefsFunction;
import org.onap.dcae.analytics.web.http.HttpClientPreferencesCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Rajiv Singla
 */
@Configuration
public class TcaAaiConfig {

    @Bean
    public TcaAaiRestClientPreferences aaiRestClientPreferences(final TcaAppProperties tcaAppProperties) {
        return new TcaAppPropsToAaiRestClientPrefsFunction().apply(tcaAppProperties);
    }

    @Bean
    public RestTemplate aaiRestTemplate(final TcaAaiRestClientPreferences aaiRestClientPreferences,
                                        final RestTemplateBuilder restTemplateBuilder) {
        if(aaiRestClientPreferences == null) {
            return null;
        }
        return restTemplateBuilder
                .additionalCustomizers(new HttpClientPreferencesCustomizer<>(aaiRestClientPreferences))
                .build();
    }

    @Bean
    public TcaAaiEnrichmentService aaiEnrichmentService(final TcaAppProperties tcaAppProperties,
                                                        final RestTemplate aaiRestTemplate) {
        if (aaiRestTemplate == null) {
            return null;
        }
        return new TcaAaiEnrichmentServiceImpl(tcaAppProperties, aaiRestTemplate);
    }


    @Bean
    public TcaAaiEnrichmentContext tcaAaiEnrichmentContext(final TcaAppProperties tcaAppProperties,
                                                           final TcaAaiEnrichmentService aaiEnrichmentService) {
        return new TcaAaiEnrichmentContextImpl(tcaAppProperties, aaiEnrichmentService);
    }

}
