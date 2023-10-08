package com.panda.sport.admin.feign;


import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.RcsUserSpecialBetLimitConfigVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Component
public class RemoteMerchantApiHystrix implements MerchantApiClient {

    @Override
    public Object kickoutMerchant(String merchantCode) {
        log.error("踢出商户失败,RPC接口异常");
        return Response.returnFail("请求超时了");
    }

    @Override
    public Object kickoutUserInternal(Long uid, String merchantCode) {
        log.error(uid + "kickoutUserInternal踢用户失败,RPC接口异常" + merchantCode);
        return null;
    }

    @Override
    public Object retryTransfer(@RequestBody UserRetryTransferVO vo, @RequestParam(value = "merchantCode") String merchantCode) {
        log.error("retryTransfer 调用异常");
        return null;
    }

    @Override
    public void updateUserCache(Long uid, String merchantCode) {
        log.error("updateUserCache 调用异常");
    }

    @Override
    public void updateMerchantUserCache(String merchantCode) {
        log.error("updateMerchantUserCache 调用异常");
    }

    @Override
    public Object updateUserSpecialBettingLimit(UserPO userInfo, String merchantCode) {
        log.error("updateUserSpecialBettingLimit 调用异常");
        return null;
    }

    @Override
    public Object updateUserSpecialBettingLimitLog(
            List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList,
            String merchantCode,
            Long userId,
            Integer specialBettingLimit
    ) {
        log.error("updateUserSpecialBettingLimitLog 调用异常");
        return null;
    }

    @Override
    public void addChangeRecordHistory(AccountChangeHistoryFindVO accountChangeHistoryFindVO, String merchantCode) {
        log.error("addChangeRecordHistory 调用异常");
    }

    @Override
    public Object kickoutMerchantUser(String merchantCode) {
        log.error("kickoutMerchantUser踢商户用户失败,RPC接口异常");
        return null;
    }
}

