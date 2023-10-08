package com.panda.sport.merchant.common.vo.merchant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantSimpleVO {
    /**
     * 商户id
     */
    private String id;
    /**
     * 商户code
     */
    private String merchantCode;

    private String parentCode;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 代理级别
     */
    private Integer agentLevel;
    /**
     * 商户等级
     */
    private Integer level;

    /**
     * 创建时间
     */
    private String createTime;


    public static MerchantSimpleVO build(MerchantVO merchantVO) {
        if (Objects.isNull(merchantVO)) {
            return new MerchantSimpleVO();
        }
        return MerchantSimpleVO.builder()
                .id(merchantVO.getId())
                .merchantCode(merchantVO.getMerchantCode())
                .merchantName(merchantVO.getMerchantName())
                .agentLevel(merchantVO.getAgentLevel())
                .level(merchantVO.getLevel())
                .createTime(merchantVO.getCreateTime())
                .build();
    }
}
