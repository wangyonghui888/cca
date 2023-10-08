package com.panda.sport.merchant.common.enums.api;

import com.panda.sport.merchant.common.enums.BaseEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Administrator
 */

public enum ApiResponseEnum implements BaseEnum {

    SUCCESS("0000", "SUCCESS", "成功", true),
    FAIL("9999", "FAIL", "系统错误", false),

    /**
     * 公共提示 00
     **/
    PARAMETER_INVALID("1001", "PARAMETER_INVALID", "非法参数", false),

    GAME_ORDERNO_NOT_FOUND("1002", "GAME_ORDERNO_NOT_FOUND", "游戏订单号不存在", false),

    SIGNATURE_FAILED("1003", "SIGNATURE_FAILED", "验签失败", false),

    MERCHANT_NOT_FOUND("2001", "MERCHANT_NOT_FOUND", "没有此商户", false),

    PLAYER_NOT_FOUND("2002", "PLAYER_NOT_FOUND", "没有此玩家", false),

    PLAYER_NAME_EXIST("2003", "PLAYER_NAME_EXIST", "玩家名称重复", false),

    PLAYER_IS_DISABLE("2004", "PLAYER_IS_DISABLE", "此玩家已被禁用", false),

    PLAYER_NAME_INVALID("2005", "PLAYER_NAME_INVALID", "玩家名称不合法", false),

    PLAYER_ORDER_NO("2006", "PLAYER_ORDER_NO", "请勿重复提交", false),


    PLAYER_BALANCE_NOT_ENOUGH("3001", "PLAYER_BALANCE_NOT_ENOUGH", "玩家余额不足", false),

    DUPLICATE_ORDER_NO("3002", "DUPLICATE_ORDER_NO", "订单号重复(投注)", false),

    INVALID_BET_AMOUNT("3003", "INVALID_BET_AMOUNT", "无效的金额(投注)", false),


    REQUEST_TIME_OUT("4001", "REQUEST_TIME_OUT", "请求超时", false),

    LOGIN_FAILED("4002", "LOGIN_FAILED", "登陆失败", false),

    PLAYER_NOT_LOGIN("4003", "PLAYER_NOT_LOGIN", "用户没有登录", false),

    TOKEN_IS_EXPIRED("4004", "TOKEN_IS_EXPIRED", "用户TOKEN已经过期", false),

    TOKEN_IS_REQUIRED("4005", "TOKEN_IS_REQUIRED", "用户TOKEN必传", false),

    USER_LOGIN_FAILED("4006", "USER_LOGIN_FAILED", "登录注册入口已关闭", false),

    SIGN_FAILS("5001", "SIGN_FAILS", "验签失败", false),
    IP_FAILS("5002", "IP_FAILS", "验证IP失败", false),


    INVALID_BALANCE("6001", "INVALID_BALANCE", "无效的金额(转入或转出)", false),

    INVALID_TRANSFER_TYPE("6002", "INVALID_TRANSFER_TYPE", "转账类型不正确", false),

    TRANSFER_MODE_ERROR("6003", "TRANSFER_MODE_ERROR", "商户转点模式错误", false),

    TRANSFER_ID_NOT_EXIST("6004", "TRANSFER_ID_NOT_EXIST", "交易订单号不存在", false),

    TRANSFER_MODE_NOT_MATCH("6005", "TRANSFER_MODE_NOT_MATCH", "panda不提供免转钱包查询余额", false),


    INTERNAL_ERROR("9001", "INTERNAL_ERROR", "系统错误", false),

    RATE_LIMIT_ERROR("9002", "RATE_LIMIT", "限流", false),

    ID_IS_NULL("9003", "ID_IS_NULL", "ID不能为空", false),

    SORT_IS_NULL("9004", "SORT_IS_NULL", "排序不能为空且必须大于0!", false),

    USER_IS_DISABLE("9005", "USER_IS_DISABLE", "用户商户已经被禁用!", false),

    TRANSFER_SYNCHRONIZE_FIAL("9006", "TRANSFER_SYNCHRONIZE_FIAL", "上下分回调转帐失败!", false),
    ;

    protected String id;

    protected String code;

    protected String label;

    protected boolean success;

    ApiResponseEnum(String id, String code, String label, boolean success) {
        this.id = id;
        this.code = code;
        this.label = label;
        this.success = success;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public static ApiResponseEnum getErrorMsg(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return ApiResponseEnum.INTERNAL_ERROR;
        }
        ApiResponseEnum[] resps = ApiResponseEnum.values();
        for (int i = 0; i < resps.length; i++) {
            ApiResponseEnum resp = resps[i];
            if (resp.getLabel().equals(msg)) {
                return resp;
            }
        }
        return ApiResponseEnum.INTERNAL_ERROR;
    }

}
