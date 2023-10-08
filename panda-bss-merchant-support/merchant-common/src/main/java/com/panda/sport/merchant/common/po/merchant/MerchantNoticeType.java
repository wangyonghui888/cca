package com.panda.sport.merchant.common.po.merchant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @auth: YK
 * @Description:公告内容类型
 * @Date:2020/6/24 13:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MerchantNoticeType {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 公告标题
    */
    private String typeName;
    /**
     * 公告标题
    */
    private String typeEn;

}
