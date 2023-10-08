package com.panda.multiterminalinteractivecenter.utils;


import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class HttpConnectionPool {
    private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    public static RestTemplate restTemplate;

    @Autowired
    private MultiterminalConfig multiterminalConfig;

    @PostConstruct
    public void initHttpPool() {
        // 总连接池数量
        connectionManager.setMaxTotal(500);
        // 可为每个域名设置单独的连接池数量
        connectionManager.setDefaultMaxPerRoute(60);
        // setConnectTimeout表示设置建立连接的超时时间
        // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
        // setSocketTimeout表示发出请求后等待对端应答的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(2000)
                .setSocketTimeout(10000).build();
        // 重试处理器，StandardHttpRequestRetryHandler这个是官方提供的，看了下感觉比较挫，很多错误不能重试，可自己实现HttpRequestRetryHandler接口去做
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();

        HttpClient httpClient;
        if(multiterminalConfig.getHttpProxySwitch()){
            httpClient = HttpClients.custom().useSystemProperties().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(retryHandler).build();
        }else{
            httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                    .setRetryHandler(retryHandler).build();
        }

        HttpComponentsClientHttpRequestFactory httpClientFactory = new HttpComponentsClientHttpRequestFactory();
        httpClientFactory.setConnectTimeout(2000);
        httpClientFactory.setReadTimeout(10000);
        httpClientFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(httpClientFactory);
    }

    @PostConstruct
    public void scheduleClear() {
        // 服务端假设关闭了连接，对客户端是不透明的，HttpClient为了缓解这一问题，在某个连接使用前会检测这个连接是否过时，如果过时则连接失效，但是这种做法会为每个请求
        // 增加一定额外开销，因此有一个定时任务专门回收长时间不活动而被判定为失效的连接，可以某种程度上解决这个问题
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 关闭失效连接并从连接池中移除
                    connectionManager.closeExpiredConnections();
                    // 关闭60秒钟内不活动的连接并从连接池中移除，空闲时间从交还给连接管理器时开始
                    connectionManager.closeIdleConnections(60, TimeUnit.SECONDS);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, 0, 1000 * 20);
    }
}

