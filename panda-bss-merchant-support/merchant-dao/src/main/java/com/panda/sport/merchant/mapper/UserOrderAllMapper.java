package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.HotPlayNameVO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderAllMapper extends BaseMapper<UserOrderAllPO> {

    UserOrderAllPO selectByUser(@Param("userId") String userId);

    List<UserOrderAllPO> queryUserBetList(UserOrderVO UserOrderVO);



    @Select("select max(last_update) from user_order_all")
    String getLastUpdate();

    long batchUpsert(@Param("list") List<UserOrderAllPO> subList, @Param("lastUpdate") String lastUpdate);
}
