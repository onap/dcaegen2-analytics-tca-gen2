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

package org.onap.dcae.analytics.tca.model.util.json.module;

import com.fasterxml.jackson.databind.module.SimpleModule;

import org.onap.dcae.analytics.tca.model.policy.BaseTcaPolicyModel;
import org.onap.dcae.analytics.tca.model.policy.ClosedLoopEventStatus;
import org.onap.dcae.analytics.tca.model.policy.ControlLoopSchemaType;
import org.onap.dcae.analytics.tca.model.policy.Direction;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.policy.Threshold;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.BaseTcaPolicyModelMixin;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.ClosedLoopEventStatusMixin;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.ControlLoopSchemaTypeMixin;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.DirectionMixin;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.MetricsPerEventNameMixin;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.TcaPolicyMixin;
import org.onap.dcae.analytics.tca.model.util.json.mixin.policy.ThresholdMixin;


/**
 * @author Rajiv Singla
 */
public class TcaPolicyModule extends SimpleModule {

    private static final long serialVersionUID = 1L;


    @Override
    public void setupModule(final SetupContext setupContext) {
        setupContext.setMixInAnnotations(BaseTcaPolicyModel.class, BaseTcaPolicyModelMixin.class);
        setupContext.setMixInAnnotations(TcaPolicy.class, TcaPolicyMixin.class);
        setupContext.setMixInAnnotations(ControlLoopSchemaType.class, ControlLoopSchemaTypeMixin.class);
        setupContext.setMixInAnnotations(ClosedLoopEventStatus.class, ClosedLoopEventStatusMixin.class);
        setupContext.setMixInAnnotations(MetricsPerEventName.class, MetricsPerEventNameMixin.class);
        setupContext.setMixInAnnotations(Direction.class, DirectionMixin.class);
        setupContext.setMixInAnnotations(Threshold.class, ThresholdMixin.class);
        setupContext.setMixInAnnotations(TcaPolicy.class, TcaPolicyMixin.class);
    }
}
