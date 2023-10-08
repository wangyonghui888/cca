package com.oubao.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class UserApiVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 976257999410742542L;

	/**  用户id */
    private String userId;

    /**  登录成功后的token */
    private String token;

    /** 用户状态 0启用 1禁用 */
//    private Integer disabled;

    /** 用户名 */
    private String username;

    /** 用户登录密码 */
//    private String password;

    /** 真实姓名 */
    private String realName;

    /** 手机号 */
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 邮箱 */
    private String email;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Long createTime;

    /** 收藏id */
    private Long tournamentId;

    /** 赛事ID*/
    private Long matchId;
  
    /***未登录用户标识***/
    private String uuid;
    
    /**1收藏 0取消收藏**/
    private Integer storeFlag;

    /*** 球类ID**/
    private Long sportId;
    private BigDecimal balance;
    /**设备**/
    private String device;

    private String merchantCode;
}
