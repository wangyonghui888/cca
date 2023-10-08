package com.panda.sport.merchant.common.po.bss;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.order.po
 * @Description: 玩法对象
 * @Date: 2019/10/16 20:39
 * @Version: 1.0
 */
@Data
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PlayPO extends BaseVO {
    private Integer id;

    private Integer playId;

    private Integer orderNo;

    private String ruleCode;

    private String sportId;

    private String playName;
    private String title;

    private String betNumber;

    private String betType;

    private String remark;

}
