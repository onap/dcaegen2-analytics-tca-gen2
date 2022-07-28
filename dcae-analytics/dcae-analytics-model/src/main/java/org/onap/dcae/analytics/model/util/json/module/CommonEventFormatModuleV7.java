/*
 * ============LICENSE_START=======================================================
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

package org.onap.dcae.analytics.model.util.json.module;

import static org.onap.dcae.analytics.model.AnalyticsModelConstants.JSON_MODULE_ARTIFACT_ID;
import static org.onap.dcae.analytics.model.AnalyticsModelConstants.JSON_MODULE_GROUP_ID;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.onap.dcae.analytics.model.cef.AlertAction;
import org.onap.dcae.analytics.model.cef.AlertType;
import org.onap.dcae.analytics.model.cef.BaseCEFModel;
import org.onap.dcae.analytics.model.cef.CommonEventHeaderV7;
import org.onap.dcae.analytics.model.cef.Criticality;
import org.onap.dcae.analytics.model.cef.Domain;
import org.onap.dcae.analytics.model.cef.EventV7;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.model.cef.EventSeverity;
import org.onap.dcae.analytics.model.cef.Field;
import org.onap.dcae.analytics.model.cef.InternalHeaderFields;
import org.onap.dcae.analytics.model.cef.MeasurementFields;
import org.onap.dcae.analytics.model.cef.NamedArrayOfFields;
import org.onap.dcae.analytics.model.cef.NicPerformance;
import org.onap.dcae.analytics.model.cef.PerformanceCounter;
import org.onap.dcae.analytics.model.cef.Priority;
import org.onap.dcae.analytics.model.cef.ThresholdCrossingAlertFields;
import org.onap.dcae.analytics.model.util.json.mixin.cef.AlertActionMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.AlertTypeMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.BaseCEFModelMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.CommonEventHeaderV7Mixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.CriticalityMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.DomainMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.EventListenerMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.EventV7Mixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.EventSeverityMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.FieldMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.InternalHeaderFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.MeasurementFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.NamedArrayOfFieldsMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.nicUsageArrayMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.PerformanceCounterMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.PriorityMixin;
import org.onap.dcae.analytics.model.util.json.mixin.cef.ThresholdCrossingAlertFieldsMixin;



/**
 * @author Rajiv Singla
 */
public class CommonEventFormatModuleV7 extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public CommonEventFormatModuleV7() {
        super("Common Event Format",
                new Version(30, 2, 1, null, JSON_MODULE_GROUP_ID, JSON_MODULE_ARTIFACT_ID));
    }

    @Override
    public void setupModule(final SetupContext setupContext) {

        setupContext.setMixInAnnotations(AlertAction.class, AlertActionMixin.class);
        setupContext.setMixInAnnotations(AlertType.class, AlertTypeMixin.class);
        setupContext.setMixInAnnotations(BaseCEFModel.class, BaseCEFModelMixin.class);
        setupContext.setMixInAnnotations(CommonEventHeaderV7.class, CommonEventHeaderV7Mixin.class);
        setupContext.setMixInAnnotations(Domain.class, DomainMixin.class);
        setupContext.setMixInAnnotations(InternalHeaderFields.class, InternalHeaderFieldsMixin.class);
        setupContext.setMixInAnnotations(Field.class, FieldMixin.class);
        setupContext.setMixInAnnotations(NamedArrayOfFields.class, NamedArrayOfFieldsMixin.class);
        setupContext.setMixInAnnotations(Criticality.class, CriticalityMixin.class);
        setupContext.setMixInAnnotations(EventListener.class, EventListenerMixin.class);
        setupContext.setMixInAnnotations(EventV7.class, EventV7Mixin.class);
        setupContext.setMixInAnnotations(EventSeverity.class, EventSeverityMixin.class);
        setupContext.setMixInAnnotations(MeasurementFields.class,
          MeasurementFieldsMixin.class);
        setupContext.setMixInAnnotations(PerformanceCounter.class, PerformanceCounterMixin.class);
        setupContext.setMixInAnnotations(Priority.class, PriorityMixin.class);
        setupContext.setMixInAnnotations(ThresholdCrossingAlertFields.class, ThresholdCrossingAlertFieldsMixin.class);
        setupContext.setMixInAnnotations(NicPerformance.class, nicUsageArrayMixin.class);

    }

}
