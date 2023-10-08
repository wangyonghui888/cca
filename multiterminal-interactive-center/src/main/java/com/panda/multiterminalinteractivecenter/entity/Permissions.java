package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.entity
 * @Description :  TODO
 * @Date: 2022-03-13 13:29:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@TableName("m_permissions")
public class Permissions {

    private Long id;

    private String name;

    private Long pid;

    private Long sort;

    private String url;

    @TableField(exist = false)
    private Integer status = 0;
}
