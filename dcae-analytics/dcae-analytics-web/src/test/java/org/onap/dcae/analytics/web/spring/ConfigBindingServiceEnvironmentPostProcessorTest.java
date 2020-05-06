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

package org.onap.dcae.analytics.web.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsClient;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import reactor.core.publisher.Flux;

public class ConfigBindingServiceEnvironmentPostProcessorTest extends BaseAnalyticsWebTest {

    @BeforeAll
    static void beforeAll() throws Exception {
        BaseAnalyticsWebTest.initializeConfigBindingServiceEnvironmentVariables();
    }

    @Test
    void postProcessEnvironmentWithoutPolicy() {
    	CbsClient cbsClient = Mockito.mock(CbsClient.class);
    	ConfigBindingServiceEnvironmentPostProcessor testObject = Mockito.spy(new ConfigBindingServiceEnvironmentPostProcessor());
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        SpringApplication springApplication = Mockito.mock(SpringApplication.class);
        String[] activeProfiles = {AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME};
    	Map<String, Object> filterKeyMap = Mockito.mock(Map.class);
    	String configServicePropertiesKey = "config-binding-service";
        MutablePropertySources sources = configurableEnvironment.getPropertySources();

        String strConfig =  "{\r\n" + 
                "    \"config\": {\r\n" + 
                "        \"spring.data.mongodb.uri\": \"mongodb://dcae-mongohost/dcae-tcagen2\",\r\n" + 
                "        \"streams_subscribes\": {\r\n" + 
                "            \"tca_handle_in\": {\r\n" + 
                "                \"type\": \"message_router\",\r\n" + 
                "                \"dmaap_info\": {\r\n" + 
                "                    \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.VES_MEASUREMENT_OUTPUT\"\r\n" + 
                "                }\r\n" + 
                "            }\r\n" + 
                "        },\r\n" + 
                "        \"tca.enable_ecomp_logging\": true,\r\n" + 
                "        \"tca.enable_abatement\": true,\r\n" + 
                "        \"tca.aai.password\": \"DCAE\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_group\": \"cg1\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up\": 10000,\r\n" + 
                "        \"tca.aai.node_query_path\": \"aai/v11/search/nodes-query\",\r\n" + 
                "        \"streams_publishes\": {\r\n" + 
                "            \"tca_handle_out\": {\r\n" + 
                "                \"type\": \"message_router\",\r\n" + 
                "                \"dmaap_info\": {\r\n" + 
                "                    \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.TCAGEN2-OUTPUT\"\r\n" + 
                "                }\r\n" + 
                "            }\r\n" + 
                "        },\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_ids[1]\": \"c1\",\r\n" + 
                "        \"tca.aai.generic_vnf_path\": \"aai/v11/network/generic-vnfs/generic-vnf\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down\": 30000,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.max\": 60000,\r\n" + 
                "        \"tca.aai.username\": \"DCAE\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.min\": 30000,\r\n" + 
                "        \"tca.aai.url\": \"http://aai.onap.svc.cluster.local\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.timeout\": -1,\r\n" + 
                "        \"tca.aai.enable_enrichment\": true,\r\n" + 
                "        \"tca.policy\": \"{\\\"domain\\\":\\\"measurementsForVfScaling\\\",\\\"metricsPerEventName\\\":[{\\\"eventName\\\":\\\"vFirewallBroadcastPackets\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"LESS_OR_EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":700,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"vLoadBalancer\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"Measurement_vGMUX\\\",\\\"controlLoopSchemaType\\\":\\\"VNF\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ABATED\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"GREATER\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]}]}\",\r\n" + 
                "        \"tca.processing_batch_size\": 10000,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.fixed_rate\": 0,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.message_limit\": 50000,\r\n" + 
                "        \"service_calls\": [],\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_ids[0]\": \"c0\"\r\n" + 
                "    }\r\n" + 
                "}";

        String parseConfig = "{\r\n" + 
                "    \"config\": {\r\n" + 
                "        \"spring.data.mongodb.uri\": \"mongodb://dcae-mongohost/dcae-tcagen2\",\r\n" + 
                "        \"streams_subscribes\": {\r\n" + 
                "            \"tca_handle_in\": {\r\n" + 
                "                \"type\": \"message_router\",\r\n" + 
                "                \"dmaap_info\": {\r\n" + 
                "                    \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.VES_MEASUREMENT_OUTPUT\"\r\n" + 
                "                }\r\n" + 
                "            }\r\n" + 
                "        },\r\n" + 
                "        \"tca.enable_ecomp_logging\": true,\r\n" + 
                "        \"tca.enable_abatement\": true,\r\n" + 
                "        \"tca.aai.password\": \"DCAE\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_group\": \"cg1\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up\": 10000,\r\n" + 
                "        \"tca.aai.node_query_path\": \"aai/v11/search/nodes-query\",\r\n" + 
                "        \"streams_publishes\": {\r\n" + 
                "            \"tca_handle_out\": {\r\n" + 
                "                \"type\": \"message_router\",\r\n" + 
                "                \"dmaap_info\": {\r\n" + 
                "                    \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.TCAGEN2-OUTPUT\"\r\n" + 
                "                }\r\n" + 
                "            }\r\n" + 
                "        },\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_ids[1]\": \"c1\",\r\n" + 
                "        \"tca.aai.generic_vnf_path\": \"aai/v11/network/generic-vnfs/generic-vnf\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down\": 30000,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.max\": 60000,\r\n" + 
                "        \"tca.aai.username\": \"DCAE\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.min\": 30000,\r\n" + 
                "        \"tca.aai.url\": \"http://aai.onap.svc.cluster.local\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.timeout\": -1,\r\n" + 
                "        \"tca.aai.enable_enrichment\": true,\r\n" + 
                "        \"tca.policy\": \"{\\\"domain\\\":\\\"measurementsForVfScaling\\\",\\\"metricsPerEventName\\\":[{\\\"eventName\\\":\\\"vFirewallBroadcastPackets\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"LESS_OR_EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":700,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"vLoadBalancer\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"Measurement_vGMUX\\\",\\\"controlLoopSchemaType\\\":\\\"VNF\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ABATED\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"GREATER\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]}]}\",\r\n" + 
                "        \"tca.processing_batch_size\": 10000,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.fixed_rate\": 0,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.message_limit\": 50000,\r\n" + 
                "        \"service_calls\": [],\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_ids[0]\": \"c0\"\r\n" + 
                "    }\r\n" + 
                "}"; 
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(strConfig).getAsJsonObject();
        JsonObject parseJson = parser.parse(parseConfig).getAsJsonObject();

        Mockito.when(configurableEnvironment.getActiveProfiles()).thenReturn(activeProfiles);

        Mockito.when(testObject.periodicConfigurationUpdates(cbsClient)).thenReturn(Flux.just(json));
        Mockito.doNothing().when(testObject).addJsonPropertySource(sources, new MapPropertySource(configServicePropertiesKey, filterKeyMap));
        testObject.postProcessEnvironment(configurableEnvironment, springApplication);
        assertEquals(ConfigBindingServiceEnvironmentPostProcessorTest.replaceBlank(strConfig), ConfigBindingServiceEnvironmentPostProcessorTest.replaceBlank(testObject.parseTcaConfig(parseJson)));
    }

    @Test
    void postProcessEnvironmentWithPolicy() {
        CbsClient cbsClient = Mockito.mock(CbsClient.class);
        ConfigBindingServiceEnvironmentPostProcessor testObject = Mockito.spy(new ConfigBindingServiceEnvironmentPostProcessor());
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        SpringApplication springApplication = Mockito.mock(SpringApplication.class);
        String[] activeProfiles = {AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME};
        Map<String, Object> filterKeyMap = Mockito.mock(Map.class);
        String configServicePropertiesKey = "config-binding-service";
        MutablePropertySources sources = configurableEnvironment.getPropertySources();

        String parseConfig =  "{\r\n" + 
                "    \"config\": {\r\n" + 
                "        \"config\": {\r\n" + 
                "            \"spring.data.mongodb.uri\": \"mongodb://dcae-mongohost/dcae-tcagen2\",\r\n" + 
                "            \"streams_subscribes\": {\r\n" + 
                "                \"tca_handle_in\": {\r\n" + 
                "                    \"type\": \"message_router\",\r\n" + 
                "                    \"dmaap_info\": {\r\n" + 
                "                        \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.VES_MEASUREMENT_OUTPUT\"\r\n" + 
                "                    }\r\n" + 
                "                }\r\n" + 
                "            },\r\n" + 
                "            \"tca.enable_ecomp_logging\": true,\r\n" + 
                "            \"tca.enable_abatement\": true,\r\n" + 
                "            \"tca.aai.password\": \"DCAE\",\r\n" + 
                "            \"streams_subscribes.tca_handle_in.consumer_group\": \"cg1\",\r\n" + 
                "            \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up\": 10000,\r\n" + 
                "            \"tca.aai.node_query_path\": \"aai/v11/search/nodes-query\",\r\n" + 
                "            \"streams_publishes\": {\r\n" + 
                "                \"tca_handle_out\": {\r\n" + 
                "                    \"type\": \"message_router\",\r\n" + 
                "                    \"dmaap_info\": {\r\n" + 
                "                        \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.TCAGEN2-OUTPUT\"\r\n" + 
                "                    }\r\n" + 
                "                }\r\n" + 
                "            },\r\n" + 
                "            \"streams_subscribes.tca_handle_in.consumer_ids[1]\": \"c1\",\r\n" + 
                "            \"tca.aai.generic_vnf_path\": \"aai/v11/network/generic-vnfs/generic-vnf\",\r\n" + 
                "            \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down\": 30000,\r\n" + 
                "            \"streams_subscribes.tca_handle_in.polling.auto_adjusting.max\": 60000,\r\n" + 
                "            \"tca.aai.username\": \"DCAE\",\r\n" + 
                "            \"streams_subscribes.tca_handle_in.polling.auto_adjusting.min\": 30000,\r\n" + 
                "            \"tca.aai.url\": \"http://aai.onap.svc.cluster.local\",\r\n" + 
                "            \"streams_subscribes.tca_handle_in.timeout\": -1,\r\n" + 
                "            \"tca.aai.enable_enrichment\": true,\r\n" + 
                "            \"tca.policy\": \"{\\\"domain\\\":\\\"measurementsForVfScaling\\\",\\\"metricsPerEventName\\\":[{\\\"eventName\\\":\\\"vFirewallBroadcastPackets\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"LESS_OR_EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":700,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"vLoadBalancer\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"Measurement_vGMUX\\\",\\\"controlLoopSchemaType\\\":\\\"VNF\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ABATED\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"GREATER\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]}]}\",\r\n" + 
                "            \"tca.processing_batch_size\": 10000,\r\n" + 
                "            \"streams_subscribes.tca_handle_in.polling.fixed_rate\": 0,\r\n" + 
                "            \"streams_subscribes.tca_handle_in.message_limit\": 50000,\r\n" + 
                "            \"service_calls\": [],\r\n" + 
                "            \"streams_subscribes.tca_handle_in.consumer_ids[0]\": \"c0\"\r\n" + 
                "        },\r\n" + 
                "        \"policies\": {\r\n" + 
                "            \"event\": {\r\n" + 
                "                \"action\": \"gathered\",\r\n" + 
                "                \"timestamp\": \"2020-04-08T19:45:38.927Z\",\r\n" + 
                "                \"update_id\": \"d86a3b58-8c4b-49e9-ade7-8238501adf02\",\r\n" + 
                "                \"policies_count\": 1\r\n" + 
                "            },\r\n" + 
                "            \"items\": [{\r\n" + 
                "                \"policyName\": \"onap.vfirewall.tca.1-0-0.xml\",\r\n" + 
                "                \"name\": \"onap.vfirewall.tca\",\r\n" + 
                "                \"config\": {\r\n" + 
                "                    \"tca.policy\": {\r\n" + 
                "                        \"domain\": \"measurementsForVfScaling\",\r\n" + 
                "                        \"metricsPerEventName\": [{\r\n" + 
                "                            \"policyName\": \"onap.vfirewall.tca\",\r\n" + 
                "                            \"policyScope\": \"resource=vLoadBalancer;type=configuration\",\r\n" + 
                "                            \"thresholds\": [{\r\n" + 
                "                                \"direction\": \"LESS_OR_EQUAL\",\r\n" + 
                "                                \"severity\": \"MAJOR\",\r\n" + 
                "                                \"closedLoopControlName\": \"VVK_ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\",\r\n" + 
                "                                \"fieldPath\": \"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\r\n" + 
                "                                \"version\": \"1.0.2\",\r\n" + 
                "                                \"closedLoopEventStatus\": \"ONSET\",\r\n" + 
                "                                \"thresholdValue\": 500\r\n" + 
                "                            }, {\r\n" + 
                "                                \"direction\": \"GREATER_OR_EQUAL\",\r\n" + 
                "                                \"severity\": \"CRITICAL\",\r\n" + 
                "                                \"closedLoopControlName\": \"VVK_ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\",\r\n" + 
                "                                \"fieldPath\": \"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\r\n" + 
                "                                \"version\": \"1.0.2\",\r\n" + 
                "                                \"closedLoopEventStatus\": \"ONSET\",\r\n" + 
                "                                \"thresholdValue\": 5000\r\n" + 
                "                            }],\r\n" + 
                "                            \"eventName\": \"vLoadBalancer\",\r\n" + 
                "                            \"controlLoopSchemaType\": \"VNF\",\r\n" + 
                "                            \"policyVersion\": \"v0.0.1\"\r\n" + 
                "                        }]\r\n" + 
                "                    }\r\n" + 
                "                },\r\n" + 
                "                \"type_version\": \"1.0.0\",\r\n" + 
                "                \"version\": \"1.0.0\",\r\n" + 
                "                \"policyVersion\": \"1.0.0\",\r\n" + 
                "                \"type\": \"onap.policies.monitoring.cdap.tca.hi.lo.app\",\r\n" + 
                "                \"metadata\": {\r\n" + 
                "                    \"policy-id\": \"onap.vfirewall.tca\",\r\n" + 
                "                    \"policy-version\": \"1.0.0\"\r\n" + 
                "                }\r\n" + 
                "            }]\r\n" + 
                "        }\r\n" + 
                "    }\r\n" + 
                "}";

        String strConfig = "{\r\n" + 
                "    \"config\": {\r\n" + 
                "        \"spring.data.mongodb.uri\": \"mongodb://dcae-mongohost/dcae-tcagen2\",\r\n" + 
                "        \"streams_subscribes\": {\r\n" + 
                "            \"tca_handle_in\": {\r\n" + 
                "                \"type\": \"message_router\",\r\n" + 
                "                \"dmaap_info\": {\r\n" + 
                "                    \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.VES_MEASUREMENT_OUTPUT\"\r\n" + 
                "                }\r\n" + 
                "            }\r\n" + 
                "        },\r\n" + 
                "        \"tca.enable_ecomp_logging\": true,\r\n" + 
                "        \"tca.enable_abatement\": true,\r\n" + 
                "        \"tca.aai.password\": \"DCAE\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_group\": \"cg1\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up\": 10000,\r\n" + 
                "        \"tca.aai.node_query_path\": \"aai/v11/search/nodes-query\",\r\n" + 
                "        \"streams_publishes\": {\r\n" + 
                "            \"tca_handle_out\": {\r\n" + 
                "                \"type\": \"message_router\",\r\n" + 
                "                \"dmaap_info\": {\r\n" + 
                "                    \"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.TCAGEN2-OUTPUT\"\r\n" + 
                "                }\r\n" + 
                "            }\r\n" + 
                "        },\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_ids[1]\": \"c1\",\r\n" + 
                "        \"tca.aai.generic_vnf_path\": \"aai/v11/network/generic-vnfs/generic-vnf\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down\": 30000,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.max\": 60000,\r\n" + 
                "        \"tca.aai.username\": \"DCAE\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.auto_adjusting.min\": 30000,\r\n" + 
                "        \"tca.aai.url\": \"http://aai.onap.svc.cluster.local\",\r\n" + 
                "        \"streams_subscribes.tca_handle_in.timeout\": -1,\r\n" + 
                "        \"tca.aai.enable_enrichment\": true,\r\n" + 
                "        \"tca.policy\": \"{\\\"domain\\\":\\\"measurementsForVfScaling\\\",\\\"metricsPerEventName\\\":[{\\\"eventName\\\":\\\"vFirewallBroadcastPackets\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"LESS_OR_EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":700,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"vLoadBalancer\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"Measurement_vGMUX\\\",\\\"controlLoopSchemaType\\\":\\\"VNF\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ABATED\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"GREATER\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]}]}\",\r\n" + 
                "        \"tca.processing_batch_size\": 10000,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.polling.fixed_rate\": 0,\r\n" + 
                "        \"streams_subscribes.tca_handle_in.message_limit\": 50000,\r\n" + 
                "        \"service_calls\": [],\r\n" + 
                "        \"streams_subscribes.tca_handle_in.consumer_ids[0]\": \"c0\"\r\n" + 
                "    },\r\n" + 
                "    \"policies\": {\r\n" + 
                "        \"event\": {\r\n" + 
                "            \"action\": \"gathered\",\r\n" + 
                "            \"timestamp\": \"2020-04-08T19:45:38.927Z\",\r\n" + 
                "            \"update_id\": \"d86a3b58-8c4b-49e9-ade7-8238501adf02\",\r\n" + 
                "            \"policies_count\": 1\r\n" + 
                "        },\r\n" + 
                "        \"items\": [{\r\n" + 
                "            \"policyName\": \"onap.vfirewall.tca.1-0-0.xml\",\r\n" + 
                "            \"name\": \"onap.vfirewall.tca\",\r\n" + 
                "            \"config\": {\r\n" + 
                "                \"tca.policy\": {\r\n" + 
                "                    \"domain\": \"measurementsForVfScaling\",\r\n" + 
                "                    \"metricsPerEventName\": [{\r\n" + 
                "                        \"policyName\": \"onap.vfirewall.tca\",\r\n" + 
                "                        \"policyScope\": \"resource=vLoadBalancer;type=configuration\",\r\n" + 
                "                        \"thresholds\": [{\r\n" + 
                "                            \"direction\": \"LESS_OR_EQUAL\",\r\n" + 
                "                            \"severity\": \"MAJOR\",\r\n" + 
                "                            \"closedLoopControlName\": \"VVK_ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\",\r\n" + 
                "                            \"fieldPath\": \"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\r\n" + 
                "                            \"version\": \"1.0.2\",\r\n" + 
                "                            \"closedLoopEventStatus\": \"ONSET\",\r\n" + 
                "                            \"thresholdValue\": 500\r\n" + 
                "                        }, {\r\n" + 
                "                            \"direction\": \"GREATER_OR_EQUAL\",\r\n" + 
                "                            \"severity\": \"CRITICAL\",\r\n" + 
                "                            \"closedLoopControlName\": \"VVK_ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\",\r\n" + 
                "                            \"fieldPath\": \"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedBroadcastPacketsAccumulated\",\r\n" + 
                "                            \"version\": \"1.0.2\",\r\n" + 
                "                            \"closedLoopEventStatus\": \"ONSET\",\r\n" + 
                "                            \"thresholdValue\": 5000\r\n" + 
                "                        }],\r\n" + 
                "                        \"eventName\": \"vLoadBalancer\",\r\n" + 
                "                        \"controlLoopSchemaType\": \"VNF\",\r\n" + 
                "                        \"policyVersion\": \"v0.0.1\"\r\n" + 
                "                    }]\r\n" + 
                "                }\r\n" + 
                "            },\r\n" + 
                "            \"type_version\": \"1.0.0\",\r\n" + 
                "            \"version\": \"1.0.0\",\r\n" + 
                "            \"policyVersion\": \"1.0.0\",\r\n" + 
                "            \"type\": \"onap.policies.monitoring.cdap.tca.hi.lo.app\",\r\n" + 
                "            \"metadata\": {\r\n" + 
                "                \"policy-id\": \"onap.vfirewall.tca\",\r\n" + 
                "                \"policy-version\": \"1.0.0\"\r\n" + 
                "            }\r\n" + 
                "        }]\r\n" + 
                "    }\r\n" + 
                "}"; 
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(strConfig).getAsJsonObject();
        JsonObject parseJson = parser.parse(parseConfig).getAsJsonObject();

        Mockito.when(configurableEnvironment.getActiveProfiles()).thenReturn(activeProfiles);

        Mockito.when(testObject.periodicConfigurationUpdates(cbsClient)).thenReturn(Flux.just(json));
        Mockito.doNothing().when(testObject).addJsonPropertySource(sources, new MapPropertySource(configServicePropertiesKey, filterKeyMap));
        testObject.postProcessEnvironment(configurableEnvironment, springApplication);
        assertEquals(ConfigBindingServiceEnvironmentPostProcessorTest.replaceBlank(strConfig), ConfigBindingServiceEnvironmentPostProcessorTest.replaceBlank(testObject.parseTcaConfig(parseJson)));
    }

    public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
