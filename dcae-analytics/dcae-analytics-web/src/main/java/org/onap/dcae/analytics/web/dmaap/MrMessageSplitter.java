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

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;
import static org.apache.commons.text.StringEscapeUtils.unescapeJson;
import static org.onap.dcae.analytics.model.AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.onap.dcae.analytics.tca.core.exception.AnalyticsParsingException;
import org.onap.dcae.analytics.tca.core.util.LogSpec;
import org.onap.dcae.analytics.web.util.AnalyticsHttpUtils;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.api.spec.AuditLogSpec;
import org.onap.dcae.utils.eelf.logger.api.spec.ErrorLogSpec;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * DMaaP MR message splitter split the incoming messages into batch of given batch size
 *
 * @author Rajiv Singla
 */
public class MrMessageSplitter extends AbstractMessageSplitter {

    private static final EELFLogger eelfLogger = EELFLogFactory.getLogger(MrMessageSplitter.class);

    private final ObjectMapper objectMapper;
    private final Integer batchSize;

    public MrMessageSplitter(@Nonnull final ObjectMapper objectMapper,
                             @Nonnull final Integer batchSize) {
        this.objectMapper = objectMapper;
        this.batchSize = batchSize;
    }

    @Override
    protected Object splitMessage(final Message<?> message) {

        final String requestId = AnalyticsHttpUtils.getRequestId(message.getHeaders());
        final List<String> dmaapMessages = convertJsonToStringMessages(requestId, String.class.cast(message.getPayload()).trim());

        final String transactionId = AnalyticsHttpUtils.getTransactionId(message.getHeaders());

        final Date requestBeginTimestamp = AnalyticsHttpUtils.getTimestampFromHeaders(message.getHeaders(),
                AnalyticsHttpConstants.REQUEST_BEGIN_TS_HEADER_KEY);
        final AuditLogSpec auditLogSpec = LogSpec.createAuditLogSpec(requestId, requestBeginTimestamp);

        eelfLogger.auditLog().info("Request Id: {}, Transaction Id: {}, dmaapMessages: {},"
                + " Received new messages from DMaaP MR. Count: {}",
                auditLogSpec, requestId, transactionId, dmaapMessages.toString(), String.valueOf(dmaapMessages.size()));

        final List<List<String>> messagePartitions = partition(dmaapMessages, batchSize);
        eelfLogger.auditLog().info("Request Id: {}, Transaction Id: {},  Max allowed messages per batch: {}. " +
                "No of batches created: {}",
                auditLogSpec, requestId, transactionId, String.valueOf(batchSize), String.valueOf(messagePartitions.size()));

        // append batch id to request id header
        return messagePartitions.isEmpty() ? null : IntStream.range(0, messagePartitions.size())
                .mapToObj(batchIndex ->
                        MessageBuilder
                                .withPayload(messagePartitions.get(0))
                                .copyHeaders(message.getHeaders())
                                .setHeader(REQUEST_ID_HEADER_KEY, requestId)
                                .build()

                );
    }

    /**
     * Converts DMaaP MR subscriber messages json string to List of messages. If message Json String is empty
     * or null
     *
     * @param messagesJsonString json messages String
     *
     * @return List containing DMaaP MR Messages
     */
    private List<String> convertJsonToStringMessages(String requestId, @Nullable final String messagesJsonString) {

        final LinkedList<String> messages = new LinkedList<>();

        // If message string is not null or not empty parse json message array to List of string messages
        if (messagesJsonString != null && !messagesJsonString.trim().isEmpty()
                && !DmaapMrConstants.SUBSCRIBER_EMPTY_MESSAGE_RESPONSE_STRING.equals(messagesJsonString.trim())) {

            try {
                // get root node
                final JsonNode rootNode = objectMapper.readTree(messagesJsonString);
                // iterate over root node and parse arrays messages
                for (JsonNode jsonNode : rootNode) {
                    // if array parse it is array of messages
                    final String incomingMessageString = jsonNode.toString();
                    if (jsonNode.isArray()) {
                        final List messageList = objectMapper.readValue(incomingMessageString, List.class);
                        for (Object message : messageList) {
                            final String jsonMessageString = objectMapper.writeValueAsString(message);
                            addUnescapedJsonToMessage(messages, jsonMessageString);
                        }
                    } else {
                        // parse it as object
                        addUnescapedJsonToMessage(messages, incomingMessageString);
                    }
                }

            } catch (IOException e) {
                ErrorLogSpec errorLogSpec = LogSpec.createErrorLogSpec(requestId);
                eelfLogger.errorLog().error("Unable to convert subscriber Json String to Messages. " +
                        "Subscriber Response String: {}, Json Error: {}", errorLogSpec, messagesJsonString, e.toString());
                String errorMessage = String.format("Unable to convert subscriber Json String to Messages. " +
                        "Subscriber Response String: %s, Json Error: %s", messagesJsonString, e);
                throw new AnalyticsParsingException(errorMessage, e);
            }

        }
        return messages;
    }

    /**
     * Adds unescaped Json messages to given messages list
     *
     * @param messages message list in which unescaped messages will be added
     * @param incomingMessageString incoming message string that may need to be escaped
     */
    private static void addUnescapedJsonToMessage(List<String> messages, String incomingMessageString) {
        if (incomingMessageString.startsWith("\"") && incomingMessageString.endsWith("\"")) {
            messages.add(unescapeJava(unescapeJson(
                    incomingMessageString.substring(1, incomingMessageString.length() - 1))));
        } else {
            messages.add(unescapeJava(unescapeJson(incomingMessageString)));
        }
    }

    /**
     * Partition list into multiple lists
     *
     * @param list input list that needs to be broken into chunks
     * @param batchSize batch size for each list
     * @param <E> element type of the list
     *
     * @return List containing list of entries of specified batch size
     */
    private static <E> List<List<E>> partition(List<E> list, final Integer batchSize) {

        if (list == null || batchSize == null || batchSize <= 0 || list.size() < batchSize) {
            return Collections.singletonList(list);
        }

        final List<List<E>> result = new LinkedList<>();

        for (int i = 0; i < list.size(); i++) {

            if (i == 0 || i % batchSize == 0) {
                List<E> sublist = new LinkedList<>();
                result.add(sublist);
            }

            final List<E> lastSubList = result.get(result.size() - 1);
            lastSubList.add(list.get(i));

        }
        return result;
    }
}
