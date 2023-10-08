package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description b端商户分组表
 * @author duwan
 * @date 2022-01-01
 */
@Data
public class TMerchantGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 主键
     */
    private Long id;

    /**
     * 商户组名称
     */
    private String groupName;

    /**
     * 1:运维组，2:业务组，3:公用组
     */
    private int groupType;

    /**
     * 商户组code 1 为电竞 2为彩票
     */
    private int groupCode;

    /**
     * 是否开启 1为开启 2为关闭
     */
    private int status;

    /**
     * 时间类型 1为分钟 2为小时 3为日 4为月
     */
    private int timeType;

    /**
     * 时间值
     */
    private int times;

    /**
     * 上次更新时间
     */
    private Long updateTime;

    /**
     * 报警数字
     */
    private Long alarmNum;

    private List<TMerchantGroupInfo> merchantGroupInfos;

    public TMerchantGroup() {}

    private String type;
}
