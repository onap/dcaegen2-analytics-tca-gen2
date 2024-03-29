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

package org.onap.dcae.analytics.web.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.integration.store.BasicMessageGroupStore;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MessageStoreTestConfig {
    @Test
    public void simpleMessageGroupStoreTest () {
        MessageStoreConfig messageStoreConfig = new MessageStoreConfig();
        BasicMessageGroupStore basicMessageGroupStore = messageStoreConfig.simpleMessageGroupStore();
        assertNotNull(basicMessageGroupStore);
    }

    @Test
    public void mongoMessageGroupStoreTest () {
        MessageStoreConfig messageStoreConfig = new MessageStoreConfig();
        MongoDatabaseFactory mongoDatabaseFactory = Mockito.mock(MongoDatabaseFactory.class);
        BasicMessageGroupStore basicMessageGroupStore = messageStoreConfig.mongoMessageGroupStore(mongoDatabaseFactory);
        assertNotNull(basicMessageGroupStore);
    }
}
