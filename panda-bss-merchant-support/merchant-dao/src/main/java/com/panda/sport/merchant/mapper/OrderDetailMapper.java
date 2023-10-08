package com.panda.sport.merchant.mapper;

import com.panda.sport.merchant.common.vo.api.BetApiVo;
import com.panda.sport.merchant.common.vo.api.BetDetailApiVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailMapper {

    List<BetDetailApiVo> selectList(@Param(value = "orderId") String orderId, @Param("startTime") Long starTime);


}
