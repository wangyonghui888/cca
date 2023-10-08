package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbnormalUserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    //用户ID
    private String uid;

    private String type;

    //用户名
    private String userName;

    //所属商户
    private String merchantCode;

    //日期
    private String crtTime;

    //风控措施
    private String updateContent;

}
