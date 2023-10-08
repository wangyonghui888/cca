package com.panda.sport.merchant.common.po.bss;

import lombok.Data;
import lombok.ToString;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/24 22:03
 * @Version: 1.0
 */

@Data
@ToString
public class MatchOddsFieldPO {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 盘口ID
     */
    private String marketId;
    /**
     * 当前交易项是否被激活.1激活；0未激活(锁盘)
     */
    private Integer active;
    /**
     * 标准投注项模板id
     */
    private Long oddsFieldsTemplateId;
    /**
     * 投注项结算结果文本
     */
    private String settlementResultText;
    /**
     * 投注项类型
     */
    private String oddsType;

    /**
     * 投注项结算结果
     */
    private String settlementResult;
    /**
     * 投注项类型
     */
    private String settlementCertainty;
    /**
     * 名称,V1.2统一命名规则。
     */
    private String name;
    /**
     * 该交易项在客户端显示的值，空则不显示
     */
    private String nameExpressionValue;

    /**
     * 投注项赔率. 单位: 0.0001
     */
    private Integer oddsValue;

    /**
     * 投注项原始赔率. 单位: 0.0001
     */
    private String originalOddsValue;
    /**
     * panda自己的赔率 单位: 0.0001
     */
    private Integer paOddsValue;
    /**
     * 投注给哪一方：T1主队，T2客队，
     */
    private String targetSide;
    /**
     * 用于排序，大于1，越小越靠前
     */
    private Integer orderOdds;

    /**
     * 取值： SR BC分别代
     */
    private String dataSourceCode;
    /**
     * 该字段用于做风控时
     */
    private String thirdOddsFieldSourceId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 结对值排序
     */
    private Integer absSort;

    /**
     * 对应的中文值
     */
    private String text;

    /**
     * 盘口值
     */
    private String marketValue;

    /**
     * 盘口类型1：赛前盘；0：滚球盘。
     */
    private Integer matchType;

}
