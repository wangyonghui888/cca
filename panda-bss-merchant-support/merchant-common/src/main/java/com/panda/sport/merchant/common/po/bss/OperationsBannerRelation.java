package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:28:45
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("t_operations_banner_relation")
public class OperationsBannerRelation implements Serializable {

    private static final long serialVersionUID = 517651354228242457L;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 后台设置ID
     */
    private Long bannerId;
}
