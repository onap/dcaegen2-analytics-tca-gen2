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

import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.model.util.json.AnalyticsModelJsonConversion;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;

/**
 * Tca Event Listener Calculation Function converts incoming CEF string message
 * to Java Object of type Event Listener
 *
 * @author Rajiv Singla
 */
public class TcaEventListenerExtractor implements TcaCalculationFunction {

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        final String cefMessage = tcaExecutionContext.getCefMessage();

        // Check blank message
        if (cefMessage == null || cefMessage.isEmpty() || cefMessage.trim().isEmpty()) {
            final String errorMessage = "Blank CEF message cannot be converted to CEF Event Listener Object";
            setTerminatingMessage(errorMessage, tcaExecutionContext, true);
            return tcaExecutionContext;
        }


        // Check CEF message is a valid JSON Object
        final String trimmedMessage = cefMessage.trim();
        if (!(trimmedMessage.startsWith("{") && trimmedMessage.endsWith("}"))) {
            final String errorMessage =
                    "CEF Message must be a JSON object starting and ending with curly braces. Invalid JSON String: " +
                            cefMessage;
            setTerminatingMessage(errorMessage, tcaExecutionContext, true);
            return tcaExecutionContext;
        }

        // Parse CEF message as JSON Object
        final Optional<EventListener> eventListenerOptional =
                AnalyticsModelJsonConversion.EVENT_LISTENER_JSON_FUNCTION.apply(cefMessage);

        if (!eventListenerOptional.isPresent()) {
            final String errorMessage =
                    "Unable to parse given CEF Message as JSON Object. CEF Message String: " + cefMessage;
            setTerminatingMessage(errorMessage, tcaExecutionContext, true);
            return tcaExecutionContext;
        }


        // JSON parsing completed successfully
        tcaExecutionContext.getTcaProcessingContext().setEventListener(eventListenerOptional.get());

        return tcaExecutionContext;
    }
}
