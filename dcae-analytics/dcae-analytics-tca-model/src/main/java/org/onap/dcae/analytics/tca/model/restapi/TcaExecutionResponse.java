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

package org.onap.dcae.analytics.tca.model.restapi;

import lombok.Data;

import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;

/**
 * @author Rajiv Singla
 */
@Data
public class TcaExecutionResponse implements TcaRestApiModel {

    private static final long serialVersionUID = 1L;

    private String requestId;
    private String transactionId;
    private MetricsPerEventName violatedMetricsPerEventName;
    private TcaAlert tcaAlert;

}
