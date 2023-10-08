package com.panda.center.mapper.activity;



import com.panda.center.vo.ActivityBetStatDTO;
import com.panda.center.vo.ActivityBetStatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReport4Mapper {
    /**
     *获取记录总数
     * @param vo
     * @return
     */
    Integer getActivityBetStatCount(ActivityBetStatDTO vo);
    /**
     * 获取投注统计列表
     * @param vo
     * @return
     */
    List<ActivityBetStatVO> getActivityBetStatList(ActivityBetStatDTO vo);


}
