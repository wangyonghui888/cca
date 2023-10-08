package com.panda.multiterminalinteractivecenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.service.IMongoService;
import com.panda.multiterminalinteractivecenter.utils.HttpClientUtil;
import com.panda.multiterminalinteractivecenter.utils.HttpConnectionPool;
import com.panda.multiterminalinteractivecenter.vo.MongoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author :  ifan
 * @Description :  mongo发消息
 * @Date: 2022-07-4
 */
@Slf4j
@Component
@RefreshScope
@Service
public class MongoServiceImpl implements IMongoService {
    @Autowired
    private MultiterminalConfig config;

    @Override
    @Async
    public void send(String text, String name, int sendMongoSwitch, String userId, String userToken) {
        try {
            sendMongeMessage(text, name, sendMongoSwitch, userId, userToken);
        } catch (Exception e) {
            log.error(text + "MongoServiceImpl.send", e);
        }
    }

    private void sendMongeMessage(String text, String targetName, int sendMongoSwitch, String userId, String userToken) {
        log.info("sendMongeMessage,text = {}, targetName = {}", text, targetName);
        MongoVO object = new MongoVO();
        object.setChatType(2);
        object.setModel(0);
        String url = "https://trobot.ymtio.com/api/robot/user" + userId + ":" + userToken + "/sendmessage_v2";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "eXwdrXrvrjsHDs7F");

        if (sendMongoSwitch == 0) {
            object.setText(text);
        } else {
            log.info("sendMongeMessage,消息被过滤掉了 text = {}", text);
            return;
        }
        object.setTargetname(targetName);
        try {
            ResponseEntity<String>  response = HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(object, headers), String.class);
            HttpStatus httpStatus = response.getStatusCode();
            if (200 == httpStatus.OK.value()) {
                log.info("sendMongeMessage发送成功ok,返回:{},text = {}, targetName = {}, sendMongoSwitch = {}, userId = {}, userToken = {}", JSON.toJSONString(response), text, targetName, sendMongoSwitch, userId, userToken);
            } else {
                log.error("sendMongeMessage发送失败,请求返回:{},text = {}, targetName = {}, sendMongoSwitch = {}, userId = {}, userToken = {}", JSON.toJSONString(httpStatus), text, targetName, sendMongoSwitch, userId, userToken);
            }
        } catch (Exception e) {
            log.error("sendMongeMessage发送异常,原因：{}", e.getMessage());
        }
    }

    @Override
    @Async
    public void send(String text) {
        sendMongeMsg(text);
    }

    private void sendMongeMsg(String text) {
        log.info("text = {}, targetname = {}", text, config.getTargetName());
        MongoVO object = new MongoVO();
        object.setChatType(2);
        object.setModel(0);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "eXwdrXrvrjsHDs7F");

        if (config.getSendMongoSwitch() == 0) {
            object.setText(text);
        } else {
            log.info("sendMongeMsg消息被过滤掉了 text = {}", text);
            return;
        }
        object.setTargetname(config.getTargetName());
        try {
            String url = "https://trobot.ymtio.com/api/robot/user" + config.getUserId() + ":" + config.getUserToken() + "/sendmessage_v2";
            ResponseEntity<String>  response  = HttpConnectionPool.restTemplate.postForEntity(url, new HttpEntity<>(object, headers), String.class);
            HttpStatus httpStatus = response.getStatusCode();
            if (200 == httpStatus.OK.value()) {
                log.info("sendMongeMsg发送成功ok,text = {},response:{} ", text, JSON.toJSONString(response));
            } else {
                log.error("sendMongeMsg发送失败,text = {},response:{}", text,JSON.toJSONString(httpStatus));
            }
        } catch (Exception e) {
            log.error("sendMongeMsg发送异常！", e);
        }
    }
}
