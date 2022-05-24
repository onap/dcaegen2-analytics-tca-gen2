/*
 * ================================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * Copyright Copyright (c) 2019 IBM
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
package org.onap.dcae.analytics.web.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GenericValidationResponseTest {

    public static GenericValidationResponse genericValidationResponse;

    @BeforeAll
    static void beforeAll() {
        genericValidationResponse = new GenericValidationResponse();
    }

    @Test
    public void testGenericValidationResponse() {
        boolean hasError = genericValidationResponse.hasErrors();
        Set<String> set = new HashSet<>();
        Map<String, String> map = new HashMap<>();
        String errorMsg = "";
        Assertions.assertEquals(false, hasError);
        Assertions.assertEquals(set, genericValidationResponse.getFieldNamesWithError());
        Assertions.assertNotNull(genericValidationResponse.getErrorMessages());
        Assertions.assertEquals(map, genericValidationResponse.getValidationResultsAsMap());
        Assertions.assertEquals(errorMsg, genericValidationResponse.getAllErrorMessage());
        Assertions.assertEquals(errorMsg, genericValidationResponse.getAllErrorMessage("testMsg"));
        genericValidationResponse.addErrorMessage("test", "test message");
        Assertions.assertEquals("test message", genericValidationResponse.getAllErrorMessage("test"));
    }
}
