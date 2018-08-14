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

package org.onap.dcae.analytics.model.util.supplier;


import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.configbindingservice.ConsulConfigBindingServiceQueryResponse;
import org.onap.dcae.analytics.model.util.function.StringToURLFunction;
import org.onap.dcae.analytics.model.util.function.URLToHttpGetFunction;
import org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Function which fetches ApplicationProperties configuration from
 * Config Binding Service when deployed via docker, typically during application startup time.
 *
 * @author Rajiv Singla
 */
public class ConfigBindingServiceJsonSupplier implements Supplier<Optional<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigBindingServiceJsonSupplier.class);

    private final Function<String, Optional<String>> fetchUrlContentFunction;

    public ConfigBindingServiceJsonSupplier(final Function<String, Optional<String>> fetchUrlContentFunction) {
        this.fetchUrlContentFunction = fetchUrlContentFunction;
    }

    public ConfigBindingServiceJsonSupplier() {
        fetchUrlContentFunction = (String s) -> new StringToURLFunction().apply(s).flatMap(new URLToHttpGetFunction());
    }

    @Override
    public Optional<String> get() {

        LOGGER.info("Consul Host Environment Variable: {}",
                ConfigBindingServiceConstants.CONSUL_HOST_ENV_VARIABLE_VALUE);
        LOGGER.info("Config Binding Service Environment Variable: {}",
                ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_ENV_VARIABLE_VALUE);
        LOGGER.info("Service Name Environment Variable: {}",
                ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE);

        if (ConfigBindingServiceConstants.CONSUL_HOST_ENV_VARIABLE_VALUE == null ||
                ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_ENV_VARIABLE_VALUE == null ||
                ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE == null) {
            LOGGER.error("Environment variables required to query Config Binding Service are not present");
            return Optional.empty();
        }

        return Optional.of(ConfigBindingServiceConstants.CONSUL_QUERY_URL_STRING)
                // Step 1: Query CONSUL to get the IP/PORT of CONFIG BINDING SERVICE
                .flatMap(fetchUrlContentFunction)
                // Step 2: Fetch the generated configurations from CONFIG BINDING SERVICE
                .flatMap(ConfigBindingServiceJsonSupplier::parseConsulConfigBindingServiceQueryResponseJson)
                // Step 3: create url from service address and service port
                .flatMap(ConfigBindingServiceJsonSupplier::createConfigServiceURL)
                // Step 4: Fetch final config binding service generated application configuration json string
                .flatMap(fetchUrlContentFunction);
    }

    /**
     * Creates URL using config binding service ip address and port
     *
     * @param consulConfigBindingServiceQueryResponse consul config binding service query response containing config
     * binding service address and service port
     *
     * @return config service url to fetch service configuration
     */
    private static Optional<String> createConfigServiceURL(final ConsulConfigBindingServiceQueryResponse
                                                                   consulConfigBindingServiceQueryResponse) {
        final String configBindingServiceAddress = consulConfigBindingServiceQueryResponse.getServiceAddress();
        final Integer configServicePort = consulConfigBindingServiceQueryResponse.getServicePort();

        if (configBindingServiceAddress == null && configServicePort == null) {
            LOGGER.error("Config Binding Service Address & Port are not present.");
            return Optional.empty();
        }

        return Optional.of(String.format(ConfigBindingServiceConstants.CONFIG_SERVICE_QUERY_URL_STRING,
                configBindingServiceAddress, configServicePort,
                ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE));

    }

    private static Optional<ConsulConfigBindingServiceQueryResponse>
    parseConsulConfigBindingServiceQueryResponseJson(final String configBindingServiceQueryResponseJson) {
        // parse json
        final Optional<List<ConsulConfigBindingServiceQueryResponse>> configBindingServiceQueryResponseOptional =
                AnalyticsModelJsonConversion.CONFIG_BINDING_SERVICE_LIST_JSON_FUNCTION
                        .apply(configBindingServiceQueryResponseJson);

        // check parsing is successful and at least 1 config binding query response is present
        if (!configBindingServiceQueryResponseOptional.isPresent() ||
                configBindingServiceQueryResponseOptional.get().isEmpty()) {
            LOGGER.error("No Consul config binding service information found in JSON: {} ",
                    configBindingServiceQueryResponseJson);
            return Optional.empty();
        }

        // return first consul query response
        return Optional.of(configBindingServiceQueryResponseOptional.get().get(0));
    }


}
