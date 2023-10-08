package com.panda.multiterminalinteractivecenter.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DomainDTO {

    private Long id;

    /**
     *  域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    private Integer domainType;

    /**
     *  域名名称
     */
    private String domainName;

    /**
     *  域名
     */
    private String domain;


    /**
     * 线路商ID
     */
    private Long lineCarrierId;

    /**
     * 线路商名称
     */
    private String lineCarrierName;

    private Long domainGroupId;

    /**
     * 0 未使用 1已使用 2待使用 3被攻击 4被劫持
     */
    private Integer enable;

    /**
     * 0 未启用 1启用
     */
    private Integer status;
    /**
     * 自检开关 0 未启用 1启用
     */
    private Integer selfTestTag;

    /**
     * 商户组id
     */
    private String merchantGroupId;

    private String tab;

    private String url;

    private Integer groupType;


}
