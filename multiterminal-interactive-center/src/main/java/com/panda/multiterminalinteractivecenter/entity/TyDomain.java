package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author :  istio
 * @Project Name :  multiterminal-interactive-center
 * @Package Name :  com.panda.multiterminalinteractivecenter.entity
 * @Description :  TODO
 * @Date: 2022-06-22 13:29:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@TableName("t_domain_ty")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class TyDomain {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    @TableField("domain_type")
    private Integer domainType;

    /**
     * 域名
     */
    @TableField("domain_name")
    private String domainName;

    /**
     * 分组类型(1Common2Y3S4B)
     */
    @TableField("group_type")
    private Integer groupType;

    /**
     * 线路商ID
     */
    @TableField("line_carrier_id")
    private Long lineCarrierId;

    /**
     * 0 未启用 1启用
     */
    @TableField("status")
    private Integer status;


    /**
     *  1已使用 2待使用 3被攻击 4被劫持
     * @see com.panda.multiterminalinteractivecenter.enums.DomainEnableEnum
     */
    @TableField("enable")
    private Integer enable;

    /**
     * 启用时间
     */
    @TableField("enable_time")
    private Long enableTime;

    /**
     * 自检开关 0 未启用 1启用
     */
    @TableField("self_test_tag")
    private Integer selfTestTag;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Long updateTime;

    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;

    /**
     * 修改人
     */
    @TableField("tab")
    private String tab;

    /**
     * 商户分组ID
     */
    @TableField("merchant_group_id")
    private Long merchantGroupId;

    /**
     * 线路商名称
     */
    @TableField(exist = false)
    private String lineCarrierName;

//    /**
//     * 不建议使用
//     */
//    @TableField(exist = false)
//    private Boolean notRecommended;

    /**
     * 域名组的详情拼接
     */
    @TableField(exist = false)
    private String domainGroupDetail;

    @TableField(exist = false)
    private String allDomainGroupDetail;


    @TableField(exist = false)
    private List<DomainGroupDTO> domainGroupDTOList;


    @Data
    @TableName("t_domain")
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DomainGroupDTO {
        /**
         * 域名组ID
         */
        private String domainGroupId;

        /**
         * 域名组名称
         */
        private String domainGroupName;

    }

}
