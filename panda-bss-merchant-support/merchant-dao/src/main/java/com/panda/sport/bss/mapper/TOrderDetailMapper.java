package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.OrderDetailPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TOrderDetailMapper extends BaseMapper<OrderDetailPO> {

    OrderDetailPO getPlayAndMarket(@Param("marketId") Long market_id);
}
