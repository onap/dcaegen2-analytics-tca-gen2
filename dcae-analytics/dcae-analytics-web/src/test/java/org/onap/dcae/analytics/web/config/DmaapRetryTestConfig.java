/*
 * ================================================================================
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
import org.springframework.integration.handler.advice.ErrorMessageSendingRecoverer;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.messaging.PollableChannel;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

public class DmaapRetryTestConfig {

    @Test
    public void errorChannelTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.errorChannel();
    }

    @Test
    public void errorMessageSendingRecovererTest () throws Exception {
        PollableChannel pollableChannel = Mockito.mock(PollableChannel.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.errorMessageSendingRecoverer(pollableChannel);
    }

    @Test
    public void recoveryChannelTest () throws Exception {
        BasicMessageGroupStore basicMessageGroupStore = Mockito.mock(BasicMessageGroupStore.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.recoveryChannel(basicMessageGroupStore);
    }

    @Test
    public void requestHandlerRetryAdviceTest () throws Exception {
        RetryTemplate retryTemplate = Mockito.mock(RetryTemplate.class);
        ErrorMessageSendingRecoverer errorMessageSendingRecoverer = Mockito.mock(ErrorMessageSendingRecoverer.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.requestHandlerRetryAdvice(retryTemplate, errorMessageSendingRecoverer);
    }

    @Test
    public void retryTemplateTest () throws Exception {
        RetryPolicy retryPolicy = Mockito.mock(RetryPolicy.class);
        BackOffPolicy backOffPolicy = Mockito.mock(BackOffPolicy.class);
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.retryTemplate(retryPolicy, backOffPolicy);
    }

    @Test
    public void retryPolicyTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.retryPolicy();
    }

    @Test
    public void backoffPolicyTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.backOffPolicy();
    }

    @Test
    public void loggingFlowTest () throws Exception {
        DmaapRetryConfig dmaapRetryConfig = new DmaapRetryConfig();
        dmaapRetryConfig.loggingFlow();
    }
}
