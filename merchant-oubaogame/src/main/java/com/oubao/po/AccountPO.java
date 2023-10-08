package com.oubao.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: david
 * @version: V1.0.0
 * @Project Name : panda-bss
 * @Package Name : com.panda.sports.bss.lottery.po
 * @Description: 账户表
 * @Date: 2019-10-15 17:39
 */
@Data
@ToString
public class AccountPO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * ID
     */
    private Long id;

    /**
     * 账户状态 0启用 1禁用
     */
    private Integer disabled;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 可用金额
     */
    private BigDecimal amount;
    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;
    /**
     * 备注
     */
    private String remark;


    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 修改人*
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;


    private String merchantCode;

}