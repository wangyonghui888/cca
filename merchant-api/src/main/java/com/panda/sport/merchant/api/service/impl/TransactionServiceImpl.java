package com.panda.sport.merchant.api.service.impl;

import com.panda.sport.bss.mapper.TAccountChangeHistoryMapper;
import com.panda.sport.bss.mapper.TAccountMapper;
import com.panda.sport.bss.mapper.TransferRecordMapper;
import com.panda.sport.merchant.api.config.DataSourceConstant;
import com.panda.sport.merchant.api.config.IdGeneratorFactory;
import com.panda.sport.merchant.api.mq.MQMsgInfo;
import com.panda.sport.merchant.api.mq.MessageProduct;
import com.panda.sport.merchant.api.service.BalanceService;
import com.panda.sport.merchant.api.service.TransactionService;
import com.panda.sport.merchant.api.util.ExecutorInstance;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.common.enums.TransferModeEnum;
import com.panda.sport.merchant.common.enums.TransferTypeEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.AccountChangeHistoryPO;
import com.panda.sport.merchant.common.po.bss.TransferRecordPO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.panda.sport.merchant.api.util.RedisConstants.*;

@Slf4j
@RefreshScope
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransferRecordMapper transferRecordMapper;

    @Autowired
    private TAccountChangeHistoryMapper tAccountChangeHistoryMapper;

    @Autowired
    private TAccountMapper accountMapper;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private MessageProduct messageProduct;

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Autowired
    public RedisTemp redisTemp;

    @Value("${panda.bss.order.user.money.freeze:false}")
    private Boolean transferFreezeSwitch;

    private static final String topic = "USER_BALANCE_CHANGE";
    public static final String USER_MONEY_FREEZE = "USER_MONEY_FREEZE:";

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstant.MERCHANT_MASTER_TRANSACTION_MANAGER)
    public APIResponse executeTransaction(Long userId, String transferType, String transferId, String userName, String merchantCode, BigDecimal amounts) {
        int result;
        TransferRecordPO oldTransfer = transferRecordMapper.getTransferRecord(transferId);
        if (oldTransfer != null && oldTransfer.getUserId().equals(userId)) {
            log.info(transferId + ",executeTransaction交易已处理!" + oldTransfer);
            return oldTransfer.getStatus() == 1 ? APIResponse.returnSuccess() : APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);//遵循幂等性原则
        } else if (oldTransfer != null && !oldTransfer.getUserId().equals(userId)) {
            log.error(transferId + ",executeTransaction交易已处理!" + oldTransfer + ",历史用户:" + oldTransfer.getUserId());
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);//遵循幂等性原则
        }
        BigDecimal origin = balanceService.getUserBalance(userId);
        log.info(transferId + ",executeTransaction开始更新玩家额度,uid=" + userId + ",transferType:" + transferType + ",amounts=" + amounts + ",初始额度=" + origin);
        if (transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode())) {
            result = accountMapper.addBalance(userId, amounts.doubleValue());
        } else if (transferType.equals(TransferTypeEnum.SUB_AMOUNT.getCode())) {
            if (transferFreezeSwitch) {
                Double freezeMoney = 0d;
                String freezeMoneyTotal = redisTemp.get(USER_MONEY_FREEZE + userId);
                if (StringUtils.isNotEmpty(freezeMoneyTotal)) {
                    freezeMoney = Double.parseDouble(freezeMoneyTotal);
                    if ((origin.doubleValue() - freezeMoney) < amounts.doubleValue()) {
                        log.error(transferId + "," + merchantCode + "executeTransaction转账失败!freezeMoney=" + freezeMoney + userName + ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                        this.insertFailedTransfer(transferType, origin, amounts, merchantCode, transferId, userId);
                        return APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                    }
                } else {
                    if (origin.doubleValue() < amounts.doubleValue()) {
                        log.error(transferId + "," + merchantCode + "executeTransaction转账失败!" + userName + ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                        return APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                    }
                }
            } else {
                if (origin.doubleValue() < amounts.doubleValue()) {
                    log.error(transferId + "," + merchantCode + "executeTransaction转账失败!" + userName + ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                    return APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                }
            }
            result = accountMapper.subBalance(userId, amounts.doubleValue());
        } else {
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
        Double after = transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ?
                (origin.longValue() + amounts.doubleValue()) : (origin.doubleValue() - amounts.doubleValue());
        TransferRecordPO transferRecordPO = this.insertTransferRecord(transferType, origin, amounts, merchantCode, transferId, userId, result, after);

        transferRecordPO.setBeforeTransfer(transferRecordPO.getBeforeTransfer() / 100);
        transferRecordPO.setAfterTransfer(transferRecordPO.getAfterTransfer() / 100);
        transferRecordPO.setAmount(transferRecordPO.getAmount() / 100);
        transferRecordPO.setUserName(userName);
        redisTemp.setObject(RedisConstants.PAY_FAMILY_KEY + TRANSFER_ID_PRE + transferRecordPO.getTransferId(), transferRecordPO, EXPIRE_TIME_ONE_HOUR);
        redisTemp.setWithExpireTime(RedisConstants.PAY_FAMILY_KEY + BALANCE + merchantCode + userName, after / 100 + "", EXPIRE_TIME_TWO_MIN);
        cacheAndNotifyUserBalanceChange(userId, merchantCode, userName, after, origin, amounts, transferType, result, transferRecordPO);
        return result == 1 ? APIResponse.returnSuccess() : APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
    }

    private TransferRecordPO insertTransferRecord(String transferType, BigDecimal origin, BigDecimal amounts, String merchantCode, String transferId,
                                                  Long userId, int result, Double after) {

        TransferRecordPO transferRecordPO = new TransferRecordPO();
        transferRecordPO.setAmount(amounts.doubleValue());
        transferRecordPO.setBizType(transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ? 7 : 8);
        transferRecordPO.setMag(result == 1 ? "成功" : "失败");
        transferRecordPO.setMerchantCode(merchantCode);
        transferRecordPO.setStatus(result);
        transferRecordPO.setTransferMode(TransferModeEnum.TRANSFER.getCode());
        transferRecordPO.setTransferId(transferId);
        transferRecordPO.setTransferType(Integer.valueOf(transferType));
        transferRecordPO.setUserId(userId);
        transferRecordPO.setCreateTime(System.currentTimeMillis());
        transferRecordPO.setBeforeTransfer(origin.doubleValue());
        transferRecordPO.setAfterTransfer(after);
        transferRecordMapper.insert(transferRecordPO);
        return transferRecordPO;
    }

    private void insertFailedTransfer(String transferType, BigDecimal origin, BigDecimal amounts, String merchantCode, String transferId, Long userId) {
        Double after = transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ?
                (origin.longValue() + amounts.doubleValue()) : (origin.doubleValue() - amounts.doubleValue());
        TransferRecordPO transferRecordPO = new TransferRecordPO();
        transferRecordPO.setAmount(amounts.doubleValue());
        transferRecordPO.setBizType(transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ? 7 : 8);
        transferRecordPO.setMag("投注扣款冻结额度导致可用额度不足");
        transferRecordPO.setMerchantCode(merchantCode);
        transferRecordPO.setStatus(0);
        transferRecordPO.setTransferMode(TransferModeEnum.TRANSFER.getCode());
        transferRecordPO.setTransferId(transferId);
        transferRecordPO.setTransferType(Integer.valueOf(transferType));
        transferRecordPO.setUserId(userId);
        transferRecordPO.setCreateTime(System.currentTimeMillis());
        transferRecordPO.setBeforeTransfer(origin.doubleValue());
        transferRecordPO.setAfterTransfer(after);
        transferRecordMapper.insert(transferRecordPO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstant.MERCHANT_MASTER_TRANSACTION_MANAGER)
    public APIResponse executeTransactionV1(Long userId, String transferType, String transferId, String userName, String merchantCode, BigDecimal amounts, String amountType) {
        int result;
        TransferRecordPO oldTransfer = transferRecordMapper.getTransferRecord(transferId);
        if (oldTransfer != null && oldTransfer.getUserId().equals(userId)) {
            log.info(transferId + ",executeTransactionV1交易已处理!" + oldTransfer);
            return APIResponse.returnFail(ApiResponseEnum.PLAYER_ORDER_NO);//遵循幂等性原则
        } else if (oldTransfer != null && !oldTransfer.getUserId().equals(userId)) {
            log.error(transferId + ",executeTransactionV1交易已处理!" + oldTransfer + ",历史用户:" + oldTransfer.getUserId());
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);//遵循幂等性原则
        }
        BigDecimal origin = balanceService.getUserBalance(userId);
        if ("1".equals(amountType) && transferType.equals(TransferTypeEnum.SUB_AMOUNT.getCode())) {
            amounts = new BigDecimal(origin.doubleValue());
        }
        log.info(transferId + ",executeTransactionV1开始更新玩家额度,uid=" + userId + ",transferType:" + transferType + ",amounts=" + amounts + ",初始额度=" + origin);
        if (transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode())) {
            result = accountMapper.addBalance(userId, amounts.doubleValue());
        } else if (transferType.equals(TransferTypeEnum.SUB_AMOUNT.getCode())) {
            if (origin.doubleValue() < amounts.doubleValue()) {
                log.error(transferId + "," + merchantCode + "executeTransactionV1转账失败!" + userName + ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
            }
            result = accountMapper.subBalance(userId, amounts.doubleValue());
        } else {
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
        Double after = transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ?
                (origin.longValue() + amounts.doubleValue()) : (origin.doubleValue() - amounts.doubleValue());
        TransferRecordPO transferRecordPO = new TransferRecordPO();
        transferRecordPO.setAmount(amounts.doubleValue());
        transferRecordPO.setBizType(transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ? 7 : 8);
        transferRecordPO.setMag(result == 1 ? "成功" : "失败");
        transferRecordPO.setMerchantCode(merchantCode);
        transferRecordPO.setStatus(result);
        transferRecordPO.setTransferMode(TransferModeEnum.TRANSFER.getCode());
        transferRecordPO.setTransferId(transferId);
        transferRecordPO.setTransferType(Integer.valueOf(transferType));
        transferRecordPO.setUserId(userId);
        transferRecordPO.setCreateTime(System.currentTimeMillis());
        transferRecordPO.setBeforeTransfer(origin.doubleValue());
        transferRecordPO.setAfterTransfer(after);
        transferRecordMapper.insert(transferRecordPO);
        transferRecordPO.setBeforeTransfer(transferRecordPO.getBeforeTransfer() / 100);
        transferRecordPO.setAfterTransfer(transferRecordPO.getAfterTransfer() / 100);
        transferRecordPO.setAmount(transferRecordPO.getAmount() / 100);
        transferRecordPO.setUserName(userName);
        redisTemp.setObject(RedisConstants.PAY_FAMILY_KEY + TRANSFER_ID_PRE + transferRecordPO.getTransferId(), transferRecordPO, EXPIRE_TIME_ONE_HOUR);
        redisTemp.setWithExpireTime(RedisConstants.PAY_FAMILY_KEY + BALANCE + merchantCode + userName, after / 100 + "", EXPIRE_TIME_TWO_MIN);
        cacheAndNotifyUserBalanceChange(userId, merchantCode, userName, after, origin, amounts, transferType, result, transferRecordPO);
        return APIResponse.returnSuccess(after / 100);
    }

    private void cacheAndNotifyUserBalanceChange(Long uid, String merchantCode, String userName, Double after, BigDecimal origin,
                                                 BigDecimal amounts, String transferType, int result, TransferRecordPO transferRecordPO) {
        try {
            AccountChangeHistoryPO tAccountChangeHistory = new AccountChangeHistoryPO();
            tAccountChangeHistory.setUid(String.valueOf(uid));
            tAccountChangeHistory.setOrderNo(transferRecordPO.getTransferId());
            String idStr = idGeneratorFactory.generateIdByBussiness("accountChange", 6);
            tAccountChangeHistory.setId(idStr);
            tAccountChangeHistory.setCurrentBalance(origin.longValue());
            tAccountChangeHistory.setChangeAmount(amounts.doubleValue());
            tAccountChangeHistory.setChangeType(transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ? 0 : 1);
            tAccountChangeHistory.setBizType(Integer.valueOf(transferType));
            tAccountChangeHistory.setRemark((transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode()) ?
                    "转账钱包上分" : "转账钱包下分") + (result == 1 ? "成功" : "失败"));
            tAccountChangeHistory.setCreateUser(userName);
            tAccountChangeHistory.setCreateTime(System.currentTimeMillis());
            tAccountChangeHistory.setModifyUser(userName);
            tAccountChangeHistory.setModifyTime(System.currentTimeMillis());
            tAccountChangeHistory.setBeforeTransfer(origin.longValue());
            tAccountChangeHistory.setAfterTransfer(after.longValue());
            tAccountChangeHistoryMapper.insert(tAccountChangeHistory);
            ExecutorInstance.executorService.submit(() -> {
                try {
                    MQMsgInfo msgInfo = new MQMsgInfo();
                    msgInfo.setTopic(topic);
                    msgInfo.setMqKey(transferRecordPO.getTransferId());
                    msgInfo.setMqTag("transfer");
                    msgInfo.setObj(uid);
                    messageProduct.sendMessage(msgInfo, "通知用户上下分余额变更");
                } catch (Exception e) {
                    log.error("下发客户端MQ异常!", e);
                }
            });
        } catch (Exception e) {
            log.error("cacheAndNotifyUserBalanceChange!", e);
        }
    }
}
