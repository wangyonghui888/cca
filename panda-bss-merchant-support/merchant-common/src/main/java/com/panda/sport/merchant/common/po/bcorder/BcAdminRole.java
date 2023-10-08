package com.panda.sport.merchant.common.po.bcorder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName AdminRole
 * @auth YK
 * @Description 用户角色
 * @Date 2020-09-01 14:16
 * @Version
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_role")
public class BcAdminRole {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 用户角色名称
     */
    private String name;

    /**
     * 英文名称
     */
    private String en;

    /**
     * 用户备注
     */
    private String remark;


    /**
     * 创建人
     */
    private String createUsername;

    /**
     * 授权时间
     */
    private String authorizedTime;

}
