package com.panda.sport.bc.exception;

import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
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
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public APIResponse handleException(Throwable e) {
        log.error("全局拦截异常:", e);
        return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR, e.getMessage());
    }

    /**
     * 处理 接口无权访问异常AccessDeniedException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public APIResponse handleAccessDeniedException(AccessDeniedException e) {
        return APIResponse.returnFail(ResponseEnum.PERMISSION_ERROR);
    }

    /**
     * 处理自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    public APIResponse handleAccessDeniedException(BadRequestException e) {
        return APIResponse.returnFail(ResponseEnum.INTERNAL_ERROR, e.getMessage());
    }


}
