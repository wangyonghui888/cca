package com.panda.sport.merchant.manage.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.SlotTicketDictMapper;
import com.panda.sport.merchant.common.enums.activity.SlotChangeType;
import com.panda.sport.merchant.common.po.bss.SSlotsTokenChangeHistory;
import com.panda.sport.merchant.common.po.bss.SlotTicketDict;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.TokenChangeHistoryVO;
import com.panda.sport.merchant.manage.service.impl.ChangeTokenHistoryService;
import com.panda.sport.merchant.manage.util.ExportUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户奖券变更历史记录 前端控制器
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-13
 */
@RestController
@Slf4j
@RequestMapping("/manage/tokenChangeHistory/")
public class ChangeTokenHistoryController {

	@Autowired
	ChangeTokenHistoryService changeTokenHistoryService;
	
	@Autowired
	SlotTicketDictMapper slotTicketDictMapper;
	
	
	@PostMapping("getTokenChangeHistory")
	public Response<?> getTokenChangeHistory(@RequestBody TokenChangeHistoryVO TokenChangeHistory){
		
		try {
			return Response.returnSuccess(changeTokenHistoryService.getTokenChangeHistory(TokenChangeHistory,true));
		} catch (Exception e) {
			log.error(" 查询活动账变异常： ",e);
			return Response.returnFail(" 查询活动账变异常： "+ExceptionUtil.stacktraceToString(e));
		}
		
	}
	
	
	@PostMapping("/exportChangeHistory")
    public void jackpotRecordExport(@RequestBody TokenChangeHistoryVO TokenChangeHistory, HttpServletResponse response) {
        
        Page<SSlotsTokenChangeHistory> page = changeTokenHistoryService.getTokenChangeHistory(TokenChangeHistory,false);
        
        LinkedHashMap<String, String> headers = Maps.newLinkedHashMap();
        headers.put("1", "账变时间");
        headers.put("2", "商户编号");
        headers.put("3", "用户名");
        headers.put("4", "用户ID");
        headers.put("5", "账变类型");
        headers.put("6", "奖券类型");
        headers.put("7", "账变奖券数");
        headers.put("8", "账变前奖券");
        headers.put("9", "账变后奖券");
        headers.put("10", "账变结果");
        headers.put("11", "订单号");
        headers.put("12", "操作员");
        
        List<LinkedHashMap<String, Object>> mapList = Lists.newArrayList();
        for (SSlotsTokenChangeHistory records : page.getRecords()) {
            LinkedHashMap<String, Object> datas = Maps.newLinkedHashMap();
            datas.put("1", DateUtil.formatDateTime(new Date(records.getCreateTime())));
            datas.put("2", records.getMerchantCode());
            datas.put("3", records.getUserName());
            datas.put("4", records.getUid().toString().concat("\t"));
            datas.put("5",SlotChangeType.getSlotChangeType(records.getChangeType()).getDescribe());
            datas.put("6", records.getTokenName());
            datas.put("7", records.getChangeTokenStr());
            datas.put("8", records.getBeforeToken());
            datas.put("9", records.getAfterToken());
            datas.put("10", records.getChangeResult());
            datas.put("11", records.getId().toString().concat("\t"));
            datas.put("12", records.getCreatedBy());
            mapList.add(datas);
        }
        String fileName = DateUtil.formatDate(new Date()) + "_活动账变记录.csv";
        ExportUtils.browserDownload(fileName, headers, mapList, response);
    }
	
	
	
	/**
	 * 账变类型
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @return
	* @author: Star
	* @Date: 2022-2-13 17:36:26
	 */
	@RequestMapping("getSlotChangeType")
	public Response<?> getSlotChangeType(){
		
		List<Map<String,Object>> list = new ArrayList<>();
		
		SlotChangeType[] values = SlotChangeType.values();
		for (SlotChangeType slotChangeType : values) {
			Map<String, Object> e = new HashMap<>();
			e.put("changeType", slotChangeType.getCode());
			e.put("name", slotChangeType.getDescribe());
			list.add(e);
		}
		return Response.returnSuccess(list);
	}
	 
	
	/**
	 * 奖券类型
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @return
	* @author: Star
	* @Date: 2022-2-13 17:36:16
	 */
	@RequestMapping("getTokenType")
	public Response<?> getTokenType(){
		List<Map<String,Object>> list = new ArrayList<>();
		List<SlotTicketDict> tickets = slotTicketDictMapper.selectList(null);
		for (SlotTicketDict slotTicketDict : tickets) {
			Map<String, Object> e = new HashMap<>();
			e.put("tokenType", slotTicketDict.getId());
			e.put("name", slotTicketDict.getTicketName());
			list.add(e);
		}
		
		Map<String, Object> luckyToken = new HashMap<>();
		luckyToken.put("tokenType", 0);
		luckyToken.put("name","幸运奖券");
		list.add(luckyToken );
		
		
		return Response.returnSuccess(list);
		
	}
}

























