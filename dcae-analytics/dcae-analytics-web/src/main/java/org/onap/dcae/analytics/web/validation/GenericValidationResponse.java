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

package org.onap.dcae.analytics.web.validation;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A generic implementation of Validation Response
 *
 * @author Rajiv Singla
 */
@ToString
@EqualsAndHashCode
public class GenericValidationResponse implements ValidationResponse {

    private static final String DEFAULT_DELIMITER = ",";

    private LinkedHashMap<String, String> errorMessageMap = new LinkedHashMap<>();

    @Override
    public boolean hasErrors() {
        return !errorMessageMap.isEmpty();
    }

    @Override
    public Set<String> getFieldNamesWithError() {
        return errorMessageMap.keySet();
    }

    @Override
    public Collection<String> getErrorMessages() {
        return errorMessageMap.values();
    }

    @Override
    public Map<String, String> getValidationResultsAsMap() {
        return errorMessageMap;
    }

    @Override
    public String getAllErrorMessage() {
        return getAllErrorMessage(DEFAULT_DELIMITER);
    }

    @Override
    public String getAllErrorMessage(String delimiter) {
        return errorMessageMap.values()
                .stream().collect(Collectors.joining(delimiter));
    }

    @Override
    public void addErrorMessage(String fieldName, String filedErrorMessage) {
        errorMessageMap.put(fieldName, filedErrorMessage);
    }

}
