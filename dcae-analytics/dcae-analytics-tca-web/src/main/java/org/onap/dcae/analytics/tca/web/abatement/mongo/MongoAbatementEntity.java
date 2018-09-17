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

package org.onap.dcae.analytics.tca.web.abatement.mongo;

import lombok.Data;

import java.util.Date;

import org.onap.dcae.analytics.tca.core.service.TcaAbatementEntity;
import org.onap.dcae.analytics.tca.model.TcaModel;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Rajiv Singla
 */
@Data
@Document(collection = "tca_abatement")
public class MongoAbatementEntity implements TcaAbatementEntity, TcaModel {

    private static final long serialVersionUID = 1L;

    @Id
    private String lookupKey;
    private String requestId;
    private boolean isAbatementAlertSent;

    @LastModifiedDate
    private Date lastModificationDate;
    @CreatedDate
    private Date createdDate;

    MongoAbatementEntity(final String lookupKey, final String requestId, final boolean isAbatementAlertSent) {
        this.lookupKey = lookupKey;
        this.requestId = requestId;
        this.isAbatementAlertSent = isAbatementAlertSent;
    }
}
