package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.dto.UserAllowListReq;
import com.panda.sport.merchant.common.dto.UserOrderTimePO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserOrderUpdateMapper extends BaseMapper<UserOrderTimePO> {

    int updateUserOrderTime(@Param(value = "list") List<UserOrderTimePO> list);

    int updateUserSevenOrder(@Param(value = "list") List<UserOrderTimePO> list);


    int updateUserSevenOrderLast1();

    void batchUpdateUserBetAmount(@Param(value = "list") List<UserOrderDayPO> userList);

    @Update("update t_user set last_bet_time =#{lastBetTime} where uid = #{uid}")
    int updateUserOrderTimeV1(@Param(value = "lastBetTime") Long lastBetTime, @Param(value = "uid") String uid);

    @Update("update t_user set seven_day_profit_amount =#{profitAmount},seven_day_bet_amount=#{validBetAmount} where uid = #{uid}")
    int updateUserSevenOrderV1(@Param(value = "profitAmount") BigDecimal profitAmount, @Param(value = "validBetAmount") BigDecimal validBetAmount, @Param(value = "uid") String uid);

    int updateUserAllLifeData(@Param(value = "list") List<UserOrderTimePO> list);

    int upsertUserVip(@Param(value = "list") List<UserVipVO> list);

    Integer countVipUser(@Param(value = "uid") Long uid);

    List<UserVipVO> selectVipUserByMerchantcode(@Param(value = "merchantCode") String merchantCode);

    List<UserVipVO> findDisableUserInfo(@Param(value = "merchantCode") String merchantCode, @Param(value = "list") List<UserVipVO> list);

    int updateUserVip(@Param(value = "list") List<UserVipVO> list);


    int upsertUserDisabled(@Param(value = "list") List<UserVipVO> list, @Param(value = "disabled") Integer disabled, @Param(value = "remark") String  remark);
    int upsertUserDisabled2Allow(@Param(value = "list") List<UserVipVO> list, @Param(value = "disabled") Integer disabled, @Param(value = "remark") String  remark);

    int upsertUserToDisabled(@Param(value = "list") List<Long> list, @Param(value = "disabled") Integer disabled);

    int updateDisabled(@Param(value = "disabled") Integer disabled, @Param(value = "userId") Long userId );

    @Update("update t_user set is_test =#{isVipDomain} where uid = #{uid}")
    void updateIsVipDomain(@Param(value = "uid") long uid, @Param(value = "isVipDomain") Integer isVipDomain);

    Integer isVipUser(@Param("userId") String userId);

    int batchUpdateAllowListUserFlag(UserAllowListReq req);

}
