package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.UserCheckLogPO;
import com.panda.sport.merchant.common.vo.user.UserCheckLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCheckMapper extends BaseMapper<UserCheckLogPO> {


    List<UserCheckLogPO> selectList(UserCheckLogVO userCheckLogVO);

    int insertUserCheckLog(UserCheckLogPO userCheckLogPO);

    int countCheckUserByDays(String uid);

    int  countByToday(@Param("userName") String username);
}