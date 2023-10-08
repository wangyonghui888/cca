package com.panda.sport.bss.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SOlympicLuckyboxDictPo;

public interface OlympicLuckyboxDictMapper extends BaseMapper<SOlympicLuckyboxDictPo> {

	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @author: Star
	* @Date: 2021-11-17 19:29:20
	*/
	@Select("SELECT IFNULL(min(order_num), 0) FROM `s_olympic_luckybox_dict`")
	int selectMinOrder();
	 
}