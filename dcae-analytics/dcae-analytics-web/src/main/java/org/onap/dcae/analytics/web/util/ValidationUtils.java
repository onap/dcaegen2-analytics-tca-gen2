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

package org.onap.dcae.analytics.web.util;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.onap.dcae.analytics.web.exception.AnalyticsValidationException;
import org.onap.dcae.analytics.web.validation.AnalyticsValidator;
import org.onap.dcae.analytics.web.validation.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Validation Utilities
 *
 * @author Rajiv Singla
 */
public abstract class ValidationUtils {

    private static final Logger log = LoggerFactory.getLogger(ValidationUtils.class);

    private ValidationUtils() {

    }

    /**
     * Checks if String is empty. For null string true is returned
     *
     * @param stringValue string value
     *
     * @return returns true is string is empty or null
     */
    public static boolean isEmpty(@Nullable final String stringValue) {
        return stringValue == null || stringValue.isEmpty() || stringValue.trim().isEmpty();
    }


    /**
     * Checks if String value is present. A null, empty, or blank values of string
     * are considered not present.
     *
     * @param stringValue string value to check if it is present or not
     *
     * @return true if string value is not null, empty or blank
     */
    public static boolean isPresent(@Nullable final String stringValue) {
        return !isEmpty(stringValue);
    }


    /**
     * Provides common functionality to validate analytics objects.
     * Throws {@link AnalyticsValidationException} exception if validation fails
     *
     * @param targetObject target object that needs to be validated
     * @param validator validator that will be used to validate the target object
     * @param <T> target object type that needs to be validated
     * @param <R> Validation Response type
     * @param <V> Validator Type
     *
     * @return target object if validation is successful
     */
    public static <T, R extends ValidationResponse, V extends AnalyticsValidator<T, R>> T validate(
            @Nonnull final T targetObject,
            @Nonnull final V validator) {

        Assert.notNull(targetObject, "target object that needs to validated must not be null");
        Assert.notNull(validator, "validator must not be null");

        final String targetObjectClass = targetObject.getClass().getSimpleName();
        final String validatorClass = validator.getClass().getSimpleName();

        log.debug("Validating target object of type: {} with validator type: {} ", targetObjectClass, validatorClass);

        final R validationResponse = validator.apply(targetObject);

        // If setting validation fails throw an exception
        if (validationResponse.hasErrors()) {
            throw new AnalyticsValidationException(validationResponse.getAllErrorMessage(),
                    new IllegalArgumentException(validationResponse.getAllErrorMessage()));
        }

        log.info("Validation Successful for target object type: {} with validator type: {}", targetObjectClass,
                validatorClass);

        return targetObject;
    }
}
