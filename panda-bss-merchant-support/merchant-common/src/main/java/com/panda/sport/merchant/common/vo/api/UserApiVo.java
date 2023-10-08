package com.panda.sport.merchant.common.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserApiVo implements Serializable {
    private static final long serialVersionUID = 976257999410742542L;
    /**
     * 用户id
     */
    private String userId;

    private String domain;

    private List<String> loginUrlArr;

    private String apiDomain;

    /**
     * 登录成功后的token
     */
    private String token;
    /**
     * 用户名
     */
    private String username;
    /**
     * 备注
     */
    private String imgDomain;

    private String url;

    private String loginUrl;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 是否VIP用户 1不是2是
     */
    private Integer isTest;


}
