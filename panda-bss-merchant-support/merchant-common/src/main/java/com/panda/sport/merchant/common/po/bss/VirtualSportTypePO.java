package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 虚拟赛事体育种类表
 * </p>
 *
 * @author baylee
 * @since 2021-08-25
 */
@Getter
@Setter
@ToString
@TableName("s_virtual_sport_type")
public class VirtualSportTypePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 体育名称编码. 用于多语言
     */
    @TableField("name_code")
    private Long nameCode;

    /**
     * 体育类型id
     */
    @TableField("virtual_sport_id")
    private String virtualSportId;

    /**
     * 取值: SR BC分别代表:SportRadar、FeedConstruc.详情见data_source
     */
    @TableField("data_source_code")
    private String dataSourceCode;

    /**
     * 当前运动的介绍. 默认为空
     */
    @TableField("introduction")
    private String introduction;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private Long createTime;

    @TableField("modify_time")
    private Long modifyTime;


}
