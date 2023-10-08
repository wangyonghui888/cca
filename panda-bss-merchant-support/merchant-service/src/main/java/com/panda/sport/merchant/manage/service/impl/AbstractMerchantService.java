package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.admin.service.ExternalMerchantConfigService;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.MerchantDomainPO;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.MerchantTree;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantKeyVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantVO;
import com.panda.sport.merchant.common.vo.merchant.QueryConditionSettingEditReqVO;
import com.panda.sport.merchant.manage.mq.MerchantProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.MERCHANT_GROUP_COMMON;

@Slf4j
@Service("abstractMerchantService")
public abstract class AbstractMerchantService {
    @Autowired
    protected MerchantMapper merchantMapper;

    @Autowired
    protected TUserMapper tUserMapper;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    protected MerchantProduct merchantProduct;

    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    @Autowired
    private ExternalMerchantConfigService externalMerchantConfigService;

    private final static String merchant_topic = "panda_order_";

    private final static Integer H5_DOMAIN = 1;
    private final static Integer PC_DOMAIN = 2;
    private final static Integer APP_DOMAIN = 3;
    private final static Integer PIC_DOMAIN = 4;
    private final static Integer OTH_DOMAIN = 5;


    /**
     * 新建商户(直营,一级,二级)
     * 发送通告
     *
     * @Param: [merchantVO]
     * @return: com.panda.sport.merchant.common.enums.ResponseEnum
     * @date: 2020/8/23 15:30
     */
    protected ResponseEnum abstractCreateMerchant(MerchantVO merchantVO) throws Exception {
        if (StringUtils.isAnyEmpty(merchantVO.getMerchantName(), merchantVO.getMerchantCode(),
                merchantVO.getEndTime(), merchantVO.getStartTime())) {
            return ResponseEnum.PARAMETER_INVALID;
        }
        String now = DateUtil.now();
        merchantVO.setCreateTime(now);
        merchantVO.setUpdateTime(now);
        if (merchantVO.getAgentLevel() == 2 && StringUtils.isEmpty(merchantVO.getParentId())) {
            return ResponseEnum.MERCHANT_PARENT_NOT_EXIST;
        }
        if (merchantVO.getAgentLevel() != 2 && merchantVO.getAgentLevel() != 10 && (merchantVO.getLevel() == null || StringUtils.isEmpty(merchantVO.getLevelName()))) {
            return ResponseEnum.MERCHANT_LEVEL_NOT_EXIST;
        }
        if (merchantMapper.countByParam(merchantVO.getMerchantName(), null, null) > 0) {
            return ResponseEnum.MARCHANT_NAME_IS_EXIST;
        }
        if (merchantMapper.countByParam(null, merchantVO.getMerchantCode(), null) > 0) {
            return ResponseEnum.MARCHANT_CODE_IS_EXIST;
        }
        if (merchantVO.getMerchantTag() != null && merchantVO.getMerchantTag() == 1) {
            merchantVO.setTagMarketLevel(1);
            merchantVO.setOpenEsport(0);
        } else {
            merchantVO.setOpenEsport(1);
        }
        //渠道名称
         String parentName = null;
        if (merchantVO.getAgentLevel() == 2) {
            MerchantPO parent = merchantMapper.selectById(merchantVO.getParentId());
            Integer transferMode = parent.getTransferMode();
            merchantVO.setTransferMode(transferMode);
            merchantVO.setMerchantKey(parent.getMerchantKey());
            if (parent.getChildMaxAmount() < parent.getChildAmount()) {
                return ResponseEnum.MERCHANT_AMOUNT_EXCEED_LIMIT;
            }
            String start = merchantVO.getStartTime();
            String end = merchantVO.getEndTime();
            String pStart = parent.getStartTime();
            String pEnd = parent.getEndTime();
            Date cStart = DateUtils.parseDate(start, "yyyy-MM-dd");
            Date cEnd = DateUtils.parseDate(end, "yyyy-MM-dd");
            Date pStartDate = DateUtils.parseDate(pStart, "yyyy-MM-dd");
            Date pEndDate = DateUtils.parseDate(pEnd, "yyyy-MM-dd");
            if (cStart.before(pStartDate) || cEnd.after(pEndDate)) {
                log.error("二级商户的有效期要在父级商户内!");
                return ResponseEnum.MERCHANT_CHILD_EXPIRE_PARENT;
            }
            //如果是渠道用户是没有parentCodede,那他是本身
            parentName = parent.getMerchantName();
            merchantVO .setParentCode(parent.getMerchantCode());
            merchantVO.setWhiteIp(parent.getWhiteIp());
            merchantVO.setComputingStandard(parent.getComputingStandard());
            merchantVO.setTechniqueAmount(parent.getTechniqueAmount());
            merchantVO.setPaymentCycle(parent.getPaymentCycle());
            merchantVO.setVipAmount(parent.getVipAmount());
            merchantVO.setVipPaymentCycle(parent.getVipPaymentCycle());
            merchantVO.setTerraceRate(parent.getTerraceRate());
            merchantVO.setTechniquePaymentCycle(parent.getTechniquePaymentCycle());
            merchantVO.setDomainGroupCode(parent.getDomainGroupCode());
        } else if (merchantVO.getAgentLevel() == 0 || merchantVO.getAgentLevel() == 1) {
            if (merchantVO.getAgentLevel() == 1 && StringUtils.isNotEmpty(merchantVO.getParentId())) {
                MerchantPO parent = merchantMapper.selectById(merchantVO.getParentId());
                merchantVO.setComputingStandard(parent.getComputingStandard());
                merchantVO.setTechniqueAmount(parent.getTechniqueAmount());
                merchantVO.setPaymentCycle(parent.getPaymentCycle());
                merchantVO.setVipAmount(parent.getVipAmount());
                merchantVO.setVipPaymentCycle(parent.getVipPaymentCycle());
                merchantVO.setTerraceRate(parent.getTerraceRate());
                merchantVO.setTechniquePaymentCycle(parent.getTechniquePaymentCycle());
            }
            merchantVO.setBookBet(Constant.SWITCH_ON);
            String key =AESUtils.aesEncode(CreateSecretKey.keyCreate());
            merchantVO.setMerchantKey(key);
            merchantVO.setDomainGroupCode(MERCHANT_GROUP_COMMON);
        }
        MerchantPO merchantPO = new MerchantPO();
        BeanUtils.copyProperties(merchantVO, merchantPO);
        merchantPO.setId(SnowFlakeUtil.getId() + "");
        merchantPO.setTopic(merchant_topic + merchantPO.getMerchantCode());
        int num = merchantMapper.insertMerchant(merchantPO);
        if (num < 1)  {
            return ResponseEnum.FAIL;
        }
        if (merchantVO.getAgentLevel() == 0 || merchantVO.getAgentLevel() == 2) {
            try {
                //商户直营与二级商户设置配置
                log.info("初始化商户配置！");
                MerchantConfig merchantConfig = new MerchantConfig();
                merchantConfig.setMerchantCode(merchantPO.getMerchantCode());
                merchantConfig.setUserPrefix(merchantVO.getUserPrefix());
                merchantConfig.setDefaultLanguage(merchantVO.getDefaultLanguage());
                merchantConfig.setMerchantTag(merchantVO.getMerchantTag());
                merchantConfig.setVideoDomain(merchantVO.getVideoDomain());
                merchantConfig.setTagMarketLevel(merchantVO.getTagMarketLevel());
                merchantConfig.setThreeDayAmount(Double.valueOf(500));
                merchantConfig.setSevenDayAmount(Double.valueOf(1500));
                merchantConfig.setSettleSwitchAdvance(Constant.SWITCH_ON);
                merchantConfigMapper.insert(merchantConfig);
            } catch (Exception e) {
                log.error("初始化配置异常！", e);
            }
        }

        //新建商户时同步往t_merchant_key表写入新建的商户数据
        MerchantKeyVO merchantKeyVO = new MerchantKeyVO();
        merchantKeyVO.setMerchantCode(merchantPO.getMerchantCode());
        merchantKeyVO.setKeyStatus(merchantPO.getStatus());
        merchantKeyVO.setEnableTime(System.currentTimeMillis());
        merchantKeyVO.setSecondEnableTime(System.currentTimeMillis());
        merchantKeyVO.setCreatedBy(merchantPO.getCreatedBy());
        merchantKeyVO.setCreateTime(String.valueOf(System.currentTimeMillis()));
        merchantKeyVO.setUpdatedBy(merchantPO.getCreatedBy());
        merchantKeyVO.setUpdateTime(String.valueOf(System.currentTimeMillis()));
        merchantMapper.addMerchantKey(merchantKeyVO);

        merchantVO.setId(merchantPO.getId());

        //如果是二级用户就设置渠道下发
        if(merchantVO.getAgentLevel() == 2){
            merchantPO.setParentName(parentName);
        }

        // 发送消息
        MerchantNews merchantNewsPo = new MerchantNews();
        String content = merchantVO.getMerchantName() + "于" + now + "正式入驻Panda" + "点击<a href='update'>此处</a>查看详情。";
        merchantNewsPo.setTitle(merchantVO.getMerchantName() + "入驻Panda").setContext(content)
                .setMerchantCode(merchantVO.getMerchantCode()).setMerchantName(merchantVO.getMerchantName()).setIsMerchant(1)
                .setSendTime(System.currentTimeMillis()).setCreateTime(System.currentTimeMillis()).setType(1);
        this.insertMerchantNews(merchantNewsPo);
        //创建商户后，发送全量信息到MQ  给风控
        if (merchantVO.getAgentLevel() != 10) {
            merchantPO.setTagMarketLevel(merchantVO.getTagMarketLevel());
            merchantProduct.sendMessage(merchantPO);
        }

        //重置密码0关
        if (StringUtils.isNotEmpty(merchantVO.getMerchantCode())) {
            QueryConditionSettingEditReqVO queryConditionVO = new QueryConditionSettingEditReqVO();
            queryConditionVO.setMerchantCode(merchantVO.getMerchantCode());
            queryConditionVO.setResetPasswordSwitch(0);
            externalMerchantConfigService.editQueryConditionSetting(queryConditionVO);
        }

        return ResponseEnum.SUCCESS;
    }

    protected abstract void insertMerchantNews(MerchantNews merchantNews);

    /**
     * 修改商户状态
     *
     * @Param: [merchantCode, status]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:32
     */
    protected MerchantPO abstractUpdateMerchantStatus(String merchantCode, String status,String remark) {
        MerchantPO merchantPO = merchantMapper.getMerchantInfo(merchantCode);
        /*if(10==merchantPO.getAgentLevel()){//需求变更 ：禁用代理商时，无关渠道商户状态。
            merchantMapper.batchUpdateMerchantStatusByAgent(merchantPO.getId(),Integer.valueOf(status));
        }*/
        if(null != merchantPO) {
            merchantMapper.updateMerchantStatus(merchantCode, Integer.valueOf(status), remark);

            //修改商户状态后，发送全量信息到MQ  给风控
            merchantPO.setStatus(Integer.valueOf(status));
            merchantPO.setUpdateTime(DateUtil.now());
            //发送消息时会将merchantName置为null,导致返回结果为null
            String merchantName = merchantPO.getMerchantName();
            merchantProduct.sendMessage(merchantPO);
            merchantPO.setMerchantName(merchantName);

            CompletableFuture.runAsync(()->{
                if ("0".equals(status)) {
                    tUserMapper.updateAllMerchantUser(merchantCode, 1);
                    kickOutAllUsers(merchantCode);
                } else {
                    tUserMapper.updateAllMerchantUser(merchantCode, 0);
                }
            });
        }
        return merchantPO;
    }

    /**
     * 修改商户状态
     *
     * @Param: [merchantCode, status]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:32
     */
    protected MerchantPO abstractUpdateMerchantBackendStatus(String merchantCode, String status) {
        MerchantPO merchantPO = merchantMapper.getMerchantInfo(merchantCode);
        if(null != merchantPO) {
            merchantMapper.updateMerchantBackendStatus(merchantCode, Integer.valueOf(status));
        }
        return merchantPO;
    }

    /**
     * 修改下级商户状态
     * @param merchantCode
     * @param status
     * @return
     */
    protected List<MerchantPO> abstractUpdateChildrenMerchantStatus(String merchantCode, String status){
        MerchantPO merchantPO = merchantMapper.getMerchantInfo(merchantCode);
        List<MerchantPO> childrenMerchantList = merchantMapper.queryChildrenMerchant(merchantPO.getId());
        if(CollectionUtils.isNotEmpty(childrenMerchantList)){
            List<String> merchantCodeList = childrenMerchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
            merchantMapper.updateChildrenMerchantStatus(merchantCodeList, Integer.valueOf(status));
            //修改商户状态后发送全量到MQ给风控
            for (MerchantPO merchant : childrenMerchantList){
                merchant.setStatus(Integer.valueOf(status));
                merchant.setUpdateTime(DateUtil.now());
                merchantProduct.sendMessage(merchant);
            }
            /*CompletableFuture.runAsync(()->{
                if ("0".equals(status)) {
                    tUserMapper.updateAllChildrenMerchantUser(merchantCodeList, 1);
                } else {
                    tUserMapper.updateAllChildrenMerchantUser(merchantCodeList, 0);
                }
            });*/
        }
        return childrenMerchantList;
    }

    /**
     * 修改下级商户登录状态
     * @param merchantCode
     * @param status
     * @return
     */
    protected List<MerchantPO> abstractUpdateChildrenMerchantBackendStatus(String merchantCode, String status){
        MerchantPO merchantPO = merchantMapper.getMerchantInfo(merchantCode);
        List<MerchantPO> childrenMerchantList = merchantMapper.queryChildrenMerchant(merchantPO.getId());
        if(CollectionUtils.isNotEmpty(childrenMerchantList)){
            List<String> merchantCodeList = childrenMerchantList.stream().map(MerchantPO::getMerchantCode).collect(Collectors.toList());
            merchantMapper.updateChildrenMerchantBackendStatus(merchantCodeList, Integer.valueOf(status));
        }
        return childrenMerchantList;
    }

    protected MerchantPO abstractDeleteSubAgent(String merchantCode, String parentId) {
        MerchantPO merchantPO = merchantMapper.getMerchantInfo(merchantCode);
        merchantMapper.deleteSubAgent(merchantCode, parentId);
        localCacheService.invalidateAll();
        return merchantPO;
    }

    protected List<MerchantTree> abstractGetMerchantTree(String key) {
        List<MerchantTree> treeList = (List<MerchantTree>) LocalCacheService.cacheMap.getIfPresent(key);
        log.info("查询treeList:" + (treeList == null ? 0 : treeList.size()));
        if (CollectionUtils.isEmpty(treeList)) {
            treeList = localCacheService.getTreeList(key);
        }
        return treeList;
    }

    protected List<TMerchantKey> assemblykeyList(List<TMerchantKey> queryKeyList, String language, Integer type) {
        Date now = new Date();
        for (TMerchantKey key : queryKeyList) {
            Integer status = key.getStatus();
            if(StringUtils.isNotEmpty(key.getKey()) && (null != type && type == Constant.INTERNAL_MERCHANT)) {
                key.setKey("***");
            }else if(StringUtils.isNotEmpty(key.getSecondMerchantKey()) && (null != type && type == Constant.INTERNAL_MERCHANT)){
                key.setSecondMerchantKey("***");
            }else if(StringUtils.isNotEmpty(key.getKey())){
                key.setKey(AESUtils.aesDecode(key.getKey()));
                if(StringUtils.isNotEmpty(key.getSecondMerchantKey())){
                    key.setSecondMerchantKey(AESUtils.aesDecode(key.getSecondMerchantKey()));
                }
            }
            try {
                Date end = DateUtils.parseDate(key.getEndTime(), "yyyy-MM-dd");
                Date before = DateUtils.addDays(end, -30);
                if (status == 0) {
                    key.setStatusDescription(language.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "已过期" : "Expired");

                } else if (status == 1 && before.before(now)) {
                    key.setStatusDescription(language.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "即将过期" : "ExpiredSoon");
                } else {
                    key.setStatusDescription(language.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "有效" : "Using");
                }
            } catch (ParseException e) {
                log.error("组装Key列表!", e);
            }
        }
        return queryKeyList;
    }

    /**
     * 查询商户列表
     *
     * @Param: [merchantCode, status]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:32
     */
    protected PageInfo<MerchantVO> queryMerchantList(MerchantVO merchantVO) {
        // 关联t_merchant_config表返回商户配置信息
        if (StringUtils.isNotEmpty(merchantVO.getMerchantCode())) {
            MerchantPO merchantPO = merchantMapper.getMerchantByKanaCode(merchantVO.getMerchantCode());
            if (merchantPO != null) {
                merchantVO.setMerchantCode(merchantPO.getMerchantCode());
            }
        }
        Integer pageIndex = merchantVO.getPageIndex();
        Integer pageSize = merchantVO.getPageSize();
        String sort = merchantVO.getSort();
        String orderBy = merchantVO.getOrderBy();
        pageSize = (pageSize == null || pageSize == 0) ? 20 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageIndex;
        // 排序规则
        sort = StringUtils.isEmpty(sort) ? Constant.ASC : sort;
        // 排序字段
        orderBy = StringUtils.isEmpty(orderBy) ? "createTime" : orderBy;
        String orderStr = orderBy + " " + sort;
        if ("".equals(orderBy)) {
            orderStr = "level desc,createTime desc";
        }
        // 分页
        PageHelper.startPage(pageIndex, pageSize, orderStr);
        MerchantPO merchantPO = new MerchantPO();
        BeanUtils.copyProperties(merchantVO, merchantPO);
        log.info("AbstractMerchantService queryMerchantList param=" + merchantPO);
        List<MerchantPO> list = merchantMapper.selectList(merchantPO);
        for (MerchantPO po : list){
            if(StringUtils.isNotEmpty(po.getCurrencyCode())){
                String str [] = po.getCurrencyCode().split(",");
                Set<String> set = new HashSet<>(Arrays.asList(str));
                List<String> currencyCnList = Lists.newArrayList();
                for (String item : set){
                    String currencyCn = CurrencyUtils.convertCurrencyCode(item);
                    currencyCnList.add(currencyCn);
                }
                po.setCurrencyCode(currencyCnList.toString().substring(1, currencyCnList.toString().length()-1));
            }

            //精彩回放
            Integer eventSwitch = 0;
            if (StringUtils.isNotEmpty(po.getMerchantEvent())) {
                Map<String, Object> erchantEventMap = JsonUtils.jsonToMap(po.getMerchantEvent());
                eventSwitch = erchantEventMap.containsKey("eventSwitch") ? Integer.valueOf(erchantEventMap.get("eventSwitch").toString()) : 0;
            }
            po.setEventSwitch(eventSwitch);
            //统计商户证书数量
            int count = 0;
            if(StringUtils.isNotEmpty(po.getMerchantKey())){
                count ++;
                po.setMerchantKeyCount(count);
            }
            if(StringUtils.isNotEmpty(po.getSecondMerchantKey())){
                count ++;
                po.setMerchantKeyCount(count);
            }
        }
        this.queryMerchantDomain(list);
        PageInfo<MerchantPO> pageInfo = new PageInfo<>(list);
        List<MerchantVO> voList = Lists.newArrayList();
        // 处理渠道商户的backend字段
        List<MerchantPO> list1 = pageInfo.getList();
        if(CollectionUtils.isNotEmpty(list1)){
            for (MerchantPO e : list1) {
                voList.add(convertToVO(e));
            }
        }

        PageInfo<MerchantVO> pageInfo2 = new PageInfo<>();
        BeanUtils.copyProperties(pageInfo, pageInfo2);
        pageInfo2.setList(voList);
        return pageInfo2;
    }

    /**
     * 查询商户域名
     * @param list
     */
    private void queryMerchantDomain(List<MerchantPO> list) {
        if(CollectionUtils.isNotEmpty(list)){
            List<String> merchantIdList = list.stream().map(MerchantPO::getId).collect(Collectors.toList());
            List<MerchantDomainPO> merchantDomainList = merchantMapper.selectMerchantDomainList(merchantIdList);
            Map<String,List<MerchantDomainPO>> merchantDomainMap = merchantDomainList.stream().filter(MerchantDomainPO -> MerchantDomainPO.getMerchantCode() != null).collect(Collectors.groupingBy(MerchantDomainPO::getMerchantCode));
            merchantDomainMap.forEach((merchantCode, MerchantDomainList)->{
                Map<Integer,List<MerchantDomainPO>> domainMap = MerchantDomainList.stream().filter(merchantDomainPO -> merchantDomainPO.getDomainType() != null).collect(Collectors.groupingBy(MerchantDomainPO::getDomainType));
                for (MerchantPO item : list){
                    domainMap.forEach((domainType,domainList)->{
                        if(StringUtils.equals(item.getMerchantCode(), merchantCode)){
                          /*  if(domainType.equals(H5_DOMAIN)){
                                item.setH5DomainList(domainList);
                            }else if(domainType.equals(PC_DOMAIN)){
                                item.setPcDomainList(domainList);
                            }else if(domainType.equals(APP_DOMAIN)){
                                item.setAppDomainList(domainList);
                            }else*/ if(domainType.equals(PIC_DOMAIN)){
                                item.setPicDomainList(domainList);
                            }else{
                                item.setOthDomainList(domainList);
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 查询单个商户详情
     *
     * @Param: [merchantCode, status]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:32
     */
    public MerchantVO abstractMerchantDetail(String id) {
        MerchantPO merchantPO = merchantMapper.selectById(id);
        List<MerchantPO> list = Lists.newArrayList();
        list.add(merchantPO);
        this.queryMerchantDomain(list);
        MerchantVO merchantVO = convertToVO(list.get(0));
        log.info(id + ",result:abstractMerchantDetail:" + merchantVO);
        if (merchantVO.getAgentLevel() == 2) {
            merchantVO.setParent(merchantMapper.selectById(merchantVO.getParentId()));
        }
        return merchantVO;
    }

    /**
     * 修改商户
     *
     * @Param: [merchantCode, status]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:32
     */
    @Transactional(rollbackFor = Exception.class)
    public Response abstractUpdateMerchant(MerchantVO merchantVO) throws Exception {
        if (StringUtils.isEmpty(merchantVO.getId())) {
            return Response.returnFail(ResponseEnum.DELETE_FAILS);
        }
        MerchantPO merchantPO = convertToPO(merchantVO);
        MerchantPO merchant = merchantMapper.selectById(merchantVO.getId());
        merchantPO.setMerchantCode(merchant.getMerchantCode());
        merchantPO.setMerchantTag(merchant.getMerchantTag());
        if (StringUtils.isNotEmpty(merchantVO.getMerchantName())) {
            if (!merchant.getMerchantName().equals(merchantVO.getMerchantName())) {
                Map<String, String> map = Maps.newHashMap();
                map.put("merchantName", merchantVO.getMerchantName());
                if (countByParam(map) > 0) {
                    return Response.returnFail(ResponseEnum.MARCHANT_NAME_IS_EXIST);
                }
            }
        }
        if (merchant.getAgentLevel() == 1) {
            Integer newChildConnectMode = merchantVO.getChildConnectMode();
            Integer oldChildConnectMode = merchant.getChildConnectMode();
            log.info("newChildConnectMode:" + newChildConnectMode + "  oldChildConnectMode:" + oldChildConnectMode);
            merchantMapper.updateChildMerchant(merchant.getId(), merchantPO.getStartTime(), merchantPO.getEndTime());
            if (!StringUtils.isAllEmpty(merchantVO.getWhiteIp())) {
                merchantVO.setId(merchant.getId());
                merchantMapper.updateChildrenIpAndDomain(merchantVO);
            } else {
                merchantMapper.updateChildMerchantAmount(merchant.getId(), merchantPO.getComputingStandard(), merchantPO.getTechniqueAmount(), merchantPO.getPaymentCycle(),
                        merchantPO.getVipAmount(), merchantPO.getVipPaymentCycle(), merchantPO.getTerraceRate(), merchantPO.getTechniquePaymentCycle());
            }
            if (newChildConnectMode != null && newChildConnectMode == 2) {
                log.info("更新商户转账模式:" + merchantPO.getTransferMode());
                merchantMapper.updateChildTransferMode(merchant.getId(), merchantPO.getTransferMode(), merchantPO.getUrl(), merchantPO.getCallbackUrl());
                log.info("更新子商户key:" + merchant.getMerchantKey() + ",start:" + merchantVO.getStartTime() + ",end:" + merchantVO.getEndTime() + ",merchantId:" + merchant.getId());
                merchantMapper.forceUpdateChildrenKey(merchant.getId(), merchant.getMerchantKey(), merchantVO.getStartTime(), merchantVO.getEndTime(), 1);
            } else if (newChildConnectMode != null && newChildConnectMode == 1 && oldChildConnectMode == 2) {
                MerchantVO paramVo = new MerchantVO();
                paramVo.setParentId(merchantVO.getId());
                List<TMerchantKey> childrenList = merchantMapper.queryKeyList(paramVo);
                if (CollectionUtils.isNotEmpty(childrenList)) {
                    log.info("查询出子商户:" + childrenList.size());
                    for (TMerchantKey t : childrenList) {
                        merchantMapper.updateHistoryKey(t.getMerchantCode(), CreateSecretKey.keyCreate(), t.getKey());
                    }
                }
            }

        } else if (merchant.getAgentLevel() == 2) {
            MerchantPO parent = merchantMapper.selectById(merchant.getParentId());
            String start = merchant.getStartTime();
            String end = merchant.getEndTime();
            String pStart = parent.getStartTime();
            String pEnd = parent.getEndTime();
            Date cStart = DateUtils.parseDate(start, "yyyy-MM-dd");
            Date cEnd = DateUtils.parseDate(end, "yyyy-MM-dd");
            Date pStartDate = DateUtils.parseDate(pStart, "yyyy-MM-dd");
            Date pEndDate = DateUtils.parseDate(pEnd, "yyyy-MM-dd");
            if (cStart.before(pStartDate) || cEnd.after(pEndDate)) {
                log.info("二级商户的有效期要在父级商户内!");
                return Response.returnFail(ResponseEnum.MERCHANT_CHILD_EXPIRE_PARENT);
            }
            merchantPO.setWhiteIp(parent.getWhiteIp());
        }

        if (merchant.getTransferMode() != null && merchant.getTransferMode() == 1) {
            if(merchantPO.getUrl()!=null && !merchantPO.getUrl().equals(merchant.getUrl())){
                String url = merchantPO.getUrl() == null ? merchant.getUrl() : merchantPO.getUrl();
                String callBackUrl = merchantPO.getCallbackUrl() == null ? merchant.getCallbackUrl() : merchantPO.getCallbackUrl();
                String balanceUrl = merchantPO.getBalanceUrl() == null ? merchant.getBalanceUrl() : merchantPO.getBalanceUrl();
                merchantMapper.updateChildUrl(merchant.getId(), url, callBackUrl, balanceUrl);
            }
        }else{
            if(merchantPO.getCallbackUrl()!=null && !merchantPO.getCallbackUrl().equals(merchant.getCallbackUrl())){
                String url = merchantPO.getUrl() == null ? merchant.getUrl() : merchantPO.getUrl();
                String callBackUrl = merchantPO.getCallbackUrl() == null ? merchant.getCallbackUrl() : merchantPO.getCallbackUrl();
                String balanceUrl = merchantPO.getBalanceUrl() == null ? merchant.getBalanceUrl() : merchantPO.getBalanceUrl();
                merchantMapper.updateChildUrl(merchant.getId(), url, callBackUrl, balanceUrl);
            }
        }
        // 创建t_merchant表管理员
        this.createAdminUser(merchantPO);
        //商户信息表保存加密帐号
        //商户信息表保存加密帐号
        if(StringUtils.isNotEmpty(merchantPO.getAdminPassword())){
            String  adminPassword=merchantPO.getAdminPassword();
            log.info("加密1："+merchantPO.getMerchantCode()+":"+merchantPO.getAdminPassword());
            merchantPO.setAdminPassword(AESUtils.aesEncode(adminPassword));
            merchantPO.setAdminpswCode(AESUtils.aesEncode(adminPassword));//保存明码
            merchantPO.setPswCode(AESUtils.aesEncode(adminPassword));//保存明码
            log.info("加密2："+merchantPO.getMerchantCode()+":"+merchantPO.getAdminPassword());
        }
        int num = merchantMapper.updateMerchant(merchantPO);
        if (merchantPO.getUserPrefix() != null || merchantPO.getDefaultLanguage() != null|| merchantPO.getVideoDomain() != null) {
            MerchantConfig merchantConfig = new MerchantConfig();
            merchantConfig.setDefaultLanguage(merchantPO.getDefaultLanguage());
            merchantConfig.setUserPrefix(merchantPO.getUserPrefix());
            merchantConfig.setMerchantCode(merchant.getMerchantCode());
            merchantConfig.setVideoDomain(merchantPO.getVideoDomain());
            merchantConfigMapper.updateMerchantConfig(merchantConfig);
        }
        if (num < 1) {
            Response.returnFail(ApiResponseEnum.FAIL);
        }
        if (merchant.getAgentLevel() == 1 && merchantPO.getStartTime() != null && merchantPO.getEndTime() != null) {
            merchantMapper.updateChildMerchant(merchant.getId(), merchantPO.getStartTime(), merchantPO.getEndTime());
        }
        if (StringUtils.isNoneEmpty(merchantVO.getStartTime(), merchantVO.getEndTime())) {
            TMerchantKey old = merchantMapper.getMerchantKeyPo(merchant.getMerchantCode());
            if (!merchantVO.getStartTime().equals(old.getStartTime()) || !merchantVO.getEndTime().equals(old.getEndTime())) {
                abstractUpdateKey(merchant.getMerchantCode(), merchant.getMerchantKey(), null,  merchantVO.getStartTime(),
                        merchantVO.getEndTime(), merchantVO.getUpdatedBy());
            }
        }
        // 更新用户的密码的时候，后台密码的更改赋值
        if (StringUtils.isEmpty(merchantPO.getMerchantAdmin())) {
            merchantPO.setMerchantAdmin(merchant.getMerchantAdmin());
            merchantPO.setMerchantCode(merchant.getMerchantCode());
            merchantPO.setAgentLevel(merchant.getAgentLevel());
        }


        //修改商户信息后，发送全量信息到MQ  给风控
        MerchantPO merchantInfo = merchantMapper.selectById(merchantVO.getId());
        merchantProduct.sendMessage(merchantInfo);

        //重置密码0关--
        if (StringUtils.isNotEmpty(merchantVO.getMerchantCode())) {
            QueryConditionSettingEditReqVO queryConditionVO = new QueryConditionSettingEditReqVO();
            queryConditionVO.setMerchantCode(merchantVO.getMerchantCode());
            queryConditionVO.setResetPasswordSwitch(0);
            externalMerchantConfigService.editQueryConditionSetting(queryConditionVO);
        }

        if(StringUtils.isNotEmpty(merchantPO.getAdminPassword())){
            String  adminPassword=merchantPO.getAdminPassword();
            log.info("加密3："+merchantPO.getMerchantCode()+":"+adminPassword);
            merchantPO.setAdminPassword(AESUtils.aesDecode(adminPassword));
            merchantPO.setAdminpswCode(AESUtils.aesDecode(adminPassword));//保存明码
            merchantPO.setPswCode(AESUtils.aesDecode(adminPassword));//保存明码
            log.info("加密4："+merchantPO.getMerchantCode()+":"+merchantPO.getAdminPassword());
        }
        return Response.returnSuccess(merchantPO);
    }


    /**
     * 新建商户管理员
     *
     * @Param: [merchantPO]
     * @return: void
     * @date: 2020/8/23 15:34
     */
    protected abstract void createAdminUser(MerchantPO merchantPO);

    protected boolean abstractUpdateKey(String merchantCode, String key, String keyLabel, String startTime, String endTime, String modifier)
            throws Exception {
        MerchantPO po = merchantMapper.getMerchantByMerchantCode(merchantCode);
        String endT = po.getEndTime();
        Date endD = DateUtils.parseDate(endT, "yyyy-MM-dd");

        Date start = DateUtils.parseDate(startTime, "yyyy-MM-dd");
        Date end = DateUtils.parseDate(endTime, "yyyy-MM-dd");
        if (start.after(end)) {
            log.warn("开始时间位于结束之后!" + start + "," + end);
            return false;
        }
        if (end.after(endD)) {
            log.warn("结束时间位于商户结束之后!" + end + "," + endD);
            return false;
        }
        String old = merchantMapper.getKey(merchantCode, keyLabel);
        int status = 1;
        Date now = getStartTime(new Date());
        String nowStr = DateFormatUtils.format(now, "yyyyMMddHHmmss");
        long nowL = Long.parseLong(nowStr);
        Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd");
        String endStr = DateFormatUtils.format(endDate, "yyyyMMddHHmmss");
        long endL = Long.parseLong(endStr);
        if (endL < nowL) {
            status = 0;
        }
        if (old != null && old.equals(key)) {
            //merchantMapper.updateKey(key,modifier);
        } else {
            if (StringUtils.isNotEmpty(keyLabel) && keyLabel.equalsIgnoreCase(Constant.REALTIME_TABLE)){
                merchantMapper.updateSecondHistoryKey(merchantCode, key, old);
            }else {
                merchantMapper.updateHistoryKey(merchantCode, key, old);
            }
        }
        log.info("更新下级商户失效时间start!" + po.toString());
        if (po.getAgentLevel() != null && po.getAgentLevel() == 1 && po.getChildConnectMode() != null) {
            if (StringUtils.isNotEmpty(keyLabel) && keyLabel.equalsIgnoreCase(Constant.REALTIME_TABLE)){
                List<String> codeList = merchantMapper.queryChildren(merchantCode);
                if (CollectionUtils.isNotEmpty(codeList)){
                    merchantMapper.updateChildrenSecondKey(key, old, modifier, codeList);
                }
            }else {
                merchantMapper.updateChildrenKey(po.getId(), key, startTime, endTime, status, modifier);
            }
            //   merchantMapper.updateUnchangeChildrenKey(po.getId(), startTime, endTime, status, modifier);
        }
        //修改商户信息后，发送全量信息到MQ  给风控
        MerchantPO merchantInfo = merchantMapper.getMerchantByMerchantCode(merchantCode);
        merchantProduct.sendMessage(merchantInfo);

        return true;
    }

    /**
     * 查询单个商户
     *
     * @Param: [merchantPO]
     * @return: void
     * @date: 2020/8/23 15:34
     */
    protected MerchantPO selectById(String id) {
        return merchantMapper.selectById(id);
    }

    protected MerchantVO convertToVO(MerchantPO merchantPO) {
        MerchantVO merchantVO = new MerchantVO();
        BeanUtils.copyProperties(merchantPO, merchantVO);
        merchantVO.setPswCode(merchantPO.getAdminPassword());
        return merchantVO;
    }

    protected MerchantPO convertToPO(MerchantVO merchantVO) {
        MerchantPO merchantPO = new MerchantPO();
        BeanUtils.copyProperties(merchantVO, merchantPO);
        return merchantPO;
    }

    protected int countByParam(Map<String, String> map) {
        if (map.containsKey("merchantName")) {
            return merchantMapper.countByParam(map.get("merchantName"), null, null);
        }
        if (map.containsKey("merchantCode")) {
            return merchantMapper.countByParam(null, map.get("merchantCode"), null);
        }
        if (map.containsKey("merchantAdmin")) {
            return merchantMapper.countByParam(null, null, map.get("merchantAdmin"));
        }
        return 0;
    }

    /**
     * 获取每天的开始时间 00:00:00:00
     *
     * @param date
     * @return
     */
    private Date getStartTime(Date date) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    protected String getMonthFirDay() {
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        return firstday;
    }

    private void kickOutAllUsers(String merchantCode) {

    }

    /**
     * 查询商户列表
     *
     * @Param: [merchantCode, status]
     * @return: java.lang.Object
     * @date: 2020/8/23 15:32
     */
    protected List<MerchantVO> queryMerchants(MerchantVO merchantVO) {
        // 关联t_merchant_config表返回商户配置信息
        if (StringUtils.isNotEmpty(merchantVO.getMerchantCode())) {
            MerchantPO merchantPO = merchantMapper.getMerchantByKanaCode(merchantVO.getMerchantCode());
            if (merchantPO != null) {
                merchantVO.setMerchantCode(merchantPO.getMerchantCode());
            }
        }
        MerchantPO merchantPO = new MerchantPO();
        BeanUtils.copyProperties(merchantVO, merchantPO);
        log.info("AbstractMerchantService queryMerchantList param=" + merchantPO);
        List<MerchantPO> list = merchantMapper.selectList(merchantPO);
        this.queryMerchantDomain(list);
        List<MerchantVO> voList = Lists.newArrayList();
        list.forEach(e -> {
            voList.add(convertToVO(e));
        });
        return voList;
    }
}
