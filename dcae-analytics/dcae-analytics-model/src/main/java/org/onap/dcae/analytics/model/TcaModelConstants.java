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

package org.onap.dcae.analytics.model;

import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;

/**
 * @author Rajiv Singla
 */
public abstract class TcaModelConstants {

    // =================== TCA CONSTANTS =========================== //

    // TCA Alert - VNF Constants
    public static final String TCA_ALERT_VNF_TARGET_TYPE = "VNF";
    public static final String AAI_VNF_KEY_PREFIX = "generic-vnf.";
    public static final String TCA_ALERT_VNF_TARGET = AAI_VNF_KEY_PREFIX + "vnf-name";
    // TCA Alert - VM Constants
    public static final String TCA_ALERT_VM_TARGET_TYPE = "VM";
    public static final String AAI_VSERVER_KEY_PREFIX = "vserver.";
    public static final String TCA_ALERT_VM_TARGET = AAI_VSERVER_KEY_PREFIX + "vserver-name";
    // VNF & VM - Common Constants
    public static final String TCA_VES_RESPONSE_FROM = "DCAE";
    public static final String TCA_SERVICE_NAME =
            ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE != null ?
                    ConfigBindingServiceConstants.SERVICE_NAME_ENV_VARIABLE_VALUE : "DCAE-TCA";


    // ================= PERSISTENCE CONSTANTS ================== //
    public static final String TCA_ROW_KEY_DELIMITER = ":";


    // ======================= DEFAULTS ====================== //
    public static final boolean DEFAULT_AAI_ENRICHMENT_ENABLED = false;
    public static final boolean DEFAULT_ABATEMENT_ENABLED = true;
    public static final boolean DEFAULT_ECOMP_LOGGING_ENABLED = true;
    public static final Integer DEFAULT_TCA_PROCESSING_BATCH_SIZE = 10_000;

    public static final boolean DEFAULT_TCA_AAI_IGNORE_SSL_VALIDATION = false;
    public static final String DEFAULT_TCA_AAI_GENERIC_VNF_PATH = "aai/v11/network/generic-vnfs/generic-vnf";
    public static final String DEFAULT_TCA_AAI_NODE_QUERY_PATH = "aai/v11/search/nodes-query";

    public static final int TCA_ABATEMENT_SIMPLE_REPOSITORY_MAX_ENTITY_COUNT = 100_000;
    public static final int TCA_ABATEMENT_SIMPLE_REPOSITORY_REMOVE_ENTITY_COUNT = 1_000;


    //====================== REST API ===================== //
    public static final String TCA_REST_API_PREFIX = "/api/v1/tca/";
    public static final String TCA_POLICY_ENDPOINT = "policy";
    public static final String TCA_EXECUTION_ENDPOINT = "execute";
    public static final String TCA_POLICY_SOURCE_HEADER_KEY = "X-TCA-POLICY-SOURCE";
    public static final String TCA_POLICY_CREATION_HEADER_KEY = "X-TCA-POLICY-CREATION-TIME";
    public static final String TCA_POLICY_VERSION_HEADER_KEY = "X-TCA-POLICY-VERSION";

}
