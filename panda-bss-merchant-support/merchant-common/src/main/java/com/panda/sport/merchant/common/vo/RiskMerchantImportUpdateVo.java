package com.panda.sport.merchant.common.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  账户中心/平台用户风控-导入用户风控记录请求VO
 * @Date: 2022-04-09
 */
@Data
@Api(value = "账户中心/平台用户风控-导入用户风控记录请求VO")
public class RiskMerchantImportUpdateVo implements Serializable {

    private static final long serialVersionUID = 4559603177306884008L;
    @ApiModelProperty(value = "用户ID")
    @ExcelProperty(value = "用户ID",index = 1)
    private String userId;

    @ApiModelProperty(value = "风控类型,1.投注特征标签,2特殊限额,3特殊延时,4提前结算,5赔率分组,6投注特征预警变更标签,7定时任务自动化标签")
    @ExcelProperty(value = "风控类型",index = 2)
    private String type;

    @ApiModelProperty(value = "操作:同意 拒绝")
    @ExcelProperty(value = "操作",index = 3)
    private String status;

    @ApiModelProperty(value = "风控补充说明")
    @ExcelProperty(value = "补充说明",index = 4)
    private String supplementExplain;

    @ApiModelProperty(value = "商户处理人")
    @ExcelProperty(value = "商户处理人",index = 5)
    private String merchantOperator;
}
