package com.panda.sport.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserApiVo implements Serializable {
    private static final long serialVersionUID = 976257999410742542L;
    /**
     * 用户id
     */
    private String userId;

    private String domain;
    /**
     * 登录成功后的token
     */
    private String token;

}
