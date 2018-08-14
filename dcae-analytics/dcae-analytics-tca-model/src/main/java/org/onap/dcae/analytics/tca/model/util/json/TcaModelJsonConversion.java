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

package org.onap.dcae.analytics.tca.model.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.function.Function;

import org.onap.dcae.analytics.model.util.function.JsonToJavaObjectBiFunction;
import org.onap.dcae.analytics.tca.model.facade.TcaAlert;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;

/**
 * @author Rajiv Singla
 */
public abstract class TcaModelJsonConversion {


    public static final ObjectMapper TCA_OBJECT_MAPPER = new TcaObjectMapperSupplier().get();

    // Type reference to convert tca policy string to tca policy object
    private static final TypeReference<TcaPolicy> TCA_POLICY_TYPE_REF = new TypeReference<TcaPolicy>() {
    };

    // Type reference to convert tca alert string to tca alert
    private static final TypeReference<TcaAlert> TCA_ALERT_TYPE_REF = new TypeReference<TcaAlert>() {
    };

    // Tca Policy JSON conversion function
    public static final Function<String, Optional<TcaPolicy>> TCA_POLICY_JSON_FUNCTION = new
            JsonToJavaObjectBiFunction<TcaPolicy>(TCA_OBJECT_MAPPER).curry(TCA_POLICY_TYPE_REF);

    // Tca Alert JSON conversion function
    public static final Function<String, Optional<TcaAlert>> TCA_ALERT_JSON_FUNCTION = new
            JsonToJavaObjectBiFunction<TcaAlert>(TCA_OBJECT_MAPPER).curry(TCA_ALERT_TYPE_REF);

    private TcaModelJsonConversion() {
        // private constructor
    }

}
