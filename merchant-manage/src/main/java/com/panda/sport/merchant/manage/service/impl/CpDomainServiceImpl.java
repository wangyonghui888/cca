package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.panda.sport.cp.mapper.TCpMerchantMapper;
import com.panda.sport.merchant.common.vo.ThirdMerchantVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
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
@Component(value = "CpDomainServiceImpl")
@Slf4j
@RefreshScope
public class CpDomainServiceImpl extends DomainAbstractService{

    @Autowired
    private TCpMerchantMapper cpMerchantMapper;

    @Value("${merchant.cp.url:http://test-admin.emkcp.com/sabang/merchant/replaceStaticSite}")
    private String configUrl;

    @Override
    public List<ThirdMerchantVo> getMerchantList() {
        List<ThirdMerchantVo> list = cpMerchantMapper.getMerchantList();
        for (ThirdMerchantVo vo : list){
            vo.setCreatTime(vo.getCreatedAt().getTime() / 1000);
        }
        return list;
    }

    @Override
    public void sendMsg(String merchantCode, Integer domainType, String url,Integer changeType) {
        try {
            PostMethod  postMethod= new PostMethod(configUrl + "?merchantAccount="+merchantCode+"&type="+domainType+"&url="+url) ;
            postMethod.setRequestHeader("clientId","internal service");
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString() ;
            if (response == 200) {
                mongoService.send("彩票域名切换成功！merchantCode =" + merchantCode + ",type=" + domainType + ",url=" + url + ",msg=" + result+ " 请求地址="+configUrl);
            }else {
                mongoService.send("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ result + " 请求地址="+configUrl);
            }
        } catch (Exception e) {
            log.error("请求异常"+e.getMessage() +"param=" + url,e);
            mongoService.send("彩票发生切换消息异常！merchantCode ="+merchantCode+",type="+domainType+",url="+url+",msg="+ e.getMessage()+ " 请求地址="+configUrl);
        }
    }
    public static void main(String[] args) {
        try {
            PostMethod  postMethod= new PostMethod("http://test-admin.emkcp.com/sabang/merchant/replaceStaticSite?merchantAccount=bob&type=2&url=http://merchant-manage-pc.google1.com/:888") ;
            postMethod.setRequestHeader("clientId","internal service");
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString() ;
            System.out.println(result);
            System.out.println(JSON.toJSONString(response));
            if (response == 200) {
                System.out.println("彩票域名切换成功！merchantCode msg=" + result);
            }else {
                System.out.println("彩票发生切换消息异常！msg="+ result);
            }
        } catch (Exception e) {
            log.info("请求异常"+e.getMessage(),e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
