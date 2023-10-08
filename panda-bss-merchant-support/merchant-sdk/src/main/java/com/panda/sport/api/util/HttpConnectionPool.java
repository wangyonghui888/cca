
package com.panda.sport.api.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpConnectionPool implements InitializingBean {
    private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    public static RestTemplate restTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(50);
        connectionManager.setMaxTotal(500);
        connectionManager.setDefaultMaxPerRoute(60);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(2000)
                .setSocketTimeout(10000).build();
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();
        HttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler).build();
        HttpComponentsClientHttpRequestFactory httpClientFactory = new HttpComponentsClientHttpRequestFactory();
        httpClientFactory.setConnectTimeout(2000);
        httpClientFactory.setReadTimeout(10000);
        httpClientFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(httpClientFactory);
    }

}


