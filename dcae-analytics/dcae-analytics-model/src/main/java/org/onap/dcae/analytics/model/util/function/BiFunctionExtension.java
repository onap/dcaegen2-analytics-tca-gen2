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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A BiFunction extension which extends {@link BiFunction} to support additional functional
 * concepts like currying
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
public interface BiFunctionExtension<T, U, R> extends BiFunction<T, U, R> {

    /**
     * Returns a {@link Function} which takes one argument
     *
     * @param t Bi Function first param
     *
     * @return Function which takes another argument
     */
    default Function<U, R> curry(T t) {
        return (U u) -> apply(t, u);
    }

}

