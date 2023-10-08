package com.panda.sport.merchant.common.vo.merchant;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;


    /**
     * 球类名称
     */
    private String name;

    private Long tournamentId;
    private Long matchId;
    private String matchInfo;

    private Integer sportId;
    private Integer status;

    private String startTime;
    private String endTime;
    private Integer pageSize;

    private Integer pageNum;

    private Integer total;

    private String orderBy;

    private String sort;

    private String merchantCode;
    private List<String> merchantCodeList;

    private Integer tournamentLevel;
    private List<Integer> agentLevelList;
    private Integer agentLevel;

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
     * 语种
     */
    private String language;
}
