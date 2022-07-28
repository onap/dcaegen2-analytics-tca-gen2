/*
 * ==========LICENSE_START======================================================================
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

package org.onap.dcae.analytics.tca.web.validation;


import static org.onap.dcae.analytics.web.util.ValidationUtils.isEmpty;

import java.util.List;

import org.onap.dcae.analytics.model.cef.EventSeverity;
import org.onap.dcae.analytics.tca.model.policy.ClosedLoopEventStatus;
import org.onap.dcae.analytics.tca.model.policy.ControlLoopSchemaType;
import org.onap.dcae.analytics.tca.model.policy.Direction;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.policy.Threshold;
import org.onap.dcae.analytics.web.validation.AnalyticsValidator;
import org.onap.dcae.analytics.web.validation.GenericValidationResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.Errors;


/**
 * TCA Policy Validator validates {@link TcaPolicy}
 *
 * @author Rajiv Singla
 */
public class TcaPolicyValidator implements AnalyticsValidator<List<TcaPolicy>, GenericValidationResponse> {

    private static final long serialVersionUID = 1L;

    @Override
    public GenericValidationResponse apply(final List<TcaPolicy> tcaPol) {

        final GenericValidationResponse validationResponse = new GenericValidationResponse();
        
        for( TcaPolicy tcaPolicy : tcaPol) {
           
           // validate TCA Policy must domain present
           final String domain = tcaPolicy.getDomain();
           if (isEmpty(domain)) {
              validationResponse.addErrorMessage("domain", "TCA Policy must have only one domain present");
           }

           // validate TCA Policy must have at lease one metrics per event name
           final List<MetricsPerEventName> metricsPerEventNames = tcaPolicy.getMetricsPerEventName();
           if (metricsPerEventNames == null || metricsPerEventNames.isEmpty()) {
              validationResponse
                    .addErrorMessage("metricsPerEventName", "TCA Policy metricsPerEventName is empty");
              return validationResponse;
           }

           // validate Metrics Per Event Name
           for (MetricsPerEventName metricsPerEventName : metricsPerEventNames) {

              // event name must be present
              final String eventName = metricsPerEventName.getEventName();
              if (isEmpty(eventName)) {
                 validationResponse.addErrorMessage("eventName",
                        "TCA Policy eventName is not present for metricsPerEventNames:" + metricsPerEventName);
              }

              // control Loop Schema type must be present
              final ControlLoopSchemaType controlLoopSchemaType = metricsPerEventName.getControlLoopSchemaType();
              if (controlLoopSchemaType == null) {
                  validationResponse.addErrorMessage("controlLoopEventType",
                          "TCA Policy controlLoopSchemaType is not present for metricsPerEventNames:"
                                + metricsPerEventName);
              }

              // must have at least 1 threshold defined
              final List<Threshold> thresholds = metricsPerEventName.getThresholds();
              if (thresholds == null || thresholds.isEmpty()) {
                 validationResponse.addErrorMessage("thresholds",
                        "TCA Policy event Name must have at least one threshold. " +
                                "Event Name causing this validation error:" + metricsPerEventName);
              } else {
                  // validate each threshold must have non null - fieldPath, thresholdValue, direction and severity
                  for (Threshold eventNameThreshold : thresholds) {
                     final String fieldPath = eventNameThreshold.getFieldPath();
                     final Long thresholdValue = eventNameThreshold.getThresholdValue();
                     final Direction direction = eventNameThreshold.getDirection();
                     final EventSeverity severity = eventNameThreshold.getSeverity();
                     final ClosedLoopEventStatus closedLoopEventStatus = eventNameThreshold.getClosedLoopEventStatus();
                     if (isEmpty(fieldPath) || thresholdValue == null || direction == null || severity == null ||
                            closedLoopEventStatus == null) {
                          validationResponse.addErrorMessage("threshold",
                                "TCA Policy threshold must have fieldPath,thresholdValue,direction, " +
                                        "closedLoopEventStatus and severity defined." +
                                        "Threshold causing this validation error:" + eventNameThreshold);
                     }
                  }
              }
       
           }
        }     

        return validationResponse;
    }

    @Override
    public boolean supports(final Class<?> type) {
        return type == TcaPolicy.class;
    }

    @Override
    public void validate(@Nullable final Object target, final Errors errors) {

        if (target == null) {
            errors.rejectValue("tcaPolicy", "TCA Policy must not be null");
            return;
        }

        final List<TcaPolicy> tcaPolicy = (List<TcaPolicy>) target;
        final GenericValidationResponse validationResponse = apply(tcaPolicy);
        if (validationResponse.hasErrors()) {
            errors.rejectValue("tca policy", validationResponse.getAllErrorMessage());
        }
    }
}
