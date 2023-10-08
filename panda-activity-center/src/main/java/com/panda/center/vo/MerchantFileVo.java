package com.panda.center.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  商户文件查询参数类
 * @Date: 2020-12-08 13:42:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class MerchantFileVo extends RequestPageVO{

    private Long id;

    private List<Long> ids;

    private String fileName;

    private String merchantCode;

    private Integer start;

    private Integer status;

    /**
     * 操作人
     */
    private String operatName;
    /**
     * 请求对象
     */
    String token;

    private List<String> merchantCodes;

    private Long creatTime;

}
