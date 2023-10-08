package com.panda.multiterminalinteractivecenter.feign.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ConfigPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 提交商户
     */
    private String name;

    private String value;
}