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

package org.onap.dcae.analytics.tca.web;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.tca.web.config.TcaWebTestConfig;
import org.onap.dcae.analytics.test.BaseAnalyticsSpringBootIT;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Rajiv Singla
 */
@ActiveProfiles({AnalyticsProfile.DEV_PROFILE_NAME})
@ContextConfiguration(classes = {TcaWebTestConfig.class})
public abstract class BaseTcaWebSpringBootIT extends BaseAnalyticsSpringBootIT {

    protected static final String TEST_POLICY_JSON_STRING;
    protected static final String TEST_CEF_EVENT_LISTENER_STRING;
    protected static final String TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING;
    protected static final String TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING;
    protected static final String TEST_CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME;
    protected static final String TEST_REQUEST_ID = "testRequestId";
    protected static final String TEST_TRANSACTION_ID = "testTransactionId";

    static {

        TEST_POLICY_JSON_STRING = fromStream(TestFileLocation.TCA_POLICY_JSON);
        TEST_CEF_EVENT_LISTENER_STRING = fromStream(TestFileLocation.CEF_JSON_MESSAGE);
        TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING = fromStream(TestFileLocation.CEF_JSON_MESSAGE_WITH_VIOLATION);
        TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING = fromStream(TestFileLocation.CEF_JSON_MESSAGE_WITH_ABATEMENT);
        TEST_CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME = fromStream(TestFileLocation
                .CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME);

    }

}
