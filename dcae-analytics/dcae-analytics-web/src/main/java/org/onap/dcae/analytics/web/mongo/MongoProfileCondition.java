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

package org.onap.dcae.analytics.web.mongo;

import java.util.Arrays;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Condition that configures mongo support only if mongo profile is present
 *
 * @author Rajiv Singla
 */
public class MongoProfileCondition implements Condition {

    private static final Logger logger = LoggerFactory.getLogger(MongoProfileCondition.class);

    @Override
    public boolean matches(final ConditionContext context, final AnnotatedTypeMetadata metadata) {

        final boolean isMongoProfileActive = Arrays.stream(context.getEnvironment().getActiveProfiles())
                .anyMatch(activeProfile -> activeProfile.equalsIgnoreCase(AnalyticsProfile.MONGO_PROFILE_NAME));

        if (isMongoProfileActive) {
            logger.info("Mongo Profile is Active. Mongo support is enabled");
            return true;
        }

        logger.info("Mongo Profile is NOT Active. Mongo support is disabled");
        return false;
    }
}
