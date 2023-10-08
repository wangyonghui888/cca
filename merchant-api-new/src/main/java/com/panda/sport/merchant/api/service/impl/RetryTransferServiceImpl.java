package com.panda.sport.merchant.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.bss.mapper.TransferRecordErrorMapper;
import com.panda.sport.bss.mapper.TransferRecordMapper;
import com.panda.sport.bss.mapper.TransferRecordRetryMapper;
import com.panda.sport.merchant.api.config.IdGeneratorFactory;
import com.panda.sport.merchant.api.config.RedisTemp;
import com.panda.sport.merchant.api.service.RetryTransferService;
import com.panda.sport.merchant.api.util.Md5Util;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.enums.BizTypeEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.HttpConnectionPool;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RefreshScope
@Service("tryTransferService")
public class RetryTransferServiceImpl extends AbstractMerchantService implements RetryTransferService {
    @Value("${DEFAULT_MERCHANT_CODE:111111}")
    private String DEFAULT_MERCHANT_CODE;
    @Autowired
    private TransferRecordMapper transferRecordMapper;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Autowired
    private TransferRecordErrorMapper transferRecordErrorMapper;

    @Autowired
    private TransferRecordRetryMapper recordRetryMapper;

    @Autowired
    private RedisTemp redisTemp;

    public static int STATUS_FAIL = 0;

    public static int STATUS_SUCCESS = 1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse retryTransferRecord(String transferId, Long retryCount, String userName) throws Exception {
        //查询过滤条件后失败的记录
        List<TransferRecordPO> transferList = transferRecordErrorMapper.getTransferRecordList(Arrays.asList(transferId), retryCount);
        log.info("retryTransfertransferList:" + (transferList == null ? 0 : transferList.size()));
        String result = "";
        if (CollectionUtils.isNotEmpty(transferList)) {
            for (TransferRecordPO po : transferList) {
                long startL = System.currentTimeMillis();
                UserPO userPO = userMapper.getUserInfo(po.getUserId());
                if (userPO == null) {
                    log.info(po.getUserId() + "retryTransfer用户为空!");
                    continue;
                }
                log.info(transferId + ",retryTransfer.userPO:" + userPO);
                Long userId = userPO.getUserId();
                try {
                    MerchantPO merchantPO = this.getMerchantPO(po.getMerchantCode());
                    log.info(new Date() + ",retryTransfer商户" + po.getMerchantCode() + ",merchant具体信息----->" + merchantPO.getUrl() + merchantPO.getBalanceUrl() + merchantPO.getCallbackUrl());
                    if (!po.getMerchantCode().equals(merchantPO.getMerchantCode())) {
                        log.info(userId + "retryTransfer交易号" + transferId + "商户code不匹配!");
                        return APIResponse.returnFail(transferId + "retryTransfer错误数据");
                    }
                    int count = transferRecordErrorMapper.countByTransferId(transferId);
                    log.info(transferId + "retryTransfer查询用户交易记录:" + count);
                    if (count > 0) {
                        TransferRecordPO transferRecordPO = transferRecordMapper.getTransferRecord(transferId);
                        if (transferRecordPO != null && transferRecordPO.getStatus() == 1) {
                            transferRecordErrorMapper.updateTransferRecord(transferId, STATUS_SUCCESS, "成功");
                            continue;
                        }
                        result = this.call(po, userPO, merchantPO, userName, 1);
                    }
                } catch (Exception e) {
                    log.error(userName + "retryTransfer分布式锁," + userId + "交易号" + transferId + "商户交易重试异常!", e);
                    result = "加锁失败";
                    throw new Exception("商户扣款失败3" + userName + transferId);
                }
                log.info(transferId + "retryTransfer.result=" + result);
            }
            if (result.equals("0000")) {
                return APIResponse.returnSuccess(result);
            } else {
                return APIResponse.returnFail(result);
            }
        } else {
            log.info("retryTransfer没有找到此交易订单号" + transferId);
            return APIResponse.returnFail("retryTransfer没有找到此交易订单号");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse addChangeRecordHistory(AccountChangeHistoryFindVO accountChangeHistoryFindVO, HttpServletRequest request) {
        String userName = accountChangeHistoryFindVO.getUsername();
        String ip = IPUtils.getIpAddr(request);
        try {
            double changeAmount = accountChangeHistoryFindVO.getChangeAmount();
            Integer bizType = accountChangeHistoryFindVO.getBizType();
            String merchantCode = StringUtils.isEmpty(accountChangeHistoryFindVO.getMerchantCode()) ? DEFAULT_MERCHANT_CODE : accountChangeHistoryFindVO
                    .getMerchantCode();
            AccountPO accountPO = merchantMapper.checkBalance(userName, merchantCode);
            double amount = 0;
            double beforeAmount = 0;
            if (null != accountPO) {
                amount = null != accountPO.getAmount() ? accountPO.getAmount().doubleValue() : 0;
                beforeAmount = null != accountPO.getAmount() ? accountPO.getAmount().doubleValue() : 0;
            }
            log.info("业务类型:" + bizType);
            AccountChangeHistoryFindVO vo = new AccountChangeHistoryFindVO();
            double thisAmount = new BigDecimal(changeAmount).multiply(new BigDecimal(100)).doubleValue();
            if (BizTypeEnum.ADD_MONEY_MANUALLY.getCode().equals(bizType)) {//增加
                vo.setChangeType(0);
                amount = amount + thisAmount;
            } else if (BizTypeEnum.MANUAL_DEBIT.getCode().equals(bizType)) {//减少
                vo.setChangeType(1);
                amount = amount - thisAmount;
            }
            merchantMapper.updateAccount(accountPO.getUid(), BigDecimal.valueOf(amount));
            vo.setChangeAmount(thisAmount);
            vo.setAccountId(accountPO.getId());
            vo.setUid(accountChangeHistoryFindVO.getUid());
            //不能用数据库的自增的id,要用时间+redis组成的id
            String idStr = idGeneratorFactory.generateIdByBussiness("accountChange", 6);
            log.info("手动加扣款生成日志I的{}：" + idStr);
            vo.setId(idStr);
            // vo.setChangeType(accountChangeHistoryFindVO.getChangeType());
            vo.setBizType(accountChangeHistoryFindVO.getBizType());
            vo.setCreateUser(userName);
            vo.setModifyUser(accountChangeHistoryFindVO.getModifyUser());
            vo.setOrderNo(accountChangeHistoryFindVO.getOrderNo());
            vo.setCurrentBalance(accountPO.getAmount());
            vo.setMerchantCode(accountChangeHistoryFindVO.getMerchantCode());
            vo.setCreateTime(System.currentTimeMillis());
            //vo.setCreateTime(accountChangeHistoryFindVO.getCreateTime());
            vo.setModifyTime(System.currentTimeMillis());
            vo.setRemark("备注: " + accountChangeHistoryFindVO.getRemark() + "  " + "手动加扣款操作人IP: " + ip);
            vo.setBeforeTransfer(beforeAmount);
            vo.setAfterTransfer(amount);
            merchantMapper.insertAccountChange(vo);
        } catch (Exception e) {
            log.error("上加扣款异常!" + accountChangeHistoryFindVO, e);
            return APIResponse.returnFail("上加扣款异常!");
        }
        return null;
    }


    /**
     * 1 HTTP请求调用商户扣款接口
     * 2 调用成功后,无论扣款成功与否,异步通知商户已收到扣款状态.
     * 3 如果调用失败或者扣款失败,扔出异常,回滚投注!
     * <p>
     * callType:1 重试 2手动发起
     *
     * @param po
     * @param userPO
     * @param merchantPO
     * @param merchantPO
     * @param userName
     * @throws Exception
     */
    public String call(TransferRecordPO po, UserPO userPO, MerchantPO merchantPO, String userName, int callType) throws Exception {
        String transferId = po.getTransferId();
        try {
            String url = merchantPO.getUrl();
            String callBackUrl = merchantPO.getCallbackUrl();
            Double amount = po.getAmount() / 100;
            String amountStr;
            if (amount > 1) {
                DecimalFormat decimalFormat = new DecimalFormat(".##");
                amountStr = decimalFormat.format(amount);
            } else {
                amountStr = amount + "";
            }
            String orderJson = po.getOrderStr();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            Long now = new Date().getTime();
            params.add("bizType", po.getBizType());
            params.add("merchantCode", merchantPO.getMerchantCode());
            params.add("userName", userPO.getUsername());
            params.add("transferId", transferId);
            int transferType = po.getTransferType();
            params.add("transferType", transferType);
            params.add("timestamp", now);
            log.info("call userPO:" + userPO);
            String stoken = (String) redisTemp.get(RedisConstants.USERCENTER_FAMILY_KEY + RedisConstants.USER_S_TOKEN_PREFIX + userPO.getUserId());
            log.info("stoken1:" + stoken);
            if (StringUtils.isNotEmpty(stoken)) {
                params.add("stoken", stoken);
            }
            log.info(transferId + ",amountStr:" + amountStr);
            params.add("signature",
                    Md5Util.getMD5(userPO.getUsername() + "&" + po.getBizType() + "&" + merchantPO.getMerchantCode() +
                            "&" + transferId + "&" + amountStr + "&" + transferType + "&" + now, merchantPO.getMerchantKey()));
            params.add("amount", amountStr);
            params.add("orderStr", orderJson);
            long callStartTime = System.currentTimeMillis();
            log.info(transferId + "商户调用入参:" + params + ",url=" + url);
            JSONObject json = callMerchantURL(po, userPO.getUsername(), transferId, url, new HttpEntity<>(params, headers), po.getRetryCount(), userName);
            log.info(transferId + json + "商户调用耗时：{}毫秒！", System.currentTimeMillis() - callStartTime);
            if (json != null) {
                String code = (String) json.get("code");
                String msg = (String) json.get("msg");
                if (code.equals("0000")) {
                    log.info(transferId + "交易成功!");
                    if (callType == 1) {
                        transferRecordMapper.updateTransferRecord(transferId, STATUS_SUCCESS, "成功");
                        transferRecordErrorMapper.updateTransferRecord(transferId, STATUS_SUCCESS, "成功");
                        retryTransferResult(po, userPO.getUsername(), userName, STATUS_SUCCESS, "成功!");
                    } else {
                        recordTransferResult(transferId, userPO, orderJson, amount, 1, msg, 1, 2, transferType);
                    }
                    callBackMerchant(callBackUrl, merchantPO, transferId, orderJson, STATUS_SUCCESS, msg, stoken);
                    return "0000";
                } else {
                    log.info(transferId + ",商户反馈扣款失败1!");
                    if (callType == 1) {
                        transferRecordErrorMapper.updateTransferRecord(transferId, STATUS_FAIL, msg);
                        retryTransferResult(po, userPO.getUsername(), userName, STATUS_FAIL, msg);
                    } else {
                        recordTransferResult(transferId, userPO, orderJson, amount, 0, msg, 1, 2, transferType);
                    }
                    callBackMerchant(callBackUrl, merchantPO, transferId, orderJson, STATUS_FAIL, msg, stoken);
                    return "transferId商户反馈加扣款失败!";
                }
            } else {
                log.info(transferId + "未知原因,商户扣款失败2!");
                callBackMerchant(callBackUrl, merchantPO, transferId, orderJson, STATUS_FAIL, "未知原因,商户扣款失败!", stoken);
            }
        } catch (Exception e) {
            log.error(transferId + "未知异常,商户扣款失败3!", e);
            throw new Exception("transferId商户扣款失败3");
        }
        return "未知原因,商户加扣款失败!";
    }


    private JSONObject callMerchantURL(TransferRecordPO po, String userName, String transferId, String url, HttpEntity<MultiValueMap<String, Object>> entity, int retryCount, String createUser) {
        try {
            ResponseEntity<String> response = HttpConnectionPool.restTemplate.postForEntity(url, entity, String.class);
            log.info(transferId + "商户调用返回结果:" + response);
            return JSONObject.parseObject(response.getBody());
        } catch (Exception e) {
            log.error(transferId + "调用商户后台第" + retryCount + "次失败!try again!", e);
            transferRecordErrorMapper.updateTransferRecord(transferId, STATUS_FAIL, "HTTPS请求失败,未知异常!");
            retryTransferResult(po, userName, createUser, STATUS_FAIL, e.getMessage());
            return null;
        }
    }

    private void retryTransferResult(TransferRecordPO po, String userName, String createUser, int status, String msg) {
        try {
            TransferRecordRetryPO retryPo = new TransferRecordRetryPO();
            retryPo.setMag(msg);
            retryPo.setTransferId(po.getTransferId());
            retryPo.setAmount(po.getAmount().longValue());
            retryPo.setMerchantCode(po.getMerchantCode());
            retryPo.setUserName(userName);
            retryPo.setBizType(po.getBizType());
            retryPo.setCreateUser(createUser);
            retryPo.setCreateTime(System.currentTimeMillis());
            retryPo.setStatus(status);
            recordRetryMapper.insert(retryPo);
        } catch (Exception e) {
            log.error("记录调用商户扣款状态异常!", e);
        }
    }

    private void recordTransferResult(String transferId, UserPO userPO, String orderJson, Double amount,
                                      int status, String msg, int transferMode, int transferSource, int transferType) {
        try {
            TransferRecordPO po = new TransferRecordPO();
            po.setTransferId(transferId);
            po.setUserId(userPO.getUserId());
            po.setMag(msg);
            po.setMerchantCode(userPO.getMerchantCode());
            po.setOrderStr(orderJson);
            po.setStatus(status);
            po.setCreateTime(System.currentTimeMillis());
            po.setAmount(new BigDecimal(amount.toString()).multiply(new BigDecimal(100)).doubleValue());
            po.setTransferMode(transferMode);
            po.setTransferType(transferType);
            po.setBizType(transferSource);
            transferRecordMapper.insert(po);
            if (status == 0) {
                TransferRecordErrorPO errorPo = new TransferRecordErrorPO();
                errorPo.setTransferId(transferId);
                errorPo.setUserId(userPO.getUserId());
                errorPo.setMag(msg);
                errorPo.setMerchantCode(userPO.getMerchantCode());
                errorPo.setOrderStr(orderJson);
                errorPo.setStatus(status);
                errorPo.setCreateTime(System.currentTimeMillis());
                Double amountS = new BigDecimal(amount.toString()).multiply(new BigDecimal(100)).doubleValue();
                errorPo.setAmount(amountS.longValue());
                errorPo.setTransferMode(transferMode);
                errorPo.setTransferType(transferType);
                errorPo.setBizType(transferSource);
                transferRecordErrorMapper.insert(errorPo);
            }
        } catch (Exception e) {
            log.error(orderJson + "记录调用商户扣款状态异常!" + transferId, e);
        }

    }


    /**
     * 1成功调用商户扣款之后
     * 2无论扣款成功与否,通知商户Panda已收到扣款状态信息
     * 3持久化本次通知.
     *
     * @param url
     * @param transferId
     * @param orderJson
     * @param status
     * @param msg
     */
    private void callBackMerchant(String url, MerchantPO merchantPO, String transferId, String orderJson, int status, String msg, String stoken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("merchantCode", merchantPO.getMerchantCode());
            params.add("transferId", transferId);
            params.add("status", status);
            if (StringUtils.isNotEmpty(stoken)) {
                params.add("stoken", stoken);
            }
            params.add("timestamp", new Date().getTime());
            params.add("signature", merchantPO.getTopic());
            params.add("msg", msg);
            params.add("orderList", orderJson);
            log.info(transferId + "callBackMerchant,param:" + params);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response1 = HttpConnectionPool.restTemplate.postForEntity(url, request, String.class);
            log.info(response1.getBody());
        } catch (Exception e) {
            log.error("回调通知商户扣款状态,异常!", e);
        }
    }
}
