package com.panda.sport.merchant.common.base;

import lombok.Data;

/**
 * @author :  sklee
 * @Project Name :
 * @Package Name :
 * @Description :
 * @Date: 2019-09-10 17:26
 */
@Data
public class BasePageVO extends BaseVO{

    /**
     * 要查询表记录的起始行
     */
    private Integer startRow;

    /**
     * 每页显示大小
     */
    private Integer pageSize;


    /** 创建用户 */
    protected Long createUser;

    /** 创建时间 从1970开始的以毫秒数为单位时间戳 */
    protected Long createTime;

    /** 修改用户 */
    protected Long modifyUser;

    /** 修改时间 从1970开始的以毫秒数为单位时间戳 */
    protected Long modifyTime;

}
