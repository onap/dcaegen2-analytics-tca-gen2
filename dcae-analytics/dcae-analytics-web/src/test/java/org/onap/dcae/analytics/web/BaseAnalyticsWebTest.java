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

package org.onap.dcae.analytics.web;

import org.junit.jupiter.api.BeforeAll;
import org.onap.dcae.analytics.test.BaseAnalyticsUnitTest;

import java.util.HashMap;

import static org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_ENV_VARIABLE_KEY;
import static org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants.CONSUL_HOST_ENV_VARIABLE_KEY;
import static org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_KEY;

/**
 * @author Rajiv Singla
 */
public abstract class BaseAnalyticsWebTest extends BaseAnalyticsUnitTest {

    public static void initializeConfigBindingServiceEnvironmentVariables() throws Exception {
        // sets up environment variables for testing purposes
        final HashMap<String, String> testEnvironmentVariables = new HashMap<>();
        final String testConsulHostValue = "localhost";
        final String testConfigBindingService = "config_binding_service";
        final String testServiceName = "tca_dev";
        testEnvironmentVariables.put(CONSUL_HOST_ENV_VARIABLE_KEY, testConsulHostValue);
        testEnvironmentVariables.put(CONFIG_BINDING_SERVICE_ENV_VARIABLE_KEY, testConfigBindingService);
        testEnvironmentVariables.put(SERVICE_NAME_ENV_VARIABLE_KEY, testServiceName);
        setEnvironmentVariables(testEnvironmentVariables);
    }

}
