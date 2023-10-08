package com.panda.multiterminalinteractivecenter.auth;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.auth
 * @Description :  TODO
 * @Date: 2022-03-14 14:17:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public class AuthConstant {

    /**
     * cookie中存储的token字段名
     */
    public final static String COOKIE_TOKEN_NAME = "Authorization";

    /**
     * token有效时间 时*分*秒*1000L
     */
    public final static Long EXPIRE_TIME = 3*60*1000L;//先设置3分钟

    //登录认证结果,返回给前端
    public final static String UNKNOWN_ACCOUNT = "登录失败, 用户不存在。";

    public final static String WRONG_PASSWORD = "登录失败，密码错误。";

    public final static String WRONG_NOT_ENABLE = "登录失败，用户禁用。";

    public final static String TOKEN_BLANK = "验证失败，token为空，请登录。";

    public final static String TOKEN_INVALID = "验证失败，token错误。";

    public final static String TOKEN_EXPIRE = "验证失败，token过期，请重新登录。";

    public final static String AUTHENTICATE_FAIL = "无访问权限，请尝试登录或联系管理员。";

    public final static String WRONG_GOOGLE_AUTH = "登录失败，谷歌验证码错误。";

    public final static String NOT_GOOGLE_AUTH = "登录失败，请先绑定谷歌验证码。";

    public final static String ALREADY_GEN_AUTH = "您已成功生成密钥，无法再次生成，如忘记请联系管理员。";

}
