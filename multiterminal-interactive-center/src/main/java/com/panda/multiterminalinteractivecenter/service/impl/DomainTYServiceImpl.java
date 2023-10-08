package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.constant.CPDomain;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.dto.DomainRelationDto;
import com.panda.multiterminalinteractivecenter.dto.DomainSqlParamTYDTO;
import com.panda.multiterminalinteractivecenter.entity.*;
import com.panda.multiterminalinteractivecenter.enums.*;
import com.panda.multiterminalinteractivecenter.feign.MerchantApiClient;
import com.panda.multiterminalinteractivecenter.mapper.*;
import com.panda.multiterminalinteractivecenter.service.DomainGroupRelationService;
import com.panda.multiterminalinteractivecenter.service.DomainService;
import com.panda.multiterminalinteractivecenter.service.ICpDomainService;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.*;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.java_websocket.enums.ReadyState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 域名彩票服务
 */
@Service
@Slf4j
@RefreshScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DomainTYServiceImpl extends ServiceImpl<TyDomainMapper, TyDomain> implements DomainService {

    private final MongoServiceImpl mongoService;

    private final ICpDomainService cpDomainService;

    private final TyDomainMapper tyDomainMapper;

    private final ThirdDomainMapper thirdDomainMapper;

    private final AbstractMerchantDomainService abstractMerchantDomainService;

    private final MerchantApiClient merchantApiClient;

    private final Domain2DomainGroupMapper domain2DomainGroupMapper;

    private final DomainWebSocketServiceImpl domainWebSocketService;

    private final OssDomainProcessor ossDomainProcessor;

    private final MultiterminalConfig config;

    private final DomainGroupServiceImpl domainGroupService;

    private final DomainGroupRelationService domainGroupRelationService;

    private final MerchantLogService merchantLogService;

    private final MerchantGroupMapper merchantGroupMapper;

    private final CpDjMerchantGroupServiceImpl cpDJMerchantGroupService;

    private final DomainProgramMapper domainProgramMapper;

    private final TelegramBot telegramBot;





    @Override
    public void checkDomainThreshold() {
        log.info("checkDomainThreshold:ty域名组自检开关：{}，dj域名组自检开关：{}，cp域名组自检开关：{}",
                config.getTyDomainGroupThresholdCheckSwitch(),
                config.getDjDomainGroupThresholdCheckSwitch(),
                config.getCpDomainGroupThresholdCheckSwitch());

        if(config.getTyDomainGroupThresholdCheckSwitch()){
            doDomainGroupCheck(TabEnum.TY.getName());
        }
        if(config.getDjDomainGroupThresholdCheckSwitch()){
            doDomainGroupCheck(TabEnum.DJ.getName());
        }
        if(config.getDjDomainGroupThresholdCheckSwitch()){
            doDomainGroupCheck(TabEnum.CP.getName());
        }
    }

    /**
     * 测域名池里面所有的域名(失效,正在使用,可用的) (每天一次)
     */
    public void checkDomainValid() {
        log.info("checkDomainValid:ty自检开关：{}，dj自检开关：{}，cp自检开关：{}",
                config.getTyDomainSelfCheckSwitch(),
                config.getDjdomainSelfCheckSwitch(),
                config.getCpDomainSelfCheckSwitch());
        if(config.getTyDomainSelfCheckSwitch()){
            List<TyDomain> domainList = tyDomainMapper.selectList(
                    Wrappers.<TyDomain>lambdaQuery()
                            .eq(TyDomain::getStatus,1)
                            .eq(TyDomain::getSelfTestTag,1)

            );
            doSelfCheck(domainList, TabEnum.TY.getName());
        }
        if(config.getDjdomainSelfCheckSwitch()){
            List<TyDomain> domainList = thirdDomainMapper.getList(null,1,1,Arrays.asList(1,2,3),TabEnum.DJ.getName());
            doSelfCheck(domainList,TabEnum.DJ.getName());
        }
        if(config.getCpDomainSelfCheckSwitch()){
            List<TyDomain> domainList = thirdDomainMapper.getList(null,1,1,Arrays.asList(1,2,3),TabEnum.CP.getName());
            doSelfCheck(domainList,TabEnum.CP.getName());
        }
    }



    /**
     * 检测商户使用域名的可用性,切换新域名
     */
    public void checkMerchantDomainToggle() {
        log.info("checkMerchantDomainToggle:ty自检切换开关：{}，dj自检切换开关：{}，cp自检切换开关：{}",
                config.getTyDomainCheckToggleSwitch(),
                config.getDjDomainCheckToggleSwitch(),
                config.getCpDomainCheckToggleSwitch());
        if(config.getTyDomainCheckToggleSwitch()){
            List<TyDomain> domainList = tyDomainMapper.selectList(
                    Wrappers.<TyDomain>lambdaQuery()
                            .eq(TyDomain::getEnable, DomainEnableEnum.USED.getCode())
                            .eq(TyDomain::getStatus, 1)
                            .eq(TyDomain::getSelfTestTag, 1)
                            .eq(TyDomain::getTab, TabEnum.TY.getName())
                            .in(TyDomain::getDomainType, Arrays.asList(1,2,3))
            );
            doCheckToggleFunc(domainList, TabEnum.TY.getName());
        }
        if(config.getDjDomainCheckToggleSwitch()){
            List<TyDomain> domainList = thirdDomainMapper.getList(DomainEnableEnum.USED.getCode(),1,1,Arrays.asList(1,2,3),TabEnum.DJ.getName());
            doCheckToggleFunc(domainList,TabEnum.DJ.getName());
        }
        if(config.getCpDomainCheckToggleSwitch()){
            List<TyDomain> domainList = thirdDomainMapper.getList(DomainEnableEnum.USED.getCode(),1,1,Arrays.asList(1,2,3),TabEnum.CP.getName());
            doCheckToggleFunc(domainList,TabEnum.CP.getName());
        }
    }

    private boolean selfTestDomain(String domain, String tab, String domainTypeStr) {
        try {
            // type 只有ty和cp，dj
            String type = "ty".equalsIgnoreCase(tab)? "ty" : "cpdj";
            log.info("selfTestDomain自检开始，参数--> domain:【{}】，type:【{}】，tab:【{}】，domaintype:【{}】", domain,type,tab,domainTypeStr);

//            CloseableHttpClient httpClient = HttpClientUtil.getSystemHttpClient(config.getHttpProxySwitch());
//
//            HttpPost httpPost = new HttpPost(config.getSelfUrl());
//
//            JSONObject param = new JSONObject();
//            param.put("type",type);
//            param.put("domain",domain);
//            param.put("domaintype",domainTypeStr);
//
//            StringEntity paramEntity = new StringEntity(JSON.toJSONString(param),"UTF-8");
//
//            httpPost.setEntity(paramEntity);
//
//            httpPost.setHeader("Content-Type", "application/json");
//
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//
//            String msg = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject param = new JSONObject();
            param.put("type",type);
            param.put("domain",domain);
            param.put("domaintype",domainTypeStr);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(config.getSelfUrl(), new HttpEntity<>(param, headers),String.class);

            log.info("selfTestDomain自检结束，结果--> result:【{}】",response.getBody());

            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            JSONObject data = jsonObject.getJSONObject("data");
            Integer noHealthNodeCount = data.getInteger("nohealthnodecount");
            Integer healthNodeCount = data.getInteger("healthnodecount");

            if (Objects.isNull(healthNodeCount) || healthNodeCount == 0) {
                return false;
            }

            BigDecimal allNodeCountB = BigDecimal.valueOf(noHealthNodeCount+healthNodeCount);
            BigDecimal healthNodeCountB = BigDecimal.valueOf(healthNodeCount);
            BigDecimal currentThread = healthNodeCountB.divide(allNodeCountB,4,RoundingMode.HALF_DOWN);
            log.info("selfTestDomain--不健康节点数：=【{}】,健康节点数：=【{}】,阈值:=【{}】，response：=【{}】", noHealthNodeCount,healthNodeCount,currentThread,JSON.toJSONString(jsonObject));


            BigDecimal checkNodes = BigDecimal.valueOf(config.getSelfCheckThreshold());
            BigDecimal allNodes = BigDecimal.valueOf(config.getSelfNodes());
            BigDecimal configThread = checkNodes.divide(allNodes,4,RoundingMode.HALF_DOWN);

            if (currentThread.compareTo(configThread) < 0) {
                log.info("selfTestDomain自检失败,tab:【{}】,domain:【{}】,threshold=【{}】,nohealthnodecount=【{}】",tab, domain, currentThread,noHealthNodeCount);
                String text = "0000,selfTestDomain自检失败域名测速检测失败,threshold=" + currentThread + "第三方域名不自动切换!";
                mongoService.send(text, config.getTargetName(), config.getSendMongoSwitch(), config.getUserId(), config.getUserToken());
                telegramBot.sendGroupMessage(text);
                return false;
            } else {
                log.info( "tab:【{}】,{}selfTestDomain自检success!",tab,domain);
                return true;
            }
        } catch (Exception e) {
            log.info( "selfTestDomain：tab:【{}】,domain:【{}】,自检接口请求失败", tab, domain);
            log.error( "selfTestDomain：tab:【{}】,【{}】selfTestDomain.exception:", tab, domain, e);
            return true;
        }
    }

    private Boolean checkUpdateTime(Integer timeType, Long updateTime, Integer times) {

        long sysTime = System.currentTimeMillis();
        long timesSum = 0L;
        /*时间类型  1为分钟 2为小时 3为日  4为月*/
        if (timeType == 1) {
            timesSum = times * 60 * 1000L;
        }
        if (timeType == 2) {
            timesSum = times * 60 * 60 * 1000L;
        }
        if (timeType == 3) {
            timesSum = times * 60 * 60 * 24 * 1000L;
        }
        if (timeType == 4) {
            timesSum = times * 60 * 60 * 24 * 30 * 1000L;
        }
        return sysTime > updateTime + timesSum;
    }



    private void doSelfCheck(List<TyDomain> domainAllList,String tab) {
        log.info("checkDomainValid.【{}】需要自检的域名数量:{}" , tab , (CollectionUtils.isEmpty(domainAllList) ? 0 : domainAllList.size()));
        try {
            if (CollectionUtils.isEmpty(domainAllList)) {
                return;
            }
            DomainWebSocket webSocket = null;
            if (config.getWebsocketSwitch() || config.getDnsSwitch()) {
                webSocket = this.initDomainWebSocketClient();
            }
            for (TyDomain tyDomain : domainAllList) {
                boolean checkPass = true;
                Integer domainType = tyDomain.getDomainType();
                Integer enable = tyDomain.getEnable();
                String dt = domainType == 3 ? "api" : "h5";
                // 自检,不靠谱
                if (config.getSelfEnable()) {
                    checkPass = selfTestDomain(tyDomain.getDomainName(), tab, dt);
                }

                log.info("checkDomainValidDomain=" + tyDomain + ",checkPass=" + checkPass);
                //如果监测没有通过,且域名本身的状态 不为 被攻击或者被劫持状态. 则把可用域名更新位不可用
                if (!checkPass && (!Objects.equals(enable, DomainEnableEnum.ATTACK.getCode())
                        && !Objects.equals(enable, DomainEnableEnum.HIJACK.getCode()))) {
                    tyDomain.setEnable(DomainEnableEnum.ATTACK.getCode());
                    abstractMerchantDomainService.getDomainServiceBean(tab).edit(tyDomain, null);
                }
                //如果监测通过,且域名本身的状态 为 被攻击或者被劫持状态. 则把不可用域名更新为可用
                if (checkPass && (Objects.equals(enable, DomainEnableEnum.ATTACK.getCode())
                        || Objects.equals(enable, DomainEnableEnum.HIJACK.getCode()))) {
                    tyDomain.setEnable(DomainEnableEnum.WAIT_USE.getCode());
                    abstractMerchantDomainService.getDomainServiceBean(tab).edit(tyDomain, null);
                }

                // 17ce检测域名
                if (config.getWebsocketSwitch() && config.getSelfEnable() && checkPass) {
                    // DomainConstants.OSS_DOMAIN区分域名池域名还是oss文件域名
                    if (config.getCheckDomainEnable()) {
                        ossDomainProcessor.check17ceDomain(webSocket,null,tyDomain.getDomainName(), domainType, DomainConstants.DOMAIN);
                    }
                }
            }
        } catch (Exception e) {
            log.error("checkDomainValid测域名池里面所有的域名异常！", e);
        }
    }



    /**域名自检切换核心逻辑*/
    private void doCheckToggleFunc(List<TyDomain> domainList, String tab) {
        try {
            log.info("域名自检切换 checkMerchantDomainToggle.【{}】需要自检切换的域名数量:{}",tab ,(CollectionUtils.isEmpty(domainList) ? 0 : domainList.size()));
            if (CollectionUtils.isEmpty(domainList)) {
                return;
            }

            DomainWebSocket webSocket = null;
            if (config.getWebsocketSwitch() || config.getDnsSwitch()) {
                webSocket = this.initDomainWebSocketClient();
            }

            for (TyDomain domain : domainList) {
                boolean checkPass = true;
                Integer domainType = domain.getDomainType();
                String domainTypeStr = domainType == 3 ? "api" : "h5";

                if(!getDomainSwitch(domainType)){
                    // h5|pc|api开关未打开不进行自检    图片和其他本身不进行自检
                    log.info("域名{}不支持自检,结束",domain.getDomainName());
                    return;
                }

                //自检,不靠谱
                if (config.getSelfEnable()) {
                    checkPass = selfTestDomain(domain.getDomainName(), tab, domainTypeStr);
                }

                log.info("checkMerchantDomainToggle:tab:【{}】,domain:【{}】,结果:【{}】", tab, domain.getDomainName(), checkPass);
                if (!checkPass) {
                    String text1 = String.format("域名自检发现此域名可能被攻击,请及时检查，域名详情：tab：【%s】,id:【%s】,domain:【%s】",
                            tab,domain.getId().toString(),domain.getDomainName());
                    mongoService.send(text1, config.getTargetName(),config.getSendMongoSwitch() , config.getUserId(), config.getUserToken());
                    telegramBot.sendGroupMessage(text1);
//                    // 旧域名更新为被攻击
//                    domain.setEnable(DomainEnableEnum.ATTACK.getCode()).setUpdateTime(System.currentTimeMillis());
//                    domain.setStatus(0);
//                    abstractMerchantDomainService.getDomainServiceBean(tab).edit(domain);
//
//                    // 做校验 ， 防止下方代码报错
//                    List<Domain2DomainGroup> domain2DomainGroups = domain2DomainGroupMapper.selectByDomainId(domain.getId(),tab);
//                    if (CollectionUtil.isEmpty(domain2DomainGroups)) {
//                        log.error("tab:【{}】，【{}】该域名没有域名组,无法完成域名切换！",tab, domain.getDomainName());
//                        return;
//                    }
//
//                    // 开始在新的域名组里找新的待使用域名进行替换
//                    final Integer newDomainType = domain.getDomainType();
//
//                    // 每个域名组都拉一个新的进来
//                    for (Domain2DomainGroup domain2DomainGroup : domain2DomainGroups) {
//
//                        if (switchByParamV2(domain,newDomainType,domain2DomainGroup.getDomainGroupId())){
//                            return;
//                        }
//
//                        String text = String.format("域名自检切换没有找到替换的域名,请及时补充,tab：【%s】,id:【%s】,domain:【%s】",
//                                tab,domain.getId().toString(),domain.getDomainName());
//                        mongoService.send(text, config.getTargetName(),config.getSendMongoSwitch() , config.getUserId(), config.getUserToken());
//                        telegramBot.sendGroupMessage(text);
//                    }
                }

                //17ce检测域名  只支持ty
                if ( config.getThirdCheckList().contains(tab) && config.getWebsocketSwitch() && config.getSelfEnable() && checkPass) {
                    // DomainConstants.OSS_DOMAIN区分域名池域名还是oss文件域名
                    if (config.getCheckDomainEnable()) {
                        ossDomainProcessor.check17ceDomain(webSocket,null,domain.getDomainName(), domainType, DomainConstants.DOMAIN);
                    }
                }
            }
        } catch (Exception e) {
            log.error("checkMerchantDomainToggle 每分钟定时检测一次已使用的域名，如果算法无法通过就替换域名！", e);
        }
    }

    private boolean getDomainSwitch(Integer domainType) {
        boolean result = false;
        if(domainType== null ){
            return result;
        }
        if(domainType.equals(DomainTypeEnum.H5.getCode()) && !config.getH5DomainSwitch()){
            return result;
        }
        if(domainType.equals(DomainTypeEnum.PC.getCode()) && !config.getPcDomainSwitch()){
            return result;
        }
        if(domainType.equals(DomainTypeEnum.APP.getCode()) && !config.getAppDomainSwitch()){
            return result;
        }
        if(domainType.equals(DomainTypeEnum.IMAGE.getCode())|| domainType.equals(DomainTypeEnum.OTHER.getCode())){
            return result;
        }
        return true;
    }

    @Override
    public Boolean switchByParamV2(TyDomain domain, Integer domainType, Long domainGroupId) {
        return this.switchByParamV2(domain,domainType,domainGroupId,null,1);
    }

    /**@param  changeType     1自检切换2手动切换*/
    @Override
    public Boolean switchByParamV2(TyDomain domain, Integer domainType, Long domainGroupId,Long targetLineId,int changeType) {
        log.info("switchByParamV2 进入：tab:【{}】，【{}】,准备替换域名！",domain.getTab(), domain.getDomainName());
        DomainSqlParamTYDTO domainSqlParamTYDTO =
                DomainSqlParamTYDTO
                        .builder()
                        .domainGroupId(domainGroupId)
                        .domainType(domainType)
                        .groupType(domain.getGroupType())
                        .lineCarrierId(domain.getLineCarrierId())
                        .tab(domain.getTab())
                        .build();

        List<TyDomain> domainNameList = abstractMerchantDomainService.getDomainServiceBean(domain.getTab())
                .getNewDomainByGroupId(domainSqlParamTYDTO);

        if(CollectionUtils.isEmpty(domainNameList)){
            return false;
        }

        // 优先取一下不同线路商
        Stream<TyDomain> newDomainStream = domainNameList.stream().filter(d-> !d.getLineCarrierId().equals(domain.getLineCarrierId()));

        if(targetLineId != null){
            newDomainStream = newDomainStream.filter(d-> d.getLineCarrierId().equals(targetLineId));
        }

        TyDomain newDomain = newDomainStream.findFirst().orElse(domainNameList.get(0));

        // 新域名改为使用中
        switchDomain(domain.getTab(), domain, newDomain,changeType);

        return true;
    }

    private void doDomainGroupCheck(String tab) {
        try {
            // 查询域名组阈值
            List<DomainGroup> domainGroupList = domainGroupService.list(Wrappers.<DomainGroup>lambdaQuery().eq(DomainGroup::getStatus, 1).eq(DomainGroup::getTab, tab));

            // 获取可用域名
            List<DomainRelationDto> domainList = domainGroupRelationService.getDomainList(tab);

            log.info("checkDomainThresholdTask.checkDomainThreshold.【{}】获取可用域名数量:{}",
                    tab, CollectionUtil.isEmpty(domainList)? 0 :domainList.size());

            Map<Long, List<DomainRelationDto>> domainMap =
                    domainList.stream().collect(Collectors.groupingBy(DomainRelationDto::getDomainGroupId));

            List<String> mongoTextList = Lists.newArrayList();


            domainGroupList.forEach(domainGroup -> {

                // 获取域名组下的所有域名
                List<DomainRelationDto> tyDomains = domainMap.get(domainGroup.getId());

                if(CollectionUtils.isEmpty(tyDomains)){
                    return;
                }

                List<DomainRelationDto> eachDomains;

                String msg = "【%s】，域名组：【%s】，类型:【%s】,数量为 %d 个，不足阈值 %d 个，请向该域名池补充域名！";

                eachDomains = tyDomains.stream().filter(d-> Objects.equals(d.getDomainType(), DomainTypeEnum.H5.getCode())).collect(Collectors.toList());

                //过滤域名数量为0，不需要报警、每种域名类型都去测试一遍
                if(CollectionUtil.isEmpty(eachDomains) || eachDomains.size() < domainGroup.getH5Threshold()){
                        mongoTextList.add(
                                String.format(msg,
                                        tab, domainGroup.getDomainGroupName(), DomainTypeEnum.H5.getName(),
                                        eachDomains.size(), domainGroup.getH5Threshold())
                        );
                }

                eachDomains = tyDomains.stream().filter(d-> Objects.equals(d.getDomainType(), DomainTypeEnum.PC.getCode())).collect(Collectors.toList());

                if(CollectionUtil.isEmpty(eachDomains) || eachDomains.size() < domainGroup.getPcThreshold()){
                    mongoTextList.add(
                            String.format(msg,
                                    tab, domainGroup.getDomainGroupName(),DomainTypeEnum.PC.getName(),
                                    eachDomains.size(), domainGroup.getPcThreshold())
                    );
                }

                eachDomains = tyDomains.stream().filter(d-> Objects.equals(d.getDomainType(), DomainTypeEnum.APP.getCode())).collect(Collectors.toList());

                if(CollectionUtil.isEmpty(eachDomains) || eachDomains.size() < domainGroup.getApiThreshold()){
                    mongoTextList.add(
                            String.format(msg,
                                    tab, domainGroup.getDomainGroupName(),DomainTypeEnum.APP.getName(),
                                    eachDomains.size(), domainGroup.getApiThreshold())
                    );
                }

                eachDomains = tyDomains.stream().filter(d-> Objects.equals(d.getDomainType(), DomainTypeEnum.IMAGE.getCode())).collect(Collectors.toList());

                if(CollectionUtil.isEmpty(eachDomains) || eachDomains.size() < domainGroup.getImgThreshold()){
                    mongoTextList.add(
                            String.format(msg,
                                    tab, domainGroup.getDomainGroupName(),DomainTypeEnum.IMAGE.getName(),
                                    eachDomains.size(), domainGroup.getImgThreshold())
                    );
                }


            });


            if(CollectionUtils.isEmpty(mongoTextList)){
                log.info("【{}】域名组阈值校验全部正常！",tab);
                return;
            }

            // 异步发送mongo消息
            CompletableFuture.runAsync(()->{
                for (String text : mongoTextList) {
                    mongoService.send(text, config.getTargetName(), config.getSendMongoSwitch(), config.getUserId(), config.getUserToken());
                    telegramBot.sendGroupMessage(text);
                }
            });
        }catch (Exception e){
            log.error("checkDomainThreshold 域名组阈值校验{}失败！异常问题：{}",tab,e.getMessage());
        }
    }

    @Override
    public void checkMerchantUseByCp() {
        // 遍历所有域名，自检并切换
        String domainTypeStr = "api";
        if(CollectionUtil.isEmpty(CPDomain.bwUseDomains)){
            log.error("istio---CPDomain.bwUseDomains无需自检");
        }
        if(CollectionUtil.isEmpty(CPDomain.topcpUseDomains)){
            log.error("istio---CPDomain.topcpUseDomains无需自检");
        }
        String[] args = {"vip1","vip2","vip3","bj1","bj2","bj3","def1","def2","def3"};

        for (int i = 0; i < 2; i++) {

            Map<String,List<String>> useMap = i==0?CPDomain.bwUseDomains:CPDomain.topcpUseDomains;
            Map<String,List<String>> dataMap = i==0?CPDomain.bwDomains:CPDomain.topcpDomains;
            String merchantAccount = i==0?"bw|bob":"topcp|ob02";

            for (String arg : args) {
                if (!arg.endsWith("3")) domainTypeStr = "h5";
                List<String> checkDomains = useMap.get(arg);
                if(CollectionUtil.isNotEmpty(checkDomains)){
                    for (String checkDomain : checkDomains) {
                        log.info("域名自检开始，param：{}",checkDomain);
                        boolean checkPass = selfTestDomain(checkDomain, TabEnum.CP.getName(),domainTypeStr);
                        if(!checkPass){
                            // 域名检测出现问题
                            List<String> dataDomains = dataMap.get(arg);
                            dataDomains.removeAll(useMap.get(arg));
                            if(domainTypeStr.equals("api")){
                                useMap.put(arg,new ArrayList<String>() {{add(dataDomains.get(0));add(dataDomains.get(1));}});
                            }else{
                                useMap.put(arg,new ArrayList<String>() {{add(dataDomains.get(0));}});
                            }
                            log.info("istio---调用CP，域名自检切换，调用参数：商户:{},arg：{},新域名：{}",merchantAccount,arg,checkDomain);
                            cpDomainService.sendMsgV2(merchantAccount, Integer.valueOf(arg.substring(arg.length()-1)),checkDomain,arg.contains("vip")? 1 : 0,arg.contains("bj")?"北京":"");

                            break;
                        }
                    }
                }
            }

        }

    }

    private void switchDomain(String tab, TyDomain oldDomain, TyDomain newDomain,int changeType) {
        log.info("switchByParam.switchDomain 开始切换新的域名：tab:【{}】，\n 旧域名：【{}】, \n 新域名：【{}】！",tab , JSON.toJSONString(oldDomain),JSON.toJSONString(newDomain));

            String text;
            if (!Objects.isNull(newDomain)) {

                // 更新为正在使用，并且清除缓存
                newDomain.setEnable(DomainEnableEnum.USED.getCode())
                        .setEnableTime(System.currentTimeMillis())
                        .setUpdateTime(System.currentTimeMillis())
                        .setStatus(1);

                if (TabEnum.TY.getName().equalsIgnoreCase(tab)) {
                    tyDomainMapper.updateById(newDomain);
                    // 商户替换新域名, 这里会清除缓存
                    merchantApiClient.updateNewDomain(oldDomain.getDomainName(), oldDomain.getDomainType(), newDomain.getDomainName());
                }else {
                    thirdDomainMapper.updateIgnoreNull(newDomain);
                }

                // 域名自检功能处理

                text = " \n selfTestDomain自检失败域名测速检测失败，域名开始切换:" +
                        "\n 旧域名【" + oldDomain.getId() + "---" + oldDomain.getDomainName() + "】," +
                        "\n 新域名【" + newDomain.getId() + "---" + newDomain.getDomainName() + "】!";

                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                List<String> fieldName = new ArrayList<>();
                fieldName.add("domain check");
                vo.setFieldName(fieldName);
                List<String> beforeValues = new ArrayList<>();
                beforeValues.add(JSON.toJSONString(oldDomain.getDomainName()));
                vo.setBeforeValues(beforeValues);
                List<String> afterValues = new ArrayList<>();
                afterValues.add(JSON.toJSONString(newDomain.getDomainName()));
                vo.setAfterValues(afterValues);
                vo.setDomainType(newDomain.getDomainType());
                String merchantName = "SYSTEM";
                if (null != newDomain.getId()) {
                    String tabStr = StringUtils.isNotBlank(newDomain.getTab())? newDomain.getTab() : TabEnum.TY.getName();
                    String reallyMerchantName = merchantGroupMapper.getMerchantGroupNameByDomainId(newDomain.getId(),tabStr);
                    if (StringUtils.isNotBlank(reallyMerchantName)) {
                        merchantName = reallyMerchantName;
                    }
                }
                String finalMerchantName = merchantName;

                // 保存日志 并且清除DJ|CP缓存
                MerchantLogTypeEnum merchantLogTypeEnum;
                if(tab.equalsIgnoreCase(TabEnum.TY.getName())){
                    merchantLogTypeEnum = changeType == 2 ? MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_Domian : MerchantLogTypeEnum.CHANGE_MERCHANT_Domian;
                    sendDomainSwitchMsg(merchantLogTypeEnum,vo,merchantName,oldDomain.getDomainType());
                }else if(tab.equalsIgnoreCase(TabEnum.DJ.getName())){
                    merchantLogTypeEnum = changeType == 2 ? MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_DJ_Domian :MerchantLogTypeEnum.CHANGE_MERCHANT_DJ_Domian;
                    CompletableFuture.runAsync(()->{
                        cpDJMerchantGroupService.sendDJMsg(finalMerchantName,0,"",DomainConstants.DOMAIN_CHANGE_AUTO);
                        sendDomainSwitchMsg(merchantLogTypeEnum,vo,finalMerchantName,oldDomain.getDomainType());
                    });
                }else {
                    merchantLogTypeEnum = changeType == 2 ? MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_CP_Domian :MerchantLogTypeEnum.CHANGE_MERCHANT_CP_Domian;
                    CompletableFuture.runAsync(()->{
                        List<Long> programIds = domainProgramMapper.getIdByDomainId(newDomain.getId(),tab);
                        if(CollectionUtils.isNotEmpty(programIds)){
                            for (Long programId : programIds) {
                                cpDJMerchantGroupService.clearCPCache(null,programId);
                            }
                        }
                        sendDomainSwitchMsg(merchantLogTypeEnum,vo,finalMerchantName,oldDomain.getDomainType());
                    });
                }
            } else {
                text = tab + "域名池已空,请立即添加域名!!!!!!!!!!! 已失效域名为" + oldDomain.getDomainName();
            }
            // 异步发送消息
            CompletableFuture.runAsync(()-> {
                mongoService.send(text, config.getTargetName(), config.getSendMongoSwitch(), config.getUserId(), config.getUserToken());
                telegramBot.sendGroupMessage(text);
            });
    }

    private void sendDomainSwitchMsg(MerchantLogTypeEnum merchantLogTypeEnum,MerchantLogFiledVO vo,String merchantName,Integer domainType){
        merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER,
                merchantLogTypeEnum, vo,
                MerchantLogConstants.MERCHANT_IN, "0",
                "admin", merchantName,
                merchantName, "9999999", "en", "localhost",
                DomainEnableEnum.ATTACK.getValue(), null, domainType);
    }

    /**
     * 注入Socket客户端
     * <p>
     * let code = $.md5($.base64.encode($.md5(api_pwd).substring(4,23) + $.trim(user) + timestamp.substring(0,10)));
     *
     * @return
     */
    public DomainWebSocket initDomainWebSocketClient() {
        URI uri = null;
        try {
            String ut = new Date().getTime() / 1000 + "";

            String md5password = OssUtil.getMd5key(config.getWebSocketPwd()).substring(4, 23);

            String tem1 = md5password + config.getWebSocketUser() + ut;

            String baseEncode = Base64.getEncoder().encodeToString(tem1.getBytes(StandardCharsets.UTF_8));
            String finalMd5 = OssUtil.getMd5key(baseEncode);// DigestUtils.md5Hex(baseEncode);
            String url = config.getWebSocketUri() + "?user=" + config.getWebSocketUser() + "&code=" + finalMd5 + "&ut=" + ut;
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.error("初始化WS连接失败!", e);
        }
        DomainWebSocket webSocketClient = new DomainWebSocket(uri, domainWebSocketService);
        //启动时创建客户端连接
        webSocketClient.connect();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!webSocketClient.getReadyState().equals(ReadyState.OPEN)){
            webSocketClient.close();
            log.info("webSocket连接没有打开!");
        }
        log.info("webSocket连接打开了");
        return webSocketClient;
    }
}
