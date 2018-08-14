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

import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Supplies random ID with upper case with desired maximum length
 *
 * @author Rajiv Singla
 */
public class RandomIdSupplier implements Supplier<String> {

    private static final char[] LETTERS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final Random RANDOM = new Random();

    private final int maxLength;

    public RandomIdSupplier(int maxLength) {
        this.maxLength = maxLength;
    }


    @Override
    public String get() {

        final StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append(LETTERS[RANDOM.nextInt(LETTERS.length)]);
        }

        return sb.toString().toUpperCase(Locale.getDefault());
    }

}
