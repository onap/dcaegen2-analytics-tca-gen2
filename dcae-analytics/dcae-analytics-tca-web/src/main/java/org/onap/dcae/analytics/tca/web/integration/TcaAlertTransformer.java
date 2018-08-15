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

package org.onap.dcae.analytics.tca.web.integration;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.ecomplogger.AnalyticsErrorType;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.util.TcaUtils;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.onap.dcae.utils.eelf.logger.api.info.ErrorLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.ResponseLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.TargetServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.model.info.ErrorLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestTimingLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.ResponseLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.TargetServiceLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.AuditLogSpecImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.ErrorLogSpecImpl;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

/**
 * @author Rajiv Singla
 */
public class TcaAlertTransformer extends AbstractTransformer {

    private static final EELFLogger logger = EELFLogFactory.getLogger(TcaAlertTransformer.class);

    private static final Predicate<TcaExecutionContext> ERROR_EXECUTION_CONTEXT_PREDICATE =
            executionContext -> executionContext.getTcaProcessingContext().getErrorMessage() != null;
    private static final Predicate<TcaExecutionContext> EARLY_TERMINATION_CONTEXT_PREDICATE =
            tcaExecutionContext -> tcaExecutionContext.getTcaProcessingContext().getEarlyTerminationMessage() != null;
    private static final Predicate<TcaExecutionContext> ABATED_EXECUTION_CONTEXT_PREDICATE =
            tcaExecutionContext -> tcaExecutionContext.getTcaResultContext().getPreviousRequestId() != null;

    private final TcaAppProperties tcaAppProperties;

    public TcaAlertTransformer(final TcaAppProperties tcaAppProperties) {
        this.tcaAppProperties = tcaAppProperties;
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Object doTransform(final Message<?> message) throws Exception {

        final Object messagePayload = message.getPayload();

        final String requestId = AnalyticsHttpUtils.getRequestId(message.getHeaders());
        final String transactionId = AnalyticsHttpUtils.getTransactionId(message.getHeaders());
        final Date requestBeginTimestamp = AnalyticsHttpUtils.getTimestampFromHeaders(message.getHeaders(),
                AnalyticsHttpConstants.REQUEST_BEGIN_TS_HEADER_KEY);

        if (messagePayload instanceof List) {
            // get execution contexts with alerts
            final List<TcaExecutionContext> tcaExecutionContexts = (List<TcaExecutionContext>) messagePayload;
            final List<TcaAlert> tcaAlerts =
                    tcaExecutionContexts.stream()
                            .map(e -> e.getTcaResultContext().getTcaAlert())
                            .filter(Objects::nonNull).collect(Collectors.toList());

            // do ecomp logging
            if (tcaAppProperties.getTca().getEnableEcompLogging()) {
                final List<TcaExecutionContext> errorExecutionContexts =
                        tcaExecutionContexts.stream().filter(ERROR_EXECUTION_CONTEXT_PREDICATE)
                                .collect(Collectors.toList());
                createAuditLog(requestId, transactionId, requestBeginTimestamp, tcaExecutionContexts, tcaAlerts,
                        errorExecutionContexts);
                createErrorLog(requestId, transactionId, errorExecutionContexts);
            }

            // if no alerts terminate further processing
            return tcaAlerts.isEmpty() ? null : tcaAlerts;
        } else {
            return null;
        }
    }


    private static void createErrorLog(final String requestId,
                                       final String transactionId,
                                       final List<TcaExecutionContext> errorExecutionContexts) {
        // no error log generated - if there was no error during tca processing
        if (!errorExecutionContexts.isEmpty()) {
            for (TcaExecutionContext errorExecutionContext : errorExecutionContexts) {
                final RequestIdLogInfoImpl requestIdLogInfo =
                        new RequestIdLogInfoImpl(errorExecutionContext.getRequestId());
                final TargetServiceLogInfo targetServiceLogInfo = new TargetServiceLogInfoImpl(
                        "DCAE-TCA", TcaModelConstants.TCA_SERVICE_NAME, "");
                final ErrorLogInfo errorLogInfo =
                        new ErrorLogInfoImpl(AnalyticsErrorType.SCHEMA_ERROR.getErrorCode(),
                                AnalyticsErrorType.SCHEMA_ERROR.getErrorDescription());
                final ErrorLogSpecImpl errorLogSpec = new ErrorLogSpecImpl(requestIdLogInfo,
                        TcaUtils.TCA_SERVICE_LOG_INFO, targetServiceLogInfo, errorLogInfo);
                logger.errorLog().error("Request Id: {}, Transaction Id: {}, Error Message: {} ",
                        errorLogSpec, requestId, transactionId, errorExecutionContext.getTcaProcessingContext()
                                .getErrorMessage());
            }
        }
    }

    private static void createAuditLog(final String requestId,
                                       final String transactionId,
                                       final Date requestBeginTimestamp,
                                       final List<TcaExecutionContext> tcaExecutionContexts,
                                       final List<TcaAlert> tcaAlerts,
                                       final List<TcaExecutionContext> errorExecutionContexts) {
        final List<TcaExecutionContext> earlyTerminationExecutionContexts =
                tcaExecutionContexts.stream().filter(EARLY_TERMINATION_CONTEXT_PREDICATE)
                        .collect(Collectors.toList());
        final List<TcaExecutionContext> abatedExecutionContexts =
                tcaExecutionContexts.stream().filter(ABATED_EXECUTION_CONTEXT_PREDICATE)
                        .collect(Collectors.toList());
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        final Date endTimestamp = new Date();
        final RequestTimingLogInfoImpl requestTimingLogInfo = new RequestTimingLogInfoImpl(requestBeginTimestamp,
                endTimestamp, endTimestamp.getTime() - requestBeginTimestamp.getTime());
        final ResponseLogInfo responseLogInfo =
                new ResponseLogInfoImpl(AnalyticsErrorType.SUCCESSFUL.getErrorCode(),
                        AnalyticsErrorType.SUCCESSFUL.getErrorDescription());
        final AuditLogSpecImpl auditLogSpec = new AuditLogSpecImpl(requestIdLogInfo, TcaUtils.TCA_SERVICE_LOG_INFO,
                requestTimingLogInfo, responseLogInfo);
        logger.auditLog().info("Request Id: {}, Transaction Id: {}, " +
                        "Message counts - Received: {}, Errors: {}, Terminated Early: {}, Abated: {}, Alerts: {}",
                auditLogSpec, requestId, transactionId,
                Integer.toString(tcaExecutionContexts.size()), Integer.toString(errorExecutionContexts.size()),
                Integer.toString(earlyTerminationExecutionContexts.size()),
                Integer.toString(abatedExecutionContexts.size()), Integer.toString(tcaAlerts.size()));
    }
}
