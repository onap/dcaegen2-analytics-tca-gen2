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

package org.onap.dcae.analytics.tca.core.util.function.calculation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.tca.core.BaseTcaCoreTest;
import org.onap.dcae.analytics.tca.core.domain.TestTcaAbatementContext;
import org.onap.dcae.analytics.tca.core.domain.TestTcaAbatementEntity;
import org.onap.dcae.analytics.tca.core.domain.TestTcaAbatementRepository;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;


/**
 * @author Rajiv Singla
 */
class TcaCalculatorTest extends BaseTcaCoreTest {

    @Test
    @DisplayName("Test TCA calculator with CEF Message without violation")
    void testCEFMessageWithoutViolation() {
        final TcaExecutionContext testExecutionContext =
                getTestExecutionContext(TEST_CEF_EVENT_LISTENER_STRING);
        final TcaExecutionContext result = new TcaCalculator().apply(testExecutionContext);
        assertThat(result.getTcaProcessingContext().isContinueProcessing()).isFalse();
        assertThat(result.getTcaProcessingContext().getErrorMessage()).isNull();
        final String earlyTerminationMessage = result.getTcaProcessingContext().getEarlyTerminationMessage();
        assertThat(earlyTerminationMessage).isNotNull();
        logger.debug(earlyTerminationMessage);
        assertThat(result.getTcaResultContext().isThresholdViolationsPresent()).isFalse();
    }

    @Test
    @DisplayName("Test TCA calculator with CEF Message with violation")
    void testCEFMessageWithViolation() {
        final TcaExecutionContext testExecutionContext =
                getTestExecutionContext(TEST_CEF_JSON_MESSAGE_WITH_VIOLATION_STRING);
        final TcaExecutionContext result = new TcaCalculator().apply(testExecutionContext);
        assertThat(result.getTcaProcessingContext().isContinueProcessing()).isTrue();
        assertThat(result.getTcaResultContext().isThresholdViolationsPresent()).isTrue();
        assertThat(result.getTcaResultContext().getTcaAlert().getRequestId()).isEqualTo(TEST_REQUEST_ID);
    }


    @Test
    @DisplayName("Test TCA calculator with Abatement")
    void testCEFMessageWithAbatement() {
        final TestTcaAbatementContext testTcaAbatementContext = new TestTcaAbatementContext();
        final TestTcaAbatementRepository testAbatementPersistenceContext =
                (TestTcaAbatementRepository) testTcaAbatementContext.getTcaAbatementRepository();
        final String previousRequestId = "previousRequestId";
        final TestTcaAbatementEntity testTcaAbatementPersistenceEntity
                = new TestTcaAbatementEntity(new Date(), "", previousRequestId, false);
        testAbatementPersistenceContext.setTestLookupAbatementEntities(
                Collections.singletonList(testTcaAbatementPersistenceEntity));

        final TcaExecutionContext testExecutionContext =
                getTestExecutionContextBuilder(TEST_CEF_JSON_MESSAGE_WITH_ABATEMENT_STRING,
                        TEST_TCA_POLICY, testTcaAbatementContext).build();

        final TcaExecutionContext result = new TcaCalculator().apply(testExecutionContext);
        assertThat(result.getTcaResultContext().getTcaAlert().getRequestId()).isEqualTo(previousRequestId);
    }


}
