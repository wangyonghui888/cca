package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.OrderSettleDetailPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSettleDetailMapper extends BaseMapper<OrderSettleDetailPO> {

    int insertList(@Param("list")List<OrderSettleDetailPO> list);

    int update(OrderSettleDetailPO detailPO);
}
