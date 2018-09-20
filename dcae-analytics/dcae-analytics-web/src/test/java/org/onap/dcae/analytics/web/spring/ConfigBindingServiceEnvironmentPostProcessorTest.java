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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcae.analytics.web.exception.AnalyticsValidationException;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

class ConfigBindingServiceEnvironmentPostProcessorTest extends BaseAnalyticsWebTest {

    @BeforeAll
    static void beforeAll() throws Exception {
        BaseAnalyticsWebTest.initializeConfigBindingServiceEnvironmentVariables();
    }


    @Test
    void postProcessEnvironment() {

        Assertions.assertThrows(AnalyticsValidationException.class, () -> {

            final ConfigBindingServiceEnvironmentPostProcessor configBindingServiceEnvironmentPostProcessor =
                    new ConfigBindingServiceEnvironmentPostProcessor();

            final ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
            final SpringApplication springApplication = Mockito.mock(SpringApplication.class);
            final String[] activeProfiles = {AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME};
            Mockito.when(configurableEnvironment.getActiveProfiles()).thenReturn(activeProfiles);

            configBindingServiceEnvironmentPostProcessor
                    .postProcessEnvironment(configurableEnvironment, springApplication);
        });
    }
}
