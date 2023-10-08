package com.panda.sport.merchant.common.enums;

/**
 * @author :  sklee
 * @Project Name :
 * @Package Name :
 * @Description : 对应体育投注业务编码类型枚举
 * @Date: 2019-09-02 19:10
 */
public enum SportMenuTypeEnum {
    ALL(0,"全部"),
    IS_LIVE(1,"滚球"),
    SOON_MATCH(2,"即将开赛"),
    TODAY_MATCH(3,"今日赛事"),
    PRESALE_MARKET(4,"早盘"),
    PRESALE_MARKET_MORE(8,"更多早盘"),
	SPORT_BASKETBALL(7,"篮球"),
    SPORT_CLASS(5,"足球"),
    FAVOURITE(6,"收藏"),
    SPORT_MENU(9,"体育"),
    ALL_BEFORE_MATCH(10,"赛前");


    private Integer menuType;

    private String desc;

    private SportMenuTypeEnum(Integer codeType, String desc){
        this.menuType = codeType;
        this.desc = desc;
    }

    public static SportMenuTypeEnum getSportMenuTypeEnum(Integer codeType)throws Exception{
        if (IS_LIVE.getMenuType().equals(codeType)){
            return IS_LIVE;
        }else if (SOON_MATCH.getMenuType().equals(codeType)){
            return SOON_MATCH;
        }else if (TODAY_MATCH.getMenuType().equals(codeType)){
            return TODAY_MATCH;
        }else if (PRESALE_MARKET.getMenuType().equals(codeType)){
            return PRESALE_MARKET;
        }else if (SPORT_CLASS.getMenuType().equals(codeType)){
            return SPORT_CLASS;
        }else if (PRESALE_MARKET_MORE.getMenuType().equals(codeType)){
            return PRESALE_MARKET_MORE;
        }else if (SPORT_BASKETBALL.getMenuType().equals(codeType)) {
            return SPORT_BASKETBALL;
        } else if (FAVOURITE.getMenuType().equals(codeType)) {
            return FAVOURITE;
        } else if (SPORT_MENU.getMenuType().equals(codeType)) {
            return SPORT_MENU;
        } else if(ALL_BEFORE_MATCH.getMenuType().equals(codeType)) {
            return ALL_BEFORE_MATCH;
        }
        else{
            throw new Exception("SportMenuTypeEnum.getSportCodeTypeEnum SELECT FAIL!");
        }
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
