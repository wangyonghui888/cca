package com.panda.sport.merchant.manage.entity.form;

import lombok.Getter;
import lombok.Setter;

/**
 * 盲盒设置记录
 */
@Setter
@Getter
public class BoxListForm {

    /**
     * 主键ID
     */
    Long id;

    /**
     * 页数
     */
    Integer pg;
    /**
     * 页数
     */
    Integer pageNum;

    /**
     * 条数
     */
    Integer size;
    Integer pageSize;
    
    int isUp = 2;
}
