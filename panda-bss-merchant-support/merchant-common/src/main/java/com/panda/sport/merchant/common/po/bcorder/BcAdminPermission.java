package com.panda.sport.merchant.common.po.bcorder;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName AdminPermission
 * @auth YK
 * @Description 用户权限
 * @Date 2020-09-01 14:16
 * @Version
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "admin_permission")
public class BcAdminPermission {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 别名
     */
    private String alias;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称英文名
     */
    private String nameEn;


    /**
     * 上级权限
     */
    private Integer pid;

    /**
     * 菜单ID
     */
    private Long menuId;

}
