package com.panda.center.result;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.center.enums.BaseEnum;
import com.panda.center.enums.ResponseEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {


    private static final long serialVersionUID = 5241526151768786394L;

    private boolean status;
    private String msg = "";
    private String code;
    private T data;
    private Long serverTime;//服务器时间

    public Response() {
        this.setApiResponseEnum(ResponseEnum.SUCCESS);
    }

    private Response(T t) {
        this();
        this.data = t;
    }

    private Response(ResponseEnum ret) {
        this.setApiResponseEnum(ret);
    }

    private Response(BaseEnum ret, T t) {
        this.setApiResponseEnum(ret);
        this.data = t;
    }

    private Response(String msg, T t) {
        this.msg = msg;
        this.data = t;
    }

    private Response(String msg, T t, boolean status) {
        this.msg = msg;
        this.data = t;
        this.status = status;
    }

    private Response(String msg, T t, boolean status, String code) {
        this.msg = msg;
        this.data = t;
        this.status = status;
        this.code = code;

    }

    public static <T> Response<T> returnSuccess() {
        return new Response();
    }

    public static <T> Response<T> returnSuccess(T data) {
        return new Response(ResponseEnum.SUCCESS, data);
    }

    public static <T> Response<T> returnSuccess(T data, BaseEnum ret) {
        return new Response(ret, data);
    }

    public static <T> Response<T> returnSuccess(T data, String msg) {
        return new Response(msg, data, true, ResponseEnum.SUCCESS.getId());
    }

    public static <T> Response<T> returnSuccess(BaseEnum ret) {
        return new Response(ret, null);
    }

    public static <T> Response<T> returnFail(BaseEnum ret, String appendErrmsg, T data) {
        Response apiResponse = returnFail(ret, appendErrmsg);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> Response<T> returnFail(BaseEnum ret, String appendErrmsg) {
        Response apiResponse = returnFail(ret);
        if (appendErrmsg != null) {
            apiResponse.msg = apiResponse.msg + "（" + appendErrmsg + "）";
        }
        return apiResponse;
    }

    public static <T> Response<T> returnFail(BaseEnum ret) {
        Response apiResponse = new Response();
        apiResponse.status = false;
        apiResponse.msg = ret.getLabel();
        apiResponse.code = ret.getId();
        return apiResponse;

        //return new APIResponse(ret); // @2016/06/01 zhuo.shi 删除此行，此行会返回SUCCESS，不是Fail信息
    }


    public static <T> Response<T> returnFail(String msg) {
        Response apiResponse = new Response();
        apiResponse.status = false;
        apiResponse.msg = msg;
        apiResponse.code = ResponseEnum.FAIL.getId();
        return apiResponse;
    }

    public static <T> Response<T> returnFail(T data, BaseEnum ret) {
        return new Response(ret, data);
    }

    public static <T> Response<T> returnFail(T data, String msg) {
        return returnSuccess(data, msg);
    }

    public static <T> Response<T> returnFail(String code, String msg) {
        Response apiResponse = new Response();
        apiResponse.code = code;
        apiResponse.msg = msg;
        apiResponse.status = false;
        return apiResponse;
    }

    public static <T> Response<T> returnFail(String code, String msg, T data) {
        Response apiResponse = new Response();
        apiResponse.code = code;
        apiResponse.msg = msg;
        apiResponse.status = false;
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> Response<T> returnSuccess(String code, String msg, T data) {
        Response apiResponse = new Response();
        apiResponse.code = code;
        apiResponse.msg = msg;
        apiResponse.status = true;
        apiResponse.setData(data);
        return apiResponse;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = System.currentTimeMillis();
    }

//    public String getVer() {
//        return ver;
//    }

    public boolean getStatus() {
        return status;
    }

    public String getMsg() {
        return StringUtils.defaultIfEmpty(msg, StringUtils.EMPTY);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }


    public void setApiResponseEnum(BaseEnum apiResponseEnum) {
        this.status = apiResponseEnum.isSuccess();
        this.code = apiResponseEnum.getId();
        this.msg = apiResponseEnum.getLabel();
        this.serverTime = System.currentTimeMillis();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((StringUtils.isEmpty(msg)) ? 0 : msg.hashCode());
        result = prime * result + (status ? 1231 : 1237);
        //result = prime * result + code;
//        result = prime * result + ((ver == null) ? 0 : ver.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Response<?> that = (Response<?>) o;

        if (code != that.code) {
            return false;
        }
        if (status != that.status) {
            return false;
        }
        if (data != null ? !data.equals(that.data) : that.data != null) {
            return false;
        }
        if (msg != null ? !msg.equals(that.msg) : that.msg != null) {
            return false;
        }
//        return !(ver != null ? !ver.equals(that.ver) : that.ver != null);
        return true;

    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
