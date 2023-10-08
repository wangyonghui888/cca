package com.oubao.controller;

import com.google.common.collect.Lists;
import com.oubao.po.UserApiVo;
import com.oubao.po.UserPO;
import com.oubao.service.SDKService;
import com.oubao.service.UserService;
import com.oubao.vo.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SDKService sdkService;

    /**
     * 试玩商户code
     */
    @Value("${try_play_merchant_code:000000}")
    private String tryPlayMerchantCode;

    @GetMapping(value = "/tryPlay")
    public APIResponse<Object> tryPlay(HttpServletRequest request,
                                       @RequestParam(value = "userName", required = false) String userName,
                                       @RequestParam(value = "lang", required = false) String languageName,
                                       @RequestParam(value = "terminal", required = false) String terminal) {
        try {
            log.info("user/tryPlay:---" + userName + ",---" + terminal);
            Boolean flag = checkUserLogin(userName);
            if(flag){
                return APIResponse.returnSuccess(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            Object userVo = userService.tryPlay(null, userName, null, terminal, languageName);
            if (userVo == null) {
                return APIResponse.returnFail(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            return APIResponse.returnSuccess(userVo, ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.login,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 添加新的试玩，登入，注册，马上试玩接口
     *
     * @param username 用户名
     * @param password 密码
     * @param terminal pc 或  h5
     * @return 结果
     */
    @GetMapping(value = "/tryPlayer")
    public APIResponse<Map<String, Object>> tryPlayer(HttpServletRequest request,
                                                      @RequestParam(value = "username") String username,
                                                      @RequestParam(value = "password") String password,
                                                      @RequestParam(value = "terminal", required = false) String terminal) {
        try {
            String ipAddr = IPUtils.getIpAddr(request);
            log.info("user/tryPlayer:---ip{}，username{}，password{}，terminal{}", ipAddr, username, password, terminal);
            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
            }

            Boolean flag = checkUserLogin(username);
            if(flag){
                return APIResponse.returnSuccess(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            UserPO user = new UserPO();
            user.setUsername(username);
            user.setPassword(MD5Utils.MD5Encode(password));
            user.setIpAddress(ipAddr);
            user.setMerchantCode(tryPlayMerchantCode);

            Map<String, Object> resultData = userService.tryPlayer(user, StringUtils.isEmpty(terminal) ? "pc" : terminal);
            return APIResponse.returnSuccess(resultData, ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.tryPlayer,exception:", e);
            return APIResponse.returnFail(e.getMessage());
        }
    }

    private Boolean checkUserLogin(String userName){
        List<String> userNameList = new ArrayList<>();
        Boolean flag = false;
        userNameList.add("pmtyodds1");
        userNameList.add("pmtyodds2");
        userNameList.add("pmtyodds3");
        userNameList.add("pmtyodds4");
        userNameList.add("pmtyodds5");
        userNameList.add("pmtyodds6");
        userNameList.add("pmtyodds7");
        userNameList.add("pmtyodds8");
        userNameList.add("pmtyodds9");
        userNameList.add("pmtyodds010");
        userNameList.add("pmtyodds011");
        userNameList.add("pmtyodds012");
        if(userNameList.contains(userName)){
            flag = true;
        }
        return flag;
    }

    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public APIResponse<UserApiVo> login(HttpServletRequest request, @RequestBody UserVO userVO) {
        try {
            log.info("user/login:" + userVO);
            if (StringUtils.isEmpty(userVO.getUsername()) || StringUtils.isEmpty(userVO.getPassword())) {
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            String merchantCode = userVO.getMerchantCode();
            String userName = userVO.getUsername();
            Boolean flag = checkUserLogin(userName);
            if(merchantCode.equalsIgnoreCase("OO957") && !flag){
                return APIResponse.returnSuccess(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            UserApiVo userVo = userService.login(userVO.getUsername(), userVO.getPhone(),
                    MD5Utils.MD5Encode(userVO.getPassword()), userVO.getMerchantCode());
            if (userVo == null) {
                return APIResponse.returnSuccess(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            return APIResponse.returnSuccess(userVo, ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.login,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * @param userPO
     * @description 添加用户
     */
    @PostMapping("/register")
    public APIResponse register(HttpServletRequest request, @RequestBody UserPO userPO) {
        try {
            log.info("user/register:" + userPO);
            if (userPO == null) {
                return APIResponse.returnSuccess(ApiResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            String merchantCode = userPO.getMerchantCode();
            if(merchantCode.equalsIgnoreCase("OO957")){
                return APIResponse.returnSuccess(ApiResponseEnum.PROHIBIT_REGISTRATION);
            }
            if (StringUtils.isEmpty(userPO.getPassword())) {
                return APIResponse.returnSuccess(ApiResponseEnum.USER_PASSWORD_NOT_NULL);
            }
            if (StringUtils.isEmpty(userPO.getUsername())) {
                return APIResponse.returnSuccess(ApiResponseEnum.USER_NAME_NOT_NULL);
            }
            if (userPO.getUserId() != null) {
                return APIResponse.returnSuccess(ApiResponseEnum.USER_EXIST);
            }
            if (userPO.getCurrencyCode() == null) {
                userPO.setCurrencyCode("1");
            }
            userPO.setIpAddress(IPUtils.getIpAddr(request));
            UserPO userPOUsername = userService.getUser(null, userPO.getUsername(), null, null, userPO.getMerchantCode());
            if (userPOUsername != null) {
                return APIResponse.returnSuccess(ApiResponseEnum.USER_EXIST);
            }
            if (tryPlayMerchantCode.equals(userPO.getMerchantCode())) {
                userPO.setBalance(10000000.0);
            }
            userService.register(userPO);
            return APIResponse.returnSuccess(ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.register,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/queryUserList")
    public APIResponse<PageVO<UserPO>> queryUserList(@RequestParam(value = "userName", required = false) String userName,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime,
                                                     @RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "size", required = false) Integer size,
                                                     @RequestParam(value = "merchantCode", required = false) String merchantCode
    ) {
        try {
            return APIResponse.returnSuccess(userService.queryUserList(userName, startTime, endTime, merchantCode, page, size));
        } catch (Exception e) {
            log.error("UserController.queryUserList,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @GetMapping(value = "/loginPanda", produces = "application/json;charset=utf-8")
    public APIResponse<Object> loginPanda(HttpServletRequest request,
                                          @RequestParam(value = "userName") String userName,
                                          @RequestParam(value = "terminal") String terminal,
                                          @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        try {
            log.info("loginPanda:---" + userName + ",---" + terminal + ",---" + merchantCode);
            if (StringUtils.isAnyEmpty(userName, terminal)) {
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            Boolean flag = checkUserLogin(userName);
            if(merchantCode.equalsIgnoreCase("OO957") && !flag){
                return APIResponse.returnSuccess(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            Object userVo = userService.loginPanda(userName, terminal, merchantCode);
            if (userVo == null) {
                return APIResponse.returnSuccess(ApiResponseEnum.USER_ACCOUNT_ERROR);
            }
            return APIResponse.returnSuccess(userVo, ApiResponseEnum.SUCCESS);
        } catch (Exception e) {
            log.error("UserController.login,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping("/sdkLogin")
    public APIResponse sdkLogin(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                @RequestParam(value = "merchantCode") String merchantCode,
                                @RequestParam(value = "terminal") String terminal,
                                @RequestParam(value = "balance", required = false) Double balance,
                                @RequestParam(value = "callbackUrl", required = false) String callbackUrl,
                                @RequestParam(value = "merchantKey") String merchantKey
    ) {
        log.info("sdkLogin " + userName + ":" + userName);

        return sdkService.sdkLogin(userName, merchantCode, terminal, balance, callbackUrl, merchantKey);

    }

    @GetMapping("/sdkGetBetDetail")
    public APIResponse sdkGetBetDetail(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                       @RequestParam(value = "orderNo") String orderNo,
                                       @RequestParam(value = "merchantKey") String merchantKey
    ) {
        log.info("sdkGetBetDetail ", orderNo + ":" + orderNo);
        return sdkService.sdkGetBetDetail(merchantCode, orderNo, merchantKey);

    }

    @GetMapping("/sdkGetTransferRecord")
    public APIResponse sdkGetTransferRecord(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                            @RequestParam(value = "userName") String userName,
                                            @RequestParam(value = "transferId") String transferId,
                                            @RequestParam(value = "merchantKey") String merchantKey
    ) {
        log.info("sdkGetTransferRecord ", transferId + ":" + transferId);
        return sdkService.sdkGetTransferRecord(merchantCode, userName, transferId, merchantKey);
    }

    @GetMapping("/sdkQueryTransferList")
    public APIResponse sdkQueryTransferList(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                            @RequestParam(value = "userName") String userName,
                                            @RequestParam(value = "startTime") String startTime,
                                            @RequestParam(value = "endTime") String endTime,
                                            @RequestParam(value = "pageNum") Integer pageNum,
                                            @RequestParam(value = "pageSize") Integer pageSize,
                                            @RequestParam(value = "merchantKey") String merchantKey
    ) {
        log.info("sdkQueryTransferList ", userName + ":" + userName);
        return sdkService.sdkQueryTransferList(merchantCode, userName, startTime, endTime, pageNum, pageSize, merchantKey);
    }
}
