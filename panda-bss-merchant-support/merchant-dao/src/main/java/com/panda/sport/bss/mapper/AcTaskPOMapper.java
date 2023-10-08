package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.bss.AcTaskPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SDailyLuckyBoxNumberPO;
import com.panda.sport.merchant.common.po.bss.SOlympicLuckyboxDictPo;
import com.panda.sport.merchant.common.vo.activity.SOlympicLuckyboxDictVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动任务表 Mapper 接口
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface AcTaskPOMapper extends BaseMapper<AcTaskPO> {

    /**
     * 查找配置
     * @return
     */
    List<SOlympicLuckyboxDictVo> getLuckyBoxDict();

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
