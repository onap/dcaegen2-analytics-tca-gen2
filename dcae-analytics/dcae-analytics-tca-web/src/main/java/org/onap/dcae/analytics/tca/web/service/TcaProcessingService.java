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

import java.util.List;

import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;

/**
 * Provides TCA functionality
 *
 * @author Rajiv Singla
 */
public interface TcaProcessingService {

    /**
     * Apply policy thresholds to CEF messages and generate TCA execution results
     *
     * @param requestId request id associated with tca execution request
     * @param transactionId transaction id associated with the tca execution request
     * @param tcaPolicy tca policy that needs to be applied to CEF messages
     * @param cefMessages list of CEF messages that needs to be processed by TCA
     *
     * @return results of TCA Processing
     */
    List<TcaExecutionContext> getTcaExecutionResults(String requestId, String transactionId,
                                                     TcaPolicy tcaPolicy,
                                                     List<String> cefMessages);


}
