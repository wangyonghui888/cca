package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.MongoVo;
import com.panda.sport.merchant.manage.service.IMongoService;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  mongo发消息
 * @Date: 2021-08-31 19:27:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Component
@RefreshScope
public class MongoServiceImpl implements IMongoService {

    @Value("${manage.send.mongo.switch:0}")
    private Integer sendMongoSwitch;

    @Value("${manage.mongo.targetname}")
    private String targetname;

    //密码重置后，token 变更
    @Value("${manage.mongo.url:https://trobot.ymtio.com/api/robot/usercod_60007531:6bdb196be8ef41b09569afcc0beee721/sendmessage_v2}")
    private String url;


    @Override
    @Async
    public void send(String text, String name) {
        if (StringUtil.isBlankOrNull(name)) {
            name = targetname;
        }
        sendMongeMessge(text, name);
    }


    @Override
    @Async
    public void send(String text, String name, String userId, String userToken) {
        sendMongeMessge(text, name, userId, userToken);
    }


    private void sendMongeMessge(String text, String targetname, String userId, String userToken) {
        log.info("text = {}, targetname = {}", text, targetname);
        //CloseableHttpClient httpclient = HttpClients.createDefault();
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


    private void sendMongeMessge(String text, String targetname) {
        log.info("text = {}, targetname = {}", text, targetname);
        //CloseableHttpClient httpclient = HttpClients.createDefault();
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

        if (sendMongoSwitch == 0) {
            object.setText(text);
        } else {
            log.info("消息被过滤掉了 text = {}", text);
            return;
        }
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

    @Override
    @Async
    public void send(String text) {
        sendMongeMessge(text, targetname);
    }
/*
   public static void main(String[] args) {
        String targetname="codm_1082015925";
      *//* if (StringUtils.isEmpty(targetname)){
            targetname = "codm_1082011300"; //codm_1080008179
        }*//*
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
                .setSocketTimeout(30000).setConnectTimeout(30000).build();
        String url = "https://trobot.ymtio.com/api/robot/user188040:0156e709ab3d4c61b939f4d5578fe8ee/sendmessage_v2";
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "eXwdrXrvrjsHDs7F");
        httpPost.setConfig(requestConfig);
        MongoVo object = new MongoVo();
        object.setChatType(2);
        object.setModel(0);
        object.setTargetname("codm_1082015925");
        String text = "<p>sadasdasd</p>";
        object.setText(text);
        StringEntity entity = new StringEntity(JSONObject.toJSONString(object), "utf-8");
        System.out.println(JSONObject.toJSONString(object));
        System.out.println("url = "+ url);
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
        }  catch (Exception e) {
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
    }*/


}
