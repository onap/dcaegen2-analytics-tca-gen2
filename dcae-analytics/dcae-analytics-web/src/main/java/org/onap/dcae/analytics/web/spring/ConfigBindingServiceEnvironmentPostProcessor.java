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

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.model.configbindingservice.ConfigBindingServiceConstants;
import org.onap.dcae.analytics.model.util.function.JsonStringToMapFunction;
import org.onap.dcae.analytics.web.config.SystemConfig;
import org.onap.dcae.analytics.web.exception.AnalyticsValidationException;
import org.onap.dcae.analytics.web.exception.EnvironmentLoaderException;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsClient;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsClientFactory;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.api.CbsRequests;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.CbsRequest;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.EnvProperties;
import org.onap.dcaegen2.services.sdk.rest.services.cbs.client.model.ImmutableEnvProperties;
import org.onap.dcaegen2.services.sdk.rest.services.model.logging.RequestDiagnosticContext;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A custom spring framework environment post processor which can fetch and populate spring context with
 * Config Binding Service application properties.
 * <p>
 * Activated only when config binding service profile is active.
 *
 * @author Rajiv Singla
 */
public class ConfigBindingServiceEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(ConfigBindingServiceEnvironmentPostProcessor.class);
    private static final String SERVLET_ENVIRONMENT_CLASS =
            "org.springframework.web.context.support.StandardServletEnvironment";

    private static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE;

    private Disposable refreshConfigTask = null;

    private  ConfigurableEnvironment env = null;

	private Map<String, Object> filterKeyMap = null;

	private String configServicePropertiesKey =
	       ConfigBindingServiceConstants.CONFIG_BINDING_SERVICE_PROPERTIES_KEY;

	@Override
	public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {

        final boolean isConfigServiceProfilePresent = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(p -> p.equalsIgnoreCase(AnalyticsProfile.CONFIG_BINDING_SERVICE_PROFILE_NAME));

        if (!isConfigServiceProfilePresent) {
            logger.info("Config Binding Service Profile is not active. " +
                    "Skipping Adding config binding service properties");
            return;
        }

        logger.info("Config Binding Service Profile is active. " +
                "Application properties will be fetched from config binding service");

        env = environment;
        initialize();

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    public synchronized void addJsonPropertySource(final ConfigurableEnvironment environment, final PropertySource<?> source) {
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

    /**
    *
    * Fetch the configuration.
    *
    */
   public void initialize() {
   	stop();

       refreshConfigTask = createRefreshTask() //
           .subscribe(e -> logger.info("Refreshed configuration data"),
               throwable -> logger.error("Configuration refresh terminated due to exception", throwable),
               () -> logger.error("Configuration refresh terminated"));
   }

   /**
    * Fetch the configuration task from CBS.
    *
    */
   private Flux<String> createRefreshTask() {
       return readEnvironmentVariables() //
           .flatMap(this::createCbsClient) //
           .flatMapMany(this::periodicConfigurationUpdates) //
           .map(this::parseTcaConfig) //
           .onErrorResume(this::onErrorResume);
   }

   /**
    * periodicConfigurationUpdates.
    *
    * @param cbsClient cbsClient
    * @return configuration refreshed
    *
    */
   public Flux<JsonObject> periodicConfigurationUpdates(CbsClient cbsClient) {
       final Duration initialDelay = Duration.ZERO;
       final Duration refreshPeriod = Duration.ofMinutes(ConfigBindingServiceConstants.CONFIG_SERVICE_REFRESHPERIOD);
       final CbsRequest getConfigRequest = CbsRequests.getAll(RequestDiagnosticContext.create());
       return cbsClient.updates(getConfigRequest, initialDelay, refreshPeriod);
   }

	/**
	 *
	 * get environment variables.
	 *
	 * @return environment properties.
	 *
	 */
	public Mono<EnvProperties> readEnvironmentVariables() {
		logger.trace("Loading configuration from system environment variables");
		EnvProperties envProperties;
		try {
			envProperties = ImmutableEnvProperties.builder() //
					.consulHost(SystemConfig.getConsulHost()) //
					.consulPort(SystemConfig.getConsultPort()) //
					.cbsName(SystemConfig.getConfigBindingService()) //
					.appName(SystemConfig.getService()) //
					.build();
		} catch (EnvironmentLoaderException e) {
			return Mono.error(e);
		}
		logger.trace("Evaluated environment system variables {}", envProperties);
		return Mono.just(envProperties);
	}

   /**
    * Stops the refreshing of the configuration.
    *
    */
   public void stop() {
       if (refreshConfigTask != null) {
           refreshConfigTask.dispose();
           refreshConfigTask = null;
       }
   }

   /**
    * periodicConfigurationUpdates.
    *
    * @param throwable throwable
    * @return Mono
    *
    */
   private <R> Mono<R> onErrorResume(Throwable throwable) {
       logger.error("Could not refresh application configuration {}", throwable.toString());
       return Mono.empty();
   }


   /**
    * create CbsClient.
    *
    * @param env environment properties
    * @return cbsclient
    *
    */
   public Mono<CbsClient> createCbsClient(EnvProperties env) {
       return CbsClientFactory.createCbsClient(env);
   }

   /**
    * Parse configuration.
    *
    * @param jsonObject the TCA service's configuration
    * @return this
    *
    */
   public String parseTcaConfig(JsonObject jsonObject) {

	   JsonElement jsonConfig = jsonObject.get(ConfigBindingServiceConstants.CONFIG);

		Optional<String> configServiceJsonOptional = Optional.of(jsonConfig.toString());
		if (!configServiceJsonOptional.isPresent()) {
			final String errorMessage = "Unable to get fetch application configuration from config binding service";
			throw new AnalyticsValidationException(errorMessage, new IllegalStateException(errorMessage));
		}

		// convert fetch config binding service json string to Map of property key and
		// values
		Map<String, Object> configPropertiesMap = configServiceJsonOptional
				.map(new JsonStringToMapFunction(configServicePropertiesKey)).orElse(Collections.emptyMap());

		if (configPropertiesMap.isEmpty()) {

			logger.warn("No properties found in config binding service");

		} else {

			// remove config service key prefix on spring reserved property key prefixes
			final Set<String> springKeyPrefixes = ConfigBindingServiceConstants.SPRING_RESERVED_PROPERTIES_KEY_PREFIXES;
			final Set<String> springKeys = springKeyPrefixes.stream()
					.map(springKeyPrefix -> configServicePropertiesKey + "." + springKeyPrefix)
					.collect(Collectors.toSet());

			filterKeyMap = configPropertiesMap.entrySet().stream()
					.collect(Collectors.toMap((Map.Entry<String, Object> e) -> springKeys.stream()
							.anyMatch(springKey -> e.getKey().startsWith(springKey))
									? e.getKey().substring(configServicePropertiesKey.toCharArray().length + 1)
									: e.getKey(),
							Map.Entry::getValue));

			filterKeyMap.forEach((key, value) -> logger
					.info("Adding property from config service in spring context: {} -> {}", key, value));

			addJsonPropertySource(env, new MapPropertySource(configServicePropertiesKey, filterKeyMap));

		}
		return configServiceJsonOptional.get();
	}

}
