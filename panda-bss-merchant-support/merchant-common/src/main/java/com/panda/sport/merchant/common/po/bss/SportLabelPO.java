package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:04
 * @Version: 1.0
 */
@Data
@ToString
public class SportLabelPO {

    /**
     * 表ID，自增
     */
    private Long id;

    /**
     * 0启用 1禁用
     */
    private Byte disabled;

    /**
     * 标签名字
     */
    private String labelName;

    /**
     * 标签描述
     */
    private String labelRemark;

    /**
     * name_code 扩展多语言用
     */
    private Long nameCode;

    /**
     * 栏目级别
     */
    private Byte labelLevel;

    /**
     * 体育编码表ID
     */
    private Long sportCodeId;

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
