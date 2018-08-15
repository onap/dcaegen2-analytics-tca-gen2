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

package org.onap.dcae.analytics.tca.web.validation;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.PubSubCommonDetails;
import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.PublisherDetails;
import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.SubscriberDetails;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for {@link TcaAppProperties}. This validator is used by spring at start up time to validate
 * TCA Application properties
 *
 * @author Rajiv Singla
 */
public class TcaAppPropertiesValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(TcaAppPropertiesValidator.class);

    @Override
    public boolean supports(final Class<?> type) {
        return type == TcaAppProperties.class;
    }

    @Override
    public void validate(@Nullable final Object props, final Errors errors) {

        if (props == null) {
            errors.rejectValue(ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_PROPERTIES_KEY,
                    "TCA App Properties are not present");
            return;
        }

        final TcaAppProperties tcaAppProperties = (TcaAppProperties) props;

        logger.info("Validating TCA App Properties: {}", tcaAppProperties);

        final Map<String, PublisherDetails> streamsPublishes = tcaAppProperties.getStreamsPublishes();
        final Map<String, SubscriberDetails> streamsSubscribes = tcaAppProperties.getStreamsSubscribes();

        // Validate that stream publishes has at least 1 publisher and subscriber
        if (!validatePubSubArePresent(errors, streamsPublishes, streamsSubscribes)) {
            return;
        }

        // Validate that streams publishes and subscribes has at least one message router
        List<? super PubSubCommonDetails> pubSubMRCommonDetails =
                validatePubSubHasMRDetails(errors, streamsPublishes, streamsSubscribes);
        if (pubSubMRCommonDetails.isEmpty()) {
            return;
        }

        //Confirm each message router has dmaap info and their url is present and valid
        validateMRProperties(errors, pubSubMRCommonDetails);

        // validated aai properties
        validateAAIProperties(errors, tcaAppProperties.getTca().getAai());

        logger.info("Validation of TCA App Properties completed successfully");

    }


    private void validateAAIProperties(final Errors errors, final TcaAppProperties.Aai aai) {
        new ConfigPropertiesAaiValidator().validate(aai, errors);
    }

    private void validateMRProperties(final Errors errors,
                                      final List<? super PubSubCommonDetails> pubSubMRCommonDetails) {

        for (Object pubSubMRCommonDetail : pubSubMRCommonDetails) {
            final PubSubCommonDetails mrDetails = (PubSubCommonDetails) pubSubMRCommonDetail;

            if (mrDetails.getDmaapInfo() == null || mrDetails.getDmaapInfo().getTopicUrl() == null) {
                errors.rejectValue("dmaap_info",
                        "dmaap_info url not present for MR configuration properties: " + mrDetails);
            }

            if (mrDetails.getDmaapInfo() != null && mrDetails.getDmaapInfo().getTopicUrl() != null
                    && !isURLValid(mrDetails.getDmaapInfo().getTopicUrl())) {
                errors.rejectValue("topic_url",
                        "Invalid MR Topic URL in configuration properties:" + mrDetails);

            }

            if (mrDetails.getProxyUrl() != null && !mrDetails.getProxyUrl().trim().isEmpty()
                    && !isURLValid(mrDetails.getProxyUrl())) {
                errors.rejectValue("proxy_url",
                        "Invalid Proxy url in configuration properties:" + mrDetails);
            }
        }

    }

    private List<? super PubSubCommonDetails> validatePubSubHasMRDetails(
            final Errors errors,
            final Map<String, PublisherDetails> streamsPublishes,
            final Map<String, SubscriberDetails> streamsSubscribes) {

        final List<Map.Entry<String, PublisherDetails>> messageRouterPublishers =
                streamsPublishes.entrySet().stream()
                        .filter(ConfigBindingServiceConstants.MESSAGE_ROUTER_PREDICATE)
                        .collect(Collectors.toList());
        if (messageRouterPublishers.isEmpty()) {
            errors.rejectValue("stream_publishes",
                    "Stream publishes must contain at least 1 message router publisher");
        }

        final List<Map.Entry<String, SubscriberDetails>> messageRouterSubscribers =
                streamsSubscribes.entrySet().stream()
                        .filter(ConfigBindingServiceConstants.MESSAGE_ROUTER_PREDICATE)
                        .collect(Collectors.toList());
        if (messageRouterSubscribers.isEmpty()) {
            errors.rejectValue("stream_subscribes",
                    "Stream subscriber must contain at least 1 message router subscriber");
        }

        // create common pub sub MR list
        final List<? super PubSubCommonDetails> pubSubMRCommonDetails = new LinkedList<>();
        pubSubMRCommonDetails.addAll(messageRouterPublishers.stream()
                .map(Map.Entry::getValue).collect(Collectors.toList()));
        pubSubMRCommonDetails.addAll(messageRouterSubscribers.stream()
                .map(Map.Entry::getValue).collect(Collectors.toList())
        );

        return pubSubMRCommonDetails;
    }

    private boolean validatePubSubArePresent(final Errors errors,
                                             final Map<String, PublisherDetails> streamsPublishes,
                                             final Map<String, SubscriberDetails>
                                                     streamsSubscribes) {
        if (streamsPublishes.isEmpty()) {
            errors.rejectValue("streams_publishes", "Streams publishes must define at least 1 publisher");
            return false;
        }
        if (streamsSubscribes.isEmpty()) {
            errors.rejectValue("streams_subscribes", "Streams subscribes must define at least 1 subscriber");
            return false;
        }

        return true;
    }

    private static boolean isURLValid(final String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }


}
