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

package org.onap.dcae.analytics.tca.web.domain;

import static org.onap.dcae.analytics.tca.model.util.json.TcaModelJsonConversion.TCA_POLICY_JSON_FUNCTION;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.onap.dcae.analytics.model.common.ConfigSource;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicyModel;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.web.exception.AnalyticsParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rajiv Singla
 */
public class TcaPolicyWrapper implements TcaPolicyModel {

    private static final Logger logger = LoggerFactory.getLogger(TcaPolicyWrapper.class);

    private final ZonedDateTime creationTime;
    private ZonedDateTime updateDateTime;
    private String tcaPolicy;
    private ConfigSource configSource;
    private AtomicInteger policyUpdateSequence;
    private String policyVersion;

    private final TcaAppProperties tcaAppProperties;

    public TcaPolicyWrapper(final TcaAppProperties tcaAppProperties) {
        this.tcaAppProperties = tcaAppProperties;
        this.creationTime = ZonedDateTime.now();
        this.tcaPolicy = tcaAppProperties.getTca().getPolicy();
        policyUpdateSequence = new AtomicInteger(0);
        this.updateDateTime = ZonedDateTime.now();
        this.policyVersion = getPolicyVersion(new AtomicInteger(0));
    }

    public TcaPolicy getTcaPolicy() {
        String tcaPolicyString = tcaAppProperties.getTca().getPolicy();
        boolean isConfigBindingServiceProfileActive = tcaAppProperties.isConfigBindingServiceProfileActive();
        if (isConfigBindingServiceProfileActive) {
            this.configSource = ConfigSource.CONFIG_BINDING_SERVICE;
        } else {
            this.configSource = ConfigSource.CLASSPATH;
        }

        if (!tcaPolicyString.equals(tcaPolicy)) {
            this.tcaPolicy = tcaPolicyString;
            this.updateDateTime = ZonedDateTime.now();
            policyUpdateSequence.getAndUpdate(sequence -> sequence + 1);
            this.policyVersion = getPolicyVersion(policyUpdateSequence);
            logger.info("Updated Tca Policy Wrapper with policy: {}, from Source: {}, policy Version: {}",
                    tcaPolicy, configSource.name(), policyVersion);
        }

        return convertTcaPolicy(tcaPolicyString);
    }

    public void setTcaPolicy(TcaPolicy tcaPolicy, ConfigSource configSource) {
        this.tcaPolicy = tcaPolicy.toString();
        this.configSource = configSource;
    }

    public TcaPolicy convertTcaPolicy(String tcaPolicyString) {
        return TCA_POLICY_JSON_FUNCTION.apply(tcaPolicyString).orElseThrow(
                () -> new AnalyticsParsingException("Unable to parse Tca Policy String: " + tcaPolicyString,
                        new IllegalArgumentException()));
    }

    private static String getPolicyVersion(final AtomicInteger policyUpdateSequence) {
        return "version-" + policyUpdateSequence.intValue();
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public ZonedDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public ConfigSource getConfigSource() {
        return configSource;
    }

    public AtomicInteger getPolicyUpdateSequence() {
        return policyUpdateSequence;
    }

    public String getPolicyVersion() {
        return policyVersion;
    }   

}
