package com.panda.sport.order.service;


import com.panda.sport.merchant.common.vo.UserOrderV2VO;

import java.util.List;
import java.util.Map;

public interface OrderV2Service {
    Map<String, Object> queryTicketList(UserOrderV2VO vo) throws Exception;

    List<Map<String, Object>> queryUserIdList(String merchantCode);
}
