package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.auth.AuthConstant;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.base.ApiResponseEnum;
import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.service.impl.LoginServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.MaintenancePlatformServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.MaintenanceSportServiceImpl;
import com.panda.multiterminalinteractivecenter.service.impl.UserServiceImpl;
import com.panda.multiterminalinteractivecenter.utils.JWTUtil;
import com.panda.multiterminalinteractivecenter.vo.LoginParamVo;
import com.panda.multiterminalinteractivecenter.vo.LoginUserVo;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.controller
 * @Description :  TODO
 * @Date: 2022-03-11 15:14:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RefreshScope
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private LoginServiceImpl loginService;

     @Autowired
     private MaintenanceSportServiceImpl maintenanceSportService;

     @Value("${open.google.auth:0}")
     private String openGoogleAuth;

    private static String googleChartUrl = "https://chart.googleapis.com/chart";

    @PostMapping(value = "/login")
    public APIResponse<Object> login(@Valid @RequestBody LoginParamVo loginParamVo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMsg = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return APIResponse.returnFail(errorMsg);
        }
        User user = userService.findUserByName(loginParamVo.getName());
        if (user == null){
            return APIResponse.returnFail(AuthConstant.UNKNOWN_ACCOUNT);
        }
        if (!user.getPassword().equals(loginParamVo.getPassword())){
            return APIResponse.returnFail(AuthConstant.WRONG_PASSWORD);
        }
        if (user.getIsEnable() == 0){
            return APIResponse.returnFail(AuthConstant.WRONG_NOT_ENABLE);
        }
        if ("1".equals(openGoogleAuth)){
            if (StringUtils.isEmpty(user.getSecret())){
                return APIResponse.returnFail(AuthConstant.NOT_GOOGLE_AUTH);
            }
            if(!checkCode(user.getSecret(), loginParamVo.getCode())) {
                return APIResponse.returnFail(AuthConstant.WRONG_GOOGLE_AUTH);
            }
        }
        String token = JWTUtil.sign(user.getName(), user.getPassword());
        LoginUserVo loginUserVo = loginService.creatLoginUserVoByName(loginParamVo.getName());
        loginUserVo.setToken(token);
        return APIResponse.returnSuccess(loginUserVo);
    }

    @PostMapping(value = "/getSecretKey")
    public APIResponse<Object> getSecretKey(@RequestBody LoginParamVo loginParamVo) {
        if (StringUtils.isEmpty(loginParamVo.getName())){
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        if (StringUtils.isEmpty(loginParamVo.getPassword())){
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        User user = userService.findUserByName(loginParamVo.getName());
        if (user == null){
            return APIResponse.returnFail(AuthConstant.UNKNOWN_ACCOUNT);
        }
        if (!user.getPassword().equals(loginParamVo.getPassword())){
            return APIResponse.returnFail(AuthConstant.WRONG_PASSWORD);
        }
        String key = null;
        if (StringUtils.isEmpty(user.getSecret())){
            key = generateSecretKey();
            User update = new User();
            update.setId(user.getId());
            update.setSecret(key);
            userService.updateUser(update);
        }else {
            return APIResponse.returnFail(AuthConstant.ALREADY_GEN_AUTH);
        }
        String qcodeUrl = getQcode(user.getName(), key);
        Map<String,String> map = new HashMap<>();
        map.put("key", key);
        map.put("url", qcodeUrl);
        return APIResponse.returnSuccess(map);
    }

    @GetMapping(value = "/test")
    public APIResponse test(@RequestParam Long id,Integer type){
        if (type == 1){
            maintenanceSportService.handleOpenSport(id);
        }else {
            maintenanceSportService.handleCloseSport(id);
        }
        return APIResponse.returnSuccess("11111111");
    }

    @GetMapping(value = "/cleanCache")
    public APIResponse cleanCache(@RequestParam("username") String username){
        LocalCacheService.userCacheMap.invalidate(username);
        return APIResponse.returnSuccess("ok");
    }

    public static String generateSecretKey() {
        GoogleAuthenticator gAuth  = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth .createCredentials();
        String keyStr = key.getKey();
        return keyStr;
    }

    public static String getQcode(String username,String secret) {
        String url = googleChartUrl
                + "?chs=200x200&chld=M|0&cht=qr&chl=otpauth://totp/"+ username + "%3Fsecret%3D" + secret;
        return url;

    }

    public static boolean checkCode(String secret, int code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(secret, code);
        return isCodeValid;
    }

    @Autowired
    private MaintenancePlatformServiceImpl maintenancePlatformService;

    @GetMapping(value = "/querySystemInfo")
    public APIResponse querySystemInfo(@RequestParam(value = "serverCode",required = false) String serverCode){
        return maintenancePlatformService.querySystemInfo(serverCode);
    }

}
