package com.panda.sport.order.feign;

import com.panda.sport.merchant.common.dto.RcsTradeRestrictMerchantSettingDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.dto.Response;
import com.panda.sport.merchant.common.dto.UserTradeRestrictDto;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fantasy
 * @date 2022/7/16
 * @TIME 18:47
 */
@Slf4j
@Component
public class AbnormalUserHystrix implements AbnormalUserApiClient {
    @Override
    public Map<String, Object> queryAbnormalUserList(AbnormalUserVo request){
        Map<String, Object> map = new HashMap<>();
        log.error ("AbnormalUserApiClient error","请求超时了");
        map.put("AbnormalUserApiClient.queryAbnormalUserList","请求超时了");
        return map;
    }
}
