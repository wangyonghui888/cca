package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

import java.io.Serializable;

/**
 * @description t_forbid_ip
 * @author duwan
 * @date 2021-09-08
 */
@Data
public class TForbidIp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 异常ip
     */
    private String ipName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private String createUser;

    public TForbidIp() {}
}