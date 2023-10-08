package com.panda.sport.merchant.common.exception;



import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
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

    public static Object responseFail(Object responseObj, String exCode, String exMessage) {
        Object result;
        if (responseObj instanceof APIResponse) {
            result = APIResponse.returnFail(exCode, exMessage);
        } else {
            result = Response.returnFail(exCode, exMessage);
        }
        return result;
    }
}
