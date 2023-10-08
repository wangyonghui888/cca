package com.panda.sport.admin.feign;


import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.RcsUserSpecialBetLimitConfigVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "merchant-api", fallback = RemoteMerchantApiHystrix.class)
public interface MerchantApiClient {
    @GetMapping(value = "/api/merchant/kickoutMerchant")
    Object kickoutMerchant(@RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping(value = "/api/user/kickoutUserInternal")
    Object kickoutUserInternal(@RequestParam(value = "uid") Long uid, @RequestParam(value = "merchantCode") String merchantCode);

    @PostMapping(value = "/v1/transfer/retryTransfer")
    Object retryTransfer(@RequestBody UserRetryTransferVO vo, @RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping("/api/user/updateUserCache")
    void updateUserCache(@RequestParam(value = "uid") Long uid, @RequestParam(value = "merchantCode") String merchantCode);


    @GetMapping("/api/user/updateMerchantUserCache")
    void updateMerchantUserCache(@RequestParam(value = "merchantCode") String merchantCode);


    //更改用户限额
    @PostMapping(value = "/api/user/updateUserSpecialBettingLimit")
    Object updateUserSpecialBettingLimit(@RequestBody UserPO userInfo, @RequestParam(value = "merchantCode") String merchantCode);

    @PostMapping(value = "/api/user/updateUserSpecialBettingLimitLog")
    Object updateUserSpecialBettingLimitLog(
            @RequestBody List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList,
            @RequestParam(value = "merchantCode") String merchantCode,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "specialBettingLimit") Integer specialBettingLimit
    );

    /**
     * 增加扣款，加款操作
     */
    @PostMapping(value = "/AbstractAbnormalFileExportService/addChangeRecordHistory")
    void addChangeRecordHistory(@RequestBody AccountChangeHistoryFindVO accountChangeHistoryFindVO, @RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping("/api/user/kickoutMerchantUser")
    Object kickoutMerchantUser(@RequestParam(value = "merchantCode") String merchantCode);

}
