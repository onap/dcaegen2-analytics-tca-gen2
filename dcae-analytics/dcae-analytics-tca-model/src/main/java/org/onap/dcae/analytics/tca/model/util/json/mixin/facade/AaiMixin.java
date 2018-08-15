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

package org.onap.dcae.analytics.tca.model.util.json.mixin.facade;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.onap.dcae.analytics.model.util.json.mixin.common.BaseDynamicPropertiesProviderMixin;

/**
 * @author Rajiv Singla
 */
public abstract class AaiMixin extends BaseDynamicPropertiesProviderMixin {

    private String genericVNFName;
    private String genericServerName;

    @JsonGetter("generic-vnf.vnf-name")
    public String getGenericVNFName() {
        return genericVNFName;
    }

    @JsonSetter("generic-vnf.vnf-name")
    public void setGenericVNFName(String genericVNFName) {
        this.genericVNFName = genericVNFName;
    }

    @JsonGetter("vserver.vserver-name")
    public String getGenericServerName() {
        return genericServerName;
    }

    @JsonSetter("vserver.vserver-name")
    public void setGenericServerName(String genericServerName) {
        this.genericServerName = genericServerName;
    }
}
