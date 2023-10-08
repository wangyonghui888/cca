package com.panda.multiterminalinteractivecenter.vo;

import com.panda.multiterminalinteractivecenter.enums.DomainTypeEnum;
import lombok.Data;

import java.util.Objects;

/**
 * @author :  ifan
 * @Description :  域名组
 * @Date: 2022-07-2
 */
@Data
public class DomainGroupVO {

    /**
     * id
     */
    private Long id;

    /**
     * 域名名称
     */
    private String domainGroupName;

    /**
     * 分组类型
     */
    private Integer groupType;

    /**
     * 域名数据量
     */
    private Long domainNum;

    /**
     * 域名
     */
    private String domain;

    /**
     * 报警阀值
     */
    private Integer h5Threshold;
    private Integer pcThreshold;
    private Integer apiThreshold;
    private Integer imgThreshold;


    /**
     * 专属类型  1 区域，2 VIP
     */
    private Integer exclusiveType;

    /**
     * 所属区域
     */
    private Long belongArea;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 用户价值
     */
    private String userValue;

    /**
     * 所属方案
     */
    private String belongProgram;

    /**
     * 状态
     */
    private Long status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 最后更新人
     */
    private String lastUpdated;

    /**
     * 域名id组
     */
    private String domainIds;

    private String tab;

    /**
     * 扩展字段
     */
    private Integer pageNum;

    private Integer pageSize;

    private Integer starNum;

    private String username;

    private String groupTypeName;

    private String exclusiveTypeName;

    private String programId;

    private String programName;

    /**
     *  域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
     */
    private Integer domainType;


    private String domainTypeName;

    private String domainName;

    private Integer delTag;

    public Integer getThreshold(Integer domainType){
        if(domainType == null){
            return 0;
        }
        if(Objects.equals(domainType, DomainTypeEnum.H5.getCode())){
            return h5Threshold;
        }
        if(Objects.equals(domainType, DomainTypeEnum.PC.getCode())){
            return pcThreshold;
        }
        if(Objects.equals(domainType, DomainTypeEnum.APP.getCode())){
            return apiThreshold;
        }
        if(Objects.equals(domainType, DomainTypeEnum.IMAGE.getCode())){
            return imgThreshold;
        }
        return 0;


    }

}
