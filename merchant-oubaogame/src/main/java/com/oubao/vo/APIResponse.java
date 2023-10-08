package com.oubao.vo;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class APIResponse<T> implements Serializable {


    private static final long serialVersionUID = 5241526151768786394L;

    private boolean status;
    private String msg = "";
    private String code;
    private T data;
    private Long serverTime;//服务器时间

    public APIResponse() {
        this.setApiResponseEnum(ApiResponseEnum.SUCCESS);
    }

    private APIResponse(T t) {
        this();
        this.data = t;
    }

    private APIResponse(ApiResponseEnum ret) {
        this.setApiResponseEnum(ret);
    }

    private APIResponse(BaseEnum ret, T t) {
        this.setApiResponseEnum(ret);
        this.data = t;
    }

    private APIResponse(String msg, T t) {
        this.msg = msg;
        this.data = t;
    }

    private APIResponse(String msg, T t, boolean status) {
        this.msg = msg;
        this.data = t;
        this.status = status;
    }

    private APIResponse(String msg, T t, boolean status, String code) {
        this.msg = msg;
        this.data = t;
        this.status = status;
        this.code = code;

    }

    public static <T> APIResponse<T> returnSuccess() {
        return new APIResponse();
    }

    public static <T> APIResponse<T> returnSuccess(T data) {
        return new APIResponse(ApiResponseEnum.SUCCESS, data);
    }
    
    public static <T> APIResponse<T> returnSuccess(ApiResponseEnum responseEnum,T data) {
        return new APIResponse(responseEnum, data);
    }

    public static <T> APIResponse<T> returnSuccess(T data, BaseEnum ret) {
        return new APIResponse(ret, data);
    }

    public static <T> APIResponse<T> returnSuccess(T data, String msg) {
        return new APIResponse(msg, data, true, ApiResponseEnum.SUCCESS.getId());
    }

    public static <T> APIResponse<T> returnSuccess(BaseEnum ret) {
        return new APIResponse(ret, null);
    }

    public static <T> APIResponse<T> returnFail(BaseEnum ret, String appendErrmsg, T data) {
        APIResponse apiResponse = returnFail(ret, appendErrmsg);
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> APIResponse<T> returnFail(BaseEnum ret, String appendErrmsg) {
        APIResponse apiResponse = returnFail(ret);
        if (appendErrmsg != null) {
            apiResponse.msg = apiResponse.msg + "（" + appendErrmsg + "）";
        }
        return apiResponse;
    }

    public static <T> APIResponse<T> returnFail(BaseEnum ret) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.status = false;
        apiResponse.msg = ret.getLabel();
        apiResponse.code = ret.getId();
        return apiResponse;

        //return new APIResponse(ret); // @2016/06/01 zhuo.shi 删除此行，此行会返回SUCCESS，不是Fail信息
    }


    public static <T> APIResponse<T> returnFail(String msg) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.status = false;
        apiResponse.msg = msg;
        apiResponse.code = ApiResponseEnum.FAIL.getId();
        return apiResponse;
    }

    public static <T> APIResponse<T> returnFail(T data, BaseEnum ret) {
        return new APIResponse(ret, data);
    }

    public static <T> APIResponse<T> returnFail(T data, String msg) {
        return returnSuccess(data, msg);
    }

    public static <T> APIResponse<T> returnFail(String code, String msg) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.code = code;
        apiResponse.msg = msg;
        apiResponse.status = false;
        return apiResponse;
    }

    public static <T> APIResponse<T> returnFail(String code, String msg, T data) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.code = code;
        apiResponse.msg = msg;
        apiResponse.status = false;
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> APIResponse<T> returnSuccess(String code, String msg, T data) {
        APIResponse apiResponse = new APIResponse();
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

        APIResponse<?> that = (APIResponse<?>) o;

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
