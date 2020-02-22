package org.onap.dcae.analytics.tca.core.util;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.ecomplogger.AnalyticsErrorType;
import org.onap.dcae.utils.eelf.logger.api.info.ErrorLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.TargetServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.spec.DebugLogSpec;
import org.onap.dcae.utils.eelf.logger.api.spec.ErrorLogSpec;
import org.onap.dcae.utils.eelf.logger.model.info.ErrorLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.TargetServiceLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.DebugLogSpecImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.ErrorLogSpecImpl;

public final class LogSpec {

    private LogSpec( ) {
        // private constructor       
    }

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

    public static DebugLogSpec createDebugLogSpec(String requestId) {
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        return new DebugLogSpecImpl(requestIdLogInfo);
    }
}
