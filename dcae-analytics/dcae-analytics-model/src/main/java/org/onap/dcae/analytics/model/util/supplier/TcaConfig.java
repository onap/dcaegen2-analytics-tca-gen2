/*-
 * ============LICENSE_START======================================================================
 * Copyright (C) 2019 China Mobile.
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

package org.onap.dcae.analytics.model.util.supplier;

import java.time.Duration;
import java.util.Optional;

import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsClient;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsClientFactory;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsRequests;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.CbsRequest;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.EnvProperties;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.ImmutableEnvProperties;
import org.onap.dcaegen2.services.sdk.rest.services.model.logging.RequestDiagnosticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * period fetch configuration from CBS.
 *
 * @author Kai Lu
 *
 */

@Component
@ComponentScan("org.onap.dcaegen2.services.sdk.rest.services.cbs.client.providers")
public class TcaConfig {
    private static final Logger logger = LoggerFactory.getLogger(TcaConfig.class);

    private Disposable refreshConfigTask = null;

    private Optional<String> config;

    /**
     *
     * Reads the configuration.
     *
     */
    public void initialize() {
    	stop();

        refreshConfigTask = createRefreshTask() //
            .subscribe(e -> logger.info("Refreshed configuration data"),
                throwable -> logger.error("Configuration refresh terminated due to exception", throwable),
                () -> logger.error("Configuration refresh terminated"));
    }

    /**
     * Reads the configuration task from CBS.
     *
     */
    private Flux<TcaConfig> createRefreshTask() {
        return readEnvironmentVariables() //
            .flatMap(this::createCbsClient) //
            .flatMapMany(this::periodicConfigurationUpdates) //
            .map(this::parseTcaConfig) //
            .onErrorResume(this::onErrorResume);
    }

    /**
     * periodicConfigurationUpdates.
     *
     * @param cbsClient cbsClient
     * @return configuration refreshed
     *
     */
    private Flux<JsonObject> periodicConfigurationUpdates(CbsClient cbsClient) {
        final Duration initialDelay = Duration.ZERO;
        final Duration refreshPeriod = Duration.ofMinutes(1);
        final CbsRequest getConfigRequest = CbsRequests.getAll(RequestDiagnosticContext.create());
        return cbsClient.updates(getConfigRequest, initialDelay, refreshPeriod);
    }

    /**
     *
     * get environment variables.
     * @return environment properties.
     *
     */
    private static Mono<EnvProperties> readEnvironmentVariables() {
        logger.trace("Loading configuration from system environment variables");
        EnvProperties envProperties;
        envProperties = ImmutableEnvProperties.builder() //
		    .consulHost(ConfigBindingServiceConstants.CONSUL_HOST_ENV_VARIABLE_VALUE) //
		    .consulPort(ConfigBindingServiceConstants.CONSUL_PORT_ENV_VARIABLE_VALUE) //
		    .cbsName(ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_ENV_VARIABLE_VALUE) //
		    .appName(ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE) //
		    .build();
        logger.trace("Evaluated environment system variables {}", envProperties);
        return Mono.just(envProperties);
    }

    /**
     * Stops the refreshing of the configuration.
     *
     */
    public void stop() {
        if (refreshConfigTask != null) {
            refreshConfigTask.dispose();
            refreshConfigTask = null;
        }
    }

    /**
     * periodicConfigurationUpdates.
     *
     * @param throwable throwable
     * @return Mono
     *
     */
    private <R> Mono<R> onErrorResume(Throwable throwable) {
        logger.error("Could not refresh application configuration {}", throwable.toString());
        return Mono.empty();
    }


    /**
     * create CbsClient.
     *
     * @param env environment properties
     * @return cbsclient
     *
     */
    Mono<CbsClient> createCbsClient(EnvProperties env) {
        return CbsClientFactory.createCbsClient(env);
    }

    /**
     * Parse configuration.
     *
     * @param jsonObject the TCA service's configuration
     * @return this
     *
     */
    private TcaConfig parseTcaConfig(JsonObject jsonObject) {
    	config = Optional.of(jsonObject.getAsString());
        return this;
    }

    /**
     *
     * Get configuration.
     *
     * @return configuration
     *
     */
    public synchronized Optional<String> getConfiguration() {
        return config;
    }
}
