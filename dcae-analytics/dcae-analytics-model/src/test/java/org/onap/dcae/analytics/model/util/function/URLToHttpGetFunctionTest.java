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
import org.mockito.Mockito;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class URLToHttpGetFunctionTest {
    @Test
    public void apply () throws Exception {
        URL url = new URL("http://localhost:8080");
        URLToHttpGetFunction urlToHttpGetFunction = new URLToHttpGetFunction();
        HttpURLConnection httpURLConnection = Mockito.mock(HttpURLConnection.class);
        Mockito.when(httpURLConnection.getResponseCode()).thenReturn(200);
        Optional<String> optional =  urlToHttpGetFunction.apply(url);
        assertTrue(optional.isEmpty());
    }
}
