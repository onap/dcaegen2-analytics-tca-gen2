/*
 * ================================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * Copyright (c) 2021 China Mobile Property. All rights reserved.
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

import java.util.LinkedHashMap;
import java.util.Map;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.integration.store.MessageGroupQueue;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.PollableChannel;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

/**
 * @author Rajiv Singla
 */
@Configuration
@Profile(AnalyticsProfile.DMAAP_PROFILE_NAME)
@Import(MessageStoreConfig.class)
public class DmaapRetryConfig {

    @Bean
    public QueueChannel errorChannel() {
        return MessageChannels.queue().get();
    }

    @Bean
    public IntegrationFlow loggingFlow() {
        return IntegrationFlows.from(errorChannel())
                .log(LoggingHandler.Level.ERROR)
                .get();
    }

    @Bean
    public ErrorMessageSendingRecoverer errorMessageSendingRecoverer(final PollableChannel recoveryChannel) {
        final ErrorMessageSendingRecoverer errorMessageSendingRecoverer = new ErrorMessageSendingRecoverer();
        errorMessageSendingRecoverer.setChannel(recoveryChannel);
        return errorMessageSendingRecoverer;
    }

    @Bean
    public PollableChannel recoveryChannel(final BasicMessageGroupStore basicMessageGroupStore) {
        return MessageChannels.queue(new MessageGroupQueue(basicMessageGroupStore,
                DmaapMrConstants.DMAAP_MR_PUBLISHER_RECOVERY_MESSAGE_STORE_GROUP_ID)).get();
    }

    @Bean
    public RequestHandlerRetryAdvice requestHandlerRetryAdvice(final RetryTemplate retryTemplate,
                                                               final ErrorMessageSendingRecoverer
                                                                       errorMessageSendingRecoverer) {
        final RequestHandlerRetryAdvice requestHandlerRetryAdvice = new RequestHandlerRetryAdvice();
        requestHandlerRetryAdvice.setRetryTemplate(retryTemplate);
        requestHandlerRetryAdvice.setRecoveryCallback(errorMessageSendingRecoverer);
        return requestHandlerRetryAdvice;
    }

    @Bean
    public RetryTemplate retryTemplate(final RetryPolicy retryPolicy,
                                       final BackOffPolicy backOffPolicy) {
        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public RetryPolicy retryPolicy() {
        final Map<Class<? extends Throwable>, Boolean> retryableExceptions = new LinkedHashMap<>();
        retryableExceptions.put(MessageHandlingException.class, true);
        retryableExceptions.put(HttpStatusCodeException.class, true);
        retryableExceptions.put(RestClientException.class, true);
        return new SimpleRetryPolicy(DmaapMrConstants.DEFAULT_NUM_OF_RETRIES_ON_FAILURE, retryableExceptions);
    }

    @Bean
    public BackOffPolicy backOffPolicy() {
        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(DmaapMrConstants.DEFAULT_RETRY_INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(DmaapMrConstants.DEFAULT_RETRY_MULTIPLIER);
        backOffPolicy.setMaxInterval(DmaapMrConstants.DEFAULT_RETRY_MAX_INTERVAL);
        return backOffPolicy;
    }

}
