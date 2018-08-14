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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;

/**
 * TCA Calculator combines all TCA calculation functions into one single function
 *
 * @author Rajiv Singla
 */
public class TcaCalculator implements TcaCalculationFunction {

    private static final List<TcaCalculationFunction> TCA_CALCULATOR_FUNCTIONS_CHAIN =
            Stream.of(
                    new TcaEventListenerExtractor(),
                    new TcaDomainFilter(),
                    new TcaEventNameFilter(),
                    new TcaThresholdViolationCalculator(),
                    new TcaAbatementCalculator(),
                    new TcaAlertCreationFunction(),
                    new TcaAaiEnrichmentFunction()).collect(Collectors.toList());

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        TcaExecutionContext accumulator = tcaExecutionContext;

        for (TcaCalculationFunction tcaCalculationFunction : TCA_CALCULATOR_FUNCTIONS_CHAIN) {
            if (!accumulator.getTcaProcessingContext().isContinueProcessing()) {
                // do early termination
                break;
            } else {
                accumulator = tcaCalculationFunction.apply(accumulator);
            }
        }
        return accumulator;
    }
}
