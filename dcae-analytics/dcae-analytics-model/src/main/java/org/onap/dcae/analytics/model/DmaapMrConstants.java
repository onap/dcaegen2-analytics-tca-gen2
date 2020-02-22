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

package org.onap.dcae.analytics.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rajiv Singla
 */
public abstract class DmaapMrConstants {

    // ================== DMaaP MR CONSTANTS ============================== //

    // MR SUBSCRIBER
    public static final String SUBSCRIBER_EMPTY_MESSAGE_RESPONSE_STRING = "[]";
    public static final String SUBSCRIBER_RANDOM_CONSUMER_GROUP_PREFIX = "DCAE-SUB-";
    public static final String SUBSCRIBER_TIMEOUT_QUERY_PARAM_NAME = "timeout";
    public static final Integer SUBSCRIBER_DEFAULT_TIMEOUT = -1;
    public static final Integer SUBSCRIBER_DEFAULT_FIXED_POLLING_INTERVAL = 30_000;
    public static final String SUBSCRIBER_MSG_LIMIT_QUERY_PARAM_NAME = "limit";
    public static final Integer SUBSCRIBER_DEFAULT_MESSAGE_LIMIT = 50_000;
    public static final Integer SUBSCRIBER_DEFAULT_PROCESSING_BATCH_SIZE = 10_000;


    // =================== INTEGRATION DEFAULTS =========================== //

    // RETRY ON FAILURE
    public static final Integer DEFAULT_NUM_OF_RETRIES_ON_FAILURE = 5;
    public static final Integer DEFAULT_RETRY_INITIAL_INTERVAL = 1_000;
    public static final Integer DEFAULT_RETRY_MULTIPLIER = 5;
    public static final Integer DEFAULT_RETRY_MAX_INTERVAL = 300_000;

    public static final String DMAAP_MR_SUBSCRIBER_OUTPUT_CHANNEL_NAME = "mrSubscriberOutputChannel";
    public static final String DMAAP_MR_PUBLISHER_INPUT_CHANNEL = "mrPublisherInputChannel";
    public static final String DMAAP_MR_PUBLISHER_OUTPUT_CHANNEL = "mrPublisherOutputChannel";

    // MESSAGE STORE NAMES
    public static final String DMAAP_MR_SUBSCRIBER_OUTPUT_MESSAGE_STORE_GROUP_ID = "mrSubscriberMessageStoreGroup";
    public static final String DMAAP_MR_PUBLISHER_RECOVERY_MESSAGE_STORE_GROUP_ID = "mrPublisherRecoveryStoreGroup";

    private DmaapMrConstants() {
        // private constructor
    }

    /**
     * getDmaapmappedHeaders
     */
    public static final Set<String> getDmaapmappedHeaders () {
         return Stream.of(
                AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY,
                AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY,
                AnalyticsHttpConstants.REQUEST_BEGIN_TS_HEADER_KEY,
                AnalyticsHttpConstants.REQUEST_END_TS_HEADER_KEY).collect(Collectors.toSet());        
    }
}
