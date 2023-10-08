package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 域名组关系实体类
 */
@Data
@TableName("t_domain_group_relation")
public class DomainGroupRelation implements Serializable {

  /**
   * 主键id
   */
  private Long id;

  /**
   * 域名组Id
   */
  private Long domainGroupId;


  /**
   * 域名Id
   */
  private Long domainId;

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
}
