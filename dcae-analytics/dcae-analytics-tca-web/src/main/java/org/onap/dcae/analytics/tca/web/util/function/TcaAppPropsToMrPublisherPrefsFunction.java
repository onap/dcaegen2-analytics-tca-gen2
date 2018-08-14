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

import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties.PublisherDetails;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.util.function.StringToURLFunction;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.web.dmaap.MrPublisherPreferences;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * @author Rajiv Singla
 */
public class TcaAppPropsToMrPublisherPrefsFunction implements Function<TcaAppProperties, MrPublisherPreferences> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TcaAppPropsToMrPublisherPrefsFunction.class);

    @Override
    public MrPublisherPreferences apply(final TcaAppProperties tcaAppProperties) {

        final Map<String, PublisherDetails> streamsPublishes = tcaAppProperties.getStreamsPublishes();

        final List<Map.Entry<String, PublisherDetails>> messageRouterPublishes =
                streamsPublishes.entrySet().stream()
                        .filter(ConfigBindingServiceConstants.MESSAGE_ROUTER_PREDICATE)
                        .collect(Collectors.toList());

        // Use first publisher properties
        final Map.Entry<String, PublisherDetails> firstPublisherProperties = messageRouterPublishes.get(0);

        if (messageRouterPublishes.size() > 1) {
            LOGGER.warn("Expected only one DMaaP MR publisher properties but found: {}.", streamsPublishes.size());
        }

        final String publisherPropertiesKey = firstPublisherProperties.getKey();
        final PublisherDetails publisherDetails = firstPublisherProperties.getValue();

        LOGGER.info("Creating DMaaP MR Publisher from config properties key: {}, props: {}",
                publisherPropertiesKey, publisherDetails);

        final String requestURL = publisherDetails.getDmaapInfo().getTopicUrl();
        final HttpHeaders httpHeaders = AnalyticsHttpUtils.createDefaultHttpHeaders();
        final String username = publisherDetails.getAafUsername();
        final String password = publisherDetails.getAafPassword();

        final URL proxyUrl = Optional.ofNullable(publisherDetails.getProxyUrl())
                .flatMap(new StringToURLFunction()).orElse(null);

        final Boolean ignoreSSLValidation = publisherDetails.getIgnoreSSLValidation();

        return new MrPublisherPreferences(requestURL, publisherPropertiesKey, httpHeaders, username, password,
                proxyUrl, ignoreSSLValidation, tcaAppProperties.getTca().getEnableEcompLogging());

    }
}
