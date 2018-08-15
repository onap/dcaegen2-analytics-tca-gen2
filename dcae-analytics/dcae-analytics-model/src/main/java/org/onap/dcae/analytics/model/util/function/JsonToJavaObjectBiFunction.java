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

package org.onap.dcae.analytics.model.util.function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts given Json String to Java Object if possible
 *
 * @param <T> Target Java Object class Type for JSON String
 *
 * @author Rajiv Singla
 */
public class JsonToJavaObjectBiFunction<T> implements BiFunctionExtension<TypeReference<T>, String, Optional<T>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonToJavaObjectBiFunction.class);

    private final ObjectMapper objectMapper;

    public JsonToJavaObjectBiFunction(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Optional<T> apply(final TypeReference<T> typeReference, final String jsonString) {

        try {
            return Optional.of(objectMapper.readValue(jsonString, typeReference));
        } catch (IOException e) {
            LOGGER.error("Unable to convert given JSON String to Java Object. JSON String: " + jsonString, e);
        }

        return Optional.empty();
    }

}
