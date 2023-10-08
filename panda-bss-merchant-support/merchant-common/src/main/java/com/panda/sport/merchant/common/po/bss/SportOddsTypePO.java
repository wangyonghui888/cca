package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:06
 * @Version: 1.0
 */
@Data
public class SportOddsTypePO extends BaseVO {

    /**
     * 表ID，自增
     */
    private Long id;

    /**
     * 运动种类id。 对应表 sport.id
     */
    private Long sportId;

    /**
     * 例如：total
     */
    private String type;

    /**
     * 如果当前记录对外起作用，则该visible为1，否则为 0。默认 1
     */
    private Integer visible;

    /**
     * 玩法名称编码. 用于多语言.
     */
    private Long nameCode;

    /**
     * 该玩法是否生效。1生效；0 不生效。 默认不生效
     */
    private Integer active;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String remark;

    /**
     *
     */
    private Long createTime;

    /**
     *
     */
    private Long modifyTime;

}
