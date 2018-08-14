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

package org.onap.dcae.analytics.tca.core.util.function.calculation;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.onap.dcae.analytics.model.cef.CommonEventHeader;
import org.onap.dcae.analytics.model.cef.Event;
import org.onap.dcae.analytics.model.cef.EventListener;
import org.onap.dcae.analytics.tca.core.exception.TcaProcessingException;
import org.onap.dcae.analytics.tca.core.service.TcaExecutionContext;
import org.onap.dcae.analytics.tca.core.service.TcaResultContext;
import org.onap.dcae.analytics.tca.model.policy.MetricsPerEventName;
import org.onap.dcae.analytics.tca.model.policy.TcaPolicy;
import org.onap.dcae.analytics.tca.model.policy.Threshold;

/**
 * @author Rajiv Singla
 */
public class TcaThresholdViolationCalculator implements TcaCalculationFunction {

    @Override
    public TcaExecutionContext calculate(final TcaExecutionContext tcaExecutionContext) {

        final String cefMessage = tcaExecutionContext.getCefMessage();
        final EventListener eventListener = tcaExecutionContext.getTcaProcessingContext().getEventListener();
        final TcaPolicy tcaPolicy = tcaExecutionContext.getTcaPolicy();

        // Get CEF Event Name
        final String cefEventName = Optional.ofNullable(eventListener)
                .map(EventListener::getEvent)
                .map(Event::getCommonEventHeader)
                .map(CommonEventHeader::getEventName)
                .orElseThrow(() -> new TcaProcessingException("Required Field: EventName not present"));

        // Get Policy's metrics per event name matching CEF message event name
        final MetricsPerEventName policyMetricsPerEventName =
                tcaPolicy.getMetricsPerEventName().stream()
                        .filter(m -> m.getEventName().equalsIgnoreCase(cefEventName))
                        .findFirst().orElseThrow(() ->
                        new TcaProcessingException("Required Field: MetricsPerEventName not present"));


        // get violated policy threshold for cef event name sorted by severity
        final Optional<Threshold> thresholdOptional =
                getViolatedThreshold(policyMetricsPerEventName.getThresholds(), cefMessage);


        // Check if threshold violation is present
        if (!thresholdOptional.isPresent()) {
            final String earlyTerminationMessage = "No Policy Threshold violation detected in CEF Message";
            setTerminatingMessage(earlyTerminationMessage, tcaExecutionContext, false);
            return tcaExecutionContext;
        }


        // Threshold violations are present - update tca processing result context
        final MetricsPerEventName violatedMetricsPerEventName = copyMetricsPerEventName(policyMetricsPerEventName);
        final Threshold violatedThreshold = thresholdOptional.get();
        violatedMetricsPerEventName.setThresholds(Collections.singletonList(violatedThreshold));
        final TcaResultContext tcaResultContext =
                tcaExecutionContext.getTcaResultContext();
        tcaResultContext.setViolatedMetricsPerEventName(violatedMetricsPerEventName);

        return tcaExecutionContext;
    }


    /**
     * Provides violated threshold
     *
     * @param policyThresholds policy thresholds that need to applied to incoming cef message
     * @param cefMessage incoming cef message
     *
     * @return list of all violated threshold
     */
    private static Optional<Threshold> getViolatedThreshold(final List<Threshold> policyThresholds,
                                                            final String cefMessage) {

        // map containing key as field path and associated policy thresholds
        final Map<String, List<Threshold>> policyFieldPathsMap = new LinkedHashMap<>();
        for (final Threshold policyThreshold : policyThresholds) {
            if (policyFieldPathsMap.get(policyThreshold.getFieldPath()) == null) {
                final LinkedList<Threshold> policyThresholdList = new LinkedList<>();
                policyThresholdList.add(policyThreshold);
                policyFieldPathsMap.put(policyThreshold.getFieldPath(), policyThresholdList);
            } else {
                policyFieldPathsMap.get(policyThreshold.getFieldPath()).add(policyThreshold);
            }
        }

        // get map containing key as field path and values for json path
        final Map<String, List<BigDecimal>> messageFieldValuesMap =
                getJsonPathValues(cefMessage, policyFieldPathsMap.keySet());

        // if no matching path values - assuming no threshold violations
        if (messageFieldValuesMap.isEmpty()) {
            return Optional.empty();
        }

        // Determine all violated thresholds per message field Path
        final Map<String, Threshold> violatedThresholdsMap = new HashMap<>();
        for (Map.Entry<String, List<BigDecimal>> messageFieldValuesMapEntry : messageFieldValuesMap.entrySet()) {
            final String messageFieldPath = messageFieldValuesMapEntry.getKey();
            final List<Threshold> messageFieldAssociatedPolicyThresholds = policyFieldPathsMap.get(messageFieldPath);
            if (messageFieldAssociatedPolicyThresholds != null) {
                final Optional<Threshold> thresholdOptional = computeViolatedThreshold(
                        messageFieldValuesMapEntry.getValue(), messageFieldAssociatedPolicyThresholds);
                thresholdOptional.ifPresent(threshold -> violatedThresholdsMap.put(messageFieldPath, threshold));
            }
        }

        // if multiple fields have violated threshold - grab the first one with max severity
        return violatedThresholdsMap.values().stream()
                .sorted(Comparator.comparing(Threshold::getSeverity)).findFirst();

    }

    /**
     * Computes if any CEF Message Fields have violated any Policy Thresholds. For the same policy field path
     * it applies threshold in order of their severity and then by direction and returns first violated threshold
     *
     * @param messageFieldValues Field Path Values extracted from CEF Message
     * @param fieldThresholds Policy Thresholds for Field Path
     *
     * @return Optional of violated threshold for a field path
     */
    private static Optional<Threshold> computeViolatedThreshold(final List<BigDecimal> messageFieldValues,
                                                                final List<Threshold> fieldThresholds) {

        // sort thresholds based on severity and then based on direction
        final List<Threshold> sortedPolicyThresholds = fieldThresholds.stream()
                .sorted((t1, t2) -> {
                    if (t1.getSeverity().compareTo(t2.getSeverity()) != 0) {
                        return t1.getSeverity().compareTo(t2.getSeverity());
                    } else {
                        return t1.getDirection().compareTo(t2.getDirection());
                    }
                })
                .collect(Collectors.toList());

        // Now apply each threshold to field values
        for (final Threshold policyThreshold : sortedPolicyThresholds) {
            for (final BigDecimal messageFieldValue : messageFieldValues) {
                final Boolean isThresholdViolated =
                        policyThreshold.getDirection().operate(messageFieldValue,
                                new BigDecimal(policyThreshold.getThresholdValue()));
                if (isThresholdViolated) {
                    final Threshold violatedThreshold = copyThreshold(policyThreshold);
                    violatedThreshold.setActualFieldValue(messageFieldValue);
                    return Optional.of(violatedThreshold);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Extracts json path values for given json Field Path from using Json path notation.
     *
     * @param message CEF Message
     * @param jsonFieldPaths Json Field Paths
     *
     * @return Map containing key as json path and values as values associated with that json path
     */
    private static Map<String, List<BigDecimal>> getJsonPathValues(@Nonnull String message,
                                                                   @Nonnull Set<String> jsonFieldPaths) {

        final Map<String, List<BigDecimal>> jsonFieldPathMap = new HashMap<>();
        final DocumentContext documentContext = JsonPath.parse(message);

        for (String jsonFieldPath : jsonFieldPaths) {
            List<BigDecimal> jsonFieldValues;

            try {
                jsonFieldValues = documentContext.read(jsonFieldPath, new TypeRef<List<BigDecimal>>() {
                });
            } catch (Exception e) {
                final String errorMessage = String.format(
                        "Unable to convert jsonFieldPath value to valid number." +
                                "Json Path: %s.Incoming message: %s", jsonFieldPath, message);
                throw new TcaProcessingException(errorMessage, e);
            }
            // If Json Field Values are present
            if (jsonFieldValues != null && !jsonFieldValues.isEmpty()) {
                // Filter out all null values in the filed values list
                final List<BigDecimal> nonNullValues =
                        jsonFieldValues.stream().filter(Objects::nonNull).collect(Collectors.toList());
                // If there are non null values put them in the map
                if (!nonNullValues.isEmpty()) {
                    jsonFieldPathMap.put(jsonFieldPath, nonNullValues);
                }
            }
        }

        return jsonFieldPathMap;
    }

    /**
     * Creates a new threshold hold object by copying the value of given threshold object
     *
     * @param threshold threshold that needs to be copied
     *
     * @return new threshold object which is copy of given threshold object
     */
    private static Threshold copyThreshold(final Threshold threshold) {
        final Threshold copyThreshold = new Threshold();
        copyThreshold.setClosedLoopControlName(threshold.getClosedLoopControlName());
        copyThreshold.setClosedLoopEventStatus(threshold.getClosedLoopEventStatus());
        copyThreshold.setVersion(threshold.getVersion());
        copyThreshold.setFieldPath(threshold.getFieldPath());
        copyThreshold.setThresholdValue(threshold.getThresholdValue());
        copyThreshold.setDirection(threshold.getDirection());
        copyThreshold.setSeverity(threshold.getSeverity());
        return copyThreshold;
    }

    /**
     * Returns a copy of metric Per event name without copying thresholds
     *
     * @param metricsPerEventName metric per event name that needs to be copied
     *
     * @return new metric per event name object which is copy of given object
     */
    private static MetricsPerEventName copyMetricsPerEventName(final MetricsPerEventName metricsPerEventName) {
        final MetricsPerEventName copyMetricsPerEventName = new MetricsPerEventName();
        copyMetricsPerEventName.setEventName(metricsPerEventName.getEventName());
        copyMetricsPerEventName.setControlLoopSchemaType(metricsPerEventName.getControlLoopSchemaType());
        copyMetricsPerEventName.setPolicyScope(metricsPerEventName.getPolicyScope());
        copyMetricsPerEventName.setPolicyName(metricsPerEventName.getPolicyName());
        copyMetricsPerEventName.setPolicyVersion(metricsPerEventName.getPolicyVersion());
        // no thresholds copied
        return copyMetricsPerEventName;
    }

}
