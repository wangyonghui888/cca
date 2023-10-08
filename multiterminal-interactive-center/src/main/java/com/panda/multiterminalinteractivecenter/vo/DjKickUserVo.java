package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-03-20 15:09:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class DjKickUserVo {

    private Integer system;

    private Integer type;

    private String sign;

    private String merchant_id;

    private String operation_by_name;

    private Integer operation_by_id;

    private Integer is_open_match;

    private String merchant;

    private String members;

}
