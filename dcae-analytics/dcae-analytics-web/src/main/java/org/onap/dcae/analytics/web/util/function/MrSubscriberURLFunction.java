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

package org.onap.dcae.analytics.web.util.function;


import static org.onap.dcae.analytics.web.util.AnalyticsWebUtils.RANDOM_ID_SUPPLIER;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Creates DMaaP MR Subscriber URLs from {@link MrSubscriberPreferences}
 *
 * @author Rajiv Singla
 */
public class MrSubscriberURLFunction implements Function<MrSubscriberPreferences, List<URL>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MrSubscriberURLFunction.class);
    private static final String URL_PATH_SEPARATOR = "/";

    @Override
    public List<URL> apply(final MrSubscriberPreferences subscriberConfig) {

        final List<URL> subscriberURLs = new LinkedList<>();

        // if consumer ids is not present generate single random consumer id
        final List<String> consumerIds = subscriberConfig.getConsumerIds() != null ?
                subscriberConfig.getConsumerIds() : Stream.of(RANDOM_ID_SUPPLIER.get()).collect(Collectors.toList());

        for (final String consumerId : consumerIds) {

            // request url must be present
            final String requestURL = subscriberConfig.getRequestURL();

            // generate random consumer group if not present
            final String consumerGroup = subscriberConfig.getConsumerGroup() != null ?
                    subscriberConfig.getConsumerGroup() :
                    DmaapMrConstants.SUBSCRIBER_RANDOM_CONSUMER_GROUP_PREFIX + RANDOM_ID_SUPPLIER.get();

            // set default message limit if not present
            final Integer messageLimit = subscriberConfig.getMessageLimit() != null ?
                    subscriberConfig.getMessageLimit() : DmaapMrConstants.SUBSCRIBER_DEFAULT_MESSAGE_LIMIT;

            // set default timeout if not present
            final Integer timeout = subscriberConfig.getTimeout() != null ?
                    subscriberConfig.getTimeout() : DmaapMrConstants.SUBSCRIBER_DEFAULT_TIMEOUT;

            final UriComponentsBuilder componentsBuilder = UriComponentsBuilder
                    .fromHttpUrl(requestURL)
                    .path(URL_PATH_SEPARATOR + consumerGroup + URL_PATH_SEPARATOR + consumerId);

            if (messageLimit != null && messageLimit >= 1) {
                componentsBuilder
                        .queryParam(DmaapMrConstants.SUBSCRIBER_MSG_LIMIT_QUERY_PARAM_NAME, messageLimit);
            }

            if (timeout != null && timeout >= 1) {
                componentsBuilder
                        .queryParam(DmaapMrConstants.SUBSCRIBER_TIMEOUT_QUERY_PARAM_NAME, timeout);
            }

            subscriberURLs.add(createURL(componentsBuilder));

        }

        return subscriberURLs;
    }

    private URL createURL(final UriComponentsBuilder uriComponentsBuilder) {
        try {
            final URL subscriberURL = uriComponentsBuilder.build().toUri().toURL();
            LOGGER.info("Created DMaaP MR Subscriber URL: {}", subscriberURL);
            return subscriberURL;
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to build DMaaP MR Subscriber URL", e);
        }
    }
}
