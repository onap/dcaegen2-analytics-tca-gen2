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
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;
import org.onap.dcae.analytics.tca.web.service.TcaProcessingService;
import org.onap.dcae.analytics.tca.web.service.TcaProcessingServiceImpl;
import org.onap.dcae.analytics.tca.web.validation.TcaAppPropertiesValidator;
import org.onap.dcae.analytics.web.config.AnalyticsWebConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Rajiv Singla
 */
@Configuration
@Import(value = {AnalyticsWebConfig.class, TcaMrConfig.class, TcaAaiConfig.class,
        TcaMongoAbatementConfig.class, TcaSimpleAbatementConfig.class, SwaggerConfig.class, ControllerConfig.class})
public class TcaWebConfig {

    @Bean
    public TcaAppProperties tcaAppProperties(final Environment environment) {
        return new TcaAppProperties(environment);
    }

    @Bean
    public static Validator configurationPropertiesValidator() {
        return new TcaAppPropertiesValidator();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return TcaModelJsonConversion.TCA_OBJECT_MAPPER;
    }

    @Bean
    public TcaPolicyWrapper tcaPolicyWrapper(final TcaAppProperties tcaAppProperties) {
        return new TcaPolicyWrapper(tcaAppProperties);
    }

    @Bean
    public TcaProcessingService tcaProcessingService(final TcaAbatementContext tcaAbatementContext,
                                                     final TcaAaiEnrichmentContext tcaAaiEnrichmentContext) {
        return new TcaProcessingServiceImpl(tcaAbatementContext, tcaAaiEnrichmentContext);
    }

}
