/**
 * 
 */
package com.panda.sport.merchant.manage.entity.form;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panda.sport.merchant.common.po.bss.SSlotsTokenChangeHistory;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: TokenChangeHistoryVO
 * @Description: TODO
 * @Author: Star
 * @Date: 2022-2-13 16:16:43
 * @version V1.0
 */
@Setter @Getter
public class TokenChangeHistoryVO {

	private Page<SSlotsTokenChangeHistory> page;

	/**
     * 商户ID
     */
    private String merchantCode;

    private String userName;
    /**
     * 用户uid
     */
    private String uid;

    /**
     * 账变类型 1:任务奖励，2:盲盒消耗，3:合成奖励，4:游戏消耗，5:合成返还，6:合成消耗，7:提升消耗，8:道具奖励，9:系统补发
     */
    private String changeType;

    /**
     * 奖券类型
     */
    private String tokenType;

    
    private Long startTime;
   
    private Long endTime;

    public void setUserName(String userName) {

    	if(StringUtils.isNumeric(userName)) {
    		uid = userName;	
    	}else {
    		this.userName = userName;
    	}
    	
    	
    }
}
