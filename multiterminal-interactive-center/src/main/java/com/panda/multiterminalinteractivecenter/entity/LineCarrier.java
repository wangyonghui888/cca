package com.panda.multiterminalinteractivecenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@TableName("t_line_carrier")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class LineCarrier {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 线路商编码
     */
    private Integer lineCarrierCode;

    /**
     * 线路商名称
     */
    private String lineCarrierName;

    /**
     * 线路商开关(0-关闭,1-开启)
     */
    private Integer lineCarrierStatus;

    private String createBy;
    private Long createTime;
    private String updateBy;
    private Long updateTime;
    private Integer pageSize;
    private Integer pageIndex;
    private String sort;
    private String orderBy;

    private Long domainNum;
    private Long alarmThreshold;
    /**
     * tab
     */
    private String tab;

}
