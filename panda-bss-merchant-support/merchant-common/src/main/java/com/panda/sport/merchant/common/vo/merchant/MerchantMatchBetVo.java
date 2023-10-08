package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-bss-report
 * @Package Name :  com.panda.sports.report.common.vo
 * @Description :  TODO
 * @Date: 2021-06-17 15:48:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantMatchBetVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tournamentId;

    private String matchStr;

    private Long matchId;

    private String matchInfo;

    private Integer sportId;

    private String startTime;

    private Long startTimeL;

    private String endTime;

    private Long endTimeL;

    private Integer pageSize;

    private Integer pageStart;

    private Integer pageNum;

    private Integer total;

    private String orderBy;

    private String sort;

    private String merchantCode;

    private List<String> merchantCodeList;

    private Integer tournamentLevel;

    /**
     * 赛事种类
     * 0 所有赛事
     * 1 常规赛事
     * 2 虚拟赛事
     */
    private Integer matchType;

    /**
     * 币种:默认1,人民币，2为积分制
     */
    private Integer currency;

    /**
     * 是否导出
     */
    private Integer isExport;

    private String language;

    private Integer matchStatus;
}
