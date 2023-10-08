package com.panda.sport.merchant.common.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-27
 */
@Data
public class ThirdMerchantVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    private Long id;

    /**
     * 商户账号
     */
    private String merchantCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 入驻时间
     */
    private Long creatTime;

    private Date createdAt;

}
