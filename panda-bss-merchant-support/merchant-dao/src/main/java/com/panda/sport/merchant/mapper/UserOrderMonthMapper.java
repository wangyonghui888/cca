package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.UserOrderMonthPO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserOrderMonthMapper extends BaseMapper<UserOrderMonthPO> {
    Map<String, Object> selectProfitByUser(@Param("userId") String userId, @Param("endTime") int endTime);

    List<UserOrderMonthPO> selectOrderByUser(@Param("userId") String userId, @Param("endTime") int endTime);

    <T> List<T> queryUserOrderList(UserOrderVO vo);

    <T> List<T> queryUserOrderSportList(UserOrderVO vo);

    <T> List<T> queryUserOrderListByYear(UserOrderVO vo);

    <T> List<T> queryUserOrderSportListByYear(UserOrderVO vo);
}
