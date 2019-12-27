/*-
 * ============LICENSE_START======================================================================
 * Copyright (C) 2019-2020 China Mobile.  All rights reserved.
 * ===============================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * ============LICENSE_END========================================================================
 */

package org.onap.dcae.analytics.web.config;

import java.util.Optional;

import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.web.exception.EnvironmentLoaderException;

public class SystemConfig {

	private SystemConfig() {
	}

	public static String getConsulHost() throws EnvironmentLoaderException {
        return Optional.ofNullable(ConfigBindingServiceConstants.CONSUL_HOST_ENV_VARIABLE_VALUE)
            .orElseThrow(() -> new EnvironmentLoaderException("$CONSUL_HOST environment has not been defined"));
    }

	public static Integer getConsultPort() {
        return ConfigBindingServiceConstants.DEFAULT_CONSUL_PORT_ENV_VARIABLE_VALUE;
    }

	public static String getConfigBindingService() throws EnvironmentLoaderException {
        return Optional.ofNullable(ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_ENV_VARIABLE_VALUE) //
            .orElseThrow(
                () -> new EnvironmentLoaderException("$CONFIG_BINDING_SERVICE environment has not been defined"));
    }

	public static String getService() throws EnvironmentLoaderException {
        return Optional.ofNullable(ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE)
            .orElseThrow(
                () -> new EnvironmentLoaderException("$HOSTNAME have not been defined as system environment"));
    }

}
