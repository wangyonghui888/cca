package com.panda.sport.merchant.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 *  三端资源注释类
 */
public class AggregateFestivalResourceCfgConstants {

    /**
     * 字段注释
     */
    enum ResourceEnum{
        IMG1,
        IMG2,
        IMG3,
        IMG4,
        IMG5,
        IMG6,
        IMG7,
        IMG8,
        IMG9,
        IMG10,
        IMG11,
        IMG12,
    }

    private static final Map<String,String> TYMap = new HashMap<>();
    private static final Map<String,String> DJMap = new HashMap<>();
    private static final Map<String,String> CPMap = new HashMap<>();

    static {
        TYMap.put(ResourceEnum.IMG1.name(),"PC顶部左侧（日间版）");
        TYMap.put(ResourceEnum.IMG2.name(),"PC顶部左侧（夜间版）");
        TYMap.put(ResourceEnum.IMG3.name(),"H5节日资源图（日间版）");
        TYMap.put(ResourceEnum.IMG4.name(),"H5节日资源图（夜间版）");
        TYMap.put(ResourceEnum.IMG5.name(),"PC轮播1（日间版）");
        TYMap.put(ResourceEnum.IMG6.name(),"PC轮播2（日间版）");
        TYMap.put(ResourceEnum.IMG7.name(),"PC轮播3（日间版）");
        TYMap.put(ResourceEnum.IMG8.name(),"PC轮播1（夜间版）");
        TYMap.put(ResourceEnum.IMG9.name(),"PC轮播2（夜间版）");
        TYMap.put(ResourceEnum.IMG10.name(),"PC轮播3（夜间版）");
        TYMap.put(ResourceEnum.IMG11.name(),"H5UI挂件（日间版）");
        TYMap.put(ResourceEnum.IMG12.name(),"H5UI挂件（夜间版）");

    }

}
