package com.panda.sport.merchant.manage.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.backup.mapper.SportMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.UserCheckLogPO;
import com.panda.sport.merchant.common.vo.HotPlayNameVO;
import com.panda.sport.merchant.common.vo.MerchantTree;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service("LocalCacheService")
public class LocalCacheService {

    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private SportMapper sportMapper;
    public static final String TREE_LIST = "treeList";
    public static final String SPORT_LIST = "sportList";
    public static final String SPORT_MAP = "sportMap";
    public static final String PLAY_LIST = "playList";
    public static final String PLAY_MAP = "playMap";
    public static final String DOMAIN = "domain";
    public static final String DOMAIN_DNS = "domain_dns_pool";
    private static final List<String> MessList =
            Lists.newArrayList("!quarternr", "!framenr", "!xth", "pointnr",
                    "!gamenr", "gamenr", "!gamenrY", "!setnr", "goalnr", "cornernr", "!penaltynr",
                    "total", "!n", "!scorenr", "!bookingnr", "!periodnr", "setnr", "!gamenrX", "!gamenrY");
    private static final String X = "X";

    private static final String Y = "Y";
    public static Cache<String, Object> cacheMap = null;
    public static Cache<String, Object> merchantMap = null;
    public static Cache<String, Object> merchantCodeMap = null;
    public static Cache<String, Object> betMap = null;
    public static Cache<String, Integer> ticketSearchMap = null;
    public static Cache<Integer, Object> tagMap = null;
    public static Cache<String, Object> statisticCache = null;
    public static Cache<String, Object> domainMap = null;
    public static Cache<String, Object> domainCountMap = null;
    public static Cache<String, List<UserVO>> userPOListMap = null;

    public static Cache<String, Object> checkUserMap = null;

    public static Cache<String, Object> dnsIpPoolMap = null;

    @PostConstruct
    private void init() {
        ticketSearchMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
        betMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
        statisticCache = Caffeine.newBuilder()
                .maximumSize(300) // 设置缓存的最大容量
                .expireAfterWrite(120, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
        cacheMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();
        merchantMap = Caffeine.newBuilder()
                .maximumSize(500) // 设置缓存的最大容量
                .expireAfterWrite(60, TimeUnit.DAYS)
                .recordStats() // 开启缓存统计
                .build();
        merchantCodeMap = Caffeine.newBuilder()
                .maximumSize(500) // 设置缓存的最大容量
                .expireAfterWrite(60, TimeUnit.DAYS)
                .recordStats() // 开启缓存统计
                .build();
        tagMap = Caffeine.newBuilder()
                .maximumSize(2) // 设置缓存的最大容量
                .expireAfterWrite(360, TimeUnit.DAYS)
                .recordStats() // 开启缓存统计
                .build();

        domainCountMap = Caffeine.newBuilder()
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        domainMap = Caffeine.newBuilder()
                .maximumSize(100) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        dnsIpPoolMap = Caffeine.newBuilder()
                .maximumSize(200) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        userPOListMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        checkUserMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        List<MerchantTree> treeList = merchantMapper.getParentMerchant();

        cacheMap.put(TREE_LIST, treeList);
        setTreeCache(treeList);
        setMerchantIdCache();
        log.info("init本地缓存放入商户树成功!" + treeList);
        setTransferModeMerchant();
    }

    private void setMerchantIdCache() {
        List<Map<String, Object>> merchantList = merchantMapper.getMerchantIdList();
        for (Map<String, Object> tempMap : merchantList) {
            Long merchantId = Long.valueOf((String) tempMap.get("id"));
            String code = (String) tempMap.get("merchantCode");
            merchantMap.put(code, merchantId);
        }
        log.info("商户缓存初始化成功" + merchantList.size());
    }

    private void setTransferModeMerchant() {
        List<String> creditMerchantList = merchantMapper.getCreditMerchant();
        tagMap.put(1, creditMerchantList);
        log.info("本地缓存放入信用网成功!" + (CollectionUtils.isEmpty(creditMerchantList) ? 0 : creditMerchantList.size()));
        List<String> cashMerchantList = merchantMapper.getCashMerchant();
        tagMap.put(0, cashMerchantList);
        log.info("本地缓存放入现金网成功!" + (CollectionUtils.isEmpty(cashMerchantList) ? 0 : cashMerchantList.size()));
    }

    private void setTreeCache(List<MerchantTree> treeList) {
        for (MerchantTree merchant : treeList) {
            int level = merchant.getAgentLevel();
            if (level != 0 && level != 2) {
                cacheMap.put(merchant.getId(), merchant.getTrees());
                setTreeCache(merchant.getTrees());
            }
        }
    }


    public List<MerchantTree> getTreeList(String merchantId) {
        log.info("getTreeList开始组装缓存:" + merchantId);
        List<MerchantTree> treeList;
        if (StringUtils.isEmpty(merchantId) || TREE_LIST.equalsIgnoreCase(merchantId)) {
            treeList = merchantMapper.getParentMerchant();
        } else {
            treeList = merchantMapper.getMerchantListTree(merchantId);
        }
        for (MerchantTree merchant : treeList) {
            int level = merchant.getAgentLevel();
            if (level != 0 && level != 2) {
                cacheMap.put((StringUtils.isEmpty(merchantId) ? TREE_LIST : merchant.getId()), merchant.getTrees());
                setTreeCache(merchant.getTrees());
            }
        }
        return treeList;
    }

    public Response getLocalCacheInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("tagMap", tagMap);
        result.put("cacheMap", cacheMap);
        result.put("betMap", betMap);
        result.put("statisticCache", statisticCache);
        result.put("merchantMap", merchantMap);
        return Response.returnSuccess(result);
    }

    public Response invalidateCache(String key) {
        cacheMap.invalidate(key);
        return Response.returnSuccess();
    }

    public Response invalidateAll() {
        cacheMap.invalidateAll();
        return Response.returnSuccess();
    }


    public List<String> getMerchantCodeList(String merchantId, Integer agentLevel) {
        List<String> merchantCodeList = (List<String>) LocalCacheService.cacheMap.getIfPresent(merchantId + "_" + agentLevel + "_children");
        log.info("缓存获取下级商户编码:" + merchantId + "_" + agentLevel + "_children, " + merchantCodeList);
        if (CollectionUtils.isEmpty(merchantCodeList)) {
            merchantCodeList = merchantMapper.queryChildList(merchantId);
            if (agentLevel == 10) {
                List<String> childrenCodeList = new ArrayList<>();
                for (String str : merchantCodeList) {
                    List<String> childrenList = merchantMapper.queryChildren(str);
                    if (CollectionUtils.isNotEmpty(childrenList)) {
                        childrenCodeList.addAll(childrenList);
                    }
                }
                merchantCodeList.addAll(childrenCodeList);
            }
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                LocalCacheService.cacheMap.put(merchantId + "_" + agentLevel + "_children", merchantCodeList);
                log.info("缓存放入子商户编码:" + merchantId + "_" + agentLevel + "_children" + "," + merchantCodeList);
            }
        }
        return merchantCodeList;
    }

    /**
     * 此缓存只能获得一层下级
     *
     * @param merchantId 商户id
     * @return 返回下级code集合
     */

    public List<String> getMerchantCodeList(String merchantId) {
        String key = merchantId + "_merchant_children";
        List<String> merchantCodeList = (List<String>) LocalCacheService.cacheMap.getIfPresent(key);
        log.info("缓存key{};缓存内容{}", key, merchantCodeList);
        if (CollectionUtils.isEmpty(merchantCodeList)) {
            merchantCodeList = merchantMapper.queryChildList(merchantId);
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                LocalCacheService.cacheMap.put(key, merchantCodeList);
                log.info("缓存放入子商户key:{},内容:{}", key, merchantCodeList);
            }
        }
        return merchantCodeList;
    }


    public List<Map<String, Object>> getSportList(String language) {
        language = StringUtils.isEmpty(language) ? Constant.LANGUAGE_CHINESE_SIMPLIFIED : language;
        List<Map<String, Object>> result = (List<Map<String, Object>>) LocalCacheService.cacheMap.getIfPresent(LocalCacheService.SPORT_LIST + language);
        if (CollectionUtils.isEmpty(result)) {
            result = sportMapper.getAllSportList(language);
            LocalCacheService.cacheMap.put(LocalCacheService.SPORT_LIST + language, result);
            log.info("缓存放入sportList列表:" + result);
            if (CollectionUtils.isNotEmpty(result)) {
                Map<Integer, String> map = new HashMap<>();
                for (Map<String, Object> tempMap : result) {
                    Long idL = (Long) tempMap.get("id");
                    Integer id = idL == null ? 0 : idL.intValue();
                    String name = tempMap.get("name") == null ? "" : (String) tempMap.get("name");
                    map.put(id, name);
                }
                LocalCacheService.cacheMap.put(LocalCacheService.SPORT_MAP + language, map);
                log.info("缓存放入sportMap列表1:" + result);
            }
        }
        return result;
    }


    public Map<Integer, String> getSportMap(String language) {
        language = StringUtils.isEmpty(language) ? Constant.LANGUAGE_CHINESE_SIMPLIFIED : language;
        Map<Integer, String> resultMap = (Map<Integer, String>) LocalCacheService.cacheMap.getIfPresent(LocalCacheService.SPORT_MAP + language);
        if (null == resultMap) {
            List<Map<String, Object>> result = sportMapper.getAllSportList(language);
            if (CollectionUtils.isNotEmpty(result)) {
                resultMap = new HashMap<>();
                for (Map<String, Object> tempMap : result) {
                    Integer id = tempMap.get("id") == null ? 0 : ((Long) tempMap.get("id")).intValue();
                    String name = tempMap.get("name") == null ? "" : (String) tempMap.get("name");
                    resultMap.put(id, name);
                }
                LocalCacheService.cacheMap.put(LocalCacheService.SPORT_MAP + language, resultMap);
                log.info("缓存放入sportMap列表2:" + resultMap);

            }
        }
        return resultMap;
    }


    public List<HotPlayNameVO> getPlayNameList(Integer sportId, String language) {
        if(null==sportId){
            return null;
        }
        List<HotPlayNameVO> list = (List<HotPlayNameVO>) LocalCacheService.cacheMap.getIfPresent(LocalCacheService.PLAY_LIST + sportId + language);
        if (CollectionUtils.isEmpty(list)) {
            list = sportMapper.queryHotPlayName(sportId, language);
            if (list != null) {
                list.forEach(e -> {
                    String playName = e.getPlayName();
                    if (StringUtils.isNotEmpty(playName) && playName.contains("{")) {
                        if (e.getPlayName().contains(Constant.TITLE_SHOW_NAME_COMPETITOR1)) {
                            playName = playName.replace(Constant.TITLE_SHOW_NAME_COMPETITOR1, language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "主队" : "Home");
                        }
                        if (e.getPlayName().contains(Constant.TITLE_SHOW_NAME_COMPETITOR2)) {
                            playName = playName.replace(Constant.TITLE_SHOW_NAME_COMPETITOR2, language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "客队" : "Away");
                        }
                        playName = playName.replaceAll("\\{", "");
                        playName = playName.replaceAll("}", "");
                        for (String str : MessList) {
                            if (playName.contains(str)) {
                                playName = playName.replaceAll(str, "n");
                            }
                        }
                        if (playName.contains(X) || playName.contains(Y)) {
                            playName = playName.replace(X, "");
                            playName = playName.replace(Y, "");
                        }
                        e.setPlayName(playName);
                    }
                });
                LocalCacheService.cacheMap.put(LocalCacheService.PLAY_LIST + sportId + language, list);
            }
        }
        return list;
    }


    public Map<String, String> getPlayNameMap(String language) {
        Map<String, String> map = (Map<String, String>) LocalCacheService.cacheMap.getIfPresent(LocalCacheService.PLAY_LIST + language);
        if (map == null) {
            List<HotPlayNameVO> list = sportMapper.queryHotPlayName(null, language);
            if (list != null) {
                list.forEach(e -> {
                    String playName = e.getPlayName();
                    if (StringUtils.isNotEmpty(playName) && playName.contains("{")) {
                        if (e.getPlayName().contains(Constant.TITLE_SHOW_NAME_COMPETITOR1)) {
                            playName = playName.replace(Constant.TITLE_SHOW_NAME_COMPETITOR1, language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "主队" : "Home");
                        }
                        if (e.getPlayName().contains(Constant.TITLE_SHOW_NAME_COMPETITOR2)) {
                            playName = playName.replace(Constant.TITLE_SHOW_NAME_COMPETITOR2, language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "客队" : "Away");
                        }
                        playName = playName.replaceAll("\\{", "");
                        playName = playName.replaceAll("}", "");
                        for (String str : MessList) {
                            if (playName.contains(str)) {
                                playName = playName.replaceAll(str, "n");
                            }
                        }
                        if (playName.contains(X) || playName.contains(Y)) {
                            playName = playName.replace(X, "");
                            playName = playName.replace(Y, "");
                        }
                        e.setPlayName(playName);
                    }
                    if (StringUtils.isEmpty(e.getPlayName())) {
                        e.setPlayName("玩法未配置");
                    }
                });
                map = list.stream().collect(Collectors.toMap(HotPlayNameVO::getPlayId, HotPlayNameVO::getPlayName,
                        (value1, value2) -> {
                            return value1;
                        }));
                LocalCacheService.cacheMap.put(LocalCacheService.PLAY_MAP + language, map);
            }
        }
        return map;
    }

    public List<Long> getMerchantIdList(List<String> codeList) {
        if (CollectionUtils.isNotEmpty(codeList)) {
            List<Long> merchantIdList = new ArrayList<>();
            for (String code : codeList) {
                Long id = (Long) merchantMap.getIfPresent(code);
                if (id != null) {
                    merchantIdList.add(id);
                } else {
                    Long idStr = merchantMapper.getMerchantId(code);
                    if (idStr != null) {
                        merchantIdList.add(idStr);
                        merchantMap.put(code, idStr);
                    }
                }
            }
            log.info("codeList:" + merchantIdList);
            return merchantIdList;
        } else {
            return null;
        }
    }

    public UserCheckLogPO getCheckUserMap(String code) {
        UserCheckLogPO userCheckLogPO = (UserCheckLogPO) checkUserMap.getIfPresent(code);
        log.info("getMerchantId.code:" + code);
        return userCheckLogPO;
    }

    public Long getMerchantId(String code) {
        Long id = (Long) merchantMap.getIfPresent(code);
        if (id == null) {
            id = merchantMapper.getMerchantId(code);
            if (null != id) {
                merchantMap.put(code, id);
            }
        }
        log.info("getMerchantId.code:" + id);
        return id;
    }
    public String getMerchantCode(String code) {
        String name = (String) merchantCodeMap.getIfPresent(code);
        if (name == null) {
            name = merchantMapper.getMerchantName(code);
            if (null != name) {
                merchantCodeMap.put(code, name);
            }
        }
        log.info("getMerchantId.code:" + name);
        return name;
    }

    public Map<String, Integer>  queryMerchantTransferModeMap(String key) {
        Map<String, Integer> resultMap = (Map) cacheMap.getIfPresent(key);
        if (ObjectUtil.isEmpty(resultMap)) {
            try {
                resultMap = new HashMap<>(CommonDefaultValue.SIXTEEN);
                List<MerchantPO> merchantPOList = merchantMapper.queryMerchantTransferModeList();
                for (MerchantPO merchantPO : merchantPOList) {
                    resultMap.put(merchantPO.getMerchantCode(),merchantPO.getTransferMode());
                }
            } catch (Exception e) {
                log.error("queryMerchantTransferModeMap 异常", e);
            }
            if (ObjectUtil.isNotEmpty(resultMap)) {
                cacheMap.put(key, resultMap);
                log.info("queryMerchantTransferModeMap缓存放入key1:" + key + ",data:" + resultMap);
            }
        }
        log.info("queryMerchantTransferModeMap缓存获取数据key1:" + key);
        return resultMap;
    }
}
