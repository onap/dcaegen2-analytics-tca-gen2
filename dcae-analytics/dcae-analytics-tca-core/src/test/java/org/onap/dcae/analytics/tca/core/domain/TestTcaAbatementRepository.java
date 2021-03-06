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

package org.onap.dcae.analytics.tca.core.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

import org.onap.dcae.analytics.tca.core.service.TcaAbatementEntity;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementRepository;

/**
 * @author Rajiv Singla
 */
@Data
public class TestTcaAbatementRepository implements TcaAbatementRepository {

    private List<TcaAbatementEntity> testLookupAbatementEntities = new LinkedList<>();

    @Override
    public void save(final TcaAbatementEntity tcaAbatementEntity) {
        // do nothing
    }

    @Override
    public List<TcaAbatementEntity> findByLookupKey(final String lookUpKey) {
        return testLookupAbatementEntities;
    }

}
