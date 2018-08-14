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

package org.onap.dcae.analytics.web.http;


import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.ssl.SSLContextBuilder;
import org.onap.dcae.analytics.model.AnalyticsHttpConstants;
import org.onap.dcae.analytics.model.util.function.StringToURLFunction;
import org.onap.dcae.analytics.web.util.AnalyticsWebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * Creates a {@link RestTemplateCustomizer} which can be used to configure the spring rest templates
 * based on given {@link HttpClientPreferences}
 *
 * @param <T> Http Client Configurations
 *
 * @author Rajiv Singla
 */
public class HttpClientPreferencesCustomizer<T extends HttpClientPreferences> implements RestTemplateCustomizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientPreferencesCustomizer.class);

    private final T httpClientConfig;

    public HttpClientPreferencesCustomizer(final T httpClientConfig) {
        this.httpClientConfig = httpClientConfig;
    }

    @Override
    public void customize(final RestTemplate restTemplate) {

        final String httpClientId = httpClientConfig.getHttpClientId() != null ? httpClientConfig.getHttpClientId()
                : AnalyticsHttpConstants.DEFAULT_HTTP_CLIENT_ID_PREFIX + AnalyticsWebUtils.RANDOM_ID_SUPPLIER.get();
        LOGGER.debug("Customizing Rest Template for Http Client Id: {}", httpClientId);

        // set request url
        final URL requestURL = new StringToURLFunction().apply(httpClientConfig.getRequestURL())
                .orElseThrow(() -> new IllegalArgumentException("Http Client URL is required"));
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(requestURL.toString()));

        // add basic authentication headers
        final String username = httpClientConfig.getUsername();
        if (username != null) {
            restTemplate.getInterceptors().add(
                    new BasicAuthorizationInterceptor(username, httpClientConfig.getPassword()));
        }

        // set default request headers
        final HttpHeaders defaultRequestHeaders = httpClientConfig.getRequestHeaders();
        if (defaultRequestHeaders != null) {
            restTemplate.getInterceptors().add(new DefaultHeadersRequestInterceptor(defaultRequestHeaders));
        }

        // create new http client builder
        final HttpClientBuilder httpClientBuilder = HttpClients.custom().useSystemProperties()
                .disableContentCompression();
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        // set basic authentication credentials
        configureAuthenticationCredentials(httpClientId, requestURL, credentialsProvider);
        // set up proxy url
        configureProxySettings(httpClientId, httpClientBuilder, credentialsProvider);
        // setup credentials provider
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        // set up ssl Context
        configureSSLContext(httpClientId, httpClientBuilder);

        // set rest client builder
        final HttpComponentsClientHttpRequestFactory httpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build());

        // set ecomp logging interceptor
        if (httpClientConfig.getEnableEcompAuditLogging()) {
            restTemplate.getInterceptors().add(new EelfAuditLogInterceptor(httpClientConfig));
        }

        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory));
    }

    /**
     * Configures authentication credentials
     *
     * @param httpClientId http client id
     * @param requestURL request url
     * @param credentialsProvider credentials provider
     */
    private void configureAuthenticationCredentials(final String httpClientId, final URL requestURL,
                                                    final CredentialsProvider credentialsProvider) {
        final String username = httpClientConfig.getUsername();
        if (username != null) {
            LOGGER.info("Setting basic Authentication credentials for Http Client Id: {} with username: {}",
                    httpClientId, username);
            final String requestURLProtocol = requestURL.getProtocol();
            final String requestUrlHost = requestURL.getHost();
            final Integer requestUrlPortNumber = requestURL.getPort();
            final HttpHost requestURLHost = new HttpHost(requestUrlHost, requestUrlPortNumber, requestURLProtocol);
            final String password = httpClientConfig.getPassword();
            final AuthScope httpClientAuthScope = new AuthScope(requestURLHost);
            final Credentials credentials = new UsernamePasswordCredentials(username, password);
            credentialsProvider.setCredentials(httpClientAuthScope, credentials);
        } else {
            LOGGER.warn("No credentials set for Http Client Id: {}. No username present", httpClientId);
        }
    }

    /**
     * Configures proxy host, port and authentication
     *
     * @param httpClientId http client id
     * @param httpClientBuilder http client builder
     * @param credentialsProvider http credentials provider
     */
    private void configureProxySettings(final String httpClientId, final HttpClientBuilder httpClientBuilder,
                                        final CredentialsProvider credentialsProvider) {

        final URL proxyURL = httpClientConfig.getProxyURL();

        if (proxyURL == null) {
            LOGGER.debug("Proxy not Enabled - bypassing setting Proxy settings for Http Client Id: {}", httpClientId);
            return;
        }

        final String proxyProtocol = proxyURL.getProtocol();
        final String proxyHost = proxyURL.getHost();
        final Integer proxyPort = proxyURL.getPort();
        final HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxyProtocol);

        LOGGER.info("Setting up proxy for Http Client Id: {} as: {}", httpClientId, proxy);

        final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        httpClientBuilder.setRoutePlanner(routePlanner);

        // get proxy credentials information
        final String userInfo = proxyURL.getUserInfo();

        if (!StringUtils.hasText(userInfo)) {
            LOGGER.debug("Proxy username not present. " +
                    "No proxy authentication credentials will be set for Http Client Id: {}", httpClientId);
            return;
        }

        final String[] userInfoArray = userInfo.split(":");
        final String proxyUsername = userInfoArray[0];
        String proxyPassword = null;
        if (userInfoArray.length > 1) {
            proxyPassword = userInfoArray[1];
        }
        LOGGER.info("Setting proxy credentials with username: {} for Http Client Id: {}", proxyUsername, httpClientId);
        final AuthScope proxyAuthScope = new AuthScope(proxyHost, proxyPort);
        final Credentials proxyCredentials = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
        credentialsProvider.setCredentials(proxyAuthScope, proxyCredentials);
    }

    /**
     * Configures SSL Context
     *
     * @param httpClientId http client id
     * @param httpClientBuilder http client builder
     */
    private void configureSSLContext(final String httpClientId, final HttpClientBuilder httpClientBuilder) {

        // Setup SSL Context to ignore SSL certificate issues if ignoreSSLCertificateErrors is true
        final boolean ignoreSSLValidation =
                Optional.ofNullable(httpClientConfig.getIgnoreSSLValidation()).orElse(false);
        LOGGER.info("Ignore SSL Certificate Errors attributed is set to: {} for Http Client Id: {}",
                ignoreSSLValidation, httpClientId);

        if (!ignoreSSLValidation) {
            LOGGER.info("SSL Validation will be enforced for Http Client Id: {}", httpClientId);
            return;
        }

        LOGGER.warn("SSL Certificate Errors will be ignored for Http Client Id: {}", httpClientId);
        try {
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(null, new AlwaysTrustingTrustStrategy());
            httpClientBuilder.setSSLContext(sslContextBuilder.build());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);

    }


    /**
     * Header Request Interceptor adds defaults headers if not set explicitly
     */
    private static class DefaultHeadersRequestInterceptor implements ClientHttpRequestInterceptor {
        private final HttpHeaders httpHeaders;

        DefaultHeadersRequestInterceptor(final HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
        }

        @Override
        public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
                                            final ClientHttpRequestExecution execution) throws IOException {
            final HttpHeaders currentRequestHeaders = request.getHeaders();
            for (Map.Entry<String, List<String>> defaultHttpHeader : httpHeaders.entrySet()) {
                if (!currentRequestHeaders.containsKey(defaultHttpHeader.getKey())) {
                    currentRequestHeaders.addAll(defaultHttpHeader.getKey(), defaultHttpHeader.getValue());
                }
            }
            currentRequestHeaders.setAccept(httpHeaders.getAccept());
            currentRequestHeaders.setAcceptCharset(httpHeaders.getAcceptCharset());
            currentRequestHeaders.remove(HttpHeaders.ACCEPT_ENCODING);
            return execution.execute(request, body);
        }
    }

    /**
     * An implementation of SSL Trust Strategy which does no SSL certificate validation effectively
     * bypassing any SSL certificate related issues
     */
    private static class AlwaysTrustingTrustStrategy implements TrustStrategy {
        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    }

}
