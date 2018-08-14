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

import org.onap.dcae.analytics.model.cef.CommonEventHeader;
import org.onap.dcae.analytics.model.cef.Domain;
import org.onap.dcae.analytics.model.cef.Event;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaProcessingContext;

/**
 * @author Rajiv Singla
 */
public class TcaDomainFilter implements TcaCalculationFunction {

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        final TcaProcessingContext tcaProcessingContext = tcaExecutionContext.getTcaProcessingContext();
        final String cefMessage = tcaExecutionContext.getCefMessage();

        // Extract CEF domain as it is must be present as per CEF Schema
        final Optional<Domain> domainOptional = Optional.ofNullable(tcaProcessingContext.getEventListener())
                .map(EventListener::getEvent)
                .map(Event::getCommonEventHeader)
                .map(CommonEventHeader::getDomain);

        // Check domain is present
        if (!domainOptional.isPresent()) {
            final String errorMessage =
                    "Event -> Common Event Header -> Domain not present. Invalid CEF Message: " + cefMessage;
            setTerminatingMessage(errorMessage, tcaExecutionContext, true);
            return tcaExecutionContext;
        }

        // Get Policy and CEF Message Domain
        final String policyDomain = tcaExecutionContext.getTcaPolicy().getDomain();
        final String cefMessageDomain = domainOptional.get().name();

        // Check Policy domain matches CEF message domain
        if (!policyDomain.equalsIgnoreCase(cefMessageDomain)) {
            final String earlyTerminationMessage = String.format(
                    "Policy Domain does not match CEF Message Domain. Policy Domain: %s, CEF  Message Domain: %s",
                    policyDomain, cefMessageDomain);
            setTerminatingMessage(earlyTerminationMessage, tcaExecutionContext, false);
            return tcaExecutionContext;
        }


        // Policy Domain and CEF Message Domain match successful
        // do nothing

        return tcaExecutionContext;
    }
}
