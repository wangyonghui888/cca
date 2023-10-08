package com.panda.sport.merchant.common.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchBetInfoReq extends PageReq{

    @ApiModelProperty("运动种类编号")
    private Integer sportId;

    @ApiModelProperty("赛事ID")
    private String matchId ;

    @ApiModelProperty("赛事ID集合")
    private List<Long> matchIds;

    @ApiModelProperty("赛事名称")
    private String matchInfoName;

    @ApiModelProperty("赛事开赛时间")
    private String matchBeginTime;

    @ApiModelProperty("赛事结束时间")
    private String matchEndTime;

    @ApiModelProperty("玩法ID")
    private Integer playId;

    @ApiModelProperty("联赛id")
    private Long tournamentId;

    @ApiModelProperty(value = "联赛名称，支持模糊搜索")
    private String tournamentNameZs;

    @ApiModelProperty("赛事名称中文简体")
    private String matchInfoZs;

    @ApiModelProperty("联赛级别")
    private Long tournamentLevel;

    @ApiModelProperty("币种")
    private String currencyCode;

    @ApiModelProperty(value="商户ID")
    private List<String> merchantIds;

    @ApiModelProperty(value="商户ID取值范围")
    private Integer merchantIdsScope;

    @ApiModelProperty("赛事状态")
    private Integer matchStatus;

    @ApiModelProperty("排序字段 1:赛事状态 2：开始时间 3：投注金额 4：投注笔数 5：有效投注笔数 6：投注用户数 7：派彩金额 8：盈利金额 9：盈利率 10:玩法数量")
    private String orderBy;

    @ApiModelProperty(value = "排序[升序：asc  降序 desc]")
    private String order;

    @ApiModelProperty(value = "多语言[中午：zs 英文：en]")
    private String language;
}
