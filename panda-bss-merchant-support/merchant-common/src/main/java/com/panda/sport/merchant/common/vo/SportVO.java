package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/24 21:24:05
 */
@Getter
@Setter
@ToString
public class SportVO implements Serializable {

    private static final long serialVersionUID = 173580030941613510L;

    private Integer sportId;

    private String sportName;
}
