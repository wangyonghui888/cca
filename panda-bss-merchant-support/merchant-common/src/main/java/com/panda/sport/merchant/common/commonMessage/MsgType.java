package com.panda.sport.merchant.common.commonMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description :  错误码（枚举建议首字母大写，未大写的以后统一）
 * @Date: 2019-08-29 下午1:58:52
 */
public enum MsgType {

    // 错误码定义 1 - 500
    Success("200", "成功"),
    Fail("100", "失败"),
    PARAMETER_INVALID( "300",  "非法参数" ),
    //400 - 500 服务器错误
    SERVER_ERROR("500", "服务器内部异常，请联系管理员。"),
    TOKEN_INVALID("511","token无效"),
    SystemErr("9999", "系统未知异常"),
    NO_ARGS("50001","参数不能为空");

    /**
     * 错误编号
     */
    private String code;

    /**
     * 错误提示
     */
    private String msg;

    MsgType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 静态缓存，提高效率
     */
    private static Map<String, MsgType> cache;

    static {
        cache = new HashMap<String, MsgType>();
        MsgType[] values = MsgType.values();
        for (MsgType error : values) {
            cache.put(error.code, error);
        }
    }

    /**
     * <pre>
     * 获取消息内容
     * </pre>
     *
     * @param code
     * @return
     */
    public static String getMsg(String code) {
        if (code != null && code.length() > 0) {
            MsgType msgType = cache.get(code);
            if (msgType != null) {
                return msgType.getMsg();
            }
        }

        return MsgType.SystemErr.getMsg();
    }
}