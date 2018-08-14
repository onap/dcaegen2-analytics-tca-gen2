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

package org.onap.dcae.analytics.model.util.function;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Function that calls http 'GET' on given URL and returns the response as String if successful. This function
 * has side effects and only used for convenience in lambda expressions
 * <p>
 * NOTE: Suitable for only light weight http get requests as this will be a blocking call
 *
 * @author Rajiv Singla
 */
public class URLToHttpGetFunction implements Function<URL, Optional<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLToHttpGetFunction.class);

    @Override
    public Optional<String> apply(final URL url) {

        try {
            final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");

            LOGGER.info("Sending 'GET' request to URL : {}", url);
            final int responseCode = httpConnection.getResponseCode();

            if (responseCode == 200) {
                final StringBuilder responseString = new StringBuilder();
                try (Scanner scanner = new Scanner(httpConnection.getInputStream(), StandardCharsets.UTF_8.name())) {
                    while (scanner.hasNext()) {
                        responseString.append(scanner.next());
                    }
                }

                LOGGER.info("Successful Response: {}", responseString);
                return Optional.of(responseString.toString());
            }

            LOGGER.error("Unsuccessful Response Code: {} when calling URL: {}", responseCode, url);

        } catch (IOException e) {

            LOGGER.error("Unable to create HTTP URL Connection to URL:" + url, e);

        }

        return Optional.empty();
    }
}
