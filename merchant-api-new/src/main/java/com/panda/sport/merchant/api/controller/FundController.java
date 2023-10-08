package com.panda.sport.merchant.api.controller;

import com.panda.sport.merchant.api.service.TransferService;
import com.panda.sport.merchant.api.util.DistributedLockerUtil;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/fund")
@Slf4j
public class FundController {

    @Autowired
    private DistributedLockerUtil distributedLockerUtil;

    @Autowired
    private TransferService transferRecordService;


    /**
     * @description:查询交易记录列表
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping("/queryTransferList")
    public APIResponse queryTransferList(HttpServletRequest request, @RequestParam(value = "userName") String username,
                                         @RequestParam(value = "merchantCode") String merchantCode,
                                         @RequestParam(value = "startTime") String startTime,
                                         @RequestParam(value = "endTime") String endTime,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                         @RequestParam(value = "timestamp") Long timestamp,
                                         @RequestParam(value = "signature") String signature) {
        log.info("api/fund/queryTransferList.username:" + username + ",merchantCode:" + merchantCode + ",timestamp:" + timestamp + ",startTime=" + startTime + ",endTime=" + endTime);
        return StringUtils.isAnyEmpty(signature, username, merchantCode) || timestamp == null || startTime == null || endTime == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                transferRecordService.queryTransferList(request, merchantCode, username, startTime, endTime, pageSize, pageNum, timestamp, signature);
    }

    /**
     * @description:查询交易记录
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping("/getTransferRecord")
    public APIResponse getTransferRecord(HttpServletRequest request, @RequestParam(value = "userName", required = false) String username,
                                         @RequestParam(value = "merchantCode") String merchantCode,
                                         @RequestParam(value = "transferId") String transferId,
                                         @RequestParam(value = "timestamp") Long timestamp,
                                         @RequestParam(value = "signature") String signature) {
        log.info("api/fund/getTransferRecord.username:" + username + ",merchantCode:" + transferId + ",transferId:" + merchantCode + ",timestamp" + timestamp);
        return StringUtils.isAnyEmpty(merchantCode, signature, transferId) || timestamp == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                transferRecordService.getTransferRecord(request, username, merchantCode, transferId, timestamp, signature);
    }

    /**
     * @description:查询玩家余额
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping("/checkBalance")
    @Trace
    public APIResponse checkBalance(HttpServletRequest request, @RequestParam(value = "userName") String username,
                                    @RequestParam(value = "merchantCode") String merchantCode,
                                    @RequestParam(value = "timestamp") Long timestamp,
                                    @RequestParam(value = "signature") String signature) {
        log.info("api/fund/checkBalance.username:" + username + ",merchantCode:" + merchantCode + ",timestamp" + timestamp);
        return StringUtils.isAnyEmpty(merchantCode, signature, username) || timestamp == null ? APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                transferRecordService.checkBalance(request, username, merchantCode, timestamp, signature);
    }

    /**
     * @description:上下分
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping("/transfer")
    public APIResponse transfer(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                @RequestParam(value = "merchantCode") String merchantCode,
                                @RequestParam(value = "transferType") String transferType,
                                @RequestParam(value = "amount") String amount,
                                @RequestParam(value = "transferId") String transferId,
                                @RequestParam(value = "currency", required = false) String currency,
                                @RequestParam(value = "timestamp") Long timestamp,
                                @RequestParam(value = "signature") String signature) {
        log.info("api/fund/transfer  username:" + userName + ",merchantCode:" + merchantCode + ",currency=" + currency +
                ",transferType:" + transferType + ",amount:" + amount + ",transferId:" + transferId + ",timestamp:" + timestamp);
        long startL = System.currentTimeMillis();
        if (StringUtils.isAnyEmpty(merchantCode, signature, userName, transferId, amount) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            distributedLockerUtil.lock(RedisConstants.PAY_FAMILY_KEY + merchantCode + userName);
            return transferRecordService.transfer(request, merchantCode, userName, transferType, amount, transferId, timestamp, signature, currency);
        } catch (Exception e) {
            log.error("FundController.transfer,exception:" + transferId, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        } finally {
            log.info("api/fund/transfer:" + transferId + (System.currentTimeMillis() - startL));
            distributedLockerUtil.unLock(RedisConstants.PAY_FAMILY_KEY + merchantCode + userName);
        }
    }


    /**
     * @description:上下分
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping("/v1/transfer")
    public APIResponse transferV1(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                  @RequestParam(value = "merchantCode") String merchantCode,
                                  @RequestParam(value = "transferType") String transferType,
                                  @RequestParam(value = "amountType", required = false) String amountType,
                                  @RequestParam(value = "amount") String amount,
                                  @RequestParam(value = "transferId") String transferId,
                                  @RequestParam(value = "timestamp") Long timestamp,
                                  @RequestParam(value = "signature") String signature) {
        log.info("api/fund/transferV1  username:" + userName + ",merchantCode:" + merchantCode +
                ",transferType:" + transferType + ",amount:" + amount + ",transferId:" + transferId + ",timestamp:" + timestamp);
        long startL = System.currentTimeMillis();
        if (StringUtils.isAnyEmpty(merchantCode, signature, userName, transferId, amount) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            distributedLockerUtil.lock(RedisConstants.PAY_FAMILY_KEY + merchantCode + userName);
            return transferRecordService.transferV1(request, merchantCode, userName, transferType, amount, transferId, timestamp, amountType, signature);
        } catch (Exception e) {
            log.error("FundController.transferV1,exception:" + transferId, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        } finally {
            log.info("api/fund/transferV1:" + transferId + (System.currentTimeMillis() - startL));
            distributedLockerUtil.unLock(RedisConstants.PAY_FAMILY_KEY + merchantCode + userName);
        }
    }
}
