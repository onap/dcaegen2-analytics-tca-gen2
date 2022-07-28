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

package org.onap.dcae.analytics.tca.core.util;

import static org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion.TCA_OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.tca.core.exception.AnalyticsParsingException;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.utils.eelf.logger.model.info.ServiceLogInfoImpl;

/**
 * @author Rajiv Singla
 */
public abstract class TcaUtils {

    /**
     * TCA Service Log Info for ECOMP Logging
     */
    public static final ServiceLogInfoImpl TCA_SERVICE_LOG_INFO =
            new ServiceLogInfoImpl(TcaModelConstants.TCA_SERVICE_NAME, System.getProperty("user.name"), "");

    /**
     * Creates a deep copy of Tca Policy
     *
     * @param tcaPolicy source tca policy object
     *
     * @return deep copy of provided tca policy
     */
    public static List<TcaPolicy> getTcaPolicyDeepCopy(final List<TcaPolicy> tcaPolicy) {
        if (tcaPolicy != null) {
          List<TcaPolicy> tcaList =  new ArrayList<TcaPolicy>();		
          for( TcaPolicy tcaPol : tcaPolicy) {		
             try {
                tcaList.add(TCA_OBJECT_MAPPER.treeToValue(TCA_OBJECT_MAPPER.valueToTree(tcaPol), TcaPolicy.class));
            } catch (JsonProcessingException e) {
                throw new AnalyticsParsingException("Unable to create deep copy of TCA Policy: " + tcaPol, e);
            }
	  }
          return tcaList;	  
        } 
	else {
            final String errorMessage = "Invalid application state. TCA Policy must not be null";
            throw new AnalyticsParsingException(errorMessage, new IllegalStateException(errorMessage));
        }
    }


    /**
     * Converts given event Listeners to list of CEF Message String
     *
     * @param eventListeners event listeners object
     *
     * @return cef messages as string
     */
    public static List<String> getCefMessagesFromEventListeners(final List<EventListener> eventListeners) {
        if (!Optional.ofNullable(eventListeners).isPresent()) {
            return Collections.emptyList();
        }
        return eventListeners.stream().map(eventListener -> {
            try {
                return TCA_OBJECT_MAPPER.writeValueAsString(eventListener);
            } catch (JsonProcessingException e) {
                throw new AnalyticsParsingException("Unable to parse EventLister to String: " + eventListener, e);
            }
        }).collect(Collectors.toList());
    }

    private TcaUtils() {
        // private constructor
    }
}
