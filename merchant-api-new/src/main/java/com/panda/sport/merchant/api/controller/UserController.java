package com.panda.sport.merchant.api.controller;

import com.panda.sport.merchant.api.service.UserService;
import com.panda.sport.merchant.api.util.DistributedLockerUtil;
import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DistributedLockerUtil distributedLockerUtil;

    @PostMapping("/create")
    @Trace
    public APIResponse create(HttpServletRequest request, @RequestParam(value = "userName") String username,
                              @RequestParam(value = "nickname", required = false) String nickname,
                              @RequestParam(value = "merchantCode") String merchantCode,
                              @RequestParam(value = "timestamp") Long timestamp,
                              @RequestParam(value = "currency", required = false) String currency,
                              @RequestParam(value = "agentId", required = false) String agentId,
                              @RequestParam(value = "signature") String signature) {
        log.info("api/user/create,merchantCode:" + merchantCode + ",username:" + username + ",timestamp" + timestamp +
                ",currency" + currency + ",agentId" + agentId + ",signature" + signature);
        if (StringUtils.isAnyEmpty(username, merchantCode, signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            distributedLockerUtil.lock(RedisConstants.CREAT_FAMILY_KEY + merchantCode + username);
            return userService.create(request, username, nickname, merchantCode, timestamp, currency, agentId, signature);
        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("UserController.create,index,exception,索引重复:" + username, e);
            return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_EXIST);
        } catch (Exception e) {
            log.error("UserController.create,exception:" + username, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        } finally {
            distributedLockerUtil.unLock(RedisConstants.CREAT_FAMILY_KEY + merchantCode + username);
        }
    }

    /**
     * @description:登录 imitate:模拟登陆(预加载使用)
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * jumpfrom :如果是从其他跳转过来的，则设置该参数。 ty：从体育跳转；zr：从真⼈跳转；qp：从棋牌跳转；
     * dy：从电游（捕⻥）跳转；lhj：从电游（⽼⻁机）；cp：从彩票跳转；dj：从电竞跳转表示C端⽀持跳转的游戏，
     * 如果C端⽀持跳转多个游戏，则使⽤逗号区分。ty：表示⽀持跳转到体育；zr：⽀持跳转真⼈；qp：⽀持跳转棋牌；dy：
     * ⽀持跳转电游（捕⻥）；lhj：表示⽀持跳转电游（⽼⻁机）；cp：表示⽀持跳转彩票；dj：表示⽀持跳转真⼈那
     * @date: 2020/8/23 11:22
     */
    @PostMapping(value = "/login")
    @Trace
    public APIResponse<Object> login(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                     @RequestParam(value = "terminal") String terminal,
                                     @RequestParam(value = "merchantCode") String merchantCode,
                                     @RequestParam(value = "imitate", required = false) Boolean imitate,
                                     @RequestParam(value = "currency", required = false) String currency,
                                     @RequestParam(value = "stoken", required = false) String stoken,
                                     @RequestParam(value = "agentId", required = false) String agentId,
                                     @RequestParam(value = "callbackUrl", required = false) String callbackUrl,
                                     @RequestParam(value = "language", required = false) String language,
                                     @RequestParam(value = "jumpsupport", required = false) String jumpsupport,
                                     @RequestParam(value = "jumpfrom", required = false) String jumpfrom,
                                     @RequestParam(value = "ip", required = false) String ip,
                                     @RequestParam(value = "timestamp") Long timestamp,
                                     @RequestParam(value = "signature") String signature) {
        log.info("api/user/login,merchantCode:" + merchantCode + ",terminal:" + terminal + ",userName:" + userName + ",imitate:" + imitate +
                ",timestamp=" + timestamp + ",currency=" + currency + ",callbackUrl=" + callbackUrl + ",signature=" + signature + ",jumpsupport=" + jumpsupport +
                ",jumpfrom=" + jumpfrom + ",ip=" + ip + ",stoken=" + stoken + ",agentId=" + agentId + ",language=" + language);
        if (StringUtils.isAnyEmpty(userName, terminal, merchantCode, signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return userService.login(request, userName, terminal, merchantCode, currency, callbackUrl, timestamp, signature, jumpsupport, jumpfrom, ip, stoken, agentId, imitate, language);
        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("UserController.create,index,exception,索引重复:" + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_EXIST);
        } catch (Exception e) {
            log.error("login登陆异常," + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * K8S 服务心跳检测接口
     * ！！！ 误删 ！！！
     * @param request
     * @return
     */
    @GetMapping("/test")
    public APIResponse test(HttpServletRequest request) {
        return APIResponse.returnSuccess("success!");
    }

    @GetMapping("/api/user/refresh/front/domain")
    public void refreshFrontDomain() {
        userService.refreshFrontDomain();
    }


    /**
     * @description:登录 imitate:模拟登陆(预加载使用)
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * jumpfrom :如果是从其他跳转过来的，则设置该参数。 ty：从体育跳转；zr：从真⼈跳转；qp：从棋牌跳转；
     * dy：从电游（捕⻥）跳转；lhj：从电游（⽼⻁机）；cp：从彩票跳转；dj：从电竞跳转表示C端⽀持跳转的游戏，
     * 如果C端⽀持跳转多个游戏，则使⽤逗号区分。ty：表示⽀持跳转到体育；zr：⽀持跳转真⼈；qp：⽀持跳转棋牌；dy：
     * ⽀持跳转电游（捕⻥）；lhj：表示⽀持跳转电游（⽼⻁机）；cp：表示⽀持跳转彩票；dj：表示⽀持跳转真⼈那
     * @date: 2020/8/23 11:22
     */
    @PostMapping(value = "/tokenRenew")
    @Trace
    public APIResponse<Object> tokenRenew(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                     @RequestParam(value = "merchantCode") String merchantCode,
                                     @RequestParam(value = "token", required = false) String token,
                                     @RequestParam(value = "timestamp") Long timestamp,
                                     @RequestParam(value = "signature") String signature) {
        log.info("api/user/login,merchantCode:" + merchantCode + ",userName:" + userName +
                ",timestamp=" + timestamp + ",signature=" + signature +  ",token=" + token );
        if (StringUtils.isAnyEmpty(userName, token, merchantCode, signature) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return userService.token(request, userName,  merchantCode,  timestamp, signature,token);
        } catch (SQLIntegrityConstraintViolationException e) {
            log.error("UserController.create,index,exception,索引重复:" + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_EXIST);
        } catch (Exception e) {
            log.error("login登陆异常," + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }
}