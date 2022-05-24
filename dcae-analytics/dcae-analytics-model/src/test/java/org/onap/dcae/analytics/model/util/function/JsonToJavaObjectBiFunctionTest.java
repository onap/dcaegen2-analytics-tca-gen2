/*
 * ============LICENSE_START=======================================================
 * Copyright (c) 2022 Huawei. All rights reserved.
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
package org.onap.dcae.analytics.model.util.function;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonToJavaObjectBiFunctionTest {
    @Test
    public void apply () throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonToJavaObjectBiFunction jsonToJavaObjectBiFunction = new JsonToJavaObjectBiFunction(objectMapper);
        TypeReference<Object> stringTypeRef = new TypeReference<Object>(){};
        Optional<Object> optional = jsonToJavaObjectBiFunction.apply(stringTypeRef, "{\"name\":\"test\",\"company\":\"test123\"}");
        assertTrue(optional.isPresent());
    }

    @Test
    public void applyError () throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonToJavaObjectBiFunction jsonToJavaObjectBiFunction = new JsonToJavaObjectBiFunction(objectMapper);
        TypeReference<String> stringTypeRef = new TypeReference<String>(){};
        Optional<Object> optional = jsonToJavaObjectBiFunction.apply(stringTypeRef, "{\"name\":\"test\",\"company\":\"test123\"}");
        assertTrue(optional.isEmpty());
    }
}
