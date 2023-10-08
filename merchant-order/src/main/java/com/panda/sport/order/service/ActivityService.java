package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.order.service
 * @Description :  活动
 * @Date: 2021-08-21 11:12
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface ActivityService {
    /**
     * 获取投注统计列表
     * @param vo
     * @return
     */
    Response getActivityBetStatList(ActivityBetStatDTO vo) ;

    /**
     * 导出excel
     * @param vo
     * @return
     */
    void exportExcel(ActivityBetStatDTO vo, HttpServletResponse response, HttpServletRequest request);


/*

    Result<?> settleActivity2(Long activityId);

    Result<?> settleActivity4(Long activityId, Integer champion, Integer bestPlayer, Integer bestShooter, Integer bestYoungPlayer);
*/

    void executeDailyTask(Long startL, Long endL, Long nowL);

    void executeSumTask();

    // void processTickets(List<TicketMessageVO> allMessages, Long nowL);

    void clearDailyTask();


    Map<String, Object> exportExcelV2(ActivityBetStatDTO vo );

    boolean queryLatestOrderExist(long startL, long endL);

    void executeMatchUserMidTask(long startL, long endL);
}
