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

package org.onap.dcae.analytics.tca.web.validation;


import org.onap.dcae.analytics.tca.web.TcaAppProperties;
import org.onap.dcae.analytics.web.util.ValidationUtils;
import org.onap.dcae.analytics.web.validation.AnalyticsValidator;
import org.onap.dcae.analytics.web.validation.GenericValidationResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.Errors;

/**
 * @author Rajiv Singla
 */
public class ConfigPropertiesAaiValidator implements
        AnalyticsValidator<TcaAppProperties.Aai, GenericValidationResponse> {

    private static final long serialVersionUID = 1L;

    @Override
    public GenericValidationResponse apply(final TcaAppProperties.Aai aai) {

        final GenericValidationResponse validationResponse = new GenericValidationResponse();

        final String genericVnfPath = aai.getGenericVnfPath();
        if (ValidationUtils.isEmpty(genericVnfPath)) {
            validationResponse.addErrorMessage("generic_vnf_path", "AAI Generic vnf path must be present");
        }

        final String nodeQueryPath = aai.getNodeQueryPath();
        if (ValidationUtils.isEmpty(nodeQueryPath)) {
            validationResponse.addErrorMessage("node_query_path", "AAI Node Query Path must be present");
        }


        final String url = aai.getUrl();
        if (ValidationUtils.isEmpty(url)) {
            validationResponse.addErrorMessage("url", "AAI url must be present");
        }

        return validationResponse;
    }

    @Override
    public boolean supports(final Class<?> type) {
        return type == TcaAppProperties.Aai.class;
    }

    @Override
    public void validate(@Nullable final Object target, final Errors errors) {

        // if aai is not present - assuming no aai enrichment is required
        if (target == null) {
            return;
        }
        final TcaAppProperties.Aai aai = (TcaAppProperties.Aai) target;

        // skip validation if aai enrichment is not enabled
        if (aai.getEnableEnrichment() == null || !aai.getEnableEnrichment()) {
            return;
        }

        final GenericValidationResponse validationResponse = apply(aai);

        if (validationResponse.hasErrors()) {
            errors.rejectValue("aai", validationResponse.getAllErrorMessage());
        }

    }
}
