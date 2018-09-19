package org.onap.dcae.analytics.web.http;

import org.junit.jupiter.api.Test;
import org.onap.dcae.analytics.web.BaseAnalyticsWebTest;
import org.onap.dcae.analytics.web.dmaap.MrSubscriberPreferences;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EelfAuditLogInterceptorTest extends BaseAnalyticsWebTest {

    @Test
    void intercept() throws Exception {
        MrSubscriberPreferences mrSubscriberPreferences = new MrSubscriberPreferences("http://tst:123");
        mrSubscriberPreferences.enableEcompAuditLogging = true;

        final EelfAuditLogInterceptor eelfAuditLogInterceptor = new EelfAuditLogInterceptor(mrSubscriberPreferences);

        HttpRequest httpRequest = new MockClientHttpRequest();
        eelfAuditLogInterceptor.intercept(httpRequest, "test".getBytes(), new ClientHttpRequestExecution() {
            @Override
            public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
                return new MockClientHttpResponse("OK".getBytes(), HttpStatus.OK);
            }
        });

    }
}
