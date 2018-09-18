package org.onap.dcae.analytics.web.http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientPreferencesCustomizerTest extends BaseAnalyticsWebTest {

    @Test
    public void customize() {

        MrSubscriberPreferences mrSubscriberPreferences = new MrSubscriberPreferences("http://tst:123");
        mrSubscriberPreferences.enableEcompAuditLogging = false;

        HttpClientPreferencesCustomizer<MrSubscriberPreferences> subscriberPreferencesHttpClientPreferencesCustomizer
                = new HttpClientPreferencesCustomizer<>(mrSubscriberPreferences);

        final RestTemplate restTemplate = new RestTemplate();
        subscriberPreferencesHttpClientPreferencesCustomizer.customize(restTemplate);

        Assertions.assertThat(restTemplate).isNotNull();

    }
}