/**
 * 
 */
package com.panda.sport.merchant.manage.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.backup.mapper.BackupSSlotsTokenChangeHistoryMapper;
import com.panda.sport.merchant.common.po.bss.SSlotsTokenChangeHistory;
import com.panda.sport.merchant.manage.entity.form.TokenChangeHistoryVO;

/**
 * @ClassName: ChangeTokenHistoryService
 * @Description: TODO
 * @Author: Star
 * @Date: 2022-2-13 16:18:35
 * @version V1.0
 */
@Service
@SuppressWarnings("all")
public class ChangeTokenHistoryService {

	
	@Autowired
    BackupSSlotsTokenChangeHistoryMapper sSlotsTokenChangeHistoryMapper;
	
	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param tokenChangeHistory
	* @return
	* @author: Star
	* @Date: 2022-2-13 16:22:49
	*/
	public Page<SSlotsTokenChangeHistory> getTokenChangeHistory(TokenChangeHistoryVO bonusRecordForm,boolean paging) {
		 
		 Page<SSlotsTokenChangeHistory> page = bonusRecordForm.getPage();
		 
		 long current = page.getCurrent();
		 
		 long size = page.getSize();
		 
		 QueryWrapper<SSlotsTokenChangeHistory> wrapper = new QueryWrapper<>();
			Long beginTime = bonusRecordForm.getStartTime();
			if(beginTime != null) {
				wrapper.ge("create_time", beginTime);
			}
			
			Long endTime = bonusRecordForm.getEndTime();
			if(endTime != null) {
				wrapper.le("create_time", endTime);
			}
			String userId = bonusRecordForm.getUid();
			if(StringUtils.isNotBlank(userId)) {
				wrapper.eq("uid", userId);
			}
			String userName = bonusRecordForm.getUserName();
			if(StringUtils.isNotBlank(userName)) {
				wrapper.eq("user_name", userName);
			}
			 
			String merchantCode = bonusRecordForm.getMerchantCode();
			if(StringUtils.isNotBlank(merchantCode)) {
				wrapper.eq("merchant_code", merchantCode);
			}
			String tokenType = bonusRecordForm.getTokenType();
			if(StringUtils.isNotBlank(tokenType)) {
				wrapper.eq("slot_ticket_id", tokenType);
			}
			String changeType = bonusRecordForm.getChangeType();
			if(StringUtils.isNotBlank(changeType)) {
				wrapper.eq("change_type", changeType);
			}
			
			wrapper.orderByDesc("create_time");
			
			if(paging) {
				
				PageHelper.startPage((int)current, (int)size);
				
				com.github.pagehelper.Page page1 = (com.github.pagehelper.Page) sSlotsTokenChangeHistoryMapper.selectList(wrapper);
				
				page.setTotal(page1.getTotal());
				
				page.setRecords(page1);
			}else {
				page.setRecords(sSlotsTokenChangeHistoryMapper.selectList(wrapper));
			}
		
		return page;
	}

}
