package com.panda.sport.merchant.api.controller;

import com.panda.sport.merchant.api.aop.RedisAPILimit;
import com.panda.sport.merchant.api.service.impl.KickOutUserServiceImpl;
import com.panda.sport.merchant.api.util.DistributedLockerUtil;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.api.service.UserService;

import com.panda.sport.merchant.api.util.RedisConstants;
import com.panda.sport.merchant.common.po.bss.UserLevelRelationVO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.bss.WindUserPO;
import com.panda.sport.merchant.common.vo.RcsUserSpecialBetLimitConfigVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DistributedLockerUtil distributedLockerUtil;

    @Autowired
    private KickOutUserServiceImpl kickOutUserService;

    @PostMapping("/create")
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
        } catch (Exception e) {
            log.error("login登陆异常," + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @GetMapping(value = "/preLogin")
    public APIResponse<Object> preLogin(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                        @RequestParam(value = "terminal") String terminal,
                                        @RequestParam(value = "merchantCode") String merchantCode,
                                        @RequestParam(value = "currency", required = false) String currency,
                                        @RequestParam(value = "timestamp") Long timestamp) {
        log.info("api/user/preLogin,merchantCode:" + merchantCode + ",terminal:" + terminal + ",userName:" + userName +
                ",timestamp=" + timestamp + ",currency=" + currency);
        if (StringUtils.isAnyEmpty(userName, terminal, merchantCode) || timestamp == null) {
            return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
        }
        try {
            return userService.preLogin(userName, terminal, merchantCode, currency, timestamp);
        } catch (Exception e) {
            log.error("preLogin登陆异常," + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }



    /**
     * @description:查询玩家是否在线
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping(value = "/checkUserOnline")
    @Trace
    public APIResponse<Object> checkUserOnline(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                               @RequestParam(value = "merchantCode") String merchantCode,
                                               @RequestParam(value = "timestamp") Long timestamp,
                                               @RequestParam(value = "signature") String signature) {
        log.info("api/user/checkUserOnline username:" + userName + ",merchantCode:" + merchantCode);
        return StringUtils.isAnyEmpty(userName, merchantCode, signature) || timestamp == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.checkUserOnline(request, userName, merchantCode, timestamp, signature);
    }

    /**
     * @description:刷新玩家余额
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2023/2/24 16:16
     */
    @PostMapping(value = "/refreshBalance")
    @Trace
    public APIResponse<Object> refreshBalance(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                               @RequestParam(value = "merchantCode") String merchantCode,
                                               @RequestParam(value = "timestamp") Long timestamp,
                                               @RequestParam(value = "signature") String signature) {
        log.info("api/user/refreshBalance username:" + userName + ",merchantCode:" + merchantCode);
        return StringUtils.isAnyEmpty(userName, merchantCode, signature) || timestamp == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.refreshBalance(request, userName, merchantCode, timestamp, signature);
    }

    /**
     * @description:踢出玩家
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @RedisAPILimit(comprehensive = true, apiKey = "kickoutMerchantUser", limit = 120, sec = 60)
    @PostMapping(value = "/kickOutUser")
    @Trace
    public APIResponse<Object> kickOutUser(HttpServletRequest request, @RequestParam(value = "userName") String userName,
                                           @RequestParam(value = "merchantCode") String merchantCode,
                                           @RequestParam(value = "timestamp") Long timestamp,
                                           @RequestParam(value = "signature") String signature) {
        log.info("api/user/kickOutUser username:" + userName + ",merchantCode:" + merchantCode);
        return StringUtils.isAnyEmpty(userName, merchantCode, signature) || timestamp == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.kickOutUser(request, userName, merchantCode, timestamp, signature);
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

    @RedisAPILimit(comprehensive = true, apiKey = "kickoutUserInternal", limit = 120, sec = 60)
    @GetMapping("/kickoutUserInternal")
    public APIResponse kickoutUserInternal(HttpServletRequest request, @RequestParam(value = "uid") Long uid
            , @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info(merchantCode + "/api/user/kickoutUserInternal  uid:" + uid);
        return userService.kickoutUserInternal(uid);
    }

    @RedisAPILimit(comprehensive = true, apiKey = "updateUserCache", limit = 1000, sec = 60)
    @GetMapping("/updateUserCache")
    public void updateUserCache(HttpServletRequest request, @RequestParam(value = "uid") Long uid,
                                @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info(merchantCode + "/api/user/updateUserCache  uid:" + uid);
        userService.updateUserCache(uid, merchantCode);
    }

    @PostMapping("/windControlUpdateUserCache")
    public void windControlUpdateUserCache(HttpServletRequest request, @RequestParam(value = "uid") Long uid,@RequestParam(value = "merchantCode", required = false) String merchantCod,
                              @RequestBody WindUserPO userInfo) {
        log.info(merchantCod + "/api/user/windControlUpdateUserCache  uid:" + uid);
        userService.windControlUpdateUserCache(uid,merchantCod,userInfo);
    }

    @PostMapping("/batchUpdateUserCache")
    @ApiOperation(value = "/api/user/batchUpdateUserCache", notes = "批量修改用户缓存")
    public void batchUpdateUserCache(@RequestBody List<Long> uidList, @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("api/user/batchUpdateUserCache:" + merchantCode);
        userService.batchUpdateUserCache(uidList, merchantCode);
    }

    @RedisAPILimit(comprehensive = true, apiKey = "kickoutMerchantUser", limit = 3, sec = 60)
    @GetMapping("/kickoutMerchantUser")
    public APIResponse kickoutMerchantUser(HttpServletRequest request, @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("/api/user/kickoutMerchantUser  merchantCode:" + merchantCode);
        return userService.kickoutMerchantUser(merchantCode);
    }

    @RedisAPILimit(comprehensive = true, apiKey = "kickOutUserMerchant", limit = 3, sec = 60)
    @GetMapping("/kickOutUserMerchant")
    public APIResponse kickOutUserMerchant(HttpServletRequest request, @RequestParam(value = "merchantCode", required = false) String merchantCode,
                                           @RequestParam(value = "userIds", required = false) String userIds) {
        log.info("/api/user/kickOutUserMerchant  KickUserVo:{}", userIds);
        if (StringUtils.isNotEmpty(userIds)) {
            String[] uids = userIds.split(",");
            for (String uid : uids) {
                try {
                    kickOutUserService.kickoutUserInternal(Long.parseLong(uid));
                } catch (Exception e) {
                    log.error("踢出用户API接口异常 参数={}", uid, e);
                }
            }
        } else if (StringUtils.isNotEmpty(merchantCode)) {
            kickOutUserService.kickoutMerchantUser(merchantCode);
        }
        return APIResponse.returnSuccess();
    }

    @RedisAPILimit(comprehensive = true, apiKey = "updateMerchantUserCache", limit = 10, sec = 60)
    @GetMapping("/updateMerchantUserCache")
    public APIResponse updateMerchantUserCache(HttpServletRequest request, @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("/api/user/updateMerchantUserCache  merchantCode:" + merchantCode);
        return userService.updateMerchantUserCache(merchantCode);
    }

    @GetMapping("/updateMaintainCache")
    public APIResponse updateMaintainCache(HttpServletRequest request, @RequestParam(value = "maintainTime", required = false) Long maintainTime) {
        log.info("/api/user/updateMaintainCache  maintainTime:" + maintainTime);
        return userService.updateMaintainCache(maintainTime);
    }

    @GetMapping("/getMaintainCache")
    public APIResponse getMaintainCache(HttpServletRequest request, @RequestParam(value = "maintainTime", required = false) Long maintainTime) {
        log.info("/api/user/updateMaintainCache  maintainTime:" + maintainTime);
        return userService.getMaintainCache();
    }

    /**
     * @description:查询玩家是否在线
     * @Param: [request, merchantCode, orderNo, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<com.panda.sport.merchant.common.vo.api.BetApiVo>
     * @date: 2020/8/23 11:22
     */
    @PostMapping(value = "/modifyUserMarketLevel")
    public APIResponse<Object> modifyUserMarketLevel(HttpServletRequest request, @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "uid", required = false) String uid,
                                                     @RequestParam(value = "merchantCode", required = false) String merchantCode, @RequestParam(value = "marketLevel") String marketLevel,
                                                     @RequestParam(value = "timestamp") Long timestamp, @RequestParam(value = "signature") String signature) {
        log.info("api/user/modifyUserMarketLevel username:" + userName + ",merchantCode:" + merchantCode + ",marketLevel=" + marketLevel + ",uid=" + uid);
        return StringUtils.isAnyEmpty(merchantCode, signature, marketLevel) || timestamp == null ? APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.modifyUserMarketLevel(request, marketLevel, userName, uid, merchantCode, timestamp, signature);
    }

    @PostMapping(value = "/getUserMarketLevel")
    @Trace
    public APIResponse<Object> getUserMarketLevel(HttpServletRequest request, @RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "uid", required = false) String uid,
                                                  @RequestParam(value = "merchantCode") String merchantCode,
                                                  @RequestParam(value = "timestamp") Long timestamp, @RequestParam(value = "signature") String signature) {
        log.info("api/user/getUserMarketLevel username:" + userName + ",merchantCode:" + merchantCode + ",uid=" + uid);
        return StringUtils.isAnyEmpty(merchantCode, signature) || timestamp == null ?
                APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.getUserMarketLevel(request, userName, uid, merchantCode, timestamp, signature);
    }

    /**
     * 对外商户后台更改用户限额
     *
     * @param request
     * @param userInfo
     * @param merchantCode
     */
    @RedisAPILimit(comprehensive = true, apiKey = "updateUserSpecialBettingLimit", limit = 120, sec = 60)
    @PostMapping(value = "/updateUserSpecialBettingLimit")
    public APIResponse updateUserSpecialBettingLimit(HttpServletRequest request, @RequestBody UserPO userInfo, @RequestParam(value = "merchantCode") String merchantCode) {

        log.info("api/user/updateUserSpecialBettingLimit ,merchantCode:" + merchantCode + ",userInfo=" + userInfo);
        return StringUtils.isAnyEmpty(merchantCode) ? APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.updateUserSpecialBettingLimit(userInfo);
    }

    /**
     * 更改日志
     *
     * @param request
     * @param merchantCode
     * @return
     */
    @RedisAPILimit(comprehensive = true, apiKey = "updateUserSpecialBettingLimitLog", limit = 120, sec = 60)
    @PostMapping(value = "/updateUserSpecialBettingLimitLog")
    public APIResponse updateUserSpecialBettingLimitLog(HttpServletRequest request,
                                                        @RequestBody List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList,
                                                        @RequestParam(value = "merchantCode") String merchantCode,
                                                        @RequestParam(value = "userId") Long userId,
                                                        @RequestParam(value = "specialBettingLimit") Integer specialBettingLimit) {

        log.info("api/user/updateUserSpecialBettingLimitLog ,merchantCode:" + merchantCode + ",userId=" + userId);
        return StringUtils.isAnyEmpty(merchantCode) ? APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID) :
                userService.updateUserSpecialBettingLimitLog(rcsUserSpecialBetLimitConfigDataVoList, merchantCode, userId, specialBettingLimit);
    }

    @GetMapping("/queryRedisCache")
    @ApiOperation(value = "/api/user/queryRedisCache", notes = "测试")
    public Response<?> queryRedisCache(@RequestParam(value = "uid") Long uid, @RequestParam(value = "merchantCode") String merchantCode) {
        return userService.queryRedisCache(uid, merchantCode);
    }

    @GetMapping("/queryUidByUserName")
    public Long queryUidByUserName(@RequestParam(value = "userName") String userName) {
        return userService.queryUidByUserName(userName);
    }


}