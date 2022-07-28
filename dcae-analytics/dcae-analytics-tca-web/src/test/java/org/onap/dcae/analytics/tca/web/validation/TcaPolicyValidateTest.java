/*
 * ===========LICENSE_START=====================================================================
 * Copyright (c) 2020 ChinaMobile. All rights reserved.
 * Copyright (c) 2022 Wipro Limited Intellectual Property. All rights reserved.
 * ================================================================================
 * Copyright (c) 2019 IBM
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
package org.onap.dcae.analytics.tca.web.validation;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.domain.TcaPolicyWrapper;
import org.onap.dcae.analytics.test.BaseAnalyticsSpringBootIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.Errors;

@ActiveProfiles({AnalyticsProfile.DEV_PROFILE_NAME})
public class TcaPolicyValidateTest extends BaseAnalyticsSpringBootIT {

    @Autowired
    Environment environment;
    
    @Test
    void TestTcaAppPropertiesValidator() {
        TcaAppProperties properties = new TcaAppProperties(environment);
        TcaPolicyValidator validate = new TcaPolicyValidator();
        Errors errors = Mockito.mock(Errors.class);
        TcaPolicyWrapper tcaPolicyWrapper = new TcaPolicyWrapper(properties);
        tcaPolicyWrapper.getConfigSource();
        List<TcaPolicy> tcaPolicy = tcaPolicyWrapper.getTcaPolicy();
        validate.validate(tcaPolicy, errors);        
        validate.apply(tcaPolicy);
        Assertions.assertEquals("version-0", tcaPolicyWrapper.getPolicyVersion());
    }
}
