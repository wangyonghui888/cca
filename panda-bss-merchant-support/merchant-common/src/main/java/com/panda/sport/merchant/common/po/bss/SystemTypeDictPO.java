package com.panda.sport.merchant.common.po.bss;

/** @pdOid c817cf1f-7e7e-4e98-a2f7-c0fd5afbacd1 */
public class SystemTypeDictPO {
   /** @pdOid 2c587602-f834-4695-a269-6131afe11441 */
   private long id;
   /** 运动种类id
    * 
    * @pdOid fe8d8c67-d2d7-4d2b-88a1-c2e840adaa54 */
   private long sportId;
   /** 字典关键字
    * 
    * @pdOid ba322b1d-2971-420c-bd92-f40213e6f20a */
   private String code;
   /** 当前数据所表示的含义.
    * 
    * @pdOid d86c105d-a94c-427d-9833-cd96bab84ac8 */
   private String value;
   /** 是否激活.1:激活;0:没有激活.
    * 
    * @pdOid 77916ed0-a20b-4841-a393-1c6b1ce29bc0 */
   private int active = 1;
   /** 描述信息.
    * 
    * @pdOid acbc8044-ef24-4e67-b745-c71e1aa925ac */
   private String description;
   /** 备注. remark
    * 
    * @pdOid 08b58b34-b859-4531-be54-a0898ebaf6d6 */
   private String remark;
   /** 创建时间. create_time
    * 
    * @pdOid cdc3cf7d-71fd-45a3-a36f-dea34212e474 */
   private long createTime = 0;
   /** 更新时间. modify_time
    * 
    * @pdOid ba6576d4-690e-4a27-9285-1770267377fa */
   private long modifyTime = 0;

}