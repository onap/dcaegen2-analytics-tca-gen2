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

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.cef.CommonEventHeader;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaResultContext;
import org.onap.dcae.analytics.tca.model.facade.Aai;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.policy.ClosedLoopEventStatus;
import org.onap.dcae.analytics.tca.model.policy.ControlLoopSchemaType;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;
import org.onap.dcae.analytics.tca.model.policy.Threshold;

/**
 * @author Rajiv Singla
 */
public class TcaAlertCreationFunction implements TcaCalculationFunction {

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        // get violated metrics per event name
        final Optional<MetricsPerEventName> violatedMetricsPerEventNameOptional =
                Optional.ofNullable(tcaExecutionContext.getTcaResultContext())
                        .map(TcaResultContext::getViolatedMetricsPerEventName);

        // If no violated metrics per event name is present skip alert creation
        if (!violatedMetricsPerEventNameOptional.isPresent()) {
            return tcaExecutionContext;
        }

        final TcaResultContext resultContext = tcaExecutionContext.getTcaResultContext();

        // Request id should be set to previous request id for abated events if present
        final String requestId = Optional.ofNullable(resultContext.getPreviousRequestId())
                .orElse(tcaExecutionContext.getRequestId());

        // create new tca alert
        final TcaAlert tcaAlert = createNewTcaAlert(requestId, tcaExecutionContext.getTcaProcessingContext()
                .getEventListener(), violatedMetricsPerEventNameOptional.get());

        // update tca processing result context with alert message
        tcaExecutionContext.getTcaResultContext().setTcaAlert(tcaAlert);

        return tcaExecutionContext;
    }

    private static TcaAlert createNewTcaAlert(final String requestId,
                                              final EventListener eventListener,
                                              final MetricsPerEventName violatedMetricsPerEventName) {
        final Threshold violatedThreshold = violatedMetricsPerEventName.getThresholds().get(0);
        final CommonEventHeader commonEventHeader = eventListener.getEvent().getCommonEventHeader();

        final TcaAlert tcaAlert = new TcaAlert();
        // ClosedLoopControlName included in the DCAE configuration Policy
        tcaAlert.setClosedLoopControlName(violatedThreshold.getClosedLoopControlName());
        // version included in the DCAE configuration Policy
        tcaAlert.setVersion(violatedThreshold.getVersion());
        // request id
        tcaAlert.setRequestId(requestId);
        // commonEventHeader.startEpochMicrosec from the received VES message
        tcaAlert.setClosedLoopAlarmStart(commonEventHeader.getStartEpochMicrosec());
        // commonEventHeader.lastEpochMicrosec from the received VES message for abated alerts
        if (violatedThreshold.getClosedLoopEventStatus() == ClosedLoopEventStatus.ABATED) {
            tcaAlert.setClosedLoopAlarmEnd(commonEventHeader.getLastEpochMicrosec());
        }
        // get service name
        tcaAlert.setClosedLoopEventClient(TcaModelConstants.TCA_SERVICE_NAME);

        final Aai aai = new Aai();
        tcaAlert.setAai(aai);

        // VM specific settings
        if (violatedMetricsPerEventName.getControlLoopSchemaType() == ControlLoopSchemaType.VM) {
            // Hard Coded - "VM"
            tcaAlert.setTargetType(TcaModelConstants.TCA_ALERT_VM_TARGET_TYPE);
            // Hard Coded - "vserver.vserver-name"
            tcaAlert.setTarget(TcaModelConstants.TCA_ALERT_VM_TARGET);
            // commonEventHeader.sourceName from the received VES message
            aai.setGenericServerName(commonEventHeader.getSourceName());
        } else {
            // VNF specific settings
            // Hard Coded - "VNF"
            tcaAlert.setTargetType(TcaModelConstants.TCA_ALERT_VNF_TARGET_TYPE);
            // Hard Coded - "generic-vnf.vnf-name"
            tcaAlert.setTarget(TcaModelConstants.TCA_ALERT_VNF_TARGET);
            // commonEventHeader.sourceName from the received VES message
            aai.setGenericVNFName(commonEventHeader.getSourceName());
        }

        // Hard Coded - "DCAE"
        tcaAlert.setFrom(TcaModelConstants.TCA_VES_RESPONSE_FROM);
        // policyScope included in the DCAE configuration Policy
        tcaAlert.setPolicyScope(violatedMetricsPerEventName.getPolicyScope());
        // policyName included in the DCAE configuration Policy
        tcaAlert.setPolicyName(violatedMetricsPerEventName.getPolicyName());
        // policyVersion included in the DCAE configuration Policy
        tcaAlert.setPolicyVersion(violatedMetricsPerEventName.getPolicyVersion());
        // Extracted from violated threshold
        tcaAlert.setClosedLoopEventStatus(violatedThreshold.getClosedLoopEventStatus().name());

        // return new tca Alert
        return tcaAlert;
    }


}
