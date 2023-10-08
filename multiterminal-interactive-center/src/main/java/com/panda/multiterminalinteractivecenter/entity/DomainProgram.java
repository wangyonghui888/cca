package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_domain_program")
public class DomainProgram implements Serializable {

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
   * 域名组数量
   */
  @TableField(exist = false)
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

  private Integer delTag;

  /**
   * 商户组id
   */
  private String merchantGroupId;
  /**
   * 商户组名称
   */
  @TableField(exist = false)
  private String groupName;
}
