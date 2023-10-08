/**
 *
 */
package com.panda.center.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.panda.center.entity.AcBonusLogPO;
import com.panda.center.entity.Member;
import com.panda.center.entity.Merchant;
import com.panda.center.entity.MerchantLog;
import com.panda.center.mapper.activity.AcBonusLogPOMapper;
import com.panda.center.mapper.trader.MemberMapper;
import com.panda.center.mapper.trader.MerchantMapper;
import com.panda.center.result.BonusRecordForm;
import com.panda.center.result.Response;
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

	@Autowired
	MerchantLogServiceImpl merchantLogServiceImpl;

	@Autowired
	MemberMapper memberMapper;
	
	@Autowired
	MerchantMapper merchantMapper;
	

	@Getter @Setter
	public class CurrentUser{
		private Integer userId;
		private String userName;

	}
	public CurrentUser getCurrentUser() {
		 HttpServletRequest request =((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		 Integer userId = SsoUtil.getUserId(request);

		 String userName = request.getHeader("merchantName");

		 CurrentUser c = new CurrentUser();
		 c.userId = userId;
		 c.userName = userName;
		 return c;

	}



	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)DJ奖券补发
	* @param bonusRecordForm
	* @return
	* @author: Star
	* @Date: 2021-10-15 12:14:03
	*/
//	@Transactional(rollbackFor = Exception.class)
	public Object reissueBonus(BonusRecordForm bonusRecordForm) {
		String userId = bonusRecordForm.getUserId();
		if(StringUtils.isBlank(userId)) return Response.returnFail("用户id不能为空 ");

		if(!StringUtils.isNumeric(userId)) return Response.returnFail("用户id格式有误，应全为数字 ");

		String merchantCode = bonusRecordForm.getMerchantCode();
		if(StringUtils.isBlank(merchantCode))  return Response.returnFail(" 商户编号不能为空 ");

		int reBonus = bonusRecordForm.getReBonus();
		if(reBonus <= 0) return Response.returnFail(" 补发奖券数量要大于 0 ");

		Member member = getMember(userId);

		if(member == null) return Response.returnFail(" 用户id = "+userId+" 不存在");

		//String merchantCode2 = member.getMerchantCode();
		//if(!merchantCode.equals(merchantCode2)) return Response.returnFail(" 商户编号 = "+merchantCode+" 不对");

		Map<String, Object> userTokens = getStringObjectMap(userId);

		if(userTokens == null) userTokens = new HashMap<>();

		int reissueBonus = acBonusLogPOMapper.reissueBonus(userId,reBonus);

		if(reissueBonus == 0) {
			//return Response.returnFail(" 用户id = "+userId+"未曾参与过任何活动，无奖券记录。不予以补发");
			//插入记录
			acBonusLogPOMapper.saveBonus(userId,reBonus,System.currentTimeMillis());
		}

		//Map<String,Object> user = acBonusLogPOMapper.getUser(userId);

		//if(user == null)  return Response.returnFail("找不到用户 id= "+userId);

		String userName = member.getAccount();//""+user.get("username");

		AcBonusLogPO entity = new AcBonusLogPO();
		entity.setParentMerchantAccount(member.getParentMerchantAccount());
		entity.setParentMerchantId(member.getParentMerchantId());
		entity.setTopMerchantAccount(member.getTopMerchantAccount());
		entity.setTopMerchantId(member.getTopMerchantId());
		entity.setSortLevel(member.getSortLevel());
		entity.setActName("奖券补发 ");
		entity.setUid(userId);
		entity.setMerchantCode(merchantCode);
		entity.setUserName(userName);
		entity.setTicketNum(reBonus);
		entity.setReceiveTime(System.currentTimeMillis());
		CurrentUser currentUser = getCurrentUser();
		entity.setCreatedBy(currentUser.getUserName());
		entity.setCreatedFrom(2);

		Merchant merchant = merchantMapper.selectById(merchantCode);
		String account = "";
		if(merchant !=null ) account = merchant.getAccount();
		entity.setMerchantAccount(account);
		acBonusLogPOMapper.insert(entity);

		MerchantLog merchantLog = new MerchantLog();
		merchantLog.setDataId("奖券补发 & "+entity.getId());
		long token = 0;//(Long) userTokens.get("token");/
		Object object = userTokens.get("token");
		if(object != null) {
			token = (long) object;
		}
		merchantLog.setBeforeValues(JSONUtil.toJsonStr(Lists.newArrayList("补发前奖券:"+token)));
		token += reBonus;
		merchantLog.setAfterValues(JSONUtil.toJsonStr(Lists.newArrayList("补发后奖券:"+token)));
		merchantLog.setLogTag(1);
		merchantLog.setOperatType(50);
		//merchantLog.setMerchantCode(merchantCode2);
		//merchantLog.setMerchantName(merchantName);
		merchantLog.setOperatField(JSONUtil.toJsonStr(Lists.newArrayList("用户:"+userId+" 补发奖券:"+reBonus+"")));
		merchantLog.setOperatTime(System.currentTimeMillis());
		merchantLog.setPageName("运营管理-奖券派发记录");
		merchantLog.setTypeName("补发奖券");
		merchantLog.setUserId(currentUser.getUserId().longValue());
		merchantLog.setUserName(currentUser.getUserName());
		merchantLogServiceImpl.save(merchantLog);
		return Response.returnSuccess();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
	public Map<String, Object> getStringObjectMap(String userId) {
		Map<String,Object> userTokens = acBonusLogPOMapper.getOneUsableTicket(userId);
		return userTokens;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED,readOnly = true)
	public Member getMember(String userId) {
		Member member = memberMapper.selectById(userId);
		return member;
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
		List<AcBonusLogPO> selectList = acBonusLogPOMapper.selectList(wrapper);
		int ticketAll = 0;
		int reTicket = 0;
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
			int usableTicket = 0;
			List<Map<String,Object>> userTokens = acBonusLogPOMapper.getUsableTicket();
			Map<String,Integer> uMap = new HashMap<>();
			for (Map<String, Object> map : userTokens) {
				Long token = (Long) map.get("token");
				usableTicket += token.intValue();
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

		private int ticketAll;

		private int usableTicket;

		private int reTicket;
	}




}


























