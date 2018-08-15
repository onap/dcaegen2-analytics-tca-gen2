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

package org.onap.dcae.analytics.model.common;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base Dynamic Provider provide functionality so that all the
 * additional dynamic Properties can be accumulated in a map.
 *
 * @author Rajiv Singla
 */
@Data
public abstract class BaseDynamicPropertiesProvider implements DynamicPropertiesProvider {

    /**
     * All non-required properties should be captured in additional properties
     */
    private Map<String, Object> dynamicProperties = new LinkedHashMap<>();


    /**
     * Add a dynamic property to Common Event Format Entity
     *
     * @param propertyName property name
     * @param propertyValue property value
     */
    @Override
    public void addDynamicProperties(String propertyName, Object propertyValue) {
        dynamicProperties.put(propertyName, propertyValue);
    }

    /**
     * Determines if dynamic properties are present for the CEF Entity
     *
     * @return return true if Dynamic Properties are present
     */
    public boolean isDynamicPropertiesPresent() {
        return dynamicProperties.size() == 0;
    }


}
