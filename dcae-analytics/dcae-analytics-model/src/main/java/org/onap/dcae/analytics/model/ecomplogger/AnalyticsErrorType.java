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

package org.onap.dcae.analytics.model.ecomplogger;

/**
 * @author Rajiv Singla
 */
public enum AnalyticsErrorType implements EcompLoggerModel {

    SUCCESSFUL(0, ""),
    PERMISSION_ERROR(100, "Permission Error"),
    TIMEOUT_ERROR(200, "Timeout Error"),
    DATA_ERROR(300, "Data Error"),
    SCHEMA_ERROR(400, "Schema Error"),
    BUSINESS_PROCESS_ERROR(500, "Business Process Error"),
    UNKNOWN_ERROR(900, "Unknown Error");

    private final Integer errorCode;
    private final String errorDescription;

    AnalyticsErrorType(final Integer errorCode, final String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
