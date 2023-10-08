package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.UserOrderWeekPO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderWeekMapper extends BaseMapper<UserOrderWeekPO> {

    <T> List<T> queryUserOrderList(UserOrderVO vo);

    <T> List<T> queryUserOrderSportList(UserOrderVO vo);
}
