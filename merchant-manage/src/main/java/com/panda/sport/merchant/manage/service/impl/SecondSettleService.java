package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.merchant.common.vo.CheckToolsQueryReqVO;
import com.panda.sport.merchant.manage.feign.MerchantReportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.sport.merchant.manage.service.impl
 * @Description :  TODO
 * @Date: 2022-05-11 14:49:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service
@RefreshScope
public class SecondSettleService {

    @Value("${second.settle.switch:0}")
    private String secondSettleSwitch;

    @Autowired
    private MerchantReportClient merchantReportClient;


    public void executeFinanceDay(String date){
        if ("1".equals(secondSettleSwitch)) {
            CheckToolsQueryReqVO queryReqVO = new CheckToolsQueryReqVO();
            queryReqVO.setStartDate(date);
            merchantReportClient.executeFinanceDay(queryReqVO);
        }
    }
}
