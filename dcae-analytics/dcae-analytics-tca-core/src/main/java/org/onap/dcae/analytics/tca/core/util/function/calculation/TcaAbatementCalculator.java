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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.tca.core.exception.TcaProcessingException;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementEntity;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementRepository;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaProcessingContext;
import org.onap.dcae.analytics.tca.core.service.TcaResultContext;
import org.onap.dcae.analytics.tca.model.policy.ClosedLoopEventStatus;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;
import org.onap.dcae.analytics.tca.model.policy.Threshold;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.model.info.RequestIdLogInfoImpl;
import org.onap.dcae.utils.eelf.logger.model.spec.DebugLogSpecImpl;

/**
 * @author Rajiv Singla
 */
public class TcaAbatementCalculator implements TcaCalculationFunction {

    private static final EELFLogger logger = EELFLogFactory.getLogger(TcaAbatementCalculator.class);

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        final TcaAbatementContext abatementContext = tcaExecutionContext.getTcaAbatementContext();
        final TcaAbatementRepository abatementPersistenceContext = abatementContext
                .getTcaAbatementRepository();
        final TcaResultContext resultContext = tcaExecutionContext.getTcaResultContext();
        final TcaProcessingContext processingContext = tcaExecutionContext.getTcaProcessingContext();


        // Skip Abatement processing - if it is not enabled
        if (!abatementContext.isAbatementEnabled() ||
                // Skip if no threshold violations are present
                !resultContext.isThresholdViolationsPresent()) {
            return tcaExecutionContext;
        }


        final EventListener eventListener = processingContext.getEventListener();
        final MetricsPerEventName violatedMetricsPerEventName = resultContext.getViolatedMetricsPerEventName();
        final Threshold violatedThreshold = violatedMetricsPerEventName.getThresholds().get(0);
        final ClosedLoopEventStatus closedLoopEventStatus = violatedThreshold.getClosedLoopEventStatus();
        final String requestId = tcaExecutionContext.getRequestId();
        final String lookupKey = createLookupKey(eventListener, violatedMetricsPerEventName);
        final RequestIdLogInfoImpl requestIdLogInfo = new RequestIdLogInfoImpl(requestId);
        final DebugLogSpecImpl debugLogSpec = new DebugLogSpecImpl(requestIdLogInfo);
        switch (closedLoopEventStatus) {

            // ONSET - save alert info in database so that next abated event can fetch its request id for abated
            // alert
            case ONSET:

                final TcaAbatementEntity tcaAbatementEntity =
                        abatementContext.create(lookupKey, requestId, false);
                logger.debugLog().debug("Request Id: {}. Alert ClosedLoop Status is ONSET. " +
                                "Saving abatement Entity to repository with lookupKey: {}",
                                debugLogSpec, requestId, tcaAbatementEntity.getLookupKey());
                abatementPersistenceContext.save(tcaAbatementEntity);
                return tcaExecutionContext;

            // ABATED - look up previous saved request id from db
            case ABATED:

                final List<TcaAbatementEntity> previousTcaAbatementEntities =
                        abatementPersistenceContext.findByLookupKey(lookupKey);

                // if previous abatement are indeed present - sort them my last modified date and get latest entity
                if (previousTcaAbatementEntities != null && !previousTcaAbatementEntities.isEmpty()) {
                    previousTcaAbatementEntities.sort(
                            Comparator.comparing(TcaAbatementEntity::getLastModificationDate));
                    final TcaAbatementEntity previousTcaAbatementEntity =
                            previousTcaAbatementEntities.get(previousTcaAbatementEntities.size() - 1);

                    logger.debugLog().debug("Request Id: {}. Found previous Abatement Entity with lookupKey: {}",
                            debugLogSpec, requestId, previousTcaAbatementEntity.getLookupKey());

                    // previous abatement entity was found - but it was already sent before - so ignore alert creation
                    if (previousTcaAbatementEntity.isAbatementAlertSent()) {
                        final String terminatingMessage = "Abatement alert was already sent before on: " +
                                previousTcaAbatementEntity.getLastModificationDate();
                        setTerminatingMessage(terminatingMessage, tcaExecutionContext, false);
                        return tcaExecutionContext;
                    }

                    // no previous abatement was sent
                    final String previousRequestId = previousTcaAbatementEntity.getRequestId();
                    // set abated alert request id to previous ONSET alert request id
                    logger.debugLog().debug("Request Id: {}. No previous abated alert was sent. Setting previous request id: {}",
                            debugLogSpec, requestId, previousRequestId);
                    resultContext.setPreviousRequestId(previousRequestId);
                    // save new entity with alert as sent
                    final TcaAbatementEntity newTcaAbatementEntity =
                            abatementContext.create(lookupKey, previousRequestId, true);
                    logger.debugLog().debug("Request Id: {}. Saving new entity with alert as sent with lookupKey: {}",
                            debugLogSpec, requestId, newTcaAbatementEntity.getLookupKey());
                    abatementPersistenceContext.save(newTcaAbatementEntity);
                    return tcaExecutionContext;

                } else {
                    // no previous onset event found
                    final String terminatingMessage =
                            "Ignored orphan Abated Message. No previous ONSET event found for lookup key: " + lookupKey;
                    setTerminatingMessage(terminatingMessage, tcaExecutionContext, false);
                    return tcaExecutionContext;
                }

                // Only ONSET and ABATED closed loop status are supported
            default:
                final String errorMessage = String.format(
                        "Request Id: %s. Unexpected ClosedLoopEventStatus: %s - Only ONSET and ABATED are supported.",
                        requestId, closedLoopEventStatus);
                throw new TcaProcessingException(errorMessage, new IllegalStateException(errorMessage));
        }

    }

    private static String createLookupKey(final EventListener eventListener,
                                          final MetricsPerEventName violatedMetricsPerEventName) {
        // no null check required as all are required fields
        final String eventName = violatedMetricsPerEventName.getEventName();
        final String sourceName = eventListener.getEvent().getCommonEventHeader().getSourceName();
        final String reportingEntityName = eventListener.getEvent().getCommonEventHeader().getReportingEntityName();
        // violated threshold will always be present
        final Threshold violatedThreshold = violatedMetricsPerEventName.getThresholds().get(0);
        final String closedLoopControlName = violatedThreshold.getClosedLoopControlName();
        final String fieldPath = violatedThreshold.getFieldPath();
        return Stream.of(eventName, sourceName, reportingEntityName, closedLoopControlName, fieldPath)
                .collect(Collectors.joining(TcaModelConstants.TCA_ROW_KEY_DELIMITER));

    }
}
