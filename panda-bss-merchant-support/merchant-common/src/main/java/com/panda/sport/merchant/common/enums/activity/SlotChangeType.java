package com.panda.sport.merchant.common.enums.activity;
/**
 * 
 */
import lombok.Getter;

/**
 * @ClassName: SlotChangeType
 * @Description: TODO
 * @Author: Star
 * @Date: 2022-2-13 17:20:05
 * @version V1.0
 */
@Getter
public enum SlotChangeType {
	/**
	 * 任务奖励
	 */
	task_reward(1,"任务奖励"),
	/**
	 *盲盒消耗
	 */
	lucky_box(2,"盲盒消耗"),
	
	/**合成奖励*/
	synthetic_reward(3,"合成奖励"),
	 
	/**游戏消耗*/
	game_consumption(4,"游戏消耗"),
	
	/**合成返还*/
	synthetic_return(5,"合成返还"),
	
	/**合成消耗*/
	synthetic_consumption(6,"合成消耗"),
	
	/**提升消耗*/
	increased_consumption(7,"提升消耗"),
	
	/**道具奖励*/
	props_reward(8,"道具奖励"),
	/**道具重置消耗*/
	props_reset_consumption(9,"道具重置消耗"),
	
	/**系统补发*/
	system_Reissue(10,"系统补发");
	
	SlotChangeType(int code,String ds){
		this.code = code;
		describe = ds;
	}
	 int code;
     String describe;
	
     
     public static SlotChangeType getSlotChangeType(int code) {
    	 for (SlotChangeType t : values()) {
    		if(t.code == code) return t;
		}
    	return null;
     }

     
}














