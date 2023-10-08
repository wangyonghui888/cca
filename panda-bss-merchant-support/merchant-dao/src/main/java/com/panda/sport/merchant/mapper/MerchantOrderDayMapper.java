package com.panda.sport.merchant.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MerchantOrderDayMapper  {

    <T> List<T> queryMerchantOrderList(@Param("time") Long time,  @Param("merchantName") String merchantName);

    <T> List<T> queryMerchantSportOrderList(@Param("sportId") String sportId, @Param("time") Long time,
                                        @Param("merchantName") String merchantName);
}
