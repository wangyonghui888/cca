package com.panda.sport.merchant.common.vo;/**
 * @author Administrator
 * @date 2021/8/18
 * @TIME 9:43
 */

import com.panda.sport.merchant.common.po.bss.MerchantPO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *@ClassName MerchantGroupPO
 *@Description TODO
 *@Author Administrator
 *@Date 2021/8/18 9:43
 */
@Data
public class MerchantGroupVO implements Serializable {

    private String id;

    private List<String> merchantCodes;
    private List<MerchantPO> merchantList;

    //1:公共组(运维)，2:Y组(Y系),3: S组(S系),4:B组(B系)
    private Integer groupType;

    //商户组code : common,y,s,b
    private String groupCode;

    private String groupName;

    private Long updateTime;

    private Integer times;

    /*时间类型  1为分钟 2为小时 3为日  4为月*/
    private Integer timeType;

    /*状态 默认 为1 开启 2为关闭*/
    private Integer status;

    //状态 默认 为1 关闭 2为开启
    private Integer thirdStatus;

    private Integer alarmNum;

    private String domain;

    //添加请求参数 "type" 0为添加，1 删除
    private String type;
}
