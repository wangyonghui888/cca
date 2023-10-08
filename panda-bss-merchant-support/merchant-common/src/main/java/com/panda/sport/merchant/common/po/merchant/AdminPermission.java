package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @auth: YK
 * @Description:用户权限
 * @Date:2020/5/12 14:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminPermission {

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
     * 英文名称
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
