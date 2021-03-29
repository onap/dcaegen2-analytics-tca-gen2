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
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingAdvice;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPollingPreferences;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.util.DynamicPeriodicTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

/**
 * @author Rajiv Singla
 */
@Configuration
@Profile(AnalyticsProfile.DMAAP_PROFILE_NAME)
public class DmaapPollerConfig {

    @Bean
    public MrSubscriberPollingAdvice mrSubscriberPollingAdvice(final DynamicPeriodicTrigger dynamicPeriodicTrigger,
                                                               final MrSubscriberPreferences mrSubscriberPreferences) {
        final MrSubscriberPollingPreferences pollingPreferences = mrSubscriberPreferences.getPollingPreferences();
        final int minInterval = pollingPreferences.getMinPollingInterval();
        final int stepUpDelta = pollingPreferences.getStepUpDelta();
        final int maxInterval = pollingPreferences.getMaxPollingInterval();
        final int stepDownDelta = pollingPreferences.getStepDownDelta();
        return new MrSubscriberPollingAdvice(dynamicPeriodicTrigger, minInterval,
                stepUpDelta, maxInterval, stepDownDelta);
    }

    @Bean
    public DynamicPeriodicTrigger dynamicPeriodicTrigger(final MrSubscriberPreferences mrSubscriberPreferences) {
        final MrSubscriberPollingPreferences pollingPreferences = mrSubscriberPreferences.getPollingPreferences();
        final int minInterval = pollingPreferences.getMinPollingInterval();
        final DynamicPeriodicTrigger dynamicPeriodicTrigger =
                new DynamicPeriodicTrigger(minInterval);
        dynamicPeriodicTrigger.setFixedRate(true);
        return dynamicPeriodicTrigger;
    }

    @Bean
    public PollerMetadata pollerMetadata(final DynamicPeriodicTrigger dynamicPeriodicTrigger) {
        final PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(dynamicPeriodicTrigger);
        return pollerMetadata;
    }


    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(1000));
        return pollerMetadata;
    }

}

