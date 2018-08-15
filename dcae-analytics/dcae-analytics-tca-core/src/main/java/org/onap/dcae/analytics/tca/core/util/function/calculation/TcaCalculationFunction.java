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

import java.util.function.Function;

import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaProcessingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Functional interface which all TCA calculation functions should implement
 *
 * @author Rajiv Singla
 */
@FunctionalInterface
public interface TcaCalculationFunction extends Function<TcaExecutionContext, TcaExecutionContext> {

    Logger logger = LoggerFactory.getLogger(TcaCalculationFunction.class);

    TcaExecutionContext calculate(TcaExecutionContext tcaExecutionContext);


    default TcaExecutionContext preCalculation(TcaExecutionContext tcaExecutionContext) {
        // do nothing
        return tcaExecutionContext;
    }

    default TcaExecutionContext postCalculation(TcaExecutionContext tcaExecutionContext) {
        // do nothing
        return tcaExecutionContext;
    }

    @Override
    default TcaExecutionContext apply(TcaExecutionContext tcaExecutionContext) {
        // triggers pre calculate, calculate nad post calculate
        return postCalculation(calculate(preCalculation(tcaExecutionContext)));
    }

    /**
     * Updates TCA Processing context with early terminating message or error message
     *
     * @param terminatingMessage terminating message to be set in processing context
     * @param tcaExecutionContext current tca execution context
     * @param isErrorMessage specifies if terminating message is an error message
     *
     * @return updated tca execution context with early termination or error message and continue processing flag as
     * false
     */
    default TcaExecutionContext setTerminatingMessage(final String terminatingMessage,
                                                      final TcaExecutionContext tcaExecutionContext,
                                                      final boolean isErrorMessage) {
        final TcaProcessingContext tcaProcessingContext = tcaExecutionContext.getTcaProcessingContext();
        if (isErrorMessage) {
            logger.error("Request Id: {}. Error Message: {}", tcaExecutionContext.getRequestId(), terminatingMessage);
            tcaProcessingContext.setErrorMessage(terminatingMessage);
        } else {
            logger.debug("Request Id: {}. Early Termination Message: {}", tcaExecutionContext.getRequestId(),
                    terminatingMessage);
            tcaProcessingContext.setEarlyTerminationMessage(terminatingMessage);
        }
        tcaProcessingContext.setContinueProcessing(false);
        return tcaExecutionContext;
    }

}
