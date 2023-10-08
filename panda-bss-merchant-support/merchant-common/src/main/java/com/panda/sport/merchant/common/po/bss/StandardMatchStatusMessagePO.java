package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 *
 * @author :  sklee
 * @Description : T_STANDARD_MATCH_STATUS_MESSAGE 赛事状态
 * @Project Name :  panda-bss
 * @Package Name :  com.panda.sports.bss.schedule.po
 * @Date : 2019-10-15 04:02:50
*/
@Data
public class StandardMatchStatusMessagePO extends BaseVO {
    /**
    * 
    */
    private Long id;

    /**
    * 体育种类id. 运动种类id 对应sport.id
    */
    private Long sportId;

    /**
    * 比赛开始时间. 比赛开始时间 UTC时间
    */
    private Long beginTime;

    /**
    * 赛事可下注状态. 0: betstart; 1: betstop
    */
    private Byte betStatus;

    /**
    * 数据来源编码. 取值见: data_source.code
    */
    private String dataSourceCode;

    /**
    * 比赛阶段id. 取自基础表 : match_status.id
    */
    private Long matchPeriodId;

    /**
    * 赛事状态.  比如:未开赛, 滚球, 取消, 延迟等. 
    */
    private Byte matchStatus;

    /**
    * 是否为中立场. 取值为 0  和1  .   1:是中立场, 0:非中立场. 操盘人员可手动处理
    */
    private Byte neutralGround;

    /**
    * 父赛事id
    */
    private Long parentId;

    /**
    * 标准赛事id
    */
    private Long standardMatchId;

    /**
    * 第三方赛事原始id. 该厂比赛在第三方数据供应商中的id. 比如:  SportRadar 发送数据时, 这场比赛的ID. 
    */
    private String thirdMatchSourceId;


}