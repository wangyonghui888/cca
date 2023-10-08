package com.panda.sport.merchant.manage.service.impl;
/**
 * @author Administrator
 * @date 2021/8/20
 * @TIME 11:25
 */

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.TDomainMapper;
import com.panda.sport.bss.mapper.TMerchantGroupMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroup;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroupInfo;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.po.bss.MerchantGroupPO;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TDomain;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.service.IMongoService;
import com.panda.sport.merchant.manage.service.MerchantGroupService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.JWTUtil;
import com.panda.sport.merchant.mapper.DomainMapper;
import com.panda.sport.merchant.mapper.MerchantGroupMapper;
import com.panda.sport.merchant.mapper.TMerchantGroupInfoMapper;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName MerchantGroupServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2021/8/20 11:25
 */
@Service("merchantGroupService")
@Slf4j
@RefreshScope
public class MerchantGroupServiceImpl implements MerchantGroupService {
    @Autowired
    private TMerchantGroupMapper tMerchantGroupMapper;
    @Value("${merchantGroupSwitch:true}")
    private Boolean merchantGroupSwitch;

    @Value("${merchant.domain.num:20}")
    private Integer merchantDomainNum;

    @Value("${nacos.api.url:null}")
    private String nacosUrl;

    @Value("${nacos.api.namespace:null}")
    private String nacosNameSpace;

    @Value("${websocket.17ce.enable:false}")
    private String websocketSwitch;

    @Value("${check.domain.enable:1}")
    private String checkDomainEnable;

    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private IMongoService mongoService;
    @Autowired
    private MerchantApiClient merchantApiClient;
    @Autowired
    private MerchantGroupMapper merchantGroupMapper;
    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private TDomainMapper tDomainMapper;
    @Autowired
    private TMerchantGroupInfoMapper merchantGroupInfoMapper;
    @Autowired
    private MerchantLogService merchantLogService;



    @Override
    public Response createMerchantGroup(MerchantGroupVO merchantGroupPO) {

        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);

        // 转化：group_code
        MerchantGroupEnum merchantGroupEnum = MerchantGroupEnum.getInstance(groupPO.getGroupType());
        if (merchantGroupEnum == null) {
            return Response.returnFail("商户分库组必传");
        }

        groupPO.setGroupCode(merchantGroupEnum.getCode());

        int num = tMerchantGroupMapper.createMerchantGroup(groupPO);

        log.info("createMerchantGroup num:" + num);
        if (num > 0 && merchantGroupPO.getMerchantCodes() != null) {
            num = merchantMapper.updateMerchantGroupId(String.valueOf(groupPO.getId()), groupPO.getGroupCode(),merchantGroupPO.getMerchantCodes());
            log.info("createMerchantGroup num1:" + num + " merchantGroupPO.getId()，" + merchantGroupPO.getId() + "---" + merchantGroupPO.getMerchantCodes());
        }
        return Response.returnSuccess(groupPO);
    }

    @Override
    public Response updateMerchantGroupNew(HttpServletRequest request,MerchantGroupVO merchantGroupPO) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        //查原来的数据
        List<Map<String, String>> oldLisMap = merchantMapper.queryOldMerchantListByParam(merchantGroupPO.getId());
        MerchantGroupPO PO =  tMerchantGroupMapper.selectMerchantGroupById(Long.valueOf(merchantGroupPO.getId()));
        // 转化：group_code
        MerchantGroupEnum merchantGroupEnum = MerchantGroupEnum.getInstance(groupPO.getGroupType());
        if (merchantGroupEnum == null) {
            log.error("商户信息修改失败，商户分库组为空，商户信息：【{}】",JSON.toJSONString(groupPO));
            return Response.returnFail("商户分库组必传");
        }

        groupPO.setGroupCode(merchantGroupEnum.getCode());

        if (!StringUtil.isBlankOrNull(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        if (groupPO.getTimes() != null || groupPO.getTimeType() != null || groupPO.getUpdateTime() != null || groupPO.getGroupName() != null || groupPO.getStatus() != null) {
            tMerchantGroupMapper.updateMerchantGroup(groupPO);
        }
        try {
            if (merchantGroupPO.getMerchantCodes() != null) {
                merchantMapper.updateMerchantGroupIdDefult(merchantGroupPO.getId());
                if (CollectionUtil.isNotEmpty(merchantGroupPO.getMerchantCodes())) {
                    merchantMapper.updateMerchantGroupId(merchantGroupPO.getId(),groupPO.getGroupCode(), merchantGroupPO.getMerchantCodes());
                }
            }
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            Map<String, Object> param = Maps.newHashMap();

            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            // 是不是三端入口来的
            boolean b = StringUtils.isBlank(username) && StringUtils.isNotBlank(request.getHeader("token"));
            username = b ? JWTUtil.getUsername(request.getHeader("token")) : username;

            MerchantLogPageEnum enumInfo= null;
            MerchantLogTypeEnum typeInfo = null;
            List<String> afters = new ArrayList<>();
            List <String> before = new ArrayList<>();
            if(null != merchantGroupPO.getType()) {//添加删除
                if ("1".equals(merchantGroupPO.getType())) {//删除
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("detailMerchant"));
                    for (int i = 0; i < oldLisMap.size(); i++) {
                        before.add(oldLisMap.get(i).get("merchantCode") + "&" + oldLisMap.get(i).get("merchantName"));
                    }
                    filedVO.setBeforeValues(before);
                    enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_48;
                    typeInfo = MerchantLogTypeEnum.DEL_MERCHANT;
                    saveLogInfo(merchantGroupPO, filedVO, param, userId, username, PO.getGroupName(),
                            PO.getGroupName() + StringPool.AMPERSAND + PO.getId(), ip, language, enumInfo, typeInfo, afters);
                } else if ("0".equals(merchantGroupPO.getType())) {
                    filedVO.getFieldName().addAll(b ? Arrays.asList("商户编码","商户名称","商户类型") : Collections.singletonList(MerchantFieldUtil.FIELD_MAPPING.get("detailMerchant")));
                    for (Map<String, String> stringStringMap : oldLisMap) {
                        before.add(stringStringMap.get("merchantCode") + "&" + stringStringMap.get("merchantName")+ "&" + AgentLevelEnum.getRemarkByCode(String.valueOf(stringStringMap.get("agentLevel"))));
                    }
                    filedVO.setBeforeValues(before);
                    enumInfo = b ? MerchantLogPageEnum.API_DOMAIN_SET : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_47;
                    typeInfo = b? MerchantLogTypeEnum.MERCHANT_SELECT : MerchantLogTypeEnum.ADD_MERCHANT_D;
                    saveLogInfo(merchantGroupPO, filedVO, param, merchantGroupPO.getGroupName(), username,PO.getGroupName(),
                            PO.getGroupName() + StringPool.AMPERSAND + PO.getId(), ip, language, enumInfo, typeInfo, afters);
                } else  if ("2".equals(merchantGroupPO.getType())) { //禁用/启用
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("closeAndOpen"));
                    typeInfo = MerchantLogTypeEnum.EDIT_DOMAIN_STATUS;
                    enumInfo = b ? MerchantLogPageEnum.API_DOMAIN_SET : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_51;
                    before.add(1==PO.getStatus()?"开":"关");
                    afters.add(1==merchantGroupPO.getStatus()?"开":"关");
                    filedVO.setBeforeValues(before);
                    filedVO.setAfterValues(afters);
                    merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                            MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username,
                            null, PO.getGroupName(), PO.getGroupName() + StringPool.AMPERSAND + PO.getId(), language, ip);
                } else if("3".equals(merchantGroupPO.getType())){//切换频率
                    enumInfo = b ? MerchantLogPageEnum.API_DOMAIN_SET : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_49;
                    typeInfo = MerchantLogTypeEnum.EDIT_INFO;
                    if(!PO.getTimes().equals(merchantGroupPO.getTimes()) || !PO.getTimeType().equals(merchantGroupPO.getTimeType())) {
                        filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("changeNum"));
                        before.add("每" + PO.getTimes() + getAfterOp(PO.getTimeType()));
                        afters.add("每" + merchantGroupPO.getTimes() + getAfterOp(merchantGroupPO.getTimeType()));
                    }
                    if(!PO.getGroupName().equals(merchantGroupPO.getGroupName())) {
                        filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("groupName"));
                        before.add(PO.getGroupName());
                        afters.add(merchantGroupPO.getGroupName());
                    }
                    filedVO.setBeforeValues(before);
                    filedVO.setAfterValues(afters);
                    merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                            MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username,
                            null, PO.getGroupName(), PO.getGroupName() + StringPool.AMPERSAND + PO.getId(), language, ip);
                }
            }


    } catch (Exception e) {
            log.error("updateMerchantGroup error:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        log.info("updateMerchantGroup:end ");
        return Response.returnSuccess();
    }

    private void saveLogInfo(MerchantGroupVO merchantGroupPO, MerchantLogFiledVO filedVO, Map<String, Object> param, String userId, String username,String merchantName,
                             String dataId,String ip, String language, MerchantLogPageEnum enumInfo, MerchantLogTypeEnum typeInfo, List<String> afters) {
        param.put("merchantCodeList", merchantGroupPO.getMerchantCodes());
        List<Map<String, String>> lisMap = merchantMapper.queryMerchantListByParam(param);

        for (int i = 0; i < lisMap.size(); i++) {
            afters.add(lisMap.get(i).get("merchantCode") + "&" + lisMap.get(i).get("merchantName")+ "&" + AgentLevelEnum.getRemarkByCode(String.valueOf(lisMap.get(i).get("agentLevel"))));
        }
        filedVO.setAfterValues(afters);
        merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId, username,
                null, merchantName, dataId, language, ip);
    }

    private String getAfterOp(Integer timeType){
        String afterOp="";
        switch(timeType){
            case 1 :
                afterOp ="分钟";
                break;
            case 2:
                afterOp ="小时";
                break;
            case 3 :
                afterOp ="日";
                break;
            case 4:
                afterOp ="月";
                break;
        }

        return afterOp;
    }


    @Override
    public Response updateMerchantGroup(MerchantGroupVO merchantGroupPO) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        // 转化：group_code
        MerchantGroupEnum merchantGroupEnum = MerchantGroupEnum.getInstance(groupPO.getGroupType());
        if (merchantGroupEnum == null) {
            log.error("商户信息修改失败，商户分库组为空，商户信息：【{}】",JSON.toJSONString(groupPO));
            return Response.returnFail("商户分库组必传");
        }

        groupPO.setGroupCode(merchantGroupEnum.getCode());

        if (!StringUtil.isBlankOrNull(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        if (groupPO.getTimes() != null || groupPO.getTimeType() != null || groupPO.getUpdateTime() != null || groupPO.getGroupName() != null || groupPO.getStatus() != null) {
            tMerchantGroupMapper.updateMerchantGroup(groupPO);
        }
        try {
            if (merchantGroupPO.getMerchantCodes() != null) {
                merchantMapper.updateMerchantGroupIdDefult(merchantGroupPO.getId());
                if (CollectionUtil.isNotEmpty(merchantGroupPO.getMerchantCodes())) {
                    merchantMapper.updateMerchantGroupId(merchantGroupPO.getId(),groupPO.getGroupCode(), merchantGroupPO.getMerchantCodes());
                }
            }
        } catch (Exception e) {
            log.error("updateMerchantGroup error:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
        log.info("updateMerchantGroup:end ");
        return Response.returnSuccess();
    }

    @Override
    public Response deleteMerchantGroup(MerchantGroupVO merchantGroupPO) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (!StringUtil.isBlankOrNull(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        int num = tMerchantGroupMapper.deleteMerchantGroup(groupPO);
        DomainVo domainVo = new DomainVo();
        domainVo.setMerchantGroupId(groupPO.getId());
        tDomainMapper.deleteAll(domainVo);
        merchantMapper.updateMerchantGroupIdDefult(merchantGroupPO.getId());
        log.info("deleteMerchantGroup:" + num);
        return Response.returnSuccess(num > 0);
    }

    @Override
    public List<MerchantGroupVO> selectMerchantGroup(MerchantGroupVO merchantGroupPO) {
        MerchantGroupPO groupPO = new MerchantGroupPO();
        BeanUtils.copyProperties(merchantGroupPO, groupPO);
        if (!StringUtil.isBlankOrNull(merchantGroupPO.getId())) {
            groupPO.setId(Long.valueOf(merchantGroupPO.getId()));
        }
        List<MerchantGroupPO> list = tMerchantGroupMapper.selectMerchantGroup(groupPO);
        List<MerchantGroupVO> groupList = Lists.newArrayList();
        Map<String, Object> param = Maps.newHashMap();
        for (MerchantGroupPO po : list) {
            // 转化：group_code
            MerchantGroupEnum merchantGroupEnum = MerchantGroupEnum.getInstance(po.getGroupType());
            MerchantGroupVO vo = new MerchantGroupVO();
            vo.setGroupCode(merchantGroupEnum.getCode());
            vo.setGroupType(po.getGroupType());
            vo.setId(String.valueOf(po.getId()));
            BeanUtils.copyProperties(po, vo);
            param.put("merchantGroupId", vo.getId());
            if (!vo.getId().equals("0")) {
                List<MerchantPO> merchantList = merchantMapper.queryMerchantListByGroup(param);
                vo.setMerchantList(merchantList);
            }
            DomainVo domainVo = new DomainVo();
            domainVo.setMerchantGroupId(po.getId());
            domainVo.setEnable(1);
            List<TDomain> list1 = tDomainMapper.selectAll(domainVo);
            if (CollectionUtil.isNotEmpty(list1)){
                vo.setDomain(list1.get(0).getDomainName());
            }
            groupList.add(vo);
        }
        return groupList;
    }

    @Override
    public void autoChangeDomain() {
        if (!merchantGroupSwitch) {
            log.info("商户API域名自动切换已关闭");
            return;
        }
        MerchantGroupVO groupPO = new MerchantGroupVO();
        groupPO.setStatus(1);
        List<MerchantGroupVO> merchantGroupPOList = selectMerchantGroup(groupPO);
        for (MerchantGroupVO merchantGroupPO1 : merchantGroupPOList) {
            log.info("商户API域名自动切换商户组：" + merchantGroupPO1.getGroupName());
            Long merchantGroupId = Long.parseLong(merchantGroupPO1.getId());
            DomainVo domainVoCheck = new DomainVo();
            domainVoCheck.setEnable(0);
            domainVoCheck.setDomainType(2);
            domainVoCheck.setMerchantGroupId(merchantGroupId);
            List<TDomain> listCheck = tDomainMapper.selectAll(domainVoCheck);
            if ((listCheck.size() + tDomainMapper.checkDomianByEnable(merchantGroupId)) < merchantGroupPO1.getAlarmNum()) {
                String text = "商户组" + merchantGroupPO1.getGroupName() + "APP域名个数低于" +  merchantGroupPO1.getAlarmNum() + "，请向域名池补充域名！";
                mongoService.send(text, null);
            }
            Long sysTime = System.currentTimeMillis();
            Long updateTime = merchantGroupPO1.getUpdateTime();
            if (null != updateTime) {
                long times = 0L;
                /*时间类型  1为分钟 2为小时 3为日  4为月*/
                if (merchantGroupPO1.getTimeType() == 1) {
                    times = merchantGroupPO1.getTimes() * 60 * 1000L;
                }
                if (merchantGroupPO1.getTimeType() == 2) {
                    times = merchantGroupPO1.getTimes() * 60 * 60 * 1000L;
                }
                if (merchantGroupPO1.getTimeType() == 3) {
                    times = merchantGroupPO1.getTimes() * 60 * 60 * 24 * 1000L;
                }
                if (merchantGroupPO1.getTimeType() == 4) {
                    times = merchantGroupPO1.getTimes() * 60 * 60 * 24 * 30 * 1000L;
                }
                if (sysTime > updateTime + times) {
                    Map<String, Object> param = Maps.newHashMap();
                    param.put("merchantGroupId", merchantGroupPO1.getId());
                    param.put("groupCode", merchantGroupPO1.getGroupCode());
                    List<MerchantPO> merchantList = merchantMapper.queryMerchantList(param);
                    DomainVo domainVo = new DomainVo();
                    domainVo.setEnable(2);
                    domainVo.setGroupType(merchantGroupPO1.getGroupType());
                    domainVo.setDomainType(2);
                    domainVo.setMerchantGroupId(Long.valueOf(merchantGroupPO1.getId()));
                    List<TDomain> list = tDomainMapper.selectAll(domainVo);
                    if (list == null) {
                        log.info("商户API域名自动切换,domain配置为空。结束");
                        continue;
                    }
                    for (MerchantPO merchant : merchantList) {
                        log.info("change merchant code:" + merchant.getMerchantCode());
                        if (merchant.getAgentLevel() == 1 || merchant.getAgentLevel() == 10) {
                            log.info("change merchant code:" + merchant.getMerchantCode() + " merchant.getAgentLevel()=" + merchant.getAgentLevel());
                            continue;
                        }
                        for (TDomain domain : list) {
                            if (null == merchant.getAppDomain() || !domain.getDomainName().equals(merchant.getAppDomain())) {
                                int num = merchantMapper.updateApiDomainByMerchantCode(domain.getDomainName(), merchant.getMerchantCode());
                                log.info("num = {}", num);
                                log.info("开始替换 {}  旧的 ={}", domain.getDomainName(), merchant.getAppDomain());
                                if (num > 0) {
                                    merchantApiClient.kickoutMerchant(merchant.getMerchantCode());
                                    tDomainMapper.updateDomianEnableTimeByid(domain.getId(), new Date());
                                    changDomainEnable();
                                    if (StringUtil.isNotBlank(merchant.getAppDomain())) {
                                        num = tDomainMapper.updateByName(merchant.getAppDomain());
                                        log.info("更新回池 num = {}   {}", num, merchant.getAppDomain());
                                        if (num == 0) {
                                            try {
                                                TDomain data = new TDomain();
                                                data.setDomainType(2);
                                                data.setCreateTime(new Date());
                                                data.setCreateUser("system");
                                                data.setMerchantGroupId(merchantGroupId);
                                                data.setDomainName(merchant.getAppDomain());
                                                tDomainMapper.saveDomian(data);
                                                log.info("保存成功！");
                                            } catch (Exception e) {
                                                tDomainMapper.updateByName(merchant.getAppDomain());
                                                log.info("不需要重复保存！");
                                            }

                                        } else {
                                            log.info("{}更新成功！", merchant.getAppDomain());
                                        }
                                    }
                                    log.info("域名自动切换：切换前：" + merchant.getAppDomain() + ",code:" + merchant.getMerchantCode() + ",切换后：" + domain.getDomainName());
                                    break;
                                }
                            }
                        }
                    }
                    MerchantGroupVO merchantGroupPO2 = new MerchantGroupVO();
                    merchantGroupPO2.setId(merchantGroupPO1.getId());
                    merchantGroupPO2.setGroupType(merchantGroupPO1.getGroupType());
                    merchantGroupPO2.setUpdateTime(sysTime);
                    updateMerchantGroup(merchantGroupPO2);
                }
            } else {
                MerchantGroupVO merchantGroupPO2 = new MerchantGroupVO();
                merchantGroupPO2.setId(merchantGroupPO1.getId());
                merchantGroupPO2.setGroupType(merchantGroupPO1.getGroupType());
                merchantGroupPO2.setUpdateTime(sysTime);
                updateMerchantGroup(merchantGroupPO2);
            }
            log.info("商户API域名自动切换end商户组：" + merchantGroupPO1.getGroupName());
        }
    }

    @Override
    public void autoChangeDomainThird() {
        if (!merchantGroupSwitch) {
            log.info("商户API域名自动切换已关闭");
            return;
        }
        List<TMerchantGroup> list1 = merchantGroupMapper.selectAllByStatus(1);
        for (TMerchantGroup merchantGroup : list1) {
            log.info("商户API域名自动切换商户组：" + merchantGroup.getGroupName());
            DomainVo domainVoCheck = new DomainVo();
            domainVoCheck.setEnable(0);
            domainVoCheck.setMerchantGroupId(merchantGroup.getId());
            List<com.panda.sport.merchant.common.po.merchant.TDomain> listCheck = domainMapper.selectAll(domainVoCheck);
            if (listCheck.size() < merchantGroup.getAlarmNum()) {
                String text = "商户组" + merchantGroup.getGroupName() + "APP域名个数低于" +  merchantGroup.getAlarmNum() + "，请向域名池补充域名！";
                mongoService.send(text, null);
            }
            Long sysTime = System.currentTimeMillis();
            Long updateTime = merchantGroup.getUpdateTime();
            if (null != updateTime) {
                long times = 0L;
                /*时间类型  1为分钟 2为小时 3为日  4为月*/
                if (merchantGroup.getTimeType() == 1) {
                    times = merchantGroup.getTimes() * 60 * 1000L;
                }
                if (merchantGroup.getTimeType() == 2) {
                    times = merchantGroup.getTimes() * 60 * 60 * 1000L;
                }
                if (merchantGroup.getTimeType() == 3) {
                    times = merchantGroup.getTimes() * 60 * 60 * 24 * 1000L;
                }
                if (merchantGroup.getTimeType() == 4) {
                    times = merchantGroup.getTimes() * 60 * 60 * 24 * 30 * 1000L;
                }
                if (sysTime > updateTime + times) {
                    List<Integer> domainType = domainMapper.getTypeByGroupId(merchantGroup.getId());
                    for (Integer type : domainType) {
                        DomainVo domainVo = new DomainVo();
                        domainVo.setEnable(2);
                        domainVo.setDomainType(type);
                        domainVo.setMerchantGroupId(merchantGroup.getId());
                        List<com.panda.sport.merchant.common.po.merchant.TDomain> list = domainMapper.selectAll(domainVo);
                        if (CollectionUtil.isEmpty(list)) {
                            log.info("商户API域名自动切换,domain配置为空。结束");
                            continue;
                        }
                        List<TMerchantGroupInfo> merchantList = merchantGroupInfoMapper.tMerchantGroupInfoByGroupId(merchantGroup.getId());
                        domainMapper.updateEnableByType(type, 0, 1, merchantGroup.getId());
                        com.panda.sport.merchant.common.po.merchant.TDomain domain = list.get(0);
                        com.panda.sport.merchant.common.po.merchant.TDomain update = new  com.panda.sport.merchant.common.po.merchant.TDomain();
                        update.setId(domain.getId());
                        update.setEnable(1);
                        update.setEnableTime(System.currentTimeMillis());
                        domainMapper.update(update);
                        for (TMerchantGroupInfo merchant : merchantList) {
                            log.info("change merchant code:" + merchant.getMerchantCode());
                            DomainAbstractService domainAbstractService = null;
                            if (merchantGroup.getGroupCode() == 1){
                                //电竞
                                domainAbstractService = (DomainAbstractService) SpringUtil.getBean("DjDomainServiceImpl");
                            }else if (merchantGroup.getGroupCode() == 2){
                                domainAbstractService = (DomainAbstractService) SpringUtil.getBean("CpDomainServiceImpl");
                            }else {
                                log.info("未配置！");
                            }
                            if (domainAbstractService != null){
                                domainAbstractService.sendMsg(merchant.getMerchantName(),type,domain.getDomainName(),1);
                            }
                        }
                    }
                    TMerchantGroup update = new TMerchantGroup();
                    update.setId(merchantGroup.getId());
                    update.setUpdateTime(sysTime);
                    merchantGroupMapper.update(update);
                }
            } else {
                TMerchantGroup update = new TMerchantGroup();
                update.setId(merchantGroup.getId());
                update.setUpdateTime(sysTime);
                merchantGroupMapper.update(update);
            }
            log.info("商户API域名自动切换end商户组：" + merchantGroup.getGroupName());
        }
    }

    @Override
    public void changDomainEnable() {
        log.info("开始检测分组待使用状态下的域名设置 checkDomainEnable={}",checkDomainEnable);
            MerchantGroupPO merchantGroupPO = new MerchantGroupPO();
            //merchantGroupPO.setStatus(1);
            List<MerchantGroupPO> list = tMerchantGroupMapper.selectMerchantGroup(merchantGroupPO);
            for (MerchantGroupPO po : list) {
                try {
                    log.info("商户API域名状态自动切换开始");
                    Integer count = tDomainMapper.checkDomianByEnable(po.getId());
                    if (count == 0) {
                        log.info("没有待使用的状态 设置一个！");
                        Long id = tDomainMapper.maxDomianIdByEnable(po.getId());
                        log.info("id = {}", id);
                        if (id == null) {
                            continue;
                        }
                        if (tDomainMapper.updateDomianEnableById(2, id) > 0) {
                            log.info("设置成功！");
                        }
                    }
                } catch (Exception e) {
                    log.error("changDomainEnable", e);
                }
            }
    }

    @Override
    public void changDomainEnableThird() {
        log.info("第三方切换开关 checkDomainEnable = {}", checkDomainEnable);
        if ("1".equals(checkDomainEnable)) {
            List<TMerchantGroup> list1 = merchantGroupMapper.selectAllByStatus(1);
            for (TMerchantGroup po : list1) {
                try {
                    List<Integer> domainType = domainMapper.getTypeByGroupId(po.getId());
                    for (Integer type : domainType) {
                        log.info("商户域名 type ={}  groupId ={} 检测开始！", type, po.getId());
                        Integer count = domainMapper.checkDomianByEnable(po.getId(), type);
                        if (count == 0) {
                            Long id = domainMapper.maxDomianIdByEnable(po.getId(), type);
                            if (id == null) {
                                continue;
                            }
                            if (domainMapper.updateDomainEnableById(2, id) > 0) {
                                log.info("商户域名 type ={}  groupId ={} 设置成功！", type, po.getId());
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("changDomainEnable", e);
                }
            }
        }
    }

    @Override
    public void updateGroupNum(HttpServletRequest request,MerchantGroupPO merchantGroupPO) {
        //查询old数据
        MerchantGroupPO po = tMerchantGroupMapper.selectMerchantGroupById(merchantGroupPO.getId());

        tMerchantGroupMapper.updateMerchantGroup(merchantGroupPO);
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("reminderSet"));
        vo.getBeforeValues().add("低于"+po.getAlarmNum());
        vo.getAfterValues().add("低于"+merchantGroupPO.getAlarmNum());;
        String userName = request.getHeader("merchantName");
        MerchantLogPageEnum pageEnum = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_50;
        if(StringUtils.isBlank(userName) && StringUtils.isNotBlank(request.getHeader("token"))){
            // 三端入口来的
            pageEnum = MerchantLogPageEnum.API_DOMAIN_SET;
            userName = JWTUtil.getUsername(request.getHeader("token"));
        }
        merchantLogService.saveLog(pageEnum, MerchantLogTypeEnum.EDIT_INFO, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN,request.getHeader("user-id"), userName, request.getHeader("user-id"),
                po.getGroupName(), po.getGroupName() + StringPool.AMPERSAND + po.getId(),request.getHeader("language") , IPUtils.getIpAddr(request));
    }

    @Override
    public Integer getThirdEnable() {
        if ("true".equalsIgnoreCase(websocketSwitch)){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public void updateThirdEnable(String enable) {
        log.info("publish: star");
        String url = nacosUrl + "?tenant=" + nacosNameSpace + "&dataId=merchant-config.properties&group=merchant&type=properties";
        Mono<?> mono = WebClient.create().get().uri(url).retrieve().bodyToMono(String.class);
        log.info("result:" + mono.block());
        String configStr = (String) mono.block();
        String[] lines = configStr.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            String[] tempSTrAr = line.split("=");
            String key = tempSTrAr[0];
            if (key.equalsIgnoreCase("websocket.17ce.enable")) {
                sb.append("websocket.17ce.enable=").append(enable).append("\r\n\r\n");
            }else {
                sb.append(key).append("=").append(tempSTrAr[1]).append("\r\n\r\n");
            }
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dataId", "merchant-config.properties");
        params.add("tenant", nacosNameSpace);
        params.add("group", "merchant");
        params.add("type", "properties");
        params.add("content", sb.toString());
        Mono<String> result = WebClient.create().post()
                .uri(nacosUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve().bodyToMono(String.class);
        log.info("publish:{}", result.block());
    }


    /**
     * 获取业务组
     * @param
     * @return
     */
    @Override
    public Response selectGroupDomain() {

        MerchantGroupPO selectMerchant = new MerchantGroupPO();
        //selectMerchant.setStatus(1);
        selectMerchant.setGroupType(2);

        //获取业务组和公用组的值
        List<MerchantGroupPO> merchantGroupPOList = tMerchantGroupMapper.selectMerchantGroup(selectMerchant);
        //获取商户分组ID
        List<Long> merchantGroupIds = merchantGroupPOList.stream().map(merchantGroupPO -> merchantGroupPO.getId()).collect(Collectors.toList());

        // 获取t_domain的
        List<TDomain> tDomainList = merchantGroupIds.size() > 0 ? tDomainMapper.getApiDomainUrl(merchantGroupIds) : new ArrayList<>();

        // merchant_group_id
        Map<Long,List<TDomain>> groupBy = tDomainList.stream().collect(Collectors.groupingBy(TDomain::getMerchantGroupId));

        // 商户分组输出
        List<MerchantApiGroupVo> merchantApiGroupVoList = new ArrayList<>();
        for (MerchantGroupPO merchantGroupPO : merchantGroupPOList) {

            MerchantApiGroupVo merchantApiGroupVo = new MerchantApiGroupVo();
            merchantApiGroupVo.setId(String.valueOf(merchantGroupPO.getId()));
            merchantApiGroupVo.setGroupType(merchantGroupPO.getGroupType());
            merchantApiGroupVo.setGroupName(merchantGroupPO.getGroupName());
            merchantApiGroupVo.setGroupCode(merchantGroupPO.getGroupCode());
            merchantApiGroupVo.setStatus(merchantGroupPO.getStatus());
            if (groupBy.containsKey(merchantGroupPO.getId())) {
                merchantApiGroupVo.setTDomainList(groupBy.get(merchantGroupPO.getId()));
            }
            // 添加
            merchantApiGroupVoList.add(merchantApiGroupVo);
        }

        return Response.returnSuccess(merchantApiGroupVoList);
    }
}
