package com.oubao.vo;

import lombok.Data;


@Data
public class UserVO extends BaseVO {


    /**
     * 实时数据命令
     */
    private Integer realStatus;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 收藏id
     */
    private Long tournamentId;

    /**
     * 赛事ID
     */
    private Long matchId;

    /**
     * 验证码
     */
    private String code;

    /**
     * 运动类型
     */
    private Long sportId;

    /***未登录用户标识***/
    private String uuid;

    /**
     * 1收藏 0取消收藏
     **/
    private Integer storeFlag;

    private String merchantCode;

}
