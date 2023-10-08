package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.po.bss
 * @Description :  TODO
 * @Date: 2021-11-07 10:58:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class SportFilterVo {

    private Long id;

    private String nameCode;

    private String name;

    private int tag = 1;
}
