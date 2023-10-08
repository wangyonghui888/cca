package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RcsBusinessLogVO implements Serializable {

    private static final long serialVersionUID = -6595547821561215897L;

    /**
     * 操作类别
     */
    private String operateCategory;

    /**
     * 操作对象id
     */
    private String objectId;

    /**
     * 操作对象名称
     */
    private String objectName;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作参数名称
     */
    private String paramName;

    /**
     * 修改前参数值
     */
    private String beforeVal;

    /**
     * 修改后参数值
     */
    private String afterVal;

    /**
     * 操作人id
     */
    private String userId;

    /**
     * 操作人IP
     */
    private String ip;
}
