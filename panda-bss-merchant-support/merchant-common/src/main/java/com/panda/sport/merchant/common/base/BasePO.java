package com.panda.sport.merchant.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 
 * @author :  christion
 * @Project Name :com.panda.sports.bss.strategy.base
 * @Package Name :
 * @Description :  所有持久层的最顶层基类,实现了序列号克隆接口。为了规范bss数据服务接口中的实体类
 * @Date: 2019-08-31 下午17:30:55
 */
@Data
@ToString
public abstract class BasePO<T extends BasePO<?>> implements Serializable,Cloneable,Comparable<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2819257632275334581L;

    /** 创建用户 */
    @FieldExplain("创建用户")
    @TableField("CREATE_USER")
    protected String createUser;

    /** 创建时间 从1970开始的以毫秒数为单位时间戳 */
    @FieldExplain("创建时间")
    @TableField("CREATE_TIME")
    protected Long createTime;

    /** 修改用户 */
    @FieldExplain("修改用户")
    @TableField("MODIFY_USER")
    protected String modifyUser;

    /** 修改时间 从1970开始的以毫秒数为单位时间戳 */
    @FieldExplain("修改时间")
    @TableField("MODIFY_TIME")
    protected Long modifyTime;

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
