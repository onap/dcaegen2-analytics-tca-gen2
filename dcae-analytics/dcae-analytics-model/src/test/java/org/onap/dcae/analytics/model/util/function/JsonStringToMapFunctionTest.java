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

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonStringToMapFunctionTest {
    @Test
    public void applyTest() throws Exception {
        JsonStringToMapFunction jsonStringToMapFunction = new JsonStringToMapFunction();
        Map<String, Object> jObj = jsonStringToMapFunction.apply("{\"name\":\"test\",\"company\":\"test123\"}");
        assertEquals("test", jObj.get("name"));
    }

    @Test
    public void applyTestObject() throws Exception {
        JsonStringToMapFunction jsonStringToMapFunction = new JsonStringToMapFunction();
        Map<String, Object> jObj = jsonStringToMapFunction.
                apply("{\"name\":\"test\",\"company\":\"test123\",\"testObj\":{\"status\":\"true\"}}");
        assertEquals("test", jObj.get("name"));
    }

    @Test
    public void applyTestArray() throws Exception {
        JsonStringToMapFunction jsonStringToMapFunction = new JsonStringToMapFunction();
        Map<String, Object> jObj = jsonStringToMapFunction.
                apply("{\"name\":\"test\",\"company\":\"test123\",\"testObj\":[{\"status\":\"true\"}]}");
        assertEquals("test", jObj.get("name"));
    }

    @Test
    public void applyTest1() throws Exception {
        JsonStringToMapFunction jsonStringToMapFunction = new JsonStringToMapFunction("abc");
        Map<String, Object> jObj = jsonStringToMapFunction.apply("{\"name\":\"test\",\"company\":\"test123\"}");
        assertEquals("test", jObj.get("abc.name"));
    }

    @Test
    public void applyTest2() throws Exception {
        JsonStringToMapFunction jsonStringToMapFunction = new JsonStringToMapFunction("xyz", "|");
        Map<String, Object> jObj = jsonStringToMapFunction.apply("{\"name\":\"test\",\"company\":\"test123\"}");
        assertEquals("test123", jObj.get("xyz|company"));
    }

}
