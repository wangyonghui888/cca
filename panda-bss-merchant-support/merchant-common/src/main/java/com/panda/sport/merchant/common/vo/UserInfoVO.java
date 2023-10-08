package com.panda.sport.merchant.common.vo;

import lombok.Data;


@Data
public class UserInfoVO {

    private Integer userId;
    private Integer disabled;
    private String username;
    private String real_name;
    private String user_level;
}
