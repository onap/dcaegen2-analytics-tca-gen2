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
