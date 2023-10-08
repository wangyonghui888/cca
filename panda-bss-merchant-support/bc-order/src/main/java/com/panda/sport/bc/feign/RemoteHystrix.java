package com.panda.sport.bc.feign;


import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.common.vo.merchant.MatchBetInfoVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RemoteHystrix implements MerchantReportClient {

    @Override
    public Response queryBCMatchStatisticList(SportVO vo) {
        return Response.returnFail("请求超时了");
    }


    @Override
    public Response getBCMatchStatistics(SportVO vo) {
        return Response.returnFail("请求超时了");
    }


}

