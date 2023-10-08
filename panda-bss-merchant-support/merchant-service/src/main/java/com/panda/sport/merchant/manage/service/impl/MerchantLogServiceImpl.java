package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.enums.BizTypeEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.JsonUtils;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.mapper.MerchantLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  商户日志接口
 * @Date: 2020-09-02 14:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service
@Slf4j
public class MerchantLogServiceImpl implements MerchantLogService, InitializingBean {
    /**
     * 商户日志接口类
     */
    @Autowired
    private MerchantLogMapper merchantLogMapper;

    /**
     * 商户外网下来菜单
     */
    private static List<MerchantPagesTree> outPagesTree = null;

    /**
     * 商户内外下拉菜单
     */
    private static List<MerchantPagesTree> intPagesTree = null;

    /**
     * 跟菜单关联的操作类型集合
     */
    private static final Map<String, List<MerchantLogTypeVo>> typeMap = new HashMap<>();

    @Override
    public void saveLog(Integer pageCode, Integer operatType, String merchantName, String dataId) {
    }


    /**
     * @see com.panda.sport.merchant.manage.service.impl.MerchantLogServiceImpl
     * saveLog
     *
     */
    @Override
    public void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO,
                        Integer tag, String userId, String username, String merchantCode, String merchantName,
                        String dataId, String language ,String ip) {
        saveLog(pageEnum,typeEnum,filedVO,tag,userId,username,merchantCode,merchantName,dataId,language,ip,
                null,null,null);
    }

    /**
     * 插入日志
     *
     * @param pageEnum     主功能
     * @param typeEnum     子功能
     * @param filedVO      修改数据集合
     * @param tag          主数据ID
     * @param userId       操作人ID
     * @param username     操作名称
     * @param merchantCode 商户编码
     * @param merchantName 商户名称
     * @param dataId       主数据ID
     * @param language     语言
     * @return 返回保存行数
     */
    @Override
    @Async
    public void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO,
                        Integer tag, String userId, String username, String merchantCode, String merchantName,
                        String dataId, String language,String ip,
                        String domainSelfResult,String domainThirdResult,Integer domainType) {
        MerchantLogPO merchantLog = new MerchantLogPO();
        if (pageEnum != null) {
            merchantLog.setPageCode(pageEnum.getCode());
            merchantLog.setPageName(pageEnum.getRemark());
        }
        merchantLog.setOperatType(typeEnum.getCode());
        merchantLog.setTypeName(Constant.LANGUAGE_ENGLISH.equalsIgnoreCase(language) ? typeEnum.getRemarkEn() : typeEnum.getRemark());
        //设置变更数据的josn
        if (filedVO != null && CollectionUtil.isNotEmpty(filedVO.getFieldName())) {
            merchantLog.setOperatField(JSON.toJSONString(filedVO.getFieldName()));
            if (CollectionUtil.isNotEmpty(filedVO.getAfterValues())) {
                merchantLog.setAfterValues(JSON.toJSONString(filedVO.getAfterValues()));
            }
            if (CollectionUtil.isNotEmpty(filedVO.getBeforeValues())) {
                merchantLog.setBeforeValues(JSON.toJSONString(filedVO.getBeforeValues()));
            }
        }
        merchantLog.setMerchantCode(merchantCode);
        merchantLog.setMerchantName(merchantName);
        merchantLog.setDataId(dataId);
        merchantLog.setLogTag(tag);
        merchantLog.setUserId(userId);
        merchantLog.setUserName(username);
        merchantLog.setOperatTime(System.currentTimeMillis());
        merchantLog.setDomainSelfResult(domainSelfResult);
        merchantLog.setDomainThirdResult(domainThirdResult);
        merchantLog.setDomainType(domainType);
        merchantLog.setIp(ip);
        log.info("saveLog " + merchantLog);
        merchantLogMapper.insert(merchantLog);
    }

    @Override
    public void saveLogNew(String head, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO, Integer tag, String userId, String username, String merchantCode, String merchantName, String dataId, String language, String ip) {
        MerchantLogPO merchantLog = new MerchantLogPO();
        merchantLog.setPageName(head);
        merchantLog.setOperatType(typeEnum.getCode());
        merchantLog.setTypeName(language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH) ? typeEnum.getRemarkEn() : typeEnum.getRemark());
        //设置变更数据的josn
        if (filedVO != null && CollectionUtil.isNotEmpty(filedVO.getFieldName())) {
            merchantLog.setOperatField(JSON.toJSONString(filedVO.getFieldName()));
            if (CollectionUtil.isNotEmpty(filedVO.getAfterValues())) {
                merchantLog.setAfterValues(JSON.toJSONString(filedVO.getAfterValues()));
            }
            if (CollectionUtil.isNotEmpty(filedVO.getBeforeValues())) {
                merchantLog.setBeforeValues(JSON.toJSONString(filedVO.getBeforeValues()));
            }
        }
        merchantLog.setMerchantCode(merchantCode);
        merchantLog.setMerchantName(merchantName);
        merchantLog.setDataId(dataId);
        merchantLog.setLogTag(tag);
        merchantLog.setUserId(userId);
        merchantLog.setUserName(username);
        merchantLog.setOperatTime(System.currentTimeMillis());
        merchantLog.setIp(ip);
        log.info("saveLog " + merchantLog);
        merchantLogMapper.insert(merchantLog);
    }

    @Override
    @Async
    public void savePlusDeductionLog(MerchantLogPageEnum pageEnum, AccountChangeHistoryFindVO vo, Integer logTag, String language,String ip,String userId,String userName) {

        MerchantLogPO merchantLog = new MerchantLogPO();
        if (pageEnum != null) {
            merchantLog.setPageCode(pageEnum.getCode());
            merchantLog.setPageName(pageEnum.getRemark());
        }
        merchantLog.setOperatType(vo.getBizType());
        merchantLog.setTypeName(language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH) ? "Operation plus" : "帐变类型加扣款");
        //设置变更数据的josn
         List<String> fieldName = new ArrayList<>();
         fieldName.add("账变类型*");
         fieldName.add("用户ID");
         fieldName.add("用户名");
         fieldName.add("用户币种");
         fieldName.add("所属商户");
         fieldName.add("注单编号");
         fieldName.add("金额 *");
         fieldName.add("备注");
         List<String> afterValues = new ArrayList<>();
        if (vo != null) {
            if (null!=vo.getBizType()) {
                if(BizTypeEnum.ADD_MONEY_MANUALLY.getCode()==vo.getBizType()){
                    afterValues.add(BizTypeEnum.ADD_MONEY_MANUALLY.getDescribe());
                }
                if(BizTypeEnum.MANUAL_DEBIT.getCode()==vo.getBizType()){
                    afterValues.add(BizTypeEnum.MANUAL_DEBIT.getDescribe());
                }
            }
            if(StringUtils.isNotBlank(vo.getUid())){
                afterValues.add(vo.getUid());
            }else{
                afterValues.add("-");
            }
            if(StringUtils.isNotBlank(vo.getUsername())){
                afterValues.add(vo.getUsername());
            }else{
                afterValues.add("-");
            }

            if(StringUtils.isNotBlank(vo.getCurrencyCode())){
                afterValues.add(vo.getCurrencyCode());
            }else{
                afterValues.add("-");
            }

            if(StringUtils.isNotBlank(vo.getMerchantName())){
                afterValues.add(vo.getMerchantName());
            }else{
                afterValues.add("-");
            }


            if (StringUtils.isNotBlank(vo.getOrderNo())) {
                afterValues.add(vo.getOrderNo());
            }else{
                afterValues.add("-");
            }
            if (null!=vo.getChangeAmount()) {
                afterValues.add(vo.getChangeAmount().toString());
            }else{
                afterValues.add("-");
            }
            if (StringUtils.isNotBlank(vo.getRemark())) {
                afterValues.add(vo.getRemark());
            }else{
                afterValues.add("-");
            }
        }
        if(CollectionUtil.isNotEmpty(afterValues)){
            merchantLog.setAfterValues(JSON.toJSONString(afterValues));
        }
        merchantLog.setOperatField(JSON.toJSONString(fieldName));
        merchantLog.setMerchantCode(vo.getMerchantCode());
        merchantLog.setMerchantName(vo.getMerchantName());
        merchantLog.setDataId(vo.getUid());
        merchantLog.setLogTag(logTag);
        merchantLog.setUserId(StringUtils.isNotBlank(userId)? userId:null);
        merchantLog.setUserName(userName);
        merchantLog.setOperatTime(System.currentTimeMillis());
        merchantLog.setIp(ip);
        log.info("saveLog " + merchantLog);
        merchantLogMapper.insert(merchantLog);
    }
    @Override
    public void saveOperationLog(Integer operationType, String typeName, String pageName, String pageCode,
                                 String merchantCode, String merchantName, String dataId, String operationField,
                                 List<String> beforeValue, List<String> afterValue, HttpServletRequest request) {
        String userId = request.getHeader("user-id");
        String userName = request.getHeader("merchantName");
        MerchantLogPO logPO = new MerchantLogPO();
        logPO.setUserId(userId)
                .setUserName(userName)
                .setOperatType(operationType)
                .setTypeName(typeName)
                .setPageName(pageName)
                .setPageCode(pageCode)
                .setMerchantCode(merchantCode)
                .setMerchantName(merchantName)
                .setDataId(dataId)
                .setOperatField(JsonUtils.listToJson(Collections.singletonList(operationField)))
                .setBeforeValues(JsonUtils.listToJson(beforeValue))
                .setAfterValues(JsonUtils.listToJson(afterValue))
                .setLogTag(1).setOperatTime(System.currentTimeMillis()).setIp(IPUtils.getIpAddr(request));
        // 插入日志
        this.saveLog(logPO);
    }

    @Override
    public void saveOperationRoomLog(Integer operationType, String typeName, String pageName, String pageCode,
                                     String merchantCode, String merchantName, String dataId, List<String> operationField,
                                     List<String> beforeValue, List<String> afterValue, String userName,String userId,String ip) {
        MerchantLogPO logPO = new MerchantLogPO();
        logPO.setUserId(userId)
                .setUserName(userName)
                .setOperatType(operationType)
                .setTypeName(typeName)
                .setPageName(pageName)
                .setPageCode(pageCode)
                .setMerchantCode(merchantCode)
                .setMerchantName(merchantName)
                .setDataId(dataId)
                .setOperatField(JsonUtils.listToJson(operationField))
                .setBeforeValues(JsonUtils.listToJson(beforeValue))
                .setAfterValues(JsonUtils.listToJson(afterValue))
                .setLogTag(1).setOperatTime(System.currentTimeMillis()).setIp(ip);
        // 插入日志
        this.saveLog(logPO);
    }

    @Override
    public void saveLog(MerchantLogPO merchantLog) {
        merchantLogMapper.insert(merchantLog);
    }

    /**
     * 查询
     *
     * @param findVO
     * @return
     */
    @Override
    public PageVO<MerchantLogPO> queryLog(MerchantLogFindVO findVO) {
        int count = merchantLogMapper.pageListCount(findVO);
        PageVO poPageVO = new PageVO<MerchantLogPO>(count, findVO.getPageSize(), findVO.getPageNum());
        if (count == 0) {
            return poPageVO;
        }
        int star = (findVO.getPageNum() - 1) * findVO.getPageSize();
        if (star > count) {
            return poPageVO;
        }
        findVO.setStart(star);
        poPageVO.setRecords(merchantLogMapper.pageList(findVO));
        return poPageVO;
    }

    @Override
    public List<MerchantPagesTree> loadLogOutPages(Integer tag, Integer agentLevel, String language) {
        List<MerchantPagesTree> uotList = new ArrayList<>();
        for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
            if (AgentLevelEnum.AGENT_LEVEL_1.equals(agentLevel)) {
                //过滤二级商户的二级商户管理菜单
                if ("secondary".equals(merchantLogPageEnum.getCode())) {
                    continue;
                }
            }
            if (tag.equals(merchantLogPageEnum.getTag())) {
                uotList.add(buildPagesTree(merchantLogPageEnum));
            }
        }
        return listToTree(uotList);
    }

    @Override
    public List<MerchantPagesTree> loadLogPages(Integer tag, String language) {
        switch (tag) {
            case 0:
                if (outPagesTree != null) {
                    return outPagesTree;
                }
                List<MerchantPagesTree> uotList = new ArrayList<>();
                for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
                    if (tag.equals(merchantLogPageEnum.getTag())) {
                        uotList.add(buildPagesTree(merchantLogPageEnum));
                    }
                }
                outPagesTree = listToTree(uotList);
                if (Constant.LANGUAGE_ENGLISH.equalsIgnoreCase(language)) {
                    List<MerchantPagesTree> resultList = new ArrayList<>();
                    assemblyResultList(outPagesTree, resultList);
                    return resultList;
                } else {
                    return outPagesTree;
                }
            case 1:
                if (intPagesTree != null) {
                    return intPagesTree;
                }
                List<MerchantPagesTree> inList = new ArrayList<>();
                for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
                    if (tag.equals(merchantLogPageEnum.getTag())) {
                        inList.add(buildPagesTree(merchantLogPageEnum));
                    }
                }
                intPagesTree = listToTree(inList);
                if (Constant.LANGUAGE_ENGLISH.equalsIgnoreCase(language)) {
                    List<MerchantPagesTree> resultList = new ArrayList<>();
                    assemblyResultList(intPagesTree, resultList);
                    return resultList;
                } else {
                    return intPagesTree;
                }
        }
        return null;
    }

    private void assemblyResultList(List<MerchantPagesTree> outPagesTree, List<MerchantPagesTree> resultList) {
        for (MerchantPagesTree tree : outPagesTree) {
            MerchantPagesTree newTree = new MerchantPagesTree();
            BeanUtils.copyProperties(tree, newTree);
            newTree.setName(newTree.getEn());
            if (CollectionUtil.isNotEmpty(tree.getNodes())) {
                List<MerchantPagesTree> innerTreesList = new ArrayList<>();
                assemblyResultList(tree.getNodes(), innerTreesList);
                newTree.setNodes(innerTreesList);
            }
            resultList.add(newTree);
        }
    }

    @Override
    public Map<String, List<MerchantLogTypeVo>> loadLogType() {
        return typeMap;
    }

    private static List<MerchantPagesTree> listToTree(List<MerchantPagesTree> oldList) {
        Map<Integer, Object> newMap = new HashMap<>();
        List<MerchantPagesTree> newList = new ArrayList<>();
        for (MerchantPagesTree treeDto : oldList) {
            newMap.put(treeDto.getId(), treeDto);
        }
        for (MerchantPagesTree treeDto : oldList) {
            MerchantPagesTree parent = (MerchantPagesTree) newMap.get(treeDto.getPid());
            if (parent != null) {
                if (parent.getNodes() == null) {
                    List<MerchantPagesTree> ch = new ArrayList<>();
                    ch.add(treeDto);
                    parent.setNodes(ch);
                } else {
                    List<MerchantPagesTree> ch = parent.getNodes();
                    ch.add(treeDto);
                    parent.setNodes(ch);
                }
            } else {
                newList.add(treeDto);
            }
        }
        return newList;
    }

    private static MerchantPagesTree buildPagesTree(MerchantLogPageEnum merchantLogPageEnum) {
        MerchantPagesTree tree = new MerchantPagesTree();
        tree.setId(merchantLogPageEnum.getId());
        tree.setPid(merchantLogPageEnum.getPrentId());
        tree.setCode(merchantLogPageEnum.getCode());
        String[] pageNames = merchantLogPageEnum.getRemark().split("-");
        tree.setName(pageNames[pageNames.length - 1]);
        tree.setEn(merchantLogPageEnum.getEn());
        return tree;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化菜单
        if (outPagesTree == null) {
            List<MerchantPagesTree> uotList = new ArrayList<>();
            for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
                if (MerchantLogConstants.MERCHANT_OUT.equals(merchantLogPageEnum.getTag())) {
                    uotList.add(buildPagesTree(merchantLogPageEnum));
                }
            }
            outPagesTree = listToTree(uotList);
        }
        if (intPagesTree == null) {
            List<MerchantPagesTree> inList = new ArrayList<>();
            for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
                if (MerchantLogConstants.MERCHANT_IN.equals(merchantLogPageEnum.getTag())) {
                    inList.add(buildPagesTree(merchantLogPageEnum));
                }
            }
            intPagesTree = listToTree(inList);
        }
        //初始化操作类型
        for (MerchantLogTypeEnum logTypeEnum : MerchantLogTypeEnum.values()) {
            List<String> pages = logTypeEnum.getPageCode();
            for (String pageId : pages) {
                if (typeMap.containsKey(pageId)) {
                    List<MerchantLogTypeVo> list = typeMap.get(pageId);
                    MerchantLogTypeVo merchantTypeVo = new MerchantLogTypeVo();
                    merchantTypeVo.setId(logTypeEnum.getCode());
                    merchantTypeVo.setName(logTypeEnum.getRemark());
                    merchantTypeVo.setNameEn(logTypeEnum.getRemarkEn());
                    list.add(merchantTypeVo);
                    typeMap.put(pageId, list);
                } else {
                    List<MerchantLogTypeVo> list = new ArrayList<>();
                    MerchantLogTypeVo merchantTypeVo = new MerchantLogTypeVo();
                    merchantTypeVo.setId(logTypeEnum.getCode());
                    merchantTypeVo.setName(logTypeEnum.getRemark());
                    merchantTypeVo.setNameEn(logTypeEnum.getRemarkEn());
                    list.add(merchantTypeVo);
                    typeMap.put(pageId, list);
                }
            }
        }
        log.info("outPagesTree = " + JSON.toJSONString(outPagesTree));
        log.info("intPagesTree = " + JSON.toJSONString(intPagesTree));
        log.info("typeMap = " + JSON.toJSONString(typeMap));
    }
}
