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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts JSON String to flatten map of key value pair
 *
 * @author Rajiv Singla
 */
public class JsonStringToMapFunction implements Function<String, Map<String, Object>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonStringToMapFunction.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String keyPrefix;
    private final String keySeparator;

    public JsonStringToMapFunction(final String keyPrefix, final String keySeparator) {
        this.keyPrefix = keyPrefix;
        this.keySeparator = keySeparator;
    }

    public JsonStringToMapFunction(final String keyPrefix) {
        this(keyPrefix, ConfigBindingServiceConstants.KEY_SEPARATOR);
    }

    public JsonStringToMapFunction() {
        this("", ConfigBindingServiceConstants.KEY_SEPARATOR);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> apply(final String jsonString) {

        LOGGER.debug("Converting JSON to flattened key value Map: {}", jsonString);
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return new HashMap<>();
        }

        final Map<String, Object> result = flattenJson(jsonString);
        LOGGER.debug("Flattened Key Value Pairs");
        result.forEach((key, value) -> LOGGER.debug("{}:{}", key, value));
        return result;
    }

    /**
     * Flattens Json String
     *
     * @param jsonString json String
     *
     * @return map containing flattened json string key and value
     */
    private Map<String, Object> flattenJson(final String jsonString) {

        try {
            final JsonNode rootNode = OBJECT_MAPPER.readTree(jsonString);
            final LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            flattenJsonNode(keyPrefix, rootNode, result);
            return result;
        } catch (IOException e) {
            final String errorMessage = String.format("Unable to flatten JSON String to Map. Invalid JSON String: " +
                    "%s", jsonString);
            LOGGER.error(errorMessage, e, jsonString);
            throw new IllegalArgumentException(jsonString, e);
        }

    }

    /**
     * Flattens json array
     *
     * @param propertyPrefix property prefix
     * @param jsonNodesIterator json array node that needs to be flattened
     * @param resultMap result map containing flattened key value pairs
     */
    private void flattenJsonArray(String propertyPrefix, Iterator<JsonNode> jsonNodesIterator,
                                  Map<String, Object> resultMap) {
        int counter = 0;
        while (jsonNodesIterator.hasNext()) {
            flattenJsonNode(propertyPrefix + "[" + counter + "]", jsonNodesIterator.next(), resultMap);
            counter++;
        }

    }

    /**
     * Flattens json array
     *
     * @param propertyPrefix property prefix
     * @param objectNode json object node that needs to be flattened
     * @param resultMap result map containing flattened key value pairs
     */
    private void flattenJsonMap(String propertyPrefix, JsonNode objectNode,
                                Map<String, Object> resultMap) {
        final Iterator<Map.Entry<String, JsonNode>> fieldsIterator = objectNode.fields();
        while (fieldsIterator.hasNext()) {
            final Map.Entry<String, JsonNode> fieldEntry = fieldsIterator.next();
            final JsonNode jsonValue = fieldEntry.getValue();
            final String jsonKey = fieldEntry.getKey();
            flattenJsonNode(
                    (propertyPrefix.isEmpty() ? "" : propertyPrefix + keySeparator) + jsonKey,
                    jsonValue, resultMap);
        }
    }

    /**
     * Checks various elements types of JSON Types and flattens them based on their type
     *
     * @param propertyPrefix property prefix
     * @param currentJsonNode currentJsonNode that may need to be flattened
     * @param resultMap result map containing flattened key value pairs
     */
    private void flattenJsonNode(final String propertyPrefix, final JsonNode currentJsonNode,
                                 final Map<String, Object> resultMap) {


        if (currentJsonNode.isObject()) {
            flattenJsonMap(propertyPrefix, currentJsonNode, resultMap);
        } else if (currentJsonNode.isArray()) {
            flattenJsonArray(propertyPrefix, currentJsonNode.iterator(), resultMap);
        } else {
            resultMap.put(propertyPrefix, getObjectValue(currentJsonNode));
        }
    }

    private static Object getObjectValue(final JsonNode jsonNode) {
        if (jsonNode.isTextual()) {
            return jsonNode.textValue();
        } else if (jsonNode.isNumber()) {
            return jsonNode.asLong();
        } else if (jsonNode.isBoolean()) {
            return jsonNode.asBoolean();
        } else {
            return jsonNode.asText();
        }
    }
}

