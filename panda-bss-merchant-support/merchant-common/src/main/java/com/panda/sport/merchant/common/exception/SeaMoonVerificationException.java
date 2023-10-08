package com.panda.sport.merchant.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shaun
 * @version v1.0
 * @date 9/8/23 5:06 pm
 * @description 海月验证异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SeaMoonVerificationException extends Throwable {
    private String code;
    private String message;

    public SeaMoonVerificationException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}