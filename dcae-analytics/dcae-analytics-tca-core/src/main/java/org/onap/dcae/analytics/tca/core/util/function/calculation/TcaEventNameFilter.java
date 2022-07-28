/*
 * ================================================================================
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

package org.onap.dcae.analytics.tca.core.util.function.calculation;

import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.cef.CommonEventHeader;
import org.onap.dcae.analytics.model.cef.Event;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;

/**
 * @author Rajiv Singla
 */
public class TcaEventNameFilter implements TcaCalculationFunction {

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        final String cefMessage = tcaExecutionContext.getCefMessage();
        final EventListener eventListener = tcaExecutionContext.getTcaProcessingContext().getEventListener();

        final Optional<String> eventNameOptional = Optional.ofNullable(eventListener)
                .map(EventListener::getEvent)
                .map(Event::getCommonEventHeader)
                .map(CommonEventHeader::getEventName);

        // Check Event name is present
        if (!eventNameOptional.isPresent()) {
            final String errorMessage =
                    "Event -> Common Event Header -> Event Name not present. Invalid CEF Message: " + cefMessage;
            setTerminatingMessage(errorMessage, tcaExecutionContext, true);
            return tcaExecutionContext;
        }

        // Get CEF Message Event name and Event names in tca policy
        final String cefMessageEventName = eventNameOptional.get();
        final List<TcaPolicy> tcaPolicy = tcaExecutionContext.getTcaPolicy();
        int count = 0;
        for( TcaPolicy tcaPol : tcaPolicy){
            final Set<String> policyEventNames = tcaPol.getMetricsPerEventName().stream()
                .map(MetricsPerEventName::getEventName).collect(Collectors.toSet());
            // Check CEF Message Event name matches any Policy Event names
            if (!policyEventNames.contains(cefMessageEventName)) {
                    count++;
                    if(count == 2) {
                         final String earlyTerminationMessage = String.format(
                            "CEF Message Event name does not match any Policy Event Names. " +
                            "Message EventName: %s, Policy Event Names: %s", cefMessageEventName, policyEventNames);
                         setTerminatingMessage(earlyTerminationMessage, tcaExecutionContext, false);
                         return tcaExecutionContext;
                    }
            }	    
        }

        // CEF Messages one of the the Policy Event names
        // do nothing

        return tcaExecutionContext;
    }
}
