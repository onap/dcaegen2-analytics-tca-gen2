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

package org.onap.dcae.analytics.web.http;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;

import org.onap.dcae.analytics.model.AnalyticsModelConstants;
import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.ecomplogger.AnalyticsErrorType;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.onap.dcae.utils.eelf.logger.api.info.ResponseLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.ServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.TargetServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestTimingLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.ResponseLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.ServiceLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.TargetServiceLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.MetricLogSpecImpl;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

/**
 * Eelf Audit log interceptor is used to log ECOMP Audit Logging
 *
 * @author Rajiv Singla
 */
public class EelfAuditLogInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private static final EELFLogger logger = EELFLogFactory.getLogger(EelfAuditLogInterceptor.class);

    private final ServiceLogInfo serviceLogInfo;
    private final String targetEntityName;

    public EelfAuditLogInterceptor(final HttpClientPreferences httpClientPreferences) {
        this.serviceLogInfo = getServiceLogInfo(httpClientPreferences);
        this.targetEntityName = getTargetEntityName(httpClientPreferences);
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request,
                                        final byte[] body,
                                        final ClientHttpRequestExecution execution) throws IOException {

        final String requestId = AnalyticsHttpUtils.getRequestId(request.getHeaders());
        final String transactionId = AnalyticsHttpUtils.getTransactionId(request.getHeaders());

        ClientHttpResponse clientHttpResponse = null;
        HttpStatus httpStatus = null;
        String statusText = "";
        String errorMessage = null;
        final Date requestBeginTimeStamp = new Date();
        try {
            clientHttpResponse = execution.execute(request, body);
            httpStatus = clientHttpResponse.getStatusCode();
            if (httpStatus.is2xxSuccessful()) {
                errorMessage = null;
                statusText = clientHttpResponse.getStatusText();
            } else {
                errorMessage = StreamUtils.copyToString(clientHttpResponse.getBody(), Charset.defaultCharset());
                statusText = clientHttpResponse.getStatusText();
            }
        } catch (IOException e) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            statusText = AnalyticsErrorType.TIMEOUT_ERROR.getErrorDescription();
            errorMessage = e.toString();
        }
        final Date requestEndTimeStamp = new Date();
        final long elapsedTime = requestEndTimeStamp.getTime() - requestBeginTimeStamp.getTime();
        final RequestTimingLogInfoImpl requestTimingLogInfo = new RequestTimingLogInfoImpl(requestBeginTimeStamp,
                requestEndTimeStamp, elapsedTime);
        final MetricLogSpecImpl metricLogSpec = new MetricLogSpecImpl(new RequestIdLogInfoImpl(requestId),
                serviceLogInfo, requestTimingLogInfo,
                getResponseLogInfo(httpStatus, statusText), getTargetServiceLogInfo(request, targetEntityName));

        if (errorMessage != null) {
            logger.metricLog().error("Request Id: {}, Transaction Id: {}, Elapsed Time: {} ms, Error Message: {} ",
                    metricLogSpec, requestId, transactionId, Long.toString(elapsedTime), errorMessage);
        } else {
            logger.metricLog().info("Request Id: {}, Transaction Id: {}, Elapsed Time: {} ms, REST Endpoint Call: {}",
                    metricLogSpec, requestId, transactionId, Long.toString(elapsedTime),
                    statusText + "-" + getTargetService(request));
        }

        return clientHttpResponse != null ? clientHttpResponse : new SimpleClientHttpResponse();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    private static ServiceLogInfo getServiceLogInfo(final HttpClientPreferences httpClientPreferences) {
        return new ServiceLogInfoImpl(TcaModelConstants.TCA_SERVICE_NAME,
                httpClientPreferences.getUsername(), "");
    }

    // translate well known http status code to corresponding Ecomp Logging error codes
    private static ResponseLogInfo getResponseLogInfo(final HttpStatus httpStatus, final String statusText) {
        if (httpStatus.is2xxSuccessful()) {
            return new ResponseLogInfoImpl(AnalyticsErrorType.SUCCESSFUL.getErrorCode(), statusText);
        } else if (httpStatus.is4xxClientError()) {
            if (httpStatus == HttpStatus.UNAUTHORIZED || httpStatus == HttpStatus.FORBIDDEN) {
                return new ResponseLogInfoImpl(AnalyticsErrorType.PERMISSION_ERROR.getErrorCode(), statusText);
            }
            return new ResponseLogInfoImpl(AnalyticsErrorType.DATA_ERROR.getErrorCode(), statusText);
        } else if (httpStatus.is5xxServerError()) {
            if (httpStatus == HttpStatus.SERVICE_UNAVAILABLE) {
                return new ResponseLogInfoImpl(AnalyticsErrorType.TIMEOUT_ERROR.getErrorCode(), statusText);
            }
            return new ResponseLogInfoImpl(AnalyticsErrorType.BUSINESS_PROCESS_ERROR.getErrorCode(),
                    statusText);
        }
        return new ResponseLogInfoImpl(AnalyticsErrorType.UNKNOWN_ERROR.getErrorCode(), statusText);
    }


    private static TargetServiceLogInfo getTargetServiceLogInfo(final HttpRequest httpRequest,
                                                                final String targetEntityName) {
        return new TargetServiceLogInfoImpl(targetEntityName, getTargetService(httpRequest),
                getTargetVirtualEntity(httpRequest));
    }

    private static String getTargetVirtualEntity(final HttpRequest httpRequest) {
        return httpRequest.getURI().getAuthority();
    }

    private static String getTargetService(final HttpRequest httpRequest) {
        return httpRequest.getMethod() + "-" + httpRequest.getURI().getPath();
    }

    private static String getTargetEntityName(final HttpClientPreferences httpClientPreferences) {
        final String simpleName = httpClientPreferences.getClass().getSimpleName().toUpperCase(Locale.getDefault());
        if (simpleName.contains("MRSUB")) {
            return "DMAAP_MR_SUBSCRIBER";
        } else if (simpleName.contains("MRPUB")) {
            return "DMAAP_MR_PUBLISHER";
        } else if (simpleName.contains("AAI")) {
            return "AAI_ENRICHMENT";
        } else {
            return "UNKNOWN";
        }
    }


    private static class SimpleClientHttpResponse extends AbstractClientHttpResponse {
        @Override
        public int getRawStatusCode() throws IOException {
            return HttpStatus.SERVICE_UNAVAILABLE.value();
        }

        @Override
        public String getStatusText() throws IOException {
            return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
        }

        @Override
        public void close() {
            // do nothing
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream("".getBytes(Charset.forName(AnalyticsModelConstants.UTF8_CHARSET_NAME)));
        }

        @Override
        public HttpHeaders getHeaders() {
            return new HttpHeaders();
        }
    }


}
