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

package org.onap.dcae.utils.eelf.logger.api.info;

/**
 * Captures field required for logging Response information
 *
 * @author Rajiv Singla
 */
public interface ResponseLogInfo extends LogInfo {

    /**
     * Required field contains application-specific response codes. While error codes are
     * application-specific, they
     * should conform categories mentioned in table below in order to provide consistency
     * <div style="display: flex">
     *     <div style="border-left: 2px solid black; border-right: 2px solid black;">
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Error Type</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">0</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">100</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">200</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">300</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">400</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">500</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;border-bottom: 2px solid black;">
     *             900</p>
     *     </div>
     *     <div style="border-right: 2px solid black;">
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Notes</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Success</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Permission errors</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Availability errors/Timeouts</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Data errors</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Schema errors</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Business process errors</p>
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;border-bottom: 2px solid black;">
     *             Unknown Errors</p>
     *     </div>
     * </div>
     *
     * @return error Code
     */
    Integer getResponseCode();


    /**
     * Required field contains a human readable description of the {@link #getResponseCode()}.
     *
     * @return human readable description of the response code
     */
    String getResponseDescription();

}
