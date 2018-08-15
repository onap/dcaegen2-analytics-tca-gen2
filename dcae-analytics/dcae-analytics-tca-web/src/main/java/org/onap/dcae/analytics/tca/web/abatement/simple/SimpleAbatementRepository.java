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

package org.onap.dcae.analytics.tca.web.abatement.simple;

import static org.onap.dcae.analytics.model.TcaModelConstants.TCA_ABATEMENT_SIMPLE_REPOSITORY_MAX_ENTITY_COUNT;
import static org.onap.dcae.analytics.model.TcaModelConstants.TCA_ABATEMENT_SIMPLE_REPOSITORY_REMOVE_ENTITY_COUNT;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.onap.dcae.analytics.tca.core.service.TcaAbatementEntity;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple implementation for {@link TcaAbatementRepository} which should be used for testing and development
 * purposes only.
 *
 * @author Rajiv Singla
 */
public class SimpleAbatementRepository implements TcaAbatementRepository {

    private static final Logger logger = LoggerFactory.getLogger(SimpleAbatementRepository.class);

    private final ConcurrentLinkedQueue<String> lookupKeysQueue;
    private final ConcurrentHashMap<String, SimpleAbatementEntity> repository;

    public SimpleAbatementRepository() {
        this.lookupKeysQueue = new ConcurrentLinkedQueue<>();
        this.repository = new ConcurrentHashMap<>(TCA_ABATEMENT_SIMPLE_REPOSITORY_MAX_ENTITY_COUNT);
    }


    @Override
    public synchronized void save(final TcaAbatementEntity tcaAbatementEntity) {

        // remove entries from repository if required
        if (lookupKeysQueue.size() == TCA_ABATEMENT_SIMPLE_REPOSITORY_MAX_ENTITY_COUNT) {

            logger.warn("Simple Abatement Repository reached its max allowed size: {}, " +
                            "Removing last inserted {} entries",
                    TCA_ABATEMENT_SIMPLE_REPOSITORY_MAX_ENTITY_COUNT,
                    TCA_ABATEMENT_SIMPLE_REPOSITORY_REMOVE_ENTITY_COUNT);

            for (int i = 0; i < TCA_ABATEMENT_SIMPLE_REPOSITORY_REMOVE_ENTITY_COUNT; i++) {
                final String lookupKey = lookupKeysQueue.poll();
                logger.warn("Removing Abatement Lookup key: {} from Simple Abatement Repository", lookupKey);
                repository.remove(lookupKey);
            }

        }

        lookupKeysQueue.add(tcaAbatementEntity.getLookupKey());
        final SimpleAbatementEntity simpleAbatementEntity =
                new SimpleAbatementEntity(tcaAbatementEntity.getLookupKey(), tcaAbatementEntity.getRequestId(),
                        tcaAbatementEntity.isAbatementAlertSent());
        repository.put(tcaAbatementEntity.getLookupKey(), simpleAbatementEntity);

    }

    @Override
    public List<TcaAbatementEntity> findByLookupKey(final String lookUpKey) {
        final SimpleAbatementEntity repoEntity = repository.getOrDefault(lookUpKey, null);
        if (repoEntity == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(repoEntity);
    }
}
