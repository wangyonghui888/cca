package com.panda.sport.merchant.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.bss.mapper.*;
import com.panda.sport.merchant.api.feign.MerchantManageClient;
import com.panda.sport.merchant.api.feign.MultiterminalClient;
import com.panda.sport.merchant.api.feign.PandaRcsCreditClient;
import com.panda.sport.merchant.api.service.MerchantService;
import com.panda.sport.merchant.api.util.ExecutorInstance;
import com.panda.sport.merchant.api.util.Md5Util;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.dto.CreditConfigApiDto;
import com.panda.sport.merchant.common.dto.CreditConfigDto;
import com.panda.sport.merchant.common.dto.CreditConfigSaveDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.panda.sport.merchant.api.service.impl.UserServiceImpl.domainPoolCacheMap;
import static com.panda.sport.merchant.api.service.impl.UserServiceImpl.merchantAreaDomainsCacheMap;
import static com.panda.sport.merchant.api.util.RedisConstants.MULTITERMINAL_DOMAIN_KEY;
import static com.panda.sport.merchant.api.util.RedisConstants.USERCENTER_FAMILY_KEY;

@Slf4j
@Service("merchantService")
@RefreshScope
@Primary
public class MerchantServiceImpl extends AbstractMerchantService implements MerchantService {

    @Autowired
    public MerchantLevelMapper merchantlogMapper;

    @Autowired
    private PandaRcsCreditClient pandaRcsCreditClient;

    @Autowired
    private MerchantManageClient merchantManageClient;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MultiterminalClient multiterminalClient;

    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private TUserMapper userMapper;
    @Autowired
    TDomainMapper tDomainMapper;
    @Value("${apidomain.default:null}")
    private String apidomain;

    @Value("${merchant.totalNum:}")
    private Integer totalNum;

    private final static String techSupport = "FDC67978BEC0E0CA84C9FA819470E092";

    @Override
    public APIResponse createAgent(HttpServletRequest request, String agentId, String agentName, String merchantCode, Long timestamp, String signature) throws Exception {
        String signStr = merchantCode + "&" + agentId + "&" + agentName + "&" + timestamp;
        try {
            APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("merchantService:" + errorResult);
                return errorResult;
            }
            String agentid = merchantMapper.getAgentId(merchantCode, agentId);
            log.info("agentid:" + agentid);
            int num = 0;
            if (StringUtil.isBlankOrNull(agentid)) {
                num = merchantMapper.insetMerchantAgent(merchantCode, agentId, agentName);
                log.info("agentid:insetMerchantAgent" + agentid);
            } else {
                num = merchantMapper.updateMerchantAgent(merchantCode, agentId, agentName);
                log.info("agentid:updateMerchantAgent" + agentid);
            }
            return APIResponse.returnSuccess(num > 0);
        } catch (Exception e) {
            log.error("MerchantController.createAgent,exception:" + signStr, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse changeUserAgent(HttpServletRequest request, String agentId, String userName, String merchantCode, Long timestamp, String signature) throws Exception {
        String signStr = merchantCode + "&" + agentId + "&" + userName + "&" + timestamp;
        try {
            APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("merchantService:" + errorResult);
                return errorResult;
            }
            String agentid = merchantMapper.getAgentId(merchantCode, agentId);
            log.info("agentid:" + agentid);
            int num = 0;
            UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
            if (userPO != null) {
                num = userMapper.updateUserAgentId(userPO.getUserId(), agentId);
                log.info("更新用户AgentId num=" + num);
            }
            return APIResponse.returnSuccess(num > 0);
        } catch (Exception e) {
            log.error("MerchantController.createAgent,exception:" + signStr, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse queryCreditLimitConfig(HttpServletRequest request, String merchantCode, String creditId, Long timestamp, String signature, String globalId) {
        String signStr = merchantCode + "&" + creditId + "&" + timestamp;
        APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
        if (errorResult != null && !errorResult.getStatus()) {
            log.error("merchantService:" + errorResult);
            return errorResult;
        }
        MerchantPO merchantPO = merchantMapper.getMerchantByMerchantCode(merchantCode);
        CreditConfigDto reqData = new CreditConfigDto();
        reqData.setCreditId(creditId);
        reqData.setMerchantId(Long.valueOf(merchantPO.getId()));
        Request req = new Request();
        req.setGlobalId(globalId);
        req.setData(reqData);
        CreditConfigDto dto = (CreditConfigDto) pandaRcsCreditClient.queryCreditLimitConfig(req).getData();
        CreditConfigApiDto apiDto = new CreditConfigApiDto();
        apiDto.setMerchantCode(merchantPO.getMerchantCode());
        apiDto.setSeriesConfigList(dto.getSeriesConfigList());
        apiDto.setSingleMatchConfigList(dto.getSingleMatchConfigList());
        apiDto.setSinglePlayConfigList(dto.getSinglePlayConfigList());
        return APIResponse.returnSuccess(apiDto);
    }

    @Override
    public APIResponse saveOrUpdateCreditLimitConfig(HttpServletRequest request, CreditConfigApiDto reqData, String globalId) {
        Request req = new Request();
        String signStr = reqData.getMerchantCode() + "&" + reqData.getTimestamp();
        APIResponse errorResult = checkParam(request, reqData.getTimestamp(), reqData.getMerchantCode(), signStr, reqData.getSignature());
        if (errorResult != null && !errorResult.getStatus()) {
            log.error("merchantService:" + errorResult);
            return errorResult;
        }
        MerchantPO merchantPO = merchantMapper.getMerchantByMerchantCode(reqData.getMerchantCode());
        CreditConfigSaveDto dto = new CreditConfigSaveDto();
        dto.setMerchantId(Long.valueOf(merchantPO.getId()));
        dto.setCreditAgentInfoList(reqData.getCreditAgentInfoList());
        dto.setSeriesConfigList(reqData.getSeriesConfigList());
        dto.setSingleMatchConfigList(reqData.getSingleMatchConfigList());
        dto.setSinglePlayConfigList(reqData.getSinglePlayConfigList());
        req.setData(dto);
        req.setGlobalId(globalId);
        Boolean bool = (Boolean) pandaRcsCreditClient.saveOrUpdateCreditLimitConfig(req).getData();
        return APIResponse.returnSuccess(bool);
    }

    @Override
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



    @Override
    public APIResponse kickoutMerchants(List<String> merchantCodes) {
        try {
            Thread.sleep(500);
            List<String> merchantCodeList;
            if (CollectionUtils.isNotEmpty(merchantCodes)) {
                merchantCodeList = merchantCodes;
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

    private void kickoutMerchantList(List<String> merchantCodeList){
        ExecutorInstance.executorService.submit(() -> {
            callClearCache(merchantCodeList, 0);
        });
    }

    @Override
    public APIResponse clearMerchantActivityCache(String merchantCode) {
        try {
            List<String> merchantCodeList;
            if (StringUtils.isNotEmpty(merchantCode)) {
                merchantCodeList = new ArrayList<>();
                merchantCodeList.add(merchantCode);
            } else {
                merchantCodeList = merchantMapper.queryAllPlatformCode();
            }
            ExecutorInstance.executorService.submit(() -> {
                for (String merchant : merchantCodeList) {
                    redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + merchant);
                    log.info("踢出商户活动:" + merchant);
                }
            });
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("踢出商户异常!", e);
            return APIResponse.returnFail("提出商户失败!");
        }
    }

    @Transactional
    @Override
    public APIResponse<Object> updateNewDomain(String oldDomain, Integer domainType, String newDomain) {
        try {
            String oldDomain2;
            if(oldDomain.endsWith("/")){
                oldDomain2 = oldDomain.substring(0,oldDomain.length()-1);
            }else{
                oldDomain2 = oldDomain + "/" ;
            }
            if(domainType == null){
                merchantMapper.updateNewDomain(oldDomain, 1, newDomain);
                merchantMapper.updateNewDomain(oldDomain, 2, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, 1, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, 2, newDomain);
            }else{
                merchantMapper.updateNewDomain(oldDomain, domainType, newDomain);
                merchantMapper.updateNewDomain(oldDomain2, domainType, newDomain);
            }
            List<MerchantPO> merchantPOList = merchantMapper.listByDomainName(domainType, oldDomain);
            merchantPOList.forEach(m -> redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + m.getMerchantCode()));
            this.kickoutMerchant(null);
            log.info("updateNewDomain,domainType:{},oldDomain:{} ,newDomain:{}", domainType,oldDomain, newDomain);
        } catch (Exception e) {
            log.error(oldDomain + ",updateNewDomain:" + newDomain, e);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public void replaceDomain(String source, String target, Integer domainType) {
        // 1h5域名 2PC域名 3App域名
        switch (domainType){
            case 1:
                merchantMapper.replaceH5(source,target);
                break;
            case 2:
                merchantMapper.replacePC(source,target);
                break;
            case 3:
                merchantMapper.replaceAPP(source,target);
        }

        List<MerchantPO> merchantPOList = merchantMapper.listByDomainName(domainType, source);
        merchantPOList.forEach(m -> redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.IN_ACTIVITY + m.getMerchantCode()));
        this.kickoutMerchant(null);
        log.info("手动切换域名：replaceDomain,domainType:{},oldDomain:{} ,newDomain:{}", domainType,source, target);

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
        String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
        String secondKey = "";
        if(StringUtils.isNotEmpty(merchantPO.getSecondMerchantKey())){
            secondKey = AESUtils.aesDecode(merchantPO.getSecondMerchantKey());
        }
        if (StringUtil.isBlankOrNull(key) && StringUtil.isBlankOrNull(secondKey)) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        Boolean keyFlag = !Md5Util.checkMd5(signStr, key, signature);
        Boolean secondKeyFlag = !Md5Util.checkMd5(signStr, secondKey, signature);
        if (keyFlag && secondKeyFlag) {
            log.error(merchantCode + ",验签失败!" + key + "," + secondKey);
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
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
    public APIResponse getAllApiDomain(String merchantCode) {
        List<String> domainList = merchantMapper.getAllApiDomain(merchantCode);
        List<String> resultList = new ArrayList<>();
        for (String str : domainList) {
            if (StringUtils.isNotEmpty(str)) {
                if (str.contains(",")) {
                    resultList.addAll(Arrays.asList(str.split(",")));
                } else {
                    resultList.add(str);
                }
            }
        }
        return APIResponse.returnSuccess(resultList);
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

    @Override
    public APIResponse<Object> changeDomain(String domain, String merchantCode, String signature) {
        String signStr = domain + "&" + merchantCode;
        MerchantPO merchantPO = this.getMerchantPO(merchantCode);
        if (merchantPO == null) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        //商户验签
        String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
        String secondKey = "";
        if(StringUtils.isNotEmpty(merchantPO.getSecondMerchantKey())){
            secondKey = AESUtils.aesDecode(merchantPO.getSecondMerchantKey());
        }
        if (StringUtil.isBlankOrNull(key) && StringUtil.isBlankOrNull(secondKey)) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        Boolean keyFlag = !Md5Util.checkMd5(signStr, key, signature);
        Boolean secondKeyFlag = !Md5Util.checkMd5(signStr, secondKey, signature);
        if (keyFlag && secondKeyFlag) {
            log.error(merchantCode + ",验签失败!" + key + "," + secondKey);
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }
        /*String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
        if (!Md5Util.checkMd5(signStr, key, signature)) {
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }*/
        String newDomain = tDomainMapper.getAvailableDomain();
        merchantMapper.updateApiDomain(domain, newDomain);
        log.info("updateApiDomain,oldDomain:" + domain + ",newDomain:" + newDomain);
        kickoutMerchant(merchantCode);
        merchantManageClient.checkSingleDomain(domain);
        log.info("修改域名成功!");
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

                String userKey = RedisConstants.MERCHANT_FAMILY + RedisConstants.USER_BUSSINESS + merchantCode;
                redisTemp.delete(userKey);

                MerchantPO merchantPO = (MerchantPO) redisTemp.getObject(key, MerchantPO.class);
                log.info(merchantCode + ",callClearCache.merchantPO=" + merchantPO);
                //此key,投注和结算使用
                redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.BUSSINESS + merchantCode);
                log.info(times + ",踢出商户:" + merchantCode);

                key = USERCENTER_FAMILY_KEY + MULTITERMINAL_DOMAIN_KEY + merchantCode;
                redisTemp.delete(key);
                log.info( "{}，删除三端商户域名缓存:{},key:{}",times,merchantCode,key);
            }
        } catch (Exception e) {
            log.error("Clear商户缓存异常!", e);
        }
        callClearCache(merchantCodeList, times + 1);
    }



    @Override
    public APIResponse getH5PcDomain() {
        List<String> list = CacheService.domainMap.getIfPresent(CacheService.DOMAIN);

        if (CollectionUtil.isEmpty(list)) {
            List<String>  domainList =buildH5PcDomain();
            CacheService.domainMap.put(CacheService.DOMAIN,domainList);
            log.info("H5、Pc域名缓存初始化成功:" + domainList.size());
            return APIResponse.returnSuccess(domainList);
        }
        return APIResponse.returnSuccess(list);
    }

    @Override
    public void updateMerchantChatSwitch(SystemSwitchVO systemSwitchVO) {
        if(systemSwitchVO != null){
            String key = RedisConstants.MERCHANT_CHAT + systemSwitchVO.getConfigKey();
            redisTemp.setObject(key, systemSwitchVO);
        }
    }

    @Override
    public APIResponse<Object> updateVideoDomain(String newDomain, String oldDomain, String merchantCode) {
        if(StringUtils.isNotEmpty(merchantCode)){
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            if(StringUtils.isNotEmpty(newDomain) && StringUtils.isNotEmpty(oldDomain)){
                if(!newDomain.equals(oldDomain)){
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
    @Async
    public void clearStressTestData(String merchantCode, Integer num) {
        Integer count = totalNum/num;
        if(totalNum%num>0){
            count = count +1;
        }
        for (int i = 0; i < count; i++){
            Integer indexNum = i * num;
            merchantMapper.clearOrderData(merchantCode, indexNum, num);
            merchantMapper.clearOrderDetailData(merchantCode, indexNum, num);
            merchantMapper.clearSettleData(merchantCode, indexNum, num);
        }
    }



    /**
     * 组装域名
     * @return
     */
    public List<String> buildH5PcDomain() {
        //获取H5、Pc老域名
        List<String> oldDomainList = merchantMapper.getOldH5PcDomain();
        try {
            //获取H5、Pc新域名
            APIResponse domain = multiterminalClient.getNewH5PcDomain();

            if (null != domain && Objects.nonNull(domain.getData())) {
                ObjectMapper mapper = new ObjectMapper();
                List<String>  newDomainList = mapper.convertValue(domain.getData(), new TypeReference<List<String>>() {
                });

                //新老域名合并
                oldDomainList.addAll(newDomainList);
            }
        }catch (Exception ex){
            log.error("获取H5、Pc新域名错误:",ex);
        }

        if(CollectionUtil.isEmpty(oldDomainList)){
            log.error("暂无域名!");
        }

        //域名去重
        List<String> list = oldDomainList.stream()
                .map(item -> item.split(","))
                .flatMap(Stream::of)
                .distinct().collect(Collectors.toList());

        return list;
    }

    /**
     * 更新商户缓存
     * @param merchantConfig
     * @return
     */
    @Override
    public APIResponse updateMerchantCache(MerchantConfig merchantConfig) {
        try {
            List<String> merchantCodeList = merchantConfig.getMerchantCodeList();
            String defaultVideoDomain = merchantConfig.getDefaultVideoDomain();
            if(CollectionUtils.isNotEmpty(merchantCodeList)){
                for (String merchantCode : merchantCodeList){
                    String key1 = RedisConstants.MERCHANT_FAMILY + RedisConstants.MERCHANT_CODE_PREFIX + merchantCode;
                    MerchantPO po = (MerchantPO) redisTemp.getObject(key1, MerchantPO.class);
                    if(po != null){
                        po.setDefaultVideoDomain(defaultVideoDomain);
                        redisTemp.delete(key1);
                        redisTemp.setObject(key1, po, RedisConstants.EXPIRE_TIME_ONE_HOUR);
                        log.info(merchantCode + ",updateMerchantCache.po=" + po);
                    }
                    String key2 = RedisConstants.MERCHANT_FAMILY + RedisConstants.BUSSINESS + merchantCode;
                    MerchantPO merchantPO = (MerchantPO) redisTemp.getObject(key2, MerchantPO.class);
                    if(null != merchantPO){
                        merchantPO.setDefaultVideoDomain(defaultVideoDomain);
                        redisTemp.delete(key2);
                        redisTemp.setObject(key2, merchantPO, RedisConstants.EXPIRE_TIME_ONE_HOUR);
                        log.info(merchantCode + ",updateMerchantCache.merchantPO=" + merchantPO);
                    }
                }
            }
        }catch (Exception e){
            log.error("clear merchant cache error!", e);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse changeMerchantDomain(String merchantDomainGroup, String merchantDomain) {
        String key;
        if(merchantDomainGroup.contains("PC")){
            key = RedisConstants.MERCHANT_FAMILY + RedisConstants.PC_DOMAIN;
            redisTemp.setObject(key, merchantDomain, RedisConstants.EXPIRE_TIME_ONE_MIN);
        }else if(merchantDomainGroup.contains("H5")){
            key = RedisConstants.MERCHANT_FAMILY + RedisConstants.H5_DOMAIN;
            redisTemp.setObject(key, merchantDomain, RedisConstants.EXPIRE_TIME_ONE_MIN);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public List<SystemConfig> querySystemConfig(SystemConfig po) {
        return systemConfigMapper.querySystemConfig(po);
    }

    @Override
    public int createSystemConfig(SystemConfig po) {
        return systemConfigMapper.createSystemConfig(po);
    }

    @Override
    public int updateSystemConfig(SystemConfig po) {
        return systemConfigMapper.updateSystemConfig(po);
    }
}