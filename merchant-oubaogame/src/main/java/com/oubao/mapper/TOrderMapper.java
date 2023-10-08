package com.oubao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oubao.po.OrderPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单表
 *
 * @author butr 2020-01-06
 */
@Repository
public interface TOrderMapper extends BaseMapper<OrderPO> {

    int insertOrder(OrderPO orderPO);

    int updateOrder(OrderPO orderPO);

    void batchInsert(List<OrderPO> orderPOList);

    @Select("select count(0) from t_order where order_no=#{orderNo}")
    Integer countOrder(@Param("orderNo") String orderNo);
}
