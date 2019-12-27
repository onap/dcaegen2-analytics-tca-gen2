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

package org.onap.dcae.analytics.model.configbindingservice;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.PubSubCommonDetails;

/**
 * @author Rajiv Singla
 */
public abstract class ConfigBindingServiceConstants {

    // ================== CONFIG SERVICE CONSTANTS ============================== //
    public static final String CONSUL_HOST_ENV_VARIABLE_KEY = "CONSUL_HOST";
    public static final Integer DEFAULT_CONSUL_PORT_ENV_VARIABLE_VALUE = 8500;
    public static final String CONSUL_HOST_ENV_VARIABLE_VALUE = System.getenv(CONSUL_HOST_ENV_VARIABLE_KEY);
    public static final String CONFIG_BINDING_SERVICE_ENV_VARIABLE_KEY = "CONFIG_BINDING_SERVICE";
    public static final String CONFIG_BINDING_SERVICE_ENV_VARIABLE_VALUE =
            System.getenv(CONFIG_BINDING_SERVICE_ENV_VARIABLE_KEY);
    public static final String SERVICE_NAME_ENV_VARIABLE_KEY = "HOSTNAME";
    public static final String SERVICE_NAME_ENV_VARIABLE_VALUE = System.getenv(SERVICE_NAME_ENV_VARIABLE_KEY);
    public static final String CONSUL_QUERY_URL_STRING = String.format("http://%s:8500/v1/catalog/service/%s",
            CONSUL_HOST_ENV_VARIABLE_VALUE, CONFIG_BINDING_SERVICE_ENV_VARIABLE_VALUE);
    public static final String CONFIG_SERVICE_QUERY_URL_STRING = "http://%s:%s/service_component/%s";


    public static final String CONFIG_BINDING_SERVICE_PROPERTIES_KEY = "config-binding-service";

    public static final Set<String> SPRING_RESERVED_PROPERTIES_KEY_PREFIXES =
            Stream.of("spring", "endpoints", "server", "logging", "management").collect(Collectors.toSet());
    public static final String CONFIG_SERVICE_MESSAGE_ROUTER_VALUE = "message_router";
    // CONVERT JSON TO MAP
    public static final String KEY_SEPARATOR = ".";
    public static final String CONFIG = "config";
    public static final int CONFIG_SERVICE_REFRESHPERIOD = 1;

    // ============== CONFIG BINDING SERVICE UTILS ========================= //
    /**
     * Predicate which can be used to filter message router publisher or subscriber details
     */
    public static final Predicate<Map.Entry<String, ? extends PubSubCommonDetails>> MESSAGE_ROUTER_PREDICATE =
            e -> e.getValue().getType() != null &&
                    e.getValue().getType().equalsIgnoreCase(CONFIG_SERVICE_MESSAGE_ROUTER_VALUE);

    private ConfigBindingServiceConstants() {
        // private constructor
    }
}
