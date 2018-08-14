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

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.onap.dcae.analytics.model.common.ConfigSource;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicyModel;
import org.onap.dcae.analytics.tca.web.validation.TcaPolicyValidator;
import org.onap.dcae.analytics.web.exception.AnalyticsParsingException;
import org.onap.dcae.analytics.web.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rajiv Singla
 */
@Getter
@ToString
public class TcaPolicyWrapper implements TcaPolicyModel {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TcaPolicyWrapper.class);

    private final ZonedDateTime creationTime;
    private ZonedDateTime updateDateTime;
    private TcaPolicy tcaPolicy;
    private ConfigSource configSource;
    private AtomicInteger policyUpdateSequence;
    private String policyVersion;

    public TcaPolicyWrapper(final String tcaPolicyString, final ConfigSource configSource) {
        createOrUpdatePolicy(getTcaPolicy(tcaPolicyString), configSource);
        this.creationTime = ZonedDateTime.now();
    }

    public void setTcaPolicy(final String tcaPolicyString, final ConfigSource configSource) {
        createOrUpdatePolicy(getTcaPolicy(tcaPolicyString), configSource);
    }

    public void setTcaPolicy(final TcaPolicy tcaPolicy, final ConfigSource configSource) {
        createOrUpdatePolicy(tcaPolicy, configSource);
    }

    private void createOrUpdatePolicy(final TcaPolicy tcaPolicy, final ConfigSource configSource) {
        ValidationUtils.validate(tcaPolicy, new TcaPolicyValidator());
        this.tcaPolicy = tcaPolicy;
        this.configSource = configSource;
        this.updateDateTime = ZonedDateTime.now();
        if (policyUpdateSequence == null) {
            policyUpdateSequence = new AtomicInteger(0);
        } else {
            policyUpdateSequence.getAndUpdate(sequence -> sequence + 1);
        }
        this.policyVersion = getPolicyVersion(policyUpdateSequence);
        final String configSourceName = configSource.name();
        LOGGER.info("Updated Tca Policy Wrapper with policy: {}, from Source: {}, policy Version: {}",
                tcaPolicy, configSourceName, policyVersion);
    }


    private TcaPolicy getTcaPolicy(final String tcaPolicyString) {
        return TCA_POLICY_JSON_FUNCTION.apply(tcaPolicyString).orElseThrow(
                () -> new AnalyticsParsingException("Unable to parse Tca Policy String: " + tcaPolicyString,
                        new IllegalArgumentException()));
    }


    private static String getPolicyVersion(final AtomicInteger policyUpdateSequence) {
        return "version-" + policyUpdateSequence.intValue();
    }

}
