package com.panda.sport.merchant.manage.excepiton;

import com.panda.sport.merchant.common.exception.SeaMoonVerificationException;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auth: YK
 * @Description:统一异常处理
 * @Date:2020/5/12 20:11
 */
@Slf4j
@RestControllerAdvice
@Order(1)
public class GlobalExceptionHandler {

    /**
     * 参数校验
     *
     * @param e params
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Response.returnFail(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    /**
     * 参数校验
     *
     * @param e params
     * @return com.panda.sport.merchant.common.vo.Response
     */
    @ExceptionHandler(Exception.class)
    public Response<?> Exception(Exception e) {
        return Response.returnFail(e.getMessage());
    }

    /**
     * 海月口令验证
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SeaMoonVerificationException.class)
    public Response<?> Exception(SeaMoonVerificationException e) {
        return Response.returnFail(e.getCode(), e.getMessage());
    }
}
