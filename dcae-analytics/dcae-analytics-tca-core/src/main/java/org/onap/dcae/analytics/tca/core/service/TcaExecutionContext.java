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

package org.onap.dcae.analytics.tca.core.service;

import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;

/**
 * TCA Execution context captures various fields required to calculate a TCA threshold violation calculation execution
 *
 * @author Rajiv Singla
 */
public interface TcaExecutionContext {

    /**
     * Provides request id associated with execution context
     *
     * @return request id associated with execution context
     */
    String getRequestId();


    /**
     * Provides transaction id associated with execution context
     *
     * @return transaction id associated with execution context
     */
    String getTransactionId();


    /**
     * Provides message order number inside a execution batch
     *
     * @return message order number inside a execution batch
     */
    int getMessageIndex();


    /**
     * Provides common event format message as JSON string that is being analyzed
     *
     * @return common event format message as JSON string that is being analyzed
     */
    String getCefMessage();


    /**
     * Provides TCA Policy associated with the processing context
     *
     * @return TCA Policy associated with processing context
     */
    TcaPolicy getTcaPolicy();


    /**
     * Provides TCA Processing Context captures various mutable fields that are computed during TCA execution
     *
     * @return TCA Processing Context captures various mutable fields that are computed during TCA execution
     */
    TcaProcessingContext getTcaProcessingContext();


    /**
     * Provides TCA Processing Result Context which captures outputs of TCA execution
     *
     * @return TCA Processing Result Context which captures outputs of TCA execution
     */
    TcaResultContext getTcaResultContext();


    /**
     * Provides TCA Abatement context containing abstractions for TCA Abatement processing calculations
     *
     * @return TCA abatement context containing abstractions for TCA Abatement processing calculations
     */
    TcaAbatementContext getTcaAbatementContext();


    /**
     * Provides TCA AAI Enrichment Context
     *
     * @return TCA AAI enrichment context
     */
    TcaAaiEnrichmentContext getTcaAaiEnrichmentContext();

}
