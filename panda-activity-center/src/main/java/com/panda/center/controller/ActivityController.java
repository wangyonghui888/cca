package com.panda.center.controller;

import com.alibaba.fastjson.JSON;
import com.panda.center.constant.Constant;
import com.panda.center.result.Response;
import com.panda.center.service.ActivityService;
import com.panda.center.service.MerchantFileService;
import com.panda.center.vo.ActivityBetStatDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.center.controller
 * @Description :  TODO
 * @Date: 2021-12-24 21:23
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/activityCenter/dj/activity/")
public class ActivityController {
	@Autowired
	private ActivityService activityService;

	@Autowired
	private MerchantFileService merchantFileService;

	/**
	 * 查询活动报表数据
	 *
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/getActivityBetStatList")
	public Response getActivityBetStatList(@RequestBody ActivityBetStatDTO vo) {
		log.info("/activity/activity/getActivityBetStatList:" + vo);
		return Response.returnSuccess(activityService.getActivityBetStatList(vo));
	}

	/**
	 * 导出excel
	 *
	 * @param vo
	 * @return
	 */
	@PostMapping(value = "/exportExcel")
	public Response activityCountExportExcel(@RequestBody ActivityBetStatDTO vo, HttpServletRequest request) {
		log.info("/activity/activityCountExportExcel:" + vo);
		String language = request.getHeader("language");
		String operUsername = request.getHeader("merchantName");
		vo.setLanguage(language);
		vo.setOperUsername(operUsername);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("code", "0000");
		resultMap.put("msg", language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "导出任务创建成功,请在文件列表等候下载！"
				: "The exporting task has been created,please click at the Download Task menu to check!");

		try {
			merchantFileService.saveFileTask(language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "dj-活动投注统计导出_" : "Report dj-Center-activityCountExport"
				, null, vo.getOperUsername(), JSON.toJSONString(vo),
				language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ? "运营活动-dj-活动投注统计下载" : "Report Center-activityCountExport", "activityCountExportServiceImpl", null);
		} catch (RuntimeException e) {
			resultMap.put("code", "0002");
			resultMap.put("msg", e.getMessage());
		}

		return Response.returnSuccess(resultMap);
	}
}
