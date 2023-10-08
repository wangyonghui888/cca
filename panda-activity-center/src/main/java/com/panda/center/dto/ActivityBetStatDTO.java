package com.panda.center.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.vo.activity
 * @Description :  活动投注统计
 * @Date: 2021-08-21 11:16
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class ActivityBetStatDTO implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 用户id
	 */
	private List<String> uidList;


	/**
	 * 商户编码列表
	 */
	private List<String> merchantCodeList;


	/**
	 * 搜索开始时间
	 */
	private String startTime;


	/**
	 * 搜索结束时间
	 */
	private String endTime;


	/**
	 * 分页相关
	 */
	private Integer pageNum;

	private Integer pageSize;


	public Integer getPageNum() {
		if (pageNum == null || pageNum <= 0) {
			return 1;
		}
		return pageNum;
	}

	public Integer getPageSize() {
		if(pageSize == null){
			return 20;
		}
		return pageSize;
	}

	public Integer getStartRow(){
		return (getPageNum() - 1) * getPageSize();
	}


	public String getStartTime() {
		return startTime.replace("-","");
	}

	public String getEndTime() {
		return endTime.replace("-","");
	}

	@Override
	public String toString() {
		return "ActivityBetStatVo{" +
		"uidList=" + uidList +
		", merchantCodeList=" + merchantCodeList +
		", startTime='" + startTime + '\'' +
		", endTime='" + endTime + '\'' +
		", pageNum=" + pageNum +
		", pageSize=" + pageSize +
		'}';
	}
}
