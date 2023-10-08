package com.panda.multiterminalinteractivecenter.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainVO {

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
     * 操作类型
     */
    private MerchantLogTypeEnum operatTypeEnum;

    /**
     * 旧域名
     */
    private String oldDomain;

    /**
     * ip
     */
    private String ipName;

    /**
     * 0 未使用 1已使用 2待使用 3被攻击 4被劫持
     */
    private Integer enable;
    /**
     * 删除状态 0 未删除 1已删除
     */
    private Integer deleteTag;

    /**
     * 替换启示时间
     */
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starDate;

    /**
     * 商户组id
     */
    private String merchantGroupId;

    /**
     * 替换结束时间
     */
    @JsonFormat(locale = "zh", timezone = "Asia/Shanghai", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private Integer pageNum;

    private Integer pageSize;

    private Integer starNum;

    private Integer merchantGroupCode;

    List<ThirdMerchantVo> thirdMerchantVos;

    private String username;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    private String tab;

    private String url;

    private Integer groupType;

    private Long oldDomainId;

    private String oldDomainName;

    private String newDomainId;

    private String newDomainName;

    private String areaName;

    private List<DomainVO> config;

    /**
     * 需要排除的域名ID
     */
    private Long excludeDomainId;

    /**
     * 需要排除的域名ID
     */
    private List<Long> excludeDomainIdList;

    /**
     * 需要排除的域名组ID
     */
    private Long excludeDomainGroupId;

    /**
     * DJ| 域名和域名组一对一，排除所有已关联的域名
     */
    private Boolean excludeUsedDomain;

    /**
     * 是否勾选
     */
    private Boolean isSelect = false;

}
