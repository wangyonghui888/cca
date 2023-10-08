package com.panda.sport.admin.feign;

import com.panda.sport.merchant.common.dto.*;
import com.panda.sport.merchant.common.vo.RiskMerchantImportUpdateVo;
import com.panda.sport.merchant.common.vo.UserRiskControlQueryAllReqVO;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(value = "panda-rcs-trade", fallbackFactory = RcsTradeRemoteHystrix.class)
public interface PandaRcsTradeClient {


    @PostMapping(value = "/api/trade/updateUserSpecialLimit")
    Response<Boolean> updateUserSpecialLimit(@RequestBody  Request<UserSpecialLimitDto> var1);

    @PostMapping(value = "/api/trade/queryUserExceptionByOnline")
    Map<String, Object>  queryAbnormalUserList(@RequestBody AbnormalUserVo request);


    @PostMapping(value = "/api/trade/getUserTradeRestrict")
    Response<RcsTradeRestrictMerchantSettingDto> getUserTradeRestrict(@RequestBody  Request<UserTradeRestrictDto> var1);

    @PostMapping(value = "/riskMerchantManager/pageList")
    Map queryPageUserRiskControlList(@RequestBody UserRiskControlPageQueryDTO userRiskControlPageQueryDTO);

    @PostMapping(value = "/riskMerchantManager/updateStatus")
    Map updateStatus(@RequestBody UserRiskControlStatusEditDTO userRiskControlStatusEditDTO);

    @PostMapping(value = "/riskMerchantManager/exportOutPutList")
    Map exportUserRiskControlList(@RequestBody UserRiskControlQueryAllReqVO userRiskControlQueryAllReqVO);

    @PostMapping(value = "/riskMerchantManager/batchUpdateStatus")
    Map importUserRiskControlList(@RequestBody List<RiskMerchantImportUpdateVo> riskMerchantImportUpdateVoList);

    @PostMapping(value = "/riskMerchantManager/pendingCount")
    Map pendingCount(@RequestBody UserRiskControlPendingCountQueryDTO queryDTO);
}
