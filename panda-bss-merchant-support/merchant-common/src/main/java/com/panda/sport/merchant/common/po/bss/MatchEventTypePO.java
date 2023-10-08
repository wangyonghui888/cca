package com.panda.sport.merchant.common.po.bss;
/** 赛事盘中事件类型表.
 * 
 * @pdOid 4fd1448f-cf5f-473b-80c6-843aef3408ac */
public class MatchEventTypePO {
   /** id
    * 
    * @pdOid 8ec49b4f-4fbc-4bc0-a0c7-b6b50ee9cb28 */
   private long id;
   /** 第三方数据源提供的该事件id
    * 
    * @pdOid 7c713f0f-016d-47ef-a6a0-5c07af73bc95 */
   private int sportId = 0;
   /** 事件id
    * 
    * @pdOid 4e8a508e-0d20-4e60-9a1b-58fa975ce9e1 */
   private int eventId = 0;
   /** 事件代表的含义,中文描述
    * 
    * @pdOid 32f6fba4-8ddf-43aa-ab8d-89b500260e66 */
   private String eventMeans;
   /** 事件名称.类似:Temporary interruption
    * 
    * @pdOid 684c2851-493e-4fca-91c2-027ce778e8ee */
   private String eventName;
   /** 额外信息.例如:0 = Unknown  -1 = Not specified  = Penalty  2 = Own goal   3 = Header  -100 = Shot   -200 = Free Kick
    * 
    * @pdOid b1ae85a9-4340-408f-ab6c-b032dd174a13 */
   private String extrainfo;
   /** @pdOid 2486737f-d4c0-481e-aeb0-144fcaa89705 */
   private String remark;
   /** @pdOid b2bb652a-8f5a-46b0-839b-fd3a4a23936f */
   private long createTime = 0;
   /** @pdOid 47aea755-5a04-49d4-ab11-04736d47aa3e */
   private long modifyTime = 0;

}