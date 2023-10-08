package com.panda.multiterminalinteractivecenter.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 域名方案关系实体类
 */
@Data
public class DomainProgramRelation implements Serializable {

  /**
   * 主键id
   */
  private Long id;

  /**
   * 域名组Id
   */
  private Long domainGroupId;

  /**
   * 分组类型
   */
  private Integer groupType;

  /**
   * 方案Id
   */
  private Long programId;

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

  private List<DomainProgramRelation> config;

}
