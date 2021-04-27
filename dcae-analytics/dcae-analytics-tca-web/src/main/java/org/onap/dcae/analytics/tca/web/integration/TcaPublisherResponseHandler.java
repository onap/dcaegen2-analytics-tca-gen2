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


import static org.onap.dcae.analytics.model.AnalyticsHttpConstants.REQUEST_BEGIN_TS_HEADER_KEY;
import static org.onap.dcae.analytics.model.AnalyticsHttpConstants.REQUEST_END_TS_HEADER_KEY;

import java.util.Date;
import java.util.Map;

import org.onap.dcae.analytics.model.ecomplogger.AnalyticsErrorType;
import org.onap.dcae.analytics.tca.core.util.TcaUtils;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.onap.dcae.utils.eelf.logger.api.info.ResponseLogInfo;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestTimingLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.ResponseLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.AuditLogSpecImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;

/**
 * TCA Publisher Response Handler is used to do logging of response received from DMaaP MR Publisher. It does not do
 * any changes to payload or headers
 *
 * @author Rajiv Singla
 */
public class TcaPublisherResponseHandler implements GenericHandler<String> {

    private static final EELFLogger ECOMP_LOGGER = EELFLogFactory.getLogger(TcaPublisherResponseHandler.class);
    private static final Logger logger = LoggerFactory.getLogger(TcaPublisherResponseHandler.class);

    private final TcaAppProperties tcaAppProperties;

    public TcaPublisherResponseHandler(final TcaAppProperties tcaAppProperties) {
        this.tcaAppProperties = tcaAppProperties;
    }

    @Override
    public Object handle(final String payload, final Map<String, Object> headers) {

        final MessageHeaders messageHeaders = new MessageHeaders(headers);
        final String requestId = AnalyticsHttpUtils.getRequestId(messageHeaders);
        final String transactionId = AnalyticsHttpUtils.getTransactionId(messageHeaders);
        final Date beginTimestamp = AnalyticsHttpUtils.getTimestampFromHeaders(headers, REQUEST_BEGIN_TS_HEADER_KEY);
        final Date endTimestamp = AnalyticsHttpUtils.getTimestampFromHeaders(headers, REQUEST_END_TS_HEADER_KEY);

        if (tcaAppProperties.getTca().getEnableEcompLogging()) {
            createAuditLog(requestId, transactionId, beginTimestamp, endTimestamp, payload);
        } else {
            logger.info("Request Id: {}, Transaction Id: {}, Transaction completion Time: {} ms, " +
                            "DMaaP MR Publisher Response: {}", requestId, transactionId,
                    endTimestamp.getTime() - beginTimestamp.getTime(), payload);
        }
        return payload;

    }

    private static void createAuditLog(final String requestId,
                                       final String transactionId,
                                       final Date requestBeginTimestamp,
                                       final Date requestEndTimestamp,
                                       final String tcaPublisherResponse) {
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        final long elapsedTime = requestEndTimestamp.getTime() - requestBeginTimestamp.getTime();
        final RequestTimingLogInfoImpl requestTimingLogInfo = new RequestTimingLogInfoImpl(requestBeginTimestamp,
                requestEndTimestamp, elapsedTime);
        final ResponseLogInfo responseLogInfo =
                new ResponseLogInfoImpl(AnalyticsErrorType.SUCCESSFUL.getErrorCode(),
                        AnalyticsErrorType.SUCCESSFUL.getErrorDescription());
        final AuditLogSpecImpl auditLogSpec = new AuditLogSpecImpl(requestIdLogInfo, TcaUtils.TCA_SERVICE_LOG_INFO,
                requestTimingLogInfo, responseLogInfo);
        ECOMP_LOGGER.auditLog().info("Request Id: {}, Transaction Id: {}, " +
                        "Transaction completion Time: {} ms, DMaaP MR Publisher Response: {}",
                auditLogSpec, requestId, transactionId, Long.toString(elapsedTime), tcaPublisherResponse);
    }

}

