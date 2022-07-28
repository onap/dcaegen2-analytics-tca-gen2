/*
 * ==========LICENSE_START=========================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * Copyright (c) 2022 Wipro Limited Intellectual Property. All rights reserved.
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

package org.onap.dcae.analytics.tca.core;

import java.util.List;

import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion;
import org.onap.dcae.analytics.tca.core.domain.TestTcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.core.domain.TestTcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaExecutionContext.GenericTcaExecutionContextBuilder;
import org.onap.dcae.analytics.tca.core.service.GenericTcaProcessingContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaResultContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaProcessingContext;
import org.onap.dcae.analytics.tca.core.service.TcaResultContext;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion;
import org.onap.dcae.analytics.test.BaseAnalyticsUnitTest;

/**
 * @author Rajiv Singla
 */
public abstract class BaseTcaCoreTest extends BaseAnalyticsUnitTest {


    protected static final String TEST_POLICY_JSON_STRING;
    protected static final String TEST_CEF_EVENT_LISTENER_STRING;
    protected static final String TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING;
    protected static final String TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING;
    protected static final String TEST_CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME;
    protected static final String TEST_REQUEST_ID = "testRequestId";
    protected static final List<TcaPolicy> TEST_TCA_POLICY;

    static {

        TEST_POLICY_JSON_STRING = fromStream(TestFileLocation.TCA_POLICY_JSON);
        TEST_CEF_EVENT_LISTENER_STRING = fromStream(TestFileLocation.CEF_JSON_MESSAGE);
        TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING = fromStream(TestFileLocation.CEF_JSON_MESSAGE_WITH_VIOLATION);
        TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING = fromStream(TestFileLocation.CEF_JSON_MESSAGE_WITH_ABATEMENT);
        TEST_CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME = fromStream(TestFileLocation
                .CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME);
        TEST_TCA_POLICY = TcaModelJsonConversion.TCA_POLICY_JSON_FUNCTION.apply(TEST_POLICY_JSON_STRING)
                .orElseThrow(() -> new IllegalStateException("Unable to get Test TcaPolicy"));
    }

    /**
     * Provides Event Listener that can be used for testing purposes
     *
     * @return test Event Listener
     */
    protected EventListener getTestEventListener() {
        return AnalyticsModelJsonConversion.EVENT_LISTENER_JSON_FUNCTION
                .apply(TEST_CEF_EVENT_LISTENER_STRING)
                .orElseThrow(() -> new IllegalStateException("Unable to get Test CEF Event Listener"));
    }

    /**
     * Provides Event Listener with violation that can be used for testing purposes
     *
     * @return test Event Listener with violation
     */
    protected EventListener getTestEventListenerWithViolation() {
        return AnalyticsModelJsonConversion.EVENT_LISTENER_JSON_FUNCTION
                .apply(TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING)
                .orElseThrow(() -> new IllegalStateException("Unable to get Test CEF Event Listeners with violation"));
    }


    /**
     * Provides Event Listener with abatement that can be used for testing purposes
     *
     * @return test Event Listener with abatement
     */
    protected EventListener getTestEventListenerWithAbatement() {
        return AnalyticsModelJsonConversion.EVENT_LISTENER_JSON_FUNCTION
                .apply(TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING)
                .orElseThrow(() -> new IllegalStateException("Unable to get Test CEF Event Listeners with abatement"));
    }


    protected TcaExecutionContext getTestExecutionContext(final String cefMessage) {
        final TestTcaAbatementContext testTcaAbatementContext = new TestTcaAbatementContext();
        return getTestExecutionContextBuilder(cefMessage, TEST_TCA_POLICY, testTcaAbatementContext).build();
    }

    protected GenericTcaExecutionContextBuilder getTestExecutionContextBuilder(
            final String cefMessage, final List<TcaPolicy> tcaPolicy, final TcaAbatementContext tcaAbatementContext) {

        final TcaProcessingContext tcaProcessingContext = new GenericTcaProcessingContext();
        final TcaResultContext tcaResultContext = new GenericTcaResultContext();
        final TestTcaAaiEnrichmentContext testTcaAaiEnrichmentContext = new TestTcaAaiEnrichmentContext();

        return GenericTcaExecutionContext.builder()
                .requestId(TEST_REQUEST_ID)
                .cefMessage(cefMessage)
                .tcaPolicy(tcaPolicy)
                .tcaProcessingContext(tcaProcessingContext)
                .tcaResultContext(tcaResultContext)
                .tcaAbatementContext(tcaAbatementContext)
                .tcaAaiEnrichmentContext(testTcaAaiEnrichmentContext);
    }

}
