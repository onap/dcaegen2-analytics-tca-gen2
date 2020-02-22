/*
 * ================================================================================
 * Copyright (c) 2018 China Mobile. All rights reserved.
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
package org.onap.dcae.analytics.tca.core.util;

import java.util.Date;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.ecomplogger.AnalyticsErrorType;
import org.onap.dcae.utils.eelf.logger.api.info.ErrorLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.ResponseLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.TargetServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.spec.AuditLogSpec;
import org.onap.dcae.utils.eelf.logger.api.spec.DebugLogSpec;
import org.onap.dcae.utils.eelf.logger.api.spec.ErrorLogSpec;
import org.onap.dcae.utils.eelf.logger.model.info.ErrorLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestTimingLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.ResponseLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.TargetServiceLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.AuditLogSpecImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.DebugLogSpecImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.ErrorLogSpecImpl;

/**
 * @author Kai Lu
 */
public final class LogSpec {

    private LogSpec( ) {
        // private constructor       
    }

    /**
     * create ErrorLogSpec
     *
     * @param requestId requestId
     *
     * @return ErrorLogSpecImpl object
     *
     */
    public static ErrorLogSpec createErrorLogSpec(final String requestId) {
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        final TargetServiceLogInfo targetServiceLogInfo = new TargetServiceLogInfoImpl(
                "DCAE-TCA", TcaModelConstants.TCA_SERVICE_NAME, "");
        final ErrorLogInfo errorLogInfo =
                new ErrorLogInfoImpl(AnalyticsErrorType.SCHEMA_ERROR.getErrorCode(),
                        AnalyticsErrorType.SCHEMA_ERROR.getErrorDescription());
        return new ErrorLogSpecImpl(requestIdLogInfo,
                TcaUtils.TCA_SERVICE_LOG_INFO, targetServiceLogInfo, errorLogInfo);
    }

    /**
     * create DebugLogSpec
     *
     * @param requestId requestId
     *
     * @return DebugLogSpecImpl object
     *
     */
    public static DebugLogSpec createDebugLogSpec(final String requestId) {
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        return new DebugLogSpecImpl(requestIdLogInfo);
    }

    /**
     * create AuditLogSpec
     *
     * @param requestId requestId
     * @param requestBeginTimestamp requestBeginTimestamp
     *
     * @return AuditLogSpec object
     *
     */
    public static AuditLogSpec createAuditLogSpec(final String requestId,
                                                  final Date requestBeginTimestamp) {
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        final Date endTimestamp = new Date();
        final RequestTimingLogInfoImpl requestTimingLogInfo = new RequestTimingLogInfoImpl(requestBeginTimestamp,
                endTimestamp, endTimestamp.getTime() - requestBeginTimestamp.getTime());
        final ResponseLogInfo responseLogInfo =
                new ResponseLogInfoImpl(AnalyticsErrorType.SUCCESSFUL.getErrorCode(),
                        AnalyticsErrorType.SUCCESSFUL.getErrorDescription());
        return new AuditLogSpecImpl(requestIdLogInfo, TcaUtils.TCA_SERVICE_LOG_INFO,
                requestTimingLogInfo, responseLogInfo);
    }

}
