package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.entity
 * @Description :  TODO
 * @Date: 2022-03-12 13:19:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@TableName("m_role")
public class Role {

    private Long id;

    private String roleName;

    private Date creatTime;

    @TableField(exist = false)
    private Integer status = 0;

}
