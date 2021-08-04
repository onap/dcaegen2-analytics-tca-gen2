/*
 * ==================================================================================
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
 */

package org.onap.dcae.analytics.tca.model.util.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.onap.dcae.analytics.tca.model.facade.Aai;

public class AaiSerializer extends StdSerializer<Aai> {

    private static final String SERVER_NAME_KEY = "vserver.vserver-name";
    private static final String VNF_NAME_KEY = "generic-vnf.vnf-name";

    public AaiSerializer(){
        this(Aai.class);
    }

    protected AaiSerializer(Class<Aai> t) {
        super(t);
    }

    @Override
    public void serialize(Aai aai, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();

        List<String> filters = new ArrayList<>();
        if (aai.getGenericVNFName() != null) {
            serializerProvider.defaultSerializeField(VNF_NAME_KEY, aai.getGenericVNFName(), jsonGenerator);
            filters.add(VNF_NAME_KEY);
        }

        if (aai.getGenericServerName() != null) {
            serializerProvider.defaultSerializeField(SERVER_NAME_KEY, aai.getGenericServerName(), jsonGenerator);
            filters.add(SERVER_NAME_KEY);
        }

        for (Map.Entry<String, Object> prop : aai.getDynamicProperties().entrySet()) {
            if (!filters.contains(prop.getKey())) {
                serializerProvider.defaultSerializeField(prop.getKey(), prop.getValue(), jsonGenerator);
            }
        }

        jsonGenerator.writeEndObject();
    }
}
