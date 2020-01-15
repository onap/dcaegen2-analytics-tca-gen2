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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsClient;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.EnvProperties;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.ImmutableEnvProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ConfigBindingServiceEnvironmentPostProcessorTest extends BaseAnalyticsWebTest {

    @Test
    void postProcessEnvironment() {
    	CbsClient cbsClient = Mockito.mock(CbsClient.class);
    	ConfigBindingServiceEnvironmentPostProcessor testObject = Mockito.spy(new ConfigBindingServiceEnvironmentPostProcessor());
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        SpringApplication springApplication = Mockito.mock(SpringApplication.class);
        String[] activeProfiles = {AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME};
    	Map<String, Object> filterKeyMap = Mockito.mock(Map.class);
    	String configServicePropertiesKey = "config-binding-service";
        MutablePropertySources sources = configurableEnvironment.getPropertySources();

    	EnvProperties envProperties;
        envProperties = ImmutableEnvProperties.builder() //
 		    .consulHost("consul-server.onap") //
 		    .consulPort(8500) //
 		    .cbsName("config-binding-service") //
 		    .appName("dcae-tca-analytics") //
 		    .build();
        String strConfig =  "{\"spring.data.mongodb.uri\": \"mongodb://tcagen2-mongohost/analytics-tca\", \"streams_subscribes\": {\"tca_handle_in\": {\"type\": \"message_router\", \"dmaap_info\": {\"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.VES_MEASUREMENT3_OUTPUT\"}}}, \"tca.enable_ecomp_logging\": true, \"tca.enable_abatement\": true, \"tca.aai.password\": \"DCAE\", \"streams_subscribes.tca_handle_in.consumer_group\": \"cg1\", \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up\": 10000, \"tca.aai.node_query_path\": \"aai/v11/search/nodes-query\", \"streams_publishes\": {\"tca_handle_out\": {\"type\": \"message_router\", \"dmaap_info\": {\"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.TCAGEN3-OUTPUT\"}}}, \"streams_subscribes.tca_handle_in.consumer_ids[1]\": \"c1\", \"tca.aai.generic_vnf_path\": \"aai/v11/network/generic-vnfs/generic-vnf\", \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down\": 30000, \"streams_subscribes.tca_handle_in.polling.auto_adjusting.max\": 60000, \"tca.aai.username\": \"DCAE\", \"streams_subscribes.tca_handle_in.polling.auto_adjusting.min\": 30000, \"tca.aai.url\": \"http://aai.onap.svc.cluster.local\", \"streams_subscribes.tca_handle_in.timeout\": -1, \"tca.aai.enable_enrichment\": true, \"tca.policy\": \"{\\\"domain\\\":\\\"measurementsForVfScaling\\\",\\\"metricsPerEventName\\\":[{\\\"eventName\\\":\\\"vFirewallBroadcastPackets\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.3\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"LESS_OR_EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":700,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"vLoadBalancer\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"Measurement_vGMUX\\\",\\\"controlLoopSchemaType\\\":\\\"VNF\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ABATED\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"GREATER\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]}]}\", \"tca.processing_batch_size\": 10000, \"streams_subscribes.tca_handle_in.polling.fixed_rate\": 0, \"streams_subscribes.tca_handle_in.message_limit\": 50000, \"service_calls\": [], \"streams_subscribes.tca_handle_in.consumer_ids[0]\": \"c0\"}";

        String parseConfig = "{\"config\": {\"spring.data.mongodb.uri\": \"mongodb://tcagen2-mongohost/analytics-tca\", \"streams_subscribes\": {\"tca_handle_in\": {\"type\": \"message_router\", \"dmaap_info\": {\"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.VES_MEASUREMENT3_OUTPUT\"}}}, \"tca.enable_ecomp_logging\": true, \"tca.enable_abatement\": true, \"tca.aai.password\": \"DCAE\", \"streams_subscribes.tca_handle_in.consumer_group\": \"cg1\", \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_up\": 10000, \"tca.aai.node_query_path\": \"aai/v11/search/nodes-query\", \"streams_publishes\": {\"tca_handle_out\": {\"type\": \"message_router\", \"dmaap_info\": {\"topic_url\": \"http://message-router.onap.svc.cluster.local:3904/events/unauthenticated.TCAGEN3-OUTPUT\"}}}, \"streams_subscribes.tca_handle_in.consumer_ids[1]\": \"c1\", \"tca.aai.generic_vnf_path\": \"aai/v11/network/generic-vnfs/generic-vnf\", \"streams_subscribes.tca_handle_in.polling.auto_adjusting.step_down\": 30000, \"streams_subscribes.tca_handle_in.polling.auto_adjusting.max\": 60000, \"tca.aai.username\": \"DCAE\", \"streams_subscribes.tca_handle_in.polling.auto_adjusting.min\": 30000, \"tca.aai.url\": \"http://aai.onap.svc.cluster.local\", \"streams_subscribes.tca_handle_in.timeout\": -1, \"tca.aai.enable_enrichment\": true, \"tca.policy\": \"{\\\"domain\\\":\\\"measurementsForVfScaling\\\",\\\"metricsPerEventName\\\":[{\\\"eventName\\\":\\\"vFirewallBroadcastPackets\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.3\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"LESS_OR_EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vFirewall-d0a1dfc6-94f5-4fd4-a5b5-4630b438850a\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":700,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"vLoadBalancer\\\",\\\"controlLoopSchemaType\\\":\\\"VM\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vDNS-6f37f56d-a87d-4b85-b6a9-cc953cf779b3\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.vNicPerformanceArray[*].receivedTotalPacketsDelta\\\",\\\"thresholdValue\\\":300,\\\"direction\\\":\\\"GREATER_OR_EQUAL\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]},{\\\"eventName\\\":\\\"Measurement_vGMUX\\\",\\\"controlLoopSchemaType\\\":\\\"VNF\\\",\\\"policyScope\\\":\\\"DCAE\\\",\\\"policyName\\\":\\\"DCAE.Config_tca-hi-lo\\\",\\\"policyVersion\\\":\\\"v0.0.1\\\",\\\"thresholds\\\":[{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"EQUAL\\\",\\\"severity\\\":\\\"MAJOR\\\",\\\"closedLoopEventStatus\\\":\\\"ABATED\\\"},{\\\"closedLoopControlName\\\":\\\"ControlLoop-vCPE-48f0c2c3-a172-4192-9ae3-052274181b6e\\\",\\\"version\\\":\\\"1.0.2\\\",\\\"fieldPath\\\":\\\"$.event.measurementsForVfScalingFields.additionalMeasurements[*].arrayOfFields[0].value\\\",\\\"thresholdValue\\\":0,\\\"direction\\\":\\\"GREATER\\\",\\\"severity\\\":\\\"CRITICAL\\\",\\\"closedLoopEventStatus\\\":\\\"ONSET\\\"}]}]}\", \"tca.processing_batch_size\": 10000, \"streams_subscribes.tca_handle_in.polling.fixed_rate\": 0, \"streams_subscribes.tca_handle_in.message_limit\": 50000, \"service_calls\": [], \"streams_subscribes.tca_handle_in.consumer_ids[0]\": \"c0\"}}"; 
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(strConfig).getAsJsonObject();
        JsonObject parseJson = parser.parse(parseConfig).getAsJsonObject();

        Mockito.when(configurableEnvironment.getActiveProfiles()).thenReturn(activeProfiles);
        Mockito.when(testObject.readEnvironmentVariables()).thenReturn(Mono.just(envProperties));
        Mockito.when(testObject.createCbsClient(envProperties)).thenReturn(Mono.just(cbsClient));
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
