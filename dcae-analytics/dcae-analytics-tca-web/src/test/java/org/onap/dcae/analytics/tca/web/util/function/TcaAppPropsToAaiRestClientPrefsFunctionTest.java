/*
 * ================================================================================
 * Copyright (c) 2020 China Mobile. All rights reserved.
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
package org.onap.dcae.analytics.tca.web.util.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.model.AnalyticsProfile;
import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.tca.web.aai.TcaAaiRestClientPreferences;
import org.onap.dcae.analytics.test.BaseAnalyticsSpringBootIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Kai Lu
 */
@ActiveProfiles({AnalyticsProfile.DEV_PROFILE_NAME})
public class TcaAppPropsToAaiRestClientPrefsFunctionTest extends BaseAnalyticsSpringBootIT  {

    @Autowired
    Environment environment;

    @Test
    void TestTcaAppPropsToAaiRestClientPrefsFunction() {
        TcaAppProperties properties = new TcaAppProperties(environment);
        TcaAppPropsToAaiRestClientPrefsFunction validate = new TcaAppPropsToAaiRestClientPrefsFunction();
        TcaAaiRestClientPreferences tcaAaiRestClientPreferences = validate.apply(properties);
        Assertions.assertEquals("aai-rest-client", tcaAaiRestClientPreferences.getHttpClientId());
    }

}
