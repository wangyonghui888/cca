package com.panda.sport.merchant.common.po.bss;

import lombok.Data;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.po
 * @Description: TODO
 * @Date: 2019/10/25 10:13
 * @Version: 1.0
 */
@Data
public class SportTeamPO extends BaseVO {

    /**
     * id。id
     */
    private Long id;

    /**
     * 体育种类id。体育种类id
     */
    private Long sportId;

    /**
     * 第三方球队id。   third_sport_team.id
     */
    private Long thirdTeamId;

    /**
     * 球队区域ID。  standard_sport_region.id
     */
    private Long regionId;

    /**
     * 数据来源编码。取值： SR BC分别代表：SportRadar、FeedConstruc。详情见data_source
     */
    private String dataSourceCode;

    /**
     * 关联数据源数量
     */
    private Integer relatedDataSourceCoderNum;

    /**
     * 关联数据源编码列表。 数据样例：SR,BC,188; SR,188; BC,188
     */
    private String relatedDataSourceCoderList;

    /**
     * 球队 logo。图标的url地址
     */
    private String logoUrl;

    /**
     * 球队 logo缩略图的url地址
     */
    private String logoUrlThumb;

    /**
     * 球队管理id。 该id 用于后台管理。
     */
    private Long teamManageId;

    /**
     * 球队名称编码。 用于多语言
     */
    private Long nameCode;

    /**
     * 对用户可见。1：可见； 0：不可见
     */
    private Integer visible;

    /**
     * 主教练。主教练名称
     */
    private String coach;

    /**
     * 主场。比如：所在地 和 名称
     */
    private String statium;

    /**
     * 球队介绍。默认是空
     */
    private String introduction;

    /**
     * 备注。
     */
    private String remark;

    /**
     * 创建时间。
     */
    private Long createTime;

    /**
     * 更新时间。
     */
    private Long modifyTime;

    /**
     * 简称的拼音大写全拼(冗余字段，用于排序)
     */
    private String nameSpell;

    /**
     * 中文简体。中文简称(冗余字段，用于查询)
     */
    private String name;

}
