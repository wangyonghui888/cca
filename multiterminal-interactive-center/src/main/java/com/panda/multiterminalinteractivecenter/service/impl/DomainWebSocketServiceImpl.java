package com.panda.multiterminalinteractivecenter.service.impl;

import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.entity.Oss;
import com.panda.multiterminalinteractivecenter.entity.WSResponse;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.DomainMapper;
import com.panda.multiterminalinteractivecenter.service.IMongoService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.service.WebSocketTransfer;
import com.panda.multiterminalinteractivecenter.utils.TelegramBot;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * Oss 服务实现类
 * </p>
 *
 * @author ifan
 * @since 2022-08-29
 */

@Slf4j
@Service("domain")
public class DomainWebSocketServiceImpl implements WebSocketTransfer<WebSocketClient> {
    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private IMongoService mongoService;

    @Autowired
    private DomainMapper domainMapper;

    @Autowired
    private MerchantManageClient merchantManageClient;

    @Autowired
    private MerchantApiClient merchantApiClient;
    @Autowired
    private OssDomainProcessor ossDomainProcessor;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private MultiterminalConfig config;


    @Override
    public void sendWebSocketMessage(WebSocketClient webSocketClient,Oss oss, String domainName, Integer domainType, String testType) {
            try {
                Integer txnid = Integer.parseInt((System.currentTimeMillis() / 10 + "").substring(6));
                LocalCacheService.DOMAIN_MAP.put(DomainConstants.DOMAIN + txnid, domainName + ";" + testType + ";" + domainType + ";" + DomainConstants.DOMAIN);
                ossDomainProcessor.sendWebSocket(webSocketClient, txnid , domainName ,  testType);
                log.info("checkDomain:" + domainName + ",LocalCacheService.domainMap=" + LocalCacheService.DOMAIN_MAP.estimatedSize());
            } catch (Exception e) {
                log.error("sendWebSocketMessage:", e);
            }

    }

    @Async
    public void processWSMessage(WSResponse wsResponse) {
        Integer rt = wsResponse.getRt();
        Integer txnid = wsResponse.getTxnid();
        String domainStr = LocalCacheService.DOMAIN_MAP.getIfPresent(DomainConstants.DOMAIN + txnid);

        if(StringUtils.isEmpty(domainStr)){
            return;
        }

        String domainName = domainStr.split(";")[0];
        String testType = domainStr.split(";")[1];
        String domainType = domainStr.split(";")[2];

        log.info(txnid + ",processWSMessage:domainStr =" + domainStr);

        if (rt != 1 && rt != 10010) {
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            List<String> fieldName = new ArrayList<>();
            fieldName.add("processWSMessage:domain check " + txnid);
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
                    MerchantLogConstants.MERCHANT_IN, "999999999999999", "System", "SYSTEM", "SYSTEM", "正常", "正常", "9999999", "-","通过",Integer.parseInt(domainType));
        } else if (rt == 10010) {
            String text = "存在无效域名,请检查!";
            mongoService.send(text);
            telegramBot.sendGroupMessage(text);
            if (StringUtils.isEmpty(domainType)) {
                log.error("此次检测域名为空!!!");
                return;
            }
//            domainMapper.updateDomainEnable(3, domainName);
        }else if (rt == 10008) {
            log.error("17ce检测OSS动态域名,auth err认证失败,{}!!!",domainName);
            return;

        } else {
            if (StringUtils.isEmpty(domainType)) {
                log.error("此次检测域名为空!!!");
                return;
            }
            log.info(txnid + ",processWSMessage:domainName=" + domainName);
            WSResponse.Result data = wsResponse.getData();
            String type = wsResponse.getType();
            CopyOnWriteArrayList<Double> timeCostList = (CopyOnWriteArrayList<Double>) LocalCacheService.DOMAIN_COUNT_MAP.getIfPresent(DomainConstants.DOMAIN + txnid);
            List<String> ipPool = this.getIpPool();
            if (type != null && type.equalsIgnoreCase("NewData") && data != null && data.getTotalTime() != null && testType.equalsIgnoreCase("HTTP")) {
                Double totalTime = data.getTotalTime();
                if (timeCostList != null) {
                    timeCostList.add(totalTime);
                } else {
                    timeCostList = new CopyOnWriteArrayList<>();
                }
                LocalCacheService.DOMAIN_COUNT_MAP.put(DomainConstants.DOMAIN + txnid, timeCostList);
            } else if (type != null && type.equalsIgnoreCase("NewData") && testType.equalsIgnoreCase("DNS") && CollectionUtils.isNotEmpty(ipPool) && data != null && data.getSrcIP() != null) {
                String scrIp = data.getSrcIP().replaceAll(" ", "").replace(";", "").replace(";", "");
                log.info("DNS:" + txnid + ",domainName=" + domainName + ",scrIp=" + scrIp);
                if (ipPool.contains(scrIp)) {
                    LocalCacheService.DOMAIN_MAP.invalidate(DomainConstants.DOMAIN + txnid);
//                    domainMapper.updateDomainEnable(4, domainName);
                    log.info(txnid + "," + domainName + "DNS失效域名池域名池成功!");
                    Map<String, String> newDomainMap = domainMapper.getAvailableDomainByGroup(domainName);
                    String newDomain = newDomainMap == null ? null : newDomainMap.get("domainName");
                    String groupName = newDomainMap == null ? "SYSTEM" : newDomainMap.get("groupName");
                    if (StringUtils.isEmpty(newDomain)) {
                        mongoService.send(txnid + ",商户组域名池没有可用域名!!!无效域名:" + domainName);
                        telegramBot.sendGroupMessage(txnid + ",商户组域名池没有可用域名!!!无效域名:" + domainName);
                        return;
                        //newDomain = tDomainMapper.getAvailableDomain();
                    }
                    log.info(txnid + "DNS查询可切换域名:" + domainName + "---->newDomain:" + newDomain);
                    if (StringUtils.isNotEmpty(newDomain)) {
                        log.info(txnid + "," + domainName + "DNS切换域名开始,newDomain:" + newDomain);
//                        merchantApiClient.updateNewDomain(domainName, Integer.parseInt(domainType), newDomain);

                        log.info(DomainConstants.DOMAIN + txnid + "失效成功!");
//                        merchantApiClient.kickoutMerchant(null);
                        MerchantLogFiledVO vo = new MerchantLogFiledVO();
                        List<String> fieldName = new ArrayList<>();
                        fieldName.add("domain check" + txnid);
                        vo.setFieldName(fieldName);
                        List<String> beforeValues = new ArrayList<>();
                        beforeValues.add(domainName);
                        vo.setBeforeValues(beforeValues);
                        List<String> afterValues = new ArrayList<>();
                        afterValues.add(newDomain);
                        vo.setAfterValues(afterValues);
                        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHANGE_MERCHANT_Domian, vo,
                                MerchantLogConstants.MERCHANT_IN, "999999999999999", groupName, groupName, groupName, "正常", "被攻击", "9999999", "-","被攻击",Integer.parseInt(domainType));
                        String text = txnid + ",DNS检测失败:" + scrIp + ",域名开始切换:" + domainName + "---->" + newDomain;
                        mongoService.send(text);
                        telegramBot.sendGroupMessage(text);
//                        domainMapper.updateDomainEnable(4, domainName);
                    } else {
                        log.error("域名池已空,请立即添加域名!!!!!!!!!!!");
                    }
                }
            }
            if (StringUtils.isNotEmpty(type) && type.equalsIgnoreCase("TaskEnd") && testType.equalsIgnoreCase("HTTP")) {
                log.info(txnid + "," + domainName + "," + wsResponse.getData());

                MathContext mc = new MathContext(2, RoundingMode.HALF_DOWN); //必须设置精度
                BigDecimal threshold = new BigDecimal(config.getThreshold());
                BigDecimal nodes = new BigDecimal(config.getNodes());
                double limit = threshold.divide(nodes,mc).doubleValue() ;

                double f1 = 0;
                if (CollectionUtils.isNotEmpty(timeCostList)) {
                    List<Double> timeoutList = new ArrayList<>();
                    for (Double cost : timeCostList) {
                        if (cost > 3) {
                            timeoutList.add(cost);
                        }
                    }
                    int timeOutSize = timeoutList.size(), timeCostSize = timeCostList.size();
                    log.info(testType + "," + domainName + ",timeOutSize=" + timeOutSize + ",timeCostSize=" + timeCostSize);
                    f1 = new BigDecimal((float) timeOutSize / timeCostSize).setScale(2, RoundingMode.HALF_UP).doubleValue();
                }
                if ((CollectionUtils.isEmpty(timeCostList) || f1 >= limit)) {
                    log.info(txnid + "," + domainName + "失效域名开始,超时PING率:" + f1);
//                    String newDomain = ossDomainProcessor.switchOssDomain(txnid, domainName, "17ce HTTP","ty",Integer.parseInt(domainType),null);
//                    if (newDomain != null) {
//                        String text = txnid + ",17ce域名检测失败,无效率=" + f1 + ",阈值=" + limit + ",域名开始切换,老域名" + domainName + ",新域名" + newDomain + ".共" +
//                                (CollectionUtils.isEmpty(timeCostList) ? 0 : timeCostList.size()) + "个测试点返回数据";
//                        mongoService.send(text);
//                        telegramBot.sendGroupMessage(text);
//                    }
                    String text = String.format("17ce域名自检发现此域名可能被攻击,请及时检查：domain:【%s】", domainName);
                    mongoService.send(text);
                } else {
                    int count = merchantManageClient.checkMerchantDomainExistByDomainType(domainName, Integer.parseInt(domainType));
                    log.info(txnid + ",processWSMessage:checkMerchantDomainExistByDomainType:" + count);
//                    if (count > 0) {
//                        domainMapper.updateDomainEnable(1, domainName);
//                    } else {
//                        domainMapper.updateDomainEnable(2, domainName);
//                    }
                }
            }
        }
    }

    private List<String> getIpPool() {
/*        List<String> ipList = (List<String>) dnsIpPoolMap.getIfPresent(DOMAIN_DNS);
        if (CollectionUtils.isEmpty(ipList)) {
            ipList = tDomainMapper.getIpPool();
            dnsIpPoolMap.put(DOMAIN_DNS, ipList);
        }
        return ipList;*/
        return null;
    }
}
