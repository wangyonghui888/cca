package com.panda.multiterminalinteractivecenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.entity.Oss;
import com.panda.multiterminalinteractivecenter.entity.WSResponse;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.service.IMongoService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.WebSocketTransfer;
import com.panda.multiterminalinteractivecenter.utils.TelegramBot;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Oss 服务实现类
 * </p>
 *
 * @author ifan
 * @since 2022-08-29
 */

@Slf4j
@Service("ossDomain")
public class OssDomainWebSocketServiceImpl implements WebSocketTransfer<WebSocketClient> {

    @Autowired
    private OssDomainProcessor ossDomainProcessor;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private IMongoService mongoService;

    @Autowired
    private TelegramBot telegramBot;


    @Override
    public void sendWebSocketMessage(WebSocketClient webSocketClient, Oss oss, String domainName, Integer domainType, String testType) {
        try {
            Integer txnid = Integer.parseInt((System.currentTimeMillis() / 10 + "").substring(6));
            LocalCacheService.OSS_DOMAIN_MAP.put(DomainConstants.OSS_DOMAIN + txnid, oss);

            log.info("checkOssDomain:" + domainName + ",LocalCacheService.domainMap=" + LocalCacheService.OSS_DOMAIN_MAP.estimatedSize());
            ossDomainProcessor.sendWebSocket(webSocketClient, txnid , domainName,  testType);
        } catch (Exception e) {
            log.error("17ce检测sendWebSocketMessage:", e);
        }
    }

    @Async
    public void processOssDomainWSMessage(WSResponse wsResponse) {
        Integer rt = wsResponse.getRt();
        Integer txnid = wsResponse.getTxnid();
        Oss oss = LocalCacheService.OSS_DOMAIN_MAP.getIfPresent(DomainConstants.OSS_DOMAIN + txnid);
        if(Objects.isNull(oss)){
            log.error("{}此次17ce检测OSS动态域名为空!!!>>>{}",txnid,wsResponse.getError());
            return;
        }

        log.info(txnid + "processOssDomainWSMessage:oss =" + JSON.toJSONString(oss));
        if (rt == 1) {
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            List<String> fieldName = new ArrayList<>();
            fieldName.add("processOssDomainWSMessage:domain check " + txnid);
            vo.setFieldName(fieldName);
            List<String> beforeValues = new ArrayList<>();
            beforeValues.add(wsResponse.getRt() + "");
            beforeValues.add(wsResponse.getError());
            beforeValues.add(wsResponse.getMsg());
            vo.setBeforeValues(beforeValues);
            List<String> afterValues = new ArrayList<>();
            if (wsResponse.getData() != null) {
                afterValues.add(wsResponse.getData().toString());
            }
            vo.setAfterValues(afterValues);
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHECK_MERCHANT_Domian, vo,
                    MerchantLogConstants.MERCHANT_IN, "999999999999999", "System", "SYSTEM", "SYSTEM", "正常", "正常", "9999999", "-","通过",oss.getDomainType());
            String text = txnid + ",此次17ce检测OSS动态域名正常:【{"+ oss.getDomainName() +"}】";
            mongoService.send(text);
            telegramBot.sendGroupMessage(text);

        }else if (rt == 10010) {
            log.error("17ce检测OSS动态域名存在无效域名,{}!!!",oss.getDomainName());
            return;

        }else if (rt == 10008 ) {
            log.error("17ce检测OSS动态域名,auth err认证失败,{}!!!",oss.getDomainName());
            return;

        }else {
            if (StringUtils.isEmpty(oss.getDomainName())) {
                log.error("此次17ce检测OSS动态域名为空!!!");
                return;
            }

            //检测未通过oss动态域名 放入缓存
            LocalCacheService.FAILED_17CE_OSS_DOMAIN_MAP.put(DomainConstants.OSS_DOMAIN
                    + oss.getGroupType() + oss.getEncryptDomainName(), oss);
        }
    }
}
