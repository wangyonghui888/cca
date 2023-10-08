package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/24 20:41:12
 */
@Getter
@Setter
@ToString
public class BettingPlayVO implements Serializable {

    private static final long serialVersionUID = -126966952749271646L;

    private Integer sportId;

    private Integer playId;

    private String playName;
}
