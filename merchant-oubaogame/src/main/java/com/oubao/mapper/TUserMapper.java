package com.oubao.mapper;

/**
 *
 */

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.oubao.po.UserPO;

import java.util.List;


@Repository
public interface TUserMapper {

    /**
     * @description 根据uId或者id查询账户
     * @param userId；
     * @return UserPO
     */
    UserPO findTUserByCondition(@Param(value = "userId") Long userId);

    /**
     *
     * @param userPO
     * @return UserPO
     */
    int insertTUser(UserPO userPO) throws Exception;

    /**
     *
     * @param userPO
     * @throws Exception
     * @description 更新用户
     */
    void updateTUser(UserPO userPO) throws Exception;

    /**
     * @param username
     * @param phone
     * @param email
     * @return UserPO
     * @description 根据用户号，用户名称，手机号或emial查询对应账户信息是否已经存在
     */
    UserPO getUser(@Param("userId") Long userId, @Param("username") String username,
                   @Param("phone") String phone, @Param("email") String email, @Param("merchantCode") String merchantCode);

    /**
     * @param userId
     * @param username
     * @param phone
     * @param email
     * @param password
     * @return UserVo
     * @description 根据用户号，用户名称，手机号或emial查询对应账户，没有查询结果时返回为null
     */
    UserPO getUserVoByUserIdOrUsernameOrPhoneOrEmail(@Param("userId") Long userId, @Param("username") String username,
                                                     @Param("phone") String phone, @Param("email") String email,
                                                     @Param("password") String password
            , @Param("merchantCode") String merchantCode);

    @Select({"<script>",
            "SELECT u.*,a.amount FROM t_user u LEFT JOIN t_account a on u.user_id=a.uid where 1=1 ",
            "<when test='username!=null'>",
            "AND u.username=#{username}",
            "</when>",
            "<when test='startTime!=null'>",
            "AND u.create_time <![CDATA[ >= ]]> #{startTime}",
            "</when>",
            "<when test='endTime!=null'>",
            "AND u.create_time <![CDATA[ <= ]]> #{endTime}",
            "</when>",
            "<when test='merchantCode!=null'>",
            "AND u.merchant_code = #{merchantCode}",
            "</when>",
            " order by u.create_time desc ",
            " limit #{start},#{size}",
            "</script>"})
    List<UserPO> queryUserList(@Param("username") String username, @Param("merchantCode") String merchantCode,
                               @Param("startTime") Long startTime, @Param("endTime") Long endTime,
                               @Param("start") Integer start, @Param("size") Integer size);

    @Select({"<script>",
            "SELECT count(1) FROM t_user u  where 1=1 ",
            "<when test='username!=null'>",
            "AND u.username=#{username}",
            "</when>",
            "<when test='startTime!=null'>",
            "AND u.create_time <![CDATA[ >= ]]> #{startTime}",
            "</when>",
            "<when test='endTime!=null'>",
            "AND u.create_time <![CDATA[ <= ]]> #{endTime}",
            "</when>",
            "<when test='merchantCode!=null'>",
            "AND u.merchant_code = #{merchantCode}",
            "</when>",
            "</script>"})
    Integer countUserList(@Param("username") String username, @Param("merchantCode") String merchantCode,
                          @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    @Select("select user_id from t_user where username=#{username}")
    Long getUserId(@Param("username") String username);

    @Select("select merchant_key from t_merchant where merchant_code=#{merchantCode}")
    String getMerchantKey(@Param("merchantCode") String merchantCode);

    UserPO getBssUserByUserName(@Param("merchantCode") String merchantCode, @Param("userName") String userName);

    UserPO createBssUser(@Param("username") String userName, @Param("merchantCode") String oubao, String s);

    int insertBssTUser(UserPO userPO);

/*    Integer countUserByCondition(UserOperationVo userOperationVo);

    List<UserOperartionDto> queryUserByCondition(UserOperationVo userOperationVo);

    Integer updateUserByCondition(QueryConditionVo queryConditionVo);

    UserDetaiDto queryUserDetail(UserDetailVo userDetailVo);

    Integer countOrderSize(@Param("uid") Long uid);

    Integer countAccountChangeHistorySize(@Param("uid") Long uid);

    List<BetOrderDTO> queryBetOrders(UserDetailVo userDetailVo);

    List<AccountChangeHistoryDTO> queryAccountChangeHistories(UserDetailVo userDetailVo);*/
}
