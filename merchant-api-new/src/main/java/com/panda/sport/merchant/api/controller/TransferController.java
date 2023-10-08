package com.panda.sport.merchant.api.controller;

import com.panda.sport.merchant.api.service.RetryTransferService;
import com.panda.sport.merchant.api.util.DistributedLockerUtil;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/api/transfer")
@Slf4j
@ResponseBody
public class TransferController {
    @Autowired
    private RetryTransferService retryTransferService;
    @Autowired
    private DistributedLockerUtil distributedLockerUtil;

    @PostMapping(value = "/retryTransfer")
    public APIResponse retryTransfer(@RequestBody UserRetryTransferVO vo, @RequestParam(value = "merchantCode") String merchantCode) {
        log.info(merchantCode + "retryTransfer交易记录重试1 等待锁 begin:" + vo);
        if (vo == null) {
            log.info("retryTransfer交易记录重试1 参数有误");
            return APIResponse.returnFail("retryTransfer参数有误");
        }
        //  如果 是 商户调用 不可为空
        if (CollectionUtils.isEmpty(vo.getTransferIdList())) {
            log.info("retryTransfer商户调用 transferId 为空" + vo.getTransferIdList());
            return APIResponse.returnFail("retryTransfer交易Id不能为空");
        }
        List<String> transferList = vo.getTransferIdList();
        log.info("retryTransfer交易记录重试2 获取锁 begin:" + vo.getTransferIdList() + " userName:" + vo.getUserName() + " retryCount:" + vo.getRetryCount());
        for (String transferId : transferList) {
            distributedLockerUtil.lock(RedisConstants.RETRY_FAMILY_KEY + transferId);
            try {
                retryTransferService.retryTransferRecord(transferId, vo.getRetryCount(), vo.getUserName());
            } catch (Exception e) {
                log.error(merchantCode + "retryTransfer:" + transferId, e);
            } finally {
                distributedLockerUtil.unLock(RedisConstants.RETRY_FAMILY_KEY + transferId);
            }
        }
        return APIResponse.returnSuccess();
    }

    @PostMapping(value = "/addChangeRecordHistory")
    public APIResponse addChangeRecordHistory(HttpServletRequest request, @RequestBody AccountChangeHistoryFindVO accountChangeHistoryFindVO, @RequestParam(value = "merchantCode") String merchantCode) {
        log.info(merchantCode + "addChangeRecordHistory添加帐变记录：" + accountChangeHistoryFindVO);
        retryTransferService.addChangeRecordHistory(accountChangeHistoryFindVO, request);
        return APIResponse.returnSuccess();
    }
}
