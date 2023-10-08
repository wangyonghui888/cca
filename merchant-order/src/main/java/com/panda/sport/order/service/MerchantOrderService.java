package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;

import java.util.List;
import java.util.Map;

public interface MerchantOrderService {

    Response<Object> userOrderMonth(String merchantCode);

    Response<?> queryMerchantReport(MerchantOrderVO pageVO);

    List<String> queryMerchantByGroupCode(String code);

    Response<?> queryMechantAgentList(String merchantCode);

    Response<?> listGroupByMerchant(MerchantOrderVO vo);

    Map reportDownload(String username, MerchantOrderVO requestVO);



    Response queryMerchantTop10(MerchantOrderVO requestVO);

    Response<Object> updateKey(String code, String key, String startTime, String merchantCode, Integer userId, String language,String ip) throws Exception;

    void exportMerchantKey(List<String> codeList);
}
