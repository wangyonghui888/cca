package com.panda.sport.merchant.common.po.bss;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 标准玩法表
 * 
 * @pdOid 67d0c694-fafe-41ce-b9fa-b74e28a41323 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BettingTypePO extends BaseVO {
   /** 表ID, 自增 */
   private Long id;
   /** 运动种类id.  对应表 sport.id */
   private Long sportId;
   /** 例如: total */
   private String type;
   /** 玩法标识. */
   private String typeIdentify;
   /** 如果当前记录对外起作用, 则该visible为1, 否则为 0. 默认 1 */
   private Integer visible;
   /** 玩法状态. 0已关闭; 1已创建; 2待二次校验; 3已开启; .  默认已创建 */
   private Integer status;
   /** 是否属于多盘口玩法. 0no; 1yes.  默认no */
   private Integer multiMarket;
   /** 排序值. */
   private Integer orderNo;
   /** 玩法构成盘口的数据格式 */
   private String dataFormate;
   /** 玩法详细说明 */
   private String description;
   /** 备注 */
   private String remark;
   /** 模板展示 */
   private Long templateShowing;
   /** 国际化nameCode */
   private Long nameCode;
   /** 赔率切换,保存多个用逗号隔开 */
   private String oddsSwitch;

   /** 选项展示 Yes 展示 No 关闭 */
   private String optionToShow;

   /** 国际化语言集合 */
  // private List<I18nItemBO> i18nItemPOList =new ArrayList<>();


}