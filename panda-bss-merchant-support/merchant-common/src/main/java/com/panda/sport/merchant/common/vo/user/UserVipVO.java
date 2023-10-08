package com.panda.sport.merchant.common.vo.user;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author : Norton
 * @Description : VIP用户
 */
@Data
public class UserVipVO implements Serializable {

    /**
     * 商户code
     */
    private String merchantCode;
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 用户id
     */
    private String  uidStr;
    /**
     * 导入类型，1系统计算2手动添加
     */
    private Integer importtype;
    /**
     * vip用户列表
     */
    private String vipuserls;

    /**
     * 用户状态 0启用 1禁用
     */
    private Integer disabled;

    private String userIds;

    private String remark;

    private String userName;

    private String type;

}
