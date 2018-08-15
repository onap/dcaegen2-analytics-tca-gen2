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

package org.onap.dcae.analytics.web.config;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.web.BaseAnalyticsWebSpringBootIT;
import org.onap.dcae.analytics.web.dmaap.MrPublisherPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;

/**
 * @author Rajiv Singla
 */
@Configuration
@Profile({AnalyticsProfile.DMAAP_PROFILE_NAME})
public class DmaapMrTestConfig {

    @Bean
    public MrSubscriberPreferences mrSubscriberPreferences() {
        return new MrSubscriberPreferences(BaseAnalyticsWebSpringBootIT.TEST_SUBSCRIBER_TOPIC_URL, null,
                null, BaseAnalyticsWebSpringBootIT.TEST_SUBSCRIBER_AAF_USERNAME,
                BaseAnalyticsWebSpringBootIT.TEST_SUBSCRIBER_AAF_PASSWORD,
                null, null, null,
                BaseAnalyticsWebSpringBootIT.TEST_SUBSCRIBER_CONSUMER_GROUP,
                BaseAnalyticsWebSpringBootIT.TEST_SUBSCRIBER_CONSUMER_IDS,
                null, null, null);
    }

    @Bean
    public MrPublisherPreferences mrPublisherPreferences() {
        return new MrPublisherPreferences(BaseAnalyticsWebSpringBootIT.TEST_PUBLISHER_TOPIC_URL);
    }

    @Bean
    public Integer processingBatchSize() {
        return 1;
    }


    @Bean
    public IntegrationFlow noOperationMrFlow(final QueueChannel mrSubscriberOutputChannel,
                                             final DirectChannel mrPublisherInputChannel) {
        return IntegrationFlows.from(mrSubscriberOutputChannel)
                .log(LoggingHandler.Level.INFO)
                .channel(mrPublisherInputChannel)
                .get();
    }

}
