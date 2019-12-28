/*-
 * ============LICENSE_START=======================================================
 * Copyright (C) 2019-2020 China Mobile. All rights reserved.
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
 */

package org.onap.dcae.analytics.web.exception;

/**
 * Exception thrown when there is a problem with the Consul environment.
 *
 * @author Kai Lu
 */
public class EnvironmentLoaderException extends Exception {

    private static final long serialVersionUID = 1L;

    public EnvironmentLoaderException(String message) {
        super(message);
    }
}
