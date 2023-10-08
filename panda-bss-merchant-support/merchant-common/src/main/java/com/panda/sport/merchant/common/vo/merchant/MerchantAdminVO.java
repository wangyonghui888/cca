package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author butr 2020-01-21
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerchantAdminVO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商户id
     */
    private String id;
    /**
     * 商户code
     */
    private String name;

}
