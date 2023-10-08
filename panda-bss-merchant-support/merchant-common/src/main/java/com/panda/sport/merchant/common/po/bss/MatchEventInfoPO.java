package com.panda.sport.merchant.common.po.bss;
/** 赛事盘中事件表. 非结算类盘中事件 third_event_id   data_source_code  组合
 * 
 * @pdOid 8f27929b-21c8-4d4f-99bc-af192711a3a8 */
public class MatchEventInfoPO {
   /** id
    * 
    * @pdOid 3a1ee1b0-3e25-498c-995c-125f06af6d3a */
   private long id;
   /** 第三方数据源提供的该事件id
    * 
    * @pdOid 542dc3a2-c9ba-4e79-a669-3cc0992fa003 */
   private String thirdEventId;
   /** 第三方赛事的id
    * 
    * @pdOid 72714004-643c-4391-a3b4-4e6d9e1ba692 */
   private String thirdMatchId;
   /** 本次进球球队的第三方 ID
    * 
    * @pdOid 5874be1e-b2b2-45ff-b32c-b5db738b5965 */
   private String thirdTeamId;
   /** 标准赛事的id
    * 
    * @pdOid cdb31d24-4f82-4167-b1a6-11947b983f82 */
   private long standardMatchId = 0;
   /** 本次事件球队的标准 ID.
    * 
    * @pdOid 4be6b381-4967-4c6f-8097-79c2e42d9b50 */
   private long standardTeamId = 0;
   /** 是否被取消.1 被取消;  0:没有被取消.
    * 
    * @pdOid eaaa35a8-ce81-4016-a1a5-c8f14bbe21bd */
   private long canceled = 0;
   /** 事件发生utc时间
    * 
    * @pdOid fd9fcc77-df08-4200-a989-edf343cbfde8 */
   private long giveTime = 0;
   /** 事件类型
    * 
    * @pdOid 80389f43-adcb-4e78-9a27-018dd43c54bd */
   private int type = 0;
   /** 距离比赛开始多少秒获得当前角球
    * 
    * @pdOid ec5c60d3-d96b-4313-ba3d-4446531467f5 */
   private long secondsFromStart = 0;
   /** 进球/得罚牌/罚球的球员id
    * 
    * @pdOid 8932e980-03b7-4557-ad19-390e94d634f8 */
   private long playerId = 0;
   /** 球员2的id
    * 
    * @pdOid 67f1009b-4799-4c49-afc0-274cf76d2ee0 */
   private long player2Id = 0;
   /** 进球类型. 0 = Unknown;  -1 = Not specified; 1 = Penalty; 2 = Own goal; 3 = Header; -100 = Shot;-200 = Free Kick
    * 
    * @pdOid 93218b35-8e5f-4f8d-aba7-3afcbf37c126 */
   private long scoreType = 0;
   /** 比赛阶段id. match_event_type.id
    * 
    * @pdOid 64a67a8b-c22e-43c9-be9d-73ef859b0799 */
   private long matchPeriodId = 0;
   /** 数据来源编码取值:  SR BC分别代表: SportRadar、FeedConstruc. 详情见data_source
    * 
    * @pdOid 9c23f8fe-e201-4624-bbc2-9e8aadd3b056 */
   private String dataSourceCode;
   /** 比如:  SportRadar 发送数据时, 这场比赛的ID.
    * 
    * @pdOid 267a51ba-c79a-4d06-be43-6e1bb6152dd7 */
   private String thirdMatchSourceId;
   /** 进球/罚牌/点球的球队, 主客场. 主场队: home  客场队: away
    * 
    * @pdOid 4f19dcc7-baff-4433-b15b-cf222639d6bd */
   private String homeAway;
   /** type对应时间类型的编号, 该字段对应名称.
    * 
    * @pdOid a72c6cb2-22dc-495f-8248-d4b4ba22278a */
   private String typeName;
   /** 点球结果, 取值:  0 1 -1;  分别代表: 1:  进球; 0: 没进球; -1: 没开始
    * 
    * @pdOid 30b68aee-8a54-4914-8ca6-a16aec5b77d4 */
   private int penaltyResult = 0;
   /** 额外信息
    * 
    * @pdOid ea5154ad-455a-4fbd-aa65-aff34e123406 */
   private String extrainfo;
   /** @pdOid 7928ab29-42d2-483c-b9bc-f418f32e20a7 */
   private String description;
   /** 该字段存放额外信息, 需要自行组织结构存放, 以便读取后能快速处理.
    * 
    * @pdOid c3027433-bf19-430e-b348-e5c326336680 */
   private String detailDescription;
   /** @pdOid 1954d692-b4ee-414f-b161-8ac3d610e313 */
   private String remark;
   /** @pdOid 01059577-9209-44b2-8062-1c9f2d77cac3 */
   private long createTime = 0;
   /** @pdOid aa3e9bc8-1248-46a3-b9ea-370ff04573cb */
   private long modifyTime = 0;

}