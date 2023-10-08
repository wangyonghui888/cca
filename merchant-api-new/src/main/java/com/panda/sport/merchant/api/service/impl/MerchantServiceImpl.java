package com.panda.sport.merchant.api.service.impl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.*;
import com.panda.sport.merchant.api.service.MerchantService;
import com.panda.sport.merchant.api.util.DomainServiceUtil;
import com.panda.sport.merchant.api.util.ExecutorInstance;
import com.panda.sport.merchant.api.util.Md5Util;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.dto.FrontDomainMerchantDTO;
import com.panda.sport.merchant.common.dto.VideoDomainMerchantDTO;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.SystemConfig;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.api.service.impl.UserServiceImpl.domainPoolCacheMap;
import static com.panda.sport.merchant.api.service.impl.UserServiceImpl.merchantAreaDomainsCacheMap;
import static com.panda.sport.merchant.api.util.RedisConstants.MULTITERMINAL_DOMAIN_KEY;
import static com.panda.sport.merchant.api.util.RedisConstants.USERCENTER_FAMILY_KEY;
import static com.panda.sport.merchant.common.constant.SystemConfigConstant.*;

@Slf4j
@Service("merchantService")
@RefreshScope
public class MerchantServiceImpl extends AbstractMerchantService implements MerchantService {

    @Autowired
    public DomainServiceUtil domainServiceUtil;

    @Autowired
    public MerchantLevelMapper merchantlogMapper;

    @Autowired
    public DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    public TransactionDefinition transactionDefinition;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private CacheService cacheService;


    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    TDomainMapper tDomainMapper;

    @Value("${apidomain.default:null}")
    private String apidomain;

    private final static String techSupport = "FDC67978BEC0E0CA84C9FA819470E092";


    @Transactional
    @Override
    public APIResponse<Object> updateNewDomain(String oldDomain, Integer domainType, String newDomain) {
        try {
            String oldDomain2;
            if (oldDomain.endsWith(DOMAIN_COLON)) {
                oldDomain2 = oldDomain.substring(0, oldDomain.length() - 1);
            } else {
                oldDomain2 = oldDomain + DOMAIN_COLON;
            }
            if (domainType == null) {
                merchantMapper.updateNewDomain(oldDomain, 1, newDomain);
                merchantMapper.updateNewDomain(oldDomain, 2, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, 1, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, 2, newDomain);
            } else {
                merchantMapper.updateNewDomain(oldDomain, domainType, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, domainType, newDomain);
            }
            List<MerchantPO> merchantPOList = merchantMapper.listByDomainName(domainType, oldDomain);
            merchantPOList.forEach(m -> redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + m.getMerchantCode()));
            this.kickoutMerchant(null);
            Thread.sleep(1000);
            log.info("updateNewDomain.开始处理系统域名缓存!");
            domainServiceUtil.initDomainSystemConfigList();
            log.info("updateNewDomain,domainType:{},oldDomain:{} ,newDomain:{}", domainType, oldDomain, newDomain);
        } catch (Exception e) {
            log.error(oldDomain + ",updateNewDomain:" + newDomain, e);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse<Object> updateNewDomainAndChangeSystemConfig(String oldDomain, Integer domainType, String newDomain) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            String oldDomain2;
            if (oldDomain.endsWith(DOMAIN_COLON)) {
                oldDomain2 = oldDomain.substring(0, oldDomain.length() - 1);
            } else {
                oldDomain2 = oldDomain + DOMAIN_COLON;
            }
            if (domainType == null) {
                merchantMapper.updateNewDomain(oldDomain, 1, newDomain);
                merchantMapper.updateNewDomain(oldDomain, 2, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, 1, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, 2, newDomain);
            } else {
                merchantMapper.updateNewDomain(oldDomain, domainType, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, domainType, newDomain);
            }
            List<MerchantPO> merchantPOList = merchantMapper.listByDomainName(domainType, oldDomain);

            log.info("updateNewDomain,domainType:{},oldDomain:{} ,newDomain:{}", domainType, oldDomain, newDomain);

            // 同步处理下t_system_config里面的域名
            List<SystemConfig> systemConfigList = this.querySystemConfig(SystemConfig.builder().configKey(FRONT_MERCHANT_GROUP_PREFIX).build());
            if(CollectionUtils.isNotEmpty(systemConfigList)){
                if(oldDomain.endsWith(DOMAIN_COLON)) oldDomain = oldDomain.substring(0,oldDomain.length()-1);
                for (SystemConfig systemConfig : systemConfigList) {
                    final String remark = systemConfig.getRemark();
                    if(this.getH5BySystemConfig(remark).contains(oldDomain)){
                        JSONObject remarkJ = JSONObject.parseObject(remark,JSONObject.class);
                        remarkJ.put(H5_DOMAIN, newDomain);
                        systemConfig.setRemark(remarkJ.toJSONString());
                        systemConfig.setUpdateTime(System.currentTimeMillis());
                        systemConfigMapper.updateSystemConfig(systemConfig);
                    }
                    if(this.getPCBySystemConfig(remark).contains(oldDomain)){
                        JSONObject remarkJ = JSONObject.parseObject(remark,JSONObject.class);
                        remarkJ.put(PC_DOMAIN, newDomain);
                        systemConfig.setRemark(remarkJ.toJSONString());
                        systemConfig.setUpdateTime(System.currentTimeMillis());
                        systemConfigMapper.updateSystemConfig(systemConfig);
                    }
                }
            }

            merchantPOList.forEach(m -> redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + m.getMerchantCode()));
            this.kickoutMerchant(null);
            Thread.sleep(1000);
            log.info("updateNewDomain.开始处理系统域名缓存!");
            domainServiceUtil.initDomainSystemConfigList();

            dataSourceTransactionManager.commit(transactionStatus);//提交
        } catch (Exception e) {
            log.error(oldDomain + ",updateNewDomain:" + newDomain, e);
        }finally {
            dataSourceTransactionManager.rollback(transactionStatus);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse<Object> updateNewVideoDomainAndChangeSystemConfig(String oldDomain, Integer domainType, String newDomain) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            String oldDomain2;
            if (oldDomain.endsWith(DOMAIN_COLON)) {
                oldDomain2 = oldDomain.substring(0, oldDomain.length() - 1);
            } else {
                oldDomain2 = oldDomain + DOMAIN_COLON;
            }
            merchantMapper.updateNewVideoDomain(oldDomain, null, newDomain);
            merchantMapper.updateNewVideoDomain(oldDomain2, null, newDomain);


            List<MerchantPO> merchantPOList = merchantMapper.listByDomainName(domainType, oldDomain);

            log.info("updateNewDomain,domainType:{},oldDomain:{} ,newDomain:{}", domainType, oldDomain, newDomain);

            // 同步处理下t_system_config里面的域名
            List<SystemConfig> systemConfigList = this.querySystemConfig(SystemConfig.builder().configKey(VIDEO_MERCHANT_GROUP_PREFIX).build());
            if(CollectionUtils.isNotEmpty(systemConfigList)){
                if(oldDomain.endsWith(DOMAIN_COLON)) oldDomain = oldDomain.substring(0,oldDomain.length()-1);
                for (SystemConfig systemConfig : systemConfigList) {
                    final String remark = systemConfig.getRemark();
                    if(this.getVideoBySystemConfig(remark).contains(oldDomain)){
                        JSONObject remarkJ = JSONObject.parseObject(remark,JSONObject.class);
                        remarkJ.put(VIDEO_COLUMN_VIDEO_ALL, newDomain);
                        systemConfig.setRemark(remarkJ.toJSONString());
                        systemConfig.setUpdateTime(System.currentTimeMillis());
                        systemConfigMapper.updateSystemConfig(systemConfig);
                    }
                }
            }

            merchantPOList.forEach(m -> redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + m.getMerchantCode()));
            this.kickoutMerchant(null);
            Thread.sleep(1000);

            dataSourceTransactionManager.commit(transactionStatus);//提交
        } catch (Exception e) {
            log.error(oldDomain + ",updateNewDomain:" + newDomain, e);
        }finally {
            dataSourceTransactionManager.rollback(transactionStatus);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public void replaceDomainByMerchant(FrontDomainMerchantDTO domainDTO) {

        List<SystemConfig> systemConfigList = systemConfigMapper.querySystemConfig(SystemConfig.builder().id(domainDTO.getId()).build());
        if(CollectionUtils.isEmpty(systemConfigList)){
            return;
        }
        final SystemConfig systemConfig = systemConfigList.get(0);
        List<String> merchantCodeList = Lists.newArrayList();
        if(Objects.equals(domainDTO.getId(), FRONT_DOMAIN_POOL_ID) || systemConfig.getConfigKey().equals(FRONT_MERCHANT_GROUP_PREFIX + "系统默认商户组")){
            List<SystemConfig> systemConfigList2 = systemConfigMapper.querySystemConfig(SystemConfig.builder().configKey(FRONT_MERCHANT_GROUP_PREFIX).build());
            for (SystemConfig config : systemConfigList2) {
                merchantCodeList.addAll(getMerchantCodeListBySystemConfig(config.getRemark()));
            }
            if(CollectionUtils.isEmpty(merchantCodeList)){
                return;
            }
            if(StringUtils.isNotBlank(domainDTO.getPcDomain())){
                merchantMapper.updateMerchantDomainByMerchantCodes(2, domainDTO.getPcDomain(), null,null,merchantCodeList);
            }
            if(StringUtils.isNotBlank(domainDTO.getH5Domain())){
                merchantMapper.updateMerchantDomainByMerchantCodes(1, domainDTO.getH5Domain(), null,null,merchantCodeList);
            }
        }else{
            merchantCodeList = domainDTO.getMerchantCodeList();
            if(CollectionUtils.isEmpty(merchantCodeList)){
                return ;
            }
            if(StringUtils.isNotBlank(domainDTO.getPcDomain())){
                merchantMapper.updateMerchantDomainByMerchantCodes(2, domainDTO.getPcDomain(), null,merchantCodeList,null);
            }
            if(StringUtils.isNotBlank(domainDTO.getH5Domain())){
                merchantMapper.updateMerchantDomainByMerchantCodes(1, domainDTO.getH5Domain(), null,merchantCodeList,null);
            }
        }
    }


    @Override
    public void replaceVideoDomainByMerchant(VideoDomainMerchantDTO domainDTO) {
        List<SystemConfig> systemConfigList = systemConfigMapper.querySystemConfig(SystemConfig.builder().id(domainDTO.getId()).build());
        if(CollectionUtils.isEmpty(systemConfigList)){
            return;
        }
        final SystemConfig systemConfig = systemConfigList.get(0);
        List<String> merchantCodeList = Lists.newArrayList();
        if(Objects.equals(domainDTO.getId(), VIDEO_DOMAIN_POOL_ID) || systemConfig.getConfigKey().equals(VIDEO_MERCHANT_GROUP_PREFIX + "系统默认商户组")){
            List<SystemConfig> systemConfigList2 = systemConfigMapper.querySystemConfig(SystemConfig.builder().configKey(VIDEO_MERCHANT_GROUP_PREFIX).build());
            for (SystemConfig config : systemConfigList2) {
                merchantCodeList.addAll(getMerchantCodeListBySystemConfig(config.getRemark()));
            }
            if(CollectionUtils.isEmpty(merchantCodeList)){
                return;
            }
            if(StringUtils.isNotBlank(domainDTO.getVideoAll())){
                merchantMapper.updateMerchantVideoDomainByMerchantCodes(null, domainDTO.getVideoAll(), null,null, merchantCodeList);
            }

        }else{
            merchantCodeList = domainDTO.getMerchantCodeList();
            if(CollectionUtils.isEmpty(merchantCodeList)){
                return ;
            }
            merchantMapper.updateMerchantVideoDomainByMerchantCodes(null, domainDTO.getVideoAll(), null,merchantCodeList, null);
        }
    }

    public APIResponse kickoutMerchant(String merchantCode) {
        try {
            Thread.sleep(500);
            List<String> merchantCodeList;
            if (StringUtils.isNotEmpty(merchantCode)) {
                merchantCodeList = new ArrayList<>();
                merchantCodeList.add(merchantCode);
            } else {
                merchantCodeList = merchantMapper.queryAllPlatformCode();
            }
            this.kickoutMerchantList(merchantCodeList);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("踢出商户异常!", e);
            return APIResponse.returnFail("踢出商户失败!");
        }
    }

    private void kickoutMerchantList(List<String> merchantCodeList) {
        ExecutorInstance.executorService.submit(() -> {
            callClearCache(merchantCodeList, 0);
        });
    }


    @Override
    public void replaceDomain(String source, String target, Integer domainType) {
        // 1h5域名 2PC域名 3App域名
        switch (domainType) {
            case 1:
                merchantMapper.replaceH5(source, target);
                break;
            case 2:
                merchantMapper.replacePC(source, target);
                break;
            case 3:
                merchantMapper.replaceAPP(source, target);
        }

        List<MerchantPO> merchantPOList = merchantMapper.listByDomainName(domainType, source);
        merchantPOList.forEach(m -> redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + m.getMerchantCode()));
        this.kickoutMerchant(null);
        log.info("手动切换域名：replaceDomain,domainType:{},oldDomain:{} ,newDomain:{}", domainType, source, target);

    }

    @Override
    public APIResponse getAPIDomain(HttpServletRequest request, String merchantCode, Long timestamp, String signature) {
        String signStr = merchantCode + "&" + timestamp;
        if (Math.abs((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60)) > 23) {
            return APIResponse.returnFail(ApiResponseEnum.REQUEST_TIME_OUT);
        }
        MerchantPO merchantPO = this.getMerchantPO(merchantCode);
        if (merchantPO == null) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        //商户验签
        APIResponse<Object> response = this.checkSignature(signStr, signature, merchantPO);
        if (response != null) {
            return response;
        }
        /*String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
        if (StringUtil.isBlankOrNull(key)) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        if (!Md5Util.checkMd5(signStr, key, signature)) {
            log.info("getAPIDomain:"+signStr+"----"+key+"---"+signature+"---getMD5(str, key)"+Md5Util.getMD5(signStr, key));
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }*/
        String domain = merchantPO.getAppDomain();
        if (StringUtils.isEmpty(domain)) {
            domain = apidomain;
        }
        log.info(merchantCode + "域名:" + domain);
        List<String> domainList = Arrays.asList(domain.split(","));
        return APIResponse.returnSuccess(domainList);
    }

    @Override
    @Transactional
    public APIResponse updateApiDomain(String oldDomain, String newDomain, String signature) {
        String signStr = oldDomain + "&" + newDomain;
        if (!Md5Util.checkMd5(signStr, techSupport, signature)) {
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }
        merchantMapper.updateApiDomain(oldDomain, newDomain);
        log.info("updateApiDomain,oldDomain:" + oldDomain + ",newDomain:" + newDomain);
        merchantMapper.updateDomainPool(oldDomain, newDomain);

        MerchantLogPO merchantLog = new MerchantLogPO();

        merchantLog.setPageCode("modify_domain");

        merchantLog.setPageName("人工修改域名");

        merchantLog.setOperatType(MerchantLogTypeEnum.MANUAL_CHANGE_MERCHANT_Domian.getCode());
        merchantLog.setTypeName("modify API domain");

        merchantLog.setAfterValues(JSON.toJSONString(newDomain));

        merchantLog.setBeforeValues(JSON.toJSONString(oldDomain));

        merchantLog.setMerchantCode("all");
        merchantLog.setMerchantName("all");
        merchantLog.setDataId(System.currentTimeMillis() + "");
        merchantLog.setLogTag(1);
        merchantLog.setUserId("999999999999999");
        merchantLog.setUserName("TechSupport");
        merchantLog.setOperatTime(System.currentTimeMillis());
        merchantlogMapper.insertMerchantLog(merchantLog);
        return APIResponse.returnSuccess();
    }

    private void callClearCache(List<String> merchantCodeList, int times) {
        try {
            if (times >= 3) {
                return;
            }
            for (String merchantCode : merchantCodeList) {

                // 清除本地缓存
                domainPoolCacheMap.invalidate(merchantCode);
                merchantAreaDomainsCacheMap.invalidateAll();

                String key = RedisConstants.MERCHANT_FAMILY + RedisConstants.MERCHANT_CODE_PREFIX + merchantCode;
                redisTemp.delete(key);

                MerchantPO merchantPO = (MerchantPO) redisTemp.getObject(key, MerchantPO.class);
                log.info(merchantCode + ",callClearCache.merchantPO=" + merchantPO);
                redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.BUSSINESS + merchantCode);
                log.info(times + ",踢出商户:" + merchantCode);

                key = USERCENTER_FAMILY_KEY + MULTITERMINAL_DOMAIN_KEY + merchantCode;
                redisTemp.delete(key);
                log.info("{}，删除三端商户域名缓存:{},key:{}", times, merchantCode, key);
            }
        } catch (Exception e) {
            log.error("Clear商户缓存异常!", e);
        }
        callClearCache(merchantCodeList, times + 1);
    }


    @Override
    public APIResponse<Object> updateVideoDomain(String newDomain, String oldDomain, String merchantCode) {
        if (StringUtils.isNotEmpty(merchantCode)) {
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            if (StringUtils.isNotEmpty(newDomain) && StringUtils.isNotEmpty(oldDomain)) {
                if (!newDomain.equals(oldDomain)) {
                    String key = RedisConstants.MERCHANT_FAMILY + RedisConstants.MERCHANT_CODE_PREFIX + merchantCode;
                    redisTemp.delete(key);
                    merchantPO.setVideoDomain(newDomain);
                    redisTemp.setObject(key, merchantPO, RedisConstants.EXPIRE_TIME_ONE_HOUR);
                }
            }
        }
        return APIResponse.returnSuccess();
    }


    @Override
    public List<SystemConfig> querySystemConfig(SystemConfig po) {
        return systemConfigMapper.querySystemConfig(po);
    }

    @Override
    public int createSystemConfig(SystemConfig po) {
        Long id = systemConfigMapper.queryMaxId();
        po.setId(id);
        return systemConfigMapper.createSystemConfig(po);
    }

    @Override
    public int updateSystemConfig(SystemConfig po) {
        return systemConfigMapper.updateSystemConfig(po);
    }

    @Override
    public APIResponse queryMerchantDomain(String oldDomain) {
        String oldDomain1;
        if(oldDomain.endsWith(DOMAIN_COLON)){
            oldDomain1 = oldDomain.substring(0,oldDomain.length()-1);
        }else{
            oldDomain1 = oldDomain + DOMAIN_COLON;
        }
        return APIResponse.returnSuccess(merchantMapper.queryMerchant(Arrays.asList(oldDomain, oldDomain1)));
    }

    @Override
    public APIResponse queryMerchantVideoDomain(String oldDomain) {
        String oldDomain1;
        if(oldDomain.endsWith(DOMAIN_COLON)){
            oldDomain1 = oldDomain.substring(0,oldDomain.length()-1);
        }else{
            oldDomain1 = oldDomain + DOMAIN_COLON;
        }
        return APIResponse.returnSuccess(merchantMapper.queryMerchantVideoDomain(Arrays.asList(oldDomain, oldDomain1)));
    }



    private List<String> getMerchantCodeListBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return Lists.newArrayList();
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        String merchantCodeStr = remarkJ.getString("merchantCodeList");
        return StringUtils.isBlank(merchantCodeStr) ?
                Lists.newArrayList() : Arrays.stream(merchantCodeStr.split(",")).collect(Collectors.toList());
    }

    private String getH5BySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString(H5_DOMAIN) != null ?remarkJ.getString(H5_DOMAIN) :"";
    }

    private String getVideoBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString(VIDEO_COLUMN_VIDEO_ALL) != null ?remarkJ.getString(VIDEO_COLUMN_VIDEO_ALL) :"";
    }

    private String getPCBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString(PC_DOMAIN) != null ?remarkJ.getString(PC_DOMAIN) :"";
    }

}