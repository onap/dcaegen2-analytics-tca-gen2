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
 * Contains constants for DCAE Analytics Model Module
 *
 * @author Rajiv Singla
 */
public abstract class AnalyticsModelConstants {

    public static final String JSON_MODULE_GROUP_ID = "org.onap.dcae.analytics.model";
    public static final String JSON_MODULE_ARTIFACT_ID = "dcae-analytics-model";

    // ========================= UTILITIES =======================================//
    // DATE CONSTANTS
    public static final String ANALYTICS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    // RANDOM ID
    public static final Integer DEFAULT_RANDOM_ID_LENGTH = 5;
    // UTF-8 CHARSET NAME
    public static final String UTF8_CHARSET_NAME = "UTF-8";
    // REQUEST ID DELIMITER
    public static final String ANALYTICS_REQUEST_ID_DELIMITER = ":";

    private AnalyticsModelConstants() {
        // private constructor
    }


}
