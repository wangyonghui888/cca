package com.oubao.vo;


import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author :  Spring
 * @Project Name :
 * @Package Name :
 * @Description : 
 * @Date: 2019-11-07 下午1:52:31
 */
public enum ApiResponseEnum implements BaseEnum {
	
	
    SUCCESS(                   "0000000", "SUCCESS"           , "成功"         ,  true),
    BlandSUCCESS(              "0000", "SUCCESS"           , "成功"         ,  true),
    FAIL(                      "9999999", "FAIL"           , "失败"         ,  false),
    
    /**公共提示 00**/	
    PARAMETER_INVALID(         "0400300", "PARAMETER_INVALID" , "非法参数"      , false),
    AUTH_FAILS(                "0400401", "AUTH_FAILS" , "认证失败"      , false),
    PERMISSION_ERROR(          "0400403", "PERMISSION_ERROR" , "无权限访问"      , false),
    PARAMETER_CANT_BE_EMPTY(   "0400301", "NO_PARAM" , "参数不能为空"      , false),
    INTERNAL_ERROR(            "0400500", "INTERNAL_ERROR"    , "服务器处理失败" , false),
    RESOURCE_NOT_FOUND(        "0400404", "RESOURCE_NOT_FOUND", "请求地址不存在"     , false),
    PERMISSION_DENIED(         "0400601", "PERMISSION_DENIED" , "无权限操作"     , false),
    TIME_OUT(                  "0400405", "TIME_OUT" , "请求超时"     , false),
    GATE_WAY(                  "0400502", "GATE_WAY" , "网关错误"     , false),
    RESPONSE_LIMIT(            "0400032", "RESPONSE_LIMIT" , "请求IP、地址受限"     , false),
    ADD_FAILS(                 "0400033", "ADD_FAILS","记录已存在",false),
    DELETE_FAILS(              "0400034", "DELETE_FAILS","记录不存在或此赛未被收藏",false),

    /** panda-bss-usercenter 01*/
    NEED_USER_CODE (           "0401001", "NEED_USER_CODE" , "验证码错误"     , false),
    NEED_USER_LOGIN (          "0401002", "NEED_USER_LOGIN" , "请先登录"     , false),
    USER_ACCOUNT_ERROR (       "0401003", "USER_ACCOUNT_MISS" , "账户或密码错误"     , false),
    USER_MISS (                "0401004", "USER_MISS" , "用户不存在"     , false),
    USER_ACCOUNT_MISS (        "0401005", "USER_ACCOUNT_MISS" , "账户不存在或已删除"     , false),
    USER_DISABLED (            "0401006", "USER_DISABLED" , "用户已冻结"     , false),
    BUSINESS_LOGIC_ERROR (     "0401007", "BUSINESS_LOGIC_ERROR" , "业务逻辑错误"     , false),
    PASSWORD_ERROR (           "0401008", "PASSWORD_ERROR" , "密码错误"     , false),
    USER_NAME_NOT_NULL (       "0401009", "USER_NAME_NOT_NULL" , "用户账号不能为空"     , false),
    USER_PASSWORD_NOT_NULL (   "0401010", "USER_PASSWORD_NOT_NULL" , "用户密码不能为空"     , false),
    USER_PHONE_NOT_NULL (      "0401011", "USER_PHONE_NOT_NULL" , "用户手机号不能为空"     , false),
    USER_EXIST (               "0401012", "USER_EXIST" , "用户已存在"     , false),
    USER_PHONE_EXIST (         "0401013", "USER_PHONE_EXIST" , "手机号已存在"     , false),
    USER_LOGIN_EXPIRE(         "0401014","USER_LOGIN_EXPIRE","账户信息已过期，请重新登录",false),
    USER_ACCOUNT_LOCK(         "0401015","ACCOUNT_BALANCE_LACK","账户已被锁定，请联系客服",false),
    ACCOUNT_BALANCE_LACK(      "0401016","ACCOUNT_BALANCE_LACK","您的余额已不足，请先充值",false),
    CURRENCY_CODE_NOT_NULL(    "0401017","CURRENCY_CODE_NOT_NULL","币种不能为空",false),
    RPC_INTER_ERROR(           "0401018","RPC_INTER_ERROR","RPC接口异常",false),
    USER_STORE_OK (            "0401019", "USER_STORE_OK" , "收藏成功"     , true),
    USER_STORE_CANCEL_OK (     "0401020", "USER_STORE_CANCEL_OK" , "取消收藏成功"     , true),
    USER_STORE_FAIL (          "0401021", "USER_STORE_FAIL" , "收藏失败或参数不齐全"     , false),
    USER_STORE_CANCEL_FAIL (   "0401022", "USER_STORE_CANCEL_FAIL" , "取消收藏失败", false),
    USER_STOREFLG_FAIL (       "0401023", "USER_STOREFLG_FAIL" , "操作失败，收藏或取消标识无效", false),
    USER_LEAGUE_NOT_EXIST (         "0401024", "USER_LEAGUE_NOT_EXIST" , "联赛不存在", false),
    USER_LEAGUE_HAS_STORAGE (       "0401025", "USER_LEAGUE_HAS_STORAGE" , "联赛已经被收藏", false),
    USER_LEAGUE_MACTH_STATUS_WRONG ("0401026", "USER_LEAGUE_MACTHSTATUS__WRONG" , "非滚球盘不能收藏", false),
    USER_LEAGUE_MACTH_HAS_STORAGE(  "0401027", "USER_LEAGUE_MACTH_HAS_STORAGE" , "赛事已收藏", false),
    USER_LEAGUE_MACTH_NOT_EXIST (   "0401028", "USER_LEAGUE_MACTH_NOT_EXIST" , "赛事不存在", false),
    USER_UUID_NOT_NULL (   "0401029", "USER_UUID_NOT_NULL" , "登录uuid必须传递 ", false),

    /** panda-bss-order 02*/
    MATCH_STATUS_EXCEPTION(     "0402001","MATCH_STATUS_EXCEPTION","赛事状态异常，投注失败",false),
    MATCH_STATUS_ENDED(         "0402002","MATCH_STATUS_ENDED","比赛结束，投注失败",false),
    MATCH_STATUS_CLOSED(        "0402003","MATCH_STATUS_CLOSED","比赛关闭，投注失败",false),
    MATCH_STATUS_CANCELLED(     "0402004","MATCH_STATUS_CANCELLED","比赛取消，投注失败",false),
    MATCH_STATUS_ABANDONED(     "0402005","MATCH_STATUS_ABANDONED","比赛放弃，投注失败",false),
    MATCH_STATUS_DELAYED(       "0402006","MATCH_STATUS_DELAYED","比赛延迟，投注失败",false),
    MATCH_STATUS_INTERRUPTED(   "0402007","MATCH_STATUS_INTERRUPTED","比赛中断，投注失败",false),
    MARKET_STATUS_NO_ACTIVE(    "0402008","MARKET_STATUS_NO_ACTIVE","盘口非激活，投注失败",false),
    ORDER_ODDS_CHANGE(          "0402009","ORDER_ODDS_CHANGE","您所选投注项的赔率已经产生变化",false),
    ORDER_MARKET_CHANGE(        "0402010","ORDER_MARKET_CHANGE","您所选投注项的盘口值已经产生变化",false),
    ORDER_MARKET_OFF_CHANGE(    "0402011","ORDER_MARKET_OFF_CHANGE","投注项已无效",false),
    ORDER_MARKET_INFO_NOT_EXIST("0402012","ORDER_MARKET_INFO_NOT_EXIST","该盘口投注项信息相关数据不存在",false),
    ORDER_SUCCESS(              "0402013","ORDER_SUCCESS","已下注",true),
    ORDER_MATCH_TYPE_EXCEPTION(              "0402014","ORDER_MATCH_TYPE_EXCEPTION","注单赛事类型数据异常",true),
    SERIES_TYPE_EXCEPTION(              "0402015","SERIES_TYPE_EXCEPTION","解析串关类型值异常",true),
    ORDER_ODDS_ERROR(          "0402009","ORDER_ODDS_ERROR","盘口不存在或无效",false),

    
    /** 接口限流返回  panda-bss-gateway 03*/  
    LIMIT_FLOW(                 "0403001","LIMIT_FLOW","网关限流",false),
    WARNING_MSG(                "0403002","WARNING_MSG","警告提醒消息,赔率数值有变请确认",false),
    PLAY_NOT_EXIST(             "0403003","PLAY_NOT_EXIST","赛程或玩法不存在",false),
    MARKET_CLOSE(               "0403004","MARKET_CLOSE","盘口已关闭",false),
    ORDER_AMOUNT_EXCEPTION(     "0403005","ORDER_AMOUNT_EXCEPTION","注单金额异常",false),
    ORDER_RCS_RESULT(           "0403006","ORDER_RCS_RESULT","订单风控处理异常",false),
    
    /** panda-bss-lottery  04*/ 
    LOTTERY_ERROR(              "0404001","LOTTERY_ERROR","派彩异常",false),

    /** panda-bss-clearing 05*/ 
    CLEARING_ERROR(             "0405001","CLEARING_ERROR","结算异常",false),
    
    /** panda-bss-job 06*/
    JOB_ERROR(                  "0406001","JOB_ERROR","定时任务异常",false),
    
    /** panda-bss-push 07*/
    PUSH_ERROR(                 "0407001","PUSH_ERROR","实时数据推送异常",false),
    
    /** panda-bss-schedule 08*/
    PUSH_SCHEDULE("0408001","PUSH_SCHEDULE","赛程模块异常",false),
    CATEGORY_ERROR("0408002","PARAM_ERROR","请求参数错误",false),
    RPC_ERROR( "0408003","RPC_ERROR","调用RPC服务异常",false),
    CATEGORY_SET_ERROR( "0408004","RPC_ERROR","上游接口异常",false),
    CATEGORY_SET_EMPTY( "0408005","SET_EMPTY","该体育类型无玩法集数据",false),
    MENU_ID_EMPTY( "0408006","NO_PARAM" , "参数menuId不能为空"      , false),
    MERCHANT_NOT_EXIST("0408007", "MERCHANT_NOT_EXIST", "商户不存在!", false),
    PROHIBIT_REGISTRATION("0408008", "PROHIBIT_REGISTRATION", "商户已被禁用,禁止注册新用户!", false),
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
