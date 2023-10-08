package com.panda.sport.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.panda.sport.admin.feign.BssBackendClient;
import com.panda.sport.admin.feign.MerchantApiClient;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.admin.vo.UserOrderDayVo;
import com.panda.sport.backup.mapper.BackupOrderMixMapper;
import com.panda.sport.bss.mapper.TMerchantGroupMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.DatabaseCommonColumnToStr;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.CurrencyRatePO;
import com.panda.sport.merchant.common.po.bss.MerchantGroupPO;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.po.merchant.AdminUser;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;
import com.panda.sport.merchant.common.utils.*;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.merchant.AdminUserNameUpdateReqVO;
import com.panda.sport.merchant.common.vo.merchant.CurrencyRateVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantVO;
import com.panda.sport.merchant.manage.service.MerchantAdminUserService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.impl.AbstractMerchantService;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.merchant.mapper.AdminUserMapper;
import com.panda.sport.order.service.AbstractOrderService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service("outMerchantService")
@RefreshScope
public class OutMerchantServiceImpl extends AbstractMerchantService implements OutMerchantService {

    @Autowired
    private MerchantReportClient merchantReportClient;
    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MerchantAdminUserService merchantAdminUserService;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private BackupOrderMixMapper backupOrderMixMapper;

    @Autowired
    private BssBackendClient bssOrderClient;

    @Autowired
    private TMerchantGroupMapper tMerchantGroupMapper;

    @Value("${canalOrderUserId:10999}")
    private String canalOrderUserId;

    /**新建耳机商户特殊域名组赋值*/
    @Value("${merchant.group.special.group:null}")
    private String specialMerchantGroupList;

    @Autowired
    AdminUserMapper adminUserMapper;

    /**
     * 查询二级商户列表
     *
     * @param request
     * @param merchantName
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public Response selectList(HttpServletRequest request, String merchantName, Integer status, Integer merchantTag, Integer pageIndex, Integer pageSize) {
        try {
            JwtUser user = SecurityUtils.getUser();
            Integer agentLevel = user.getAgentLevel();
            if (agentLevel == null || agentLevel == 2) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            pageIndex = pageIndex == null ? 1 : pageIndex;
            pageSize = pageSize == null ? 20 : pageSize;
            String parentCode = user.getMerchantCode();
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setMerchantName(merchantName);
            merchantVO.setStatus(status);
            merchantVO.setAgentLevel((agentLevel == 1) ? 2 : 1);
            merchantVO.setParentCode(parentCode);
            merchantVO.setPageSize(pageSize);
            merchantVO.setMerchantTag(merchantTag);
            merchantVO.setPageIndex(pageIndex);
            return Response.returnSuccess(queryMerchantList(merchantVO));
        } catch (Exception e) {
            log.error("MerchantServiceImpl.selectList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    @Override
    public Response<Object> deleteSubAgent(String merchantCode, String parentId) {
        try {
            this.abstractDeleteSubAgent(merchantCode, parentId);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("MerchantController.deleteSubAgent,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response getMerchantLanguageList() {
        return Response.returnSuccess(merchantMapper.getMerchantLanguageList());
    }


    @Override
    public Response getMerchantListTree() {
        try {
            JwtUser user = SecurityUtils.getUser();
            String merchantId = user.getMerchantId();
            Integer agentLevel = user.getAgentLevel();
            if (agentLevel == 2 || agentLevel == 0) {
                return Response.returnFail("没有下级!");
            }
            return Response.returnSuccess(this.abstractGetMerchantTree(merchantId));
        } catch (Exception e) {
            log.error("获取商户树失败", e);
            return Response.returnFail("获取商户树失败!");
        }
    }

    @Override
    public Response queryMechantAgentList(String merchantCode) {
        return Response.returnSuccess(merchantMapper.getAgentList(merchantCode));
    }

    /**
     * 查询商户详情
     *
     * @param id
     * @return
     */
    @Override
    public Response getMerchantDetail(String id) {
        try {
            JwtUser user = SecurityUtils.getUser();
            if (StringUtils.isEmpty(id)) {
                id = user.getMerchantId() + "";
            }
            // 2020 825版本外部用户商户类型为二级商户账号不可查询技术参数
            MerchantVO merchantVO = convertToVO(merchantMapper.selectById(id));
            if (AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(user.getAgentLevel())) {
                merchantVO.setBalanceUrl(null);
                merchantVO.setCallbackUrl(null);
                merchantVO.setWhiteIp(null);
                merchantVO.setUrl(null);
                MerchantPO merchant = merchantMapper.selectById(merchantVO.getParentId());
                if (merchant!= null) {
                    merchant.setWhiteIp(null);
                    merchant.setBalanceUrl(null);
                    merchant.setUrl(null);
                    merchant.setCallbackUrl(null);
                }
                merchantVO.setParent(merchant);
            }
            return Response.returnSuccess(merchantVO);
        } catch (Exception e) {
            log.error("MerchantController.getMerchantDetail,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 创建商户
     *
     * @param merchantVO
     * @param language
     * @return
     */
    @Override
    public Response createMerchant(MerchantVO merchantVO, String language,String ip) {
        try {
            JwtUser user = SecurityUtils.getUser();
            Integer agentLevel = user.getAgentLevel();
            if (agentLevel != 1) {
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }
            merchantVO.setAgentLevel(2);

            // fix 42322 新商户域名组设置
            buildMerchantGroupId(merchantVO);

            ResponseEnum responseEnum = this.abstractCreateMerchant(merchantVO);
            //新增操作日志
            if (responseEnum == ResponseEnum.SUCCESS) {
                merchantLogService.saveLog(MerchantLogPageEnum.SECONDARY, MerchantLogTypeEnum.NEW_LEVEL_TOW_MERCHANT, null,
                        MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchantVO.getMerchantCode(), merchantVO.getMerchantName(), merchantVO.getId(), language, ip);
            }
            return responseEnum == ResponseEnum.SUCCESS ? Response.returnSuccess(ResponseEnum.SUCCESS) : Response.returnFail(responseEnum);
        } catch (Exception e) {
            log.error("MerchantController.createMerchant,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询商户KEY列表
     *
     * @param merchantName
     * @param parentId
     * @param pageSize
     * @param pageNum
     * @param language
     * @return
     */
    @Override
    public Response<PageInfo<TMerchantKey>> queryKeyList(String merchantName, String parentId, Integer pageSize, Integer pageNum, String language) {
        pageSize = pageSize == null ? 20 : pageSize;
        pageNum = pageNum == null ? 1 : pageNum;
        PageHelper.startPage(pageNum, pageSize, true);
        MerchantVO merchantVO = new MerchantVO();
        //上级商户ID
        merchantVO.setParentId(parentId);
        merchantVO.setMerchantName(merchantName);
        return Response.returnSuccess(new PageInfo<>(assemblykeyList(merchantMapper.queryKeyList(merchantVO), language, Constant.EXTERNAL_MERCHANT)));
    }

    /**
     * 修改商户
     *
     * @param merchantVO
     * @param language
     * @return
     */
    @Override
    public Response updateMerchant(MerchantVO merchantVO, String language, String ip) {
        try {
            MerchantPO merchantPO = merchantMapper.selectById(merchantVO.getId());
            JwtUser user = SecurityUtils.getUser();
            merchantVO.setUpdatedBy(user.getUsername());
            Response response = this.abstractUpdateMerchant(merchantVO);
            if (response.getStatus()) {
                merchantApiClient.kickoutMerchant(merchantPO.getMerchantCode());
                //记录日志
                if (StringUtils.isNotBlank(merchantVO.getAdminPassword())) {
                    //设置管理员日志
                    MerchantLogFiledVO filedVO = null;
                    if (StringUtils.isBlank(merchantVO.getMerchantAdmin())) {
                        filedVO = new MerchantLogFiledVO();
                        filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("admin_password"));
                        filedVO.getBeforeValues().add("*");
                        filedVO.getAfterValues().add("*");
                    }
                    //新增操作日志
                    merchantLogService.saveLog(MerchantLogPageEnum.SECONDARY, MerchantLogTypeEnum.SET_MANAGER, filedVO,
                            MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language,ip);
                } else {
                    //更新日志
                    MerchantPO merchantNew = convertToPO(merchantVO);
                    MerchantFieldUtil filedUtil = new MerchantFieldUtil<MerchantPO>();
                    MerchantLogFiledVO filedVO = filedUtil.compareObject(merchantPO, merchantNew);
                    if (filedVO != null && CollectionUtil.isNotEmpty(filedVO.getFieldName())) {
                        for (int i = 0; i < filedVO.getFieldName().size(); i++) {
                            if ("币种".equals(filedVO.getFieldName().get(i))) {
                                filedVO.getBeforeValues().set(i, MerchantFieldUtil.currencyMap.get(filedVO.getBeforeValues().get(i)));
                                filedVO.getAfterValues().set(i, MerchantFieldUtil.currencyMap.get(filedVO.getAfterValues().get(i)));
                            }
                            if ("缴纳周期".equals(filedVO.getFieldName().get(i))) {
                                filedVO.getBeforeValues().set(i, MerchantFieldUtil.paymentCycle.get(filedVO.getBeforeValues().get(i)));
                                filedVO.getAfterValues().set(i, MerchantFieldUtil.paymentCycle.get(filedVO.getAfterValues().get(i)));
                            }
                            if ("VIP费用缴纳周期".equals(filedVO.getFieldName().get(i))) {
                                filedVO.getBeforeValues().set(i, MerchantFieldUtil.vipPaymentCycle.get(filedVO.getBeforeValues().get(i)));
                                filedVO.getAfterValues().set(i, MerchantFieldUtil.vipPaymentCycle.get(filedVO.getAfterValues().get(i)));
                            }
                            if ("技术服务费缴纳周期".equals(filedVO.getFieldName().get(i))) {
                                filedVO.getBeforeValues().set(i, MerchantFieldUtil.techniquePaymentCycleMap.get(filedVO.getBeforeValues().get(i)));
                                filedVO.getAfterValues().set(i, MerchantFieldUtil.techniquePaymentCycleMap.get(filedVO.getAfterValues().get(i)));
                            }
                            if ("转账类型".equals(filedVO.getFieldName().get(i))) {
                                filedVO.getBeforeValues().set(i, MerchantFieldUtil.transferModeMap.get(filedVO.getBeforeValues().get(i)));
                                filedVO.getAfterValues().set(i, MerchantFieldUtil.transferModeMap.get(filedVO.getAfterValues().get(i)));
                            }
                            if ("计算模式".equals(filedVO.getFieldName().get(i))) {
                                filedVO.getBeforeValues().set(i, MerchantFieldUtil.computingStandardMap.get(filedVO.getBeforeValues().get(i)));
                                filedVO.getAfterValues().set(i, MerchantFieldUtil.computingStandardMap.get(filedVO.getAfterValues().get(i)));
                            }
                        }
                    }
                    //新增操作日志
                    merchantLogService.saveLog(MerchantLogPageEnum.BASIC, MerchantLogTypeEnum.EDIT_INFO, filedVO,
                            MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language ,ip);
                }
            }

            return response;
        } catch (Exception e) {
            log.error("MerchantController.updateMerchant,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询所有KEY列表
     *
     * @param language
     * @return
     */
    @Override
    public Response<TMerchantKey> getKey(String language) {
        try {
            JwtUser user = SecurityUtils.getUser();
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setAgentLevel(user.getAgentLevel());
            merchantVO.setMerchantCode(user.getMerchantCode());
            log.info("获取key:" + merchantVO);
            List<TMerchantKey> list = assemblykeyList(merchantMapper.queryKeyList(merchantVO), language, Constant.EXTERNAL_MERCHANT);
            return Response.returnSuccess(CollectionUtils.isEmpty(list) ? null : list.get(0));
        } catch (Exception e) {
            log.error("MerchantController.getKey,exception", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 创建商户管理员
     *
     * @param id
     * @param adminName
     * @param adminPassword
     * @param language
     * @return
     */
    @Override
    public Response<?> createAdmin(String id, String adminName, String adminPassword, String language ,String ip) {
        try {
            JwtUser user = SecurityUtils.getUser();
            if (user.getAgentLevel() != 1 && user.getAgentLevel() != 10) {
                log.error("用户所属商户代理级别异常!" + user);
                return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
            }
            MerchantPO merchantPO = this.selectById(id);
            if (merchantPO == null) {
                return Response.returnFail(ResponseEnum.DELETE_FAILS);
            }
            Map<String, String> map = Maps.newHashMap();
            map.put("merchantAdmin", adminName);
            int count = this.countByParam(map);
            if (count > 0) {
                return Response.returnFail(ResponseEnum.MARCHANT_ADMIN_NAME_IS_EXIST);
            }
            AdminUser adminUserResult = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", merchantPO.getMerchantAdmin()));
            if (adminUserResult != null){
                return Response.returnFail(ResponseEnum.MARCHANT_ADMIN_NAME_IS_EXIST);
            }
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setId(id);
            BeanUtils.copyProperties(merchantPO, merchantVO);
//            merchantVO.setAdminPassword(adminName + merchantPO.getMerchantCode());
            //2058需求，增加密码复杂程度，随机生成至少12位且必须含特殊字符+大小字母+数字
            String psw= CreatPswUtil.getPsw(12);//生成随机密码
            merchantVO.setAdminPassword(psw);//保存MD5加密码
            merchantVO.setPswCode(psw);
            merchantVO.setMerchantAdmin(adminName);
            merchantVO.setMerchantCode(merchantPO.getMerchantCode());
            return this.updateMerchant(merchantVO, language,ip);
        } catch (Exception e) {
            log.error("OutMerchantServiceImpl.createAdmin,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 修改商户管理员
     *
     * @param id
     * @param adminNewPassword
     * @param language
     * @return
     */
    @Override
    public Response<?> updateAdminPassword(String id, String adminNewPassword, String language ,String ip) {
        try {
            JwtUser user = SecurityUtils.getUser();
            if (user.getAgentLevel() != 1 && user.getAgentLevel() != 10) {
                log.error("用户所属商户代理级别异常!" + user);
                return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
            }
            MerchantVO merchantVO = new MerchantVO();
            merchantVO.setId(id);
            //设置新密码
            MerchantPO merchantPO = merchantMapper.selectById(id);
//            merchantVO.setAdminPassword(merchantPO.getMerchantAdmin() + merchantPO.getMerchantCode());
            //2058需求，增加密码复杂程度，随机生成至少12位且必须含特殊字符+大小字母+数字
            String psw= CreatPswUtil.getPsw(12);//生成随机密码
            merchantVO.setAdminPassword(psw);//保存MD5加密码
            merchantVO.setPswCode(psw);
            merchantVO.setMerchantCode(merchantPO.getMerchantCode());
            return this.updateMerchant(merchantVO, language ,ip);
        } catch (Exception e) {
            log.error("MerchantController.updateAdminPassword,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 生成新的KEY
     *
     * @return
     */
    @Override
    public Response<Object> generateKey() {
        try {
            return Response.returnSuccess(CreateSecretKey.keyCreate());
        } catch (Exception e) {
            log.error("MerchantController.generateKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 修改KEY
     *
     * @param key
     * @param startTime
     * @param endTime
     * @param language
     * @return
     */
    @Override
    public Response<Object> updateKey(String key, String startTime, String endTime, String language ,String ip, String keyLabel) {
        try {
            JwtUser user = SecurityUtils.getUser();
            MerchantPO merchantPO = merchantMapper.getMerchantInfo(user.getMerchantCode());
            boolean result = this.abstractUpdateKey(user.getMerchantCode(), key, keyLabel, startTime, endTime, user.getUsername());
            if (result) {
                //记录日志
                MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
                filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("key"));
                filedVO.getBeforeValues().add(merchantPO.getMerchantKey());
                filedVO.getAfterValues().add(key);
                //新增操作日志
                merchantLogService.saveLog(MerchantLogPageEnum.MY_KEY, MerchantLogTypeEnum.EDIT_MERCHANT_INFO_KEY, filedVO,
                        MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), language ,ip);
            }
            return result ?
                    Response.returnSuccess(true) : Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        } catch (Exception e) {
            log.error("MerchantController.updateKey,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 修改商户状态
     *
     * @param merchantCode
     * @param status
     * @param language
     * @return
     */
    @Override
    public Response<Object> updateMerchantStatus(String merchantCode, String status, String language ,String ip) {
        try {
            MerchantPO merchant = abstractUpdateMerchantStatus(merchantCode, status,null);
            //如果是禁用商户则同步踢出商户和商户下对应的用户
            merchantApiClient.kickoutMerchant(merchantCode);
            merchantApiClient.kickoutMerchantUser(merchantCode);
            if (merchant != null) {
                //记录日志
                JwtUser user = SecurityUtils.getUser();
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
                vo.getBeforeValues().add(merchant.getStatus() == 1 ? "启用" : "禁用");
                vo.getAfterValues().add("1".equals(status) ? "启用" : "禁用");
                merchantLogService.saveLog(MerchantLogPageEnum.SECONDARY, MerchantLogTypeEnum.EDIT_INFO_STATUS, vo,
                        MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language , ip);
            }
            return Response.returnSuccess(true);
        } catch (Exception e) {
            log.error("MerchantController.updateMerchantStatus,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> updateMerchantBackendStatus(String merchantCode, String status, String language, String ip) {
        MerchantPO merchant = abstractUpdateMerchantBackendStatus(merchantCode, status);
        //如果是禁用商户则同步踢出商户和商户下对应的用户
//        merchantApiClient.kickoutMerchant(merchantCode);
        if (merchant != null) {
            //记录日志
            JwtUser user = SecurityUtils.getUser();
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("backendSwitch"));
            vo.getBeforeValues().add(merchant.getBackendSwitch() != null && merchant.getBackendSwitch() == 1 ? "启用" : "禁用");
            vo.getAfterValues().add("1".equals(status) ? "启用" : "禁用");
            merchantLogService.saveLog(MerchantLogPageEnum.SECONDARY, MerchantLogTypeEnum.EDIT_INFO_STATUS, vo,
                    MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchant.getMerchantCode(), merchant.getMerchantName(), merchant.getId(), language , ip);
        }
        return Response.returnSuccess(true);
    }

    /**
     * 商户注单统计
     */
    @Override
    public Response<?> listGroupByMerchant(MerchantOrderVO merchantOrderRequestVO) {
        Response<Map<String, Object>> response = merchantReportClient.listGroupByMerchant(merchantOrderRequestVO);
        Map<String, Object> map = response.getData();
        if (null != map) {
            List<?> resultList = (List<?>) map.get("list");
            if (CollectionUtils.isNotEmpty(resultList)) {
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<MerchantOrderDayPO>>() {
                });
                List<MerchantOrderDayPO> result = resetRepeatUser(filterList, merchantOrderRequestVO);
                reorderSubMerchant(result);
                for (MerchantOrderDayPO po : result) {
                    po.setMerchantName(localCacheService.getMerchantCode(po.getMerchantCode()));
                }
                map.put("list", result);
            }
        }
        return response;
    }

    /**
     * 将商户二级放到相应的渠道商户下面
     */
    private void reorderSubMerchant(List<MerchantOrderDayPO> result) {
        Map<String, Integer> orderMap = AbstractOrderService.setParentTimeForOrder(result);
        List<String> allChildMerchantCode = AbstractOrderService.getAllChildMerchantCode(result);
        if (CollectionUtils.isNotEmpty(allChildMerchantCode)) {
            List<Map<String, String>> codeMapList = merchantMapper.listParentCodeByMerchantCode(allChildMerchantCode);
            if (CollectionUtils.isNotEmpty(codeMapList)) {
                Map<String, String> codeMap = codeMapList.stream().collect(Collectors.toMap(tempMap -> tempMap.get("child_code"), tempMap -> tempMap.get("parent_code"), (a, b) -> b));
                for (MerchantOrderDayPO temp : result) {
                    if (AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(temp.getAgentLevel())) {
                        String parentMerchantCode = codeMap.get(temp.getMerchantCode());
                        Integer order = orderMap.get(parentMerchantCode);
                        if (null != order) {
                            temp.setTime(order + 1);
                        }
                    }
                }
                Collections.sort(result, Comparator.comparingInt(MerchantOrderDayPO::getTime));
            }
        }
    }

    /**
     * 过滤重复用户统计
     *
     * @param filterList 需要过滤的数据
     * @param vo         统计时的参数
     * @return 过滤后的结果
     */
    private List<MerchantOrderDayPO> resetRepeatUser(List<MerchantOrderDayPO> filterList, MerchantOrderVO vo) {
        Set<String> parentCodes = AbstractOrderService.getAllParentMerchantCodeSet(filterList);
        if (CollectionUtils.isNotEmpty(parentCodes)) {
            List<MerchantPO> merchantList = merchantMapper.getMerchantInMerchantCode(parentCodes);
            //根据代理商或渠道商获得所有的下级商户code,用于查询
            List<String> allChildMerchantCode = new ArrayList<>();
            Map<String, List<String>> parentWithChildMerchantCodeMap = new HashMap<>();
            for (MerchantPO temp : merchantList) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(temp.getId(), temp.getAgentLevel());
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    allChildMerchantCode.addAll(merchantCodeList);
                    parentWithChildMerchantCodeMap.put(temp.getMerchantCode(), merchantCodeList);
                }
            }

            vo.setMerchantCodeList(allChildMerchantCode);
            Response<Map<String, Object>> mapResponse = merchantReportClient.listGroupByMerchantRepeatUser(vo);
            if (null != mapResponse.getData()) {
                final Map<String, Object> tempData = mapResponse.getData();
                AbstractOrderService.resetSettleUsers(filterList, tempData, parentWithChildMerchantCodeMap);
            }
        }
        return filterList;
    }

    /**
     * 商户注单统计
     *
     * @param merchantOrderRequestVO
     * @return
     */
    @Override
    public Response<?> queryMerchantReportList(MerchantOrderVO merchantOrderRequestVO) {
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        String merchantId = user.getMerchantId();
        Integer agentLevel = user.getAgentLevel();
        if (agentLevel == 2 || agentLevel == 0) {
            merchantOrderRequestVO.setMerchantCode(merchantCode);
        } else {
            //如果参数里已经有商户查询条件时，则不用取缓存里的下级商户树了
            if (CollectionUtils.isEmpty(merchantOrderRequestVO.getMerchantCodeList())) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                List<String> resultList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    resultList.addAll(merchantCodeList);
                    resultList.add(merchantCode);
                    merchantOrderRequestVO.setMerchantCodeList(resultList);
                }
            }
        }
        try {
            if (merchantOrderRequestVO.getCurrency() != null &&
                    !merchantOrderRequestVO.getCurrency().equals(CurrencyTypeEnum.RMB.getId())) {
                return new Response();
            }

            if (merchantOrderRequestVO.getMerchantCode() == null &&
                    merchantOrderRequestVO.getMerchantCodeList() == null) {
                return new Response();
            }
            Response response = merchantReportClient.queryMerchantReportList(merchantOrderRequestVO);
            Map<String, Object> result = (Map<String, Object>) response.getData();
            if (result == null) {
                return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
            }
            Map<String, Object> map = Maps.newHashMap();
            List<?> resultList = (List<?>) result.get("list");
            if (CollectionUtils.isNotEmpty(resultList)) {
                BigDecimal betAmountSettled = null, settleBetAmountSum = null, profitSum = null, settleProfitSum = null, profitRate = null, settleProfitRate = null;
                int validTicketsSum = 0, validBetUsersSum = 0, settleUsersSum = 0, settleOrderNumSum = 0, addUserSum = 0;
                for (Object object : resultList) {
                    Map<String, Object> innerMap = (Map<String, Object>) object;
                    innerMap.put("currency", CurrencyTypeEnum.RMB.getId());
                }
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantOrderDayPO> list = mapper.convertValue(resultList, new TypeReference<List<MerchantOrderDayPO>>() {
                });
                Integer paramAgentLevel = merchantOrderRequestVO.getAgentLevel();
                if (paramAgentLevel == null && agentLevel != 0 && agentLevel != 2) {
                    if (agentLevel == 1) {
                        betAmountSettled = list.stream().filter(e -> e.getAgentLevel() == 2).map(MerchantOrderDayPO::getValidBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        settleBetAmountSum = list.stream().filter(e -> e.getAgentLevel() == 2).map(MerchantOrderDayPO::getSettleBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        profitSum = list.stream().filter(e -> e.getAgentLevel() == 2).map(MerchantOrderDayPO::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
                        settleProfitSum = list.stream().filter(e -> e.getAgentLevel() == 2).map(MerchantOrderDayPO::getSettleProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
                        profitRate = betAmountSettled.doubleValue() > 0 ? profitSum.divide(betAmountSettled, 4, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)) : betAmountSettled;
                        settleProfitRate = settleBetAmountSum.doubleValue() > 0 ? settleProfitSum.divide(settleBetAmountSum, 4, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)) : settleBetAmountSum;
                        validTicketsSum = list.stream().filter(e -> e.getTicketSettled() != null && e.getAgentLevel() == 2).mapToInt(MerchantOrderDayPO::getTicketSettled).sum();
                        validBetUsersSum = list.stream().filter(e -> e.getValidBetUsers() != null && e.getAgentLevel() == 2).mapToInt(MerchantOrderDayPO::getValidBetUsers).sum();
                        settleUsersSum = list.stream().filter(e -> e.getSettleUsers() != null && e.getAgentLevel() == 2).mapToInt(MerchantOrderDayPO::getSettleUsers).sum();
                        settleOrderNumSum = list.stream().filter(e -> e.getSettleOrderNum() != null && e.getAgentLevel() == 2).mapToInt(MerchantOrderDayPO::getSettleOrderNum).sum();
                        addUserSum = list.stream().filter(e -> e.getAddUser() != null && e.getAgentLevel() == 2).mapToInt(MerchantOrderDayPO::getAddUser).sum();
                    } else if (agentLevel == 10) {
                        betAmountSettled = list.stream().filter(e -> e.getAgentLevel() == 1).map(MerchantOrderDayPO::getValidBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        settleBetAmountSum = list.stream().filter(e -> e.getAgentLevel() == 1).map(MerchantOrderDayPO::getSettleBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        profitSum = list.stream().filter(e -> e.getAgentLevel() == 1).map(MerchantOrderDayPO::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
                        settleProfitSum = list.stream().filter(e -> e.getAgentLevel() == 1).map(MerchantOrderDayPO::getSettleProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
                        profitRate = betAmountSettled.doubleValue() > 0 ? profitSum.divide(betAmountSettled, 4, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)) : betAmountSettled;
                        settleProfitRate = settleBetAmountSum.doubleValue() > 0 ? settleProfitSum.divide(settleBetAmountSum, 4, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)) : settleBetAmountSum;
                        validTicketsSum = list.stream().filter(e -> e.getTicketSettled() != null && e.getAgentLevel() == 1).mapToInt(MerchantOrderDayPO::getTicketSettled).sum();
                        validBetUsersSum = list.stream().filter(e -> e.getValidBetUsers() != null && e.getAgentLevel() == 1).mapToInt(MerchantOrderDayPO::getValidBetUsers).sum();
                        settleUsersSum = list.stream().filter(e -> e.getSettleUsers() != null && e.getAgentLevel() == 1).mapToInt(MerchantOrderDayPO::getSettleUsers).sum();
                        settleOrderNumSum = list.stream().filter(e -> e.getSettleOrderNum() != null && e.getAgentLevel() == 1).mapToInt(MerchantOrderDayPO::getSettleOrderNum).sum();
                        addUserSum = list.stream().filter(e -> e.getAddUser() != null && e.getAgentLevel() == 1).mapToInt(MerchantOrderDayPO::getAddUser).sum();
                    }
                } else {
                    betAmountSettled = list.stream().map(MerchantOrderDayPO::getValidBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    settleBetAmountSum = list.stream().map(MerchantOrderDayPO::getSettleBetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    profitSum = list.stream().map(MerchantOrderDayPO::getProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
                    settleProfitSum = list.stream().map(MerchantOrderDayPO::getSettleProfit).reduce(BigDecimal.ZERO, BigDecimal::add);
                    profitRate = betAmountSettled.doubleValue() > 0 ? profitSum.divide(betAmountSettled, 4, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)) : betAmountSettled;
                    settleProfitRate = settleBetAmountSum.doubleValue() > 0 ? settleProfitSum.divide(settleBetAmountSum, 4, BigDecimal.ROUND_FLOOR).multiply(new BigDecimal(100)) : settleBetAmountSum;
                    validTicketsSum = list.stream().mapToInt(MerchantOrderDayPO::getTicketSettled).sum();
                    validBetUsersSum = list.stream().mapToInt(MerchantOrderDayPO::getValidBetUsers).sum();
                    settleUsersSum = list.stream().mapToInt(MerchantOrderDayPO::getSettleUsers).sum();
                    settleOrderNumSum = list.stream().mapToInt(MerchantOrderDayPO::getSettleOrderNum).sum();
                    addUserSum = list.stream().mapToInt(MerchantOrderDayPO::getAddUser).sum();
                }
                map.put("validBetAmountSum", betAmountSettled);
                map.put("profitSum", profitSum);
                map.put("profitRate", profitRate);
                map.put("validTicketsSum", validTicketsSum);
                map.put("validBetUsersSum", validBetUsersSum);
                map.put("addUserSum", addUserSum);
                map.put("settleBetAmountSum", settleBetAmountSum);
                map.put("settleProfitSum", settleProfitSum);
                map.put("settleProfitRate", settleProfitRate);
                map.put("settleUsersSum", settleUsersSum);
                map.put("settleOrderNumSum", settleOrderNumSum);
                result.put("aggregate", map);
            }
            return response;
        } catch (Exception e) {
            log.error("MerchantReportController.queryMerchantReportList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 商户报表导出
     *
     * @param response
     * @param merchantOrderVO
     * @param token
     */
    @Override
    public Map exportMerchantReport(HttpServletResponse response, MerchantOrderVO merchantOrderVO, String token, String language) {
        assemblyMerchantQueryParam(merchantOrderVO, token,1);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String merchantCode;
            String username;
            if (StringUtils.isNotEmpty(token)) {
                Map<String, String> claims = JwtTokenUtil.verifyToken(token);
                merchantCode = claims.get("merchantCode");
                username = claims.get("userName");
            } else {
                JwtUser user = SecurityUtils.getUser();
                merchantCode = user.getMerchantCode();
                username = user.getUsername();
            }
            merchantOrderVO.setLanguage(language);
            merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "商户注单统计_" : "MerchantReport_", merchantCode, username, JSON.toJSONString(merchantOrderVO),
                    language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-商户注单统计" : "Report Center-Merchant Report", "groupByMerchantReportExportServiceImpl");
            resultMap.put("code", "0000");
            resultMap.put("msg", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
                    : "The exporting task has been created,please click at the Download Task menu to check!");
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    /**
     * 本月商户排行TOP10
     *
     * @return
     */
    @Override
    public Response<?> queryMerchantTop10(String orderBy) {
        try {
            MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
            assemblyMerchantQueryParam(merchantOrderVO, "",0);
            merchantOrderVO.setDate(DateFormatUtils.format(new Date(), "yyyyMM"));
            JwtUser user = SecurityUtils.getUser();
            Integer agentLevel = user.getAgentLevel();
            if (agentLevel == 0 || agentLevel == 2) return Response.returnFail("权限不足!");
            if (agentLevel == 1) merchantOrderVO.setAgentLevel(2);
            if (agentLevel == 10) merchantOrderVO.setAgentLevel(1);
            merchantOrderVO.setOrderBy(orderBy);
            return merchantReportClient.queryMerchantTop10(merchantOrderVO);
        } catch (Exception e) {
            log.error("HomeController.queryMerchantTop10,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 本月商户投注量排行TOP10
     *
     * @return
     */
    @Override
    public Response<?> amountGrowthRateTop10() {
        try {
            MerchantOrderVO vo = new MerchantOrderVO();
            assemblyMerchantQueryParam(vo, "",0);
            JwtUser user = SecurityUtils.getUser();
            String merchantCode = user.getMerchantCode();
            String merchantId = user.getMerchantId();
            Integer agentLevel = user.getAgentLevel();
            vo.setAgentLevel(agentLevel);
            if (agentLevel == 2 || agentLevel == 0) {
                vo.setMerchantCode(merchantCode);
            } else {
                List<String> merchantCodeList = merchantMapper.queryChildList(merchantId);
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    vo.setMerchantCodeList(merchantCodeList);
                }
            }
            vo.setDateType(Constant.MONTH);
            vo.setPageSize(100);
            Date now = new Date();
            String currentMonth = DateFormatUtils.format(DateUtils.addMonths(now, -1), "yyyyMM");
            vo.setOrderBy("betAmount");
            vo.setSort("desc");
            vo.setTime(Long.parseLong(currentMonth));
            vo.setDate(currentMonth);
            vo.setTimeZone(Constant.EZ);
            log.info("amountGrowthRateTop10:" + vo);
            if (agentLevel == 1 || agentLevel == 10) {
                vo.setAgentLevel((agentLevel == 1) ? 2 : 1);
                List<?> currentResultList = merchantReportClient.merchantDataQuery(vo);
                if (CollectionUtils.isEmpty(currentResultList)) {
                    return Response.returnSuccess();
                }
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantOrderDayPO> currentList = mapper.convertValue(currentResultList, new TypeReference<List<MerchantOrderDayPO>>() {
                });
                String lastMonth = DateFormatUtils.format(DateUtils.addMonths(now, -2), "yyyyMM");
                vo.setTime(Long.parseLong(lastMonth));
                vo.setDate(lastMonth);
                List<?> lastResultList = merchantReportClient.merchantDataQuery(vo);
                if (CollectionUtils.isEmpty(lastResultList)) {
                    return Response.returnSuccess();
                }
                List<MerchantOrderDayPO> lastList = mapper.convertValue(lastResultList, new TypeReference<List<MerchantOrderDayPO>>() {
                });
                Map<String, MerchantOrderDayPO> currentMap = currentList.stream().collect(Collectors.toMap(MerchantOrderDayPO::getMerchantCode, a -> a, (k1, k2) -> k1));
                Map<String, MerchantOrderDayPO> lastMap = lastList.stream().collect(Collectors.toMap(MerchantOrderDayPO::getMerchantCode, a -> a, (k1, k2) -> k1));

                currentList.sort((a, b) -> b.getValidBetAmount().compareTo(a.getValidBetAmount()));
                lastList.sort((a, b) -> b.getValidBetAmount().compareTo(a.getValidBetAmount()));

                Map<String, Integer> currentSortMap = listToMap(currentList);
                Map<String, Integer> lastSortMap = listToMap(lastList);
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Map.Entry<String, MerchantOrderDayPO> current : currentMap.entrySet()) {
                    String tempCode = current.getKey();
                    MerchantOrderDayPO lastPo = lastMap.get(tempCode);
                    if (lastPo == null || lastPo.getValidBetAmount().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    MerchantOrderDayPO currentPo = currentMap.get(tempCode);
                    BigDecimal latAmount = lastPo.getValidBetAmount();
                    BigDecimal currentAmount = currentPo.getValidBetAmount();
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("id", tempCode);
                    temp.put("name", currentPo.getMerchantName());
                    temp.put("currentRank", currentSortMap.get(tempCode));
                    temp.put("orderValidBetMoney",currentPo.getOrderValidBetMoney());
                    temp.put("settleValidBetMoney",currentPo.getSettleValidBetMoney());
                    temp.put("lastRank", lastSortMap.get(tempCode));
                    temp.put("betAmount", currentAmount);
                    temp.put("increasedAmount", currentAmount.subtract(latAmount));
                    temp.put("growthRate", ((currentAmount.subtract(latAmount)).divide(latAmount, 4, BigDecimal.ROUND_FLOOR)).multiply(new BigDecimal(100)));
                    resultList.add(temp);
                }
                resultList.sort((o1, o2) -> {
                    BigDecimal key1 = (BigDecimal) o1.get("increasedAmount");
                    BigDecimal key2 = (BigDecimal) o2.get("increasedAmount");
                    return key2.compareTo(key1);
                });
                return Response.returnSuccess(resultList.size() > 10 ? resultList.subList(0, 10) : resultList);
            } else {
                UserOrderVO userVo = new UserOrderVO();
                BeanUtils.copyProperties(vo, userVo);
                List<?> currentResultList = merchantReportClient.queryUserReport(userVo);
                if (CollectionUtils.isEmpty(currentResultList)) {
                    return Response.returnSuccess();
                }
                ObjectMapper mapper = new ObjectMapper();
                List<UserOrderDayVo> currentList = mapper.convertValue(currentResultList, new TypeReference<List<UserOrderDayVo>>() {
                });
                String lastMonth = DateFormatUtils.format(DateUtils.addMonths(now, -2), "yyyyMM");
                userVo.setTime(Long.parseLong(lastMonth));
                userVo.setDate(lastMonth);
                List<?> lastResultList = merchantReportClient.queryUserReport(userVo);
                if (CollectionUtils.isEmpty(lastResultList)) {
                    return Response.returnSuccess();
                }
                List<UserOrderDayVo> lastList = mapper.convertValue(lastResultList, new TypeReference<List<UserOrderDayVo>>() {
                });
                Map<String, UserOrderDayVo> currentMap = currentList.stream().collect(Collectors.toMap(UserOrderDayVo::getUserId, a -> a, (k1, k2) -> k1));
                Map<String, UserOrderDayVo> lastMap = lastList.stream().collect(Collectors.toMap(UserOrderDayVo::getUserId, a -> a, (k1, k2) -> k1));
                currentList.sort((a, b) -> b.getValidBetAmount().compareTo(a.getValidBetAmount()));
                lastList.sort((a, b) -> b.getValidBetAmount().compareTo(a.getValidBetAmount()));
                Map<String, Integer> currentSortMap = listToUserMap(currentList);
                Map<String, Integer> lastSortMap = listToUserMap(lastList);
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Map.Entry<String, UserOrderDayVo> current : currentMap.entrySet()) {
                    String tempCode = current.getKey();
                    UserOrderDayVo lastPo = lastMap.get(tempCode);
                    if (lastPo == null || lastPo.getValidBetAmount().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    UserOrderDayVo currentPo = currentMap.get(tempCode);
                    BigDecimal latAmount = lastPo.getValidBetAmount();
                    BigDecimal currentAmount = currentPo.getValidBetAmount();
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("id", tempCode);
                    temp.put("name", currentPo.getUserName());
                    temp.put("currentRank", currentSortMap.get(tempCode));
                    temp.put("orderValidBetMoney",currentPo.getOrderValidBetMoney());
                    temp.put("settleValidBetMoney",currentPo.getSettleValidBetMoney());
                    temp.put("lastRank", lastSortMap.get(tempCode));
                    temp.put("betAmount", currentAmount);
                    temp.put("increasedAmount", currentAmount.subtract(latAmount));
                    temp.put("growthRate", ((currentAmount.subtract(latAmount)).divide(latAmount, 4, BigDecimal.ROUND_FLOOR)).multiply(new BigDecimal(100)));
                    resultList.add(temp);
                }
                resultList.sort((o1, o2) -> {
                    BigDecimal key1 = (BigDecimal) o1.get("increasedAmount");
                    BigDecimal key2 = (BigDecimal) o2.get("increasedAmount");
                    return key2.compareTo(key1);
                });
                return Response.returnSuccess(resultList.size() > 10 ? resultList.subList(0, 10) : resultList);
            }

        } catch (Exception e) {
            log.error("HomeController.amountGrowthRateTop10,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 商户联想查询列表
     *
     * @return
     */
    @Override
    public Response queryMerchantListByParam() {
        Map<String, Object> param = Maps.newHashMap();
        JwtUser user = SecurityUtils.getUser();
        Integer agentLevel = user.getAgentLevel();
        if (agentLevel != 1) {
            return Response.returnFail("无权查看");
        }
        String merchantId = user.getMerchantId();
        String merchantCode = user.getMerchantCode();
        List<String> merchantCodeList = merchantMapper.queryChildList(merchantId);
        if (CollectionUtils.isEmpty(merchantCodeList)) {
            return Response.returnSuccess(merchantCode);
        }
        //merchantCodeList.add(merchantCode);
        param.put("merchantCodeList", merchantCodeList);
        return Response.returnSuccess(merchantMapper.queryMerchantListByParam(param));
    }

    @Override
    public List<String> queryMerchantList() {
        JwtUser user = SecurityUtils.getUser();
        String merchantId = user.getMerchantId();
        return merchantMapper.queryChildList(merchantId);
    }

    /**
     * 二级商户本月投注统计
     *
     * @return
     */
    @Override
    public Response<?> queryCurrentMonthMerchantList() {
        try {
            JwtUser user = SecurityUtils.getUser();
            String merchantId = user.getMerchantId();
            Integer agentLevel = user.getAgentLevel();
            //如果是二级商户没有权限查看
            if (agentLevel == 2 || agentLevel == 0) {
                return Response.returnFail("无权查看");
            }
            MerchantVO requestVO = new MerchantVO();
            requestVO.setAgentLevel(agentLevel == 1 ? 2 : 1);
            requestVO.setCreateTime(getMonthFirDay());
            requestVO.setParentId(merchantId);
            return Response.returnSuccess(merchantMapper.selectMonthList(requestVO));
        } catch (Exception e) {
            log.error("MerchantController.queryCurrentMonthMerchantList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    protected void insertMerchantNews(MerchantNews merchantNews) {
        log.info("hi,不能通知下级商户!");

    }

    /**
     * 创建商户管理员
     *
     * @param merchantPO
     */
    @Override
    protected void createAdminUser(MerchantPO merchantPO) {
        merchantAdminUserService.createAdminUser(merchantPO);
    }

    private String getAgent(Integer agentLevel, String newMerchant, String oldMerchant) {
        if (newMerchant.equals(oldMerchant)) {
            if (agentLevel == 0) {
                return "直营";
            } else if (agentLevel == 1) {
                return "渠道";
            } else if (agentLevel == 10) {
                return "代理商";
            }
        }
        return "二级代理";
    }

    private void assemblyMerchantQueryParam(MerchantOrderVO vo, String token,Integer type) {
        String merchantCode, merchantId;
        Integer agentLevel;
        if (StringUtils.isNotEmpty(token)) {
            Map<String, String> claims = JwtTokenUtil.verifyToken(token);
            merchantCode = claims.get("merchantCode");
            merchantId = claims.get("merchantId");
            agentLevel = Integer.valueOf(claims.get("agentLevel"));
        } else {
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            merchantId = user.getMerchantId();
            agentLevel = user.getAgentLevel();
        }
        if(1==type){
            vo.setAgentLevel(agentLevel);
        }
        if (agentLevel == 1 | agentLevel == 10) {
            List<String> resultList = new ArrayList<>();
            List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                resultList.addAll(merchantCodeList);
                resultList.add(merchantCode);
                vo.setMerchantCodeList(resultList);
            } else {
                vo.setMerchantCode(merchantCode);
            }
        } else {
            vo.setMerchantCode(merchantCode);
        }
    }

    @Override
    public Response merchantChannelOrderTop10() {
        try {
            MerchantOrderVO merchantOrderVO = new MerchantOrderVO();
            String merchantCode, merchantId;
            Integer agentLevel;
            JwtUser user = SecurityUtils.getUser();
            merchantCode = user.getMerchantCode();
            merchantId = user.getMerchantId();
            agentLevel = user.getAgentLevel();
            if (agentLevel != 1) {
                UserOrderVO userOrderVO = new UserOrderVO();
                userOrderVO.setMerchantCode(merchantCode);
                return merchantReportClient.userOrderTop10(userOrderVO);
            } else {
                List<String> merchantCodeList = merchantMapper.queryChildList(merchantId);
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    merchantOrderVO.setMerchantCodeList(merchantCodeList);
                } else {
                    merchantOrderVO.setMerchantCode(merchantCode);
                }
                return merchantReportClient.merchantChannelOrderTop10(merchantOrderVO);
            }
        } catch (Exception e) {
            log.error("HomeReportController.merchantChannelOrderTop10,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    private Map<String, MerchantOrderDayPO> listToMerchantMap(List<MerchantOrderDayPO> list) {
        Map<String, MerchantOrderDayPO> result = new HashMap<>();
        for (MerchantOrderDayPO temp : list) {
            result.put(temp.getMerchantCode(), temp);
        }
        return result;
    }

    private Map<String, Integer> listToMap(List<MerchantOrderDayPO> list) {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            result.put(list.get(i).getMerchantCode(), i + 1);
        }
        return result;
    }

    private Map<String, Integer> listToUserMap(List<UserOrderDayVo> list) {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            result.put(list.get(i).getUserId(), i + 1);
        }
        return result;
    }


    @Override
    public List<CurrencyRateVO> currencyRateList() {
        List<CurrencyRatePO> currencyRatePOList = backupOrderMixMapper.queryCurrencyRateList();
        List<CurrencyRateVO> currencyRateVOList = new ArrayList<>();
        for (CurrencyRatePO currencyRatePO : currencyRatePOList) {

            CurrencyRateVO currencyRateVO = new CurrencyRateVO();
            currencyRateVO.setCountryCn(currencyRatePO.getCountryCn());
            currencyRateVO.setCountryZh(currencyRatePO.getCountryZh());
            currencyRateVO.setCurrencyCode(Integer.valueOf(currencyRatePO.getCurrencyCode()));
            currencyRateVOList.add(currencyRateVO);
        }
        return currencyRateVOList;
    }


    @Override
    public Response orderOperation(MerchantOrderOperationVO merchantOrderOperationVO ,String ip) {
        JwtUser user = SecurityUtils.getUser();
        MerchantPO merchantPO = merchantMapper.getMerchant(user.getMerchantCode());
        if (merchantPO.getMerchantTag() != 1) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        if (merchantOrderOperationVO.getBetNos() == null) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String betNOs = String.join(",", merchantOrderOperationVO.getBetNos());
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        vo.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("cancelBet"));
        OrderOperationVO orderOperation = new OrderOperationVO();
        orderOperation.setRemark(merchantOrderOperationVO.getRemark());
        orderOperation.setOperateType(6);
        orderOperation.setBetNos(betNOs);
        orderOperation.setOperateUser(user.getUsername());
        orderOperation.setUserId(canalOrderUserId);
        orderOperation.setOperateUser(user.getUsername());
        vo.getBeforeValues().add(orderOperation.getBetNos());
        vo.getAfterValues().add(merchantOrderOperationVO.getRemark());
        merchantLogService.saveLog(MerchantLogPageEnum.DATA_CENTER_ORDER, MerchantLogTypeEnum.DELETE_BET_ORDER, vo,
                MerchantLogConstants.MERCHANT_OUT, user.getId(), user.getUsername(), merchantPO.getMerchantCode(), merchantPO.getMerchantName(), merchantPO.getId(), merchantOrderOperationVO.getLanguage() ,ip);
        bssOrderClient.orderOperation(orderOperation);
        return Response.returnSuccess(bssOrderClient.orderOperation(orderOperation));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateAdminUserName(AdminUserNameUpdateReqVO adminUserNameUpdateReqVO, String language,String ipAddress) {
        try {
            MerchantPO merchantPO = merchantMapper.selectById(adminUserNameUpdateReqVO.getId());
            if (Objects.isNull(merchantPO)){
                return Response.returnFail(ResponseEnum.USER_MISS);
            }

            merchantPO.setMerchantAdmin(adminUserNameUpdateReqVO.getUserName());
            merchantMapper.updateMerchantAdminById(merchantPO);

            QueryWrapper<AdminUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(DatabaseCommonColumnToStr.MERCHANT_ID,merchantPO.getId());
            queryWrapper.eq(DatabaseCommonColumnToStr.IS_ADMIN, CommonDefaultValue.DifferentiateStatus.YES);
            List<AdminUser> adminUserList = adminUserMapper.selectList(queryWrapper);
            if (adminUserList.size() == CommonDefaultValue.ONE){
                AdminUser adminUser = adminUserList.get(0);
                AdminUser adminUserAnother = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", adminUserNameUpdateReqVO.getUserName()));
                if (adminUserAnother != null && !Objects.equals(adminUser.getId(),adminUserAnother.getId())) {
                    return Response.returnFail(ResponseEnum.MARCHANT_ADMIN_NAME_IS_EXIST);
                }
                AdminUser adminUserUpd = new AdminUser();
                adminUserUpd.setUsername(adminUserNameUpdateReqVO.getUserName());
                adminUserUpd.setId(adminUser.getId());
                adminUserMapper.updateById(adminUserUpd);
            }


            MerchantLogPO merchantLogPO = new MerchantLogPO();
            merchantLogPO.setMerchantCode(merchantPO.getMerchantCode());
            merchantLogPO.setMerchantName(merchantPO.getMerchantName());
            merchantLogPO.setOperatType(MerchantLogTypeEnum.SET_MANAGER.getCode());
            merchantLogPO.setOperatField(JsonUtils.listToJson(Collections.singletonList(DatabaseCommonColumnToStr.USER_NAME)));
            merchantLogPO.setBeforeValues(JsonUtils.listToJson(Collections.singletonList(merchantPO.getMerchantAdmin())));
            merchantLogPO.setAfterValues(JsonUtils.listToJson(Collections.singletonList(adminUserNameUpdateReqVO.getUserName())));
            merchantLogPO.setPageCode(MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getCode());
            merchantLogPO.setPageName(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED,language) ? MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getRemark() : MerchantLogPageEnum.MERCHANT_INFO_MANAGER.getEn());
            merchantLogPO.setTypeName(Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED,language) ? MerchantLogTypeEnum.SET_MANAGER.getRemark() : MerchantLogTypeEnum.SET_MANAGER.getRemarkEn());
            merchantLogPO.setIp(ipAddress);
            merchantLogService.saveLog(merchantLogPO);

            // 踢商户
            Object obj = merchantApiClient.kickoutMerchant(merchantPO.getMerchantCode());
            log.info("踢出商户:" + obj);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("对外商户后台-账户中心-二级商户管理-修改超级管理员名称异常：", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    /**
     *  S组特殊渠道商户配置
     *  渠道商户符合NACOS 配置  则设置merchantGroupId
     *  若merchantGroupId位空 则随机区任意S组商户的组id
     *  若不存在S组的商户组  则走系统预设组 即默认逻辑
     * @param merchantVO 商户VO
     */
    private void buildMerchantGroupId(MerchantVO merchantVO) {
        try{
            if(StringUtils.isBlank(specialMerchantGroupList)){
                return;
            }
            if(StringUtils.isEmpty(merchantVO.getParentId())){
                return;
            }
            List<String> specialMerchantGroupKVList = Arrays.stream(specialMerchantGroupList.split(",")).collect(Collectors.toList());
            for (String str : specialMerchantGroupKVList) {
                String [] setupStr = str.split("\\|");
                if(StringUtils.isEmpty(setupStr[0])){
                    continue;
                }
                Long merchantId = merchantMapper.getMerchantId(setupStr[0]);
                if(merchantId == null || !merchantId.equals(Long.valueOf(merchantVO.getParentId()))) {
                    continue;
                }
                Long merchantGroupId = Long.valueOf(setupStr[1]);
                MerchantGroupPO merchantGroup = tMerchantGroupMapper.selectMerchantGroupById(merchantGroupId);
                if(merchantGroup != null){
                    merchantVO.setMerchantGroupId(merchantGroupId);
                    continue;
                }
                log.error("createMerchant.buildMerchantGroupId.warning,NACOS配置ID不存在 ： {}", merchantGroupId);
                MerchantGroupPO param = new MerchantGroupPO();
                param.setGroupCode("s");
                List<MerchantGroupPO> merchantGroupPOList = tMerchantGroupMapper.selectMerchantGroup(param);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(merchantGroupPOList)){
                    merchantVO.setMerchantGroupId(merchantGroupPOList.get(0).getId());
                }
                log.error("createMerchant.buildMerchantGroupId.warning,S组商户组不存在，请及时补充 " );
            }
        }catch (Exception e){
            log.error("createMerchant.buildMerchantGroupId error",e);
        }
    }

}
