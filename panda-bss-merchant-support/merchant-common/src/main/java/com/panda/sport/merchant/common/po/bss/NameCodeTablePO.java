package com.panda.sport.merchant.common.po.bss;

/** 熊猫多语言编码表
 * 
 * @pdOid 26d4f321-7085-4017-985b-bdefb8ca7a60 */
public class NameCodeTablePO {
   /** 数据库id. 对应其他表的name_code字段
    * 
    * @pdOid 82e65c62-751f-4798-bb2f-6e7c3d257165 */
   private long id;
   /** 文字标识.
    * 
    * @pdOid 8504faf0-e516-46eb-8faf-c077917f4e5b */
   private String textIdentity;
   /** 数据类型.1, 体育区域: 2, 基本玩法: 3, 联赛: 4, 赛事: 5, 球队: 6, 盘口: 7, 交易项: 8
    * 
    * @pdOid 5430a4c8-0715-428f-a925-736cf1108f7d */
   private int dataType = 0;
   /** @pdOid 240810a6-507a-4552-bf3c-46ebe11f0841 */
   private String remark;
   /** @pdOid 3025aef0-090a-42e2-8322-fadd7701893e */
   private long createTime = 0;
   /** @pdOid d485814a-853a-4c44-ad18-b8ea0febb8b8 */
   private long modifyTime = 0;

}