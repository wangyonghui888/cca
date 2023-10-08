package com.oubao.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author spring
 * @version V1.0.0
 * @Project Name : panda-bss
 * @Package Name : com.panda.sports.bss.clearing.po
 * @Description: 赛事信息表，根据赛事事件产生相关数据 PO
 * @date 2019-11-08
 */
@EqualsAndHashCode(callSuper = false)
@Data
@ToString
public class SMatchInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**id. id*/
    private Long id;

    /**体育种类id. 运动种类id 对应s_sport_type.id*/
    private Long sportId;

    /**标准联赛 id. 对应联赛 id  对应  s_tournament.id*/
    private Long tournamentId;
    
    /**用户ID**/
    private Long uid;
}
