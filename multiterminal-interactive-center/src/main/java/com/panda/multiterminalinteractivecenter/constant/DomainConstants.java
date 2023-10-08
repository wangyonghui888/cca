package com.panda.multiterminalinteractivecenter.constant;

import com.panda.multiterminalinteractivecenter.enums.DomainTypeEnum;
import com.panda.multiterminalinteractivecenter.enums.TabEnum;
import org.apache.commons.lang3.StringUtils;

public class DomainConstants {

    public static String DOMAIN = "domain";

    public static String OSS_DOMAIN = "ossDomain";

    public static String OSS_JSON_DOMAIN = "ossJsonDomain";

    /**自检切换*/
    public static int DOMAIN_CHANGE_AUTO = 1;

    /**手动切换*/
    public static int DOMAIN_CHANGE_MANUAL = 2;


    /**DJ方推送域名 配置域名数量*/
    public static final int DJ_H5_DOMAIN_COUNT = 2;
    public static final int DJ_PC_DOMAIN_COUNT = 2;
    public static final int DJ_API_DOMAIN_COUNT = 2;
    public static final int DJ_OSS_DOMAIN_COUNT = 2;
    public static final int DJ_OTHER_DOMAIN_COUNT = 2;
    public static final int DJ_DOMAINS_THRESHOLD = 10;

    /**CP方推送域名 配置域名数量*/
    public static final int CP_H5_DOMAIN_COUNT = 1;
    public static final int CP_PC_DOMAIN_COUNT = 1;
    public static final int CP_API_DOMAIN_COUNT = 2;
    public static final int CP_OSS_DOMAIN_COUNT = 2;
    public static final int CP_OTHER_DOMAIN_COUNT = 2;
    public static final int CP_DOMAINS_THRESHOLD = 8;

    public static int getSuffixByDomainType(Integer domainType,String tab) {
        int defaultSuffix = 2;
        if(StringUtils.isBlank(tab)||domainType==null){
            return defaultSuffix;
        }
        if(tab.equalsIgnoreCase(TabEnum.DJ.getName())){
            if(domainType.equals(DomainTypeEnum.H5.getCode())){
                return DomainConstants.DJ_H5_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.PC.getCode())){
                return DomainConstants.DJ_PC_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.APP.getCode())){
                return DomainConstants.DJ_API_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.IMAGE.getCode())){
                return DomainConstants.DJ_OSS_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.OTHER.getCode())){
                return DomainConstants.DJ_OTHER_DOMAIN_COUNT;
            }
        }else if(tab.equalsIgnoreCase(TabEnum.CP.getName())){
            if(domainType.equals(DomainTypeEnum.H5.getCode())){
                return DomainConstants.CP_H5_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.PC.getCode())){
                return DomainConstants.CP_PC_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.APP.getCode())){
                return DomainConstants.CP_API_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.IMAGE.getCode())){
                return DomainConstants.CP_OSS_DOMAIN_COUNT;
            }
            if(domainType.equals(DomainTypeEnum.OTHER.getCode())){
                return DomainConstants.CP_OTHER_DOMAIN_COUNT;
            }
        }
        return defaultSuffix;
    }





}
