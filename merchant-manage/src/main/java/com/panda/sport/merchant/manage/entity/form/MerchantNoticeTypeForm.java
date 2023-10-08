package com.panda.sport.merchant.manage.entity.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/6/24 14:19
 */
@Data
public class MerchantNoticeTypeForm {

    private Long id;
    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称不能为空")
    private String typeName;
}
