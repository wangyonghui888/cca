/**
 * 
 */
package com.panda.sport.merchant.manage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panda.sport.merchant.common.po.bss.AcTaskPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.BonusRecordForm;
import com.panda.sport.merchant.manage.feign.BonusRecordClient;
import com.panda.sport.merchant.manage.service.IAcTaskService;
import com.panda.sport.merchant.manage.service.impl.BonusRecordService;
import com.panda.sports.auth.permission.AuthRequiredPermission;

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
@RequestMapping("/manage/bonusRecord")
@Slf4j
public class BonusRecordController {

	@Autowired
	BonusRecordService bonusRecordService;

	@Autowired
	BonusRecordClient bonusRecordClient;
	
//	@Autowired
//	AcBonusLogPOMapper acBonusLogPOMapper;

	 @Resource
	 private IAcTaskService acTaskService;
	
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
	@AuthRequiredPermission("merchant:manage:bonusRecord:reissueBonus")
	public Response<?> reissueBonus(@RequestBody BonusRecordForm bonusRecordForm,HttpServletRequest request) {
		try {
			
    		 bonusRecordForm.setAdminName(request.getHeader("merchantName"));
    		 
			return (Response<?>) bonusRecordClient.reissueBonus(bonusRecordForm,bonusRecordForm.getMerchantCode()) ;
		} catch (Exception e) {
			log.error("补发奖券异常： ",e);
			return Response.returnFail("补发奖券异常： "+ExceptionUtil.stacktraceToString(e));
		}
	}
	@RequestMapping("/getBonusByTaskId")
	public Response<?> getBonusByTaskId(BonusRecordForm bonusRecordForm) {
		try {
			
			AcTaskPO taskPO = acTaskService.getById(bonusRecordForm.getTaskId());
			
			if(taskPO == null) Response.returnFail(" 任务id= "+bonusRecordForm.getTaskId()+" 找不到对应的任务 ");
			
			return Response.returnSuccess(taskPO);
			
		} catch (Exception e) {
			log.error("补发奖券异常： ",e);
			return Response.returnFail("补发奖券异常： "+ExceptionUtil.stacktraceToString(e));
		}
	}


//
//	@RequestMapping("/test")
//	public Response<?> reissueBonus() {
//		try {
//			List<Map<String, Object>> usableTicket = acBonusLogPOMapper.getUsableTicket();
//			for (Map<String, Object> map : usableTicket) {
//				log.info("测试查询日志：{}",map);
//			}
//			return Response.returnSuccess(usableTicket);
//		} catch (Exception e) {
//			log.error("补发奖券异常： ",e);
//			return Response.returnFail("补发奖券异常： "+ExceptionUtil.stacktraceToString(e));
//		}
//	}

}
