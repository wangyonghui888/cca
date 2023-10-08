package com.panda.sport.merchant.common.po.bss;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 标准体育区域表
 *
 * @pdOid e0fa40c7-146c-433c-ad37-885d4263a159
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SportRegionPO {
    /**
     * id
     *
     * @pdOid 004436f5-d151-40b5-8fb8-12c9a97fd24d
     */
    private Long id;
    /**
     * 如果当前记录对外起作用, 则该visible 为 1, 否则为 0. 默认true
     *
     * @pdOid f706b823-66ae-4eab-90eb-f15a7e3c5c70
     */
    private Integer visible;
    /**
     * 区域名称编码. 用于多语言. 存放体育区域名称
     *
     * @pdOid 1705fcd0-ad48-4ddd-88cd-b3dcb6aa82ec
     */
    private Integer nameCode;
    /**
     * 介绍, 默认为空
     *
     * @pdOid c5ebb8e5-f415-4553-88c6-cdaa2720e03e
     */
    private String introduction;
    /**
     * 区域名称大写字母拼写
     *
     * @pdOid 3f4ff8c9-9d1e-4f9e-8dfc-cb08a3958aa4
     */
    private String spell;
    /**
     * @pdOid 3ad0fe74-b5c2-4237-ae6e-a36c33c40274
     */
    private String remark;
    /**
     * @pdOid 49f3528b-c619-4d85-9dfe-49f2dea166b5
     */
    private Long createTime;
    /**
     * @pdOid 42aebfdc-58da-43fe-bf3e-abeeca681e2a
     */
    private Long modifyTime;


    private long regionId;

    /**
     * 赛种list
     */
    private List<TournamentPO> tournamentConditionVos;


}
