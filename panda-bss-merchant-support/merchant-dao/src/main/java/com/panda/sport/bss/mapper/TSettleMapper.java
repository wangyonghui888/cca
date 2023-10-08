package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SettlePO;
import com.panda.sport.merchant.common.po.bss.TSettle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结算mapper
 */
@Repository
public interface TSettleMapper extends BaseMapper<TSettle> {

    /**
     * 查询 商户IDS
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<String> queryMerchantIdList(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("orderNoList") List<String> orderNoList);

    /**
     * 查询结算的订单号
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<String> querySettleOrderNo(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    Integer  querySettleTypeByOrderNo(@Param("orderNo") String orderNo);
}
