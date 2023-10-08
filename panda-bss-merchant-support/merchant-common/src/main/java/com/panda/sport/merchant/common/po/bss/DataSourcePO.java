package com.panda.sport.merchant.common.po.bss;



/** 该系统从哪些平台接收数据
 * 
 * @pdOid ed1bc0f0-5934-47b7-9f4b-bb05d9446eed */
public class DataSourcePO {
   /** 数据表id, 自增
    * 
    * @pdOid ce1b2add-132c-451d-8dda-72d3e3e3e4cd */
   private long id;
   /** 该数据源的编码.比如 SportRadar的编码是 SR
    * 
    * @pdOid 1911052f-6795-420b-b49f-0b68aaeec877 */
   private String code;
   /** 数据源全称.比如 SportRadar
    * 
    * @pdOid d0991829-fa65-4785-a66c-3e53a30f1697 */
   private String fullName;
   /** 数据源简称.比如 SR , 球探
    * 
    * @pdOid 293ee270-10db-46e2-a153-677bb556238a */
   private String shortName;
   /** 数据的优先级. 值越大, 重要程度越高.
    * 
    * @pdOid f5d467ca-255b-479c-88d3-7d79994c256c */
   private int priority = 0;
   /** 是否是商业来源的数据. 1: 商业来源;0:非商业
    * 
    * @pdOid 234f2635-5123-4374-b43f-64e72b5fae49 */
   private long commerce = 0;
   /** 是否为标准数据源. 1: 是; 0: 否
    * 
    * @pdOid afa9a21a-f2d6-42c2-9c2a-bca0264b9079 */
   private long standard = 0;
   /** 该数据源是否使用. 1: 使用; 0: 不使用
    * 
    * @pdOid 525cf6e5-b6d4-4b83-81e3-ee417306ac7c */
   private long active = 0;
   /** @pdOid 232e5cb3-e27d-4b24-bb1f-c2162aaba436 */
   private String remark;
   /** @pdOid d0c1c104-f077-4179-921a-c74eb66d926a */
   private long createTime = 0;
   /** @pdOid c0215424-32e1-4503-bd7e-13af8b8aca6f */
   private long modifyTime = 0;

}