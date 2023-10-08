package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.dto.KickUserDto;
import com.panda.multiterminalinteractivecenter.dto.MaintainRecordDto;
import com.panda.multiterminalinteractivecenter.utils.HttpClientUtil;
import com.panda.multiterminalinteractivecenter.utils.HttpConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-29 13:06:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component
@Slf4j
@RefreshScope
public class LotterySendServiceImpl {

    @Value("${cp.merchant.url:http://test-mk-sabang-api.emkcp.com}")
    private String baseUrl;

    @Autowired
    private MultiterminalConfig config;

/*    public static void main(String[] args) throws IOException {
        KickUserDto kickUserDto = new KickUserDto();
        kickUserDto.setSystemCode("1");
        kickUserDto.setType(2);
        kickUserDto.setUserIds("222222,11111");
        LotterySendServiceImpl cpSendService = new LotterySendServiceImpl();
        cpSendService.sendKickUser(kickUserDto, "/sabang/pulic/kickUser");
    }*/

    public void sendKickUser(KickUserDto kickUserDto, String url) throws IOException {
        try {
            String jsonStr = JSONUtil.toJsonStr(kickUserDto);
//            CloseableHttpClient httpClient = HttpClientUtil.getSystemHttpClient(config.getHttpProxySwitch());
//            HttpPost httpPost = new HttpPost(baseUrl+url);
//            StringEntity paramEntity = new StringEntity(JSON.toJSONString(jsonStr),"UTF-8");
//            httpPost.setEntity(paramEntity);
//            httpPost.setHeader("clientId", "internal service");
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//            log.info("调用彩票踢出用户API调用{}--> result:【{}】",baseUrl + url,response);
//            if(response.getStatusLine().getStatusCode()== HttpStatus.OK.value()){
//                log.info("调用彩票踢出用户API成功！ param={} result={}",JSON.toJSONString(kickUserDto),msg);
//            }else{
//                log.error("调用彩票踢出用户API异常！ param={} result={}",JSON.toJSONString(kickUserDto),msg);
//                throw new RuntimeException("调用彩票踢出用户API异常!");
//            }


            HttpHeaders headers = new HttpHeaders();
            headers.set("clientId", "internal service");
            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(baseUrl+url, new HttpEntity<>(jsonStr, headers),String.class);

            HttpStatus httpStatus = response.getStatusCode();

            if (200 == httpStatus.OK.value()) {
                log.info("调用彩票踢出用户API成功！ param={} result={}",JSON.toJSONString(kickUserDto),response.getBody());
            }else{
                log.error("调用彩票踢出用户API异常！ param={} result={}",JSON.toJSONString(kickUserDto),response.getBody());
                throw new RuntimeException("调用彩票踢出用户API异常!");
            }
        } catch (Exception e) {
            log.error("调用彩票踢出用户API异常！ param={}",JSON.toJSONString(kickUserDto), e);
            throw e;
        }
    }

    public void MaintainRemindAndNotice(MaintainRecordDto maintainRecordDto, String url) throws IOException {
        try {
            String jsonStr = JSONUtil.toJsonStr(maintainRecordDto);

//            CloseableHttpClient httpClient = HttpClientUtil.getSystemHttpClient(config.getHttpProxySwitch());
//            HttpPost httpPost = new HttpPost(baseUrl+url);
//            StringEntity paramEntity = new StringEntity(JSON.toJSONString(jsonStr),"UTF-8");
//            httpPost.setEntity(paramEntity);
//            httpPost.setHeader("Content-Type", "application/json");
//            httpPost.setHeader("clientId", "internal service");
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
//            log.info("调用彩票公告API{}--> result:【{}】",url,response.toString());
//            if(response.getStatusLine().getStatusCode()== HttpStatus.OK.value()){
//                log.info("调用彩票公告API成功！ param={} result={}",JSON.toJSONString(maintainRecordDto),msg);
//            }else{
//                log.error("调用彩票公告API异常！ param={} result={}",JSON.toJSONString(maintainRecordDto),msg);
//            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("clientId", "internal service");

            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(baseUrl + url, new HttpEntity<>(jsonStr, headers),String.class);

            HttpStatus httpStatus = response.getStatusCode();

            log.info("调用彩票公告API{}--> result:【{}】",url,response.getBody());
            if (200 == httpStatus.OK.value()) {
                log.info("调用彩票公告API成功！ param={} result={}",JSON.toJSONString(maintainRecordDto),response.getBody());
            }else{
                log.error("调用彩票公告API异常！ param={} result={}",JSON.toJSONString(maintainRecordDto),response.getBody());
            }

        } catch (Exception e) {
            log.error("调用彩票公告API异常！ param={}",JSON.toJSONString(maintainRecordDto), e);
        }
    }


}
