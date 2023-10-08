package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.DomainTypeEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.merchant.TDomain;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroup;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroupInfo;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.JWTUtil;
import com.panda.sport.merchant.mapper.DomainMapper;
import com.panda.sport.merchant.mapper.MerchantGroupMapper;
import com.panda.sport.merchant.mapper.TMerchantGroupInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  TODO
 * @Date: 2021-12-30 10:43:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
public abstract class DomainAbstractService {

    @Autowired
    private MerchantGroupMapper tMerchantGroupMapper;

    @Autowired
    private TMerchantGroupInfoMapper tMerchantGroupInfoMapper;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    protected MongoServiceImpl mongoService;

    @Autowired
    private DomainMapper tDomainMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private MerchantGroupMapper merchantGroupMapper;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取商户信息
     * @return
     */
    public abstract List<ThirdMerchantVo> getMerchantList();

    public List<ThirdMerchantVo> getMerchantList(Integer code){
        List<ThirdMerchantVo> result = new ArrayList<>();
        List<ThirdMerchantVo> list = getMerchantList();
        if (code != null) {
            List<TMerchantGroupInfo> self = tMerchantGroupInfoMapper.getAllByCode(code);
            if (CollectionUtil.isNotEmpty(self)) {
                List<String> names = self.stream().map(TMerchantGroupInfo::getMerchantName).collect(Collectors.toList());
                for (ThirdMerchantVo vo : list) {
                    if (!names.contains(vo.getMerchantName())) {
                        result.add(vo);
                    }
                }
            } else {
                result.addAll(list);
            }
        }else {
            result.addAll(list);
        }
        return result;
    }

    /**
     * 获取分组域名
     * @param domainVo
     * @return
     */
    public Response getMerchantGroup(DomainVo domainVo){
        List<TMerchantGroup> merchantGroups = tMerchantGroupMapper.selectAll(domainVo.getMerchantGroupCode());
        if (CollectionUtil.isNotEmpty(merchantGroups)){
            for (TMerchantGroup merchantGroup : merchantGroups) {
                merchantGroup.setMerchantGroupInfos(tMerchantGroupInfoMapper.tMerchantGroupInfoByGroupId(merchantGroup.getId()));
            }
        }
        return Response.returnSuccess(merchantGroups);
    }

    public Response getMerchantGroupInfoByThirdCode(String code,String account){
        Integer groupCode = 0;
        if ("cp".equalsIgnoreCase(code)){
            groupCode = 2;
        }else if ("dj".equalsIgnoreCase(code)){
            groupCode = 1;
        }else {
            return Response.returnFail("input code fail!");
        }
        List<TMerchantGroupInfoVo> merchantGroups = tMerchantGroupMapper.getMerchantGroupInfoByThirdCode(groupCode,account);
        return Response.returnSuccess(merchantGroups);
    }

    /**
     * 新增分组
     * @param tMerchantGroup
     * @return
     */
    public Response saveMerchantGroup(TMerchantGroup tMerchantGroup){
        Map result  = new HashMap();
        if (tMerchantGroup.getId() != null){
            //查原来的数据
            TMerchantGroup PO = merchantGroupMapper.load(tMerchantGroup.getId().intValue());
            tMerchantGroupMapper.update(tMerchantGroup);
            result.put("id",tMerchantGroup.getId());
            if (CollectionUtil.isNotEmpty(tMerchantGroup.getMerchantGroupInfos())){
                log.info("有详细数据开始新增! size ={}", tMerchantGroup.getMerchantGroupInfos().size());
                tMerchantGroupInfoMapper.deleteByGroupId(tMerchantGroup.getId().intValue());
                for (TMerchantGroupInfo vo : tMerchantGroup.getMerchantGroupInfos()) {
                    TMerchantGroupInfo merchantGroup = new TMerchantGroupInfo();
                    merchantGroup.setCreatTime(vo.getCreatTime());
                    merchantGroup.setMerchantCode(vo.getId());
                    merchantGroup.setMerchantName(vo.getMerchantName());
                    merchantGroup.setMerchantGroupId(tMerchantGroup.getId().intValue());
                    merchantGroup.setOperator("SYS");
                    merchantGroup.setUpdateTime(System.currentTimeMillis());
                    tMerchantGroupInfoMapper.insert(merchantGroup);
                }
            }else {
                tMerchantGroupInfoMapper.deleteByGroupId(tMerchantGroup.getId().intValue());
            }
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            Map<String, Object> param = Maps.newHashMap();

            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            MerchantLogPageEnum enumInfo= null;
            MerchantLogTypeEnum typeInfo = null;
            List<String> afters = new ArrayList<>();
            List <String> before = new ArrayList<>();
            if(null != tMerchantGroup.getType()) {//添加删除


                if ("1".equals(tMerchantGroup.getType())) {//删除
                    List<TMerchantGroupInfo> Vos = tMerchantGroupInfoMapper.getAllByCode(tMerchantGroup.getGroupCode());
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("detailMerchant"));
                    for (TMerchantGroupInfo op : Vos) {
                        before.add(op.getMerchantCode() + "&" + op.getMerchantName());
                    }
                    filedVO.setBeforeValues(before);
                    if(2==tMerchantGroup.getGroupCode()){
                      enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_63;
                    }else{
                      enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_68;
                    }
                    typeInfo = MerchantLogTypeEnum.DEL_MERCHANT;
                    saveLogInfo(tMerchantGroup, filedVO, param, userId, username, ip, language, enumInfo, typeInfo, afters);
                } else if ("0".equals(tMerchantGroup.getType())) {
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("detailMerchant"));
                    filedVO.getBeforeValues().add("-");
                    if(1==tMerchantGroup.getGroupCode()) {
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_62;
                    }else{
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_67;
                    }
                    typeInfo = MerchantLogTypeEnum.ADD_MERCHANT_D;
                    saveLogInfo(tMerchantGroup, filedVO, param, userId, username, ip, language, enumInfo, typeInfo, afters);
                } else  if ("2".equals(tMerchantGroup.getType())) { //禁用/启用
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("closeAndOpen"));

                    if(1==tMerchantGroup.getGroupCode()) {
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_66;
                    }else{
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_71;
                    }
                    typeInfo = MerchantLogTypeEnum.EDIT_DOMAIN_STATUS;
                    before.add(1==PO.getStatus()?"开":"关");
                    afters.add(1==tMerchantGroup.getStatus()?"开":"关");
                    filedVO.setBeforeValues(before);
                    filedVO.setAfterValues(afters);
                    merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                            MerchantLogConstants.MERCHANT_IN, userId, username,
                            null, username, userId, language, ip);
                } else if("3".equals(tMerchantGroup.getType())){//切换频率
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("changeNum"));
                    if(1==tMerchantGroup.getGroupCode()) {
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_64;
                    }else{
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_69;
                    }
                    typeInfo = MerchantLogTypeEnum.EDIT_INFO;
                    before.add("每" + PO.getTimes() + getAfterOp(PO.getTimeType()));
                    afters.add("每" + tMerchantGroup.getTimes() + getAfterOp(tMerchantGroup.getTimeType()));
                    filedVO.setBeforeValues(before);
                    filedVO.setAfterValues(afters);
                    merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                            MerchantLogConstants.MERCHANT_IN, userId, username,
                            null, username, userId, language, ip);
                } else if("4".equals(tMerchantGroup.getType())){//提醒设定
                    filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("setWack"));
                    if(1==tMerchantGroup.getGroupCode()) {
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_65;
                    }else{
                        enumInfo = MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_70;
                    }
                    typeInfo = MerchantLogTypeEnum.EDIT_INFO;
                    before.add("低于" + PO.getAlarmNum());
                    afters.add("低于" + tMerchantGroup.getAlarmNum());
                    filedVO.setBeforeValues(before);
                    filedVO.setAfterValues(afters);
                    merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                            MerchantLogConstants.MERCHANT_IN, userId, username,
                            null, username, userId, language, ip);
                }
            }
            return Response.returnSuccess(result);
        }
        tMerchantGroup.setUpdateTime(System.currentTimeMillis());
        TMerchantGroup m = tMerchantGroupMapper.loadIdByGroupName(tMerchantGroup.getGroupName(),tMerchantGroup.getGroupCode());
        if (m !=null){
            return Response.returnFail("存在相同分组名称！");
        }
        Integer count  = tMerchantGroupMapper.insert(tMerchantGroup);
        if (count > 0){
            result.put("id",tMerchantGroupMapper.loadIdByGroupName(tMerchantGroup.getGroupName(),tMerchantGroup.getGroupCode()).getId());
            if (CollectionUtil.isNotEmpty(tMerchantGroup.getMerchantGroupInfos())){
                log.info("初始新增 有详细数据开始新增! size ={}", tMerchantGroup.getMerchantGroupInfos().size());
                for (TMerchantGroupInfo vo : tMerchantGroup.getMerchantGroupInfos()) {
                    TMerchantGroupInfo merchantGroup = new TMerchantGroupInfo();
                    merchantGroup.setCreatTime(vo.getCreatTime());
                    merchantGroup.setMerchantCode(vo.getId());
                    merchantGroup.setMerchantName(vo.getMerchantName());
                    merchantGroup.setMerchantGroupId(tMerchantGroup.getId().intValue());
                    merchantGroup.setOperator("SYS");
                    merchantGroup.setUpdateTime(System.currentTimeMillis());
                    tMerchantGroupInfoMapper.insert(merchantGroup);
                }
            }
        }
        return Response.returnSuccess(result);
    }

    private void saveLogInfo(TMerchantGroup merchantGroupPO, MerchantLogFiledVO filedVO, Map<String, Object> param, String userId, String username, String ip, String language, MerchantLogPageEnum enumInfo, MerchantLogTypeEnum typeInfo, List<String> afters) {
         List<TMerchantGroupInfo> pp= merchantGroupPO.getMerchantGroupInfos();
        for (TMerchantGroupInfo vo : pp) {
            afters.add(vo.getMerchantCode() + "&" + vo.getMerchantName());
        }
        filedVO.setAfterValues(afters);
        merchantLogService.saveLog(enumInfo, typeInfo, filedVO,
                MerchantLogConstants.MERCHANT_IN, userId, username,
                null, username, userId, language, ip);
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
    /**
     * 跟新分组状态
     * @param tMerchantGroup
     * @return
     */
    public Response updateMerchantGroup(TMerchantGroup tMerchantGroup){
        tMerchantGroupMapper.update(tMerchantGroup);
        return Response.returnSuccess();
    }

    /**
     * 获取分组域名
     * @param merchantGroupId
     * @return
     */
    public Response deleteMerchantGroup(Integer merchantGroupId){
        if(tMerchantGroupMapper.delete(merchantGroupId) > 0){
            log.info("删除商户组id {} 成功!",merchantGroupId);
            if (tMerchantGroupInfoMapper.deleteByGroupId(merchantGroupId) > 0){
                log.info("删除分组详细信息成功！merchantGroupId = {}", merchantGroupId);
            }
            if (tDomainMapper.deleteByGroupId(merchantGroupId) > 0){
                log.info("删除域名池信息成功！merchantGroupId = {}", merchantGroupId);
            }
        }
        return Response.returnSuccess();
    }

    /**
     * 设置商户分组
     */
    public void setMerchantGroupInfo(List<ThirdMerchantVo> thirdMerchantVos,Integer merchantGroupId){
        tMerchantGroupInfoMapper.deleteByGroupId(merchantGroupId);
        for (ThirdMerchantVo vo : thirdMerchantVos) {
            TMerchantGroupInfo merchantGroup = new TMerchantGroupInfo();
            merchantGroup.setCreatTime(vo.getCreatTime());
            merchantGroup.setMerchantCode(vo.getId());
            merchantGroup.setMerchantName(vo.getMerchantName());
            merchantGroup.setMerchantGroupId(merchantGroupId);
            merchantGroup.setOperator("SYS");
            merchantGroup.setUpdateTime(System.currentTimeMillis());
            tMerchantGroupInfoMapper.insert(merchantGroup);
        }
    }


    /**
     * 获取域名池
     */
    public Response getDomainList(DomainVo domainVo){
        Map<String, Object> resultMap = new HashMap();
        int count = tDomainMapper.pageListCount(domainVo);
        resultMap.put("total", count);
        if (count == 0) {
            return Response.returnSuccess(resultMap);
        }
        domainVo.setStarNum((domainVo.getPageNum() - 1) * domainVo.getPageSize());
        List<TDomainVo> list = tDomainMapper.pageList(domainVo);
        resultMap.put("list", list);
        return Response.returnSuccess(resultMap);
    }

    /**
     * 保存域名
     */
    public Response saveDomain(Integer userId, DomainVo domainVo){
        if (domainVo.getMerchantGroupId() == null) {
            return Response.returnFail("商户分组id未携带！");
        }
        String[] strArray = domainVo.getDomainName().split(",");
        Set<String> set = Arrays.stream(strArray).collect(Collectors.toSet());
        String username = loginUserService.getLoginUser(userId);
        Integer count = tDomainMapper.checkDomianByEnable(domainVo.getMerchantGroupId(),domainVo.getDomainType());
        for (String value : set) {
            TDomain data = new TDomain();
            data.setDomainType(domainVo.getDomainType());
            data.setDomainName(value.trim());
            data.setCreateTime(System.currentTimeMillis());
            data.setCreateUser(username);
            data.setMerchantGroupId(domainVo.getMerchantGroupId().intValue());
            if (count == 0) {
                data.setEnable(2);
                count = 1;
            }
            try {
                log.info("data = {}", JSON.toJSONString(data));
                tDomainMapper.insert(data);
            } catch (Exception e) {
                log.error("数据重复保存失败！data ={}", JSON.toJSONString(data), e);
            }
        }
        return Response.returnSuccess();
    }

    /**
     * 删除域名
     * @param userId
     * @param id
     */
    public void deleteDomain(Integer userId, Integer id,String ip) {
        String username = loginUserService.getLoginUser(userId);
        //逻辑删除
        TDomain tDomain = tDomainMapper.load(id);
        if (tDomain == null) {
            return;
        }
        if (tDomain.getEnable() == 1) {
            return;
        }
        if (tDomainMapper.delete(id) > 0) {
            //删除成功 记日志
            try {
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add("删除域名" + tDomain.getDomainName());
                vo.getBeforeValues().add(id + "");
                merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_Domian, MerchantLogTypeEnum.EDIT_MERCHANT_Domian, vo,
                        MerchantLogConstants.MERCHANT_IN, userId.toString(), username, null, null, null, Constant.LANGUAGE_CHINESE_SIMPLIFIED,ip);
            } catch (Exception e) {
                log.error("记录日志出错", e);
            }
        }
    }

    /**
     * 删除域名池
     */
    public void deleteDomainList(Integer merchantGroupId){
        tDomainMapper.deleteByGroupId(merchantGroupId);
    }

    /**
     * 手工切换
     * @param userId
     * @param domain
     * @param domainType
     * @param groupId
     * @return
     */
    public Response updateMerchantDomain(HttpServletRequest req,Integer userId,MerchantLogTypeEnum operatType, String oldDomain, String domain, Integer domainType,Integer groupId,String ip,String operator,String requestType) {
        List<TMerchantGroupInfo> tMerchantGroupInfos = tMerchantGroupInfoMapper.tMerchantGroupInfoByGroupId(groupId.longValue());
        if (CollectionUtil.isEmpty(tMerchantGroupInfos)){
            return Response.returnFail("商户组未查询到商户信息");
        }
        DomainVo domainVo = new DomainVo();
        domainVo.setMerchantGroupId(groupId.longValue());
        domainVo.setEnable(1);
        domainVo.setDomainType(domainType);
        domainVo.setMerchantGroupId(groupId.longValue());
        List<TDomain> list = tDomainMapper.selectAll(domainVo);
        if (CollectionUtil.isNotEmpty(list)){
            for (TDomain tDomain : list){
                tDomainMapper.resetDomain(tDomain.getId());
            }
        }
        String username = JWTUtil.getUsername(request.getHeader("token"));
        DomainVo domainVo2 = new DomainVo();
        domainVo2.setDomainName(domain);
        domainVo2.setMerchantGroupId(groupId.longValue());
        List<TDomain> list2 = tDomainMapper.selectAll(domainVo2);
        if (CollectionUtil.isEmpty(list2)) {
            TDomain newDomain = new TDomain();
            newDomain.setEnable(1);
            newDomain.setMerchantGroupId(groupId);
            newDomain.setDomainName(domain.trim());
            newDomain.setDomainType(domainType);
            newDomain.setCreateUser(username);
            newDomain.setEnableTime(System.currentTimeMillis());
            newDomain.setCreateTime(System.currentTimeMillis());
            tDomainMapper.insert(newDomain);
        }else {
            tDomainMapper.updateDomianEnableTimeByid(list2.get(0).getId(),System.currentTimeMillis());
        }
        for (TMerchantGroupInfo merchant : tMerchantGroupInfos){
            //调用第三方通知
            sendMsg(merchant.getMerchantName(),domainType,domain,2);
        }

        String logUserName = req.getHeader("merchantName");
        // 记录日志
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        List<String> fieldName = new ArrayList<>();
        fieldName.add(getDomain(domainType));
        vo.setFieldName(fieldName);
        List<String> beforeValues = new ArrayList<>();
        beforeValues.add(JSON.toJSONString(oldDomain));
        vo.setBeforeValues(beforeValues);
        List<String> afterValues = new ArrayList<>();
        afterValues.add(JSON.toJSONString(domain));
        vo.setAfterValues(afterValues);
        String merchantName = "SYSTEM";
        TMerchantGroup load = merchantGroupMapper.load(groupId);
        if(load !=null){
            merchantName= load.getGroupName();
        }

        if (userId == null){
            userId = 0;
        }
        if (StringUtils.isNotEmpty(operator)){
            username = operator;
        }
        String language = req.getHeader("language");
        merchantLogService.saveLog( "cp".equals(requestType) ? MerchantLogPageEnum.DOMAIN_NAME_CP_SETTINGS: "dj".equals(requestType) ? MerchantLogPageEnum.DOMAIN_NAME_DJ_SETTINGS:MerchantLogPageEnum.DOMAIN_NAME_SETTINGS,
                MerchantLogTypeEnum.DO_DOMAIN, vo,
                MerchantLogConstants.THREE_TERMINAL_AND_MERCHANT_IN, userId.toString(),
                logUserName, merchantName,
                merchantName, "9999999", language,ip,"","",domainType);

        return Response.returnSuccess();
    }

    private String  getDomain(Integer domainType) {
        String name = "";
        switch (domainType){
            case 1 :
                name = DomainTypeEnum.H5.getName() + "域名";
                break;
            case 2:
                name = DomainTypeEnum.PC.getName() + "域名";
                break;
            case 3 :
                name = DomainTypeEnum.APP.getName() + "域名";
                break;
            case 4:
                name = DomainTypeEnum.IMAGE.getName() + "域名";
                break;
            case 5 :
                name = DomainTypeEnum.OTHER.getName() + "域名";
                break;
        }
        return name;
    }

    public abstract void sendMsg(String merchantCode,Integer domainType,String url,Integer changeType);

    public void resetDomain(Integer userId, Long id){
        tDomainMapper.resetDomain(id);
        log.info("恢复成功！");
    }
}
