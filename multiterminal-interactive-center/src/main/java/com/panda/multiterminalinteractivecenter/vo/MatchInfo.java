package com.panda.multiterminalinteractivecenter.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName(value = "m_match_info")
public class MatchInfo {

    private Long id;
    private String name;
    private String tab;
    private Integer status;

}
