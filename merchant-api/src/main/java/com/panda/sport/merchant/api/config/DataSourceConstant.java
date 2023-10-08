package com.panda.sport.merchant.api.config;

import com.panda.sport.merchant.common.constant.Constant;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class DataSourceConstant {

    public final static String MERCHANT_MASTER_TRANSACTION_MANAGER = "merchantMasterTransactionManager";

    public static final List<String> dataSourceList = Stream.of(Constant.MERCHANT_GROUP_COMMON, Constant.MERCHANT_GROUP_Y,
            Constant.MERCHANT_GROUP_S, Constant.MERCHANT_GROUP_B).collect(toList());

}
