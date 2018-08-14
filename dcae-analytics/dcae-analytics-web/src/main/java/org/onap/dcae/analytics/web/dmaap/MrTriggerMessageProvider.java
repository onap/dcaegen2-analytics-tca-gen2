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


import java.net.URL;
import java.util.List;
import java.util.function.Supplier;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.util.supplier.UnboundedSupplier;
import org.onap.dcae.analytics.web.util.AnalyticsWebUtils;
import org.onap.dcae.analytics.web.util.function.MrSubscriberURLFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

/**
 * Provides DMaaP MR Subscriber Trigger Message
 *
 * @author Rajiv Singla
 */
public class MrTriggerMessageProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MrTriggerMessageProvider.class);

    public static final String TRIGGER_METHOD_NAME = "getTriggerMessage";

    private final Supplier<URL> subscriberUrlSupplier;

    public MrTriggerMessageProvider(final MrSubscriberPreferences subscriberPreferences) {
        final List<URL> urls = new MrSubscriberURLFunction().apply(subscriberPreferences);
        subscriberUrlSupplier = new UnboundedSupplier<>(urls.toArray(new URL[urls.size()]));
    }

    /**
     * DMaaP MR subscriber trigger message
     *
     * @return dmaap mr subscriber trigger message
     */
    public Message<String> getTriggerMessage() {
        final String requestId = AnalyticsWebUtils.REQUEST_ID_SUPPLIER.get();
        final String transactionId = AnalyticsWebUtils.RANDOM_ID_SUPPLIER.get();
        final String beginTimestamp = AnalyticsWebUtils.CREATION_TIMESTAMP_SUPPLIER.get();
        LOGGER.debug("Request Id: {}. Transaction Id: {}. Begin TS: {}. Starting new DMaaP MR Subscriber poll.",
                requestId, transactionId, beginTimestamp);
        return MessageBuilder
                .withPayload(subscriberUrlSupplier.get().toString())
                .setHeader(AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY, requestId)
                .setHeader(AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY, transactionId)
                .setHeader(AnalyticsHttpConstants.REQUEST_BEGIN_TS_HEADER_KEY, beginTimestamp)
                .build();
    }
}
