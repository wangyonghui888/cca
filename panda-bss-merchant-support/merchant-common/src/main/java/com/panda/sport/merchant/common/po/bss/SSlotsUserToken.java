package com.panda.sport.merchant.common.po.bss;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户老虎机奖券表
 * </p>
 *
 * @author Auto Generator
 * @since 2022-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SSlotsUserToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 拥有奖券数量
     */
    private Long tokenNum;

    /**
     * 奖券类型id ,幸运奖 id 为 0
     */
    private Long tokenId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后更新时间
     */
    private Long lastUpdateTime;


}
