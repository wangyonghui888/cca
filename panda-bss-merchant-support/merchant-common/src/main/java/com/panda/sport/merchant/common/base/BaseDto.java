package com.panda.sport.merchant.common.base;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description :  定义bss所有视图的最顶层基类,实现了序列号克隆接口。为了规范bss数据服务接口中的实体类
 * @Date: 2019-08-29 下午6:01:55
 */
@Data
public abstract class BaseDto<T extends BaseDto<?>> implements Serializable,Cloneable,Comparable<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2819257632275334581L;

	/**
     * 默认实现了排序的接口
     * @param o
     * @return
     */
    @Override
    public int compareTo(T o) {
        return 0;
    }

}
