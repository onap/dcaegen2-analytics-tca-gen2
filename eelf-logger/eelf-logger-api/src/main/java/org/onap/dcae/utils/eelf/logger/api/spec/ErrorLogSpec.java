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

package org.onap.dcae.utils.eelf.logger.api.spec;


import org.onap.dcae.utils.eelf.logger.api.info.ErrorLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.RequestIdLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.ServiceLogInfo;
import org.onap.dcae.utils.eelf.logger.api.info.TargetServiceLogInfo;

/**
 * Captures fields required for EELF Error Log Specification
 *
 * @author Rajiv Singla
 */
public interface ErrorLogSpec extends
        // request id must be preset for all log specifications
        RequestIdLogInfo,
        // app acting as a service fields
        ServiceLogInfo,
        // app calling external service fields
        TargetServiceLogInfo,
        // error details fields
        ErrorLogInfo {
}
