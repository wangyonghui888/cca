package com.panda.multiterminalinteractivecenter.exception;



import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 自定义异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException{
    private Integer code;
    private String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

}
