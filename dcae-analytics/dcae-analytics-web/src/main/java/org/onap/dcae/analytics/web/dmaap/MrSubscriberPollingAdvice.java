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

package org.onap.dcae.analytics.web.dmaap;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.onap.dcae.analytics.tca.core.util.LogSpec;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.api.spec.DebugLogSpec;
import org.springframework.http.HttpStatus;
import org.springframework.integration.handler.advice.AbstractRequestHandlerAdvice;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.util.DynamicPeriodicTrigger;
import org.springframework.messaging.Message;

/**
 * A polling advice which can auto adjust polling intervals depending on DMaaP MR message availability.
 * Can be configured to slow down polling when messages are not available and increase polling when messages are
 * indeed available.
 * <p>
 * The next polling interval is <b>increased</b> by given step up delta if message is <b>not found</b> up to maximum
 * Polling Interval
 * <br>
 * The next polling interval is <b>decreased</b> by step down delta if message <b>is found</b> up to minimum
 * polling interval
 *
 * @author Rajiv Singla
 */
public class MrSubscriberPollingAdvice extends AbstractRequestHandlerAdvice {

    private static final EELFLogger eelfLogger = EELFLogFactory.getLogger(MrSubscriberPollingAdvice.class);

    private final DynamicPeriodicTrigger trigger;
    private final int minPollingInterval;
    private final int stepUpPollingDelta;
    private final int maxPollingInterval;
    private final int stepDownPollingDelta;

    private final AtomicInteger nextPollingInterval;

    /**
     * Creates variable polling intervals based on message availability.
     *
     * @param trigger Dynamic Trigger instance
     * @param minPollingInterval Minimum polling interval
     * @param stepUpPollingDelta Delta by which next polling interval will be increased when message is not found
     * @param maxPollingInterval Maximum polling interval
     * @param stepDownPollingDelta Delta by which next polling interval will be decreased when message is found
     */
    public MrSubscriberPollingAdvice(final DynamicPeriodicTrigger trigger,
                                     final int minPollingInterval,
                                     final int stepUpPollingDelta,
                                     final int maxPollingInterval,
                                     final int stepDownPollingDelta) {
        this.trigger = trigger;
        this.minPollingInterval = minPollingInterval;
        this.stepUpPollingDelta = stepUpPollingDelta;
        this.maxPollingInterval = maxPollingInterval;
        this.stepDownPollingDelta = stepDownPollingDelta;
        nextPollingInterval = new AtomicInteger(minPollingInterval);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object doInvoke(final ExecutionCallback callback, final Object target, final Message<?> message) {

        // execute call back
        Object result = callback.execute();

        // if result is not of type message builder just return
        if (!(result instanceof MessageBuilder)) {
            return result;
        }

        final MessageBuilder<String> resultMessageBuilder = (MessageBuilder<String>) result;
        final String payload = resultMessageBuilder.getPayload();
        final Map<String, Object> headers = resultMessageBuilder.getHeaders();
        final Object httpStatusCode = headers.get(AnalyticsHttpConstants.HTTP_STATUS_CODE_HEADER_KEY);

        // get http status code
        if (httpStatusCode == null) {
            return result;
        }
        // TODO: Needs closer look
        final HttpStatus httpStatus = HttpStatus.resolve(Integer.parseInt(httpStatusCode.toString().split(" ")[0]));


        // if status code is present and successful apply polling adjustments
        if (httpStatus != null && httpStatus.is2xxSuccessful()) {
            final boolean areMessagesPresent = areMessagesPresent(payload);
            updateNextPollingInterval(areMessagesPresent);

            final String requestId = AnalyticsHttpUtils.getRequestId(message.getHeaders());
            final String transactionId = AnalyticsHttpUtils.getTransactionId(message.getHeaders());
            final DebugLogSpec debugLogSpec = LogSpec.createDebugLogSpec(requestId);
            eelfLogger.debugLog().debug("Request Id: {}, Transaction Id: {}, Messages Present: {}, " +
                            "Next Polling Interval will be: {}", debugLogSpec, requestId, transactionId,
                            String.valueOf(areMessagesPresent), nextPollingInterval.toString());

            trigger.setDuration(Duration.ofMillis(nextPollingInterval.get()));

            // if no messages were found in dmaap poll - terminate further processing
            if (!areMessagesPresent) {
                eelfLogger.debugLog().debug("Request Id: {}, Transaction Id: {}, No new messages found in DMaaP MR Response. " +
                        "No further processing required", debugLogSpec, requestId, transactionId);
                return null;
            }

        }

        return result;
    }

    private boolean areMessagesPresent(final String payload) {

        return !(payload.isEmpty() || payload.equals(DmaapMrConstants.SUBSCRIBER_EMPTY_MESSAGE_RESPONSE_STRING));
    }

    private void updateNextPollingInterval(final boolean areMessagesPresent) {
        if (areMessagesPresent) {
            nextPollingInterval.getAndUpdate(interval -> interval - stepDownPollingDelta <= minPollingInterval ?
                    minPollingInterval : interval - stepDownPollingDelta);
        } else {
            nextPollingInterval.getAndUpdate(interval -> interval + stepUpPollingDelta >= maxPollingInterval ?
                    maxPollingInterval : interval + stepUpPollingDelta);
        }
    }
}

