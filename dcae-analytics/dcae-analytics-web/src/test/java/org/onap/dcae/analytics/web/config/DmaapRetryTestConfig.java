/*
 * ============LICENSE_START=======================================================
 * Copyright (c) 2022 Huawei. All rights reserved.
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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.messaging.PollableChannel;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DmaapRetryTestConfig {

    @Test
    public void errorChannelTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        QueueChannel queueChannel = dmaapRetryConfig.errorChannel();
        assertNotNull(queueChannel);
    }

    @Test
    public void errorMessageSendingRecovererTest () throws Exception {
        PollableChannel pollableChannel = Mockito.mock(PollableChannel.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        ErrorMessageSendingRecoverer errorMessageSendingRecoverer =
                dmaapRetryConfig.errorMessageSendingRecoverer(pollableChannel);
        Throwable throwable = new Throwable("test");
        assertEquals("test", errorMessageSendingRecoverer.getErrorMessageStrategy()
                .buildErrorMessage(throwable, null).getPayload().getMessage());
    }

    @Test
    public void recoveryChannelTest () throws Exception {
        BasicMessageGroupStore basicMessageGroupStore = Mockito.mock(BasicMessageGroupStore.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        PollableChannel pollableChannel = dmaapRetryConfig.recoveryChannel(basicMessageGroupStore);
        assertNotNull(pollableChannel);
    }

    @Test
    public void requestHandlerRetryAdviceTest () throws Exception {
        RetryTemplate retryTemplate = Mockito.mock(RetryTemplate.class);
        ErrorMessageSendingRecoverer errorMessageSendingRecoverer = Mockito.mock(ErrorMessageSendingRecoverer.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        RequestHandlerRetryAdvice requestHandlerRetryAdvice = dmaapRetryConfig.requestHandlerRetryAdvice(retryTemplate,
                errorMessageSendingRecoverer);
        assertNotNull(requestHandlerRetryAdvice);
    }

    @Test
    public void retryTemplateTest () throws Exception {
        RetryPolicy retryPolicy = Mockito.mock(RetryPolicy.class);
        BackOffPolicy backOffPolicy = Mockito.mock(BackOffPolicy.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        RetryTemplate retryTemplate = dmaapRetryConfig.retryTemplate(retryPolicy, backOffPolicy);
        assertNotNull(retryTemplate);
    }

    @Test
    public void retryPolicyTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        RetryPolicy retryPolicy = dmaapRetryConfig.retryPolicy();
        assertNotNull(retryPolicy);
    }

    @Test
    public void backoffPolicyTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        BackOffPolicy backOffPolicy = dmaapRetryConfig.backOffPolicy();
        assertNotNull(backOffPolicy);
    }

    @Test
    public void loggingFlowTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        IntegrationFlow integrationFlow = dmaapRetryConfig.loggingFlow();
        assertNotNull(integrationFlow);
    }
}
