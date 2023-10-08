package com.panda.sport.merchant.api.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.bss.mapper.MerchantConfigMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.api.util.Md5Util;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Slf4j
@RefreshScope
@Service("merchantService")
public abstract class AbstractMerchantService {

    @Autowired
    public MerchantMapper merchantMapper;

    @Autowired
    public MerchantConfigMapper merchantConfigMapper;

    @Autowired
    public RedisTemp redisTemp;

    @Value("${cache_switch:off}")
    private String cacheSwitch;

    @Value("${merchant.ip.check.switch:on}")
    public String ipCheckSwitch;

    public static Cache<String, String> merchantPOCacheMap = null;

    @PostConstruct
    private void init() {
        merchantPOCacheMap = Caffeine.newBuilder()
                .maximumSize(5_000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
    }

    protected MerchantPO getMerchantPO(String merchantCode) {
        if ("off".equals(cacheSwitch)) {
            log.info("getMerchantPO--商户缓存开关:" + cacheSwitch);
            return merchantMapper.getMerchantAPI(merchantCode);
        }

        if (merchantPOCacheMap != null && merchantPOCacheMap.getIfPresent(merchantCode) != null) {
            log.info("getMerchantPO--cache" + merchantCode);
            if (Objects.equals(merchantPOCacheMap.getIfPresent(merchantCode), "1")) {
                return null;
            }
            return JSONObject.parseObject(merchantPOCacheMap.getIfPresent(merchantCode), MerchantPO.class);
        }

        String key = RedisConstants.MERCHANT_FAMILY + RedisConstants.MERCHANT_CODE_PREFIX + merchantCode;
        MerchantPO redisMerchantInfo = (MerchantPO) redisTemp.getObject(key, MerchantPO.class);
        if (redisMerchantInfo != null) {
            log.info("getMerchantPO--redis" + merchantCode);
            merchantPOCacheMap.put(merchantCode, JSON.toJSONString(redisMerchantInfo));
            redisTemp.setObject(key, redisMerchantInfo, RedisConstants.EXPIRE_TIME_ONE_HOUR);
            return redisMerchantInfo;
        }
        MerchantPO merchantPO = merchantMapper.getMerchantAPI(merchantCode);
        if (Objects.isNull(merchantPO)) {
            log.info("getMerchantPO--mysql--null:" + merchantCode);
            merchantPOCacheMap.put(merchantCode, "1");
            return null;
        }
        log.info("getMerchantPO--mysql--{}", merchantCode);
        try {
            //解析盘口开关
            String coilSwitch = merchantPO.getCoilSwitch();
            List<String> switchList = Arrays.asList(coilSwitch.substring(1, coilSwitch.length() - 1).split(","));
            for (String item : switchList) {
                String[] str = item.split(":");
                if (str[0].equalsIgnoreCase("1")) {
                    merchantPO.setBookMarketSwitch(Integer.valueOf(str[1]));
                } else if (str[0].equalsIgnoreCase("2")) {
                    merchantPO.setBookMarketSwitchBasketball(Integer.valueOf(str[1]));
                }
            }
        } catch (Exception e) {
            log.error(merchantCode + "MerchantPO:", e);
            merchantPO.setBookMarketSwitch(1);
            merchantPO.setBookMarketSwitchBasketball(1);
        }
        merchantPOCacheMap.put(merchantCode, JSON.toJSONString(merchantPO));
        redisTemp.setObject(key, merchantPO, RedisConstants.EXPIRE_TIME_ONE_HOUR);
        return merchantPO;
    }

    protected MerchantConfig getMerchantConfig(String merchantCode) {
        return merchantConfigMapper.getConfigByMerchantCode(merchantCode);
    }

    protected boolean checkIp(HttpServletRequest request, String targetIp) {
        String originIp = IPUtils.getIpAddr(request);
        if (StringUtils.isEmpty(originIp)) {
            return false;
        }
        if (StringUtils.isEmpty(targetIp)) {
            return true;
        }
        return targetIp.contains(originIp);
    }


    protected APIResponse checkParam(HttpServletRequest request, Long timestamp, String merchantCode, String signStr, String signature) {
        if (Math.abs((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60)) > 23) {
            return APIResponse.returnFail(ApiResponseEnum.REQUEST_TIME_OUT);
        }
        MerchantPO merchantPO = this.getMerchantPO(merchantCode);
        if (merchantPO == null) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        if (ipCheckSwitch.equalsIgnoreCase("on") && !this.checkIp(request, merchantPO.getWhiteIp())) {
            String originIp = IPUtils.getIpAddr(request);
            log.error("请求IP:" + originIp + ", 验证IP失败:" + merchantPO.getWhiteIp());
            //StringBuilder sb = new StringBuilder();
            //telegramService.sendTelegram(sb.append(merchantPO.getMerchantName()).append(",").append(merchantCode).append(",").append("验证IP失败!!!请求IP:").append(originIp).append(",白名单:").append(merchantPO.getWhiteIp()));
            return APIResponse.returnFail(ApiResponseEnum.IP_FAILS);
        }
        //商户验签
        APIResponse<Object> response = checkSignature(signStr, signature, merchantPO);
        if (response != null) {
            return response;
        }
        return APIResponse.returnSuccess(merchantPO);
    }

    protected APIResponse<Object> checkSignature(String signStr, String signature, MerchantPO merchantPO) {
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
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }
        return null;
    }

}
