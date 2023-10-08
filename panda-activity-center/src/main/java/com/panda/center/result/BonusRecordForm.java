/**
 *
 */
package com.panda.center.result;

import com.panda.center.entity.AcBonusLogPO;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: BonusRecordForm
 * @Description: TODO
 * @Author: Star
 * @Date: 2021-10-13 11:27:00
 * @version V1.0
 */
@Setter
@Getter
public class BonusRecordForm {

	 /**
     * 开始时间
     */
    private Long beginTime;

    /**
     * 结束时间
     */
    private Long endTime;


    private String merchantCode;

    private String userName;

    private String userId;

    /**
     * 补发奖券数
     */
    private int reBonus;

    /**
     * 来源
     */
    private int from;

    private Page<AcBonusLogPO> page;

    public void setUserName(String userName) {

    	if(StringUtils.isNumeric(userName)) {
    		userId = userName;
    	}else {
    		this.userName = userName;
    	}


    }

}
