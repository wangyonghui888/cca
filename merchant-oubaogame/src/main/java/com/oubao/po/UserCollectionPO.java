package com.oubao.po;

import com.oubao.vo.BaseVO;
import lombok.Data;


@Data
public class UserCollectionPO extends BaseVO {

    /** 版本号 */
    private static final long serialVersionUID = -8429533972818011019L;

    /** 表ID，自增 */
    private Long id;

    /** 用户表ID */
    private Long uId;

    /** 联赛表ID */
    private Long tournamentId;

    /** 赛事ID*/
    private Long matchId;

    /** 创建用户 */
    private String createUser;

    /** 创建时间 */
    private Long createTime;
    
    /** 运动类型***/
    private Long sportId;
    
    /** 是否收藏**/
    private Integer storeFlag;
    
    /***未登录用户标识***/
    private String uuid;
}