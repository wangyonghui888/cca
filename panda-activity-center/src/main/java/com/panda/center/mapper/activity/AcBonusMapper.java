package com.panda.center.mapper.activity;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.center.entity.AcBonusLogPO;
import com.panda.center.entity.AcBonusPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动奖励表 Mapper 接口
 * </p>
 *
 * @author Baylee
 * @since 2021-06-30
 */
public interface AcBonusMapper extends BaseMapper<AcBonusPO> {

    List<Map<String, Object>> queryAllTaskConfig(@Param("type") int type);

    @Update("update t_ac_bonus set ticket_num=0,bonus_type=2 where task_type=0")
    void clearTicketsOfTask();

    @Update("update t_ac_bonus set ticket_num=0,bonus_type=2 where task_type=1 and task_id in(SELECT id FROM `t_ac_task` WHERE type = 1 and condition_id=#{conditionId})")
    void clearTicketsOfMardigraTask(@Param("conditionId") int conditionId);

    void upsertUserBonus(List<AcBonusPO> userBonusList);

    /**
     * 获取 用户
     *
     * @param aids
     * @param uids
     * @return
     */
    List<AcBonusPO> queryUserBonus(@Param("aids") List<Long> aids, @Param("uids") Long uids, @Param("bonusType") Integer bonusType);

    /**
     * 批量更新
     *
     * @param
     * @param
     * @param
     * @returnD
     */
    Integer insertBatchBonusLog(List<AcBonusLogPO> acBonusLogPOList);


    /**
     * @param aids
     * @param uids
     * @param bonusType
     * @return
     */
    Integer updateBatchBonusType(@Param("aids") List<Long> aids, @Param("uids") Long uids, @Param("bonusType") Integer bonusType);


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

    void resetGrowthTaskData();

    void deleteGrowthTaskData();
}
