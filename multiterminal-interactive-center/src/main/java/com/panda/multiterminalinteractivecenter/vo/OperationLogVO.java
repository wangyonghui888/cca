package com.panda.multiterminalinteractivecenter.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OperationLogVO {
    private static final long serialVersionUID = -6595547821561215867L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * ip
     */
    private String ip;

    /**
     * 语种
     */
    private String language;

    /**
     * 操作页面
     */
    private String operationPage;

    /**
     * 操作字段
     */
    private List<String> operationColumns;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 修改之前的值
     */
    private List<String> beforeValues;

    /**
     * 修改之后的值
     */
    private List<String> afterValues;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 数据ID
     */
    private String dataId;
}
