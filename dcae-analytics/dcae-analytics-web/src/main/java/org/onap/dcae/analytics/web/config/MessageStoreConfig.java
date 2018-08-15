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

package org.onap.dcae.analytics.web.config;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.integration.mongodb.store.MongoDbChannelMessageStore;
import org.springframework.integration.store.BasicMessageGroupStore;
import org.springframework.integration.store.SimpleMessageStore;

/**
 * @author Rajiv Singla
 */
@Configuration
@Profile(AnalyticsProfile.DMAAP_PROFILE_NAME)
public class MessageStoreConfig {

    @Bean
    @Profile(AnalyticsProfile.NOT_MONGO_PROFILE_NAME)
    public BasicMessageGroupStore simpleMessageGroupStore() {
        return new SimpleMessageStore();
    }

    @Bean
    @Profile(AnalyticsProfile.MONGO_PROFILE_NAME)
    public BasicMessageGroupStore mongoMessageGroupStore(final MongoDbFactory mongoDbFactory) {
        final MongoDbChannelMessageStore store = new MongoDbChannelMessageStore(mongoDbFactory);
        store.setPriorityEnabled(true);
        return store;
    }

}
