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

import java.io.Serializable;
import java.util.Map;

/**
 * This contract allows the deserialization mechanism to catch dynamic properties
 * in a Map so that deserialization mechanism will not loose any information and
 * can be serialized back everything without any loss in information
 *
 * @author Rajiv Singla
 */
public interface DynamicPropertiesProvider extends Serializable {


    /**
     * Adds dynamic properties in a Map object
     *
     * @param propertyName property name
     * @param propertyValue property value
     */
    void addDynamicProperties(String propertyName, Object propertyValue);

    /**
     * Provides dynamic properties map
     *
     * @return dynamic properties map object
     */
    Map<String, Object> getDynamicProperties();
}
