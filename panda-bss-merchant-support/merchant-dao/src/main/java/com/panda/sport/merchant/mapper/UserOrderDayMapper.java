package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserOrderDayMapper extends BaseMapper<UserOrderDayPO> {

    int selectUserBetDays(@Param("userId") String userId);

    Map<String, Object> selectProfitByUser(@Param("userId") String userId, @Param("startTime") int startTime);

    List<UserOrderDayPO> selectOneMonthList(@Param("userId") String userId, @Param("startTime") int startTime, @Param("endTime") int endTime);

    <T> List<T> queryUserOrderList(UserOrderVO vo);

    <T> List<T> queryUserOrderSportList(UserOrderVO vo);
}
