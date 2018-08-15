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

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;

/**
 * @author Rajiv Singla
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class GenericTcaExecutionContext implements TcaExecutionContext {

    private final String requestId;
    private final String transactionId;
    private final int messageIndex;
    private final String cefMessage;
    private final TcaPolicy tcaPolicy;
    private final TcaProcessingContext tcaProcessingContext;
    private final TcaResultContext tcaResultContext;
    private final TcaAbatementContext tcaAbatementContext;
    private final TcaAaiEnrichmentContext tcaAaiEnrichmentContext;

}
