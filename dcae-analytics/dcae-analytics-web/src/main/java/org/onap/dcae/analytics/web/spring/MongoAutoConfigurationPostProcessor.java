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

package org.onap.dcae.analytics.web.spring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * Disables mongo auto configuration if {@link AnalyticsProfile#MONGO_PROFILE_NAME} is not present
 *
 * @author Rajiv Singla
 */
public class MongoAutoConfigurationPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(MongoAutoConfigurationPostProcessor.class);

    private static final String PROPERTY_SOURCE_NAME = "defaultProperties";
    private static final String SPRING_AUTO_CONFIG_EXCLUDE_PROPERTY_KEY = "spring.autoconfigure.exclude";
    private static final List<String> MONGO_AUTO_CONFIG_PROPERTIES = Arrays.asList(
            "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration");

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {

        final boolean isMongoProfileActive = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase(AnalyticsProfile.MONGO_PROFILE_NAME));

        // if mongo profile is not active disable mongo auto configuration
        if (!isMongoProfileActive) {
            logger.info("Mongo Profile is not active - disabling Mongo Auto Configuration");
            final Map<String, Object> mongoExcludePropsMap = new HashMap<>();
            mongoExcludePropsMap.put(SPRING_AUTO_CONFIG_EXCLUDE_PROPERTY_KEY, MONGO_AUTO_CONFIG_PROPERTIES);
            addMongoPropertiesIfAbsent(environment.getPropertySources(), mongoExcludePropsMap);
        }
    }

    private void addMongoPropertiesIfAbsent(final MutablePropertySources propertySources,
                                            final Map<String, Object> mongoPropertiesMap) {
        MapPropertySource target = null;
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            PropertySource<?> source = propertySources.get(PROPERTY_SOURCE_NAME);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                for (final Map.Entry<String, Object> entry : mongoPropertiesMap.entrySet()) {
                    if (!target.containsProperty(entry.getKey())) {
                        target.getSource().putIfAbsent(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        if (target == null) {
            target = new MapPropertySource(PROPERTY_SOURCE_NAME, mongoPropertiesMap);
        }
        if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
            propertySources.addLast(target);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
