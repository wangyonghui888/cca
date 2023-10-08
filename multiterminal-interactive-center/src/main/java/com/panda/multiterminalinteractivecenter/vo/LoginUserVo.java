package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-03-13 15:01:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class LoginUserVo {

    private String username;

    private List<RoleVo> roles;

    private String token;

}
