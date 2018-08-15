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

package org.onap.dcae.analytics.model.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.model.configbindingservice.ConsulConfigBindingServiceQueryResponse;
import org.onap.dcae.analytics.model.util.function.JsonToJavaObjectBiFunction;

/**
 * @author Rajiv Singla
 */
public abstract class AnalyticsModelJsonConversion {

    public static final ObjectMapper ANALYTICS_MODEL_OBJECT_MAPPER = new BaseObjectMapperSupplier() {
        @Override
        public void registerCustomModules(final ObjectMapper objectMapper) {
            // do nothing
        }
    }.get();

    // Type reference to convert a list or array of Config Service Binding
    private static final TypeReference<List<ConsulConfigBindingServiceQueryResponse>>
            CONFIG_BINDING_SERVICE_LIST_TYPE_REF = new TypeReference<List<ConsulConfigBindingServiceQueryResponse>>() {
    };
    // Type reference to convert a list or array of event listener list
    private static final TypeReference<List<EventListener>> EVENT_LISTENER_LIST_TYPE_REF =
            new TypeReference<List<EventListener>>() {
            };
    // Type reference to convert single event listener
    private static final TypeReference<EventListener> EVENT_LISTENER_TYPE_REF = new TypeReference<EventListener>() {
    };


    // Event Listener Json Conversion Function
    public static final Function<String, Optional<EventListener>> EVENT_LISTENER_JSON_FUNCTION =
            new JsonToJavaObjectBiFunction<EventListener>(ANALYTICS_MODEL_OBJECT_MAPPER)
                    .curry(EVENT_LISTENER_TYPE_REF);

    // Event Listener List Json Conversion Function
    public static final Function<String, Optional<List<EventListener>>> EVENT_LISTENER_LIST_JSON_FUNCTION =
            new JsonToJavaObjectBiFunction<List<EventListener>>(ANALYTICS_MODEL_OBJECT_MAPPER)
                    .curry(EVENT_LISTENER_LIST_TYPE_REF);

    // Consul Config Binding Service Query Json Conversion Function
    public static final Function<String, Optional<List<ConsulConfigBindingServiceQueryResponse>>>
            CONFIG_BINDING_SERVICE_LIST_JSON_FUNCTION = new
            JsonToJavaObjectBiFunction<List<ConsulConfigBindingServiceQueryResponse>>(ANALYTICS_MODEL_OBJECT_MAPPER)
            .curry(CONFIG_BINDING_SERVICE_LIST_TYPE_REF);

    private AnalyticsModelJsonConversion() {
        // private constructor
    }
}
