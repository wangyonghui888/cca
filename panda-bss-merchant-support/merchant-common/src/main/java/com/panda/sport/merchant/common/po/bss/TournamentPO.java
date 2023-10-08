package com.panda.sport.merchant.common.po.bss;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/** SR中对应tournament
 * BC中对应Competition
 * 
 * @pdOid 95193e3f-bd84-4355-9569-a615e54d6c26 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPO implements Serializable {
   /** 表ID, 自增. id
    * 
    * @pdOid c3f8e2f5-ff7e-48ad-a788-64166be712eb */
   private long id;
   /** 运动种类ID. 联赛所属体育种类id, 对应 sport.id
    * 
    * @pdOid bdcd6526-c0d5-40d2-bd20-89bba7369526 */
   private long sportId = 0;
   /** 第三方联赛id. 第三方联赛在 表 third_sport_tournament 中的id
    * 
    * @pdOid 0eb7911b-84f5-4a53-a57a-156e2d297133 */
   private long thirdTournamentId = 0;
   /** 所属标准区域 id.  对应  standard_sport_region.id
    * 
    * @pdOid f8cafe6e-d3b8-48ef-94ed-539934102de7 */
   private long regionId = 0;
   /** 联赛分级. 1: 一级联赛; 2:二级联赛; 3: 三级联赛; 以此类推; 0: 未分级
    * 
    * @pdOid 6ff4bca6-88ed-400c-a5cd-30688bc31c28 */
   private long tournamentLevel = 0;
   /** 后台管理使用的联赛id.
    * 
    * @pdOid bf7d8f57-78d6-416a-bb37-f81c99ff9aae */
   private long tournamentManagerId = 0;
   /** 对用户可见. 1: 可见;  0: 不可见
    * 
    * @pdOid bd933ba9-f9e0-4260-a373-ad938d86ac52 */
   private long visible = 0;
   /** 第三方联赛原始id.第三方提供的联赛的id
    * 
    * @pdOid 4650c443-7db6-4466-9c75-35fe7b91e764 */
   private String thirdTournamentSourceId;
   /** 联赛名称编码. 联赛名称编码. 用于多语言
    * 
    * @pdOid 50c28d04-cf8e-4e1f-91e1-5f353cd54daf */
   private long nameCode = 0;
   /** 数据来源编码.取值: SR BC分别代表:SportRadar、FeedConstruc.详情见data_source
    * 
    * @pdOid d858da4f-f865-4e26-8b74-acdf79590632 */
   private String dataSourceCode;
   /** 联赛 logo. 图标的url地址
    * 
    * @pdOid c290b68b-2fe0-4417-861b-8a70a01bce8d */
   private String logoUrl;
   /** 联赛 logo. 缩略图的url地址
    * 
    * @pdOid 06276a31-b769-4b37-9c18-21baca96e044 */
   private String logoUrlThumb;
   /** 关联数据源数量
    * 
    * @pdOid 18b2aaec-0a2a-4045-ba68-318ad2162437 */
   private int relatedDataSourceCoderNum = 0;
   /** 关联数据源编码列表. 数据样例:SR,BC,188; SR,188; BC,188 (冗余字段,用于查询)
    * 
    * @pdOid 8f6826a6-9f65-4058-8795-a073fbd83668 */
   private String relatedDataSourceCoderList;
   /** 简介.
    * 
    * @pdOid c5f28385-c0ac-4754-9d63-c3085428997d */
   private String introduction;
   /** 备注.
    * 
    * @pdOid a29a0d83-46b2-4e4b-9be5-4160b306cc96 */
   private String remark;
   /** 创建时间.
    * 
    * @pdOid 961feb11-a6bf-47ea-9102-6098fb78206d */
   private Long createTime;
   /** 修改时间.
    * 
    * @pdOid ded4ff25-bb17-4ec0-9f37-80a3a7ffffce */
   private Long modifyTime;
   /** 英文名称(冗余字段,用于排序)
    * 
    * @pdOid caf13313-f837-49bb-9dbf-8c7dc667a07c */
   private String nameSpell;
   /** 中文简体(冗余字段,用于查询,修改是需要维护)
    * 
    * @pdOid e6f8b900-f602-4b50-9184-93e9455ca69d */
   private String name;
   /** 第三方联赛id. 第三方联赛在 表 third_sport_tournament 中的id
    *
    * @pdOid e6f8b900-f602-4b50-9184-93e9455ca69d */
   private Integer standardSportRegionId;
   /** 联赛 logo. 图标的url地址
    *
    * @pdOid e6f8b900-f602-4b50-9184-93e9455ca69d */
   private String picUrl;
   /** 联赛 logo. 缩略图的url地址
    *
    * @pdOid e6f8b900-f602-4b50-9184-93e9455ca69d */
   private String picUrlThumb;

}