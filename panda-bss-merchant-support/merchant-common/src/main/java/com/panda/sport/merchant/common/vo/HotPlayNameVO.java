package com.panda.sport.merchant.common.vo;

import com.panda.sport.merchant.common.annotation.FieldExplain;
import lombok.Data;


@Data
public class HotPlayNameVO {

    @FieldExplain("体种ID")
    private String sportId;
    @FieldExplain("玩法ID")
    private String playId;
    @FieldExplain("玩法名称")
    private String playName;
    @FieldExplain("下注数量")
    private String count;

}
