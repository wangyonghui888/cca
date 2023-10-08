package com.panda.sport.merchant.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  域名查询
 * @Date: 2021-08-19 16:35:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class DomainVo{

    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 域名类型 1 前端域名，2 app域名，3 静态资源域名
     */
    private Integer domainType;

    private Integer groupType;

    /**
     * 操作类型
     */
    private MerchantLogTypeEnum operatTypeEnum;

    /**
     * 旧域名
     */
    private String oldDomain;

    /**
     * 域名
     */
    private String domainName;

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
    private Long merchantGroupId;

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

    private String merchantGroupType;

}
