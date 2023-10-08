package com.panda.sport.order.controller;


import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.order.service.ActivityService;
import com.panda.sport.order.service.UserAccountClearDataService;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.order.controller
 * @Description :  活动
 * @Date: 2021-08-21 11:10
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/order/activity/")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

	@Autowired
	private UserAccountClearDataService userAccountClearDataService;


	/**
	 * 查询活动报表数据
	 *
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/getActivityBetStatList")
	@AuthRequiredPermission("Report:activity:getActivityBetStatList")
	public Response getActivityBetStatList(@RequestBody ActivityBetStatDTO vo) {
		log.info("/report/activity/getActivityBetStatList:" + vo);
		return activityService.getActivityBetStatList(vo);
	}

	/**
	 * 导出excel
	 *
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/exportExcel")
	@AuthRequiredPermission("Report:activity:exportExcel")
	public Response activityCountExportExcel(@RequestBody ActivityBetStatDTO vo, HttpServletRequest request) {
		log.info("/report/activity/activityCountExportExcel:" + vo);
		String language = request.getHeader("language");
		String operUsername = request.getHeader("merchantName");
		vo.setLanguage(language);
		vo.setOperUsername(operUsername);
		Map<String, Object> resultMap = activityService.exportExcelV2(vo);

		return Response.returnSuccess(resultMap);
	}

	@GetMapping(value = "/executeSumTask")
	public void executeSumTask(HttpServletResponse response) {
		log.info("/report/activity/executeSumTask:");
		activityService.executeSumTask();
	}

	@GetMapping("executeDailyTask")
	public void executeDailyTask(@RequestParam(value = "time")Long time){
		Date now = new Date();
		long endL = now.getTime();
		long startL = time;//now.getTime() - 1000 * 60 * 60 * 24 * 2;
		Long nowL = Long.parseLong(DateFormatUtils.format(now, "yyyyMMdd"));
		activityService.executeDailyTask(startL, endL, nowL);
	}
}

