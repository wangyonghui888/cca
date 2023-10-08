package com.panda.sport.merchant.common.enums;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.panda.sport.merchant.common.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/6/25 20:23
 */
public enum  NoticeReleaseEnum {

    RETAIL_MERCHANTS(1, "直营商户"),
    CHANNEL_MERCHANTS(2, "渠道商户"),
    SECONDARY_MERCHANTS(3,"二级商户"),
    BET_USER(4,"投注用户"),
    INTERNAL_USER(5,"panada内部用户"),
    ALL_MERCHANT_USER(6,"全部商户"),
    SOME_MERCHANT_USER(7,"部分商户"),
    CREDIT_MERCHANTS(8,"信用商户")
    ;

    private Integer code;

    private String describe;

    NoticeReleaseEnum(Integer code, String describe){
        this.code = code;
        this.describe = describe;
    }

    public Integer getCode(){
        return this.code;
    }

    public String getDescribe(){
        return this.describe;
    }

    /**
     * 获取描述
     * @param codes
     * @return
     */
    public static String getReleaseDescribe(String codes) {
        String str = "";
        if(StringUtils.isBlank(codes)) {
            return str;
        }
        for (String code : codes.split(StringPool.COMMA)) {
            String describe = Arrays.stream(NoticeReleaseEnum.values())
                    .filter(e -> e.getCode().equals(Integer.valueOf(code)))
                        .map(NoticeReleaseEnum::getDescribe).
                             findFirst().orElse("");
            if(!"".equals(describe)) {
                str = StringUtil.isNotBlank(str) ? str + StringPool.COMMA + describe : describe;
            }
        }
        return str;
    }
}
