package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.bss.ActivityMerchantPo;
import com.panda.sport.merchant.common.vo.api.TActivityMerchant;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ActivityMerchantMapper {

    /**
     * 新增
     *
     * @author dorf
     * @date 2021/08/24
     **/
    int insert(ActivityMerchantPo activityMerchantPo);

    /**
     * 更新
     *
     * @param id
     * @param status
     */
    void update(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新
     */
    ActivityMerchantPo getActivityMerchantById(@Param("id") Long id);

    /**
     *
     */
    List<ActivityMerchantPo> getActivityMerchantByCode(@Param("merchantCode") String merchantCode);

    /**
     * 更新对应所有商户的时间
     *
     * @param startTime
     * @param endTime
     * @param id
     */
    void activityTimeUpdate(@Param("inStartTime") Long startTime, @Param("inEndTime") Long endTime,@Param("activityType")Long activityType,@Param("id") Long id);

    ActivityMerchantPo getActivityMerchantPO(@Param("id") Long id, @Param("merchantCode") String merchantCode);

    /**
     * 删除活动关系列表的内容
     *
     * @param merchantCode
     */
    void deleteActivityMerchant(@Param("merchantCode") String merchantCode);

    /**
     * 删除活动关系列表的内容
     *
     * @param merchantCode
     */
    void deleteActivityMerchantPO(@Param("merchantCode") String merchantCode, @Param("activityId") Long activityId);

    void deleteActityMerchant(@Param("list") List<Long> list, @Param("merchantCode") String merchantCode);

    /**
     * 查询所有商户的code
     *
     * @return
     */
    List<String> queryActivityMerchantCode();

    /**
     * 查询所有商户的code
     *
     * @return
     */
    List<String> queryMerchantCode();

    /**
     * 查询所有商户的code
     *
     * @return
     */
    List<ActivityMerchantPo> queryMerchant();

    /**
     * 删除所有活动配置商户
     *
     * @param list
     */
    void deleteAllActityMerchant(@Param("list") List<Long> list);

    /**
     * 删除多余的活动配置
     *
     * @param list
     */
    void deleteActityMerchantByCode(@Param("list") List<String> list);

    /**
     * 查询所有活动的id
     *
     * @return
     */
    List<Long> queryActivityIdsByName();

    List<ActivityMerchantPo> queryMerchantByMerchantCode(@Param("list") List<String> list);


    List<ActivityMerchantPo> queryMerchantByMerchant();

    List<ActivityMerchantPo> queryActivityMerchantById(@Param("id") Long id);

    List<TActivityMerchant> queryActivityMerchant(@Param("merchantCode") String merchantCode);

    /**
     * 更新入口状态
     * @param list
     * @param status
     */
    void updateActivityMerchantEntrance(@Param("list") List<String> list, @Param("status")Integer status);
    /**
     * 更新活动状态
     * @param list
     * @param status
     */
    void updateMerchantActivity(@Param("list") List<String> list, @Param("status")Integer status);

    /**
     * 更新活动状态
     * @param status
     */
    void updateMerchantActivityAll(@Param("status")Integer status);

    /**
     * 删除商户活动
     * @param list
     */
    void deleteMerchantActivity(@Param("list") List<String> list);
    /**
     * 更新入口状态所有
     * @param status
     */
    void updateActivityMerchantEntranceAll(@Param("status") Integer status);
    /**
     * 删除商户活动所有
     */
    void deleteMerchantActivityAll();

   Map getSummerTaskActivity();
}