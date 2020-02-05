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

package org.onap.dcae.analytics.tca.web.service;

import lombok.Data;

import java.util.Date;

import org.onap.dcae.analytics.tca.core.service.TcaAbatementContext;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementEntity;
import org.onap.dcae.analytics.tca.core.service.TcaAbatementRepository;

/**
 * @author Rajiv Singla
 */
@Data
public class TestTcaAbatementContext implements TcaAbatementContext {

    private boolean isAbatementEnabled = true;
    private TcaAbatementRepository tcaAbatementRepository = new TestTcaAbatementRepository();


    @Override
    public TcaAbatementEntity create(
            final String lookupKey, final String requestId, final boolean isAbatementAlertSent) {
        return new TestTcaAbatementEntity(new Date(), lookupKey, requestId, isAbatementAlertSent);
    }

}
