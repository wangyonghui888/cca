package com.panda.sport.merchant.common.dto;

public class CreditConfigHttpQueryDto extends CreditConfigDto {

    /**
     * 用户Id
     */
    private Long userId;

    Boolean hasInitSpecialFlag = Boolean.FALSE;

    public boolean getHasInitSpecialFlag() {
        return hasInitSpecialFlag;
    }

    public void setHasInitSpecialFlag(boolean hasInitSpecialFlag) {
        this.hasInitSpecialFlag = hasInitSpecialFlag;
    }

    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
