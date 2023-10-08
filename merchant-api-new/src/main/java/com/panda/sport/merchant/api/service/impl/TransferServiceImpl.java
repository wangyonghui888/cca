package com.panda.sport.merchant.api.service.impl;

import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.TAccountMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.bss.mapper.TransferRecordMapper;
import com.panda.sport.merchant.api.service.BalanceService;
import com.panda.sport.merchant.api.service.TransactionService;
import com.panda.sport.merchant.api.service.TransferService;
import com.panda.sport.merchant.api.util.DistributedLockerUtil;
import com.panda.sport.merchant.api.util.Md5Util;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.TransferModeEnum;
import com.panda.sport.merchant.common.enums.TransferTypeEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TransferRecordPO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.api.TransferApiVo;
import com.panda.sport.merchant.common.vo.api.UserApiVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.panda.sport.merchant.api.util.RedisConstants.*;

@RefreshScope
@Service("transferService")
@Slf4j
public class TransferServiceImpl extends AbstractMerchantService implements TransferService {
    @Autowired
    private TransferRecordMapper transferRecordMapper;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private TAccountMapper accountMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DistributedLockerUtil distributedLockerUtil;

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private CacheService cacheService;

    @Value("${transfer.switch:on}")
    private String transferSwitch;

    /**
     * 查询转账记录列表
     * a 验签,b 查询
     *
     * @description:
     * @Param: [request, merchantCode, username, startTime, endTime, pageSize, pageNum, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:33
     */
    @Override
    public APIResponse queryTransferList(HttpServletRequest request, String merchantCode, String username, String startTime, String endTime,
                                         Integer pageSize, Integer pageNum, Long timestamp, String signature) {
        String signStr = merchantCode + "&" + username + "&" + startTime + "&" + endTime + "&" + timestamp;
        try {
            APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("queryTransferList:" + errorResult);
                return errorResult;
            }
            //UserPO userPO = userMapper.getUserByUserName(merchantCode, username);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
            if (userPO == null) {
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            pageSize = (pageSize == null || pageSize > 100) ? 10 : pageSize;
            pageNum = pageNum == null ? 1 : pageNum;
            Long before7 = DateUtils.getStartTime();
            long startTimeL = Long.parseLong(startTime);
            if (startTimeL <= before7) {
                startTimeL = before7;
            }
            int count = transferRecordMapper.countListByUser(merchantCode, userPO.getUserId(), startTimeL, Long.parseLong(endTime));
            Map<String, Object> result = new HashMap();
            result.put("totalCount", count);
            if (count > 0) {
                List<TransferRecordPO> list = transferRecordMapper.selectListByUser(merchantCode, userPO.getUserId(),
                        (pageNum - 1) * pageSize, pageSize, startTimeL, Long.parseLong(endTime));
                list.forEach(e -> {
                    e.setUserName(username);
                });
                result.put("list", list);
            }
            result.put("pageSize", pageSize);
            result.put("pageNum", pageNum);
            return APIResponse.returnSuccess(result);
        } catch (Exception e) {
            log.error("FundController.queryTransferList,exception:" + signStr, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询转账记录
     * a 验签,b 查询
     *
     * @description:
     * @Param: [request, merchantCode, username, startTime, endTime, pageSize, pageNum, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:33
     */
    @Override
    public APIResponse getTransferRecord(HttpServletRequest request, String username, String merchantCode, String transferId, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + transferId + "&" + timestamp;
            APIResponse errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("1getTransferRecord:" + errorResult);
                return errorResult;
            }
            Long userId = null;
            String key = RedisConstants.PAY_FAMILY_KEY + TRANSFER_ID_PRE + transferId;
            log.info("2getTransferRecord.key:" + key);
            TransferRecordPO recordPO = (TransferRecordPO) redisTemp.getObject(key, TransferRecordPO.class);
            if (recordPO == null) {
                //UserPO userPO = userMapper.getUserByUserName(merchantCode, username);
                UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
                if (userPO != null) {
                    userId = userPO.getUserId();
                }
                recordPO = transferRecordMapper.selectById(merchantCode, userId, transferId);
                log.info("3database.getTransferRecord=" + recordPO);
                if (recordPO == null) {
                    return APIResponse.returnFail(ApiResponseEnum.TRANSFER_ID_NOT_EXIST);
                }
            } else {
                if (recordPO.getMerchantCode() != null && !recordPO.getMerchantCode().equalsIgnoreCase(merchantCode)) {
                    return APIResponse.returnFail(ApiResponseEnum.TRANSFER_ID_NOT_EXIST);
                }
                log.info("4cache.getTransferRecord=" + recordPO);
            }
            TransferApiVo apiVo = new TransferApiVo();
            BeanUtils.copyProperties(recordPO, apiVo);
            return APIResponse.returnSuccess(apiVo);
        } catch (Exception e) {
            log.error("5FundController.getTransferRecord,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * http://172.18.178.153:8090/pages/viewpage.action?pageId=32361168
     * 查询余额
     * a验签 b查询余额
     *
     * @Param: [request, userName, merchantCode, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:34
     */
    @Override
    public APIResponse checkBalance(HttpServletRequest request, String userName, String merchantCode, Long timestamp, String signature) {
        try {
            if (Math.abs((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60)) > 23) {
                return APIResponse.returnFail(ApiResponseEnum.REQUEST_TIME_OUT);
            }
            Map<String, Object> map = Maps.newHashMap();
            String balanceStr = redisTemp.get(RedisConstants.PAY_FAMILY_KEY + BALANCE + merchantCode + userName);
            if (StringUtils.isNotEmpty(balanceStr) && !"null".equalsIgnoreCase(balanceStr)) {
                log.info("redis获取余额" + userName + ":" + balanceStr);
                map.put("balance", Double.parseDouble(balanceStr));
                map.put("userName", userName);
            } else {
                MerchantPO merchantPO = this.getMerchantPO(merchantCode);
                if (merchantPO == null) {
                    return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
                }
                String singStr = merchantCode + "&" + userName + "&" + timestamp;
                //商户验签
                APIResponse<Object> response = this.checkSignature(singStr, signature, merchantPO);
                if (response != null) {
                    log.error(merchantCode + "获取余额失败,验签失败!" + userName);
                    return response;
                }
                /*String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
                if (StringUtil.isBlankOrNull(key)) {
                    return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
                }
                if (!Md5Util.checkMd5(merchantCode + "&" + userName + "&" + timestamp, key, signature)) {
                    return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
                }*/
                if (merchantPO.getTransferMode().intValue() != TransferModeEnum.TRANSFER.getCode()) {
                    return APIResponse.returnFail(ApiResponseEnum.TRANSFER_MODE_NOT_MATCH);
                }
                // UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
                UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
                if (userPO == null) {
                    return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
                }
                Long userId = userPO.getUserId();
                // BigDecimal amount = accountMapper.getUserBalance(userId);
                BigDecimal amount = balanceService.getUserBalance(userId).divide(new BigDecimal(100));
                map.put("balance", amount);
                map.put("userName", userName);
                log.info("database获取余额" + userName + ":" + amount);
                redisTemp.setWithExpireTime(RedisConstants.PAY_FAMILY_KEY + BALANCE + merchantCode + userName, amount + "", EXPIRE_TIME_TWO_MIN);
            }
            return APIResponse.returnSuccess(map);
        } catch (Exception e) {
            log.error("FundController.checkBalance,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 上下分
     * a验签 b交易c记录交易
     *
     * @Param: [request, userName, merchantCode, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:34
     */
    @Override
    public APIResponse transfer(HttpServletRequest request, String merchantCode, String userName, String transferType, String amount, String transferId, Long timestamp, String signature, String currency) throws Exception {
        if (Math.abs((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60)) > 23) {
            return APIResponse.returnFail(ApiResponseEnum.REQUEST_TIME_OUT);
        }
        if (transferSwitch.equals("off")) {
            return APIResponse.returnFail(ApiResponseEnum.RATE_LIMIT_ERROR);
        }
        MerchantPO merchantPO = this.getMerchantPO(merchantCode);
        if (merchantPO == null || StringUtil.isBlankOrNull(merchantPO.getMerchantKey())) {
            log.error(merchantCode + "转账失败!商户无效," + transferId);
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        if (merchantPO.getTransferMode().intValue() != TransferModeEnum.TRANSFER.getCode()) {
            log.error(merchantCode + "转账失败!转账模式错误," + transferId);
            return APIResponse.returnFail(ApiResponseEnum.INVALID_TRANSFER_TYPE);
        }
        //商户验签
        String signStr = merchantCode + "&" + userName + "&" + transferType + "&" + amount + "&" + transferId + "&" + timestamp;
        APIResponse<Object> response = checkSignature(signStr, signature, merchantPO);
        if (response != null) {
            log.error(merchantCode + "转账失败!验签失败," + transferId);
            return response;
        }
        /*String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
        if (!Md5Util.checkMd5(merchantCode + "&" + userName + "&" + transferType + "&" + amount + "&" + transferId + "&" + timestamp, key, signature)) {
            log.error(merchantCode + "转账失败!验签失败," + transferId);
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }*/
        UserApiVo userApiVo = (UserApiVo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + Colon + "mobile", UserApiVo.class);
        Long userId;
        if (userApiVo != null) {
            log.info("transfer-cache-getUser:" + userApiVo);
            userId = Long.parseLong(userApiVo.getUserId());
        } else {
            //UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
            if (userPO == null && transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode())) {
                String currencyCode;
                if (StringUtils.isEmpty(currency) || currency.equalsIgnoreCase("null")) {
                    currencyCode = merchantPO.getCurrency() == null ? CurrencyTypeEnum.RMB.getId() : (merchantPO.getCurrency() + "");
                } else {
                    currencyCode = currency;
                }
                Integer userPrefix = merchantPO.getUserPrefix();
                try {
                    distributedLockerUtil.lock(RedisConstants.CREAT_FAMILY_KEY + merchantCode + userName);
                    log.info(merchantCode + "注册时获取锁插入用户" + userName);
                    userPO = createUserService.createUser(userName, merchantCode, currencyCode, getFakeName(merchantCode, userName, userPrefix), null, merchantPO.getMerchantTag());
                } finally {
                    distributedLockerUtil.unLock(RedisConstants.CREAT_FAMILY_KEY + merchantCode + userName);
                }
                if (userPO == null) {
                    return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_INVALID);
                }
            } else if (userPO == null) {
                log.error(transferId + ",转账失败!玩家不存在:" + merchantCode + "," + userName);
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            userId = userPO.getUserId();
        }
        BigDecimal amounts = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0, RoundingMode.FLOOR);
        if (amounts.compareTo(BigDecimal.ZERO) <= 0) {
            log.error(transferId + "," + merchantCode + "转账失败!额度错误:" + userName + "," + amounts);
            return APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
        }
        long startL = System.currentTimeMillis();
        try {
            distributedLockerUtil.lock(RedisConstants.PAY_FAMILY_KEY + userId);
            log.info(transferId + "获取lock花费:" + (System.currentTimeMillis() - startL) + "," + userName);
            return transactionService.executeTransaction(userId, transferType, transferId, userName, merchantCode, amounts);
        } catch (Exception e) {
            log.error(merchantCode + "," + userName + ",分布式锁:" + userId + ",商户上下分异常!" + transferId, e);
            throw new Exception(userName + "分布式锁" + userId + "商户上下分异常!" + transferId);
        } finally {
            log.info(RedisConstants.PAY_FAMILY_KEY + userId + "释放锁共耗时>>>>>>>" + (System.currentTimeMillis() - startL) + "," + transferId);
            distributedLockerUtil.unLock(RedisConstants.PAY_FAMILY_KEY + userId);
        }
    }

    /**
     * 上下分
     * a验签 b交易c记录交易
     *
     * @Param: [request, userName, merchantCode, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:34
     */
    @Override
    public APIResponse transferV1(HttpServletRequest request, String merchantCode, String userName, String transferType, String amount, String transferId, Long timestamp, String amountType, String signature) throws Exception {
        if (Math.abs((System.currentTimeMillis() - timestamp) / (1000 * 60)) > 5) {
            return APIResponse.returnFail(ApiResponseEnum.REQUEST_TIME_OUT);
        }
        if (transferSwitch.equals("off")) {
            return APIResponse.returnFail(ApiResponseEnum.RATE_LIMIT_ERROR);
        }
        MerchantPO merchantPO = this.getMerchantPO(merchantCode);
        if (merchantPO == null || StringUtil.isBlankOrNull(merchantPO.getMerchantKey())) {
            log.error(merchantCode + "转账失败!商户无效," + transferId);
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        if (merchantPO.getTransferMode().intValue() != TransferModeEnum.TRANSFER.getCode()) {
            log.error(merchantCode + "转账失败!转账模式错误," + transferId);
            return APIResponse.returnFail(ApiResponseEnum.INVALID_TRANSFER_TYPE);
        }
        //商户验签
        String signStr = merchantCode + "&" + userName + "&" + transferType + "&" + amount + "&" + transferId + "&" + timestamp;
        APIResponse<Object> response = this.checkSignature(signStr, signature, merchantPO);
        if (response != null) {
            log.error(merchantCode + "转账失败!验签失败," + transferId);
            return response;
        }
        /*String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
        if (!Md5Util.checkMd5(merchantCode + "&" + userName + "&" + transferType + "&" + amount + "&" + transferId + "&" + timestamp, key, signature)) {
            log.error(merchantCode + "转账失败!验签失败," + transferId);
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }*/
        UserApiVo userApiVo = (UserApiVo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + Colon + "mobile", UserApiVo.class);
        Long userId;
        if (userApiVo != null) {
            log.info("transfer-cache-getUser:" + userApiVo);
            userId = Long.parseLong(userApiVo.getUserId());
        } else {
            //UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
            if (userPO == null && transferType.equals(TransferTypeEnum.ADD_AMOUNT.getCode())) {
                String currencyCode = merchantPO.getCurrency() == null ? CurrencyTypeEnum.RMB.getId() : (merchantPO.getCurrency() + "");
                Integer userPrefix = merchantPO.getUserPrefix();
                userPO = createUserService.createUser(userName, merchantCode, currencyCode, getFakeName(merchantCode, userName, userPrefix), null, merchantPO.getMerchantTag());
                if (userPO == null) {
                    return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_INVALID);
                }
            } else if (userPO == null) {
                log.error(transferId + ",转账失败!玩家不存在:" + merchantCode + "," + userName);
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            userId = userPO.getUserId();
        }
        BigDecimal amounts = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0, RoundingMode.FLOOR);
        if (!"1".equals(amountType)) {
            if (amounts.compareTo(BigDecimal.ZERO) <= 0) {
                log.error(transferId + "," + merchantCode + "转账失败!额度错误:" + userName + "," + amounts);
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_BALANCE_NOT_ENOUGH);
            }
        }
        long startL = System.currentTimeMillis();
        try {
            distributedLockerUtil.lock(RedisConstants.PAY_FAMILY_KEY + userId);
            log.info(transferId + "获取lock花费:" + (System.currentTimeMillis() - startL) + "," + userName);
            return transactionService.executeTransactionV1(userId, transferType, transferId, userName, merchantCode, amounts, amountType);
        } catch (Exception e) {
            log.error(merchantCode + "," + userName + ",分布式锁:" + userId + ",商户上下分异常!" + transferId, e);
            throw new Exception(userName + "分布式锁" + userId + "商户上下分异常!" + transferId);
        } finally {
            log.info(RedisConstants.PAY_FAMILY_KEY + userId + "释放锁共耗时>>>>>>>" + (System.currentTimeMillis() - startL) + "," + transferId);
            distributedLockerUtil.unLock(RedisConstants.PAY_FAMILY_KEY + userId);
        }
    }

    private String getFakeName(String merchantCode, String username, Integer userPrefix) {
        if (userPrefix == null || userPrefix == 0) {
            return merchantCode + "_" + username;
        }
        StringBuilder tempSb = new StringBuilder();
        for (int i = 0; i < userPrefix; i++) {
            tempSb.append("*");
        }
        if (username.length() <= userPrefix) {
            return merchantCode + "_" + tempSb.toString();
        }
        StringBuilder sb = new StringBuilder(username);
        StringBuilder fake = sb.replace(0, userPrefix, tempSb.toString());
        return merchantCode + "_" + fake.toString();
    }
}
