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

package org.onap.dcae.analytics.tca.web.aai;

import static org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion.TCA_OBJECT_MAPPER;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.tca.core.service.TcaAaiEnrichmentService;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.util.LogSpec;
import org.onap.dcae.analytics.tca.model.facade.Aai;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogFactory;
import org.onap.dcae.utils.eelf.logger.api.log.EELFLogger;
import org.onap.dcae.utils.eelf.logger.api.spec.DebugLogSpec;
import org.onap.dcae.utils.eelf.logger.api.spec.ErrorLogSpec;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Rajiv Singla
 */
public class TcaAaiEnrichmentServiceImpl implements TcaAaiEnrichmentService {

    private static final EELFLogger logger = EELFLogFactory.getLogger(TcaAaiEnrichmentServiceImpl.class);

    private final TcaAppProperties tcaAppProperties;
    private final RestTemplate aaiRestTemplate;

    public TcaAaiEnrichmentServiceImpl(final TcaAppProperties tcaAppProperties,
                                       final RestTemplate aaiRestTemplate) {
        this.tcaAppProperties = tcaAppProperties;
        this.aaiRestTemplate = aaiRestTemplate;
    }


    @Override
    public TcaAlert doAaiEnrichment(final TcaExecutionContext tcaExecutionContext) {

        final TcaAlert tcaAlert = tcaExecutionContext.getTcaResultContext().getTcaAlert();

        // do aai enrichment
        if (isVNFAlert(tcaAlert)) {
            // do vnf enrichment
            doAaiVnfEnrichment(tcaExecutionContext, aaiRestTemplate, tcaAppProperties);
        } else {
            // do vm enrichment
            doAaiVmEnrichment(tcaExecutionContext, aaiRestTemplate, tcaAppProperties);
        }

        return tcaAlert;
    }

    private static void doAaiVmEnrichment(final TcaExecutionContext tcaExecutionContext,
                                          final RestTemplate aaiRestTemplate,
                                          final TcaAppProperties tcaAppProperties) {

        final TcaAlert tcaAlert = tcaExecutionContext.getTcaResultContext().getTcaAlert();
        final String requestId = tcaExecutionContext.getRequestId();
        final String transactionId = tcaExecutionContext.getTransactionId();
        final String vServerName = tcaAlert.getAai().getGenericServerName();

        // create new node query uri
        final TcaAppProperties.Aai aai = tcaAppProperties.getTca().getAai();
        final URI nodeQueryUri = UriComponentsBuilder.fromHttpUrl(aai.getUrl())
                .path(aai.getNodeQueryPath())
                .queryParam("search-node-type", "vserver")
                .queryParam("filter", "vserver-name:EQUALS:" + vServerName)
                .build(Collections.emptyMap());

        // get resource link
        final String resourceLink =
                getVMResourceLink(requestId, getAAIRestAPIResponse(aaiRestTemplate, nodeQueryUri, requestId, transactionId));
        if (resourceLink == null) {
            return;
        }

        // create virtual server enrichment uri
        final URI vServerEnrichmentUri = UriComponentsBuilder.fromHttpUrl(aai.getUrl())
                .path(resourceLink)
                .build(Collections.emptyMap());

        // fetch virtual server enrichment details
        final String vServerEnrichmentDetails =
                getAAIRestAPIResponse(aaiRestTemplate, vServerEnrichmentUri, requestId, transactionId);

        // do aai enrichment
        enrichAAI(requestId, vServerEnrichmentDetails, tcaAlert, TcaModelConstants.AAI_VSERVER_KEY_PREFIX);
    }

    private static void doAaiVnfEnrichment(final TcaExecutionContext tcaExecutionContext,
                                           final RestTemplate aaiRestTemplate,
                                           final TcaAppProperties tcaAppProperties) {

        final TcaAlert tcaAlert = tcaExecutionContext.getTcaResultContext().getTcaAlert();
        final String requestId = tcaExecutionContext.getRequestId();
        final String transactionId = tcaExecutionContext.getTransactionId();
        final String genericVnfName = tcaAlert.getAai().getGenericVNFName();

        // create new generic vnf uri
        final TcaAppProperties.Aai aai = tcaAppProperties.getTca().getAai();
        final URI genericVnfUri = UriComponentsBuilder.fromHttpUrl(aai.getUrl())
                .path(aai.getGenericVnfPath())
                .queryParam("vnf-name", genericVnfName)
                .build(Collections.emptyMap());

        // fetch response
        final String aaiResponse = getAAIRestAPIResponse(aaiRestTemplate, genericVnfUri, requestId, transactionId);

        // do AAI enrichment
        enrichAAI(requestId, aaiResponse, tcaAlert, TcaModelConstants.AAI_VNF_KEY_PREFIX);
    }


    /**
     * Fetch response from A&AI Rest API as json string for given aai uri. Returns null if unable to fetch response
     * from A&AI API
     *
     * @param aaiRestTemplate aai rest template
     * @param aaiUri aai uri
     *
     * @return A&AI API response as json string
     */
    private static String getAAIRestAPIResponse(final RestTemplate aaiRestTemplate, final URI aaiUri,
                                                final String requestId, final String transactionId) {
        // fetch response
        ResponseEntity<String> aaiResponseEntity = null;
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.add(AnalyticsHttpConstants.REQUEST_ID_HEADER_KEY, requestId);
            headers.add(AnalyticsHttpConstants.REQUEST_TRANSACTION_ID_HEADER_KEY, transactionId);
            final HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            aaiResponseEntity = aaiRestTemplate.exchange(aaiUri, HttpMethod.GET, httpEntity, String.class);
        } catch (Exception e) {
            final ErrorLogSpec errorLogSpec = LogSpec.createErrorLogSpec(requestId);
            logger.errorLog().error("Request id: " + requestId + ". Unable to get A&AI enrichment details",
                    errorLogSpec, e.toString());
        }

        if (aaiResponseEntity != null && aaiResponseEntity.getStatusCode().is2xxSuccessful()) {
            return aaiResponseEntity.getBody();
        }

        return null;
    }


    /**
     * Populates A&AI details retrieved from A&AI Enrichment API into Alerts A&AI Object
     *
     * @param aaiEnrichmentDetails A&AI Enrichment API fetched JSON String
     * @param tcaAlert tca alert
     * @param keyPrefix Key prefix that needs to be added to each fetched A&AI Enrichment record
     *
     * @return true if A&AI enrichment completed successfully
     */
    private static boolean enrichAAI(final String requestId, final String aaiEnrichmentDetails, final TcaAlert tcaAlert,
                                     final String keyPrefix) {

        final Aai preEnrichmentAAI = tcaAlert.getAai();
        final ErrorLogSpec errorLogSpec = LogSpec.createErrorLogSpec(requestId);
        final DebugLogSpec debugLogSpec = LogSpec.createDebugLogSpec(requestId);

        if (aaiEnrichmentDetails == null) {
            logger.errorLog().error("Request id: {}. No A&AI Enrichment possible. A&AI Enrichment details are absent.",
                    errorLogSpec, tcaAlert.getRequestId());
            return false;
        }

        final Aai enrichedAAI = getNewEnrichedAAI(requestId, aaiEnrichmentDetails);

        if (enrichedAAI != null) {
            final Set<Map.Entry<String, Object>> enrichedAAIEntrySet =
                    enrichedAAI.getDynamicProperties().entrySet();
            final Map<String, Object> preEnrichmentAAIDynamicProperties = preEnrichmentAAI.getDynamicProperties();

            // populate Alert A&AI Enrichment details and add prefix to key
            for (Map.Entry<String, Object> enrichedAAIEntry : enrichedAAIEntrySet) {
                preEnrichmentAAIDynamicProperties.put(
                        keyPrefix + enrichedAAIEntry.getKey(), enrichedAAIEntry.getValue());
            }

            logger.debugLog().debug("Request id: {}. A&AI Enrichment was completed successfully.",
                    debugLogSpec, tcaAlert.getRequestId());
            return true;
        } else {
            logger.errorLog().error("Request id: {}. No A&AI Enrichment possible. Skipped - Invalid A&AI Response.",
                    errorLogSpec, tcaAlert.getRequestId());
            return false;
        }

    }

    /**
     * Creates a new A&AI object with only top level A&AI Enrichment details
     *
     * @param aaiEnrichmentDetails A&AI Enrichment details
     *
     * @return new A&AI with only top level A&AI Enrichment details
     */
    private static Aai getNewEnrichedAAI(final String requestId, final String aaiEnrichmentDetails) {
        try {
            final JsonNode rootNode = TCA_OBJECT_MAPPER.readTree(aaiEnrichmentDetails);
            final Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {
                final Map.Entry<String, JsonNode> fieldEntry = fieldsIterator.next();
                final JsonNode jsonNode = fieldEntry.getValue();
                // remove all arrays, objects from A&AI Enrichment Json
                if (jsonNode.isPojo() || jsonNode.isObject() || jsonNode.isArray()) {
                    fieldsIterator.remove();
                }
            }
            return TCA_OBJECT_MAPPER.treeToValue(rootNode, Aai.class);
        } catch (IOException e) {
            final ErrorLogSpec errorLogSpec = LogSpec.createErrorLogSpec(requestId);
            logger.errorLog().error(
                    "Failed to Parse AAI Enrichment Details from JSON: {}, Exception: {}.",
                    errorLogSpec, aaiEnrichmentDetails, e.toString());
        }
        return null;
    }

    /**
     * Fetches VM Object Resource Link from A&AI Resource Link Json
     *
     * @param vmAAIResourceLinkDetails VM Object Resource Link from A&AI Resource Link Json
     *
     * @return object resource link String
     */
    private static String getVMResourceLink(final String requestId, final String vmAAIResourceLinkDetails) {
        if (StringUtils.hasText(vmAAIResourceLinkDetails)) {
            try {
                final JsonNode jsonNode = TCA_OBJECT_MAPPER.readTree(vmAAIResourceLinkDetails);
                final JsonNode resourceLinkJsonNode = jsonNode.findPath("resource-link");
                if (!resourceLinkJsonNode.isMissingNode()) {
                    return resourceLinkJsonNode.asText();
                }
            } catch (IOException e) {
                final ErrorLogSpec errorLogSpec = LogSpec.createErrorLogSpec(requestId);
                logger.errorLog().error("Unable to determine VM Object link inside AAI Resource Link Response JSON: {}",
                        errorLogSpec, vmAAIResourceLinkDetails, e.toString());
            }
        }
        return null;
    }


    private static boolean isVNFAlert(final TcaAlert tcaAlert) {
        return tcaAlert.getTargetType().equals(TcaModelConstants.TCA_ALERT_VNF_TARGET_TYPE);
    }

}
