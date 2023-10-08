package com.panda.multiterminalinteractivecenter.feign.dto;

import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -8626066276225990603L;

    /**
     * 状态码
     */
    private String code = ApiResponseEnum.SUCCESS.getId();


    /**
     * 服务器返回时间
     */
    private BigInteger ts = BigInteger.valueOf(System.currentTimeMillis());

    /**
     * 返回的数据
     */
    private T data;


}
