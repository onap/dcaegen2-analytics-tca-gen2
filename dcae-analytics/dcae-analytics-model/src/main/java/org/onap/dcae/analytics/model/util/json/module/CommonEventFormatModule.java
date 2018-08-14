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

package org.onap.dcae.analytics.model.util.json.module;

import static org.onap.dcae.analytics.model.AnalyticsModelConstants.JSON_MODULE_ARTIFACT_ID;
import static org.onap.dcae.analytics.model.AnalyticsModelConstants.JSON_MODULE_GROUP_ID;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.onap.dcae.analytics.model.cef.AlertAction;
import org.onap.dcae.analytics.model.cef.AlertType;
import org.onap.dcae.analytics.model.cef.BaseCEFModel;
import org.onap.dcae.analytics.model.cef.CommonEventHeader;
import org.onap.dcae.analytics.model.cef.Criticality;
import org.onap.dcae.analytics.model.cef.Domain;
import org.onap.dcae.analytics.model.cef.Event;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.model.cef.EventSeverity;
import org.onap.dcae.analytics.model.cef.Field;
import org.onap.dcae.analytics.model.cef.InternalHeaderFields;
import org.onap.dcae.analytics.model.cef.MeasurementsForVfScalingFields;
import org.onap.dcae.analytics.model.cef.NamedArrayOfFields;
import org.onap.dcae.analytics.model.cef.PerformanceCounter;
import org.onap.dcae.analytics.model.cef.Priority;
import org.onap.dcae.analytics.model.cef.ThresholdCrossingAlertFields;
import org.onap.dcae.analytics.model.cef.VNicPerformance;
import org.onap.dcae.analytics.model.util.json.mixin.cef.AlertActionMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.AlertTypeMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.BaseCEFModelMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.CommonEventHeaderMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.CriticalityMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.DomainMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.EventListenerMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.EventMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.EventSeverityMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.FieldMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.InternalHeaderFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.MeasurementsForVfScalingFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.NamedArrayOfFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.PerformanceCounterMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.PriorityMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.ThresholdCrossingAlertFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.VNicUsageArrayMixin;


/**
 * @author Rajiv Singla
 */
public class CommonEventFormatModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public CommonEventFormatModule() {
        super("Common Event Format",
                new Version(28, 3, 0, null, JSON_MODULE_GROUP_ID, JSON_MODULE_ARTIFACT_ID));
    }

    @Override
    public void setupModule(final SetupContext setupContext) {

        setupContext.setMixInAnnotations(AlertAction.class, AlertActionMixin.class);
        setupContext.setMixInAnnotations(AlertType.class, AlertTypeMixin.class);
        setupContext.setMixInAnnotations(BaseCEFModel.class, BaseCEFModelMixin.class);
        setupContext.setMixInAnnotations(CommonEventHeader.class, CommonEventHeaderMixin.class);
        setupContext.setMixInAnnotations(Domain.class, DomainMixin.class);
        setupContext.setMixInAnnotations(InternalHeaderFields.class, InternalHeaderFieldsMixin.class);
        setupContext.setMixInAnnotations(Field.class, FieldMixin.class);
        setupContext.setMixInAnnotations(NamedArrayOfFields.class, NamedArrayOfFieldsMixin.class);
        setupContext.setMixInAnnotations(Criticality.class, CriticalityMixin.class);
        setupContext.setMixInAnnotations(EventListener.class, EventListenerMixin.class);
        setupContext.setMixInAnnotations(Event.class, EventMixin.class);
        setupContext.setMixInAnnotations(EventSeverity.class, EventSeverityMixin.class);
        setupContext.setMixInAnnotations(MeasurementsForVfScalingFields.class,
                MeasurementsForVfScalingFieldsMixin.class);
        setupContext.setMixInAnnotations(PerformanceCounter.class, PerformanceCounterMixin.class);
        setupContext.setMixInAnnotations(Priority.class, PriorityMixin.class);
        setupContext.setMixInAnnotations(ThresholdCrossingAlertFields.class, ThresholdCrossingAlertFieldsMixin.class);
        setupContext.setMixInAnnotations(VNicPerformance.class, VNicUsageArrayMixin.class);

    }

}
