package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.OrderSettlePO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSettleMapper extends BaseMapper<OrderSettlePO> {

    int batchInsertOrder(@Param("list") List<OrderSettlePO> list);

    int update(OrderSettlePO orderSettlePO);

    int countOrder(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<OrderSettle> queryOrderList(@Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                     @Param("merchantCode") String merchantCode, @Param("start") Integer start, @Param("size") Integer size);



    int countOrderList(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("merchantCode") String merchantCode);

    List<String> queryMerchantList(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
    
}
