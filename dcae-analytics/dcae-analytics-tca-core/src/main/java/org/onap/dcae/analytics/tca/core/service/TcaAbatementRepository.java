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

package org.onap.dcae.analytics.tca.core.service;

import java.util.List;

/**
 * Provides abstractions for Tca Abatement Persistence
 *
 * @author Rajiv Singla
 */
public interface TcaAbatementRepository {

    /**
     * Saves new Tca Abatement Entity to some persistent store
     *
     * @param tcaAbatementEntity Tca Abatement Entity that need to be stored
     */
    void save(TcaAbatementEntity tcaAbatementEntity);


    /**
     * Provides any saved Tca Abatement Entities which same look up key
     *
     * @param lookUpKey look up key for abatement persistence entity
     *
     * @return list of previously saved tca abatement persistence entities has given lookup key
     */
    List<TcaAbatementEntity> findByLookupKey(String lookUpKey);


}
