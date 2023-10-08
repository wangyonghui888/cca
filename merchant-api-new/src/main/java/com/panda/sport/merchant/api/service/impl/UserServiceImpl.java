package com.panda.sport.merchant.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.panda.sport.bss.mapper.*;
import com.panda.sport.merchant.api.config.IdGeneratorFactory;
import com.panda.sport.merchant.api.feign.MerchantReportClient;
import com.panda.sport.merchant.api.feign.MultiterminalClient;
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
import com.panda.sport.merchant.common.vo.MerchantEventSwitchVO;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.common.vo.api.*;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
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
    private CreateUserService createUserService;

    @Autowired
    private MQProducer mqProducer;

    @Autowired
    private IdGeneratorFactory idGeneratorFactory;

    @Autowired
    private ActivityMerchantMapper activityMerchantMapper;

    @Autowired
    private IpUtil ipUtil;

    @Autowired
    private DomainServiceUtil domainServiceUtil;

    @Autowired
    private MultiterminalClient multiterminalClient;

    @Autowired
    private MerchantReportClient reportClient;

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

    @Value("${login.front.domain.pool:off}")
    private String frontDomainPool;

    @Value("${login.history.switch:off}")
    private String loginHistorySwitch;

    @Value("${login.domain.switch:off}")
    private String domainPoolSwitch;

    @Value("${login.domain.pool.merchant.codes:null}")
    private String domainPoolMerchantCodeStr;

    private static final Map<String, Integer> marketMap = new HashMap<>();
    private static final Map<Integer, String> marketReverseMap = new HashMap<>();

    public static Cache<String, List<DomainGroupApiVO>> domainPoolCacheMap = null;

    public static Cache<String, List<UserVipVO>> merchantVipDetailCacheMap = null;

    public static Cache<String, String> merchantAreaDomainsCacheMap = null;

    public static Cache<String, Object> merchantDomainMap = null;

    public static Cache<String, List<SystemConfig>> frontDomainCache = null;


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

        merchantDomainMap = Caffeine.newBuilder()
                .maximumSize(1000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .recordStats() // 开启缓存统计
                .build();

        frontDomainCache = Caffeine.newBuilder()
                .maximumSize(3000) // 设置缓存的最大容量
                .expireAfterWrite(1, TimeUnit.MINUTES)
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
    public APIResponse create(HttpServletRequest request, String username, String nickname, String merchantCode, Long timestamp, String currency, String agentId, String signature) throws Exception {
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
//            UserPO userPO = cacheService.getUserCheckBalanceCache(merchantCode, username);
//            if (userPO != null) {
//                log.error(merchantCode + "创建账号异常!用户已存在!" + username);
//                return APIResponse.returnFail(ApiResponseEnum.PLAYER_NAME_EXIST);
//            }
            if (!cacheService.validateUserName(merchantCode, username)) {
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
    public APIResponse<Object> login(HttpServletRequest request, String username, String terminal, String merchantCode, String currency, String callbackUrl, Long timestamp, String signature, String jumpsupport, String jumpfrom, String ip, String stoken, String agentId, Boolean imitate, String language) throws Exception {
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
            log.info("merchantPO.getIsTest:" + merchantPO.getIsTest());
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
                        String newDomain = domainServiceUtil.getDomain(terminal, merchantPO);
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
            UserPO userPO;
            List<UserLevelRelationVO> userLevelList;
            try {
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
                //处理用户二级标签，查询report标签列表，去除无效二级标签
                getUserLevelRelationList(userLevelList);
            } catch (Exception e) {
                log.error("UserServiceImpl.login.创建用户失败:" + username, e);
                throw new Exception("UserServiceImpl.login.创建用户失败!");
            }
            Long userId = userPO.getUserId();
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(userPO, userInfo);
            log.info("userInfo.getIsTestMerchant_before:" + userInfo.getIsTestMerchant());
            userInfo.setIsTestMerchant(merchantPO.getIsTest() != null ? merchantPO.getIsTest() : null);
            log.info("userInfo.getIsTestMerchant_after:" + userInfo.getIsTestMerchant());
            userInfo.setDynamicMargin(getExtraMargin(userLevelList));
            userInfo.setMarketLevel(getMarketLevel(merchantPO, userPO, terminal, userLevelList));
            userInfo.setUserMarketLevel(userPO.getMarketLevel());
            userInfo.setSettleInAdvance(getSettleInAdvanceSwitch(merchantPO, userPO));
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
            userApiVo.setUserId(String.valueOf(userId));
            userApiVo.setIsTest(Objects.isNull(userInfo.getIsTest()) ? 1 : userInfo.getIsTest());

            String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
            if (StringUtils.isAllEmpty(frontPcUrl, frontH5Url, pcDomain, h5Domain)) {
                log.error("panda主页域名未配置!");
                return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
            }
            String domain = domainServiceUtil.getDomain(terminal, merchantPO);
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
            userInfo.setImgDomain(imageDomain);
            userInfo.setApiDomain(userApiVo.getApiDomain());
            userInfo.setLoginUrlArr(userApiVo.getLoginUrlArr());
            userInfo.setLiveH5Url(liveH5Url);
            userInfo.setLivePcUrl(livePcUrl);
            userInfo.setChatroomUrl(chatroomUrl);
            userInfo.setChatroomHttpUrl(chatroomHttpUrl);
            userInfo.setMerchantEventSwitchVO(getMerchantEventInfo(merchantPO));
            log.info("merchantPO.getSettleSwitchBasket()-before:" + merchantPO.getSettleSwitchBasket());
            userInfo.setSettleSwitchBasket(merchantPO.getSettleSwitchBasket() != null && merchantPO.getSettleSwitchBasket() == 1 ? 1 : 0);
            log.info("merchantPO.getSettleSwitchBasket()_after:" + userInfo.getSettleSwitchBasket());

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


    /**
     * 查询二级标签列表，并放入缓存，用户登录时比对标签列表，去除无效标签
     *
     * @param userLevelList
     * @return List<UserLevelRelationVO>
     */
    private List<UserLevelRelationVO> getUserLevelRelationList(List<UserLevelRelationVO> userLevelList) {
        try {
            if (userLevelList != null && userLevelList.size() > 0) {
                UserLevelRelationVO userLevelRelationVO = userLevelList.get(0);
                if (userLevelRelationVO.getSecondaryLabelIdsJson() != null) {
                    String key = SECOND_LEVEL_LIST;
                    List<String> secondLevelId = (List<String>) redisTemp.getObject(key, List.class);
                    if (secondLevelId == null) {
                        secondLevelId = reportClient.queryUserSecondLevelList();
                        log.info("RPC获取getUserLevelRelationList:" + secondLevelId);
                        redisTemp.setObject(key, secondLevelId, RedisConstants.EXPIRE_TIME_ONE_WEEK);
                    }

                    JSONArray jsonArray = new JSONArray(userLevelRelationVO.getSecondaryLabelIdsJson());
                    List<String> list = jsonArray.toList(String.class);
                    List<String> secondIds = Lists.newArrayList();
                    for (String integer : list) {
                        if (secondLevelId.contains(integer)) {
                            secondIds.add(integer);
                        }
                    }
                    JSONArray jsons = new JSONArray(secondIds);
                    userLevelRelationVO.setSecondaryLabelIdsJson(jsons.toString());
                }
            }
        } catch (Exception e) {
            log.error("UserServiceImpl.login.queryUserSecondLevelList error:", e);
        }

        return userLevelList;
    }

    private MerchantEventSwitchVO getMerchantEventInfo(MerchantPO merchantPO) {
        try {
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
            return eventSwitchVO;
        } catch (Exception e) {
            log.error("getMerchantEventInfo:", e);
            return null;
        }
    }

    @Override
    public APIResponse<Object> token(HttpServletRequest request, String username, String merchantCode, Long timestamp, String signature, String token) throws Exception {
        MerchantPO merchantPO = this.getMerchantPO(merchantCode);
        if (merchantPO == null || (merchantPO.getAgentLevel() != 0 && merchantPO.getAgentLevel() != 2)) {
            return APIResponse.returnFail(ApiResponseEnum.MERCHANT_NOT_FOUND);
        }
        String key = AESUtils.aesDecode(merchantPO.getMerchantKey());

        if(!Md5Util.checkMd5(merchantCode+"&"+username+"&"+token+"&"+timestamp,key,signature)) {
            log.info("token:checkMd5-false--"+signature);
            return APIResponse.returnFail(ApiResponseEnum.SIGN_FAILS);
        }
        boolean hasToken = redisTemp.hasKey(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token);
        if(hasToken) {
            UserInfo userInfo = (UserInfo) redisTemp.getObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token,UserInfo.class);
            //int duration = EXPIRE_TIME_THIRTEEN_HOUR;
            int duration = 360*20;
            if (StringUtils.isNotEmpty(merchantSession) && merchantSession.contains(merchantCode)) {
                duration = EXPIRE_TIME_ONE_HOUR;
            }
            redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, duration);
            return APIResponse.returnSuccess(duration);
        }

        return APIResponse.returnFail(ApiResponseEnum.TOKEN_IS_EXPIRED);
    }

    @Override
    public void refreshFrontDomain() {
        domainServiceUtil.initDomainSystemConfigList();
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
    private Boolean buildDomainListByParam(MerchantPO merchantPO, UserApiVo userApiVo, boolean isVipUser, List<DomainGroupApiVO> domainPoolList, String terminal, String ipArea, String groupCode) {
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
        String newDomain = domainServiceUtil.getDomain(terminal, merchantPO);

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
            log.info("getMarketLevel开关：" + marketLevelSwitch);
            return 0;
        }
        Long uid = userPO.getUserId();
        try {
            log.info("getMarketLevel-- userId=" + uid + "terminal=" + terminal + ",merchantPO=" + merchantPO.getTagMarketLevelIdPc() + "," + merchantPO.getTagMarketLevel());
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
            List<Integer> sortList = Arrays.asList(mlArry);
            Collections.sort(sortList);
            log.info("getMarketLevel-- userId=" + uid + "userMarketLevel=" + userMarketLevel
                    + "relationUserMarketLevel=" + relationUserMarketLevel
                    + "tagMarketLevel=" + tagMarketLevel
                    + ",merchantMarketLevel=" + merchantMarketLevel);
            return sortList.get(sortList.size() - 1);
        } catch (Exception e) {
            log.info(uid + "getMarketLevel:", e);
            return 0;
        }
    }

    /**
     * 之前的逻辑 暂时不动
     */
    private String getDomainOld(String terminal, MerchantPO merchantPO) {
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

    private boolean domainListIsEmpty(List<?> param) {
        if (CollectionUtils.isEmpty(param)) {
            return true;
        }
        return param.size() == 1 && Objects.isNull(param.get(0));
    }
}
