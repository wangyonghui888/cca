package com.panda.sport.merchant.common.po.bss;


/** 记录联赛或者比赛 关联的映射关系
 * 
 * @pdOid 8a5e08d9-7333-4d8f-afeb-618a4a1c1534 */
public class TournamenMatchMapPO {
   /** 数据库维护, 自增
    * 
    * @pdOid c3d8bf94-09af-41b0-b72e-039f2240d764 */
   private long id;
   /** 0: 联赛; 1: 比赛;
    * 
    * @pdOid 24b77d58-0f9d-46b2-889f-ff5cbfad88ca */
   private long type = 0;
   /** 映射关系中的 源id. 第三方数据的ID
    * 
    * @pdOid 22d4bb19-5d30-4700-afda-3a2c08961def */
   private long sourceId = 0;
   /** 映射关系中的目标id. 标准数据的ID
    * 
    * @pdOid dcc26c5e-b14d-47c3-aac7-b3d3c810b61c */
   private long targetId = 0;
   /** @pdOid f53ffa28-0619-4d69-855d-0ca8663fb87e */
   private String remark;
   /** @pdOid 5646629f-a3a3-4e55-bc89-d4799772b99b */
   private long createTime = 0;
   /** @pdOid fbb7e7c3-6b71-4758-8853-e850665bc4a8 */
   private long modifyTime = 0;

}