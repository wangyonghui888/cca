package com.panda.sport.merchant.common.po.bss;


import lombok.Data;


@Data
public class SportPO {

    private Long id;

    private Long nameCode;

    private String name;


    private String remark;


    private String createUser;

    private String modifyUser;


    private Long createTime;


    private Long modifyTime;

    private String spell;
}
