package com.panda.sport.admin.feign;

import com.panda.sport.merchant.common.vo.merchant.AbnormalVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fantasy
 * @date 2022/8/14
 * @TIME 18:47
 */
@Slf4j
@Component
public class AbnormalOutApiHystrix implements AbnormalOutApiClient {
    @Override
    public Map<String, Object> queryAbnormalList(AbnormalVo request){
        Map<String, Object> map = new HashMap<>();
        log.error ("AbnormalApiClient error","请求超时了");
        map.put("AbnormalApiClient.queryAbnormalList","请求超时了");
        return map;
    }
}
