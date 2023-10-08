package com.panda.sport.backup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.AcBonusLogPO;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BackupAcBonusLogPOMapper  extends BaseMapper<AcBonusLogPO> {

    List<Map<String,Object>> getUsableTicket();

    @Select(" SELECT user_id uid ,token_num token FROM `t_ac_user_token` WHERE  user_id = #{userId}")
    Map<String, Object> getOneUsableTicket(String userId);

    @Select("SELECT * FROM `t_user` WHERE uid = #{userId}")
    Map<String, Object> getUser(String userId);
}
