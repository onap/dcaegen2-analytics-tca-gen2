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


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * An unbounded supplier that can be used to generated unbounded sequence of elements
 * in random or round robin generation mode.
 *
 * @param <T> The type of result supplied by the supplier
 *
 * @author Rajiv Singla
 */
public class UnboundedSupplier<T> implements Supplier<T> {

    private final T[] elements;
    private final GenerationMode generationMode;
    private final int numElements;
    private AtomicInteger index;
    private Random random;

    /**
     * Sequence Generation Mode
     */
    public enum GenerationMode {
        /**
         * Sequence will be generated in uniformly distributed random
         */
        RANDOM,
        /**
         * Sequence will be generated in round robin manner
         */
        ROUND_ROBIN;
    }

    @SafeVarargs
    public UnboundedSupplier(final GenerationMode generationMode, final T... elements) {
        if (elements == null || elements.length < 1) {
            throw new IllegalArgumentException("Element size must be greater than 1");
        }
        this.generationMode = generationMode;
        this.elements = elements;
        numElements = elements.length;
        if (numElements > 1) {
            if (generationMode == GenerationMode.ROUND_ROBIN) {
                index = new AtomicInteger(-1);
            } else {
                random = new Random();
            }
        }
    }

    @SafeVarargs
    public UnboundedSupplier(T... elements) {
        this(GenerationMode.ROUND_ROBIN, elements);
    }

    @Override
    public T get() {
        if (numElements == 1) {
            return elements[0];
        }
        if (generationMode == GenerationMode.ROUND_ROBIN) {
            index.getAndUpdate(idx -> idx >= numElements - 1 ? 0 : idx + 1);
            return elements[index.get()];
        } else {
            return elements[random.nextInt(numElements)];
        }
    }

}
