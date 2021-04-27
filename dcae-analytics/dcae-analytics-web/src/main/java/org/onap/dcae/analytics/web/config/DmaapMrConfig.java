
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
import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.onap.dcae.analytics.web.dmaap.MrMessageSplitter;
import org.onap.dcae.analytics.web.dmaap.MrPublisherPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingAdvice;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.onap.dcae.analytics.web.dmaap.MrTriggerMessageProvider;
import org.onap.dcae.analytics.web.http.HttpClientPreferencesCustomizer;
import org.onap.dcae.analytics.web.util.AnalyticsWebUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.endpoint.MethodInvokingMessageSource;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * @author Rajiv Singla
 */
@Configuration
@Import(value = {DmaapPollerConfig.class, DmaapRetryConfig.class})
@Profile(AnalyticsProfile.DMAAP_PROFILE_NAME)
public class DmaapMrConfig {
    private static final String[] DMAAP_MAPPED_REQUEST_HEADERS =
            DmaapMrConstants.getDmaapmappedHeaders().toArray(new String[DmaapMrConstants.getDmaapmappedHeaders().size()]);
                .channel(mrSubscriberOutputChannel)
                .get();
    }
    @Bean
    public IntegrationFlow mrPublisherFlow(final MrPublisherPreferences mrPublisherPreferences,
                                           final RestTemplate mrPublisherRestTemplate,
                                           final DirectChannel mrPublisherInputChannel,
                                           final RequestHandlerRetryAdvice requestHandlerRetryAdvice) {
        return IntegrationFlows.from(mrPublisherInputChannel)
                .handle(Http.outboundGateway(mrPublisherPreferences.getRequestURL(), mrPublisherRestTemplate)
                        .mappedRequestHeaders(DMAAP_MAPPED_REQUEST_HEADERS)
                        .httpMethod(HttpMethod.POST)
                        .extractPayload(true)
                        .expectedResponseType(String.class), c -> c.advice(requestHandlerRetryAdvice))
//                // add end timestamp
//                .handle((String p, Map<String, Object> headers) ->
//                        MessageBuilder.withPayload(p).copyHeaders(headers)
//                                .setHeader(AnalyticsHttpConstants.REQUEST_END_TS_HEADER_KEY,
//                                        AnalyticsWebUtils.CREATION_TIMESTAMP_SUPPLIER.get()).build()
                .handle(new GenericHandler<String>() {
                    @Override
                    public Object handle(String payload, MessageHeaders headers) {
                          MessageBuilder.withPayload(payload).copyHeaders(headers)
                          .setHeader(AnalyticsHttpConstants.REQUEST_END_TS_HEADER_KEY,
                                  AnalyticsWebUtils.CREATION_TIMESTAMP_SUPPLIER.get()).build();
                            return payload;
                    }                    
                })
                .channel(DmaapMrConstants.DMAAP_MR_PUBLISHER_OUTPUT_CHANNEL)
                .get();
    }
}

