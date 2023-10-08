package com.panda.sport.merchant.common.po.bss;

/**
 *
 * @author :  sklee
 * @Description : s_team 球队信息表
 * @Date : 2019-09-30 07:41:59
*/
public class TeamPO {
    /**
    * 球队表id
    */
    private Long id;

    /**
    * 体育种类id。体育种类id
    */
    private Long sportId;

    /**
    * 球队区域ID
    */
    private Long regionId;

    /**
    * 数据来源编码。取值： SR BC分别代表
    */
    private String dataSourceCode;

    /**
    * 球队 logo。图标的url地址
    */
    private String logoUrl;

    /**
    * 球队 logo缩略图的url地址
    */
    private String logoUrlThumb;

    /**
    * 球队管理id。 该id 用于后台管理
    */
    private Long teamManageId;

    /**
    * 球队名称编码。国际化信息
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
    * 备注
    */
    private String remark;

    /**
    * 更新时间。
    */
    private Long modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getDataSourceCode() {
        return dataSourceCode;
    }

    public void setDataSourceCode(String dataSourceCode) {
        this.dataSourceCode = dataSourceCode;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrlThumb() {
        return logoUrlThumb;
    }

    public void setLogoUrlThumb(String logoUrlThumb) {
        this.logoUrlThumb = logoUrlThumb;
    }

    public Long getTeamManageId() {
        return teamManageId;
    }

    public void setTeamManageId(Long teamManageId) {
        this.teamManageId = teamManageId;
    }

    public Long getNameCode() {
        return nameCode;
    }

    public void setNameCode(Long nameCode) {
        this.nameCode = nameCode;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getStatium() {
        return statium;
    }

    public void setStatium(String statium) {
        this.statium = statium;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }
}