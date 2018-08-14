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

package org.onap.dcae.analytics.tca.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Rajiv Singla
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_PROPERTIES_KEY)
@Validated
public class TcaAppProperties extends BaseConfigBindingServiceProperties {

    private static final long serialVersionUID = 1L;

    private Tca tca;

    /**
     * TCA Application properties
     */
    @Data
    public static class Tca implements ConfigBindingServiceModel {

        private static final long serialVersionUID = 1L;

        private String policy;
        private Integer processingBatchSize = TcaModelConstants.DEFAULT_TCA_PROCESSING_BATCH_SIZE;
        private Boolean enableAbatement = TcaModelConstants.DEFAULT_ABATEMENT_ENABLED;
        private Boolean enableEcompLogging = TcaModelConstants.DEFAULT_ECOMP_LOGGING_ENABLED;
        private Aai aai = new Aai();

    }


    /**
     * A&amp;AI properties
     */
    @Data
    @ToString(exclude = "password")
    public static class Aai implements ConfigBindingServiceModel {

        private static final long serialVersionUID = 1L;

        private Boolean enableEnrichment = TcaModelConstants.DEFAULT_AAI_ENRICHMENT_ENABLED;
        private String url;
        private String username;
        private String password;

        private String proxyUrl = null;
        private Boolean ignoreSSLValidation = TcaModelConstants.DEFAULT_TCA_AAI_IGNORE_SSL_VALIDATION;

        private String genericVnfPath = TcaModelConstants.DEFAULT_TCA_AAI_GENERIC_VNF_PATH;
        private String nodeQueryPath = TcaModelConstants.DEFAULT_TCA_AAI_NODE_QUERY_PATH;

    }


}
