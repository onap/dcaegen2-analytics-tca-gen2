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

package org.onap.dcae.analytics.tca.model.util.json.mixin.policy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author Rajiv Singla
 */
public abstract class ThresholdMixin extends BaseTcaPolicyModelMixin {

    @JsonIgnore
    private BigDecimal actualFieldValue;

    @JsonIgnore
    public BigDecimal getActualFieldValue() {
        return actualFieldValue;
    }

    @JsonProperty
    public void setActualFieldValue(BigDecimal actualFieldValue) {
        this.actualFieldValue = actualFieldValue;
    }
}
