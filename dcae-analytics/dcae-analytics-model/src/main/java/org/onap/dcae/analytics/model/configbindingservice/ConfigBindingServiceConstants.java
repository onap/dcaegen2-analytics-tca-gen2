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
    public static final String CONFIG_BINDING_SERVICE_ENV_VARIABLE_KEY = "CONFIG_BINDING_SERVICE";
    public static final String SERVICE_NAME_ENV_VARIABLE_KEY = "HOSTNAME";
    public static final String SERVICE_NAME_ENV_VARIABLE_VALUE = System.getenv(SERVICE_NAME_ENV_VARIABLE_KEY);
    public static final String ENV_CBS_PORT = "CONFIG_BINDING_SERVICE_SERVICE_PORT";
    public static final String CONFIG_BINDING_SERVICE_PROPERTIES_KEY = "config-binding-service";
    public static final String SPRING_CONFIG_BINDING_SERVICE_PROPERTIES_KEY = "config-binding-service.config";
    public static final String CONFIG_SERVICE_MESSAGE_ROUTER_VALUE = "message_router";
    // CONVERT JSON TO MAP
    public static final String KEY_SEPARATOR = ".";
    public static final String CONFIG = "config";
    public static final String TCAPOLICY = "tca.policy";
    public static final String ITEMS = "items";
    public static final int CONFIG_SERVICE_REFRESHPERIOD = 1;

    public static final String POLICY = "config-binding-service.config.tca.policy";
    public static final String POLICIES = "config-binding-service.policies";
    public static final String PROCESSINGBATCHSIZE = "config-binding-service.config.tca.processing_batch_size";
    public static final String ENABLEABATEMENT = "config-binding-service.config.tca.enable_abatement";
    public static final String ENABLEECOMPLOGGING = "config-binding-service.config.tca.enable_ecomp_logging";
    public static final String ENABLEENRICHMENT = "config-binding-service.config.tca.aai.enable_enrichment";
    public static final String AAIURL = "config-binding-service.config.tca.aai.url";
    public static final String AAIUSERNAME = "config-binding-service.config.tca.aai.username";
    public static final String AAIPASSWORD = "config-binding-service.config.tca.aai.password";
    public static final String AAIGENERICVNFPATH = "config-binding-service.config.tca.aai.generic_vnf_path";
    public static final String AAINODEQUERYPATH = "config-binding-service.config.tca.aai.node_query_path";
    public static final String PUBTOPICURL = "config-binding-service.config.streams_publishes.tca_handle_out.dmaap_info.topic_url";
    public static final String PUBTYPE = "config-binding-service.config.streams_publishes.tca_handle_out.type";
    public static final String PUBKEY = "tca_handle_out";
    public static final String SUBAUTOADJUSTINGSTEPUP = "config-binding-service.config.streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up";
    public static final String SUBAUTOADJUSTINGSTEPDOWN = "config-binding-service.config.streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down";
    public static final String SUBAUTOADJUSTINGMAX = "config-binding-service.config.streams_subscribes.tca_handle_in.polling.auto_adjusting.max";
    public static final String SUBAUTOADJUSTINGMIN = "config-binding-service.config.streams_subscribes.tca_handle_in.polling.auto_adjusting.min";
    public static final String SUBTOPICURL = "config-binding-service.config.streams_subscribes.tca_handle_in.dmaap_info.topic_url";
    public static final String SUBFIXEDRATE = "config-binding-service.config.streams_subscribes.tca_handle_in.polling.fixed_rate";
    public static final String SUBTYPE = "config-binding-service.config.streams_subscribes.tca_handle_in.type";
    public static final String SUBCONSUMERGROUP = "config-binding-service.config.streams_subscribes.tca_handle_in.consumer_group";
    public static final String SUBMESSAGELIMIT = "config-binding-service.config.streams_subscribes.tca_handle_in.message_limit";
    public static final String SUBTIMEOUT = "config-binding-service.config.streams_subscribes.tca_handle_in.timeout";
    public static final String SUBCONSUMERIDS0 = "config-binding-service.config.streams_subscribes.tca_handle_in.consumer_ids[0]";
    public static final String SUBCONSUMERIDS1 = "config-binding-service.config.streams_subscribes.tca_handle_in.consumer_ids[1]";
    public static final String SUBKEY = "tca_handle_in";

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

    /**
     * getSpringReservedPropertiesKeyPrefixes
     */
    public static final Set<String> getSpringReservedPropertiesKeyPrefixes() {
        return Stream.of("spring", "endpoints", "server", "logging", "management").collect(Collectors.toSet());
    }
}
