package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.dto.UserAllowListReq;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.UserLevelRelationVO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.user.UserVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface BackupTUserMapper {

    UserPO getUserByUserName(@Param(value = "merchantCode") String merchantCode, @Param(value = "merchantCodeList") List<String> merchantCodeList, @Param(value = "realName") String realName);

    List<UserVO> getUserByUserNameOther(@Param(value = "merchantCode") String merchantCode, @Param(value = "merchantCodeList") List<String> merchantCodeList, @Param(value = "userName") String userName);

    List<UserPO> getUserPO(@Param(value = "merchantCode") String merchantCode, @Param(value = "merchantCodeList") List<String> merchantCodeList, @Param(value = "userId") String userId, @Param(value = "userName") String userName);

    UserPO getUserInfo(@Param(value = "uid") Long uid);

    @Select("select u.uid userId,u.special_betting_limit_type,u.special_betting_limit_time,u.special_betting_limit_remark,u.special_betting_limit_delay_time,u.market_level from t_user u where u.uid=#{uid}")
    UserPO getUserLimitInfo(@Param(value = "uid") String uid);


    @Select("select u.id,u.uid,u.level_id as levelId,u.remark,u.sport_json as sportJson,u.tournament_json as tournamentJson,"
            + "u.order_type_json orderTypeJson,u.play_json playJson,u.order_stage_json as orderStageJson  from t_user_level_relation u  where u.uid=#{uid}")
    List<UserLevelRelationVO> getUserLevelList(@Param(value = "uid") Long uid);

    @Select("select id from t_merchant  where merchant_code=(select merchant_code from t_user where uid=#{uid})")
    String getUserMerchantId(@Param(value = "uid") String uid);

    @Select("select id,merchant_name as merchantName,merchant_code as merchantCode from t_merchant  where merchant_code=(select merchant_code from t_user where uid=#{uid})")
    MerchantPO getUserMerchantUserLog(@Param(value = "uid") String uid);

    @Update("update `t_user` set unactive = 1 WHERE modify_time < (unix_timestamp(now())*1000 -185*24*60*60*1000 ) and modify_time > (unix_timestamp(now())*1000 -186*24*60*60*1000 )")
    int updateUserActive();

    /**
     * @param userPO
     * @return UserPO
     */
    int insertTUser(UserPO userPO);

    /**
     * 避免丢失精度返回String
     *
     * @param vo
     * @return
     */
    List<String> queryUserIdListByUserName(UserOrderVO vo);

    @Select("select amount from t_user where uid=#{userId}")
    BigDecimal getBalance(@Param("userId") Long userId);



    @Update("update t_user set disabled=#{disabled} where merchant_code=#{merchantCode}")
    void updateAllMerchantUser(@Param(value = "merchantCode") String merchantCode, @Param(value = "disabled") int disabled);

    @Update("update t_user set agent_id=#{agentId} where uid=#{uid}")
    int updateUserAgentId(@Param(value = "uid") Long uid, @Param(value = "agentId") String agentId);

    @Update("update t_user set market_level =#{marketLevel} where uid=#{uid}")
    void updateUserMarketLevel(@Param(value = "uid") Long uid, @Param(value = "marketLevel") Integer marketLevel);

    void updateUserLastLogin(@Param(value = "uid") Long uid, @Param(value = "lastLogin") Long lastLogin);

    void updateUserSpecialBettingLimit(UserPO userInfo);

    int countAllUserList(UserOrderVO vo);


    List<UserOrderAllPO> queryAllUserList(UserOrderVO vo);


    List<Map<String, Object>> queryUserListByIdList(@Param(value = "list") List<Long> userList);

    List<UserVipVO> queryUserInfoByIdList(@Param(value = "list") List<Long> userList);
    List<UserVipVO> queryUserInfoAllAllow();

    UserVipVO queryUserInfoById(@Param(value = "userId") Long userId);

    UserOrderAllPO getUserDetail(UserOrderVO vo);

    List<UserOrderAllPO> getUserDetailList(UserOrderVO vo);

    @Select("select uid from t_user where fake_name = #{fakeName}")
    String getUserIdByFakeName(@Param(value = "fakeName") String fakeName);

    <T> List<T> queryRiskUserList(UserOrderVO vo);

    void batchUpdateIp(@Param(value = "startL") Long startL, @Param(value = "endL") Long endL);

    @Select("select uid from t_user where fake_name like #{fakeName}")
    List<String> listIdByUserName(@Param(value = "fakeName") String fakeName);

    List<Map<String, Object>> queryUserIdList(@Param(value = "merchantCode") String merchantCode);


    List<Map<Long, Long>> queryVipUpdateTime(@Param(value = "list") List<Long> userList);

    List<UserVipVO> listUserInfoById(@Param(value = "list") List<Long> userList);

    Long getOneUid(@Param(value = "merchantCode") String merchantCode);

    List<Long> getUserIdListByParam(Map<String, Object> paramMap);

    int getUserCountByParam(Map<String, Object> paramMap);

    int countUserAllowList(UserAllowListReq req);

    List<UserOrderAllPO> queryUserAllowList(UserAllowListReq req);

    List<Map<String, Object>> queryUserlogHistoryIDList(@Param(value = "merchantCode") String merchantCode);



    void clearUserDailyTask(@Param(value = "list") List<Long> userIds);

    List<Long> getDeleteUidList(@Param(value = "merchantCode") String merchantCode);
}
