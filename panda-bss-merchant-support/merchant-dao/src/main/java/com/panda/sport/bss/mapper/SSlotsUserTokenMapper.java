package com.panda.sport.bss.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.SSlotsUserToken;

/**
 * <p>
 * 用户老虎机奖券表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-20
 */
public interface SSlotsUserTokenMapper extends BaseMapper<SSlotsUserToken> {

	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param slotToken
	* @param string
	* @author: Star
	* @Date: 2022-2-20 11:46:04
	*/
	@Update("update s_slots_user_token set token_num=#{slotToken.tokenNum} ,last_update_time = #{slotToken.lastUpdateTime} where user_id =#{slotToken.userId} and token_id =#{slotToken.tokenId}")
	void updateSlotToken(@Param("slotToken")SSlotsUserToken slotToken);

}
