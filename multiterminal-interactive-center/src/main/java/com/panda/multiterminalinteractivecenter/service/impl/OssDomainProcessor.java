package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.entity.*;
import com.panda.multiterminalinteractivecenter.enums.DomainEnableEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.feign.MerchantManageClient;
import com.panda.multiterminalinteractivecenter.mapper.DomainMapper;
import com.panda.multiterminalinteractivecenter.mapper.TyDomainMapper;
import com.panda.multiterminalinteractivecenter.service.*;
import com.panda.multiterminalinteractivecenter.utils.OssUtil;
import com.panda.multiterminalinteractivecenter.utils.SpringUtil;
import com.panda.multiterminalinteractivecenter.utils.TelegramBot;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.TDomainVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@RefreshScope
public class OssDomainProcessor {
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
    private Map<String, WebSocketTransfer> webSocketTransfer;

    @Autowired
    private MultiterminalConfig config;

    @Autowired
    private OssService ossService;

    @Autowired
    private TyDomainMapper tyDomainMapper;

    @Autowired
    private TelegramBot telegramBot;


    /*    var postdata = {
        txnid: txnid,
        nodetype: [ 1, 2 ],
        num: 1,
        Url: $('#tx_url').val(),
        TestType: 'HTTP',
        Host: 'baidu.com',
        TimeOut: 20,
        Request: 'GET',
        NoCache: true,
        Speed: 0,
        Cookie: '',
        Trace: false,
        Referer: '',
        UserAgent: 'curl/7.47.0',
        FollowLocation: 2,
        GetMD5: true,
        GetResponseHeader: true,
        MaxDown: 1048576,
        AutoDecompress: true,
        type: 1,
        isps: [ 1, 2 ],
        pro_ids: '221,49',
        areas: [ 1 ],
        PingSize: 32,
        PingCount: 10
    }*/
    public void sendWebSocket(WebSocketClient webSocketClient, Integer txnid, String domainName, String testType) {
        try {
            JSONObject jsonObject = new JSONObject();
            String nodetype = "1,2,3,4";
            Integer num = 1;
            Integer TimeOut = 20;
            String Request = "GET";
            Boolean NoCache = true;
            Integer type = 1;
            String isps = "1,2,6,7,8,17,18,19";
            String areas = "1,2";
            jsonObject.put("txnid", txnid);
            jsonObject.put("nodetype", nodetype);
            jsonObject.put("num", num);
            jsonObject.put("Url", domainName);
            String host = "";
            if (testType.equalsIgnoreCase("DNS")) {
                host = domainName.replace("https://", "");
            }
            jsonObject.put("Host", host);
            jsonObject.put("TestType", testType);
            jsonObject.put("TimeOut", TimeOut);
            jsonObject.put("Request", Request);
            jsonObject.put("NoCache", NoCache);
            jsonObject.put("Cookie", "");
            jsonObject.put("Speed", 0);
            jsonObject.put("Trace", false);
            jsonObject.put("UserAgent", "curl/7.47.0");
            jsonObject.put("type", type);
            jsonObject.put("GetMD5", true);
            jsonObject.put("isps", isps);
            jsonObject.put("FollowLocation", 3);
            jsonObject.put("GetResponseHeader", true);
            jsonObject.put("MaxDown", 1048576);
            jsonObject.put("areas", areas);
            jsonObject.put("pro_ids", config.getProvinceId());
            //        jsonObject.put("city_ids", config.getCityId());
            jsonObject.put("AutoDecompress", false);
            jsonObject.put("PingCount", 10);
            jsonObject.put("PingSize", 32);
            log.info("sendWebSocketMessage:" + jsonObject);
            webSocketClient.send(jsonObject.toJSONString());
        } catch (Exception ex) {
            log.error("webSocket发送失败:", ex);
        }
    }


    public String switchOssDomain(Integer txnid, String domain, String type, String code, Integer domainType, Long groupIdNew) {
        if ("ty".equalsIgnoreCase(code)) {
            domainMapper.updateDomainEnable(3, domain);
            Map<String, String> newDomainMap = domainMapper.getAvailableDomainByGroup(domain);
            String newDomain = newDomainMap == null ? null : newDomainMap.get("domainName");
            String groupName = newDomainMap == null ? "SYSTEM" : newDomainMap.get("groupName");
            if (StringUtils.isEmpty(newDomain)) {
                mongoService.send(txnid + type + ",商户组域名池没有可用域名!!!无效域名:" + domain);
                telegramBot.sendGroupMessage(txnid + type + ",商户组域名池没有可用域名!!!无效域名:" + domain);
                return null;
            }
            log.info(txnid + type + "可切换域名:" + domain + "---->newDomain:" + newDomain);
            if (StringUtils.isNotEmpty(newDomain)) {
                log.info(txnid + "," + domain + "切换域名开始,newDomain:" + newDomain);
                merchantApiClient.updateNewDomain(domain, domainType, newDomain);
                domainMapper.updateDomainEnable(1, newDomain);
                merchantApiClient.kickoutMerchant(null);
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                List<String> fieldName = new ArrayList<>();
                fieldName.add("domain check" + txnid);
                vo.setFieldName(fieldName);
                List<String> beforeValues = new ArrayList<>();
                beforeValues.add(domain);
                vo.setBeforeValues(beforeValues);
                List<String> afterValues = new ArrayList<>();
                afterValues.add(newDomain);
                vo.setAfterValues(afterValues);
                if ("selfTest".equals(type)) {
                    merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHANGE_MERCHANT_Domian, vo,
                            MerchantLogConstants.MERCHANT_IN, "999999999999999", groupName, groupName, groupName,
                            "9999999", "en", null, "被攻击", "-", domainType);
                } else {
                    merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHANGE_MERCHANT_Domian, vo,
                            MerchantLogConstants.MERCHANT_IN, "999999999999999", groupName, groupName, groupName,
                            "9999999", "en", null, "-", "被攻击", domainType);
                }
                return newDomain;
            } else {
                log.error("域名池已空,请立即添加域名!!!!!!!!!!!");
            }
        } else {
            DomainAbstractService domainAbstractService = null;
            MerchantLogTypeEnum merchantLogTypeEnum = null;
            if ("dj".equalsIgnoreCase(code)) {
                //电竞
                merchantLogTypeEnum = MerchantLogTypeEnum.CHANGE_MERCHANT_DJ_Domian;
                domainAbstractService = (DomainAbstractService) SpringUtil.getBean("DjDomainServiceImpl");
            } else if ("cp".equalsIgnoreCase(code)) {
                merchantLogTypeEnum = MerchantLogTypeEnum.CHANGE_MERCHANT_CP_Domian;
                domainAbstractService = (DomainAbstractService) SpringUtil.getBean("CpDomainServiceImpl");
            } else {
                log.info("未配置！");
            }
            if (domainAbstractService == null) {
                log.error("数据配置异常！");
                return null;
            }
            TDomainVo domainVoOld = domainMapper.getTypeByDomainName(domain, groupIdNew);
            Integer oldDomainType = domainVoOld.getDomainType();
            Long groupId = domainVoOld.getMerchantGroupId();
            TDomainVo domainVo = domainMapper.getDomainByTypeAndGroupId(oldDomainType, groupId);
            if (domainVo == null) {
                mongoService.send(txnid + type + "," + code + "商户组域名池没有可用域名!!!无效域名:" + domain);
                telegramBot.sendGroupMessage(txnid + type + "," + code + "商户组域名池没有可用域名!!!无效域名:" + domain);
                return null;
            }
            domainMapper.updateByDomainTypeAndName(3, oldDomainType, domain, groupId, System.currentTimeMillis());
            domainMapper.updateByDomainTypeAndName(1, oldDomainType, domainVo.getDomainName(), groupId, System.currentTimeMillis());
            APIResponse merchants = merchantManageClient.merchantGroupInfoByGroupId(groupId);

            if (null != merchants && Objects.nonNull(merchants.getData())) {
                ObjectMapper mapper = new ObjectMapper();
                List<TMerchantGroupInfo> merchantList = mapper.convertValue(merchants.getData(), new TypeReference<List<TMerchantGroupInfo>>() {
                });
                for (TMerchantGroupInfo merchant : merchantList) {
                    domainAbstractService.sendMsg(merchant.getMerchantName(), oldDomainType, domainVo.getDomainName());
                }
            }


            APIResponse groupNames = merchantManageClient.load(groupId.intValue());
            String groupName = "";
            if (null != groupNames && Objects.nonNull(groupNames.getData())) {
                ObjectMapper mapper = new ObjectMapper();
                TMerchantGroup merchant = mapper.convertValue(merchants.getData(), new TypeReference<TMerchantGroup>() {
                });
                groupName = merchant.getGroupName();
            }
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            List<String> fieldName = new ArrayList<>();
            fieldName.add("domain check" + txnid);
            vo.setFieldName(fieldName);
            List<String> beforeValues = new ArrayList<>();
            beforeValues.add(domain);
            vo.setBeforeValues(beforeValues);
            List<String> afterValues = new ArrayList<>();
            afterValues.add(domainVo.getDomainName());
            vo.setAfterValues(afterValues);
            if ("selfTest".equals(type)) {
                merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, merchantLogTypeEnum, vo,
                        MerchantLogConstants.MERCHANT_IN, "999999999999999", "system", groupName, groupName,
                        "9999999", "en", null, "被攻击", "-", oldDomainType);
            } else {
                merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, merchantLogTypeEnum, vo,
                        MerchantLogConstants.MERCHANT_IN, "999999999999999", "system", groupName, groupName,
                        "9999999", "en", null, "-", "被攻击", oldDomainType);
            }
            return domainVo.getDomainName();
        }
        return null;
    }

    /**
     * 17ce检测域名
     *
     * @param isOssDomain 区分域名池域名还是oss文件域名
     */
    public void check17ceDomain(Object object, Oss oss, String domainName, Integer domainType, String isOssDomain) {

        log.info("check17ceDomain域名池检测开始!");
        WebSocketClient webSocket = (WebSocketClient) object;
        long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(domainName)) {
            domainName = oss.getDomainName();
        }
        try {
            if (config.getWebsocketSwitch()) {
                WebSocketTransfer transfer = webSocketTransfer.get(isOssDomain);
                if (null != transfer) {
                    transfer.sendWebSocketMessage(webSocket, oss, domainName, domainType, "HTTP");
                    Thread.sleep(500);
                }
            }
            if (config.getDnsSwitch()) {
                Date now = new Date();
                String tempDate = DateFormatUtils.format(now, "yyyy-MM-dd HH:mm");
                log.info("DNS时间:" + tempDate);
                if (tempDate.endsWith("0") || tempDate.endsWith("4") || tempDate.endsWith("7")) {
                    WebSocketTransfer transfer = webSocketTransfer.get(isOssDomain);
                    if (null != transfer) {
                        transfer.sendWebSocketMessage(webSocket, oss, domainName, domainType, "DNS");
                        Thread.sleep(400);
                    }
                }
            }
        } catch (Exception e) {
            log.error("check17ceDomain:", e);
        }

        if (config.getWebsocketSwitch() || config.getDnsSwitch()) {
            int waitWs = Integer.parseInt(config.getDalay());
            try {
                Thread.sleep(waitWs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            webSocket.close();
        }
        log.info("check17ceDomain域名检测结束:" + (System.currentTimeMillis() - start));
    }

    public boolean selfTestDomain(String domain, String code, String domainTypeStr) {
        try {
            Mono<String> result = WebClient.create().post().uri(config.getSelfUrl()).contentType(MediaType.APPLICATION_JSON_UTF8).body(BodyInserters.fromObject("{" + "  \"type\" : \"" + code + "\"," + "  \"domain\" : \"" + domain + "\"," + "  \"domaintype\" : \"" + domainTypeStr + "\"" + "}")).retrieve().bodyToMono(String.class);
            log.info("selfTestDomain开始，参数--> domain:【{}】,result:【{}】", domain, result.block());
            JSONObject jsonObject = JSONObject.parseObject(result.block());
            JSONObject data = jsonObject.getJSONObject("data");
            Object obj = data.get("nohealthnodecount");
            log.info("selfTestDomain查询到不健康节点 nohealthnodecount=【{}】", obj);

            MathContext mc = new MathContext(2, RoundingMode.HALF_DOWN); //必须设置精度
            BigDecimal selfCheckThreshold = new BigDecimal(config.getSelfCheckThreshold());
            BigDecimal nodes = new BigDecimal(config.getSelfNodes());
            double threshold = selfCheckThreshold.divide(nodes,mc).doubleValue() ;

            if (Objects.isNull(obj)) {
                return true;
            }
            int count = (int) obj;
            if (count > threshold) {
                log.info("selfTestDomain自检失败!,domain:【{}】,threshold=【{}】", domain, threshold);
                if ("0".equals(config.getSelfCheckSwitch())) {
                    String text = "0000,selfTestDomain自检失败域名测速检测失败,threshold=" + threshold + "第三方域名不自动切换!";
                    mongoService.send(text, config.getTargetName(), config.getSendMongoSwitch(), config.getUserId(), config.getUserToken());
                    telegramBot.sendGroupMessage(text);
                }
                return false;
            } else {
                log.info(domain + "selfTestDomain自检success!");
                return true;
            }
        } catch (Exception e) {
            log.error("CheckInOssDomainTask,selfTestDomain自检域名:" + domain, e);
            return true;
        }
    }


    /**
     * 不可用域名替换并上传
     *
     * @param ossList
     * @param jsonObject
     */
    public void replaceUnavailableDomain(Set<Oss> ossList, JSONObject jsonObject) {

        if (CollectionUtils.isEmpty(ossList)) {
            return;
        }

        try {
            //json转换
            OssDynamicDomain ossDynamicDomain = JSON.toJavaObject(jsonObject, OssDynamicDomain.class);

            ossList.forEach(oss -> {
                //查询体育域名池中域名类型为api及待使用的域名数据放入缓存
                List<String> map = LocalCacheService.OSS_NEW_DOMAIN_MAP.getIfPresent(oss.getGroupType().toString());
                if (CollectionUtil.isEmpty(map)) {
                    //查询ty待使用域名
                    List<TyDomain> domains = domainMapper.findDomainByGroupType();
                    //商户类型分组
                    Map<Integer, List<TyDomain>> tyDomainMap = domains.stream().collect(Collectors.groupingBy(TyDomain::getGroupType));
                    for (Map.Entry<Integer, List<TyDomain>> entry : tyDomainMap.entrySet()) {
                        List<TyDomain> tyDomains = entry.getValue();
                        List<String> stringList = new ArrayList<>();
                        for (int i = 0; i < tyDomains.size(); i++) {
                            stringList.add(tyDomains.get(i).getDomainName());
                        }
                        LocalCacheService.OSS_NEW_DOMAIN_MAP.put(entry.getKey().toString(), stringList);
                    }
                }
                //修改oss动态域名
                updateOssDynamicDomain(oss, ossDynamicDomain);

                //自检旧oss域名不通过，替换之后需要将旧的oss域名入库t_domain_ty
                insertCheckFailedOssDomain(oss);
            });


            //设置当前时间
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ossDynamicDomain.setUpdateTime(simpleDateFormat.format(date));

            //新oss动态域名对象转json
            JSONObject newJsonObject = (JSONObject) JSONObject.toJSON(ossDynamicDomain);

            log.info("更新后oss动态域名:>>>{}", newJsonObject);
            //上传新oss文件
            ossService.ossUpload(newJsonObject);

            LocalCacheService.NEW_JSON_OBJECT.put("newJsonObject", newJsonObject);

        } catch (Exception ex) {
            log.error("OssDynamicDomain上传新oss文件错误:", ex);
        }

    }

    /**
     * 修改oss动态域名
     *
     * @param oss
     * @param ossDynamicDomain
     */
    private void updateOssDynamicDomain(Oss oss, OssDynamicDomain ossDynamicDomain) {
        try {
            if (Objects.equals(oss.getMerchantTypeName(), "GACOMMON")) {
                for (int i = 0; i < ossDynamicDomain.getCommon().getApi().size(); i++) {
                    List<String> str = LocalCacheService.OSS_NEW_DOMAIN_MAP.getIfPresent(oss.getGroupType().toString());
                    //判断缓存域名池数据是否空
                    if (CollectionUtils.isEmpty(str)) {
                        continue;
                    }
                    //设定域名池数据,数组下标越界
                    if (str.size() <= i) {
                        continue;
                    }
                    //获取新域名且加密
                    String encryptStr = OssUtil.encryptAES(str.get(i));
                    oss.setEncryptNewDomain(encryptStr);
                    //判断COMMON检测未通过的域名和加密后oss动态域名比较，且设置加密后的新域名
                    if (Objects.equals(ossDynamicDomain.getCommon().getApi().get(i), oss.getEncryptDomainName())) {
                        ossDynamicDomain.getCommon().getApi().set(i, encryptStr);
                    }
                }
            }

            if (Objects.equals(oss.getMerchantTypeName(), "GAY")) {
                for (int i = 0; i < ossDynamicDomain.getGay().getApi().size(); i++) {
                    List<String> str = LocalCacheService.OSS_NEW_DOMAIN_MAP.getIfPresent(oss.getGroupType().toString());
                    //判断缓存域名池数据是否空
                    if (CollectionUtils.isEmpty(str)) {
                        continue;
                    }
                    //设定域名池数据,数组下标越界
                    if (str.size() <= i) {
                        continue;
                    }
                    //获取新域名且加密
                    String encryptStr = OssUtil.encryptAES(str.get(i));
                    oss.setEncryptNewDomain(encryptStr);
                    //判断GAY检测未通过的域名和加密后oss动态域名比较，且设置加密后的新域名
                    if (Objects.equals(ossDynamicDomain.getGay().getApi().get(i), oss.getEncryptDomainName())) {
                        ossDynamicDomain.getGay().getApi().set(i, encryptStr);
                    }
                }
            }

            if (Objects.equals(oss.getMerchantTypeName(), "GAS")) {
                for (int i = 0; i < ossDynamicDomain.getGas().getApi().size(); i++) {
                    List<String> str = LocalCacheService.OSS_NEW_DOMAIN_MAP.getIfPresent(oss.getGroupType().toString());
                    //判断缓存域名池数据是否空
                    if (CollectionUtils.isEmpty(str)) {
                        continue;
                    }
                    //设定域名池数据,数组下标越界
                    if (str.size() <= i) {
                        continue;
                    }
                    //获取新域名且加密
                    String encryptStr = OssUtil.encryptAES(str.get(i));
                    oss.setEncryptNewDomain(encryptStr);
                    //判断GAS检测未通过的域名和加密后oss动态域名比较，且设置加密后的新域名
                    if (Objects.equals(ossDynamicDomain.getGas().getApi().get(i), oss.getEncryptDomainName())) {
                        ossDynamicDomain.getGas().getApi().set(i, encryptStr);
                    }
                }
            }

            if (Objects.equals(oss.getMerchantTypeName(), "GAB")) {
                for (int i = 0; i < ossDynamicDomain.getGab().getApi().size(); i++) {
                    List<String> str = LocalCacheService.OSS_NEW_DOMAIN_MAP.getIfPresent(oss.getGroupType().toString());
                    //判断缓存域名池数据是否空
                    if (CollectionUtils.isEmpty(str)) {
                        continue;
                    }
                    //设定域名池数据,数组下标越界
                    if (str.size() <= i) {
                        continue;
                    }
                    //获取新域名且加密
                    String encryptStr = OssUtil.encryptAES(str.get(i));
                    oss.setEncryptNewDomain(encryptStr);
                    //判断GAB检测未通过的域名和加密后oss动态域名比较，且设置加密后的新域名
                    if (Objects.equals(ossDynamicDomain.getGab().getApi().get(i), oss.getEncryptDomainName())) {
                        ossDynamicDomain.getGab().getApi().set(i, encryptStr);
                    }
                }
            }

        } catch (Exception ex) {
            log.error("修改旧oss动态域名错误:", ex);
        }
    }


    /**
     * 自检旧oss域名不通过，替换之后需要将旧的oss域名入库t_domain_ty
     *
     * @param oss
     */
    public void insertCheckFailedOssDomain(Oss oss) {

        Long lineCarrierId = domainMapper.getLineCarrierOne();

        //查域名重复
        int count = tyDomainMapper.countByName(oss.getDomainName(),oss.getGroupType(), null);
        if (count != 0) {
            log.info("{} 此域名已存在！", oss.getDomainName());
            return;
        }

        long now = System.currentTimeMillis();
        TDomain tyDomain = new TDomain();
        tyDomain.setDomainName(oss.getDomainName());
        tyDomain.setStatus(0);
        tyDomain.setEnable(DomainEnableEnum.ATTACK.getCode());
        tyDomain.setCreateTime(now);
        tyDomain.setSelfTestTag(0);
        tyDomain.setGroupType(oss.getGroupType());
        tyDomain.setDomainType(oss.getDomainType());
        tyDomain.setLineCarrierId(lineCarrierId);
        tyDomain.setTab("ty");
        domainMapper.saveDomian(tyDomain);
    }
}
