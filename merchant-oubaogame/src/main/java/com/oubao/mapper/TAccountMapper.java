package com.oubao.mapper;

import com.oubao.po.OrderPO;
import com.oubao.po.TAccountChangeHistoryPO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import com.oubao.po.AccountPO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: david
 * @version: V1.0.0
 * @Project Name : panda-bss
 * @Package Name : com.panda.sports.bss.lottery.mapper
 * @Description: 用户表Mapper接口
 * @Date: 2019-10-10 16:37
 */
@Repository
public interface TAccountMapper {

    /**
     * @param accounts 批量账户
     * @Author :  david
     * @Description :批量更新账户
     * @Since: 2019-10-22 14:10
     */
    int batchUpdateAccount(List<AccountPO> accounts);

    /**
     * @param accounts 账户
     * @Author :  david
     * @Description :开户
     * @Since: 2019-10-22 14:10
     */
    int insertAccount(AccountPO accounts);

    @Select("select a.id,a.uid,a.amount from t_account a LEFT join t_user u on a.uid=u.user_id where u.username=#{userName} and u.merchant_code=#{merchantCode}")
    AccountPO checkBalance(@Param("userName") String username, @Param("merchantCode") String merchantCode);

    @Update("update t_account a set a.amount =#{amount} where a.uid =#{uid}")
    void updateAccount(@Param("uid") Long uid, @Param("amount") BigDecimal amount);

    void insertAccountChange(TAccountChangeHistoryPO po);

    @Select({"<script>",
            "SELECT u.* from t_account_change_history u LEFT JOIN t_user a on a.user_id=u.uid where 1=1 ",
            "<when test='username!=null'>",
            "AND a.username=#{username}",
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
    List<TAccountChangeHistoryPO> queryAccountChangeList(@Param("username") String username, @Param("merchantCode") String merchantCode,
                                                         @Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                                         @Param("start") Integer start, @Param("size") Integer size);

    @Select({"<script>",
            "SELECT count(1) FROM t_account_change_history u LEFT JOIN t_user a on a.user_id=u.uid where 1=1 ",
            "<when test='username!=null'>",
            "AND a.username=#{username}",
            "</when>",
            "<when test='startTime!=null'>",
            "AND u.create_time <![CDATA[ >= ]]> #{startTime}",
            "</when>",
            "<when test='endTime!=null'>",
            "AND u.create_time <![CDATA[ <= ]]> #{endTime}",
            "</when>",
            "<when test='merchantCode!=null'>",
            "AND u.merchant_code =  #{merchantCode}",
            "</when>",
            "</script>"})
    Integer countAccountChangeList(@Param("username") String username, @Param("merchantCode") String merchantCode,
                                   @Param("startTime") Long startTime, @Param("endTime") Long endTime);


    @Select({"<script>",
            "SELECT u.* from t_order u LEFT JOIN t_user a on a.user_id=u.uid where 1=1 ",
            "<when test='username!=null'>",
            "AND a.username=#{username}",
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
    List<OrderPO> queryOrderList(@Param("username") String username, @Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                 @Param("merchantCode") String merchantCode, @Param("start") Integer start, @Param("size") Integer size);

    @Select({"<script>",
            "SELECT count(1) FROM t_order u LEFT JOIN t_user a on a.user_id=u.uid where 1=1 ",
            "<when test='username!=null'>",
            "AND a.username=#{username}",
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
    Integer countOrderList(@Param("username") String username, @Param("startTime") Long startTime,
                           @Param("endTime") Long endTime, @Param("merchantCode") String merchantCode);


    void insertBssAccount(AccountPO accounts);
}