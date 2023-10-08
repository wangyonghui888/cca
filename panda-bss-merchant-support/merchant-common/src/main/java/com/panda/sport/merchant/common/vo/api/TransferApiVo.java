package com.panda.sport.merchant.common.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class TransferApiVo implements Serializable {
    private static final long serialVersionUID = 5241526151768786394L;

    private String transferId;
    private String merchantCode;
    private Long userId;
    private Integer transferType;
    private Double amount;
    private Double beforeTransfer;
    private Double afterTransfer;
    private Integer status;
    private String mag;
    private Integer transferMode;
    private Integer transferSource;
    private Long createTime;
    private String orderStr;
}
