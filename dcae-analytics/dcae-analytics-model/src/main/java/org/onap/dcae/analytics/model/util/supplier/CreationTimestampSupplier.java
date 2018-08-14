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

package org.onap.dcae.analytics.model.util.supplier;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Supplier;

import org.onap.dcae.analytics.model.AnalyticsModelConstants;

/**
 * Provides current date/time in UTC formatted string
 *
 * @author Rajiv Singla
 */
public class CreationTimestampSupplier implements Supplier<String> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(AnalyticsModelConstants.ANALYTICS_DATE_FORMAT);

    @Override
    public String get() {
        return DATE_TIME_FORMATTER.format(ZonedDateTime.now(ZoneOffset.UTC));
    }


    public static Date getParsedDate(final String dateString) {
        return Date.from(ZonedDateTime.parse(dateString, DATE_TIME_FORMATTER).toInstant());
    }
}
