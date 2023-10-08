package com.panda.sport.merchant.common.vo;
import lombok.Data;

import java.io.Serializable;

@Data
public class MerchantInfoVo  implements Serializable {

    private String merchantCodes;

    private String merchantCode;

    private String merchantName;

    private String remark;

    private Integer status;

    private Integer userId;

    private String language;

    private String ip;
}
