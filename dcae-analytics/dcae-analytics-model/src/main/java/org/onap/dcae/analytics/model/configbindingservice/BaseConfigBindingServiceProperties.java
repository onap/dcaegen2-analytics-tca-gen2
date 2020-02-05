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

package org.onap.dcae.analytics.model.configbindingservice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Base call for Controller Config Binding Service Properties. Other analytics components
 * must extend this base class and add properties specific to their requirements
 * <p>NOTE: The base class supports all standard config binding service properties and additional custom properties</p>
 *
 * @author Rajiv Singla
 */
@Data
public abstract class BaseConfigBindingServiceProperties implements ConfigBindingServiceModel {

    protected Map<String, List<String>> servicesCalls = new LinkedHashMap<>();
    protected Map<String, PublisherDetails> streamsPublishes = new LinkedHashMap<>();
    protected Map<String, SubscriberDetails> streamsSubscribes = new LinkedHashMap<>();

    /**
     * Publisher and Subscriber common properties
     */
    @Data
    @ToString(exclude = "aafPassword")
    public static class PubSubCommonDetails {

        private String type;
        private String aafUsername;
        private String aafPassword;
        private DmaapInfo dmaapInfo;

        // custom additional properties
        private String proxyUrl;
        private Boolean ignoreSSLValidation;

    }

    /**
     * Publisher Details
     */
    @Getter
    @Setter
    @RequiredArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class PublisherDetails extends PubSubCommonDetails {

    }


    /**
     * Subscriber Details
     */
    @Getter
    @Setter
    @RequiredArgsConstructor
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class SubscriberDetails extends PubSubCommonDetails {

        // custom subscriber properties
        private String consumerGroup;
        private List<String> consumerIds;
        private Integer messageLimit;
        private Integer timeout;

        // custom polling configuration
        private Polling polling;
    }

    /**
     * DMaaP Info
     */
    @Data
    public static class DmaapInfo {

        private String clientRole;
        private String clientId;
        private String location;
        private String topicUrl;

    }


    /**
     * Polling Details
     */
    @Data
    public static class Polling {

        private Integer fixedRate;
        private AutoAdjusting autoAdjusting;

    }


    /**
     * Auto Adjusting Polling Details
     */
    @Data
    public static class AutoAdjusting {

        private Integer min;
        private Integer stepUp;
        private Integer max;
        private Integer stepDown;

    }


}
