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

/**
 * Analytics Profiles
 *
 * @author Rajiv Singla
 */
public abstract class AnalyticsProfile {

    public static final String NEGATION_PREFIX = "!";

    public static final String DEV_PROFILE_NAME = "dev";
    public static final String NOT_DEV_PROFILE_NAME = NEGATION_PREFIX + DEV_PROFILE_NAME;
    public static final String CONFIG_BINDING_SERVICE_PROFILE_NAME = "configBindingService";
    public static final String NOT_CONFIG_BINDING_SERVICE_PROFILE_NAME =
            NEGATION_PREFIX + CONFIG_BINDING_SERVICE_PROFILE_NAME;
    public static final String DMAAP_PROFILE_NAME = "dmaap";
    public static final String NOT_DMAAP_PROFILE_NAME = NEGATION_PREFIX + DMAAP_PROFILE_NAME;
    public static final String REDIS_PROFILE_NAME = "redis";
    public static final String NOT_REDIS_PROFILE_NAME = NEGATION_PREFIX + REDIS_PROFILE_NAME;
    public static final String MONGO_PROFILE_NAME = "mongo";
    public static final String NOT_MONGO_PROFILE_NAME = NEGATION_PREFIX + MONGO_PROFILE_NAME;

    private AnalyticsProfile() {
        // private constructor
    }

}
