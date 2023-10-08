package com.panda.sport.merchant.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.bss.mapper.SystemConfigMapper;

import com.panda.sport.merchant.api.config.RedisTemp;

import com.panda.sport.merchant.common.po.bss.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service("domainServiceUtil")
@RefreshScope
public class DomainServiceUtil {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    public RedisTemp redisTemp;

    @Value("${login.front.domain.pool:off}")
    private String frontDomainPool;

    @Value("${front_pc_url}")
    private String frontPcUrl;

    @Value("${front_inh5_url}")
    private String frontinH5Url;

    @Value("${front_mobile_url}")
    private String frontMobileUrl;

    @Value("${front_bwh5_url}")
    private String front_bwh5_url;

    private static final String FRONT_END_DOMAIN_KEY = "front_end_domain_";
    private static final String FRONT_DOMAIN_KEY = "front_merchant_group_";

    public static Cache<String, List<SystemConfig>> frontDomainCache = null;

    @PostConstruct
    private void init() {

        frontDomainCache = Caffeine.newBuilder()
                .maximumSize(3000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        initDomainSystemConfigList();
    }

    /**
     * 是否走前端的域名池
     * frontDomainPool = on  则优先域名池 最后NACOS
     * frontDomainPool = off 则直接取NACOS
     */
    public String getDomain(String terminal, MerchantPO merchantPO) {
        return frontDomainPool.equalsIgnoreCase("off") ? getDomainOld(terminal, merchantPO)
                : getDomainByFrontDomainPool(terminal, merchantPO);
    }

    private String getDomainByFrontDomainPool(String terminal, MerchantPO merchantPO) {
        try {
            String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
            if ("PC".equalsIgnoreCase(terminal)) {
                if (StringUtils.isNotEmpty(pcDomain)) {
                    return pcDomain;
                } else {
                    return getFrontDomainByMulti("PC", merchantPO.getMerchantCode());
                }
            } else if ("MOBILE".equalsIgnoreCase(terminal)) {
                if (StringUtils.isNotEmpty(h5Domain)) {
                    return h5Domain;
                } else {
                    return getFrontDomainByMulti("H5", merchantPO.getMerchantCode());
                }
            } else if ("H5-1".equalsIgnoreCase(terminal)) {
                return front_bwh5_url;
            } else {
                //走兜底域名的商户及设备类型记录
                this.saveParameter(terminal, merchantPO);
                return frontinH5Url;
            }
        } catch (Exception e) {
            log.error(terminal + ",getFrontDomainByMulti,异常!", e);
            return getDomainOld(terminal, merchantPO);
        }
    }

    /**
     * 之前的逻辑 暂时不动
     */
    private String getDomainOld(String terminal, MerchantPO merchantPO) {
        String domain;
        String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
        if (StringUtils.isNotEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = pcDomain;
        } else if (StringUtils.isNotEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = h5Domain;
        } else if (StringUtils.isEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = frontPcUrl;
        } else if (StringUtils.isEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = frontMobileUrl;
        } else if ("H5-1".equalsIgnoreCase(terminal)) {
            domain = front_bwh5_url;
        } else {
            //走兜底域名的商户及设备类型记录
            this.saveParameter(terminal, merchantPO);
            domain = frontinH5Url;
        }
        return domain;
    }

    /**
     * 记录走兜底域名商户信息
     * @param terminal
     * @param merchantPO
     */
    private void saveParameter(String terminal, MerchantPO merchantPO) {
        String key = "merchant:terminal";
        redisTemp.lPush(key, merchantPO.getMerchantCode());
        redisTemp.expire(key, 30, TimeUnit.DAYS);
    }

    private String getFrontDomainByMulti(String terminal, String merchantCode) {

        List<SystemConfig> domainSystemConfigList = frontDomainCache.getIfPresent(FRONT_END_DOMAIN_KEY);
        if (CollectionUtils.isEmpty(domainSystemConfigList)) {
            String systemConfigStr = redisTemp.get(FRONT_END_DOMAIN_KEY);

            if (StringUtils.isBlank(systemConfigStr)) {
                return "PC".equalsIgnoreCase(terminal) ? frontPcUrl : frontMobileUrl;
            }
            domainSystemConfigList = JSONArray.parseArray(systemConfigStr, SystemConfig.class);
            if (CollectionUtils.isEmpty(domainSystemConfigList)) {
                domainSystemConfigList = initDomainSystemConfigList();
            } else {
                frontDomainCache.put(FRONT_END_DOMAIN_KEY, domainSystemConfigList);
            }
        }
        if (CollectionUtils.isEmpty(domainSystemConfigList)) {
            return "PC".equalsIgnoreCase(terminal) ? frontPcUrl : frontMobileUrl;
        }
        for (SystemConfig systemConfig : domainSystemConfigList) {
            final String remark = systemConfig.getRemark();
            List<String> merchantCodeList = getMerchantCodeListBySystemConfig(remark);
            if (CollectionUtils.isNotEmpty(merchantCodeList) && merchantCodeList.contains(merchantCode)) {
                return "PC".equalsIgnoreCase(terminal) ? getPCBySystemConfig(remark)
                        : getH5BySystemConfig(remark);
            }
        }
        // 所有商户组都取不到 取默认商户组的
        List<SystemConfig> defaultConfigList = domainSystemConfigList.stream().filter(s -> s.getConfigKey().equals(FRONT_DOMAIN_KEY + "系统预设商户组") || s.getId().equals(10L)).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(defaultConfigList)) {
            log.error("前端域名组没有配置 默认商户组 ，请抓紧检查！！！！！！");
            defaultConfigList = domainSystemConfigList;
        }
        SystemConfig defaultSystemConfig = defaultConfigList.get(0);
        log.info("getFrontDomainByMulti:{}没有取到商户组，随机取得：{}:", merchantCode, defaultSystemConfig);
        final String remark = defaultSystemConfig.getRemark();
        return "PC".equalsIgnoreCase(terminal) ? getPCBySystemConfig(remark)
                : getH5BySystemConfig(remark);

    }

    public List<SystemConfig> initDomainSystemConfigList() {
        try {
            SystemConfig systemConfig = new SystemConfig();
            systemConfig.setConfigKey(FRONT_DOMAIN_KEY);
            systemConfig.setConfigValue("1");
            List<SystemConfig> domainSystemConfigList = systemConfigMapper.querySystemConfig(systemConfig);

            frontDomainCache.put(FRONT_END_DOMAIN_KEY, domainSystemConfigList);
            redisTemp.setWithExpireTime(FRONT_END_DOMAIN_KEY, JSON.toJSONString(domainSystemConfigList), 3600 * 24 * 60);
            log.info("initDomainSystemConfigList:" + domainSystemConfigList);
            return domainSystemConfigList;
        } catch (Exception e) {
            log.error("initDomainSystemConfigList,初始化前端域名异常!", e);
        }
        return null;
    }

    private String getH5BySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString("h5");
    }

    private String getPCBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return "";
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        return remarkJ.getString("pc");
    }

    private List<String> getMerchantCodeListBySystemConfig(String remark) {
        JSONObject remarkJ;
        if (StringUtils.isBlank(remark)) {
            return null;
        } else {
            remarkJ = JSON.parseObject(remark);
        }
        String merchantCodeStr = remarkJ.getString("merchantCodeList");
        return StringUtils.isBlank(merchantCodeStr) ?
                null : Arrays.stream(merchantCodeStr.split(",")).collect(Collectors.toList());
    }

}
