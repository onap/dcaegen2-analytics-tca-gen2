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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreationTimestampSupplierTest {
    @Test
    public void getTest () throws Exception {
        CreationTimestampSupplier creationTimestampSupplier =
                new CreationTimestampSupplier();
        String str = creationTimestampSupplier.get();
        assertNotNull(str);
    }

    @Test
    public void getParsedDateTest () throws Exception {
        Date date = CreationTimestampSupplier.getParsedDate("2020-08-24T21:49:31.702+0400");
        assertNotNull(date.getTime());
    }
}
