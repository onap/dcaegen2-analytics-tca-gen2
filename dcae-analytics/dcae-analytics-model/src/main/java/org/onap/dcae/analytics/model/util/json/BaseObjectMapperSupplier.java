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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;

import org.onap.dcae.analytics.model.util.json.module.CommonEventFormatModule;
import org.onap.dcae.analytics.model.util.json.module.ConfigBindingServiceModule;
import org.onap.dcae.analytics.model.util.json.module.DynamicPropertiesModule;

/**
 * Base Object mapper supplies Jackson {@link ObjectMapper} that specializes in serialize and deserialize -
 * Analytics model objects. Various analytics components should inherit from this supplier and register
 * their custom modules
 *
 * @author Rajiv Singla
 */
public abstract class BaseObjectMapperSupplier implements Supplier<ObjectMapper> {

    /**
     * Class that can used to configure Json Path configuration
     */
    public static class JsonPathConfiguration implements Configuration.Defaults {

        private final JsonProvider jsonProvider;
        private final MappingProvider mappingProvider;
        private final Set<Option> options;

        private JsonPathConfiguration(final ObjectMapper objectMapper, final Set<Option> options) {
            jsonProvider = new JacksonJsonProvider(objectMapper);
            mappingProvider = new JacksonMappingProvider(objectMapper);
            this.options = options;
        }


        @Override
        public JsonProvider jsonProvider() {
            return jsonProvider;
        }

        @Override
        public Set<Option> options() {
            return options;
        }

        @Override
        public MappingProvider mappingProvider() {
            return mappingProvider;
        }
    }

    public abstract void registerCustomModules(final ObjectMapper objectMapper);

    @Override
    public ObjectMapper get() {

        final ObjectMapper objectMapper = new ObjectMapper();

        //  Ignore null values during serialization. Null values will not be included in serialized JSON object
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Don't fail on unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // register dynamic properties module
        objectMapper.registerModule(new DynamicPropertiesModule());
        // register config binding service module
        objectMapper.registerModule(new ConfigBindingServiceModule());
        // register common event format module
        objectMapper.registerModule(new CommonEventFormatModule());

        // register custom modules
        registerCustomModules(objectMapper);

        // Setup JsonPath default config
        setupJsonPathDefaultConfig(objectMapper);

        return objectMapper;
    }

    /**
     * Setups up default Config for Json Path
     *
     * @param objectMapper Jackson object mapper
     */
    private void setupJsonPathDefaultConfig(final ObjectMapper objectMapper) {

        Configuration.setDefaults(new JsonPathConfiguration(objectMapper, EnumSet.of(
                Option.DEFAULT_PATH_LEAF_TO_NULL,  // missing properties are tolerated
                Option.SUPPRESS_EXCEPTIONS, // Json Path exceptions are suppressed
                Option.ALWAYS_RETURN_LIST // always return results as list
                ))
        );

    }

}
