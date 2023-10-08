package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
@Getter
@Setter
@ToString
@TableName("s_betting_play")
public class BettingPlayPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 玩法投注项数量
     */
    @TableField("fields_num")
    private Integer fieldsNum;

    /**
     * 是否支持多玩法
     */
    @TableField("multi_market")
    private Integer multiMarket;

    /**
     * PC模板展示
     */
    @TableField("template_pc")
    private Integer templatePc;

    /**
     * H5模板展示
     */
    @TableField("template_h5")
    private Integer templateH5;

    /**
     * 支持赔率类型,1：支持欧式、英式、美式、香港、马来、印尼赔率；2：支持欧式、英式、美式赔率
     */
    @TableField("support_odds")
    private String supportOdds;

    /**
     * 玩法排序
     */
    @TableField("order_no")
    private Integer orderNo;

    /**
     * 玩法状态:0、无效1、有效
     */
    @TableField("status")
    private Integer status;

    /**
     * 玩法上游更新时间
     */
    @TableField("modify_time")
    private Long modifyTime;

    /**
     * 玩法id
     */
    @TableField("play_id")
    private Long playId;

    /**
     * 玩法多语言编码
     */
    @TableField("play_name_code")
    private Long playNameCode;

    /**
     * 赛种id
     */
    @TableField("sport_id")
    private Long sportId;

    /**
     * 是否展开，1：“是” 代表默认展开，0：“否” 代表默认收起
     */
    @TableField("is_collapse")
    private Integer isCollapse;

    /**
     * 玩法赛种修改时间
     */
    @TableField("sport_modify_time")
    private Long sportModifyTime;

    /**
     * 赛种玩法展示排序
     */
    @TableField("sport_order_no")
    private Integer sportOrderNo;

    /**
     * 所属时段,对应融合字典parent_type_id=7
     */
    @TableField("scope_id")
    private Integer scopeId;

    /**
     * 该赛种玩法状态:0、无效1、有效
     */
    @TableField("sport_status")
    private Integer sportStatus;

    /**
     * 该赛种玩法展示名称
     */
    @TableField("sport_name_code")
    private Long sportNameCode;

    /**
     * 玩法描述国际化编码
     */
    @TableField("desc_name_code")
    private Long descNameCode;

    /**
     * 业务创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 业务更新时间
     */
    @TableField("update_time")
    private Long updateTime;

    /**
     * H5模板展示-自己平台
     */
    @TableField("template_self_h5")
    private Integer templateSelfH5;

    /**
     * PC模板展示-自己平台
     */
    @TableField("template_self_pc")
    private Integer templateSelfPc;


}
