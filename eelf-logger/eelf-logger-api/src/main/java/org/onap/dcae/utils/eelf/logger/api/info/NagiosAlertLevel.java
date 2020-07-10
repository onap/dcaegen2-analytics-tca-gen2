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
 * Enum for Nagios monitoring/alerting codes as per table below.
 *
 *
 * <div style="display: flex">
 *         <div style="border-left: 2px solid black; border-right: 2px solid black;">
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Return Code</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">0</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">1</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">2</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;border-bottom: 2px solid black;">3</p>
 *         </div>
 *         <div style="border-right: 2px solid black;">
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Service State</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">OK</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">WARNING</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">CRITICAL</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;border-bottom: 2px solid black;">
 *                 UNKNOWN</p>
 *         </div>
 *         <div style="border-right: 2px solid black;">
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">Host State</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">UP</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">UP or DOWN/UNREACHABLE*</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;">DOWN/UNREACHABLE</p>
 *             <p style="margin: 0;padding: 0 1px 0;border-top: 2px solid black;border-bottom: 2px solid black;">
 *                 DOWN/UNREACHABLE</p>
 *         </div>
 * </div>
 *
 * @author Rajiv Singla
 */
public enum NagiosAlertLevel implements LogInfo {

    OK("0"),
    WARNING("1"),
    CRITICAL("2"),
    UNKNOWN("3");

    private String severityCode;

    NagiosAlertLevel(final String severityCode) {
        this.severityCode = severityCode;
    }

    public String getSeverityCode() {
        return severityCode;
    }

}
