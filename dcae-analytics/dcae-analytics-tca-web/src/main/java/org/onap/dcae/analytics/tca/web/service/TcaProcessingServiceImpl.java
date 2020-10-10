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

package org.onap.dcae.analytics.tca.web.service;

import static org.onap.dcae.analytics.web.util.AnalyticsWebUtils.RANDOM_ID_SUPPLIER;
import static org.onap.dcae.analytics.web.util.AnalyticsWebUtils.REQUEST_ID_SUPPLIER;
import static org.onap.dcae.analytics.web.util.ValidationUtils.isPresent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.onap.dcae.analytics.tca.core.service.GenericTcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaProcessingContext;
import org.onap.dcae.analytics.tca.core.service.GenericTcaResultContext;
import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.util.TcaUtils;
import org.onap.dcae.analytics.tca.core.util.function.calculation.TcaCalculator;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;

/**
 * @author Rajiv Singla
 */
public class TcaProcessingServiceImpl implements TcaProcessingService {

    private final TcaAbatementContext tcaAbatementContext;
    private final TcaAaiEnrichmentContext tcaAaiEnrichmentContext;

    public TcaProcessingServiceImpl(final TcaAbatementContext tcaAbatementContext,
                                    final TcaAaiEnrichmentContext tcaAaiEnrichmentContext) {
        this.tcaAbatementContext = tcaAbatementContext;
        this.tcaAaiEnrichmentContext = tcaAaiEnrichmentContext;
    }


    @Override
    public List<TcaExecutionContext> getTcaExecutionResults(final String requestId,
                                                            final String transactionId,
                                                            final TcaPolicyWrapper tcaPolicyWrapper,
                                                            final List<String> cefMessages) {
        // create tca policy deep copy as it should be same for current execution
        final TcaPolicy tcaPolicyDeepCopy = TcaUtils.getTcaPolicyDeepCopy(tcaPolicyWrapper.getTcaPolicy());
        // create new request id if not present
        final String executionRequestId = isPresent(requestId) ? requestId : REQUEST_ID_SUPPLIER.get();
        // create transaction id if not present
        final String executionTransactionId = isPresent(transactionId) ? transactionId : RANDOM_ID_SUPPLIER.get();

        return IntStream.range(0, cefMessages.size())
                // generate initial Processing contexts
                .mapToObj(cefMessageIndex -> GenericTcaExecutionContext.builder()
                        .requestId(executionRequestId)
                        .transactionId(executionTransactionId)
                        .messageIndex(cefMessageIndex)
                        .cefMessage(cefMessages.get(cefMessageIndex))
                        .tcaPolicy(tcaPolicyDeepCopy)
                        .tcaProcessingContext(new GenericTcaProcessingContext())
                        .tcaAbatementContext(tcaAbatementContext)
                        .tcaAaiEnrichmentContext(tcaAaiEnrichmentContext)
                        .tcaResultContext(new GenericTcaResultContext())
                        .build())
                // apply tca calculator
                .map(new TcaCalculator())
                // return result
                .collect(Collectors.toList());
    }
}
