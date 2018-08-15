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

package org.onap.dcae.analytics.tca.web.util.function;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.AutoAdjusting;
import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.SubscriberDetails;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.util.function.StringToURLFunction;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * @author Rajiv Singla
 */
public class TcaAppPropsToMrSubscriberPrefsFunction implements Function<TcaAppProperties, MrSubscriberPreferences> {

    private static final Logger logger = LoggerFactory.getLogger(TcaAppPropsToMrSubscriberPrefsFunction.class);

    @Override
    public MrSubscriberPreferences apply(final TcaAppProperties tcaAppProperties) {

        final Map<String, SubscriberDetails> streamsSubscribes = tcaAppProperties.getStreamsSubscribes();

        final List<Map.Entry<String, SubscriberDetails>> messageRouterSubscribers =
                streamsSubscribes.entrySet().stream()
                        .filter(ConfigBindingServiceConstants.MESSAGE_ROUTER_PREDICATE)
                        .collect(Collectors.toList());

        // Use first subscriber properties
        final Map.Entry<String, SubscriberDetails> firstSubscriberProperties = messageRouterSubscribers.get(0);

        if (messageRouterSubscribers.size() > 1) {
            logger.warn("Expected one DMaaP MR subscriber properties but found: {}.", messageRouterSubscribers.size());
        }

        final String subscriberPropertiesKey = firstSubscriberProperties.getKey();
        final SubscriberDetails subscriberDetails = firstSubscriberProperties.getValue();

        logger.info("Creating DMaaP MR Subscriber from config properties key: {}, props: {}",
                subscriberPropertiesKey, subscriberDetails);

        final String requestURL = subscriberDetails.getDmaapInfo().getTopicUrl();
        final HttpHeaders httpHeaders = AnalyticsHttpUtils.createDefaultHttpHeaders();
        final String username = subscriberDetails.getAafUsername();
        final String password = subscriberDetails.getAafPassword();
        final URL proxyUrl = Optional.ofNullable(subscriberDetails.getProxyUrl())
                .flatMap(new StringToURLFunction()).orElse(null);
        final Boolean ignoreSSLValidation = subscriberDetails.getIgnoreSSLValidation();

        final String consumerGroup = subscriberDetails.getConsumerGroup();
        final List<String> consumerIds = subscriberDetails.getConsumerIds();
        final Integer messageLimit = subscriberDetails.getMessageLimit();
        final Integer timeout = subscriberDetails.getTimeout();

        final Boolean enableEcompLogging = tcaAppProperties.getTca().getEnableEcompLogging();

        if (subscriberDetails.getPolling() == null) {

            return new MrSubscriberPreferences(requestURL, subscriberPropertiesKey, httpHeaders, username, password,
                    proxyUrl, ignoreSSLValidation, enableEcompLogging, consumerGroup, consumerIds, messageLimit,
                    timeout, null);
        }

        final Integer fixedRate = subscriberDetails.getPolling().getFixedRate();

        if (fixedRate != null && fixedRate != 0) {
            logger.info("Fixed Rate polling will be used for DMaaP MR Subscriber: {}", subscriberPropertiesKey);
            final MrSubscriberPollingPreferences pollingPreferences =
                    new MrSubscriberPollingPreferences(fixedRate, 0, fixedRate, 0);
            return new MrSubscriberPreferences(requestURL, subscriberPropertiesKey, httpHeaders, username, password,
                    proxyUrl, ignoreSSLValidation, enableEcompLogging, consumerGroup, consumerIds, messageLimit,
                    timeout, pollingPreferences);
        }

        final AutoAdjusting autoAdjusting = subscriberDetails.getPolling().getAutoAdjusting();
        logger.info("Selecting Auto Adjusting polling rate for DMaaP MR Subscriber: {}", subscriberPropertiesKey);

        final MrSubscriberPollingPreferences pollingPreferences =
                new MrSubscriberPollingPreferences(autoAdjusting.getMin(), autoAdjusting.getStepUp(),
                        autoAdjusting.getMax(), autoAdjusting.getStepDown());
        return new MrSubscriberPreferences(requestURL, subscriberPropertiesKey, httpHeaders, username, password,
                proxyUrl, ignoreSSLValidation, enableEcompLogging, consumerGroup, consumerIds, messageLimit, timeout,
                pollingPreferences);

    }
}
