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

package org.onap.dcae.analytics.model.util.json.mixin.common;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

import org.onap.dcae.analytics.model.common.BaseDynamicPropertiesProvider;
import org.onap.dcae.analytics.model.util.json.mixin.JsonMixin;

/**
 * Json Mixin for {@link BaseDynamicPropertiesProvider}
 *
 * @author Rajiv Singla
 */
public abstract class BaseDynamicPropertiesProviderMixin implements JsonMixin {

    /**
     * Provides hint to Jackson Json Object mapper to bind any put all dynamic properties in a map
     *
     * @param propertyName dynamic property name
     * @param propertyValue dynamic property value
     */
    @JsonAnySetter
    public abstract void addDynamicProperties(String propertyName, Object propertyValue);

    /**
     * Provides hint to serialize dynamic properties as map
     *
     * @return dynamic properties map
     */
    @JsonAnyGetter
    public abstract Map<String, Object> getDynamicProperties();

    /**
     * Ignores isDynamicPropertiesPresent for json serialization
     *
     * @return true if dynamic properties are present
     */
    @JsonIgnore
    public abstract boolean isDynamicPropertiesPresent();
}
