package com.panda.sport.merchant.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.api.service.IMongoMsgService;
import com.panda.sport.merchant.common.vo.MongoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RefreshScope
public class MongoMsgServiceImpl implements IMongoMsgService {

    @Value("${manage.send.mongo.switch:0}")
    private Integer sendMongoSwitch;

    @Value("${manage.mongo.url:https://trobot.ymtio.com/api/robot/usercod_60007531:6bdb196be8ef41b09569afcc0beee721/sendmessage_v2}")
    private String url;

    @Override
    public void send(String text, String targetName, String userId, String userToken) {
        log.info("text = {}, targetName = {}", text, targetName);
        CloseableHttpClient httpclient = HttpClientBuilder.create().useSystemProperties().build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).setConnectTimeout(30000).build();
        HttpPost httpPost = new HttpPost("https://trobot.ymtio.com/api/robot/user" + userId + ":" + userToken + "/sendmessage_v2");// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "eXwdrXrvrjsHDs7F");
        httpPost.setConfig(requestConfig);
        MongoVo object = new MongoVo();
        object.setChatType(2);
        object.setModel(0);

        if (sendMongoSwitch == 0) {
            object.setText(text);
        } else {
            log.info("消息被过滤掉了 text = {}", text);
            return;
        }
        object.setTargetname(targetName);
        StringEntity entity = new StringEntity(JSONObject.toJSONString(object), "utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                System.out.println(EntityUtils.toString(responseEntity));
            } else {
                log.error("请求返回:" + state + "(" + url + ")");
            }
        } catch (Exception e) {
            log.error("发送异常！", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (Exception e) {
                log.error("发送消息关闭异常！", e);
            }
        }
    }
}
