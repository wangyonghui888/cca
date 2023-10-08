package com.panda.sport.merchant.common.po.bss;

/** @pdOid f0dcbe76-5492-4a83-869a-5b6ea1934f4d */
public class SystemItemDictPO {
   /** @pdOid 05be565f-19f5-4af2-8ba3-fe2b5b915ab4 */
   private long id;
   /** 字典类型id.system_type_dict.id
    * 
    * @pdOid 2a5a29f3-4b85-482d-8e9b-a2c2ee4a4a8c */
   private long parentTypeId = 0;
   /** 项目编码
    * 
    * @pdOid 9f3e9772-55d8-4bb4-9df7-7284256f9ee0 */
   private String code;
   /** 项目值.
    * 
    * @pdOid 9f5cd2dc-d796-43b8-b78b-602984ef1247 */
   private String value;
   /** 是否激活.1:激活;0:没有激活.
    * 
    * @pdOid fdb58baa-8735-425d-8ac1-49789aa016ff */
   private int active = 1;
   /** 描述信息.
    * 
    * @pdOid 06a1e656-2feb-4d8f-b232-f3c943ff24f4 */
   private String description;
   /** 备注.remark
    * 
    * @pdOid 51536cde-cf42-4a76-9dfc-843643dc56f2 */
   private String remark;
   /** 创建时间. create_time
    * 
    * @pdOid 99702240-55ca-4012-b031-d09c2bd12ba0 */
   private long createTime = 0;
   /** 更新时间. modify_time
    * 
    * @pdOid 787b3b0c-c742-4273-85e7-3f2b0c831acc */
   private long modifyTime = 0;

}