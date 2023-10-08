/**
 *
 */
package com.panda.center.controller;

import java.util.List;
import java.util.Map;

import com.panda.center.result.BonusRecordForm;
import com.panda.center.result.Response;
import com.panda.center.mapper.activity.AcBonusLogPOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panda.center.service.impl.BonusRecordService;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: BonusRecordController
 * @Description: TODO
 * @Author: Star
 * @Date: 2021-10-13 13:06:32
 * @version V1.0
 */
@RestController
@RequestMapping("/manage/dj/bonusRecord")
@Slf4j
public class BonusRecordController {

	@Autowired
	BonusRecordService bonusRecordService;

	@RequestMapping("/recordList")
	public Response<?> recordList(@RequestBody BonusRecordForm bonusRecordForm) {
		try {
			return Response.returnSuccess(bonusRecordService.recordList(bonusRecordForm));
		} catch (Exception e) {
			log.error(" 奖券记录查询异常： ",e);
			return Response.returnFail(" 奖券记录查询异常： "+ExceptionUtil.stacktraceToString(e));
		}
    }
	@RequestMapping("/reissueBonus")
	public Response<?> reissueBonus(@RequestBody BonusRecordForm bonusRecordForm) {
		try {
			return (Response<?>) bonusRecordService.reissueBonus(bonusRecordForm) ;
		} catch (Exception e) {
			log.error("补发奖券异常： ",e);
			return Response.returnFail("补发奖券异常： "+ExceptionUtil.stacktraceToString(e));
		}
	}

	@Autowired
    AcBonusLogPOMapper acBonusLogPOMapper;

	@RequestMapping("/test")
	public Response<?> reissueBonus() {
		try {
			List<Map<String, Object>> usableTicket = acBonusLogPOMapper.getUsableTicket();
			for (Map<String, Object> map : usableTicket) {
				log.info("测试查询日志：{}",map);
			}
			return Response.returnSuccess(usableTicket);
		} catch (Exception e) {
			log.error("补发奖券异常： ",e);
			return Response.returnFail("补发奖券异常： "+ExceptionUtil.stacktraceToString(e));
		}
	}
}
