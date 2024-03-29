/*
 * ==================================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * Copyright (C) 2021 Samsung Electronics Intellectual Property. All rights reserved.
 * ==================================================================================
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
 * ============LICENSE_END===========================================================
 *
 */

package org.onap.dcae.analytics.tca.model.facade;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.onap.dcae.analytics.model.common.BaseDynamicPropertiesProvider;
import org.onap.dcae.analytics.tca.model.util.json.serializer.AaiSerializer;

/**
 * @author Rajiv Singla
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = AaiSerializer.class)
public class Aai extends BaseDynamicPropertiesProvider implements TcaFacadeModel {

    private static final long serialVersionUID = 1L;

    private String genericVNFName;

    private String genericServerName;

}
