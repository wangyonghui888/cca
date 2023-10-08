package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.po.bss.RcsUserSpecialBetLimitConfigPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BackupRcsUserSpecialBetLimitConfigMapper {

    /**
     * 批量保存（存在则更新）
     */
    void batchInsertOrUpdate(List<RcsUserSpecialBetLimitConfigPO> rcsUserSpecialBetLimitConfigList);

    /**
     * 批量保存
     */
    void batchInsert(List<RcsUserSpecialBetLimitConfigPO> rcsUserSpecialBetLimitConfigList);

    @Select("select * from rcs_user_special_bet_limit_config where user_id=#{userId} and special_betting_limit_type=#{specialBettingLimitType} order by create_time desc")
    List<RcsUserSpecialBetLimitConfigPO> selectListByUserIdAndType(@Param("userId")String userId, @Param("specialBettingLimitType")Integer specialBettingLimitType);

    @Delete("delete from rcs_user_special_bet_limit_config where user_id=#{userId} and special_betting_limit_type=#{specialBettingLimitType}")
    void deleteByUserIdAndLimitType(@Param("userId")Long userId, @Param("specialBettingLimitType")Integer specialBettingLimitType);
}
