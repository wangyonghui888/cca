package com.panda.sport.merchant.common.vo.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author fantasy
 * @version 1.0
 * @date 2022/8/2 12:23:21
 */
@Getter
@Setter
@ToString
public class MerchantComboVO implements Serializable {

    private static final long serialVersionUID = -6595547821561215863L;

    private String merchantCode;

    private String merchantName;
}
