package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:05
 * @Version: 1.0
 */

@Data
@ToString
public class SportMenuPO {

    /**
     * 表ID，自增
     */
    private Long id;

    /**
     * 0启用 1禁用
     */
    private Byte disabled;

    /**
     * 当前节点ID t_sport_label id
     */
    private Long lableId;

    /**
     * 父级栏目ID t_sport_label id
     */
    private Long parentId;

    /**
     * 排序
     */
    private Byte dataIndex;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;

}
