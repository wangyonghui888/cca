package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.Constant;
import com.panda.multiterminalinteractivecenter.constant.MerchantLogConstants;
import com.panda.multiterminalinteractivecenter.dto.MerchantLogDTO;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.mapper.MerchantLogMapper;
import com.panda.multiterminalinteractivecenter.po.MerchantLogPO;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.IPUtils;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogQueryVO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  ifan
 * @Description :  商户日志接口
 * @Date: 2022-07-11
 */
@Service
@Slf4j
public class MerchantLogServiceImpl implements MerchantLogService, InitializingBean {
    /**
     * 商户日志接口类
     */
    @Autowired
    private MerchantLogMapper merchantLogMapper;

    @Autowired
    MaintenanceLogServiceImpl maintenanceServiceLog;

    @Autowired
    KickUserLogServiceImpl kickUserLogService;

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
    public void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO, Integer tag, String userId, String username, String merchantCode, String merchantName,
                        String dataId, String language ,String ip,String domainSelfResult,String domainThirdResult,Integer domainType) {
        MerchantLogPO merchantLog = new MerchantLogPO();
        if (pageEnum != null) {
            merchantLog.setPageCode(pageEnum.getCode());
            merchantLog.setPageName(pageEnum.getRemark());
        }
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
        if(StringUtils.isNotBlank(dataId) && dataId.contains(StringPool.AMPERSAND) && dataId.contains(StringPool.NULL)) {
            //过滤带"null" 的
            dataId = Arrays.stream(dataId.split(StringPool.AMPERSAND)).filter(s -> !s.equals(StringPool.NULL)).collect(Collectors.joining(StringPool.AMPERSAND));
        }
        merchantLog.setMerchantCode(merchantCode);
        merchantLog.setMerchantName(merchantName);
        merchantLog.setDataId(dataId);
        merchantLog.setLogTag(tag);
        merchantLog.setUserId(userId);
        merchantLog.setUserName(username);
        merchantLog.setOperatTime(System.currentTimeMillis());
        merchantLog.setIp(ip);
        merchantLog.setDomainSelfResult(domainSelfResult);
        merchantLog.setDomainThirdResult(domainThirdResult);
        merchantLog.setDomainType(domainType);
        log.info("saveLog " + merchantLog);
        merchantLogMapper.insert(merchantLog);
    }


    @Override
    @Async
    public void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO, String userId, String dataId, HttpServletRequest request) {
        saveLog(pageEnum, typeEnum, filedVO,MerchantLogConstants.THREE_TERMINAL, userId, JWTUtil.getUsername(request.getHeader("token")), null, filedVO.getMerchantName(),
                dataId, Constant.LANGUAGE_CHINESE, IPUtils.getIpAddr(request), null,null, null);
    }


    /**
     * create MerchantLogFiledVO
     * @param fieldName 操作的字段名
     * @param beforeValues 操作之前的值
     * @param afterValues 操作之后的值
     * @return MerchantLogFiledVO
     */
    public MerchantLogFiledVO createFiledVO(String fieldName, String beforeValues, String afterValues) {
        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        filedVO.getFieldName().add(fieldName);
        if(beforeValues != null) filedVO.getBeforeValues().add(beforeValues);
        if(afterValues != null) filedVO.getAfterValues().add(afterValues);
        return filedVO;
    }

    /**
     * 查询
     *
     * @param queryVO
     * @return
     */
    @Override
    public Response queryLog(MerchantLogQueryVO queryVO) {
        if(StringUtils.isNotBlank(queryVO.getFieldName())) {
            queryVO.setOperatField(queryVO.getFieldName());
        }

        if(StringUtils.isNotBlank(queryVO.getPageCode())) {
            List<String> pageCodeList = new ArrayList<>();
            //44427
            boolean isQueryIP = false;
            boolean b = MerchantLogPageEnum.ALL.getCode().equals(queryVO.getPageCode()) || MerchantLogPageEnum.MAINTENANCE_SET.getCode().equals(queryVO.getPageCode());
            queryVO.setIsQueryKickUserLog(b || MerchantLogPageEnum.KICK_USER.getCode().equals(queryVO.getPageCode()) ? 1 : 0);
            queryVO.setIsQueryMaintenanceLog(b || MerchantLogPageEnum.MAINTENANCE_CONSOLE.getCode().equals(queryVO.getPageCode()) ? 1: 0 );
            if(MerchantLogPageEnum.MAINTENANCE_SET.getCode().equals(queryVO.getPageCode())) {
                pageCodeList = getChildPageCode(MerchantLogPageEnum.MAINTENANCE_SET);
            }else if(MerchantLogPageEnum.DOMAIN_SET_NEW.getCode().equals(queryVO.getPageCode())) {
                isQueryIP = true;
                pageCodeList = getChildPageCode(MerchantLogPageEnum.DOMAIN_SET_NEW);
            } else if (MerchantLogPageEnum.SYSTEM_MANAGE.getCode().equals(queryVO.getPageCode())) {
                pageCodeList = getChildPageCode(MerchantLogPageEnum.SYSTEM_MANAGE);
            }else if (MerchantLogPageEnum.DOMAIN_SET_OLD.getCode().equals(queryVO.getPageCode())) {
                isQueryIP = true;
                pageCodeList = getChildPageCode(MerchantLogPageEnum.DOMAIN_SET_OLD);
            }else if (MerchantLogPageEnum.ALL.getCode().equals(queryVO.getPageCode())) {
                queryVO.setPageCode(null);
            }
            queryVO.setPageCodes(pageCodeList);
            if(MerchantLogPageEnum.ABNORMAL_IP.getCode().equals(queryVO.getPageCode()) || isQueryIP) {
                // 兼容 tob 管理后台
                //queryVO.setPageCode(null);
                queryVO.getPageCodes().addAll(Arrays.asList(MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_45.getCode(),
                        MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_58.getCode(), MerchantLogPageEnum.SET_MERCHANT_INFO_MANAGER_AGENT_59.getCode()));
            }
        }

        int count =  merchantLogMapper.pageListCount(queryVO);
        PageVO<MerchantLogDTO> result = new PageVO<>(count, queryVO.getPageSize(), queryVO.getPageNum());
        if (count == 0) {
            return Response.returnSuccess(result);
        }
        int star = (queryVO.getPageNum() - 1) * queryVO.getPageSize();
        if (star > count) {
            return Response.returnSuccess(result);
        }
        queryVO.setStart(star);
        List<MerchantLogDTO> records = merchantLogMapper.pageList(queryVO);
        records = records.stream().peek(ml -> {
            //整合踢用户、维护日志表， 对应的字段转换后再返回前端
            if (3 == ml.getLogSource() || 4 == ml.getLogSource()) {
                if (StringUtils.isNotBlank(ml.getOperatField())) {
                    ml.setOperatField(JSON.toJSONString(Collections.singletonList(ml.getOperatField())));
                }
                if (StringUtils.isNotBlank(ml.getBeforeValues())) {
                    ml.setBeforeValues(JSON.toJSONString(Collections.singletonList(ml.getBeforeValues())));
                }
                if (StringUtils.isNotBlank(ml.getAfterValues())) {
                    ml.setAfterValues(JSON.toJSONString(Collections.singletonList(ml.getAfterValues())));
                }
            }
        }).collect(Collectors.toList());

        result.setRecords(records);
        return Response.returnSuccess(result);
    }

    @Override
    public List<MerchantPagesTree> loadLogPages() {
        if (outPagesTree != null) {
            return outPagesTree;
        }
        List<MerchantPagesTree> uotList = new ArrayList<>();
        for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
            if (MerchantLogConstants.THREE_TERMINAL.equals(merchantLogPageEnum.getTag())) {
                uotList.add(buildPagesTree(merchantLogPageEnum));
            }
        }
        outPagesTree = listToTree(uotList);
        return outPagesTree;
    }
    @Override
    public Map<String, List<MerchantLogTypeVo>> loadLogType() {
        return typeMap;
    }


    @Override
    public void afterPropertiesSet() {
        //初始化菜单
        if (outPagesTree == null) {
            List<MerchantPagesTree> uotList = new ArrayList<>();
            for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
                if (MerchantLogConstants.THREE_TERMINAL.equals(merchantLogPageEnum.getTag())) {
                    uotList.add(buildPagesTree(merchantLogPageEnum));
                }
            }
            outPagesTree = listToTree(uotList);
        }
        if (intPagesTree == null) {
            List<MerchantPagesTree> inList = new ArrayList<>();
            for (MerchantLogPageEnum merchantLogPageEnum : MerchantLogPageEnum.values()) {
                if (MerchantLogConstants.THREE_TERMINAL.equals(merchantLogPageEnum.getTag())) {
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

    private List<String> getChildPageCode(MerchantLogPageEnum merchantLogPageEnum) {

        ArrayList<String> pageCodeList = new ArrayList<>();

        if (outPagesTree == null) {
            List<MerchantPagesTree> uotList = new ArrayList<>();
            for (MerchantLogPageEnum pageEnum : MerchantLogPageEnum.values()) {
                if (MerchantLogConstants.THREE_TERMINAL.equals(pageEnum.getTag())) {
                    uotList.add(buildPagesTree(pageEnum));
                }
            }
            outPagesTree = listToTree(uotList);
        }
        MerchantPagesTree pageTree = outPagesTree.get(0).getNodes().stream().filter(mpt -> merchantLogPageEnum.getCode().equals(mpt.getCode())).findFirst().orElse(new MerchantPagesTree());
        List<MerchantPagesTree> nodes = pageTree.getNodes();
        nodes.forEach(item -> {
            if(CollectionUtil.isNotEmpty(item.getNodes())) {
                pageCodeList.addAll(item.getNodes().stream().map(MerchantPagesTree::getCode).collect(Collectors.toList()));
            }
            pageCodeList.add(item.getCode());
        });
        return pageCodeList;
    }
}
