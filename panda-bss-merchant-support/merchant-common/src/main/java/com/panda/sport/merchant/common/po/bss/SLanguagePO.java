package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @auth: YK
 * @Description:多语言表
 * @Date:2020/6/28 17:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SLanguagePO {

    @TableId(type = IdType.INPUT)
    private Long nameCode;

    /**
     * 中文简体
     */
    private String zs;

    /**
     * 英文
     */
    private String en;

    /**
     * 中文繁体
     */
    private String zh;

    /**
     * 日语
     */
    private String jp;

    /**
     * 简称
     */
    private String jc;
    /**
     * 西班牙语
     */
    private String es;
    /**
     * 意大利语
     */
    private String it;
    /**
     * 德语
     */
    private String de;
    /**
     * 法语
     */
    private String fr;
    /**
     * 葡萄牙语
     */
    private String pt;
    /**
     * 俄语
     */
    private String ru;
    /**
     * 韩语
     */
    private String ko;
    /**
     * 泰语
     */
    private String th;
    /**
     * 越南语
     */
    private String vi;
    /**
     * 其他
     */
    private String other;

    /**
     * 业务更新时间
     */
    private Long updateTime;


}
