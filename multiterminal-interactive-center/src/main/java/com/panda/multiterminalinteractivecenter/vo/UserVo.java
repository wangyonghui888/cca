package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-04-29 13:22:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class UserVo extends PageVo{

    private String username;

    private Long id;

    private Integer isEnable;

    private String oldPassword;

    private String newPassword;

    private List<Long> roleIds;

}
