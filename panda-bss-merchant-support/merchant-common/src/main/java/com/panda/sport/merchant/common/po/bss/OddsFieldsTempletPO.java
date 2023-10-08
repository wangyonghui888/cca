package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/** 标准玩法投注项表
 * 
 * @pdOid 496fa244-27b5-4cef-93ff-b23c6917ad45 */
@Data
public class OddsFieldsTempletPO extends BaseVO {
   /** 表ID, 自增
    * 
    * @pdOid 09a35275-4bf3-4853-aab3-2a680ac75865 */
   private Long id;
   /** 运动种类id.  对应表 sport.id
    * 
    * @pdOid 8d22e132-b0b1-4d9e-a762-c020f5313a63 */
   private Long marketCategoryId;
   /** 玩法名称编码. 用于多语言.
    * 
    * @pdOid b88ab1d9-b82f-4c26-8145-7fda57b40939 */
   private Integer nameCode;
   /** 投注项名称.
    * 
    * @pdOid edcc2c62-3946-4c35-8034-9da93c7fd602 */
   private String name;
   /** 排序值.
    * 
    * @pdOid 9b0ebe0c-95d7-4328-a48e-665c419bdcbc */
   private Integer orderNo = 0;
   /** 创建时间. UTC时间, 精确到毫秒
    * 
    * @pdOid 1af8c86c-e86c-4fea-8ac6-6841895bfa15 */
   private Long createTime;
   /** 更新时间. UTC时间, 精确到毫秒
    * 
    * @pdOid 4117ce1a-b84f-4d1b-bb76-d09992ff5d47 */
   private Long modifyTime;

}