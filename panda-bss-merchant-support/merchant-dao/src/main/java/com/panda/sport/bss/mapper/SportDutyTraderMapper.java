package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SportDutyTraderPO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author javier
 * 操盘手排班
 */
@Repository
public interface SportDutyTraderMapper extends BaseMapper<SportDutyTraderPO> {

    int batchInsert(List<SportDutyTraderPO> list);

    @Delete("delete from tybss_report.s_sport_duty_trader where 1=1")
    int deleteAll();
}
