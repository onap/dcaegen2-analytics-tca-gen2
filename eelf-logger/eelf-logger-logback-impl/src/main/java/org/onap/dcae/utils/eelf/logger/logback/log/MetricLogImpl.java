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

package org.onap.dcae.utils.eelf.logger.logback.log;

import org.onap.dcae.utils.eelf.logger.api.info.LogLevelCategory;
import org.onap.dcae.utils.eelf.logger.api.log.MetricLog;
import org.onap.dcae.utils.eelf.logger.api.spec.MetricLogSpec;
import org.onap.dcae.utils.eelf.logger.api.spec.OptionalLogSpec;
import org.onap.dcae.utils.eelf.logger.logback.utils.LogUtils;
import org.slf4j.Logger;

/**
 * Metric Log Logback Implementation
 *
 * @author Rajiv Singla
 */
public class MetricLogImpl implements MetricLog {

    private final Logger logger;
    private final Class<?> clazz;

    public MetricLogImpl(final Logger logger, final Class<?> clazz) {
        this.logger = logger;
        this.clazz = clazz;
    }


    @Override
    public void log(final LogLevelCategory logLevelCategory, final String message, final MetricLogSpec metricLogSpec,
                    final OptionalLogSpec optionalLogSpec, final String... args) {

        // if metric log spec or log level category is null throw an exception
        if (metricLogSpec == null || logLevelCategory == null) {
            throw new IllegalArgumentException("Metric Log Spec and Log level category must not be null");
        }

        // required fields
        LogUtils.CUSTOM_MDC_MAP.put(LogUtils.LOG_LEVEL_CATEGORY_KEY, logLevelCategory);
        LogUtils.CUSTOM_MDC_MAP.put(LogUtils.METRIC_LOG_SPEC_KEY, metricLogSpec);
        LogUtils.CUSTOM_MDC_MAP.put(LogUtils.LOGGER_CLASS_KEY, clazz);
        // optional fields
        LogUtils.CUSTOM_MDC_MAP.put(LogUtils.OPTIONAL_LOG_SPEC_KEY, optionalLogSpec);

        // log with normalized log level category
        LogUtils.logWithLogLevel(logLevelCategory, logger, LogUtils.METRIC_LOG_MARKER, message, args);

        // clean up
        LogUtils.CUSTOM_MDC_MAP.remove(LogUtils.LOG_LEVEL_CATEGORY_KEY);
        LogUtils.CUSTOM_MDC_MAP.remove(LogUtils.METRIC_LOG_SPEC_KEY);
        LogUtils.CUSTOM_MDC_MAP.remove(LogUtils.LOGGER_CLASS_KEY);
        LogUtils.CUSTOM_MDC_MAP.remove(LogUtils.OPTIONAL_LOG_SPEC_KEY);
    }

    @Override
    public void log(final LogLevelCategory logLevelCategory, final String message, final MetricLogSpec metricLogSpec,
                    final String... args) {
        log(logLevelCategory, message, metricLogSpec, null, args);
    }

    @Override
    public void info(final String message, final MetricLogSpec metricLogSpec, final OptionalLogSpec optionalLogSpec,
                     final String... args) {
        log(LogLevelCategory.INFO, message, metricLogSpec, optionalLogSpec, args);
    }

    @Override
    public void info(final String message, final MetricLogSpec metricLogSpec, final String... args) {
        log(LogLevelCategory.INFO, message, metricLogSpec, args);
    }

    @Override
    public void warn(final String message, final MetricLogSpec metricLogSpec, final OptionalLogSpec optionalLogSpec,
                     final String... args) {
        log(LogLevelCategory.WARN, message, metricLogSpec, optionalLogSpec, args);
    }

    @Override
    public void warn(final String message, final MetricLogSpec metricLogSpec, final String... args) {
        warn(message, metricLogSpec, null, args);
    }

    @Override
    public void error(final String message, final MetricLogSpec metricLogSpec, final OptionalLogSpec optionalLogSpec,
                      final String... args) {
        log(LogLevelCategory.ERROR, message, metricLogSpec, optionalLogSpec, args);
    }

    @Override
    public void error(final String message, final MetricLogSpec metricLogSpec, final String... args) {
        error(message, metricLogSpec, null, args);
    }

    @Override
    public void fatal(final String message, final MetricLogSpec metricLogSpec, final OptionalLogSpec optionalLogSpec,
                      final String... args) {
        log(LogLevelCategory.FATAL, message, metricLogSpec, optionalLogSpec, args);
    }

    @Override
    public void fatal(final String message, final MetricLogSpec metricLogSpec, final String... args) {
        fatal(message, metricLogSpec, null, args);
    }
}
