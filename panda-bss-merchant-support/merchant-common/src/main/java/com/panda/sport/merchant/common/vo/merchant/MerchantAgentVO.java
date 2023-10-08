package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @date 2020/12/15
 * @description 批量添加代理商下的渠道用到的VO
 * @author javier
 */
@Data
@Accessors(chain = true)
public class MerchantAgentVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String merchantAgentId;
    private Set<String> merchantIds;
    private Date updateTime;
    private String updateUser;
    private String userId;
    private Integer merchantOutIn;
}
