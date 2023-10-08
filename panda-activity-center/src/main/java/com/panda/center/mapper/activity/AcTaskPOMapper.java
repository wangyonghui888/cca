package com.panda.center.mapper.activity;

import java.util.List;

import com.panda.center.entity.AcTask;
import com.panda.center.entity.SOlympicLuckyboxDictPo;
import com.panda.center.result.SDailyLuckyBoxNumberPO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 活动任务表 Mapper 接口
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface AcTaskPOMapper extends BaseMapper<AcTask> {

    /**
     * 查找配置
     * @return
     */
    //List<SOlympicLuckyboxDictVo> getLuckyBoxDict();

    /**
     * 更新
     * @param sOlympicLuckyboxDict
     * @return
     */
    Integer boxUpdate(SOlympicLuckyboxDictPo sOlympicLuckyboxDict);


    /**
     * 发现
     * @param id
     * @return
     */
    SOlympicLuckyboxDictPo findBox(@Param("id") Long id);


    /**
     * 查找每天的配置
     * @return
     */
    List<SDailyLuckyBoxNumberPO> getDailyBox();

    /**
     * 更新每日
     * @param sDailyLuckyBoxNumberPO
     * @return
     */
    Integer dailyUpdate(SDailyLuckyBoxNumberPO sDailyLuckyBoxNumberPO);

    Integer getCurMaxNo(@Param("actId") Integer actId);
}
