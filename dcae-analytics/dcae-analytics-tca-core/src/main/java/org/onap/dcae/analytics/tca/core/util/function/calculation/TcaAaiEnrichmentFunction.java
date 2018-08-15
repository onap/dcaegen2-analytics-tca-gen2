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

import java.util.Optional;

import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentService;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.policy.ClosedLoopEventStatus;

/**
 * @author Rajiv Singla
 */
public class TcaAaiEnrichmentFunction implements TcaCalculationFunction {

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        final TcaAaiEnrichmentContext aaiEnrichmentContext = tcaExecutionContext.getTcaAaiEnrichmentContext();
        final Optional<TcaAlert> tcaAlertOptional =
                Optional.ofNullable(tcaExecutionContext.getTcaResultContext().getTcaAlert());

        // Skip Aai Enrichment - if aai enrichment is not enabled or no alert is present
        if (!aaiEnrichmentContext.isAaiEnrichmentEnabled() || !tcaAlertOptional.isPresent()) {
            return tcaExecutionContext;
        }

        // Skip Aai Enrichment - if Alert Closed Loop Event status is not ONSET
        final TcaAlert tcaAlert = tcaAlertOptional.get();
        final ClosedLoopEventStatus closedLoopEventStatus =
                ClosedLoopEventStatus.valueOf(tcaAlert.getClosedLoopEventStatus());
        if (closedLoopEventStatus != ClosedLoopEventStatus.ONSET) {
            return tcaExecutionContext;
        }

        // do Aai Enrichment
        final TcaAaiEnrichmentService aaiEnrichmentService = aaiEnrichmentContext.getAaiEnrichmentService();
        final TcaAlert enrichedTcaAlert = aaiEnrichmentService.doAaiEnrichment(tcaExecutionContext);
        tcaExecutionContext.getTcaResultContext().setTcaAlert(enrichedTcaAlert);

        return tcaExecutionContext;
    }
}
