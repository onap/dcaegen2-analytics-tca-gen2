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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.net.URL;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.onap.dcae.analytics.model.DmaapMrConstants;
import org.onap.dcae.analytics.web.http.BaseHttpClientPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * DMaaP MR Subscriber config
 *
 * @author Rajiv Singla
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MrSubscriberPreferences extends BaseHttpClientPreferences {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MrSubscriberPreferences.class);

    private String consumerGroup;
    private List<String> consumerIds;
    private Integer messageLimit;
    private Integer timeout;
    private MrSubscriberPollingPreferences pollingPreferences;

    public MrSubscriberPreferences(@Nonnull final String requestURL) {
        super(requestURL);
    }

    public MrSubscriberPreferences(@Nonnull final String requestURL,
                                   @Nullable final String httpClientId,
                                   @Nullable final HttpHeaders httpHeaders,
                                   @Nullable final String username,
                                   @Nullable final String password,
                                   @Nullable final URL proxyURL,
                                   @Nullable final Boolean ignoreSSLValidation,
                                   @Nullable final Boolean enableEcompAuditLogging,
                                   @Nullable final String consumerGroup,
                                   @Nullable final List<String> consumerIds,
                                   @Nullable final Integer messageLimit,
                                   @Nullable final Integer timeout,
                                   @Nullable final MrSubscriberPollingPreferences pollingPreferences) {
        super(requestURL, httpClientId, httpHeaders, username, password, proxyURL,
                ignoreSSLValidation, enableEcompAuditLogging);
        this.consumerGroup = consumerGroup;
        this.consumerIds = consumerIds;
        this.messageLimit = messageLimit;
        this.timeout = timeout;
        this.pollingPreferences = pollingPreferences;
    }


    public MrSubscriberPollingPreferences getPollingPreferences() {
        if (pollingPreferences == null) {
            logger.warn("DMaaP MR Subscriber Polling details are missing. " +
                            "Fixed polling rate will be used by default with polling interval: {}",
                    DmaapMrConstants.SUBSCRIBER_DEFAULT_FIXED_POLLING_INTERVAL);
            setFixedPollingRate(DmaapMrConstants.SUBSCRIBER_DEFAULT_FIXED_POLLING_INTERVAL);
        }
        return pollingPreferences;
    }

    private void setFixedPollingRate(final int fixedPollingInterval) {
        this.pollingPreferences =
                new MrSubscriberPollingPreferences(fixedPollingInterval, 0, fixedPollingInterval, 0);
    }

}
