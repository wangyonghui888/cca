package com.panda.multiterminalinteractivecenter.enums;

import java.util.Arrays;

// 商户分组
public enum MerchantGroupEnum {

    COMMON(1, "公共组","common"),
    Y(2, "Y组(Y系)","y"),
    S(3, "S组(S系)","s"),
    B(4, "B组(B系)","b");


    private Integer key;
    private String label;
    private String code;


    MerchantGroupEnum(Integer key, String label, String code) {
        this.key = key;
        this.label = label;
        this.code = code;
    }


    public static MerchantGroupEnum getInstance(Integer key) {
        if(key==null){
            return null;
        }
        return Arrays.stream(values())
                .filter(item -> item.getKey().equals(key))
                .findFirst().orElse(null);
    }


    public Integer getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }

    public static String getNameByKey(Integer key){
        if(null == key) return "";
        for (MerchantGroupEnum mge : MerchantGroupEnum.values()) {
            if(mge.getKey().equals(key)) {
                return mge.getLabel();
            }
        }
        return "";
    }
}
