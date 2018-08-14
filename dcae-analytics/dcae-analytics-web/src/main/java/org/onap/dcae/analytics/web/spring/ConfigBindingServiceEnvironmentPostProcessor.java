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
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.util.function.JsonStringToMapFunction;
import org.onap.dcae.analytics.model.util.supplier.ConfigBindingServiceJsonSupplier;
import org.onap.dcae.analytics.web.exception.AnalyticsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * A custom spring framework environment post processor which can fetch and populate spring context with
 * Config Binding Service application properties.
 * <p>
 * Activated only when config binding service profile is active.
 *
 * @author Rajiv Singla
 */
public class ConfigBindingServiceEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigBindingServiceEnvironmentPostProcessor.class);
    private static final String SERVLET_ENVIRONMENT_CLASS =
            "org.springframework.web.context.support.StandardServletEnvironment";

    private static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE;

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {

        final boolean isConfigServiceProfilePresent = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(p -> p.equalsIgnoreCase(AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME));

        if (!isConfigServiceProfilePresent) {
            LOGGER.info("Config Binding Service Profile is not active. " +
                    "Skipping Adding config binding service properties");
            return;
        }

        LOGGER.info("Config Binding Service Profile is active. " +
                "Application properties will be fetched from config binding service");

        // Fetch config binding service json
        final Optional<String> configServiceJsonOptional = new ConfigBindingServiceJsonSupplier().get();

        if (!configServiceJsonOptional.isPresent()) {
            final String errorMessage = "Unable to get fetch application configuration from config binding service";
            throw new AnalyticsValidationException(errorMessage, new IllegalStateException(errorMessage));
        }

        final String configServicePropertiesKey = ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_PROPERTIES_KEY;

        // convert fetch config binding service json string to Map of property key and values
        final Map<String, Object> configPropertiesMap = configServiceJsonOptional
                .map(new JsonStringToMapFunction(configServicePropertiesKey))
                .orElse(Collections.emptyMap());

        if (configPropertiesMap.isEmpty()) {

            LOGGER.warn("No properties found in config binding service");

        } else {

            // remove config service key prefix on spring reserved property key prefixes
            final Set<String> springKeyPrefixes = ConfigBindingServiceConstants.SPRING_RESERVED_PROPERTIES_KEY_PREFIXES;
            final Set<String> springKeys = springKeyPrefixes.stream()
                    .map(springKeyPrefix -> configServicePropertiesKey + "." + springKeyPrefix)
                    .collect(Collectors.toSet());

            final Map<String, Object> filterKeyMap = configPropertiesMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            (Map.Entry<String, Object> e) ->
                                    springKeys.stream().anyMatch(springKey -> e.getKey().startsWith(springKey)) ?
                                            e.getKey().substring(configServicePropertiesKey.toCharArray().length + 1) :
                                            e.getKey(),
                            Map.Entry::getValue)
                    );

            filterKeyMap.forEach((key, value) ->
                    LOGGER.info("Adding property from config service in spring context: {} -> {}", key, value));

            addJsonPropertySource(environment, new MapPropertySource(configServicePropertiesKey, filterKeyMap));
        }

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    private void addJsonPropertySource(final ConfigurableEnvironment environment, final PropertySource<?> source) {
        final MutablePropertySources sources = environment.getPropertySources();
        final String name = findPropertySource(sources);
        if (sources.contains(name)) {
            sources.addBefore(name, source);
        } else {
            sources.addFirst(source);
        }
    }

    private String findPropertySource(final MutablePropertySources sources) {
        if (ClassUtils.isPresent(SERVLET_ENVIRONMENT_CLASS, null) &&
                sources.contains(StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME)) {
            return StandardServletEnvironment.JNDI_PROPERTY_SOURCE_NAME;

        }
        return StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;
    }

}
