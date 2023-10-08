package com.panda.multiterminalinteractivecenter.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class DomainProgramVO {

  /**
   * 主键id
   */
  private Long id;

    /**
     * 方案名称
   */
  private String programName;

    /**
     * 分组类型
   */
  private Integer groupType;

  /**
   * 分组类型名称
   */
  private String groupTypeName;

  /**
     * 域名组数量
   */
  private Long domainGroupNum;

  /**
   * 主域名数量
   */
  private Long h5PushDomainNum;

  /**
   * 主域名数量
   */
  private Long pcPushDomainNum;

  /**
   * 主域名数量
   */
  private Long apiPushDomainNum;

  /**
   * 主域名数量
   */
  private Long imgPushDomainNum;

  /**
   * 域名组ID
   */
  private String domainGroupIds;

  /**
   * 方案programIds
   */
  private String programIds;

  /**
   * 商户组ID
   */
  private String merchantGroupIds;

  /**
   * 状态
   */
  private Integer status;

    /**
     * 创建时间
   */
  private Long createTime;

    /**
     * 修改时间
   */
  private Long updateTime;

    /**
     * 修改人
   */
  private String lastUpdated;

  private String tab;

  private Integer pageNum;

  private Integer pageSize;

  private Integer starNum;

  private String username;

  private Integer delTag;
  /**
   * 商户组id
   */
  private String merchantGroupId;
  /**
   * 商户组名称
   */
  private String groupName;
}
