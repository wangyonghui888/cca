package com.panda.sport.merchant.common.vo.user;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author : Jeffrey
 * @Date: 2020-01-07 10:44
 * @Description : 商户结算
 */
@Data
@ToString
public class UserFakeVO implements Serializable {

    /**
     * 数据源给的赛果
     */
    private String userName;
    /**
     * 用户id
     */
    private String userId;


}
