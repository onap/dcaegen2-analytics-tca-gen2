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
 * Captures fields required to log error related information
 *
 * @author Rajiv Singla
 */
public interface ErrorLogInfo extends LogInfo {


    /**
     * Required field contains an error code representing the error condition. The codes can be chose by
     * the logging application but they should adhere to the guidelines embodied in the table below:
     *
     * <div style="display: flex">
     *     <div style="border-left: 2px solid black; border-right: 2px solid black;">
     *         <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Error Type</p>
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
    Integer getErrorCode();


    /**
     * Required field contains a human readable description of the error condition.
     *
     * @return human readable description of the error condition
     */
    String getErrorDescription();

}
