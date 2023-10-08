package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:14
 * @Version: 1.0
 */
@Data
public class SportTypePO extends BaseVO {

    /**
     * id
     */
    private Long id;

    /**
     * 体育名称编码. 用于多语言.存放体育种类名称
     */
    private Long nameCode;

    /**
     * 如果当前记录对外起作用（该体育项目是否展示在客户端）。则该visible为 1，否则为0。默认true
     */
    private Byte visible;

    /**
     * 当前运动的介绍。默认为空
     */
    private String introduction;

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
