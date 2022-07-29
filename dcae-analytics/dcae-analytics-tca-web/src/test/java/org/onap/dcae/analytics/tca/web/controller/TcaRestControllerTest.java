/*
 * ============LICENSE_START====================================================================
 * Copyright (c) 2019 IBM Intellectual Property. All rights reserved.
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

package org.onap.dcae.analytics.tca.web.controller;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.model.common.ConfigSource;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.restapi.TcaExecutionRequest;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;
import org.onap.dcae.analytics.tca.web.service.TcaProcessingService;

public class TcaRestControllerTest {

  @Test
  void getTcaPolicyResponseTest() throws Exception {
  TcaPolicyWrapper tcaPolicyWrapper = Mockito.mock(TcaPolicyWrapper.class);
    TcaProcessingService tcaProcessingService = Mockito.mock(TcaProcessingService.class);
    TcaPolicy tcaPolicy = Mockito.mock(TcaPolicy.class);
    List<TcaPolicy> TcaList = new ArrayList<TcaPolicy>();
    TcaList.add(tcaPolicy);
    Mockito.when(tcaPolicyWrapper.getConfigSource()).thenReturn(ConfigSource.valueOf("MONGO"));
    Mockito.when(tcaPolicyWrapper.getTcaPolicy()).thenReturn(TcaList);
    Mockito.when(tcaPolicyWrapper.getCreationTime()).thenReturn(ZonedDateTime.now());
    TcaRestController restcontroller = new TcaRestController(tcaProcessingService, tcaPolicyWrapper);
    restcontroller.getTcaPolicy();
    restcontroller.setTcaPolicy(TcaList);
    assertThat(TcaList).isNotNull();
    assertThat(restcontroller.getTcaPolicy()).isNotNull();
    assertThat(restcontroller.setTcaPolicy(TcaList).getStatusCodeValue()).isEqualTo(200);
    
  }

  @Test
  void getTcaExecutionResponseTest() throws Exception {
    TcaPolicyWrapper tcaPolicyWrapper = Mockito.mock(TcaPolicyWrapper.class);
    TcaExecutionRequest tcaExecutionRequest = Mockito.mock(TcaExecutionRequest.class);
    TcaProcessingService tcaProcessingService = Mockito.mock(TcaProcessingService.class);
    TcaExecutionContext tcaExecutionContext = Mockito.mock(TcaExecutionContext.class);
    List<String> cefMessages = Arrays.asList("Test1", "Test2");
    List<TcaExecutionContext> executionContexts = Arrays.asList(tcaExecutionContext, tcaExecutionContext);
    Mockito.when(tcaProcessingService.getTcaExecutionResults("requestId", "transactioId", tcaPolicyWrapper, cefMessages))
           .thenReturn(executionContexts);
    TcaRestController restcontroller = new TcaRestController(tcaProcessingService, tcaPolicyWrapper);
    restcontroller.execute(tcaExecutionRequest);
    assertThat(restcontroller.execute(tcaExecutionRequest).getStatusCodeValue()).isEqualTo(200);
  }

}
