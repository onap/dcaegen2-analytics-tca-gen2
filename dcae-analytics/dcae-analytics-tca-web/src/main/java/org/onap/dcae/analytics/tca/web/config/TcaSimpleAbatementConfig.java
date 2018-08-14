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

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementRepository;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.abatement.simple.SimpleAbatementContextImpl;
import org.onap.dcae.analytics.tca.web.abatement.simple.SimpleAbatementRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Rajiv Singla
 */
@Configuration
@Profile(AnalyticsProfile.NOT_MONGO_PROFILE_NAME)
public class TcaSimpleAbatementConfig {

    @Bean
    public TcaAbatementRepository simpleAbatementRepository() {
        return new SimpleAbatementRepository();
    }

    @Bean
    public TcaAbatementContext tcaAbatementContext(final TcaAppProperties tcaAppProperties,
                                                   final TcaAbatementRepository tcaAbatementRepository) {
        return new SimpleAbatementContextImpl(tcaAppProperties, tcaAbatementRepository);
    }

}
