package com.panda.multiterminalinteractivecenter.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HttpClient {
    private static PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    public  static RestTemplate restTemplate;

    @PostConstruct
    public  void initHttpPool(){
        connectionManager.setMaxTotal(500);
//        connectionManager.setDefaultMaxPerRoute(100);
        RequestConfig r = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(2000).setSocketTimeout(10000).build();
        HttpRequestRetryHandler httpRequestRetryHandler = new StandardHttpRequestRetryHandler();
        CloseableHttpClient httpClient = HttpClients.custom().useSystemProperties().setConnectionManager(connectionManager).setDefaultRequestConfig(r)
                .setRetryHandler(httpRequestRetryHandler).build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(3000);
        httpRequestFactory.setReadTimeout(10000);
        httpRequestFactory.setHttpClient(httpClient);
        restTemplate= new RestTemplate(httpRequestFactory);

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
                    log.error("HTTP连接池异常!", t);
                }
            }
        }, 0, 1000 * 20);
    }

}
