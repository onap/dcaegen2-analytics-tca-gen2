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

package org.onap.dcae.analytics.web.spring;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.web.BaseAnalyticsWebSpringBootIT;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_PROPERTIES_KEY;

/**
 * @author Rajiv Singla
 */
@ActiveProfiles(value = {AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME}, inheritProfiles = false)
@Disabled
class ConfigBindingServiceEnvironmentPostProcessorIT extends BaseAnalyticsWebSpringBootIT {

    @BeforeAll
    static void beforeAll() throws Exception {
        BaseAnalyticsWebTest.initializeConfigBindingServiceEnvironmentVariables();
    }

    @Autowired
    private Environment environment;

    @Test
    void postProcessEnvironment() {

        final Map<String, Object> properties = new HashMap<>();
        for (final PropertySource<?> propertySource : ((AbstractEnvironment) environment).getPropertySources()) {
            if (propertySource.getName().equals(CONFIG_BINDING_SERVICE_PROPERTIES_KEY)) {
                properties.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        properties.forEach((key, value) -> logger.debug("{} -> {}", key, value));

    }

}
