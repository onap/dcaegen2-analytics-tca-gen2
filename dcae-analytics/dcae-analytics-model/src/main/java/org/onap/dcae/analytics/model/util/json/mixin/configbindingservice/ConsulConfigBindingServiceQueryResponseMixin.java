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

package org.onap.dcae.analytics.model.util.json.mixin.configbindingservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

import org.onap.dcae.analytics.model.util.json.mixin.common.BaseDynamicPropertiesProviderMixin;

/**
 * @author Rajiv Singla
 */
public abstract class ConsulConfigBindingServiceQueryResponseMixin extends BaseDynamicPropertiesProviderMixin {

    @JsonProperty("ID")
    private String id;
    @JsonProperty("Node")
    private String node;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Datacenter")
    private String dataCenter;
    @JsonProperty("TaggedAddresses")
    private Map<String, String> taggedAddresses;
    @JsonProperty("NodeMeta")
    private Map<String, String> nodeMeta;
    @JsonProperty("ServiceID")
    private String serviceId;
    @JsonProperty("ServiceName")
    private String serviceName;
    @JsonProperty("ServiceTags")
    private List<String> serviceTags;
    @JsonProperty("ServiceAddress")
    private String serviceAddress;
    @JsonProperty("ServicePort")
    private Integer servicePort;
    @JsonProperty("ServiceEnableTagOverride")
    private Boolean serviceEnableTagOverride;
    @JsonProperty("CreateIndex")
    private long createIndex;
    @JsonProperty("ModifyIndex")
    private long modifyIndex;

}
