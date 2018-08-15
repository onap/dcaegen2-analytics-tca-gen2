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

package org.onap.dcae.analytics.tca.model.policy;

import java.math.BigDecimal;

/**
 * Enum for Threshold Direction
 *
 * @author Rajiv Singla
 */
public enum Direction implements TcaPolicyModel {

    EQUAL {
        @Override
        public Boolean operate(final BigDecimal value1, final BigDecimal value2) {
            return value1.compareTo(value2) == 0;
        }
    },
    LESS {
        @Override
        public Boolean operate(final BigDecimal value1, final BigDecimal value2) {
            return value1.compareTo(value2) < 0;
        }
    },
    LESS_OR_EQUAL {
        @Override
        public Boolean operate(final BigDecimal value1, final BigDecimal value2) {
            return value1.compareTo(value2) <= 0;
        }
    },
    GREATER {
        @Override
        public Boolean operate(final BigDecimal value1, final BigDecimal value2) {
            return value1.compareTo(value2) > 0;
        }
    },
    GREATER_OR_EQUAL {
        @Override
        public Boolean operate(final BigDecimal value1, final BigDecimal value2) {
            return value1.compareTo(value2) >= 0;
        }
    };

    /**
     * Configure logic for a particular Direction
     *
     * @param value1 left operand for Direction operation
     * @param value2 right operand for Direction operation
     *
     * @return result of operation for the direction logic
     */
    public abstract Boolean operate(final BigDecimal value1, final BigDecimal value2);


}
