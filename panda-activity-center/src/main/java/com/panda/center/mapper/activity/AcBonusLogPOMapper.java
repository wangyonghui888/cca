package com.panda.center.mapper.activity;

import java.util.List;
import java.util.Map;

import com.panda.center.entity.AcBonusLogPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 优惠券领取日志表 Mapper 接口
 * </p>
 *
 * @author baylee
 * @since 2021-08-26
 */
public interface AcBonusLogPOMapper extends BaseMapper<AcBonusLogPO> {

	List<Map<String,Object>> getUsableTicket();

	@Select(" SELECT user_id uid ,token_num token FROM `t_ac_user_token` WHERE  user_id = #{userId}")
	Map<String, Object> getOneUsableTicket(String userId);

	@Update("UPDATE t_ac_user_token SET token_num = token_num + #{reBonus} WHERE user_id = #{userId}")
	int reissueBonus(@Param("userId")String userId, @Param("reBonus")int reBonus);

	@Insert("INSERT INTO `t_ac_user_token` (`user_id`, `token_num`, `cost_token_num`, `create_time`, `update_time`, `last_update_time`) VALUES ( #{userId}, #{tokenNum}, 0, #{time},#{time},#{time} )")
	void saveBonus(@Param("userId")String userId, @Param("tokenNum")int reBonus, @Param("time")long currentTimeMillis);

	@Select("SELECT * FROM `t_user` WHERE uid = #{userId}")
	Map<String, Object> getUser(String userId);

}
