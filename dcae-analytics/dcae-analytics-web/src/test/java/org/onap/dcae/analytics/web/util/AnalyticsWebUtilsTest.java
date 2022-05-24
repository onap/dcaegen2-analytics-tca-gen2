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


package org.onap.dcae.analytics.web.util;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnalyticsWebUtilsTest {

    @Test
    public void AnalyticsWebUtilsTst () throws Exception {
        Supplier<String> creationTimestampSupplier = AnalyticsWebUtils.CREATION_TIMESTAMP_SUPPLIER;
        Supplier<String> randomIdSupplier = AnalyticsWebUtils.RANDOM_ID_SUPPLIER;
        Supplier<String> requestIdSupplier = AnalyticsWebUtils.REQUEST_ID_SUPPLIER;
        assertNotNull(creationTimestampSupplier);
        assertNotNull(randomIdSupplier.toString());
        assertNotNull(requestIdSupplier);
    }
}
