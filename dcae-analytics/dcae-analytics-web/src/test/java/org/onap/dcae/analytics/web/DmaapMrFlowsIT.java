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

package org.onap.dcae.analytics.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Rajiv Singla
 */
@ActiveProfiles(value = {AnalyticsProfile.DMAAP_PROFILE_NAME})
@Disabled
class DmaapMrFlowsIT extends BaseAnalyticsWebSpringBootIT {

    @Autowired
    private QueueChannel mrSubscriberOutputChannel;
    @Autowired
    private DirectChannel mrPublisherInputChannel;

    @Test
    void mrPubSubFlows() {

        try {
            Thread.sleep(300_000);
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }


}
