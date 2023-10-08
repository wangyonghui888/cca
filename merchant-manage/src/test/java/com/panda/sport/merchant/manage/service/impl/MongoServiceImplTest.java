package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.vo.MongoVo;
import com.panda.sport.merchant.manage.ManageApplication;
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
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageApplication.class)
@Slf4j
class MongoServiceImplTest {

    @Test
    void send() {

    }


    @Test
     void sendMongeMessge() {
        String text = "商户【test】变更白名单";
        String targetname = "codm_1082015925"; //群id
        String userId = "188040";
        String userToken = "0156e709ab3d4c61b939f4d5578fe8ee";
        //String url = "https://trobot.ymtio.com/api/robot/user188040:0156e709ab3d4c61b939f4d5578fe8ee/sendmessage_v2";
        String url = "https://trobot.ymtio.com/api/robot/user" + userId + ":" + userToken + "/sendmessage_v2";
        log.info("text = {}, targetname = {}", text, targetname);
        CloseableHttpClient httpclient = HttpClientBuilder.create().useSystemProperties().build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).setConnectTimeout(30000).build();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "eXwdrXrvrjsHDs7F");
        httpPost.setConfig(requestConfig);
        MongoVo object = new MongoVo();
        object.setChatType(2);
        object.setModel(0);
        object.setText(text);

        object.setTargetname(targetname);
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