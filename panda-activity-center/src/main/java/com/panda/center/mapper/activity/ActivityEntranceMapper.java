package com.panda.center.mapper.activity;

import com.panda.center.entity.ActivityConfigPo;
import com.panda.center.entity.ActivityEntrancePo;
import com.panda.center.vo.ActivityEntranceVO;
import com.panda.center.vo.ActivityMerchantVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ActivityEntranceMapper {

    List<ActivityEntrancePo> selectByMerchantName(ActivityEntranceVO activityEntranceVO);

    void update(@Param("id") Long id, @Param("status") Integer status);

    ActivityConfigPo queryActivityConfigPoById(@Param("activityId") Long activityId);

    void saveActivityConfig(ActivityConfigPo activityConfigPo);

    Long selectKeyId();

    ActivityMerchantVO getActivityMerchantById(@Param("id") Long id);

    void activityMechantUpdate(@Param("activityId") Long activityId, @Param("name") String name);

    List<ActivityConfigPo> queryActivityConfigPoList(@Param("id") Long id);

    int selectByMerchantCount(ActivityEntranceVO activityEntranceVO);

    ActivityConfigPo getActivityConfigPO(@Param("id") Long id);

    List<ActivityConfigPo> queryActivityConfigGroupByName();

    List<ActivityConfigPo> queryActivityConfigPoByCode(@Param("merchantCode") String merchantCode);

    void deleteActityConfig(@Param("list") List<Long> list);

    List<ActivityConfigPo> queryActivityConfigPo();

    void updateActivityConfig(@Param("id") Long id, @Param("h5Url") String h5Url, @Param("pcUrl") String pcUrl);

    void deleteAllActityConfig(@Param("list") List<Long> list);

    void insertPojo(@Param("list") List<ActivityConfigPo> updatList);

    void updateActivityMerchant(@Param("list") List<Long> list, @Param("entranceStatus") Integer entranceStatus);

    void updateActivityMerchantStatus(@Param("list") List<Long> list, @Param("status") Integer status);

    void updateActivityMerchantBycode(@Param("merchantCode") String merchantCode, @Param("activityId") Long activityId, @Param("status") Integer status);

    void updateActivityMerchantStatusBycode(@Param("merchantCode") String merchantCode, @Param("activityId") Long activityId, @Param("status") Integer status);

    void updateActivity(@Param("maintainStatus") Integer maintainStatus, @Param("maintainEndTime") Long maintainEndTime,
                        @Param("h5MaintainUrl") String h5MaintainUrl, @Param("pcMaintainUrl") String pcMaintainUrl,
                        @Param("title") String title, @Param("content") String content);

    Map<String, Object> getActivityMaintain();

    void updateActivityStatus(@Param("status") int status,@Param("id") int id);
}