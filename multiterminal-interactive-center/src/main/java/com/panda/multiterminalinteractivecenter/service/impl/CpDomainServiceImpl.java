package com.panda.multiterminalinteractivecenter.service.impl;

import com.panda.multiterminalinteractivecenter.service.ICpDomainService;
import com.panda.multiterminalinteractivecenter.service.IMongoService;
import com.panda.multiterminalinteractivecenter.utils.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@RefreshScope
@Service
public class CpDomainServiceImpl implements ICpDomainService {

    @Value("${merchant.cp.url:http://test-admin.emkcp.com/sabang/merchant/replaceStaticSite}")
    private String configUrl;

    @Value("${merchant.cp.url:http://test-admin.emkcp.com/sabang/merchant/v2/replaceStaticSite}")
    private String configUrlV2;

    @Autowired
    private IMongoService mongoService;

    @Autowired
    private TelegramBot telegramBot;


    @Override
    public void sendMsg(String merchantCode, Integer domainType, String url) {
        try {
            PostMethod postMethod= new PostMethod(configUrl + "?merchantAccount="+merchantCode+"&type="+domainType+"&url="+url) ;
            postMethod.setRequestHeader("clientId","internal service");
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString() ;
            if (response == 200) {
                mongoService.send("彩票域名切换成功！merchantCode =" + merchantCode + ",type=" + domainType + ",url=" + url + ",msg=" + result+ " 请求地址="+configUrl);
                telegramBot.sendGroupMessage("彩票域名切换成功！merchantCode =" + merchantCode + ",type=" + domainType + ",url=" + url + ",msg=" + result+ " 请求地址="+configUrl);
            }else {
                mongoService.send("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ result + " 请求地址="+configUrl);
                telegramBot.sendGroupMessage("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ result + " 请求地址="+configUrl);
            }
        } catch (Exception e) {
            log.error("请求异常"+e.getMessage() +"param=" + url,e);
            mongoService.send("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ e.getMessage()+ " 请求地址="+configUrl);
            telegramBot.sendGroupMessage("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ e.getMessage()+ " 请求地址="+configUrl);
        }
    }

    @Override
    public void sendMsgV2(String merchantCode, Integer domainType, String url, int isVip, String ipArea) {
        try {
            PostMethod postMethod= new PostMethod(configUrl + "?merchantAccount="+merchantCode+"&type="+domainType+"&url="+url+"&isVip="+isVip+"&ipArea="+ipArea) ;
            postMethod.setRequestHeader("clientId","internal service");
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString() ;
            if (response == 200) {
                mongoService.send("彩票域名切换成功！merchantCode =" + merchantCode + ",type=" + domainType + ",url=" + url + ",isVip=" + isVip + ",ipArea=" + ipArea + ",msg=" + result+ " 请求地址="+configUrl);
                telegramBot.sendGroupMessage("彩票域名切换成功！merchantCode =" + merchantCode + ",type=" + domainType + ",url=" + url + ",isVip=" + isVip + ",ipArea=" + ipArea + ",msg=" + result+ " 请求地址="+configUrl);
            }else {
                mongoService.send("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+ ",isVip=" + isVip + ",ipArea=" + ipArea +",msg="+ result + " 请求地址="+configUrl);
                telegramBot.sendGroupMessage("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+ ",isVip=" + isVip + ",ipArea=" + ipArea +",msg="+ result + " 请求地址="+configUrl);
            }
        } catch (Exception e) {
            log.error("请求异常"+e.getMessage() +"param=" + url,e);
            mongoService.send("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+ ",isVip=" + isVip + ",ipArea=" + ipArea +",msg="+ e.getMessage()+ " 请求地址="+configUrl);
            telegramBot.sendGroupMessage("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+ ",isVip=" + isVip + ",ipArea=" + ipArea +",msg="+ e.getMessage()+ " 请求地址="+configUrl);
        }
    }
}
