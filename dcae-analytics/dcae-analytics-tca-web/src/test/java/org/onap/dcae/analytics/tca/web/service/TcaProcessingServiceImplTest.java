/*
 * ================================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.dcae.analytics.tca.web.service;

import static org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion.TCA_POLICY_JSON_FUNCTION;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.tca.core.exception.AnalyticsParsingException;
import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Rajiv Singla
 */
class TcaProcessingServiceImplTest {

//  @Autowired
//  Environment environment;

    public List<TcaPolicy> convertTcaPolicy(String tcaPolicyString) {
      return TCA_POLICY_JSON_FUNCTION.apply(tcaPolicyString).orElseThrow(
                () -> new AnalyticsParsingException("Unable to parse Tca Policy String: " + tcaPolicyString,
                        new IllegalArgumentException()));
    }

    @Test
    void getTcaExecutionResults() throws Exception {
        TcaAbatementContext tcaAbatementContext = new TestTcaAbatementContext();
        TcaAaiEnrichmentContext tcaAaiEnrichmentContext = new TestTcaAaiEnrichmentContext();

        TcaPolicyWrapper tcaPolicyWrapper = Mockito.mock(TcaPolicyWrapper.class);
        //TcaAppProperties tcaAppProperties = new TcaAppProperties(environment);
        String policy = "[{\"domain\":\"measurementsForVfScaling\",\"metricsPerEventName\":[{\"eventName\":\"Mfvs_eNodeB_RANKPI\",\"controlLoopSchemaType\":\"VNF\",\"policyScope\":\"resource=vFirewall;type=configuration\",\"policyName\":\"configuration.dcae.microservice.tca.xml\",\"policyVersion\":\"v0.0.1\",\"thresholds\":[{\"closedLoopControlName\":\"CL-FRWL-LOW-TRAFFIC-SIG-d925ed73-8231-4d02-9545-db4e101f88f8\",\"closedLoopEventStatus\":\"ONSET\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\"thresholdValue\":4000,\"direction\":\"LESS_OR_EQUAL\",\"severity\":\"MAJOR\"},{\"closedLoopControlName\":\"CL-FRWL-HIGH-TRAFFIC-SIG-EA36FE84-9342-5E13-A656-EC5F21309A09\",\"closedLoopEventStatus\":\"ONSET\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\"thresholdValue\":20000,\"direction\":\"GREATER_OR_EQUAL\",\"severity\":\"CRITICAL\"},{\"closedLoopControlName\":\"CL-FRWL-HIGH-TRAFFIC-SIG-EA36FE84-9342-5E13-A656-EC5F21309A09\",\"closedLoopEventStatus\":\"ABATED\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\"thresholdValue\":0,\"direction\":\"EQUAL\",\"severity\":\"CRITICAL\"}]},{\"eventName\":\"vLoadBalancer\",\"controlLoopSchemaType\":\"VNF\",\"policyScope\":\"resource=vLoadBalancer;type=configuration\",\"policyName\":\"configuration.dcae.microservice.tca.xml\",\"policyVersion\":\"v0.0.1\",\"thresholds\":[{\"closedLoopControlName\":\"CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A\",\"closedLoopEventStatus\":\"ONSET\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\"thresholdValue\":500,\"direction\":\"LESS_OR_EQUAL\",\"severity\":\"MAJOR\"},{\"closedLoopControlName\":\"CL-LBAL-LOW-TRAFFIC-SIG-0C5920A6-B564-8035-C878-0E814352BC2B\",\"closedLoopEventStatus\":\"ONSET\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\"thresholdValue\":5000,\"direction\":\"GREATER_OR_EQUAL\",\"severity\":\"CRITICAL\"}]},{\"eventName\":\"virtualVMEventName\",\"controlLoopSchemaType\":\"VM\",\"policyScope\":\"resource=virtualVM;type=configuration\",\"policyName\":\"configuration.dcae.microservice.tca.xml\",\"policyVersion\":\"v0.0.1\",\"thresholds\":[{\"closedLoopControlName\":\"CL-LBAL-LOW-TRAFFIC-SIG-FB480F95-A453-6F24-B767-FD703241AB1A\",\"closedLoopEventStatus\":\"ONSET\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\"thresholdValue\":500,\"direction\":\"LESS_OR_EQUAL\",\"severity\":\"MAJOR\"}]}]},{\"domain\":\"measurement\",\"metricsPerEventName\":[{\"eventName\":\"vFirewallBroadcastPackets\",\"controlLoopSchemaType\":\"VM\",\"policyScope\":\"DCAE\",\"policyName\":\"DCAE.Config_tca-hi-lo\",\"policyVersion\":\"v0.0.1\",\"thresholds\":[{\"closedLoopControlName\":\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementFields.nicPerformanceArray[*].receivedTotalPacketsDelta\",\"thresholdValue\":300,\"direction\":\"LESS_OR_EQUAL\",\"severity\":\"MAJOR\",\"closedLoopEventStatus\":\"ABATED\"},{\"closedLoopControlName\":\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementFields.nicPerformanceArray[*].receivedTotalPacketsDelta\",\"thresholdValue\":700,\"direction\":\"GREATER_OR_EQUAL\",\"severity\":\"CRITICAL\",\"closedLoopEventStatus\":\"ONSET\"}]},{\"eventName\":\"vLoadBalancer\",\"controlLoopSchemaType\":\"VM\",\"policyScope\":\"DCAE\",\"policyName\":\"DCAE.Config_tca-hi-lo\",\"policyVersion\":\"v0.0.1\",\"thresholds\":[{\"closedLoopControlName\":\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementFields.nicPerformanceArray[*].receivedTotalPacketsDelta\",\"thresholdValue\":300,\"direction\":\"GREATER_OR_EQUAL\",\"severity\":\"CRITICAL\",\"closedLoopEventStatus\":\"ONSET\"}]},{\"eventName\":\"Measurement_vGMUX\",\"controlLoopSchemaType\":\"VNF\",\"policyScope\":\"DCAE\",\"policyName\":\"DCAE.Config_tca-hi-lo\",\"policyVersion\":\"v0.0.1\",\"thresholds\":[{\"closedLoopControlName\":\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementFields.additionalMeasurements[*].arrayOfFields[0].value\",\"thresholdValue\":0,\"direction\":\"EQUAL\",\"severity\":\"MAJOR\",\"closedLoopEventStatus\":\"ABATED\"},{\"closedLoopControlName\":\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\",\"version\":\"1.0.2\",\"fieldPath\":\"$.event.measurementFields.additionalMeasurements[*].arrayOfFields[0].value\",\"thresholdValue\":0,\"direction\":\"GREATER\",\"severity\":\"CRITICAL\",\"closedLoopEventStatus\":\"ONSET\"}]}]}]";
        //TcaPolicyWrapper tcaPolicyWrapper = new TcaPolicyWrapper(tcaAppProperties);
        List<TcaPolicy>  tcaPolicy = convertTcaPolicy(policy);
        Mockito.when(tcaPolicyWrapper.getTcaPolicy()).thenReturn(tcaPolicy);
        TcaProcessingService tcaProcessingService = new TcaProcessingServiceImpl(tcaAbatementContext, tcaAaiEnrichmentContext);

        String cefMessage = "{\r\n" + 
                "  \"event\": {\r\n" + 
                "    \"commonEventHeader\": {\r\n" + 
                "      \"domain\": \"measurementsForVfScaling\",\r\n" + 
                "      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" + 
                "      \"eventName\": \"Mfvs_eNodeB_RANKPI\",\r\n" + 
                "      \"lastEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"nfNamingCode\": \"ENBE\",\r\n" + 
                "      \"priority\": \"Normal\",\r\n" + 
                "      \"reportingEntityId\": \"\",\r\n" + 
                "      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" + 
                "      \"sequence\": 0,\r\n" + 
                "      \"sourceId\": \"SCL01081_9B_1\",\r\n" + 
                "      \"sourceName\": \"SCL01081\",\r\n" + 
                "      \"startEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"version\": 3.0\r\n" + 
                "    },\r\n" + 
                "    \"measurementsForVfScalingFields\": {\r\n" + 
                "      \"additionalFields\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"software_version \",\r\n" + 
                "          \"value\": \"version1\"\r\n" + 
                "        },\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"vendor \",\r\n" + 
                "          \"value\": \"Ericsson \"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"additionalMeasurements\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"OaaS_UC1_EricssonSleepingCell\",\r\n" + 
                "          \"arrayOfFields\": [\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRAATTCBRA\",\r\n" + 
                "              \"value\": \"1353\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRASUCCCBRA\",\r\n" + 
                "              \"value\": \"1351\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEAUTO\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEMAN\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABATT\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABSUCC\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            }\r\n" + 
                "          ]\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"vNicPerformanceArray\": [\r\n" + 
                "        {\r\n" + 
                "          \"receivedBroadcastPacketsAccumulated\": 5000,\r\n" + 
                "          \"receivedBroadcastPacketsDelta\": 5,\r\n" + 
                "          \"receivedDiscardedPacketsAccumulated\": 12,\r\n" + 
                "          \"receivedDiscardedPacketsDelta\": 2,\r\n" + 
                "          \"receivedErrorPacketsAccumulated\": 2,\r\n" + 
                "          \"receivedErrorPacketsDelta\": 1,\r\n" + 
                "          \"valuesAreSuspect\": false,\r\n" + 
                "          \"vNicIdentifier\": \"someVNicIdentifier\"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"measurementInterval\": 900,\r\n" + 
                "      \"measurementsForVfScalingVersion\": 2.0\r\n" + 
                "    }\r\n" + 
                "  }\r\n" + 
                "}\r\n" + 
                "";

	String cefV7Message = "{\r\n" + 
		"  \"event\": {\r\n" + 
		"    \"commonEventHeader\": {\r\n" + 
		"      \"domain\": \"measurement\",\r\n" + 
		"      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" + 
		"      \"eventName\": \"vFirewallBroadcastPackets\",\r\n" + 
		"      \"lastEpochMicrosec\": 1492639920787,\r\n" + 
		"      \"nfNamingCode\": \"ENBE\",\r\n" + 
		"      \"priority\": \"Normal\",\r\n" + 
		"      \"reportingEntityId\": \"\",\r\n" + 
		"      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" + 
		"      \"sequence\": 0,\r\n" + 
		"      \"sourceId\": \"SCL01081_9B_1\",\r\n" + 
		"      \"sourceName\": \"SCL01081\",\r\n" + 
		"      \"startEpochMicrosec\": 1492639920787,\r\n" + 
		"      \"version\": 3.0,\r\n" + 
		"      \"vesEventListenerVersion\":\"7.2.1\"\r\n" +
		"    },\r\n" + 
		"    \"measurementFields\": {\r\n" + 
		"      \"additionalFields\": [\r\n" + 
		"        {\r\n" + 
		"          \"name\": \"software_version \",\r\n" + 
		"          \"value\": \"version1\"\r\n" + 
		"        },\r\n" + 
		"        {\r\n" + 
		"          \"name\": \"vendor \",\r\n" + 
		"          \"value\": \"Ericsson \"\r\n" + 
		"        }\r\n" + 
		"      ],\r\n" + 
		"      \"additionalMeasurements\": [\r\n" + 
		"        {\r\n" + 
		"          \"name\": \"OaaS_UC1_EricssonSleepingCell\",\r\n" + 
		"          \"arrayOfFields\": [\r\n" + 
		"            {\r\n" + 
		"              \"name\": \"PMRAATTCBRA\",\r\n" + 
		"              \"value\": \"1353\"\r\n" + 
		"            },\r\n" + 
		"            {\r\n" + 
		"              \"name\": \"PMRASUCCCBRA\",\r\n" + 
		"              \"value\": \"1351\"\r\n" + 
		"            },\r\n" + 
		"            {\r\n" + 
		"              \"name\": \"PMCELLDOWNTIMEAUTO\",\r\n" + 
		"              \"value\": \"0\"\r\n" + 
		"            },\r\n" + 
		"            {\r\n" + 
		"              \"name\": \"PMCELLDOWNTIMEMAN\",\r\n" + 
		"              \"value\": \"0\"\r\n" + 
		"            },\r\n" + 
		"            {\r\n" + 
		"              \"name\": \"PMRRCCONNESTABATT\",\r\n" + 
		"              \"value\": \"297\"\r\n" + 
		"            },\r\n" + 
		"            {\r\n" + 
		"              \"name\": \"PMRRCCONNESTABSUCC\",\r\n" + 
		"              \"value\": \"297\"\r\n" + 
		"            }\r\n" + 
		"          ]\r\n" + 
		"        }\r\n" + 
		"      ],\r\n" + 
		"      \"nicPerformanceArray\": [\r\n" + 
		"        {\r\n" + 
		"          \"receivedBroadcastPacketsAccumulated\": 1002,\r\n" + 
		"          \"receivedBroadcastPacketsDelta\": 5,\r\n" + 
		"          \"receivedDiscardedPacketsAccumulated\": 12,\r\n" + 
		"          \"receivedDiscardedPacketsDelta\": 2,\r\n" + 
		"          \"receivedErrorPacketsAccumulated\": 2,\r\n" + 
		"          \"receivedErrorPacketsDelta\": 1,\r\n" + 
		"          \"valuesAreSuspect\": false,\r\n" + 
		"          \"nicIdentifier\": \"someVNicIdentifier\"\r\n" + 
		"        }\r\n" + 
		"      ],\r\n" + 
		"      \"measurementInterval\": 900,\r\n" + 
		"      \"measurementFieldsVersion\": 2.0\r\n" + 
		"    }\r\n" + 
		"  }\r\n" + 
		"}\r\n" + 
		"";

        String cefV7OnsetMessage = "{\r\n" +
		"  \"event\": {\r\n" +
		"    \"commonEventHeader\": {\r\n" +
		"      \"domain\": \"measurement\",\r\n" +
		"      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" +
		"      \"eventName\": \"vFirewallBroadcastPackets\",\r\n" +
		"      \"lastEpochMicrosec\": 1492639920787,\r\n" +
		"      \"nfNamingCode\": \"ENBE\",\r\n" +
		"      \"priority\": \"Normal\",\r\n" +
		"      \"reportingEntityId\": \"\",\r\n" +
		"      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" +
		"      \"sequence\": 0,\r\n" +
		"      \"sourceId\": \"SCL01081_9B_1\",\r\n" +
		"      \"sourceName\": \"SCL01081\",\r\n" +
		"      \"startEpochMicrosec\": 1492639920787,\r\n" +
		"      \"version\": 3.0,\r\n" +
		"      \"vesEventListenerVersion\":\"7.2.1\"\r\n" +
		"    },\r\n" +
		"    \"measurementFields\": {\r\n" +
                "      \"cpuUsageArray\": [\r\n" +
		"        {\r\n" +
		"          \"percentUsage\": 0,\r\n" +
		"          \"cpuIdentifier\": \"cpu1\",\r\n" +
		"          \"cpuIdle\": 100,\r\n" +
		"          \"cpuUsageSystem\": 0\r\n" +
		"        }\r\n" +
		"      ],\r\n" +  
                "      \"nicPerformanceArray\": [\r\n" +
                "        {\r\n" +
                "          \"receivedTotalPacketsDelta\": 1002,\r\n" +
                "          \"transmittedOctetsDelta\": 0,\r\n" +
                "          \"transmittedTotalPacketsDelta\": 0,\r\n" +
                "          \"receivedOctetsDelta\": 61200,\r\n" +
                "          \"valuesAreSuspect\": true,\r\n" +
                "          \"nicIdentifier\": \"someNicIdentifier\"\r\n" +
                "        }\r\n" +
                "      ],\r\n" +
                "      \"measurementInterval\": 900,\r\n" +
                "      \"measurementFieldsVersion\": 4.0\r\n" +
                "    }\r\n" +
                "  }\r\n" +
                "}\r\n" +
                "";		

        String cefV7AbatementMessage = "{\r\n" +
		"  \"event\": {\r\n" +
		"    \"commonEventHeader\": {\r\n" +
		"      \"domain\": \"measurement\",\r\n" +
		"      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" +
		"      \"eventName\": \"vFirewallBroadcastPackets\",\r\n" +
		"      \"lastEpochMicrosec\": 1492639920787,\r\n" +
		"      \"nfNamingCode\": \"ENBE\",\r\n" +
		"      \"priority\": \"Normal\",\r\n" +
		"      \"reportingEntityId\": \"\",\r\n" +
		"      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" +
		"      \"sequence\": 0,\r\n" +
		"      \"sourceId\": \"SCL01081_9B_1\",\r\n" +
		"      \"sourceName\": \"SCL01081\",\r\n" +
		"      \"startEpochMicrosec\": 1492639920787,\r\n" +
		"      \"version\": 3.0,\r\n" +
		"      \"vesEventListenerVersion\":\"7.2.1\"\r\n" +
		"    },\r\n" +
		"    \"measurementFields\": {\r\n" +
		"      \"cpuUsageArray\": [\r\n" +
		"        {\r\n" +
		"          \"percentUsage\": 0,\r\n" +
		"          \"cpuIdentifier\": \"cpu1\",\r\n" +
		"          \"cpuIdle\": 100,\r\n" +
		"          \"cpuUsageSystem\": 0\r\n" +
		"        }\r\n" +
		"      ],\r\n" +
		"      \"nicPerformanceArray\": [\r\n" +
		"        {\r\n" +
		"          \"receivedTotalPacketsDelta\": 100,\r\n" +
		"          \"transmittedOctetsDelta\": 0,\r\n" +
		"          \"transmittedTotalPacketsDelta\": 0,\r\n" +
		"          \"receivedOctetsDelta\": 61200,\r\n" +
		"          \"valuesAreSuspect\": true,\r\n" +
		"          \"nicIdentifier\": \"someNicIdentifier\"\r\n" +
		"        }\r\n" +
		"      ],\r\n" +
		"      \"measurementInterval\": 900,\r\n" +
		"      \"measurementFieldsVersion\": 4.0\r\n" +
		"    }\r\n" +
		"  }\r\n" +
		"}\r\n" +
		"";

        String cefViolationMessage = "{\r\n" + 
                "  \"event\": {\r\n" + 
                "    \"commonEventHeader\": {\r\n" + 
                "      \"domain\": \"measurementsForVfScaling\",\r\n" + 
                "      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" + 
                "      \"eventName\": \"Mfvs_eNodeB_RANKPI\",\r\n" + 
                "      \"lastEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"nfNamingCode\": \"ENBE\",\r\n" + 
                "      \"priority\": \"Normal\",\r\n" + 
                "      \"reportingEntityId\": \"\",\r\n" + 
                "      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" + 
                "      \"sequence\": 0,\r\n" + 
                "      \"sourceId\": \"SCL01081_9B_1\",\r\n" + 
                "      \"sourceName\": \"SCL01081\",\r\n" + 
                "      \"startEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"version\": 3.0\r\n" + 
                "    },\r\n" + 
                "    \"measurementsForVfScalingFields\": {\r\n" + 
                "      \"additionalFields\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"software_version \",\r\n" + 
                "          \"value\": \"version1\"\r\n" + 
                "        },\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"vendor \",\r\n" + 
                "          \"value\": \"Ericsson \"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"additionalMeasurements\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"OaaS_UC1_EricssonSleepingCell\",\r\n" + 
                "          \"arrayOfFields\": [\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRAATTCBRA\",\r\n" + 
                "              \"value\": \"1353\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRASUCCCBRA\",\r\n" + 
                "              \"value\": \"1351\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEAUTO\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEMAN\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABATT\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABSUCC\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            }\r\n" + 
                "          ]\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"vNicPerformanceArray\": [\r\n" + 
                "        {\r\n" + 
                "          \"receivedBroadcastPacketsAccumulated\": 21000,\r\n" + 
                "          \"receivedBroadcastPacketsDelta\": 5,\r\n" + 
                "          \"receivedDiscardedPacketsAccumulated\": 12,\r\n" + 
                "          \"receivedDiscardedPacketsDelta\": 2,\r\n" + 
                "          \"receivedErrorPacketsAccumulated\": 2,\r\n" + 
                "          \"receivedErrorPacketsDelta\": 1,\r\n" + 
                "          \"valuesAreSuspect\": false,\r\n" + 
                "          \"vNicIdentifier\": \"someVNicIdentifier\"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"measurementInterval\": 900,\r\n" + 
                "      \"measurementsForVfScalingVersion\": 2.0\r\n" + 
                "    }\r\n" + 
                "  }\r\n" + 
                "}\r\n" + 
                "";

        String cefAbatementMessage = "{\r\n" + 
                "  \"event\": {\r\n" + 
                "    \"commonEventHeader\": {\r\n" + 
                "      \"domain\": \"measurementsForVfScaling\",\r\n" + 
                "      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" + 
                "      \"eventName\": \"Mfvs_eNodeB_RANKPI\",\r\n" + 
                "      \"lastEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"nfNamingCode\": \"ENBE\",\r\n" + 
                "      \"priority\": \"Normal\",\r\n" + 
                "      \"reportingEntityId\": \"\",\r\n" + 
                "      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" + 
                "      \"sequence\": 0,\r\n" + 
                "      \"sourceId\": \"SCL01081_9B_1\",\r\n" + 
                "      \"sourceName\": \"SCL01081\",\r\n" + 
                "      \"startEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"version\": 3.0\r\n" + 
                "    },\r\n" + 
                "    \"measurementsForVfScalingFields\": {\r\n" + 
                "      \"additionalFields\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"software_version \",\r\n" + 
                "          \"value\": \"version1\"\r\n" + 
                "        },\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"vendor \",\r\n" + 
                "          \"value\": \"Ericsson \"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"additionalMeasurements\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"OaaS_UC1_EricssonSleepingCell\",\r\n" + 
                "          \"arrayOfFields\": [\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRAATTCBRA\",\r\n" + 
                "              \"value\": \"1353\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRASUCCCBRA\",\r\n" + 
                "              \"value\": \"1351\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEAUTO\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEMAN\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABATT\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABSUCC\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            }\r\n" + 
                "          ]\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"vNicPerformanceArray\": [\r\n" + 
                "        {\r\n" + 
                "          \"receivedBroadcastPacketsAccumulated\": 0,\r\n" + 
                "          \"receivedBroadcastPacketsDelta\": 5,\r\n" + 
                "          \"receivedDiscardedPacketsAccumulated\": 12,\r\n" + 
                "          \"receivedDiscardedPacketsDelta\": 2,\r\n" + 
                "          \"receivedErrorPacketsAccumulated\": 2,\r\n" + 
                "          \"receivedErrorPacketsDelta\": 1,\r\n" + 
                "          \"valuesAreSuspect\": false,\r\n" + 
                "          \"vNicIdentifier\": \"someVNicIdentifier\"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"measurementInterval\": 900,\r\n" + 
                "      \"measurementsForVfScalingVersion\": 2.0\r\n" + 
                "    }\r\n" + 
                "  }\r\n" + 
                "}\r\n" + 
                "";

        String cefInapplicableMessage = "{\r\n" + 
                "  \"event\": {\r\n" + 
                "    \"commonEventHeader\": {\r\n" + 
                "      \"domain\": \"measurementsForVfScaling\",\r\n" + 
                "      \"eventId\": \"UC1-SCL01081-1492639920787\",\r\n" + 
                "      \"eventName\": \"testEventName\",\r\n" + 
                "      \"lastEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"nfNamingCode\": \"ENBE\",\r\n" + 
                "      \"priority\": \"Normal\",\r\n" + 
                "      \"reportingEntityId\": \"\",\r\n" + 
                "      \"reportingEntityName\": \"vtc2e7admn2\",\r\n" + 
                "      \"sequence\": 0,\r\n" + 
                "      \"sourceId\": \"SCL01081_9B_1\",\r\n" + 
                "      \"sourceName\": \"SCL01081\",\r\n" + 
                "      \"startEpochMicrosec\": 1492639920787,\r\n" + 
                "      \"version\": 3.0\r\n" + 
                "    },\r\n" + 
                "    \"measurementsForVfScalingFields\": {\r\n" + 
                "      \"additionalFields\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"software_version \",\r\n" + 
                "          \"value\": \"version1\"\r\n" + 
                "        },\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"vendor \",\r\n" + 
                "          \"value\": \"Ericsson \"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"additionalMeasurements\": [\r\n" + 
                "        {\r\n" + 
                "          \"name\": \"OaaS_UC1_EricssonSleepingCell\",\r\n" + 
                "          \"arrayOfFields\": [\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRAATTCBRA\",\r\n" + 
                "              \"value\": \"1353\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRASUCCCBRA\",\r\n" + 
                "              \"value\": \"1351\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEAUTO\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMCELLDOWNTIMEMAN\",\r\n" + 
                "              \"value\": \"0\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABATT\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            },\r\n" + 
                "            {\r\n" + 
                "              \"name\": \"PMRRCCONNESTABSUCC\",\r\n" + 
                "              \"value\": \"297\"\r\n" + 
                "            }\r\n" + 
                "          ]\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"vNicPerformanceArray\": [\r\n" + 
                "        {\r\n" + 
                "          \"receivedBroadcastPacketsAccumulated\": 5000,\r\n" + 
                "          \"receivedBroadcastPacketsDelta\": 5,\r\n" + 
                "          \"receivedDiscardedPacketsAccumulated\": 12,\r\n" + 
                "          \"receivedDiscardedPacketsDelta\": 2,\r\n" + 
                "          \"receivedErrorPacketsAccumulated\": 2,\r\n" + 
                "          \"receivedErrorPacketsDelta\": 1,\r\n" + 
                "          \"valuesAreSuspect\": false,\r\n" + 
                "          \"vNicIdentifier\": \"someVNicIdentifier\"\r\n" + 
                "        }\r\n" + 
                "      ],\r\n" + 
                "      \"measurementInterval\": 900,\r\n" + 
                "      \"measurementsForVfScalingVersion\": 2.0\r\n" + 
                "    }\r\n" + 
                "  }\r\n" + 
                "}\r\n" + 
                "";

        ObjectMapper objectMapper = new ObjectMapper();
        final List<TcaExecutionContext> tcaExecutionResults = tcaProcessingService.getTcaExecutionResults(
                "testRequestId", "testTransactionId", tcaPolicyWrapper,
                Arrays.asList(cefMessage, cefViolationMessage,
                        cefAbatementMessage, cefV7Message, cefV7OnsetMessage, cefV7AbatementMessage));
        
        for (TcaExecutionContext tcaExecutionResult : tcaExecutionResults) {
            final TcaAlert tcaAlert = tcaExecutionResult.getTcaResultContext().getTcaAlert();
            String tcaAlertString = "";
            if (tcaAlert != null) {
                tcaAlertString = objectMapper.writeValueAsString(tcaAlert);
            }
        }
    }
}


