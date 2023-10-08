package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

import java.io.Serializable;

/**
 * @author :  Gardening
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.usercenter.service.impl
 * @Description :  用户service实现类
 * @Creation Date:  2019-09-04 10:28
 * --------  ---------  --------------------------
 */
@Data
public class UserCollectionPO implements Serializable {

    /** 版本号 */
    private static final long serialVersionUID = -8429533972818011019L;

    /** 表ID，自增 */
    private Long id;

    /** 用户表ID */
    private Long uId;

    /** 联赛表ID */
    private Long tournamentId;

    /** 创建用户 */
    private String createUser;

    /** 创建时间 */
    private Long createTime;


}