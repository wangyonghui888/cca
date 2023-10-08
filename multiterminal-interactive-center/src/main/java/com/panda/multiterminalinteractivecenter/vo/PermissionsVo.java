package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-04-29 15:02:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class PermissionsVo extends PageVo{

    private Long id;

    private String name;

    private Long pid;

    private Long sort;

    private String url;

    private Integer status = 0;

    List<PermissionsVo> list;


}
