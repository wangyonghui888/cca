package com.oubao.controller;

import com.oubao.mq.OubaoDistributedLockerUtil;
import com.oubao.po.NotifyPO;
import com.oubao.po.TAccountChangeHistoryPO;
import com.oubao.po.TransferPO;
import com.oubao.service.UserService;
import com.oubao.vo.APIResponse;
import com.oubao.vo.ApiResponseEnum;
import com.oubao.vo.PageVO;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private UserService userService;
    @Autowired
    private OubaoDistributedLockerUtil oubaoDistributedLockerUtil;

    @GetMapping("/queryOrderList")
    public APIResponse queryOrderList(@RequestParam(value = "userName", required = false) String userName,
                                      @RequestParam(value = "startTime", required = false) String startTime,
                                      @RequestParam(value = "endTime", required = false) String endTime,
                                      @RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "size", required = false) Integer size,
                                      @RequestParam(value = "merchantCode", required = false) String merchantCode
    ) {
        try {
            return APIResponse.returnSuccess(userService.queryOrderList(userName, startTime, endTime, page, size, merchantCode));
        } catch (Exception e) {
            log.error("AccountController.queryOrderList,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/queryAccountChangeList")
    public APIResponse<PageVO<TAccountChangeHistoryPO>> queryAccountChangeList(@RequestParam(value = "userName", required = false) String userName,
                                                                               @RequestParam(value = "startTime", required = false) String startTime,
                                                                               @RequestParam(value = "endTime", required = false) String endTime,
                                                                               @RequestParam(value = "page", required = false) Integer page,
                                                                               @RequestParam(value = "size", required = false) Integer size,
                                                                               @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        try {
            return APIResponse.returnSuccess(userService.queryAccountChangeList(userName, merchantCode, startTime, endTime, page, size));
        } catch (Exception e) {
            log.error("AccountController.queryUserList,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/changeCredit")
    public APIResponse changeCredit(@RequestParam("userName") String userName, @RequestParam("type") Integer type,
                                    @RequestParam("credit") Double credit,
                                    @RequestParam(value = "merchantCode", required = false) String merchantCode
    ) {
        try {
            if (type == null || credit == null) {
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            if (StringUtils.isEmpty(userName)) {
                return APIResponse.returnFail(ApiResponseEnum.USER_NAME_NOT_NULL);
            }
            return userService.changeCredit(userName, type, credit, merchantCode) ?
                    APIResponse.returnSuccess(ApiResponseEnum.SUCCESS) :
                    APIResponse.returnFail(ApiResponseEnum.ACCOUNT_BALANCE_LACK);
        } catch (Exception e) {
            log.error("AccountController.changeCredit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 假如欧宝为转账钱包,尝试到panda上下分
     *
     * @param request
     * @param username
     * @return
     */
    @GetMapping("/transferPandaCredit")
    public APIResponse transferPandaCredit(HttpServletRequest request, @RequestParam(value = "userName") String username
            , @RequestParam(value = "transferType") Integer transferType, @RequestParam(value = "amount") BigDecimal amount,
                                           @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("username:" + username + " transferType:" + transferType);
        try {
            return userService.transferPandaCredit(username, transferType, amount, merchantCode);
        } catch (Exception e) {
            log.error("transferPandaCredit 异常!", e);
            if (e.getMessage().contains("没有此玩家")) {
                return APIResponse.returnFail("panda 没有此玩家!");
            }
            if (e.getMessage().contains("玩家")) {
                return APIResponse.returnFail("panda 玩家余额不足!");
            }
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/checkBalance")
    public APIResponse checkBalance(@RequestParam("userName") String userName,
                                    @RequestParam(value = "merchantCode", required = false) String merchantCode
    ) {
        try {
            if (StringUtils.isEmpty(userName)) {
                return APIResponse.returnFail(ApiResponseEnum.USER_NAME_NOT_NULL);
            }
            return APIResponse.returnSuccess(userService.checkBalance(userName, merchantCode));
        } catch (Exception e) {
            log.error("AccountController.changeCredit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/changeOubaoCredit")
    public APIResponse changeOubaoCredit(HttpServletRequest request, @RequestParam(value = "userName") String username,
                                         @RequestParam(value = "merchantCode") String merchantCode,
                                         @RequestParam(value = "bizType") Integer bizType,
                                         @RequestParam(value = "transferId") String transferId,
                                         @RequestParam(value = "amount") Double amount,
                                         @RequestParam(value = "transferType") Integer transferType,
                                         @RequestParam(value = "orderStr") String orderList,
                                         @RequestParam(value = "timestamp") Long timestamp,
                                         @RequestParam(value = "signature") String signature) {
        try {
            TransferPO transferPO = new TransferPO();
            transferPO.setUserName(username);
            transferPO.setAmount(amount);
            transferPO.setMerchantCode(merchantCode);
            transferPO.setBizType(bizType);
            transferPO.setTransferId(transferId);
            transferPO.setTransferType(transferType);
            transferPO.setTimestamp(timestamp);
            transferPO.setSignature(signature);
            transferPO.setOrderList(orderList);
            log.info("加扣款接口参数:" + transferPO);
            oubaoDistributedLockerUtil.lock("oubao-emulate:" + merchantCode + username);
            return userService.changeOubaoCredit(transferPO) ? APIResponse.returnSuccess(ApiResponseEnum.BlandSUCCESS) :
                    APIResponse.returnFail(ApiResponseEnum.ACCOUNT_BALANCE_LACK);
        } catch (Exception e) {
            log.error("AccountController.changeCredit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        } finally {
            oubaoDistributedLockerUtil.unLock("oubao-emulate:" + merchantCode + username);
        }
    }

    @PostMapping("/checkOubaoCredit")
    public APIResponse checkOubaoCredit(HttpServletRequest request, @RequestParam(value = "userName") String username,
                                        @RequestParam(value = "merchantCode") String merchantCode,
                                        @RequestParam(value = "timestamp") String timestamp,
                                        @RequestParam(value = "signature") String signature) {
        try {
            log.info("checkOubaoCredit>>>>>>>>>>>userName=" + username + ",merchantCode=" + merchantCode + ",signature=" + signature);
            return userService.checkOubaoCredit(merchantCode, username, timestamp, signature);
        } catch (Exception e) {
            log.error("AccountController.changeCredit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/notifyMerchant")
    public APIResponse notifyMerchant(HttpServletRequest request,
                                      @RequestParam(value = "merchantCode") String merchantCode,
                                      @RequestParam(value = "transferId") String transferId,
                                      @RequestParam(value = "status") String status,
                                      @RequestParam(value = "msg") String msg,
                                      @RequestParam(value = "orderList") String orderList,
                                      @RequestParam(value = "timestamp") Long timestamp) {
        try {
            NotifyPO notifyPO = new NotifyPO();
            notifyPO.setMerchantCode(merchantCode);
            notifyPO.setTransferId(transferId);
            notifyPO.setTimestamp(timestamp);
            notifyPO.setMsg(msg);
            notifyPO.setStatus(status);
            notifyPO.setOrderList(orderList);
            log.info("回调通知:" + notifyPO);
            return userService.notifyMerchant(notifyPO) ?
                    APIResponse.returnSuccess(ApiResponseEnum.SUCCESS) :
                    APIResponse.returnFail(ApiResponseEnum.ACCOUNT_BALANCE_LACK);
        } catch (Exception e) {
            log.error("AccountController.changeCredit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @PostMapping("/notifySafetyMerchant")
    public APIResponse notifySafetyMerchant(HttpServletRequest request,
                                      @RequestParam(value = "merchantCode") String merchantCode,
                                      @RequestParam(value = "transferId") String transferId,
                                      @RequestParam(value = "userName") String userName,
                                      @RequestParam(value = "amount") String amount,
                                      @RequestParam(value = "transferType") String transferType,
                                      @RequestParam(value = "timestamp") Long timestamp,
                                      @RequestParam(value = "signature") String signature) {
        try {
            return userService.notifySafetyMerchant(merchantCode,transferId,userName,amount,transferType,timestamp,signature) ?
                    APIResponse.returnSuccess(ApiResponseEnum.BlandSUCCESS) :
                    APIResponse.returnFail(ApiResponseEnum.ACCOUNT_BALANCE_LACK);
        } catch (Exception e) {
            log.error("AccountController.changeCredit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }
}
