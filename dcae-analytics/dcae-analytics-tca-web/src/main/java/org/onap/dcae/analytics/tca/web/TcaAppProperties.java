/*
 * ================================================================================
 * Copyright (c) 2019-2020 China Mobile. All rights reserved.
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

package org.onap.dcae.analytics.tca.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onap.dcae.analytics.model.TcaModelConstants;
import org.onap.dcae.analytics.model.configbindingservice.BaseConfigBindingServiceProperties;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

/**
 * @author Kai Lu
 */
@Component
public class TcaAppProperties extends BaseConfigBindingServiceProperties {

    @Autowired
    private Environment env;

    /**
     * TCA Application properties
     */
    @Data
    public static class Tca {
        private String policy;
        private Integer processingBatchSize = TcaModelConstants.DEFAULT_TCA_PROCESSING_BATCH_SIZE;
        private Boolean enableAbatement = TcaModelConstants.DEFAULT_ABATEMENT_ENABLED;
        private Boolean enableEcompLogging = TcaModelConstants.DEFAULT_ECOMP_LOGGING_ENABLED;
        private Aai aai = new Aai();
    }

    /**
     * A&amp;AI properties
     */
    @Data
    @ToString(exclude = "password")
    public static class Aai {

        private Boolean enableEnrichment = TcaModelConstants.DEFAULT_AAI_ENRICHMENT_ENABLED;
        private String url;
        private String username;
        private String password;

        private String proxyUrl = null;
        private Boolean ignoreSSLValidation = TcaModelConstants.DEFAULT_TCA_AAI_IGNORE_SSL_VALIDATION;

        private String genericVnfPath = TcaModelConstants.DEFAULT_TCA_AAI_GENERIC_VNF_PATH;
        private String nodeQueryPath = TcaModelConstants.DEFAULT_TCA_AAI_NODE_QUERY_PATH;

    }

    public Tca getTca() {
        Tca tca = new Tca();
        tca.setPolicy(env.getProperty(ConfigBindingServiceConstants.POLICY));
        tca.setProcessingBatchSize(env.getProperty(ConfigBindingServiceConstants.PROCESSINGBATCHSIZE, Integer.class));
        tca.setEnableAbatement(env.getProperty(ConfigBindingServiceConstants.ENABLEABATEMENT, Boolean.class));
        tca.setEnableEcompLogging(env.getProperty(ConfigBindingServiceConstants.EnableEcompLogging, Boolean.class));

        Aai aai = tca.getAai();
        aai.setEnableEnrichment(env.getProperty(ConfigBindingServiceConstants.EnableEnrichment, Boolean.class));
        aai.setUrl(env.getProperty(ConfigBindingServiceConstants.AAIURL));
        aai.setUsername(env.getProperty(ConfigBindingServiceConstants.AAIUSERNAME));
        aai.setPassword(env.getProperty(ConfigBindingServiceConstants.AAIPASSWORD));
        aai.setGenericVnfPath(ConfigBindingServiceConstants.AAIGENERICVNFPATH);
        aai.setNodeQueryPath(env.getProperty(ConfigBindingServiceConstants.AAINODEQUERYPATH));
        tca.setAai(aai);

        return tca;
    }

    @Override
    public Map<String, PublisherDetails> getStreamsPublishes() {
        DmaapInfo dmaapInfo = new DmaapInfo();
        dmaapInfo.setTopicUrl(env.getProperty(ConfigBindingServiceConstants.PUBTOPICURL));

        PublisherDetails detail = new PublisherDetails();
        detail.setType(env.getProperty(ConfigBindingServiceConstants.PUBTYPE));
        detail.setDmaapInfo(dmaapInfo);
        streamsPublishes.put(ConfigBindingServiceConstants.PUBKEY, detail);
        return streamsPublishes;
    }

    @Override
    public Map<String, SubscriberDetails> getStreamsSubscribes() {
        DmaapInfo dmaapInfo = new DmaapInfo();
        dmaapInfo.setTopicUrl(env.getProperty(ConfigBindingServiceConstants.SUBTOPICURL));

        AutoAdjusting autoAdjust = new AutoAdjusting();
        autoAdjust.setStepUp(env.getProperty(ConfigBindingServiceConstants.SUBAUTOADJUSTINGSTEPUP, Integer.class));
        autoAdjust.setStepDown(env.getProperty(ConfigBindingServiceConstants.SUBAUTOADJUSTINGSTEPDOWN, Integer.class));
        autoAdjust.setMax(env.getProperty(ConfigBindingServiceConstants.SUBAUTOADJUSTINGMAX, Integer.class));
        autoAdjust.setMin(env.getProperty(ConfigBindingServiceConstants.SUBAUTOADJUSTINGMIN, Integer.class));

        Polling poll = new Polling();
        poll.setAutoAdjusting(autoAdjust);
        poll.setFixedRate(env.getProperty(ConfigBindingServiceConstants.SUBFIXEDRATE, Integer.class));

        SubscriberDetails detail = new SubscriberDetails();
        detail.setType(env.getProperty(ConfigBindingServiceConstants.SUBTYPE));
        detail.setDmaapInfo(dmaapInfo);
        detail.setPolling(poll);

        detail.setConsumerGroup(env.getProperty(ConfigBindingServiceConstants.SUBCONSUMERGROUP));
        detail.setMessageLimit(env.getProperty(ConfigBindingServiceConstants.SUBMESSAGELIMIT, Integer.class));
        detail.setTimeout(env.getProperty(ConfigBindingServiceConstants.SUBTIMEOUT, Integer.class));
        List<String> consumerIds = new ArrayList<>();
        consumerIds.add(env.getProperty(ConfigBindingServiceConstants.SUBCONSUMERIDS0));
        consumerIds.add(env.getProperty(ConfigBindingServiceConstants.SUBCONSUMERIDS1));
        detail.setConsumerIds(consumerIds);

        streamsSubscribes.put(ConfigBindingServiceConstants.SUBKEY, detail);
        return streamsSubscribes;
    }

}