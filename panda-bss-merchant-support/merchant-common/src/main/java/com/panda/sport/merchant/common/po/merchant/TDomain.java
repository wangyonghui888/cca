package com.panda.sport.merchant.common.po.merchant;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.po.merchant
 * @Description :  TODO
 * @Date: 2022-01-01 10:57:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 域名池表
 * @author duwan
 * @date 2022-01-01
 */
@Data
public class TDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 域名类型 1 前端域名，2 app域名，3 静态资源域名
     */
    private int domainType;

    /**
     * 域名
     */
    private String domainName;

    /**
     * 0 未使用 1已使用 2待使用 3被攻击 4被劫持
     */
    private int enable;

    /**
     * 启用时间
     */
    private Long enableTime;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 商户分组id
     */
    private Integer merchantGroupId;

    public TDomain() {}
}
