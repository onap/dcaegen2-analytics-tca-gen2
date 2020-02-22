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

package org.onap.dcae.analytics.tca.web.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.common.ConfigSource;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaResultContext;
import org.onap.dcae.analytics.tca.core.util.TcaUtils;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.restapi.TcaExecutionRequest;
import org.onap.dcae.analytics.tca.model.restapi.TcaExecutionResponse;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;
import org.onap.dcae.analytics.tca.web.service.TcaProcessingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Rajiv Singla
 */
@RestController
@RequestMapping(TcaModelConstants.TCA_REST_API_PREFIX)
@Api(value = "Provides endpoints for TCA micro service")
public class TcaRestController {

    private final TcaProcessingService tcaProcessingService;
    private final TcaPolicyWrapper tcaPolicyWrapper;


    public TcaRestController(final TcaProcessingService tcaProcessingService,
                             final TcaPolicyWrapper tcaPolicyWrapper) {
        this.tcaProcessingService = tcaProcessingService;
        this.tcaPolicyWrapper = tcaPolicyWrapper;
    }

    @GetMapping(value = TcaModelConstants.TCA_POLICY_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Provides current TCA Policy")
    public ResponseEntity<TcaPolicy> getTcaPolicy() {
        return getTcaPolicyResponse(tcaPolicyWrapper);
    }

    @PostMapping(value = TcaModelConstants.TCA_POLICY_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Sets new value for TCA Policy and returns current Policy")
    public ResponseEntity<TcaPolicy> setTcaPolicy(@RequestBody final TcaPolicy tcaPolicy) {
        tcaPolicyWrapper.setTcaPolicy(tcaPolicy, ConfigSource.REST_API);
        return getTcaPolicyResponse(tcaPolicyWrapper);
    }


    @PostMapping(value = TcaModelConstants.TCA_EXECUTION_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Applies TCA to provided execution request and generated TCA execution response")
    public ResponseEntity<List<TcaExecutionResponse>> execute(@RequestBody final TcaExecutionRequest
                                                                      tcaExecutionRequest) {
        // process tca execution request
        final List<TcaExecutionContext> executionContexts = tcaProcessingService.getTcaExecutionResults(
                tcaExecutionRequest.getRequestId(), tcaExecutionRequest.getTransactionId(),
                tcaExecutionRequest.getTcaPolicy(), TcaUtils.getCefMessagesFromEventListeners
                        (tcaExecutionRequest.getEventListeners()));
        // create execution response
        final List<TcaExecutionResponse> tcaExecutionResponses = executionContexts.stream().map(tcaExecutionContext -> {
            final TcaExecutionResponse tcaExecutionResponse = new TcaExecutionResponse();
            tcaExecutionResponse.setRequestId(tcaExecutionContext.getRequestId());
            tcaExecutionResponse.setTransactionId(tcaExecutionContext.getTransactionId());
            final TcaResultContext tcaResultContext = tcaExecutionContext.getTcaResultContext();
            tcaExecutionResponse.setViolatedMetricsPerEventName(tcaResultContext.getViolatedMetricsPerEventName());
            tcaExecutionResponse.setTcaAlert(tcaResultContext.getTcaAlert());
            return tcaExecutionResponse;
        }).collect(Collectors.toList());

        return ResponseEntity.ok().body(tcaExecutionResponses);
    }


    private static ResponseEntity<TcaPolicy> getTcaPolicyResponse(final TcaPolicyWrapper tcaPolicyWrapper) {
        return ResponseEntity.ok()
                .header(TcaModelConstants.TCA_POLICY_SOURCE_HEADER_KEY, tcaPolicyWrapper.getConfigSource().name())
                .header(TcaModelConstants.TCA_POLICY_CREATION_HEADER_KEY,
                        tcaPolicyWrapper.getCreationTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .header(TcaModelConstants.TCA_POLICY_VERSION_HEADER_KEY, tcaPolicyWrapper.getPolicyVersion())
                .body(tcaPolicyWrapper.getTcaPolicy());
    }

}
