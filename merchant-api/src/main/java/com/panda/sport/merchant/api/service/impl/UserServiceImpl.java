package com.panda.sport.merchant.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.bss.mapper.*;
import com.panda.sport.merchant.api.config.IdGeneratorFactory;
import com.panda.sport.merchant.api.feign.MerchantOrderClient;
import com.panda.sport.merchant.api.feign.MultiterminalClient;
import com.panda.sport.merchant.api.mq.MQMsgInfo;
import com.panda.sport.merchant.api.mq.MessageProduct;
import com.panda.sport.merchant.api.service.UserService;
import com.panda.sport.merchant.api.util.*;
import com.panda.sport.merchant.common.base.MQMsgBody;
import com.panda.sport.merchant.common.base.UserInfo;
import com.panda.sport.merchant.common.constant.RedisKeyNameConstant;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.DomainTypeEnum;
import com.panda.sport.merchant.common.enums.GroupTypeEnum;
import com.panda.sport.merchant.common.enums.MarketTypeEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.JsonUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.api.*;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.api.util.RedisConstants.*;

@Slf4j
@Service("userService")
@RefreshScope
@Primary
public class UserServiceImpl extends AbstractMerchantService implements UserService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private UserOrderUpdateMapper userOrderUpdateMapper;

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Autowired
    private ActivityMerchantMapper activityMerchantMapper;

    @Autowired
    private RcsUserSpecialBetLimitConfigMapper rcsUserSpecialBetLimitConfigMapper;

    @Autowired
    private MerchantVideoManageMapper videoManageMapper;

    @Autowired
    private MerchantChatRoomSwitchMapper chatRoomSwitchMapper;

    @Autowired
    private IpUtil ipUtil;

    @Autowired
    private MultiterminalClient multiterminalClient;

    @Autowired
    private MessageProduct messageProduct;

    @Autowired
    private MerchantOrderClient merchantOrderClient;

    @Value("${front_pc_url}")
    private String frontPcUrl;

    @Value("${front_h5_url}")
    private String frontH5Url;

    @Value("${live_h5_url:null}")
    private String liveH5Url;

    @Value("${chatroom_url:null}")
    private String chatroomUrl;

    @Value("${chatroom_http_url:null}")
    private String chatroomHttpUrl;


    @Value("${live_pc_url:null}")
    private String livePcUrl;

    @Value("${front_inh5_url}")
    private String frontinH5Url;

    @Value("${front_mobile_url}")
    private String frontMobileUrl;

    @Value("${front_bwh5_url}")
    private String front_bwh5_url;

    @Value("${all_user_login_off:on}")
    private String allUserLoginOff;

    @Value("${merchant_user_login_off:null}")
    private String merchantUserLoginOff;

    @Value("${merchant_session:null}")
    private String merchantSession;

    @Value("${marketLevelSwitch:on}")
    private String marketLevelSwitch;

    @Value("${imageDomain:null}")
    private String imageDomain;

    @Value("${filter.league.ids:-1}")
    private String filterLeagueIds;

    @Value("${filter.merchant.code:test}")
    private String filterMerchantCode;

    @Value("${special.merchant.theme:111111}")
    private String specialTheme;

    @Value("${login.switch:on}")
    private String loginSwitch;

    @Value("${login.history.switch:off}")
    private String loginHistorySwitch;

    @Value("${login.domain.switch:off}")
    private String domainPoolSwitch;

    @Value("${login.domain.pool.merchant.codes:null}")
    private String domainPoolMerchantCodeStr;

    // 用户余额主动刷新
    private static final String topic = "USER_BALANCE_CHANGE";


    private static final Map<String, Integer> marketMap = new HashMap<>();
    private static final Map<Integer, String> marketReverseMap = new HashMap<>();

    public static Cache<String, List<DomainGroupApiVO>> domainPoolCacheMap = null;

    public static Cache<String, List<UserVipVO>> merchantVipDetailCacheMap = null;

    public static Cache<String, String> merchantAreaDomainsCacheMap = null;

    /**
     * 单次批量数量
     */
    public static int BATCH_COUNT = 1000;

    @PostConstruct
    private void init() {
        marketMap.put("A", 1);
        marketMap.put("B", 2);
        marketMap.put("C", 3);
        marketMap.put("D", 4);
        marketMap.put("0", 0);
        marketMap.put("1", 11);
        marketMap.put("2", 12);
        marketMap.put("3", 13);
        marketMap.put("4", 14);
        marketMap.put("5", 15);
        marketReverseMap.put(1, "A");
        marketReverseMap.put(2, "B");
        marketReverseMap.put(3, "C");
        marketReverseMap.put(4, "D");
        marketReverseMap.put(0, "0");
        marketReverseMap.put(11, "1");
        marketReverseMap.put(12, "2");
        marketReverseMap.put(13, "3");
        marketReverseMap.put(14, "4");
        marketReverseMap.put(15, "5");

        domainPoolCacheMap = Caffeine.newBuilder()
                .maximumSize(3_000) // 设置缓存的最大容量
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .recordStats() // 开启缓存统计
                .build();

        merchantVipDetailCacheMap = Caffeine.newBuilder()
                .maximumSize(3_000) // 设置缓存的最大容量
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        merchantAreaDomainsCacheMap = Caffeine.newBuilder()
                .maximumSize(3_000) // 设置缓存的最大容量
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .recordStats() // 开启缓存统计
                .build();

    }

    /**
     * 注册玩家
     * a 验签 b 去重 c 注册 d 初始化余额 e 发送风控 校验用户注册登录禁止是否开启 ，商户下用户登录注册禁止是否开启
     *
     * @Param: [request, username, nickname, merchantCode, timestamp, currency, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse
     * @date: 2020/8/23 11:36
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse create(HttpServletRequest request, String username, String nickname, String merchantCode,
                              Long timestamp, String currency, String agentId, String signature) throws Exception {
        try {
            APIResponse error = checkUserLogin(merchantCode, null);
            if (error != null) {
                return error;
            }
            String signStr = username + "&" + merchantCode + "&" + timestamp;
            APIResponse<Object> errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                log.error("创建账号异常!" + errorResult);
                return errorResult;
            }
            // UserPO userPO = userMapper.getUserByUserName(merchantCode, username);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
            if (userPO != null) {
                log.error(merchantCode + "创建账号异常!用户已存在!" + username);
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_EXIST);
            }
            //Long userId = snowflakeIdWorker.nextId();
            String userIdStr = idGeneratorFactory.generateIdByBussiness("user", 5);
            UserPO newUser = new UserPO();
            newUser.setUserId(Long.parseLong(userIdStr));
            newUser.setPassword(merchantCode);
            newUser.setMerchantCode(merchantCode);
            newUser.setRealName(nickname);
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            Integer merchantTag = merchantPO.getMerchantTag();
            if (merchantTag != null && merchantTag == 1 && StringUtils.isEmpty(agentId)) {
                log.error(merchantCode + "信用网创建账号异常!用代理不存在!" + username);
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
            }
            Integer userPrefix = merchantPO.getUserPrefix();
            newUser.setFakeName(getFakeName(merchantCode, username, userPrefix));
            newUser.setUsername(username);
            String currencyCode;
            if (merchantPO.getMerchantTag() != null && merchantPO.getMerchantTag() == 1) {
                newUser.setSpecialBettingLimitType(5);
            }
            log.info("createUser userId:" + userIdStr + " merchantTag:" + merchantPO.getMerchantTag());
            if (StringUtils.isEmpty(currency) || currency.equalsIgnoreCase("null")) {
                currencyCode = merchantPO.getCurrency() == null ? CurrencyTypeEnum.RMB.getId() : (merchantPO.getCurrency() + "");
            } else {
                currencyCode = currency;
            }
            if (!StringUtils.isNumeric(currencyCode) || !CurrencyTypeEnum.validateCurrency(currencyCode)) {
                log.error("{}1创建账号异常!币种无效!{}", merchantCode, currencyCode);
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
            }
            newUser.setCurrencyCode(currencyCode);
            newUser.setUserLevel(230);
            newUser.setIpAddress("111");
            newUser.setUserBetPrefer(2);
            newUser.setAgentId(agentId);
            long nowTime = System.currentTimeMillis();
            newUser.setCreateUser(username);
            newUser.setCreateTime(nowTime);
            newUser.setModifyTime(nowTime);
            newUser.setSettleInAdvance(1);
            newUser.setSettleSwitchBasket(1);
            return createUserService.createUserByObj(newUser);
        } catch (Exception e) {
            log.error(username + ",UserController.create,exception:" + merchantCode, e);
            throw new Exception("创建用户失败!");
        }
    }

    @Override
    public APIResponse<Object> preLogin(String userName, String terminal, String merchantCode, String currency, Long timestamp) throws Exception {
        APIResponse result = this.login(null, userName, terminal, merchantCode, currency, null, timestamp, null, null, null, null, null, null, true, null);
        return result;
    }

    /**
     * 登录即注册
     * a 验签 b获取token c单点登录 d记录登录历史
     * //校验用户注册登录禁止是否开启 ，商户下用户登录注册禁止是否开启
     *
     * @param terminal
     * @param merchantCode
     * @param callbackUrl
     * @param timestamp
     * @param signature
     * @param jumpsupport
     * @param jumpfrom
     * @param imitate(模拟登陆,预加载)
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse<Object> login(HttpServletRequest request, String username, String terminal, String merchantCode, String currency,
                                     String callbackUrl, Long timestamp, String signature, String jumpsupport, String jumpfrom, String ip, String stoken, String agentId, Boolean imitate, String language) throws Exception {
        try {
            if (loginSwitch.equals("off")) {
                return APIResponse.returnFail(ApiResponseEnum.RATE_LIMIT_ERROR);
            }

            APIResponse error = checkUserLogin(merchantCode, imitate);
            if (error != null) {
                log.error("用户登录开关已关!" + merchantCode);
                return error;
            }

            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            if (merchantPO == null || (merchantPO.getAgentLevel() != 0 && merchantPO.getAgentLevel() != 2)) {
                return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
            }

            final String domainGroupCode = StringUtils.isEmpty(merchantPO.getDomainGroupCode()) ? "common" : merchantPO.getDomainGroupCode();

            UserApiVo userApiVo = (UserApiVo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + Colon + terminal + language, UserApiVo.class);

            if (userApiVo != null && userApiVo.getUserId() != null) {
                boolean hasToken = redisTemp.hasKey(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + userApiVo.getToken());
                log.info(username + imitate + "hasToken:" + hasToken + ",redis缓存获取用户login:" + userApiVo);
                if (hasToken) {
                    if (imitate == null || !imitate) {
                        logHistory(Long.parseLong(userApiVo.getUserId()), username, jumpfrom, terminal, merchantCode);
                    }
                    if (StringUtils.isNotEmpty(stoken)) {
                        redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_S_TOKEN_PREFIX + userApiVo.getUserId(), stoken, EXPIRE_TIME_TWO_WEEK);
                    }
                    if (!assemblyDomainInfo(userApiVo, merchantPO, terminal, ip)) {
                        String appDomain = merchantPO.getAppDomain();
                        if (StringUtils.isNotEmpty(appDomain)) {
                            if (appDomain.contains(",")) {
                                String[] domainArray = appDomain.split(",");
                                appDomain = domainArray[0];
                            }
                            userApiVo.setApiDomain(appDomain);
                            userApiVo.setImgDomain(imageDomain);
                        }
                        String oldDomain = userApiVo.getDomain();
                        String newDomain = getDomain(terminal, merchantPO);
                        if (!oldDomain.equalsIgnoreCase(newDomain)) {
                            log.info(username + oldDomain + "用戶緩存讀取商戶最新緩存切換域名！" + newDomain);
                            userApiVo.setDomain(newDomain);
                            String loginUrl = userApiVo.getLoginUrl();
                            if (StringUtils.isNotEmpty(loginUrl)) {
                                loginUrl = loginUrl.replace(oldDomain, newDomain);
                                userApiVo.setLoginUrl(loginUrl);
                            }
                        }
                        String loginUrl = userApiVo.getLoginUrl();
                        if (StringUtils.isNotEmpty(loginUrl)) {
                            String[] addr = loginUrl.split("&");
                            String apiStr = addr[addr.length - 1];
                            if (apiStr.contains("api")) {
                                //新增客户端域名数组
                                String apiDomain = userApiVo.getApiDomain();
                                if (StringUtils.isNotEmpty(apiDomain)) {
                                    String api = AesUtil.encrypt(apiDomain);
                                    log.info(apiDomain + ",cache.encrypt=" + api);
                                    String newLoginUrl = loginUrl.replace(apiStr, "api=" + api);
                                    userApiVo.setLoginUrl(newLoginUrl);
                                }
                            }
                            List<String> domainStr = Lists.newArrayList();
                            domainStr.add(userApiVo.getLoginUrl());
                            userApiVo.setLoginUrlArr(domainStr);
                        }
                    }
                    return APIResponse.returnSuccess(new UserApiResultVo(userApiVo));
                }
            }
            if (Math.abs((System.currentTimeMillis() - timestamp) / (1000 * 60 * 60)) > 23) {
                return APIResponse.returnFail(ApiResponseEnum.REQUEST_TIME_OUT);
            }
            if ((imitate == null || !imitate) && !this.checkIp(request, merchantPO.getWhiteIp()) && ipCheckSwitch.equalsIgnoreCase("on")) {
                String originIp = IPUtils.getIpAddr(request);
                log.error(imitate + ",请求IP:" + originIp + ", 验证IP失败:" + merchantPO.getWhiteIp());
                //StringBuilder sb = new StringBuilder();
                //telegramService.sendTelegram(sb.append(merchantPO.getMerchantName()).append(",").append(merchantCode).append(",").append("验证IP失败!!!请求IP:").append(originIp).append(",白名单:").append(merchantPO.getWhiteIp()));
                return APIResponse.returnFail(ApiResponseEnum.IP_FAILS);
            }
            String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
            String secondKey = "";
            if(StringUtils.isNotEmpty(merchantPO.getSecondMerchantKey())){
                secondKey = AESUtils.aesDecode(merchantPO.getSecondMerchantKey());
            }
            if (StringUtil.isBlankOrNull(key) && StringUtil.isBlankOrNull(secondKey)) {
                return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
            }
            String signStr = merchantCode + "&" + username + "&" + terminal + "&" + timestamp;
            Boolean keyFlag = !Md5Util.checkMd5(signStr, key, signature);
            Boolean secondKeyFlag = !Md5Util.checkMd5(signStr, secondKey, signature);
            if ((imitate == null || !imitate) && keyFlag && secondKeyFlag) {
                log.error(imitate + ",验签失败!" + key);
                return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
            }
            /*String key = AESUtils.aesDecode(merchantPO.getMerchantKey());
            if (StringUtil.isBlankOrNull(key)) {
                return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
            }
            if ((imitate == null || !imitate) && !Md5Util.checkMd5(merchantCode + "&" + username + "&" + terminal + "&" + timestamp, key, signature)) {
                log.error(imitate + ",验签失败!" + key);
                return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
            }*/
            UserPO userPO;
            List<UserLevelRelationVO> userLevelList;
            try {
                //userPO = userMapper.getUserByUserName(merchantCode, username);
                userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
                if (userPO == null) {
                    String currencyCode;
                    if (StringUtils.isEmpty(currency) || currency.equalsIgnoreCase("null")) {
                        currencyCode = merchantPO.getCurrency() == null ? CurrencyTypeEnum.RMB.getId() : (merchantPO.getCurrency() + "");
                    } else {
                        currencyCode = currency;
                    }
                    if (!StringUtils.isNumeric(currencyCode) || !CurrencyTypeEnum.validateCurrency(currencyCode)) {
                        log.error("{}2创建账号异常!币种无效!{}", merchantCode, currencyCode);
                        return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
                    }
                    Integer userPrefix = merchantPO.getUserPrefix();
                    userPO = createUserService.createUser(username, merchantCode, currencyCode, getFakeName(merchantCode, username, userPrefix), agentId, merchantPO.getMerchantTag());
                    if (userPO == null) {
                        return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_INVALID);
                    }
                } else {
                    if (null != userPO.getDisabled() && 1 == userPO.getDisabled()) {
                        log.info("禁用用户信息=：{}", userPO);
                        return APIResponse.returnFail(ApiResponseEnum.USER_IS_DISABLE);
                    }
                    if (StringUtils.isNotBlank(agentId) && !agentId.equals(userPO.getAgentId())) {
                        int count = userMapper.updateUserAgentId(userPO.getUserId(), agentId);
                        log.info("ds" + count);
                    }
                }
                userLevelList = userMapper.getUserLevelList(userPO.getUserId());
            } catch (Exception e) {
                log.error("UserServiceImpl.login.创建用户失败:" + username, e);
                throw new Exception("UserServiceImpl.login.创建用户失败!");
            }
            Long userId = userPO.getUserId();
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(userPO, userInfo);
            userInfo.setDynamicMargin(getExtraMargin(userLevelList));
            userInfo.setUserMarketLevel(userPO.getMarketLevel());
            userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, terminal, userLevelList));
            userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
            userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
            userInfo.setCallBackUrl(callbackUrl);
            userInfo.setUsername(username);
            userInfo.setTransferMode(merchantPO.getTransferMode());
            //设置虚拟游戏开关
            userInfo.setOpenVrSport(merchantPO.getOpenVrSport());
            //设置过滤赛种信息
            userInfo.setFilterSport(merchantPO.getFilterSport());
            userInfo.setFilterLeague(merchantPO.getFilterLeague());
            if (!merchantCode.equals(filterMerchantCode)) {
                if (StringUtils.isEmpty(userInfo.getFilterLeague())) {
                    userInfo.setFilterLeague(filterLeagueIds);
                } else {
                    userInfo.setFilterLeague(userInfo.getFilterLeague() + "," + filterLeagueIds);
                }
            }
            if (!StringUtil.isBlankOrNull(language) && "zh,en,ml,yd,tw,vi,th,ad,ms".contains(language)) {
                userInfo.setLanguageName(language);
            }
            userInfo.setOpenEsport(merchantPO.getOpenEsport() == null ? 1 : merchantPO.getOpenEsport());
            userInfo.setOpenVideo(merchantPO.getOpenVideo() == null ? 1 : merchantPO.getOpenVideo());
            userInfo.setUserlevelList(userLevelList);
            userInfo.setVideoDomain(merchantPO.getVideoDomain());
            userInfo.setJumpsupport(jumpsupport);
            userInfo.setJumpfrom(jumpfrom);
            userInfo.setTerminal(terminal);
            Integer specialBetType = userPO.getSpecialBettingLimitType();
            userInfo.setSpecialBettingLimitType(specialBetType);
            if (StringUtils.isNotBlank(agentId)) {
                userInfo.setAgentId(agentId);
            }
            deleteHistoryLogin(terminal, userId);
            String token = TokenProccessor.makeToken(userId);
            int duration = EXPIRE_TIME_THIRTEEN_HOUR;
            if (StringUtils.isNotEmpty(merchantSession) && merchantSession.contains(merchantCode)) {
                duration = EXPIRE_TIME_ONE_HOUR;
            }
            userInfo.setDuration(duration);

            userInfo.setMinSeriesNum(merchantPO.getMinSeriesNum());
            userInfo.setMaxSeriesNum(merchantPO.getMaxSeriesNum());
            userInfo.setDomainGroupCode(domainGroupCode);
            userInfo.setBookBet(merchantPO.getBookBet());

            if (imitate == null || !imitate) {
                logHistory(userId, username, jumpfrom, terminal, merchantCode);
            }
            userApiVo = new UserApiVo();
            userApiVo.setToken(token);
            userApiVo.setUserId(userId + "");
            userApiVo.setIsTest(Objects.isNull(userInfo.getIsTest()) ? 1 : userInfo.getIsTest());
            String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
            if (StringUtils.isAllEmpty(frontPcUrl, frontH5Url, pcDomain, h5Domain)) {
                log.error("panda主页域名未配置!");
                return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
            }
            final String domain = getDomain(terminal, merchantPO);
            userApiVo.setDomain(domain);

            String appDomain = merchantPO.getAppDomain();
            if (StringUtils.isNotEmpty(appDomain)) {
                if (appDomain.contains(",")) {
                    String[] domainArray = appDomain.split(",");
                    appDomain = domainArray[0];
                }
                userApiVo.setApiDomain(appDomain);
            }
            userApiVo.setImgDomain(imageDomain);
            takePartInActivityOrNot(userPO, userApiVo, merchantPO, terminal, domainGroupCode, userInfo.getLanguageName());
            log.info(userPO.getUserId() + "," + username + userApiVo);

            //新增客户端域名数组
            List<String> domainStr = Lists.newArrayList();
            domainStr.add(userApiVo.getLoginUrl());
            userApiVo.setLoginUrlArr(domainStr);

            // 试一下能不能走域名池，如果不能还是用旧的
            if (!assemblyDomainInfo(userApiVo, merchantPO, terminal, ip)) {
                userApiVo.setDomain(domain);
                if (StringUtils.isNotEmpty(appDomain)) {
                    if (appDomain.contains(",")) {
                        String[] domainArray = appDomain.split(",");
                        appDomain = domainArray[0];
                    }
                    userApiVo.setApiDomain(appDomain);
                }
                userApiVo.setImgDomain(imageDomain);
                userApiVo.setLoginUrlArr(domainStr);
            }

            userInfo.setLoginUrl(userApiVo.getLoginUrl());
            //用户对象中新增图片域名和登录域名数组
            userInfo.setImgDomain(imageDomain);
            userInfo.setApiDomain(userApiVo.getApiDomain());
            userInfo.setLoginUrlArr(userApiVo.getLoginUrlArr());
            userInfo.setLiveH5Url(liveH5Url);
            userInfo.setLivePcUrl(livePcUrl);
            userInfo.setChatroomUrl(chatroomUrl);
            userInfo.setChatroomHttpUrl(chatroomHttpUrl);

            redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId + Colon + terminal, token, duration);
            redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, duration);

            if (StringUtils.isNotEmpty(stoken)) {
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_S_TOKEN_PREFIX + userId, stoken, EXPIRE_TIME_TWO_WEEK);

            }
            redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + Colon + terminal + language, userApiVo, duration);

            log.info(merchantCode + username + "登录结果:" + USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + terminal + token + "," + userApiVo);
            return APIResponse.returnSuccess(new UserApiResultVo(userApiVo));
        } catch (Exception e) {
            log.error(merchantCode + "UserServiceImpl.login,exception:" + username, e);
            throw new Exception("UserServiceImpl.login登录异常!");
        }
    }

    private Boolean assemblyDomainInfo(UserApiVo userApiVo, MerchantPO merchantPO, String terminal, String ipaddress) {
        try {
            final String merchantCode = merchantPO.getMerchantCode();
            final Long merchantGroupId = merchantPO.getMerchantGroupId();
            final String userId = userApiVo.getUserId();
            final String domainGroupCode = StringUtils.isNotEmpty(merchantPO.getDomainGroupCode()) ? merchantPO.getDomainGroupCode() : "common";
            if (domainPoolSwitch.equals("off")) {
                // 这里可以单独商户处理
                if (StringUtils.isBlank(domainPoolMerchantCodeStr) || !domainPoolMerchantCodeStr.contains(merchantCode)) {
                    return false;
                }
            }
            log.info("assemblyDomainInfo--merchantCode:{},userId:{}, merchantGroupId:{}, domainGroupCode:{}.", merchantCode, userId, merchantGroupId, domainGroupCode);
            //判断是否VIP用户
            boolean isVipUser = userApiVo.getIsTest() != null && userApiVo.getIsTest() == 2;
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},是否vip：【{}】", merchantCode, userId, isVipUser);
            long startTime = System.currentTimeMillis();
            String ipArea = ipUtil.findCity(ipaddress, 2);
            long endTime = System.currentTimeMillis();
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},解析ip耗时：【{}】", merchantCode, userId, endTime - startTime);
            if (!isVipUser) {
                String key = merchantCode + ipArea + terminal;
                String domainDetails = merchantAreaDomainsCacheMap.getIfPresent(key);
                if (StringUtils.isNotBlank(domainDetails)) {
                    log.info("assemblyDomainInfo--merchantCode:{},userId:{},获取商户区域{}缓存，缓存详情：{}", merchantCode, userId, ipArea, domainDetails);
                    return buildUserApiInfo(userApiVo, domainDetails);
                }
            }
            //获取域名： 本地缓存->redis->mysql
            List<DomainGroupApiVO> domainPoolList = getDomainGroupVO(merchantCode, merchantGroupId, domainGroupCode, userId);
            if (CollectionUtil.isEmpty(domainPoolList)) return false;

            return buildDomainListByParam(merchantPO, userApiVo, isVipUser, domainPoolList, terminal, ipArea, domainGroupCode);
        } catch (Exception e) {
            log.error(userApiVo.getUserId() + "assemblyDomainInfo,exception!", e);
            return false;
        }
    }

    private Boolean buildUserApiInfo(UserApiVo userApiVo, String domainDetails) {
        UserApiVo userApiVoCatch = JSON.parseObject(domainDetails, UserApiVo.class);
        userApiVo.setApiDomain(userApiVoCatch.getApiDomain());
        userApiVo.setDomain(userApiVoCatch.getDomain());
        userApiVo.setImgDomain(userApiVoCatch.getImgDomain());
        userApiVo.setUrl(userApiVoCatch.getUrl());
        userApiVo.setLoginUrl(userApiVoCatch.getLoginUrl());
        userApiVo.setLoginUrlArr(userApiVoCatch.getLoginUrlArr());
        return true;
    }

    /**
     * 根据参数获取用户应该拿到的域名并且拼接
     */
    private Boolean buildDomainListByParam(MerchantPO merchantPO, UserApiVo userApiVo, boolean isVipUser,
                                           List<DomainGroupApiVO> domainPoolList, String terminal, String ipArea, String groupCode) {
        final String merchantCode = merchantPO.getMerchantCode();
        final String userId = userApiVo.getUserId();
        List<DomainGroupApiVO> currentDomainPoolList = Lists.newArrayList();
        List<DomainApiVO> allDomains;
        List<DomainApiVO> h5Domains;
        List<DomainApiVO> pcDomains;
        List<DomainApiVO> appDomains;
        List<DomainApiVO> imgDomains;
        List<List<DomainApiVO>> listParam;
        if (isVipUser) {
            // vip
            currentDomainPoolList = domainPoolList.stream()
                    .filter(d -> GroupTypeEnum.getStrByType(d.getGroupType()).equals(groupCode))
                    .filter(d -> d.getExclusiveType().equals(2))
                    .collect(Collectors.toList());
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},获取域名--vip，缓存详情：{}", merchantCode, userId, currentDomainPoolList);
        }
        listParam = currentDomainPoolList.stream().map(DomainGroupApiVO::getDomainls).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(currentDomainPoolList) || domainListIsEmpty(listParam)) {
            // 区域
            currentDomainPoolList = domainPoolList.stream()
                    .filter(d -> GroupTypeEnum.getStrByType(d.getGroupType()).equals(groupCode))
                    .filter(d -> d.getExclusiveType().equals(1))
                    .filter(d -> ipArea.contains(d.getAreaName()))
                    .collect(Collectors.toList());
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},获取域名--区域-{}，缓存详情：{}", merchantCode, userId, ipArea, currentDomainPoolList);
        }
        listParam = currentDomainPoolList.stream().map(DomainGroupApiVO::getDomainls).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(currentDomainPoolList) || domainListIsEmpty(listParam)) {
            // 默认
            currentDomainPoolList = domainPoolList.stream()
                    .filter(d -> GroupTypeEnum.getStrByType(d.getGroupType()).equals(groupCode))
                    .filter(d -> d.getExclusiveType().equals(1))
                    .filter(d -> d.getBelongArea() == null || d.getBelongArea().equals(0L))
                    .collect(Collectors.toList());
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},获取域名--区域-默认，缓存详情：{}", merchantCode, userId, currentDomainPoolList);
        }
        listParam = currentDomainPoolList.stream().map(DomainGroupApiVO::getDomainls).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(currentDomainPoolList) || domainListIsEmpty(listParam)) {
            return false;
        }

        allDomains = listParam.stream().flatMap(List::stream).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(allDomains)) {
            return false;
        }

        h5Domains = allDomains.stream().filter(d -> d.getDomainType().equals(DomainTypeEnum.H5.getCode())).collect(Collectors.toList());
        pcDomains = allDomains.stream().filter(d -> d.getDomainType().equals(DomainTypeEnum.PC.getCode())).collect(Collectors.toList());
        appDomains = allDomains.stream().filter(d -> d.getDomainType().equals(DomainTypeEnum.APP.getCode())).collect(Collectors.toList());
        imgDomains = allDomains.stream().filter(d -> d.getDomainType().equals(DomainTypeEnum.IMAGE.getCode())).collect(Collectors.toList());

        final String apiDomain = CollectionUtil.isNotEmpty(appDomains) ? appDomains.get(0).getDomainName() : merchantPO.getAppDomain();
        final String imgDomain = CollectionUtil.isNotEmpty(imgDomains) ? imgDomains.get(0).getDomainName() : imageDomain;
        final String h5Domain = CollectionUtil.isNotEmpty(h5Domains) ? h5Domains.get(0).getDomainName() : merchantPO.getH5Domain();
        final String pcDomain = CollectionUtil.isNotEmpty(pcDomains) ? pcDomains.get(0).getDomainName() : merchantPO.getPcDomain();

        userApiVo.setApiDomain(apiDomain);
        userApiVo.setImgDomain(imgDomain);

        final String oldDomain = userApiVo.getDomain();
        String newDomain = getDomain(terminal, pcDomain, h5Domain);

        userApiVo.setDomain(newDomain);

        // 准备参数 进行domain前缀和api加密串的替换
        String newLoginUrl;
        List<String> loginUrlArr = Lists.newArrayList();
        final String loginUrlTemp = userApiVo.getLoginUrl();
        final String oldDomainTemp = userApiVo.getLoginUrl().split("\\?")[0];
        String[] loginUrlTempArr = loginUrlTemp.split("&");
        final String oldApiStrTemp = loginUrlTempArr[loginUrlTempArr.length - 1];
        final List<String> appDomainNames = appDomains.stream().map(DomainApiVO::getDomainName).collect(Collectors.toList());

        String newApiArr = AesUtil.encrypt(appDomainNames);
        log.info("{},cache.encrypt2={}", appDomainNames, newApiArr);

        // 封装loginUrl
        newLoginUrl = loginUrlTemp.replace(oldDomainTemp, newDomain);
        newLoginUrl = newLoginUrl.replace(oldApiStrTemp, "api=" + newApiArr);
        userApiVo.setLoginUrl(newLoginUrl);

        // 封装loginUrlArr
        List<String> newDomainList = getDomainList(terminal, pcDomains, h5Domains);
        if (CollectionUtils.isNotEmpty(newDomainList)) {
            for (String newDomainDetail : newDomainList) {
                newLoginUrl = loginUrlTemp.replace(oldDomainTemp, newDomainDetail);
                String[] addr = newLoginUrl.split("&");
                String oldApiStr = addr[addr.length - 1];
                if (oldApiStr.contains("api")) {
                    newLoginUrl = newLoginUrl.replace(oldApiStrTemp, "api=" + newApiArr);
                    loginUrlArr.add(newLoginUrl);
                }
            }
            userApiVo.setLoginUrlArr(loginUrlArr);
        }

        if (!isVipUser) {
            String key = merchantCode + ipArea + terminal;
            merchantAreaDomainsCacheMap.put(key, JSON.toJSONString(userApiVo));
        }
        return true;
    }

    /**
     * 获取域名详细信息
     * 1.本地缓存
     * 2.redis缓存
     * 3.三端服务查库
     */
    private List<DomainGroupApiVO> getDomainGroupVO(String merchantCode, Long merchantGroupId, String domainGroupCode, String userId) throws JsonProcessingException {
        final String key = USERCENTER_FAMILY_KEY + MULTITERMINAL_DOMAIN_KEY + merchantCode;
        List<DomainGroupApiVO> domainGroupApiVO;
        domainGroupApiVO = domainPoolCacheMap.getIfPresent(key);

        if (CollectionUtils.isNotEmpty(domainGroupApiVO)) {
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},getDomainList--cache", merchantCode, userId);
            return domainGroupApiVO;
        }
        ObjectMapper mapper = new ObjectMapper();

        String domainPoolStr = (String) redisTemp.getObject(key, String.class);

        if (StringUtils.isNotBlank(domainPoolStr)) {
            domainGroupApiVO = mapper.readValue(domainPoolStr, new TypeReference<List<DomainGroupApiVO>>() {
            });
            if (CollectionUtils.isNotEmpty(domainGroupApiVO)) {
                log.info("assemblyDomainInfo--merchantCode:{},userId:{},getDomainList--redis", merchantCode, userId);
                domainPoolCacheMap.put(key, domainGroupApiVO);
                return domainGroupApiVO;
            }
        }

        List<?> convertList = multiterminalClient.getDomainByMerchantAndArea(merchantGroupId, domainGroupCode);
        domainGroupApiVO = mapper.convertValue(convertList, new TypeReference<List<DomainGroupApiVO>>() {
        });
        if (CollectionUtil.isNotEmpty(domainGroupApiVO)) {
            log.info("assemblyDomainInfo--merchantCode:{},userId:{},getDomainList--mysql", merchantCode, userId);
            domainPoolCacheMap.put(key, domainGroupApiVO);
            redisTemp.setObject(key, domainGroupApiVO, EXPIRE_TIME_ONE_HOUR);
            return domainGroupApiVO;
        }
        return null;
    }

    /**
     * 根据用户详情，获取相关的域名并返回
     */
    private Boolean assemblyGroupToDomainInfo(MerchantPO merchantPO, UserApiVo userApiVo, DomainGroupApiVO domainGroup, String terminal) {
        log.info("assemblyDomainInfo->assemblyGroupToDomainInfo, userApiVo:{}, domainGroup:{}, terminal:{}.", userApiVo, domainGroup, terminal);
        List<DomainApiVO> domainls = domainGroup.getDomainls();
        if (CollectionUtil.isEmpty(domainls)) return false;
        Map<Integer, List<DomainApiVO>> domainmap = domainls.stream().collect(Collectors.groupingBy(DomainApiVO::getDomainType));
        //域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名
        List<DomainApiVO> h5domainlsAll = domainmap.get(1);
        List<DomainApiVO> pcdomainlsAll = domainmap.get(2);
        List<DomainApiVO> appdomainlsAll = domainmap.get(3);
        List<DomainApiVO> imgdomainlsAll = domainmap.get(4);
        List<DomainApiVO> h5FilterList = null, pcdomainlsFilter = null, appdomainlsFilter = null, imgdomainlsFilter = null;
        if (CollectionUtils.isNotEmpty(h5domainlsAll)) {
            h5FilterList = h5domainlsAll.stream().filter(e -> e.getEnable().equals(1)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(h5FilterList)) {
                h5FilterList = h5domainlsAll.stream().filter(e -> e.getEnable().equals(2)).collect(Collectors.toList());
            }
        }

        if (CollectionUtils.isNotEmpty(pcdomainlsAll)) {
            pcdomainlsFilter = pcdomainlsAll.stream().filter(e -> e.getEnable().equals(1)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(h5FilterList)) {
                pcdomainlsFilter = pcdomainlsAll.stream().filter(e -> e.getEnable().equals(2)).collect(Collectors.toList());
            }
        }

        if (CollectionUtils.isNotEmpty(appdomainlsAll)) {
            appdomainlsFilter = appdomainlsAll.stream().filter(e -> e.getEnable().equals(1)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(h5FilterList)) {
                appdomainlsFilter = appdomainlsAll.stream().filter(e -> e.getEnable().equals(2)).collect(Collectors.toList());
            }
        }

        if (CollectionUtils.isNotEmpty(imgdomainlsAll)) {
            imgdomainlsFilter = imgdomainlsAll.stream().filter(e -> e.getEnable().equals(1)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(h5FilterList)) {
                imgdomainlsFilter = imgdomainlsAll.stream().filter(e -> e.getEnable().equals(2)).collect(Collectors.toList());
            }
        }
        userApiVo.setApiDomain(CollectionUtil.isNotEmpty(appdomainlsFilter) ? appdomainlsFilter.get(0).getDomainName() : merchantPO.getAppDomain());
        userApiVo.setImgDomain(CollectionUtil.isNotEmpty(imgdomainlsFilter) ? imgdomainlsFilter.get(0).getDomainName() : imageDomain);
        String oldDomain = userApiVo.getDomain();
        String newDomain = getDomain(terminal, (CollectionUtil.isNotEmpty(pcdomainlsFilter) ? pcdomainlsFilter.get(0).getDomainName() : merchantPO.getPcDomain()),
                (CollectionUtil.isNotEmpty(h5FilterList) ? h5FilterList.get(0).getDomainName() : merchantPO.getH5Domain()));
        userApiVo.setDomain(newDomain);
        if (StringUtils.isNotEmpty(oldDomain) && !oldDomain.equalsIgnoreCase(newDomain)) {
            String loginUrl = userApiVo.getLoginUrl();
            if (StringUtils.isNotEmpty(loginUrl)) {
                loginUrl = loginUrl.replace(oldDomain, newDomain);
                userApiVo.setLoginUrl(loginUrl);
                // 修复loginUrl中api参数重新解密
                String[] addr = loginUrl.split("&");
                String apiStr = addr[addr.length - 1];
                if (apiStr.contains("api")) {
                    //新增客户端域名数组
                    String apiDomain = userApiVo.getApiDomain();
                    if (StringUtils.isNotEmpty(apiDomain)) {
                        String api = AesUtil.encrypt(apiDomain);
                        log.info(apiDomain + ",cache.encrypt2=" + api);
                        String newLoginUrl = loginUrl.replace(apiStr, "api=" + api);
                        userApiVo.setLoginUrl(newLoginUrl);
                    }
                    List<String> domainStr = Lists.newArrayList();
                    domainStr.add(userApiVo.getLoginUrl());
                    userApiVo.setLoginUrlArr(domainStr);
                }
            }
        }
        return true;
    }

    private Integer getExtraMargin(List<UserLevelRelationVO> userLevelList) {
        if (CollectionUtils.isNotEmpty(userLevelList)) {
            return userLevelList.get(0).getDynamicMargin();
        } else {
            return null;
        }
    }

    private void deleteHistoryLogin(String terminal, Long userId) {
        String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId + Colon + terminal);
        if (StringUtils.isNotEmpty(token)) {
            log.info(userId + ",删除用户会话:" + token);
            redisTemp.delete(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token);
            redisTemp.delete(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId + Colon + terminal);
        }
    }

    public void logHistory(Long uid, String userName, String jumpfrom, String terminal, String merchantCode) {
        ExecutorInstance.executorService.submit(() -> {
            TLogHistory history = new TLogHistory();
            history.setUid(uid);
            history.setUsername(userName);
            history.setLoginTime(System.currentTimeMillis());
            history.setMerchantCode(merchantCode);
            if (StringUtils.isNotEmpty(jumpfrom)) {
                history.setLogDetail(jumpfrom);
                history.setLogType(2);
            } else {
                history.setLogDetail("登录");
                history.setLogType(1);
            }
            try {
                if (loginHistorySwitch.equalsIgnoreCase("off")) {
                    MQMsgBody body = new MQMsgBody();
                    body.setKey(uid + "");
                    body.setTag("login");
                    body.setTopic("panda_user_info");
                    body.setObj(history);
                    mqProducer.sendMessage(body);
                }
            } catch (Exception e) {
                log.error("登陆日志落库存入消息队列失败.", e);
            }
            // this.tLogHistoryMapper.insert(history);
        });
    }

    /**
     * 查询玩家在线
     * a 验签
     * b 缓存获取玩家
     *
     * @Param: [request, username, merchantCode, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<java.lang.Object>
     * @date: 2020/8/23 11:39
     */
    @Override
    public APIResponse<Object> checkUserOnline(HttpServletRequest request, String username, String merchantCode, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + username + "&" + timestamp;
            APIResponse<Object> errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                return errorResult;
            }
            //UserPO userPO = userMapper.getUserByUserName(merchantCode, username);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
            if (userPO == null) {
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            log.info(userPO.getUserId() + ": " + USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userPO.getUserId());
            long uid = userPO.getUserId();
            //Set<String> obj = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userPO.getUserId() + ":*");
            String userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":Mobile");
            if (StringUtils.isEmpty(userToken)) {
                userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":MOBILE");
                if (StringUtils.isEmpty(userToken)) {
                    userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":mobile");
                    if (StringUtils.isEmpty(userToken)) {
                        userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":PC");
                        if (StringUtils.isEmpty(userToken)) {
                            userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":pc");

                            if (StringUtils.isEmpty(userToken)) {
                                userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":H5");

                                if (StringUtils.isEmpty(userToken)) {
                                    userToken = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":h5");
                                }
                            }
                        }
                    }
                }
            }
            Map<String, Object> map = Maps.newHashMap();
            map.put("isOnline", StringUtils.isNotEmpty(userToken));
            map.put("userName", username);
            map.put("merchantCode", merchantCode);
            return APIResponse.returnSuccess(map);
        } catch (Exception e) {
            log.error(merchantCode + "UserController.checkUserOnline,exception:" + username, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 踢出玩家
     * a 验签
     * b 缓存踢出
     * String token = redisTemp.get(RedisConstants.USERCENTER_FAMILY_KEY + RedisConstants.USER_ID_KEY_PREFIX + userPO.getUserId());
     * if (StringUtils.isNotEmpty(token)) {
     * redisTemp.delete(RedisConstants.USERCENTER_FAMILY_KEY + RedisConstants.USER_TOKEN_PREFIX + token);
     * redisTemp.delete(RedisConstants.USERCENTER_FAMILY_KEY + RedisConstants.USER_ID_KEY_PREFIX + userPO.getUserId());
     * }
     *
     * @Param: [request, username, merchantCode, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<java.lang.Object>
     * @date: 2020/8/23 11:39
     */
    @Override
    public APIResponse<Object> kickOutUser(HttpServletRequest request, String userName, String merchantCode, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + userName + "&" + timestamp;
            APIResponse<Object> errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                return errorResult;
            }
            //UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
            if (userPO == null) {
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            Long uid = userPO.getUserId();
/*
            Set<String> userIdKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":*");
            Set<String> userNameKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + merchantCode + userName + ":*");
*/

            Set<String> userIdKey = new HashSet<>();
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":Mobile");
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":MOBILE");
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":mobile");
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":PC");
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":pc");
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":H5");
            userIdKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":h5");
            //  Set<String> userNameKey2 = scheduleRedisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + merchantCode + username + ":*");
            Set<String> userNameKey = new HashSet<>();
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":Mobile");
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":MOBILE");
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":mobile");
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":PC");
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":pc");
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":H5");
            userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userName + ":h5");

            if (CollectionUtils.isNotEmpty(userIdKey)) {
                log.info("kickOutUser:" + userIdKey + userNameKey);
                for (String user : userIdKey) {
                    String token = redisTemp.get(user);
                    redisTemp.delete(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token);
                }
                redisTemp.deleteKeys(userIdKey);
                redisTemp.deleteKeys(userNameKey);
            }
/*
            Set<String> userIdKey2 = scheduleRedisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":*");
            Set<String> userNameKey2 = scheduleRedisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + merchantCode + userName + ":*");*/

            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error(merchantCode + "UserController.kickOutUser,exception:" + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    @Override
    public APIResponse kickoutDisableUser(String merchantCode, List<UserVipVO> list) {
        try {
            Thread.sleep(1000);
            //查询本地缓存
            if (StringUtils.isNotEmpty(merchantCode)) {
                List<UserVipVO> userList = userOrderUpdateMapper.findDisableUserInfo(merchantCode, list);
                if (CollectionUtils.isEmpty(userList)) {
                    return APIResponse.returnFail("没有查到用户为禁用!");
                }
                for (UserVipVO vo : userList) {
                    String userName = vo.getUserName();
                    Long uid = vo.getUid();
                    cleaningRedisUserCache(uid, userName, merchantCode);
                }
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error(merchantCode + "踢出商户异常!", e);
            return APIResponse.returnFail("提出商户失败!");
        }
    }

    @Override
    public APIResponse kickoutUserInternal(Long uid) {
        try {
            Thread.sleep(1000);
            UserPO userPO = userMapper.getUserInfo(uid);
            if (userPO == null) {
                log.error("kickoutUserInternal.uid=" + uid);
                return APIResponse.returnFail("修改失败!");
            }
            cleaningRedisUserCache(uid, userPO.getUsername(), userPO.getMerchantCode());
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("UserController.kickoutUserInternal,exception:", e);
            return APIResponse.returnFail("修改失败!");
        }
    }

    @Override
    public void updateUserCache(Long uid, String mCode) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(uid + "UserController.updateUserCache,exception:", e);
        }
        updateUserCacheInfo(uid, mCode);
    }

    @Override
    public void windControlUpdateUserCache(Long uid,String merchantCod,WindUserPO userPO) {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            log.error(uid + "UserController.windControlUpdateUserCache,exception:", e);
        }
        windControlUpdateUserCacheInfo(uid, merchantCod,userPO);
    }

    @Override
    public void updateUserAndDomainCache(Long uid, String mCode, Integer isVipDomain) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(uid + "UserController.updateUserCache,exception:", e);
        }
        // 删除缓存的userId，登录重新走域名池
        updateUserAndDomainCacheInfo(uid, mCode, isVipDomain);
    }

    @Override
    public boolean batchUpdateUserCache(List<Long> uidList, String merchantCode) {
        for (Long userId : uidList) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(userId + "UserController.updateUserCache,exception:", e);
            }
            updateUserCacheInfo(userId, merchantCode);
        }
        return true;
    }

    @Override
    public boolean batchUpdateUserAndDomainCache(List<Long> uidList, String merchantCode, Integer isVipDomain) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
                for (Long userId : uidList) {
                    updateUserAndDomainCacheInfo(userId, merchantCode, isVipDomain);
                }
            } catch (InterruptedException e) {
                log.error("UserController.batchUpdateUserAndDomainCache,exception:", e);
            }
        });
        return true;
    }


    private void updateUserCacheInfo(Long uid, String mCode) {
        try {
            UserPO userPO = userMapper.getUserInfo(uid);
            if (userPO == null) {
                log.error("kickoutUserInternal.updateUserCacheInfo.uid=" + uid);
                return;
            }
            String merchantCode = userPO.getMerchantCode();
            if (!merchantCode.equalsIgnoreCase(mCode)) {
                log.error("updateUserCache,失败!参数：{},{}", mCode, uid);
                return;
            }
            Integer userLevel = userPO.getUserLevel();
            String userMarketPrefer = userPO.getUserMarketPrefer();
            Integer userBetPrefer = userPO.getUserBetPrefer();
            String languageName = userPO.getLanguageName();
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            Integer isTest = Objects.isNull(userPO.getIsTest()) ? 1 : userPO.getIsTest();
            List<UserLevelRelationVO> userLevelList = userMapper.getUserLevelList(userPO.getUserId());
            log.info(uid + ",updateUserCache:" + userLevelList + "," + userLevel);
            String mobileLower = "mobile";
            String mobileUpper = "MOBILE";
            String pcUpper = "PC";
            String pcLower = "pc";
            String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower);
            log.info(uid + ",updateUserCache.mobile.token=" + token);
            if (StringUtils.isNotEmpty(token)) {
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",updateUserCache.mobile.userInfo=" + userInfo + mobileLower);
                userInfo.setUserlevelList(userLevelList);
                userInfo.setUserLevel(userLevel);
                userInfo.setUserMarketPrefer(userMarketPrefer);
                userInfo.setUserBetPrefer(userBetPrefer);
                userInfo.setLanguageName(languageName);
                userInfo.setUserMarketLevel(userPO.getMarketLevel());
                userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                userInfo.setIsTest(isTest);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower, token, userInfo.getDuration());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
            }
            token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower);
            log.info(uid + ",updateUserCache.pc.token=" + token + pcLower);
            if (StringUtils.isNotEmpty(token)) {
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",updateUserCache.pc.userInfo=" + userInfo);
                userInfo.setUserlevelList(userLevelList);
                userInfo.setUserLevel(userLevel);
                userInfo.setUserMarketPrefer(userMarketPrefer);
                userInfo.setUserBetPrefer(userBetPrefer);
                userInfo.setLanguageName(languageName);
                userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                userInfo.setUserMarketLevel(userPO.getMarketLevel());
                userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                userInfo.setIsTest(isTest);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower, token, userInfo.getDuration());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
            }
            token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper);
            log.info(uid + ",updateUserCache.pc.token=" + token + pcUpper);
            if (StringUtils.isNotEmpty(token)) {
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",updateUserCache.pc.userInfo=" + userInfo + pcUpper);
                userInfo.setUserlevelList(userLevelList);
                userInfo.setUserLevel(userLevel);
                userInfo.setUserMarketPrefer(userMarketPrefer);
                userInfo.setUserBetPrefer(userBetPrefer);
                userInfo.setLanguageName(languageName);
                userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                userInfo.setUserMarketLevel(userPO.getMarketLevel());
                userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                userInfo.setIsTest(isTest);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper, token, userInfo.getDuration());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
            }
            redisTemp.delete(USERCENTER_FAMILY_KEY + CACHE + uid);
            redisTemp.delete(merchantCode + userPO.getUsername());
        } catch (Exception e) {
            log.error("UserController.kickoutUserInternal,exception:" + uid, e);
        }
    }

    private void windControlUpdateUserCacheInfo(Long uid,String merchantCode, WindUserPO userPO) {
        try {
            String mobileLower = "mobile";
            String pcUpper = "PC";
            String pcLower = "pc";
            String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower);

            log.info(uid + ",windControlUpdateUserCacheInfo.mobile.token=" + token);
            Integer userLevel = userPO.getUserLevel();
            Integer financeTag = userPO.getFinanceTag();
            List<UserLevelRelationVO> userLevelList = new ArrayList<>();
            if (StringUtils.isNotEmpty(token)) {
               Long expire = redisTemp.getExpire(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower,TimeUnit.SECONDS);
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",windControlUpdateUserCacheInfo.mobile.userInfo=" + userInfo + mobileLower);
                /**
                 * 处理userLevelList数据
                 */
                userLevelList = getUserLevelList(userPO,userInfo);
                getNewUserInfo(userPO, userLevel, financeTag, userLevelList, userInfo);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower, token, expire.intValue());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, expire.intValue());
            }
            token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower);
            log.info(uid + ",windControlUpdateUserCacheInfo.pc.token=" + token + pcLower);
            if (StringUtils.isNotEmpty(token)) {
                Long expire = redisTemp.getExpire(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower,TimeUnit.SECONDS);
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                userLevelList = getUserLevelList(userPO,userInfo);
                log.info(uid + ",windControlUpdateUserCacheInfo.pc.userInfo=" + userInfo);
                getNewUserInfo(userPO, userLevel, financeTag, userLevelList, userInfo);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower, token, expire.intValue());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, expire.intValue());
            }
            token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper);
            log.info(uid + ",windControlUpdateUserCacheInfo.pc.token=" + token + pcUpper);
            if (StringUtils.isNotEmpty(token)) {
                Long expire = redisTemp.getExpire(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper,TimeUnit.SECONDS);
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                userLevelList = getUserLevelList(userPO,userInfo);
                log.info(uid + ",windControlUpdateUserCacheInfo.pc.userInfo=" + userInfo + pcUpper);
                getNewUserInfo(userPO, userLevel, financeTag, userLevelList, userInfo);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper, token, expire.intValue());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, expire.intValue());
            }
            redisTemp.delete(USERCENTER_FAMILY_KEY + CACHE + uid);
            redisTemp.delete(merchantCode + userPO.getUsername());
        } catch (Exception e) {
            log.error("UserController.windControlUpdateUserCacheInfo,exception:" + uid, e);
        }
    }

    private void getNewUserInfo(WindUserPO userPO, Integer userLevel, Integer financeTag, List<UserLevelRelationVO> userLevelList, UserInfo userInfo) {
        if(CollectionUtils.isNotEmpty(userLevelList)) userInfo.setUserlevelList(userLevelList);
        if(ObjectUtils.isNotEmpty(userLevel)) userInfo.setUserLevel(userLevel);
        if(ObjectUtils.isNotEmpty(financeTag)) userInfo.setFinanceTag(financeTag);
        if(ObjectUtils.isNotEmpty(userPO.getMarketLevel())) userInfo.setUserMarketLevel(userPO.getMarketLevel());
        if(CollectionUtils.isNotEmpty(userLevelList)) userInfo.setDynamicMargin(getExtraMargin(userLevelList));
    }

    private List<UserLevelRelationVO> getUserLevelList(WindUserPO userPO,UserInfo userInfo) {
        if(CollectionUtils.isEmpty(userPO.getUserlevelList())){
            return null;
        }
        UserLevelRelationVO vo = new UserLevelRelationVO();
        if(CollectionUtils.isNotEmpty(userInfo.getUserlevelList())) {
             vo = userInfo.getUserlevelList().get(0);
        }
        UserLevelRelationPO po = userPO.getUserlevelList().get(0);
        BeanUtils.copyProperties(po, vo,BeanUtil.getNullPropertyNames(po));
        List<UserLevelRelationVO> vos= Arrays.asList(vo);
        return vos;
    }

    private void updateUserAndDomainCacheInfo(Long uid, String mCode, Integer isVipDomain) {
        try {
            UserPO userPO = userMapper.getUserInfo(uid);
            if (userPO == null) {
                log.error("kickoutUserInternal.updateUserCacheInfo.uid=" + uid);
                return;
            }
            String merchantCode = userPO.getMerchantCode();
            if (!merchantCode.equalsIgnoreCase(mCode)) {
                log.error("updateUserCache,失败!参数：{},{}", mCode, uid);
                return;
            }
            Integer userLevel = userPO.getUserLevel();
            String userMarketPrefer = userPO.getUserMarketPrefer();
            Integer userBetPrefer = userPO.getUserBetPrefer();
            String languageName = userPO.getLanguageName();
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            Integer isTest = Objects.isNull(userPO.getIsTest()) ? 1 : userPO.getIsTest();
            List<UserLevelRelationVO> userLevelList = userMapper.getUserLevelList(userPO.getUserId());
            log.info(uid + ",updateUserCache:" + userLevelList + "," + userLevel);
            String mobileLower = "mobile";
            String mobileUpper = "MOBILE";
            String pcUpper = "PC";
            String pcLower = "pc";
            String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower);
            // 清除用户所有登录设备的缓存
            Set<String> userApiVoKeys = new HashSet<>();
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":Mobile" + userPO.getLanguageName());
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":MOBILE" + userPO.getLanguageName());
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":mobile" + userPO.getLanguageName());
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":PC" + userPO.getLanguageName());
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":pc" + userPO.getLanguageName());
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":H5" + userPO.getLanguageName());
            userApiVoKeys.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + userPO.getUsername() + ":h5" + userPO.getLanguageName());
            log.info(uid + ",updateUserAndDomainCache.all.keys" + userApiVoKeys);
            redisTemp.deleteKeys(userApiVoKeys);

            log.info(uid + ",updateUserAndDomainCache.mobile.token=" + token);
            if (StringUtils.isNotEmpty(token)) {
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",updateUserCache.mobile.userInfo=" + userInfo + mobileLower);
                userInfo.setUserlevelList(userLevelList);
                userInfo.setUserLevel(userLevel);
                userInfo.setUserMarketPrefer(userMarketPrefer);
                userInfo.setUserBetPrefer(userBetPrefer);
                userInfo.setLanguageName(languageName);
                userInfo.setUserMarketLevel(userPO.getMarketLevel());
                userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                userInfo.setIsTest(isVipDomain);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + mobileLower, token, userInfo.getDuration());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
            }
            token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower);
            log.info(uid + ",updateUserCache.pc.token=" + token + pcLower);
            if (StringUtils.isNotEmpty(token)) {
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",updateUserCache.pc.userInfo=" + userInfo);
                userInfo.setUserlevelList(userLevelList);
                userInfo.setUserLevel(userLevel);
                userInfo.setUserMarketPrefer(userMarketPrefer);
                userInfo.setUserBetPrefer(userBetPrefer);
                userInfo.setLanguageName(languageName);
                userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                userInfo.setUserMarketLevel(userPO.getMarketLevel());
                userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                userInfo.setIsTest(isVipDomain);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcLower, token, userInfo.getDuration());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
            }
            token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper);
            log.info(uid + ",updateUserCache.pc.token=" + token + pcUpper);
            if (StringUtils.isNotEmpty(token)) {
                UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                log.info(uid + ",updateUserCache.pc.userInfo=" + userInfo + pcUpper);
                userInfo.setUserlevelList(userLevelList);
                userInfo.setUserLevel(userLevel);
                userInfo.setUserMarketPrefer(userMarketPrefer);
                userInfo.setUserBetPrefer(userBetPrefer);
                userInfo.setLanguageName(languageName);
                userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                userInfo.setUserMarketLevel(userPO.getMarketLevel());
                userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                userInfo.setIsTest(isVipDomain);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + pcUpper, token, userInfo.getDuration());
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
            }
            redisTemp.delete(USERCENTER_FAMILY_KEY + CACHE + uid);
        } catch (Exception e) {
            log.error("UserController.kickoutUserInternal,exception:" + uid, e);
        }
    }

    private void cleaningRedisUserCache(Long uid, String username, String merchantCode) {
        Set<String> userKey = new HashSet<>();
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":Mobile");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":MOBILE");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":mobile");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":PC");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":pc");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":H5");
        userKey.add(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + ":h5");
        Set<String> userNameKey = new HashSet<>();
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":Mobile");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":MOBILE");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":mobile");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":PC");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":pc");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":H5");
        userNameKey.add(USERCENTER_FAMILY_KEY + USER_NAME_KEY_PREFIX + merchantCode + username + ":h5");
        if (CollectionUtils.isNotEmpty(userKey)) {
            log.info("kickoutUserInternal:" + userKey + userNameKey);
            for (String user : userKey) {
                String token = redisTemp.get(user);
                redisTemp.delete(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token);
            }
            redisTemp.deleteKeys(userKey);
            redisTemp.deleteKeys(userNameKey);
        }
        redisTemp.delete(merchantCode + username);
    }

    @Override
    public APIResponse kickoutMerchantUser(String merchantCode) {
        try {
            Thread.sleep(1000);
            if (StringUtils.isNotEmpty(merchantCode)) {
                List<Map<String, Object>> userList = userMapper.queryUserIdList(merchantCode);
                if (CollectionUtils.isEmpty(userList)) {
                    return APIResponse.returnFail("kickoutMerchantUser商户失败!");
                }
                for (Map<String, Object> tempUser : userList) {
                    String userName = (String) tempUser.get("userName");
                    Long uid = (Long) tempUser.get("uid");
                    cleaningRedisUserCache(uid, userName, merchantCode);
                }
            } else {
                Set<String> userKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + "*");
                redisTemp.deleteKeys(userKey);
                Set<String> tokenKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + "*");
                redisTemp.deleteKeys(tokenKey);
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error(merchantCode + "踢出商户异常!", e);
            return APIResponse.returnFail("提出商户失败!");
        }
    }

    @Override
    public APIResponse updateMerchantUserCache(String merchantCode) {
        try {
            Thread.sleep(1000);
            if (StringUtils.isNotEmpty(merchantCode)) {
                String key = RedisConstants.MERCHANT_FAMILY + RedisConstants.MERCHANT_CODE_PREFIX + merchantCode;
                redisTemp.delete(key);
                //预约投注开关-需要清除缓存，不是order服务拿不到新的值
                redisTemp.delete(RedisConstants.MERCHANT_FAMILY + RedisConstants.BUSSINESS + merchantCode);
                //用户中心key
                String userKey = RedisConstants.MERCHANT_FAMILY + RedisConstants.USER_BUSSINESS + merchantCode;
                redisTemp.delete(userKey);
                MerchantPO merchantPO = this.getMerchantPO(merchantCode);
                if (merchantPO == null) {
                    return APIResponse.returnFail("该商户不存在，商户编码:" + merchantCode + "!");
                }
                log.info(merchantCode + ",updateMerchantUserCache=" + merchantPO);
                //List<Map<String, Object>> userList = userMapper.queryUserIdList(merchantCode);
                List<Map<String, Object>> userList = merchantOrderClient.queryUserIdList(merchantCode);
                log.info(merchantCode + ",updateMerchantUserCache.queryUserIdList:" + (CollectionUtils.isNotEmpty(userList) ? userList.size() : 0));
                if (CollectionUtils.isEmpty(userList)) {
                    return APIResponse.returnFail("updateMerchantUserCache商户失败!");
                }
                MerchantVideoManageVo videoManageVo = videoManageMapper.queryMerchantVideoManage(merchantCode);
                MerchantChatRoomSwitchVO chatRoomSwitchVO = chatRoomSwitchMapper.queryMerchantChatRoomSwitch(merchantCode);

                Integer tempMinSeriesNum = merchantPO.getMinSeriesNum();
                Integer tempMaxSeriesNum = merchantPO.getMaxSeriesNum();

                //精彩回放开关
                MerchantEventSwitchVO eventSwitchVO = new MerchantEventSwitchVO();
                if (StringUtils.isNotEmpty(merchantPO.getMerchantEvent())) {
                    Map<String, Object> erchantEventMap = JsonUtils.jsonToMap(merchantPO.getMerchantEvent());
                    eventSwitchVO.setCornerEvent(Integer.valueOf(erchantEventMap.get("cornerEvent").toString()));
                    eventSwitchVO.setPenaltyEvent(Integer.valueOf(erchantEventMap.get("penaltyEvent").toString()));
                    eventSwitchVO.setEventSwitch(Integer.valueOf(erchantEventMap.get("eventSwitch").toString()));
                } else {
                    eventSwitchVO.setCornerEvent(0);
                    eventSwitchVO.setPenaltyEvent(0);
                    eventSwitchVO.setEventSwitch(0);
                }

                log.info("eventSwitchVO=" + eventSwitchVO);


                for (Map<String, Object> tempUser : userList) {
                    try {
                        String userName = (String) tempUser.get("userName");
                        Long uid = (Long) tempUser.get("uid");
                        String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + "mobile");
                        if (StringUtils.isNotEmpty(token)) {
                            UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                            //UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
                            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
                            List<UserLevelRelationVO> userLevelList = userMapper.getUserLevelList(userPO.getUserId());
                            userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                            userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                            userInfo.setOpenVideo(merchantPO.getOpenVideo() == null ? 1 : merchantPO.getOpenVideo());
                            userInfo.setVideoDomain(merchantPO.getVideoDomain());
                            userInfo.setFilterLeague(merchantPO.getFilterLeague());
                            userInfo.setFilterSport(merchantPO.getFilterSport());
                            userInfo.setMinSeriesNum(tempMinSeriesNum);
                            userInfo.setMaxSeriesNum(tempMaxSeriesNum);
                            userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                            userInfo.setUserMarketLevel(userPO.getMarketLevel());
                            userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                            userInfo.setBookBet(merchantPO.getBookBet());
                            userInfo.setBookMarketSwitch(merchantPO.getBookMarketSwitch());
                            userInfo.setBookMarketSwitchBasketball(merchantPO.getBookMarketSwitchBasketball());
//                            userInfo.setVideoManageVo(videoManageVo);
                            userInfo.setClosedWithoutOperation(videoManageVo.getClosedWithoutOperation());
                            userInfo.setVideoSettings(videoManageVo.getVideoSettings());
                            userInfo.setViewingTime(videoManageVo.getViewingTime());
                            userInfo.setCustomViewTime(videoManageVo.getCustomViewTime());
                            userInfo.setNoBackgroundPlay(videoManageVo.getNoBackgroundPlay());
//                            userInfo.setChatRoomSwitchVO(chatRoomSwitchVO);
                            userInfo.setMerchantEventSwitchVO(eventSwitchVO);
                            log.info(uid + ",userInfo=" + userInfo);
                            redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + "mobile", token, userInfo.getDuration());
                            redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
                        }
                        token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + "pc");
                        if (StringUtils.isNotEmpty(token)) {
                            UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, UserInfo.class);
                            //UserPO userPO = userMapper.getUserByUserName(merchantCode, userName);
                            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
                            List<UserLevelRelationVO> userLevelList = userMapper.getUserLevelList(userPO.getUserId());
                            userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
                            userInfo.setSettleSwitchBasket(getSettleSwitchBasket(merchantPO, userPO));
                            userInfo.setOpenVideo(merchantPO.getOpenVideo() == null ? 1 : merchantPO.getOpenVideo());
                            userInfo.setVideoDomain(merchantPO.getVideoDomain());
                            userInfo.setFilterLeague(merchantPO.getFilterLeague());
                            userInfo.setFilterSport(merchantPO.getFilterSport());
                            userInfo.setMinSeriesNum(tempMinSeriesNum);
                            userInfo.setMaxSeriesNum(tempMaxSeriesNum);
                            userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, userInfo.getTerminal(), userLevelList));
                            userInfo.setUserMarketLevel(userPO.getMarketLevel());
                            userInfo.setDynamicMargin(getExtraMargin(userLevelList));
                            userInfo.setBookBet(merchantPO.getBookBet());
                            userInfo.setBookMarketSwitch(merchantPO.getBookMarketSwitch());
                            userInfo.setBookMarketSwitchBasketball(merchantPO.getBookMarketSwitchBasketball());
//                            userInfo.setVideoManageVo(videoManageVo);
                            userInfo.setClosedWithoutOperation(videoManageVo.getClosedWithoutOperation());
                            userInfo.setVideoSettings(videoManageVo.getVideoSettings());
                            userInfo.setViewingTime(videoManageVo.getViewingTime());
                            userInfo.setCustomViewTime(videoManageVo.getCustomViewTime());
                            userInfo.setNoBackgroundPlay(videoManageVo.getNoBackgroundPlay());
//                            userInfo.setChatRoomSwitchVO(chatRoomSwitchVO);
                            userInfo.setMerchantEventSwitchVO(eventSwitchVO);
                            log.info(uid + ",userInfo=" + userInfo);
                            redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + uid + Colon + "pc", token, userInfo.getDuration());
                            redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, userInfo.getDuration());
                        }
                    } catch (Exception e) {
                        log.error(tempUser + ",updateMerchantUserCache", e);
                    }
                }
            } else {
                Set<String> userKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + "*");
                redisTemp.deleteKeys(userKey);
                Set<String> tokenKey = redisTemp.keys(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + "*");
                redisTemp.deleteKeys(tokenKey);
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error(merchantCode + "updateMerchantUserCache踢出商户异常!", e);
            return APIResponse.returnFail("提出商户失败!");
        }
    }

    @Override
    public APIResponse<Object> modifyUserMarketLevel(HttpServletRequest request, String marketLevel, String userName, String uid, String merchantCode, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + (StringUtils.isEmpty(userName) ? uid : userName) + "&" + marketLevel + "&" + timestamp;
            APIResponse<Object> errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                return errorResult;
            }
            String mLevel = marketReverseMap.get(Integer.parseInt(marketLevel));
            if (mLevel == null) {
                log.error("modifyUserMarketLevel.mLevel=" + mLevel);
                return APIResponse.returnFail(ApiResponseEnum.PARAMETER_INVALID);
            }
            modifyMarketLevel(merchantCode, userName, uid, marketLevel);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error(merchantCode + "UserController.modifyUserMarketLevel,exception:" + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public APIResponse<Object> getUserMarketLevel(HttpServletRequest request, String userName, String uid, String merchantCode, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + (StringUtils.isEmpty(userName) ? uid : userName) + "&" + timestamp;
            APIResponse<Object> errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                return errorResult;
            }
            UserPO userPO;
            if (StringUtils.isNotEmpty(userName)) {
                //userPO = userMapper.getUserByUserName(merchantCode, userName);
                userPO = cacheService.getUserCheckBalanceCache(merchantCode, userName);
            } else {
                userPO = userMapper.getUserInfo(Long.valueOf(uid));
            }
            if (userPO == null) {
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            List<UserLevelRelationVO> userLevelList = userMapper.getUserLevelList(userPO.getUserId());
            Integer marketLevel = getMarketLevel(merchantPO, userPO, "pc", userLevelList);
            Map<String, Object> result = new HashMap<>();
            result.put("userName", userName);
            result.put("uid", uid);
            result.put("marketLevel", marketLevel);
            log.info("getUserMarketLevel.result:" + result);
            return APIResponse.returnSuccess(result);
        } catch (Exception e) {
            log.error(merchantCode + "UserService.getUserMarketLevel,exception:" + userName, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    private void modifyMarketLevel(String merchantCode, String temUser, String uid, String marketLevel) {
        if (StringUtils.isNotEmpty(temUser)) {
            //UserPO userPO = userMapper.getUserByUserName(merchantCode, temUser);
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, temUser);
            if (userPO == null) {
                return;
            }
            userMapper.updateUserMarketLevel(userPO.getUserId(), Integer.parseInt(marketLevel));
        } else {
            userMapper.updateUserMarketLevel(Long.parseLong(uid), Integer.parseInt(marketLevel));
        }
    }

    private APIResponse checkUserLogin(String merchantCode, Boolean imitate) {
        if ("off".equals(allUserLoginOff)) {
            log.info("用户登录开关" + allUserLoginOff);
            return APIResponse.returnFail(ApiResponseEnum.USER_LOGIN_FAILED);
        }
        if (StringUtils.isNotEmpty(merchantUserLoginOff) && merchantUserLoginOff.contains(merchantCode)) {
            log.info("商户所属用户登录开关关闭," + merchantUserLoginOff);
            return APIResponse.returnFail(ApiResponseEnum.USER_LOGIN_FAILED);
        }
        /**
         * 模拟登陆,自定义会话时长的商户不做预加载
         */
        if (imitate != null && imitate && merchantSession.contains(merchantCode)) {
            log.error("用户登陆失败,imitate=" + imitate + ",merchantCode=" + merchantCode);
            return APIResponse.returnFail(ApiResponseEnum.USER_LOGIN_FAILED);
        }
        return null;
    }

    private String getFakeName(String merchantCode, String username, Integer userPrefix) {
        if (userPrefix == null || userPrefix == 0) {
            return merchantCode + "_" + username;
        }
        StringBuilder tempSb = new StringBuilder();
        for (int i = 0; i < userPrefix; i++) {
            tempSb.append("*");
        }
        if (username.length() <= userPrefix) {
            return merchantCode + "_" + tempSb;
        }
        StringBuilder sb = new StringBuilder(username);
        StringBuilder fake = sb.replace(0, userPrefix, tempSb.toString());
        return merchantCode + "_" + fake;
    }

    private Integer getSettleInAdvanceSwitch(MerchantPO merchantPO, UserPO userPO) {
        Integer settleSwitchUser = userPO.getSettleInAdvance();
        Integer settleSwitchMer = merchantPO.getSettleSwitchAdvance();
        log.info(userPO.getUserId() + "settleSwitchUser==" + settleSwitchUser + ",settleSwitchMerchant=" + settleSwitchMer);
        return settleSwitchUser != null && settleSwitchMer != null && settleSwitchUser == 1 && settleSwitchMer == 1 ? 1 : 0;
    }

    private Integer getSettleSwitchBasket(MerchantPO merchantPO, UserPO userPO) {
        //Integer settleSwitchUserBasket = userPO.getSetBa();
        Integer settleSwitchMerBasket = merchantPO.getSettleSwitchBasket();
        //log.info(userPO.getUserId() + "settleSwitchUserBasket==" + settleSwitchUserBasket + ",settleSwitchMerBasket=" + settleSwitchMerBasket);
        //return settleSwitchUserBasket != null && settleSwitchMerBasket != null && settleSwitchUserBasket == 1 && settleSwitchMerBasket == 1 ? 1 : 0;
        return settleSwitchMerBasket != null && settleSwitchMerBasket == 1 ? 1 : 0;
    }

    private void takePartInActivityOrNot(UserPO userPO, UserApiVo userApiVo, MerchantPO merchantPO, String terminal, String domainGroupCode, String language) {
        try {
            String apiDomain = userApiVo.getApiDomain();
            String merchantCode = merchantPO.getMerchantCode();
            StringBuilder sb = new StringBuilder();
            sb.append(userApiVo.getDomain()).append("?token=").append(userApiVo.getToken());
            MerchantConfig merchantConfig = cacheService.getMerchantCache(merchantCode);
            log.info(userPO.getUserId() + "," + userPO.getUsername() + ",domainGroupCode=" + domainGroupCode);
            sb.append("&gr=").append(domainGroupCode);
            String defaultLanguage = "";
            if (merchantConfig != null) {
                if (terminal.equalsIgnoreCase("PC") && merchantConfig.getProfesTag() != null) {
                    sb.append("&tm=").append(merchantConfig.getProfesTag());
                } else if (!terminal.equalsIgnoreCase("PC") && merchantConfig.getH5Default() != null) {
                    sb.append("&tm=").append(merchantConfig.getH5Default());
                }
                defaultLanguage = merchantConfig.getDefaultLanguage();
            }
            if (StringUtil.isBlankOrNull(language)) {
                language = StringUtil.isBlankOrNull(userPO.getLanguageName()) ? defaultLanguage : userPO.getLanguageName();
            }
            if (StringUtils.isNotEmpty(language)) {
                sb.append("&lg=").append(language);
            }
            String market = userPO.getUserMarketPrefer();
            if (StringUtils.isBlank(userPO.getUserMarketPrefer())) {
                if (merchantConfig != null && merchantConfig.getMarketDefault() != null) {
                    market = MarketTypeEnum.getMarketTypeEnumByCode(merchantConfig.getMarketDefault()).getMarketType();
                } else {
                    market = MarketTypeEnum.EU.getMarketType();
                }
            }
            sb.append("&mk=").append(market);
            if (specialTheme != null && specialTheme.contains(merchantCode)) {
                sb.append("&stm=").append("blue");
            }
            if (StringUtils.isNotEmpty(apiDomain)) {
                String api = AesUtil.encrypt(apiDomain);
                log.info(apiDomain + ",encrypt=" + api);
                sb.append("&api=").append(api);
            }
            userApiVo.setLoginUrl(sb.toString());
            String result = redisTemp.getKey(MERCHANT_FAMILY + IN_ACTIVITY + merchantCode);
            List<TActivityMerchant> activityMerchantList = null;
            userApiVo.setUrl(sb.toString());
            if (StringUtils.isNotEmpty(result) && result.equalsIgnoreCase("false")) {
                return;
            }
            if (StringUtils.isNotEmpty(result) && !result.equalsIgnoreCase("false")) {
                activityMerchantList = JSON.parseArray(result, TActivityMerchant.class);
            }
            if (CollectionUtils.isEmpty(activityMerchantList)) {
                activityMerchantList = activityMerchantMapper.queryActivityMerchant(merchantCode);
                log.info(merchantCode + ",查询数据库商户参与活动配置:" + activityMerchantList);
                String redisValue = CollectionUtils.isNotEmpty(activityMerchantList) ? JSON.toJSONString(activityMerchantList) : "false";
                redisTemp.setKey(MERCHANT_FAMILY + IN_ACTIVITY + merchantCode,
                        redisValue, EXPIRE_TIME_HALF_HOUR);
            }
            if (CollectionUtils.isNotEmpty(activityMerchantList)) {
                List<Long> activityList = activityMerchantList.stream().map(TActivityMerchant::getActivityId).collect(Collectors.toList());
                String activityIdStr = StringUtils.join(activityList, ',');
                sb.append("&activity=").append(activityIdStr);
            }
            userApiVo.setUrl(sb.toString());
        } catch (Exception e) {
            log.error("takePartInActivityOrNot参加活动异常!" + domainGroupCode, e);
        }
    }

    private Integer getMarketLevel(MerchantPO merchantPO, UserPO userPO, String terminal, List<UserLevelRelationVO> userLevelList) {
        if (StringUtils.isNotBlank(marketLevelSwitch) && marketLevelSwitch.equalsIgnoreCase("off")) {
            return 0;
        }
        Long uid = userPO.getUserId();
        try {
            log.info("getMarketLevel-- userId=" + uid + "terminal=" + terminal + ",merchantPO=" + merchantPO);
            int merchantMarketLevel = (terminal.equalsIgnoreCase("mobile") ? (merchantPO.getTagMarketLevel() == null ? 0 : merchantPO.getTagMarketLevel()) : 0);//除了PC外的其他设备
            Integer dynamicMarketLevelStatus = merchantPO.getDynamicMarketLevelStatus();
            Integer userMarketLevel = userPO.getMarketLevel();
            Integer tagMarketLevel = 0;
            Integer relationUserMarketLevel = null;
            String redisKey = RedisKeyNameConstant.SystemControlConfig_Dynamic_MarketGroup;
            String systemConfig = cacheService.getSystemConfig(redisKey);
            UserLevelRelationVO userLevelRelationVO = null;
            if (CollectionUtils.isNotEmpty(userLevelList)) {
                userLevelRelationVO = userLevelList.get(0);
                tagMarketLevel = userLevelRelationVO.getTagMarketLevelId();
            }
            log.info("getMarketLevel-- userId=" + uid + "systemConfig=" + systemConfig + ",terminal=" + terminal);
            boolean isOpen = false;
            if (StringUtils.isNotEmpty(systemConfig) && systemConfig.equals("1")) {
                log.info("getMarketLevel-- userId=" + uid + "systemMerchantConfig=" + dynamicMarketLevelStatus);
                if (dynamicMarketLevelStatus != null && 1 == dynamicMarketLevelStatus) {
                    Integer userDynamicMarketLevelSwitch = null;

                    if (CollectionUtils.isNotEmpty(userLevelList)) {
                        userDynamicMarketLevelSwitch = userLevelList.get(0).getDynamicMarketLevelSwitch();
                    }
                    if (userDynamicMarketLevelSwitch == null) {
                        isOpen = true;
                    }
                    log.info("getMarketLevel-- userId=" + uid + "systemUserConfig=" + userDynamicMarketLevelSwitch);
                    if (userLevelRelationVO != null && (userDynamicMarketLevelSwitch == null || 1 == userDynamicMarketLevelSwitch)) {
                        relationUserMarketLevel = userLevelRelationVO.getDynamicMarketLevel();
                        log.info("getMarketLevel-- userId=" + uid + "systemRelationUserMarketLevel=" + relationUserMarketLevel);
                        isOpen = true;
                    }
                }
            }
            log.info("getMarketLevel-- userId=" + uid + "userMarketLevel=" + userMarketLevel + ",isOpen=" + isOpen);
            if (!isOpen && userMarketLevel != null && userMarketLevel > 0) {
                log.info("getMarketLevel-- userId=" + uid + "userMarketLevel=" + userMarketLevel);
                return userMarketLevel;
            }
            if (userMarketLevel == null) userMarketLevel = 0;
            if (relationUserMarketLevel == null) relationUserMarketLevel = 0;
            Integer[] mlArry = {merchantMarketLevel, userMarketLevel, relationUserMarketLevel, tagMarketLevel};
            log.info("getMarketLevel-- userId=" + uid + "userMarketLevel=" + userMarketLevel +
                    ",merchantMarketLevel=" + merchantMarketLevel + ",relationUserMarketLevel=" + relationUserMarketLevel + ",tagMarketLevel=" + tagMarketLevel);
            List<Integer> sortList = Arrays.asList(mlArry);
            Collections.sort(sortList);
            return sortList.get(sortList.size() - 1);
        } catch (Exception e) {
            log.info(uid + "getMarketLevel:", e);
            return 0;
        }
    }

    /**
     * 获取域名(a:先到商户配置查看,b:采用通用的域名)
     *
     * @Param: [terminal, merchantPO]
     * @return: java.lang.String
     * @date: 2020/9/13 15:21
     */
    private String getDomain(String terminal, MerchantPO merchantPO) {
        String domain;
        String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
        if (StringUtils.isNotEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = pcDomain;
        } else if (StringUtils.isNotEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = h5Domain;
        } else if (StringUtils.isEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = frontPcUrl;
        } else if (StringUtils.isEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = frontMobileUrl;
        } else if ("H5-1".equalsIgnoreCase(terminal)) {
            domain = front_bwh5_url;
        } else {
            domain = frontinH5Url;
        }
        return domain;
    }

    /**
     * 获取域名(a:先到商户配置查看,b:采用通用的域名)
     *
     * @Param: [terminal, pcDomain, h5Domain]
     * @return: java.lang.String
     * @date: 2020/9/13 15:21
     */
    private String getDomain(String terminal, String pcDomain, String h5Domain) {
        String domain;
        if (StringUtils.isNotEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = pcDomain;
        } else if (StringUtils.isNotEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = h5Domain;
        } else if (StringUtils.isEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = frontPcUrl;
        } else if (StringUtils.isEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = frontMobileUrl;
        } else if ("H5-1".equalsIgnoreCase(terminal)) {
            domain = front_bwh5_url;
        } else {
            domain = frontinH5Url;
        }
        return domain;
    }

    /**
     * 获取域名(a:先到商户配置查看,b:采用通用的域名)
     *
     * @Param: [terminal, pcDomain, h5Domain]
     * @return: java.lang.String
     * @date: 2020/9/13 15:21
     */
    private List<String> getDomainList(String terminal, List<DomainApiVO> pcDomain, List<DomainApiVO> h5Domain) {
        List<String> domain;
        if (CollectionUtils.isNotEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = pcDomain.stream().map(DomainApiVO::getDomainName).collect(Collectors.toList());
        } else if (CollectionUtils.isNotEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = h5Domain.stream().map(DomainApiVO::getDomainName).collect(Collectors.toList());
        } else if (CollectionUtils.isEmpty(pcDomain) && "PC".equalsIgnoreCase(terminal)) {
            domain = Collections.singletonList(frontPcUrl);
        } else if (CollectionUtils.isEmpty(h5Domain) && "MOBILE".equalsIgnoreCase(terminal)) {
            domain = Collections.singletonList(frontMobileUrl);
        } else if ("H5-1".equalsIgnoreCase(terminal)) {
            domain = Collections.singletonList(front_bwh5_url);
        } else {
            domain = Collections.singletonList(frontinH5Url);
        }
        return domain;
    }

    @Override
    public APIResponse updateMaintainCache(Long maintainTime) {
        if (maintainTime != null) {
            redisTemp.setKey(MAINTAIN_TIME, maintainTime.toString(), EXPIRE_TIME_TWELVE_HOUR);
        }
        return APIResponse.returnSuccess();
    }

    @Override
    public APIResponse getMaintainCache() {
        return APIResponse.returnSuccess(redisTemp.get(MAINTAIN_TIME));
    }

    /**
     * 更改用户限额等级
     *
     * @param userInfo
     */
    @Override
    public APIResponse updateUserSpecialBettingLimit(UserPO userInfo) {

        try {
            userMapper.updateUserSpecialBettingLimit(userInfo);
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error("UserService.updateUserSpecialBettingLimit,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 更改用户等级
     *
     * @param rcsUserSpecialBetLimitConfigDataVoList
     * @param merchantCode
     * @param userId
     * @param specialBettingLimit
     * @return
     */
    @Override
    public APIResponse updateUserSpecialBettingLimitLog(List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList, String merchantCode, Long userId, Integer specialBettingLimit) {
        try {
            if (CollectionUtils.isNotEmpty(rcsUserSpecialBetLimitConfigDataVoList)) {
                rcsUserSpecialBetLimitConfigMapper.deleteByUserIdAndLimitType(userId, specialBettingLimit);
                rcsUserSpecialBetLimitConfigMapper.batchInsert(parseRcsUserSpecialBetLimitConfigDataVoList(userId, specialBettingLimit, rcsUserSpecialBetLimitConfigDataVoList));
            }
            return APIResponse.returnSuccess();
        } catch (Exception e) {
            log.error(merchantCode + "UserService.updateUserSpecialBettingLimitLog,exception:", e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }


    private List<RcsUserSpecialBetLimitConfigPO> parseRcsUserSpecialBetLimitConfigDataVoList(Long userId, Integer type, List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList) {
        List<RcsUserSpecialBetLimitConfigPO> result = new ArrayList<>();
        rcsUserSpecialBetLimitConfigDataVoList.forEach(temp -> {
            if (CollectionUtils.isNotEmpty(temp.getRcsUserSpecialBetLimitConfigList1())) {
                temp.getRcsUserSpecialBetLimitConfigList1().forEach(temp1 -> temp1.setId(Long.valueOf(idGeneratorFactory.generateIdByBussiness("rcs", 5))).setUserId(String.valueOf(userId)).setSpecialBettingLimitType(type));
                result.addAll(temp.getRcsUserSpecialBetLimitConfigList1());
            }
            if (CollectionUtils.isNotEmpty(temp.getRcsUserSpecialBetLimitConfigList2())) {
                temp.getRcsUserSpecialBetLimitConfigList2().forEach(temp2 -> temp2.setId(Long.valueOf(idGeneratorFactory.generateIdByBussiness("rcs", 5))).setUserId(String.valueOf(userId)).setSpecialBettingLimitType(type));
                result.addAll(temp.getRcsUserSpecialBetLimitConfigList2());
            }
        });
        return result;
    }

    @Override
    public Response<?> queryRedisCache(Long uid, String merchantCode) {
        String key = RedisKeyNameConstant.USER_LEVEL_RELATION_STATISTICS + uid;
        BigDataUserDynamicsRiskControlStatisticsVO bigDataUserDynamicsRiskControlStatisticsVO = (BigDataUserDynamicsRiskControlStatisticsVO) redisTemp.getObject(RedisKeyNameConstant.BIG_DATA_USER_DYNAMICS_RISK_CONTROL_STATISTICS + uid, BigDataUserDynamicsRiskControlStatisticsVO.class);
        UserLevelRelationStatisticsVO userLevelRelationStatisticsVO = (UserLevelRelationStatisticsVO) redisTemp.getObject(key, UserLevelRelationStatisticsVO.class);

        String redisKey = RedisKeyNameConstant.SystemControlConfig_Dynamic_MarketGroup + uid.toString();
        Object userObj = redisTemp.getObject(redisKey, String.class);

        String redisKey2 = RedisKeyNameConstant.SystemControlConfig_Dynamic_Merchant_MarketGroup + merchantCode.toString();
        Object merchantObj = redisTemp.getObject(redisKey2, String.class);

        String redisKey3 = RedisKeyNameConstant.SystemControlConfig_Dynamic_User_MarketGroup;
        Object systemObj = redisTemp.getObject(redisKey3, String.class);
        return Response.returnSuccess("源数据：" + bigDataUserDynamicsRiskControlStatisticsVO
                + " 结果：" + userLevelRelationStatisticsVO
                + " 缓存配置：系统配置==" + systemObj + ",商户配置==" + merchantObj + ",用户配置==" + userObj);
    }

    /**
     * 查询玩家在线
     * a 验签
     * b 缓存获取玩家
     *
     * @Param: [request, username, merchantCode, timestamp, signature]
     * @return: com.panda.sport.merchant.common.vo.api.APIResponse<java.lang.Object>
     * @date: 2020/8/23 11:39
     */
    @Override
    public APIResponse<Object> refreshBalance(HttpServletRequest request, String username, String merchantCode, Long timestamp, String signature) {
        try {
            String signStr = merchantCode + "&" + username + "&" + timestamp;
            APIResponse<Object> errorResult = checkParam(request, timestamp, merchantCode, signStr, signature);
            if (errorResult != null && !errorResult.getStatus()) {
                return errorResult;
            }
            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
            if (userPO == null) {
                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NOT_FOUND);
            }
            log.info(userPO.getUserId() + ": " + USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userPO.getUserId());
            long uid = userPO.getUserId();


            ExecutorInstance.executorService.submit(() -> {
                try {
                    MQMsgInfo msgInfo = new MQMsgInfo();
                    msgInfo.setTopic(topic);
                    msgInfo.setMqKey(userPO.getUserId().toString());
                    msgInfo.setMqTag("refreshBalance");
                    msgInfo.setObj(uid);
                    messageProduct.sendMessage(msgInfo, "通知前端主动刷新用户余额");
                } catch (Exception e) {
                    log.error("下发用户余额刷新MQ异常!", e);
                }
            });
            return APIResponse.returnSuccess("ok");
        } catch (Exception e) {
            log.error(merchantCode + "UserController.checkUserOnline,exception:" + username, e);
            return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Long queryUidByUserName(String userName) {
        return userMapper.queryUserIdByName(userName);
    }


    private boolean domainListIsEmpty(List<?> param) {
        if (CollectionUtils.isEmpty(param)) {
            return true;
        }
        return param.size() == 1 && Objects.isNull(param.get(0));
    }
}
