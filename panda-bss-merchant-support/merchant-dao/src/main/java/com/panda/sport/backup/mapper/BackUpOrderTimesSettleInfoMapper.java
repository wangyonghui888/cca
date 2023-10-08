package com.panda.sport.backup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.OrderTimesSettleInfoPO;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoReqVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单多次结算账变 Mapper 接口
 * </p>
 *
 * @author amos
 * @since 2022-05-22
 */
public interface BackUpOrderTimesSettleInfoMapper extends BaseMapper<OrderTimesSettleInfoPO> {

    /**
     *  二次结算账变统计
     * @param pageReqVO
     * @param isCompensate  0： 不赔偿  1：赔偿
     * @return
     */
    Map<String, Object> statistic(@Param("pageReqVO") OrderTimesSettleInfoReqVO pageReqVO);

    /**
     * 二次结算账变统计分页
     *
     * @param pageReqVO 分页请求参数
     * @return
     */
    List<OrderTimesSettleInfoPO> pageList(@Param("pageReqVO")  OrderTimesSettleInfoReqVO pageReqVO);

    List<OrderTimesSettleInfoPO>  selectTOrderTimesSettleInfoList(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
