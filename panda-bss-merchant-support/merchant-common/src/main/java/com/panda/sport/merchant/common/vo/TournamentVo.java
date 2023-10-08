package com.panda.sport.merchant.common.vo;

import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.vo
 * @Description :  联赛数据
 * @Date: 2020-11-06 13:14:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class TournamentVo {

    private Long id;

    private String idStr;

    private String name;

    private Long level;

    private Long sportId;

    private Integer tag = 1;
    /**
     * 联赛数据商
     */
    private String dataSourceCode;

}
