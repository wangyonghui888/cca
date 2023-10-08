package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.vo.ThirdMerchantVo;
import com.panda.sport.trader.mapper.ThirdMerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  TODO
 * @Date: 2021-12-30 13:17:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Component(value = "DjDomainServiceImpl")
@Slf4j
@RefreshScope
public class DjDomainServiceImpl extends DomainAbstractService{

    @Value("${merchant.dj.url:http://www.phiqui.com/v1/domain/update}")
    private String configUrl;

    @Autowired
    private ThirdMerchantMapper thirdMerchantMapper;

    @Override
    public List<ThirdMerchantVo> getMerchantList() {
        return thirdMerchantMapper.getMerchantList();
    }

    @Override
    public void sendMsg(String merchantCode, Integer domainType, String url,Integer changeType) {
        try {
            PostMethod postMethod = new PostMethod(configUrl) ;
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
            NameValuePair[] data = {
                    new NameValuePair("merchantAccount",merchantCode),
                    new NameValuePair("type",domainType+""),
                    new NameValuePair(",Integer changeType", changeType.toString()),
                    new NameValuePair("url",url)
            };
            postMethod.setRequestBody(data);
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString() ;
            if (response == 200) {
                mongoService.send("电竞域名切换成功！merchantCode =" + merchantCode + ",type=" + domainType + ",url=" + url + ",msg=" + result + " 请求地址="+configUrl);
            }else {
                mongoService.send("电竞发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ result + " 请求地址="+configUrl);
            }
        } catch (Exception e) {
            log.error("请求异常"+e.getMessage() +"param=" + url,e);
            mongoService.send("电竞发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ e.getMessage()+ " 请求地址="+configUrl);
        }
    }

    public static void main(String[] args) {
        try {
            PostMethod postMethod = new PostMethod("http://www.phiqui.com/v1/domain/update") ;
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
            NameValuePair[] data = {
                    new NameValuePair("merchantAccount","bob"),
                    new NameValuePair("type","1"),
                    new NameValuePair("url","http://h5.zxjlbvip.org")
            };
            postMethod.setRequestBody(data);
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString() ;
            System.out.println(result);
            System.out.println(JSON.toJSONString(response));
        } catch (Exception e) {
            log.info("请求异常"+e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }



}
