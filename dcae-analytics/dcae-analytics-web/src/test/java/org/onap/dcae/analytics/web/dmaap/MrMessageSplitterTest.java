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

package org.onap.dcae.analytics.web.dmaap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.onap.dcae.analytics.model.AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY;
import static org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion.ANALYTICS_MODEL_OBJECT_MAPPER;

class MrMessageSplitterTest extends BaseAnalyticsWebTest {

    @Test
    @SuppressWarnings("unchecked")
    void splitMessage() {

        final Map<String, Object> headers = new HashMap<>();
        headers.put(REQUEST_ID_HEADER_KEY, "testRequestId");
        final MessageHeaders messageHeaders = new MessageHeaders(headers);

        final Message<?> message =
                MessageBuilder.createMessage(fromStream(TestFileLocation.CEF_JSON_MESSAGES), messageHeaders);

        final MrMessageSplitter mrMessageSplitter = new MrMessageSplitter(ANALYTICS_MODEL_OBJECT_MAPPER, 10);
        final Object splitMessageStream = mrMessageSplitter.splitMessage(message);

        final List<String> messages = ((Stream<String>) splitMessageStream).collect(Collectors.toList());
        Assertions.assertThat(messages.size()).isEqualTo(4);
    }
}
