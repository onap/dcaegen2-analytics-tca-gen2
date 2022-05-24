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
package org.onap.dcae.analytics.model.util.supplier;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnboundedSupplierTest {
    @Test
    public void getTest () throws Exception {
        Map<String, String> str = new HashMap();
        str.put("name", "test");
        Map<String, String> str2 = new HashMap();
        str2.put("name1", "test1");
        UnboundedSupplier unboundedSupplier =
                new UnboundedSupplier(UnboundedSupplier.GenerationMode.ROUND_ROBIN, str, str2);
        String s = unboundedSupplier.get().toString();
        assertTrue(unboundedSupplier.get().toString().contains("test"));
    }

    @Test
    public void getRandomTest () throws Exception {
        Map<String, String> str = new HashMap();
        str.put("name", "test");
        Map<String, String> str2 = new HashMap();
        str2.put("name1", "test1");
        UnboundedSupplier unboundedSupplier =
                new UnboundedSupplier(UnboundedSupplier.GenerationMode.RANDOM, str, str2);
        String s = unboundedSupplier.get().toString();
        assertTrue(unboundedSupplier.get().toString().contains("test"));
    }

    @Test
    public void getTest1 () throws Exception {
        Map<String, String> str = new HashMap();
        str.put("name", "test");
        UnboundedSupplier unboundedSupplier =
                new UnboundedSupplier(str);
        assertTrue(unboundedSupplier.get().toString().contains("test"));
    }
}
