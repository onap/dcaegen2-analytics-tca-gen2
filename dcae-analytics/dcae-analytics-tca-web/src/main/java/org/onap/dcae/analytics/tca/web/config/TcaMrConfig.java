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

package org.onap.dcae.analytics.tca.web.config;

import static org.onap.dcae.analytics.model.AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY;
import static org.onap.dcae.analytics.model.AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY;

import java.util.List;
import java.util.Map;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;
import org.onap.dcae.analytics.tca.web.integration.TcaAlertTransformer;
import org.onap.dcae.analytics.tca.web.integration.TcaPublisherResponseHandler;
import org.onap.dcae.analytics.tca.web.service.TcaProcessingService;
import org.onap.dcae.analytics.tca.web.util.function.TcaAppPropsToMrPublisherPrefsFunction;
import org.onap.dcae.analytics.tca.web.util.function.TcaAppPropsToMrSubscriberPrefsFunction;
import org.onap.dcae.analytics.web.dmaap.MrPublisherPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageHeaders;

/**
 * @author Rajiv Singla
 */
@Profile({AnalyticsProfile.DMAAP_PROFILE_NAME})
@Configuration
public class TcaMrConfig {

    @Bean
    public MrSubscriberPreferences mrSubscriberPreferences(final TcaAppProperties tcaAppProperties) {
        return new TcaAppPropsToMrSubscriberPrefsFunction().apply(tcaAppProperties);
    }

    @Bean
    public MrPublisherPreferences mrPublisherPreferences(final TcaAppProperties tcaAppProperties) {
        return new TcaAppPropsToMrPublisherPrefsFunction().apply(tcaAppProperties);
    }

    @Bean
    public Integer processingBatchSize(final TcaAppProperties tcaAppProperties) {
        return tcaAppProperties.getTca().getProcessingBatchSize();
    }

    @Bean
    public TcaAlertTransformer tcaAlertTransformer(final TcaAppProperties tcaAppProperties) {
        return new TcaAlertTransformer(tcaAppProperties);
    }

    @Bean
    public IntegrationFlow tcaMrFlow(final TcaPolicyWrapper tcaPolicyWrapper,
                                     final QueueChannel mrSubscriberOutputChannel,
                                     final DirectChannel mrPublisherInputChannel,
                                     final TcaProcessingService tcaProcessingService,
                                     final TcaAlertTransformer tcaAlertTransformer) {
        // get messages from dmaap subscriber channel
        return IntegrationFlows.from(mrSubscriberOutputChannel)
                // handle incoming message from dmaap
                .handle((List<String> cefMessages, Map<String, Object> headers) ->
                        tcaProcessingService.getTcaExecutionResults(
                                headers.getOrDefault(REQUEST_ID_HEADER_KEY, headers.get(MessageHeaders.ID)).toString(),
                                headers.getOrDefault(REQUEST_TRANSACTION_ID_HEADER_KEY, "").toString(),
                                tcaPolicyWrapper.getTcaPolicy(), cefMessages))
                // transform tca execution results to alerts - if not alerts are detected terminate further processing
                .transform(tcaAlertTransformer, c -> c.requiresReply(false))
                // post messages to dmaap publisher input channel
                .channel(mrPublisherInputChannel)
                .get();
    }


    @Bean
    public TcaPublisherResponseHandler tcaPublisherResponseHandler(final TcaAppProperties tcaAppProperties) {
        return new TcaPublisherResponseHandler(tcaAppProperties);
    }

    @Bean
    public IntegrationFlow tcaPublisherResponseFlow(final TcaPublisherResponseHandler tcaPublisherResponseHandler) {
        return IntegrationFlows.from(DmaapMrConstants.DMAAP_MR_PUBLISHER_OUTPUT_CHANNEL)
                // log response from dmaap publisher output channel
                .handle(tcaPublisherResponseHandler)
                // finish processing
                .channel(new NullChannel())
                .get();
    }

}
