package com.panda.sport.merchant.common.vo;/**
 * @author Administrator
 * @date 2021/8/3
 * @TIME 16:57
 */

import lombok.Data;

import java.util.List;

/**
 *@ClassName MerchantOrderOperationVO
 *@Description TODO
 *@Author Administrator
 *@Date 2021/8/3 16:57
 */
@Data
public class MerchantOrderOperationVO {

    private List<String> orderNos;

    private List<String> betNos;

    private String language;

    private String remark;

}
