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

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.ecomplogger.AnalyticsErrorType;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaProcessingContext;
import org.onap.dcae.analytics.tca.core.util.TcaUtils;
import org.onap.dcae.utils.eelf.logger.api.info.ErrorLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.TargetServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.model.info.ErrorLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.info.TargetServiceLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.DebugLogSpecImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.ErrorLogSpecImpl;

/**
 * Functional interface which all TCA calculation functions should implement
 *
 * @author Rajiv Singla
 */
@FunctionalInterface
public interface TcaCalculationFunction extends Function<TcaExecutionContext, TcaExecutionContext> {

    EELFLogger logger = EELFLogFactory.getLogger(TcaCalculationFunction.class);

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

        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(tcaExecutionContext.getRequestId());

        if (isErrorMessage) {
            final TargetServiceLogInfo targetServiceLogInfo = new TargetServiceLogInfoImpl(
                    "DCAE-TCA", TcaModelConstants.TCA_SERVICE_NAME, "");
            final ErrorLogInfo errorLogInfo =
                    new ErrorLogInfoImpl(AnalyticsErrorType.SCHEMA_ERROR.getErrorCode(),
                            AnalyticsErrorType.SCHEMA_ERROR.getErrorDescription());
            final ErrorLogSpecImpl errorLogSpec = new ErrorLogSpecImpl(requestIdLogInfo,
                    TcaUtils.TCA_SERVICE_LOG_INFO, targetServiceLogInfo, errorLogInfo);
            logger.errorLog().error("Request Id: {}. Error Message: {}",
                    errorLogSpec, tcaExecutionContext.getRequestId(), terminatingMessage);
            tcaProcessingContext.setErrorMessage(terminatingMessage);
        } else {
            final DebugLogSpecImpl debugLogSpec = new DebugLogSpecImpl(requestIdLogInfo);
            logger.debugLog().debug("Request Id: {}. Early Termination Message: {}",
                    debugLogSpec, tcaExecutionContext.getRequestId(), terminatingMessage);
            tcaProcessingContext.setEarlyTerminationMessage(terminatingMessage);
        }
        tcaProcessingContext.setContinueProcessing(false);
        return tcaExecutionContext;
    }

}
