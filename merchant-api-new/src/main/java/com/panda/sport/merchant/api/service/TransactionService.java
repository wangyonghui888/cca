package com.panda.sport.merchant.api.service;

import com.panda.sport.merchant.common.vo.api.APIResponse;

import java.math.BigDecimal;

public interface TransactionService {
    APIResponse executeTransaction(Long userPO, String type, String id, String userName, String merchantCode, BigDecimal amounts);
    APIResponse executeTransactionV1(Long userPO, String type, String id, String userName, String merchantCode, BigDecimal amounts, String amountType);
}
