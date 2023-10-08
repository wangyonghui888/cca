package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.AcBonusLogPO;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository(value = "bssBackUpAcBonusMapper")
public interface BackUpAcBonusMapper extends BaseMapper<AcBonusPO> {

    List<Map<String, Object>> queryAllTaskConfig(@Param("type") int type);

    /**
     * 获取 用户
     *
     * @param aids
     * @param uids
     * @return
     */
    List<AcBonusPO> queryUserBonus(@Param("aids") List<Long> aids, @Param("uids") Long uids, @Param("bonusType") Integer bonusType);

    /**
     * 查今天的日志
     *
     * @param aids
     * @param uids
     * @param bonusTime
     * @return
     */
    List<AcBonusLogPO> queryBonusLog(@Param("aids") List<Long> aids, @Param("uids") Long uids, @Param("bonusTime") String bonusTime);

    List<Long> queryAllReceivedUserListByType(@Param("taskId") Integer taskId, @Param("taskType") Integer taskType, @Param("userIdList") List<Long> userIdList);

    List<Long> queryAllReceivedUserListByTime(@Param("taskId") Integer taskId, @Param("timeL") Long timeL, @Param("userIdList") List<Long> userIdList);

    @Select("select max(last_update) from t_ac_bonus where task_id=#{taskId} and task_type=#{taskType}")
    Long queryMaxUpdateTime(@Param("taskId") Integer taskId, @Param("taskType") Integer taskType);

    List<Map<String, Object>> queryDailyLotteryUser(@Param("times") int times, @Param("startL") Long startL, @Param("endL") Long endL);
}
