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

import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringToURLFunctionTest {
    @Test
    public void apply () throws Exception {
        StringToURLFunction stringToURLFunction = new StringToURLFunction ();
        Optional<URL> optionalURL = stringToURLFunction.apply("https://localhost:8080/test");
        assertTrue(optionalURL.isPresent());
    }

    @Test
    public void applyException () throws Exception {
        StringToURLFunction stringToURLFunction = new StringToURLFunction ();
        Optional<URL> optionalURL = stringToURLFunction.apply("");
        assertTrue(optionalURL.isEmpty());
    }
}
