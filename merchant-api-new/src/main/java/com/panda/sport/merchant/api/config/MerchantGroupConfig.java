package com.panda.sport.merchant.api.config;

import com.panda.sport.bss.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MerchantGroupConfig {
    @Autowired
    public MerchantMapper merchantMapper;

    public final static Map<String, String> groupMap = new HashMap<>();

    @PostConstruct
    private void init() {

        List<Map<String, String>> merchantList = merchantMapper.getGroupMerchantList();
        if (CollectionUtils.isNotEmpty(merchantList)) {
            for (Map<String, String> map : merchantList) {
                String group = map.get("groupCode");
                if (StringUtils.isNotEmpty(group)) {
                    groupMap.put(map.get("merchantCode"), group.toLowerCase());
                }
            }
            log.info("初始化商户分组缓存结束:" + groupMap);
        }
    }
}
