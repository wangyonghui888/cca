/**
 * 
 */
package com.panda.sport.merchant.manage.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panda.sport.backup.mapper.BackupAcBonusLogPOMapper;
import com.panda.sport.backup.mapper.BackupSSlotsTokenChangeHistoryMapper;
import com.panda.sport.backup.mapper.AcBonusLogPOMapper;
import com.panda.sport.bss.mapper.AcBonusMapper;
import com.panda.sport.bss.mapper.SSlotsUserTokenMapper;
import com.panda.sport.bss.mapper.SlotTicketDictMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.activity.SlotChangeType;
import com.panda.sport.merchant.common.po.bss.AcBonusLogPO;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import com.panda.sport.merchant.common.po.bss.AcTaskPO;
import com.panda.sport.merchant.common.po.bss.SSlotsTokenChangeHistory;
import com.panda.sport.merchant.common.po.bss.SSlotsUserToken;
import com.panda.sport.merchant.common.po.bss.SlotTicketDict;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.BonusRecordForm;
import com.panda.sport.merchant.manage.service.IAcTaskService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.IDUtils;
import com.panda.sports.auth.util.SsoUtil;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: BonusRecordService
 * @Description: TODO
 * @Author: Star
 * @Date: 2021-10-13 11:15:18
 * @version V1.0
 */
@Service
public class BonusRecordService {

	@Autowired
    AcBonusLogPOMapper acBonusLogPOMapper;

	/**
	 *分库以后到备份库去查询
	 */
	@Autowired
	BackupAcBonusLogPOMapper backupAcBonusLogPOMapper;
	
	
	@Autowired
	TUserMapper tUserMapper;
	
	@Autowired
	LoginUserService loginUserService;
	
	 @Autowired
	 private MerchantLogService merchantLogService;
	
	 @Resource
	 private IAcTaskService acTaskService;
	 
	 @Resource
	 private AcBonusMapper acBonusMapper;

	 @Autowired
	 SlotTicketDictMapper slotTicketDictMapper;
	 
	 @Autowired
	 SSlotsUserTokenMapper sSlotsUserTokenMapper;
	 
	 @Autowired
     BackupSSlotsTokenChangeHistoryMapper sSlotsTokenChangeHistoryMapper;
	 
	@Getter @Setter
	public class CurrentUser{
		private Integer userId;
		private String userName;
		
	}
	public CurrentUser getCurrentUser() {
		 HttpServletRequest request =
		            ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		 Integer userId = SsoUtil.getUserId(request);
		 CurrentUser c = new CurrentUser();
		 c.userId = userId;
		 c.userName = loginUserService.getLoginUser(userId);
		 return c;
		
	}
	
	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param bonusRecordForm
	* @return
	* @author: Star
	* @Date: 2021-10-15 12:14:03
	*/
	@Transactional(rollbackFor = Exception.class)
	public Object reissueBonus(BonusRecordForm bonusRecordForm) {
		String userId = bonusRecordForm.getUserId();
		if(StringUtils.isBlank(userId)) return Response.returnFail("用户id不能为空 ");
		
		if(!StringUtils.isNumeric(userId)) return Response.returnFail("用户id格式有误，应全为数字 ");
		
		String merchantCode = bonusRecordForm.getMerchantCode();
		if(StringUtils.isBlank(merchantCode))  return Response.returnFail(" 商户编号不能为空 ");
		
		int reBonus = bonusRecordForm.getReBonus();
		
		if(bonusRecordForm.getTaskId() != 0 && reBonus <= 0) return Response.returnFail(" 补发奖券数量要大于 0 ");
		
		UserPO userInfo = tUserMapper.getUserInfo(Long.valueOf(userId));
		
		if(userInfo == null) return Response.returnFail(" 用户id = "+userId+" 不存在");
		
		String merchantCode2 = userInfo.getMerchantCode();
		
		if(!merchantCode.equals(merchantCode2)) return Response.returnFail(" 商户编号 = "+merchantCode+" 不对");
		
		SlotTicketDict ticketDict = slotTicketDictMapper.selectById(bonusRecordForm.getTokenType());
		
		if(ticketDict == null)  return Response.returnFail("不支持的奖券类型");
		
		Integer ticketType = ticketDict.getTicketType();
		
		if(ticketType != 1) {//老虎机奖券
			reissueBonus4Slot(userInfo,bonusRecordForm,ticketDict);
			return Response.returnSuccess();
		}
		Map<String,Object> userTokens = acBonusLogPOMapper.getOneUsableTicket(userId);
		long taskId = bonusRecordForm.getTaskId();
		String taskName = "";
		Integer actId = null;
		if(taskId != 0) {
			//当日重复补发校验
			AcBonusPO bonusPO = acBonusMapper.selectOne(new QueryWrapper<AcBonusPO>().eq("uid", userId).eq("task_id", taskId));
			if (bonusPO != null && Constant.INT_1.equals(bonusPO.getBonusType())) {
				return Response.returnFail("当前任务今日已完成，请勿重复补发!");
			}
			AcTaskPO taskPo = acTaskService.getById(taskId);		
			
			if(taskPo == null) return Response.returnFail(" 任务id= "+taskId+" 找不到对应的任务 ");
			reBonus = getBonusFromTask(userInfo, taskId, taskPo);
			taskName = taskPo.getTaskName();
			actId = taskPo.getActId();
		}
		
		int reissueBonus = acBonusLogPOMapper.reissueBonus(userId,reBonus);
		
		if(reissueBonus == 0) {
			//插入记录
			acBonusLogPOMapper.saveBonus(userId,reBonus,System.currentTimeMillis());
		}
		
		
		String userName = userInfo.getUsername();//""+user.get("username");	
		 
		AcBonusLogPO entity = new AcBonusLogPO();
		entity.setId(IDUtils.getId("activity"));
		entity.setActName("奖券补发 ");
		entity.setUid(userId);
		entity.setMerchantCode(merchantCode);
		entity.setUserName(userName);
		entity.setTicketNum(reBonus);
		entity.setReceiveTime(System.currentTimeMillis());
		CurrentUser currentUser = getCurrentUser();
		entity.setCreatedBy(currentUser.getUserName());
		entity.setCreatedFrom(2);
		entity.setTaskId(taskId);
		entity.setTaskName(taskName);
		if (actId != null) {
			entity.setActId(Long.valueOf(actId));
		}
		acBonusLogPOMapper.insert(entity);
		
		
		if(userTokens == null) userTokens = new HashMap<>();
		
		MerchantLogPO merchantLog = new MerchantLogPO();
		merchantLog.setId(IDUtils.getId("activity"));
		merchantLog.setDataId("奖券补发 & "+entity.getId());
		long beforeToken = 0;//(Long) userTokens.get("token");/
		Object object = userTokens.get("token");
		if(object != null) {
			beforeToken = (long) object;
		}
		merchantLog.setBeforeValues(JSONUtil.toJsonStr(Lists.newArrayList("补发前奖券:"+beforeToken)));
		 
		insertHistory(ticketDict, userInfo,beforeToken, reBonus,  SlotChangeType.system_Reissue);
		
		merchantLog.setAfterValues(JSONUtil.toJsonStr(Lists.newArrayList("补发后奖券:"+(beforeToken+reBonus))));
		merchantLog.setLogTag(1);
		merchantLog.setOperatType(50);
		merchantLog.setOperatField(JSONUtil.toJsonStr(Lists.newArrayList("用户:"+userId+" 补发奖券:"+reBonus+"")));
		merchantLog.setOperatTime(System.currentTimeMillis());
		merchantLog.setPageName("运营管理-奖券派发记录");
		merchantLog.setTypeName("补发奖券");
		merchantLog.setUserId(currentUser.getUserId().toString());
		merchantLog.setUserName(currentUser.getUserName());
		merchantLogService.saveLog(merchantLog);
		return Response.returnSuccess();
	}

	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param bonusRecordForm
	* @param ticketDict
	* @author: Star
	* @Date: 2022-2-20 11:29:20
	*/
	private void reissueBonus4Slot(UserPO userPO,BonusRecordForm bonusRecordForm, SlotTicketDict ticketDict) {
		String userId = bonusRecordForm.getUserId();
		QueryWrapper<SSlotsUserToken> queryWrapper = new QueryWrapper<SSlotsUserToken>().eq("user_id", userId).eq("token_id", ticketDict.getId());
		SSlotsUserToken slotToken = sSlotsUserTokenMapper.selectOne(queryWrapper);
		boolean save = false;
		if(slotToken == null) {
			save = true;
			slotToken = new SSlotsUserToken();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			slotToken.setCreateTime(fmt.format(new Date()));
			slotToken.setTokenId(ticketDict.getId());
			slotToken.setUserId(Long.valueOf(userId));
			slotToken.setTokenNum(0L);
		}
		
		int changeToken = bonusRecordForm.getReBonus();
		Long beforeToken = slotToken.getTokenNum();
		slotToken.setLastUpdateTime(System.currentTimeMillis());
		slotToken.setTokenNum(beforeToken+changeToken);
		if(save)
			sSlotsUserTokenMapper.insert(slotToken);
		else 
			sSlotsUserTokenMapper.updateSlotToken(slotToken);
		//添加幸运奖账变
		insertHistory(ticketDict, userPO, beforeToken, changeToken, SlotChangeType.system_Reissue);
	}

	 
	private void insertHistory(SlotTicketDict ticketDict, UserPO userPO, long beforeToken,long changeToken,SlotChangeType SlotChangeType) {
		SSlotsTokenChangeHistory history = new SSlotsTokenChangeHistory();
		history.setId(IDUtils.getId("activity"));
		history.setBeforeToken(beforeToken);
		history.setAfterToken(beforeToken + changeToken);
		history.setChangeType(SlotChangeType.getCode());
		history.setChangeToken(changeToken);
		CurrentUser currentUser = getCurrentUser();
		history.setCreatedBy(currentUser.getUserName());
		history.setCreateTime(System.currentTimeMillis());
		history.setMerchantCode(userPO.getMerchantCode());
		history.setTokenType(ticketDict.getId());
		history.setTokenName(ticketDict.getTicketName());
		history.setUid(userPO.getUserId());
		history.setUserName(userPO.getUsername());
		//添加账变记录
		sSlotsTokenChangeHistoryMapper.insert(history);
	}
	 
	private int getBonusFromTask(UserPO userInfo, long taskId, AcTaskPO taskPo) {
		QueryWrapper<AcBonusPO> queryWrapper = new QueryWrapper<AcBonusPO>();
		queryWrapper.eq("uid", userInfo.getUserId());
		queryWrapper.eq("task_id", taskId);
		AcBonusPO bonusPO = acBonusMapper.selectOne(queryWrapper);
		Long lastUpdate = null;
		//每日任务
		if (taskPo.getActId() == 1) {
			lastUpdate = Long.parseLong(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		}
		//成长任务
		if (taskPo.getActId() == 2) {
			//周任务
			if (taskPo.getConditionId() == 2) {
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.setFirstDayOfWeek(Calendar.MONDAY);
				String weekStr = c.get(Calendar.YEAR) + "" + c.get(Calendar.WEEK_OF_YEAR);
				lastUpdate = Long.parseLong(weekStr);
			}
			//月任务
			if (taskPo.getConditionId() == 1 || taskPo.getConditionId() == 3) {
				lastUpdate = Long.parseLong(DateFormatUtils.format(new Date(), "yyyyMM"));
			}
		}
		if(bonusPO == null) {
			AcBonusPO entity = new AcBonusPO();
			entity.setId(IDUtils.getId("activity"));
			entity.setActId(taskPo.getActId().longValue());
			entity.setActName(taskPo.getActName());
			entity.setBonusType(1);
			entity.setCreateTime(System.currentTimeMillis());
			entity.setUpdateTime(System.currentTimeMillis());
			entity.setTaskId(taskId);
			entity.setTaskName(taskPo.getTaskName());
			entity.setTaskType(taskPo.getType());
			entity.setUid(userInfo.getUserId());
			entity.setUserName(userInfo.getUsername());
			entity.setTicketNum(taskPo.getTicketNum());
			entity.setRemark("奖券补发");
			entity.setLastUpdate(lastUpdate);
			acBonusMapper.insert(entity);
		}else {
			bonusPO.setTicketNum(taskPo.getTicketNum());
			bonusPO.setBonusType(1);
			bonusPO.setLastUpdate(lastUpdate);
			acBonusMapper.updateById(bonusPO);
		}
		return taskPo.getTicketNum();
	}
	
	
	
	public Object recordList(BonusRecordForm bonusRecordForm){
		 
		QueryWrapper<AcBonusLogPO> wrapper = new QueryWrapper<>();
		Long beginTime = bonusRecordForm.getBeginTime();
		if(beginTime != null) {
			wrapper.ge("receive_time", beginTime);
		}
		
		Long endTime = bonusRecordForm.getEndTime();
		if(endTime != null) {
			wrapper.le("receive_time", endTime);
		}
		String userId = bonusRecordForm.getUserId();
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
		int from = bonusRecordForm.getFrom();
		
		if(from != 0) wrapper.eq("created_from", from);
		 
		//Integer selectCount = acBonusLogPOMapper.selectCount(wrapper);
		Page<AcBonusLogPO> page = bonusRecordForm.getPage();
		//page.setTotal(selectCount);
		//IPage<AcBonusLogPO> selectPage = acBonusLogPOMapper.selectPage(page, wrapper);
		//page.setRecords(selectList);
		List<AcBonusLogPO> selectList = backupAcBonusLogPOMapper.selectList(wrapper);
		long ticketAll = 0;
		long reTicket = 0;
		for (AcBonusLogPO acBonusLogPO : selectList) {
			ticketAll += acBonusLogPO.getTicketNum();
			if(acBonusLogPO.getCreatedFrom() != 1) {
				reTicket += acBonusLogPO.getTicketNum();
			}
		}
		//IPage<AcBonusLogPO> selectPage = acBonusLogPOMapper.selectPage(page, wrapper);
		MyPage myPage = new MyPage();
		BeanUtils.copyProperties(page, myPage);
		int count = selectList.size();
		if(count > 0) {
			myPage.setTotal(count);
			int start = (int)page.offset();
			int size = (int) page.getSize(); 
			int end = start + size;
			if(end > count) end = count;
			List<AcBonusLogPO> subList = selectList.subList(start, end);
			myPage.setRecords(subList);
			myPage.setTicketAll(ticketAll);
			myPage.setReTicket(reTicket);
			long usableTicket = 0;
			
			List<Map<String,Object>> userTokens = backupAcBonusLogPOMapper.getUsableTicket();
			Map<String,Integer> uMap = new HashMap<>();
			for (Map<String, Object> map : userTokens) {
				Long token = (Long) map.get("token");
				usableTicket += token;
				uMap.put(map.get("uid").toString(), token.intValue());
			}
			myPage.setUsableTicket(usableTicket);
			for (AcBonusLogPO acPO : subList) {
				Integer integer = uMap.get(acPO.getUid());
				if(integer != null)
					acPO.setTicketNow(integer);
			}
			
		}
		return myPage;
	}
	 
	@Setter
	@Getter
	public class MyPage extends Page<AcBonusLogPO>{
		private static final long serialVersionUID = 1L;
		
		private long ticketAll;
		
		private long usableTicket;
		
		private long reTicket;
	}

	
	
	
}


























