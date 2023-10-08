package com.panda.center.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/26 17:23:35
 */
@Getter
@Setter
@ToString
public class LuckyBoxRecordsVO {
    /**
     * 商户ID
     */
    private String merchantId;

    /**
     * 商户名称
     */
    private String merchantAccount;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户表id
     */
    private String uid;

    /**
     * 盲盒类型，1：白银盲盒  2：黄金盲盒  3：钻石盲盒
     */
    private String boxName;

    /**
     * 单次奖金(单位 分)
     */
    private Long award;

    /**
     * 消耗奖券
     */
    private Integer useToken;

    /**
     * 领取状态
     */
    private String status;

    /**
     * 创建时间
     */
    private String createTime;
}
