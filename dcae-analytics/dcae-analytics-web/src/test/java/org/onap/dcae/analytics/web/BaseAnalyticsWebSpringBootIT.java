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

import java.util.Arrays;
import java.util.List;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.test.BaseAnalyticsSpringBootIT;
import org.onap.dcae.analytics.web.config.AnalyticsWebTestConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Rajiv Singla
 */
@ActiveProfiles({AnalyticsProfile.DEV_PROFILE_NAME})
@ContextConfiguration(classes = {AnalyticsWebTestConfig.class})
public abstract class BaseAnalyticsWebSpringBootIT extends BaseAnalyticsSpringBootIT {

    public static final String TEST_SUBSCRIBER_TOPIC_URL = "http://localhost:8080/events/SubTopic";
    public static final String TEST_SUBSCRIBER_AAF_USERNAME = "USER";
    public static final String TEST_SUBSCRIBER_AAF_PASSWORD = "PASSWORD";

    public static final String TEST_SUBSCRIBER_CONSUMER_GROUP = "cg1";
    public static final List<String> TEST_SUBSCRIBER_CONSUMER_IDS = Arrays.asList("c0", "c1");


    public static final String TEST_PUBLISHER_TOPIC_URL = "http://localhost:8080/events/PubTopic";

}
