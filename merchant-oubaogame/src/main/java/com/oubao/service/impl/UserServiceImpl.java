package com.oubao.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.oubao.mapper.TMerchantMapper;
import com.oubao.mq.RedisTemp;
import com.oubao.po.*;
import com.oubao.utils.AESUtils;
import com.oubao.utils.RedisConstants;
import com.oubao.vo.*;


import lombok.extern.slf4j.Slf4j;
import com.oubao.mapper.TAccountMapper;
import com.oubao.mapper.TUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oubao.service.UserService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RefreshScope
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private TUserMapper userMapper;

    @Autowired
    private TAccountMapper tAccountMapper;

    @Autowired
    private TMerchantMapper merchantMapper;
    @Autowired
    private RedisTemp redisTemp;

    @Value("${panda_url}")
    private String pandaUrl;

    @Value("${panda_transfer_url}")
    private String transferUrl;

    @Value("${oubao_callback_url}")
    private String callbackUrl;

    @Value("${transfer_switch:off}")
    private String transferSwitch;

    @Value("${register_switch:off}")
    private String registerSwitch;

    @Value("${try_play_switch:off}")
    private String tryPlaySwitch;

    @Value("${jumpSupport:null}")
    private String jumpSupport;
    @Value("${jumpFrom:null}")
    private String jumpFrom;
    @Value("${stoken:aaaabbbbaaa}")
    private String stoken;

    @Value("${front_pc_url:null}")
    private String frontPcUrl;

    @Value("${front_h5_url:null}")
    private String frontH5Url;

    @Value("${front_inh5_url:null}")
    private String frontinH5Url;

    @Value("${front_mobile_url:null}")
    private String frontMobileUrl;

    @Value("${front_bwh5_url:null}")
    private String front_bwh5_url;
    @Value("${DEFAULT_MERCHANT_CODE:111111}")
    private String DEFAULT_MERCHANT_CODE;
    public static final String Colon = ":";
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    //默认商户code
    public static final String TRY_PLAY_MERCHANT_CODE = "000000";
    public static final String USERCENTER_FAMILY_KEY = "usercenter:";
    public static final String USER_ID_KEY_PREFIX = "id:";//用户Id
    public static final String USER_TOKEN_PREFIX = "token:";//用户Token
    public static final String MERCHANT_FAMILY = "merchant:";
    public static final String TRY = "tryPlay:";
    public static final String MERCHANT_CODE_PREFIX = "merchantCode:";//用户Id
    public static List<String> SpecialUserNameList = Lists.newArrayList("|", "_", "*", "@", "/", "\\", "&");
    public final static String regEx = "[ `!@#$%^&*()|{}':;',\\[\\].<>/?~！@#￥%……&*（）|{}【】‘；：”“’。，、？]|\n|\r|\t";
    private final static Integer NUBER_ZERO = 0;

    @Override
    public Object tryPlay(String merchantCode, String userName, String password, String terminal, String languageName) throws Exception {
        try {
            if ("off".equals(tryPlaySwitch)) {
                return "开关已关闭";
            }
            UserPO userInfo;
            if (StringUtils.isEmpty(userName)) {
                userName = "try_" + getRandomString(6);
            }
            if (StringUtils.isEmpty(merchantCode)) {
                merchantCode = DEFAULT_MERCHANT_CODE;
            }
            if (StringUtils.isEmpty(password)) {
                password = DEFAULT_MERCHANT_CODE;
            }
            terminal = StringUtils.isEmpty(terminal) ? "pc" : terminal;
            MerchantPO merchantPO = this.getMerchantPO(merchantCode);
            if (merchantPO == null) {
                return "无默认商户";
            }
            try {
                userInfo = userMapper.getBssUserByUserName(merchantCode, userName);
                if (userInfo == null) {
                    userInfo = createUser(null, userName, password, merchantCode, "1", languageName);
                    if (userInfo == null) {
                        return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
                    }
                    userInfo.setOpenEsport(merchantPO.getOpenEsport());
                    userInfo.setOpenVideo(merchantPO.getOpenVideo());
                    userInfo.setOpenVrSport(merchantPO.getOpenVrSport());
                    userInfo.setMarketLevel(merchantPO.getTagMarketLevel());
                    userInfo.setSettleInAdvance(merchantPO.getSettleSwitchAdvance());
                    userInfo.setSettleSwitchBasket(merchantPO.getSettleSwitchBasket());
                    userInfo.setDomainGroupCode(merchantPO.getDomainGroupCode());
                }
            } catch (Exception e) {
                log.error("创建用户失败:" + userName, e);
                throw new Exception("创建用户失败!");
            }
            Long userId = userInfo.getUserId();
            userInfo.setUsername(userName);
            userInfo.setTransferMode(2);
            if (StringUtils.isNotEmpty(languageName)) {
                userInfo.setLanguageName(languageName);
            }
            String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId);
            if (StringUtils.isEmpty(token)) {
                deleteHistoryLogin(userId);
                token = TokenProccessor.makeToken(userId);
                userInfo.setDuration(3600 * 48);
                userInfo.setTerminal(terminal);
                redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId + Colon + terminal, token, 3600 * 48);
                redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, 3600 * 48);
                String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
                if (StringUtils.isAllEmpty(frontPcUrl, frontH5Url, pcDomain, h5Domain)) {
                    log.error("panda主页域名未配置!");
                    return "panda主页域名未配置!";
                }
            }
            String domain = getDomain(terminal, merchantPO);
            Map<String, Object> returnResult = new HashMap<>();
            returnResult.put("token", token);
            returnResult.put("userName", userName);
            returnResult.put("domain", getDomain(terminal, merchantPO));
            returnResult.put("lang", userInfo.getLanguageName());
            returnResult.put("loginUrl", domain + "?token=" + token + "&gr=common");
            log.info(userName + "登录结果:" + returnResult);
            return returnResult;
        } catch (Exception e) {
            log.error("tryPlay,exception:", e);
            throw new Exception("登录异常!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> tryPlayer(UserPO user, String terminal) throws Exception {
        if ("off".equals(tryPlaySwitch)) {
            throw new RuntimeException("试玩开关没有打开");
        }
        MerchantPO merchantPO = this.getMerchantPO(user.getMerchantCode());
        if (merchantPO == null) {
            throw new RuntimeException("默认商户未创建");
        }
        String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
        if (StringUtils.isAllEmpty(frontPcUrl, frontH5Url, pcDomain, h5Domain)) {
            throw new RuntimeException("panda主页域名未配置");
        }
        //查询用户是否注册过，没有则重新注册
        UserPO userInfo = userMapper.getUser(null, user.getUsername(), null, null, user.getMerchantCode());
        if (userInfo == null) {
            userInfo = createUserAndAccount(user);
        }
        deleteHistoryLogin(userInfo.getUserId());
        String token = TokenProccessor.makeToken(userInfo.getUserId());
        redisTemp.setWithExpireTime(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userInfo.getUserId(), token, 3600);
        redisTemp.setObject(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token, userInfo, 3600);
        Map<String, Object> returnResult = new HashMap<>();
        returnResult.put("token", token);
        returnResult.put("userName", user.getUsername());
        returnResult.put("domain", getDomain(terminal, merchantPO));
        log.info("登录结果:{}", returnResult);
        return returnResult;
    }

    public UserPO createUser(String ipAddr, String username, String password, String merchantCode, String currency, String languageName) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(username);
        if (SpecialUserNameList.contains(username) || (username.length() <= 2 && m.find())) {
            log.error(username + ",用户名包含特殊字符!" + new Date());
            return null;
        }
        Long userId = snowflakeIdWorker.nextId();
        UserPO userPO = new UserPO();
        userPO.setUserId(userId);
        userPO.setPassword(password);
        userPO.setMerchantCode(merchantCode);
        userPO.setRealName(username);
        userPO.setUsername(username);
        userPO.setCurrencyCode(StringUtils.isEmpty(currency) ? "1" : currency);
        userPO.setLanguageName(StringUtils.isEmpty(languageName) ? "zs" : languageName);
        userPO.setUserLevel(1);
        userPO.setPhone("111");
        userPO.setIpAddress(null == ipAddr ? "111" : ipAddr);
        userPO.setUserBetPrefer(2);
        userPO.setEmail("111");
        long nowTime = System.currentTimeMillis();
        userPO.setCreateTime(nowTime);
        userPO.setModifyTime(nowTime);
        userPO.setCreateUser(username);
        int result = this.userMapper.insertBssTUser(userPO);
        if (result == 0) {
            return null;
        }
        AccountPO accounts = new AccountPO();
        accounts.setUid(userId);
        accounts.setAmount(new BigDecimal(1000000000));
        accounts.setDisabled(0);
        accounts.setCreateUser(username);
        accounts.setModifyUser(username);
        this.tAccountMapper.insertBssAccount(accounts);
        return userPO;
    }

    public UserPO createUserAndAccount(UserPO userPO) {
        Pattern p = Pattern.compile(regEx);
        final String username = userPO.getUsername();
        Matcher m = p.matcher(username);
        if (SpecialUserNameList.contains(username) || (username.length() <= 2 && m.find())) {
            log.error(username + ",用户名包含特殊字符!" + new Date());
            return null;
        }
        Long userId = snowflakeIdWorker.nextId();
        userPO.setUserId(userId);
        userPO.setRealName(username);
        userPO.setCurrencyCode("1");
        userPO.setUserLevel(1);
        userPO.setPhone("15888888888");
        userPO.setUserBetPrefer(2);
        userPO.setEmail("15888888888@163.com");
        long nowTime = System.currentTimeMillis();
        userPO.setCreateTime(nowTime);
        userPO.setModifyTime(nowTime);
        userPO.setCreateUser(username);
        int result = this.userMapper.insertBssTUser(userPO);
        if (result == 0) {
            return null;
        }
        AccountPO accounts = new AccountPO();
        accounts.setUid(userId);
        accounts.setAmount(new BigDecimal(10000000));
        accounts.setDisabled(0);
        accounts.setCreateUser(username);
        accounts.setModifyUser(username);
        this.tAccountMapper.insertBssAccount(accounts);
        return userPO;
    }

    private String getDomain(String terminal, MerchantPO merchantPO) {
        String domain;
        String pcDomain = merchantPO.getPcDomain(), h5Domain = merchantPO.getH5Domain();
        if (StringUtils.isNotEmpty(pcDomain) && "PC".equals(terminal.toUpperCase())) {
            domain = pcDomain;
        } else if (StringUtils.isNotEmpty(h5Domain) && "MOBILE".equals(terminal.toUpperCase())) {
            domain = h5Domain;
        } else if (StringUtils.isEmpty(pcDomain) && "PC".equals(terminal.toUpperCase())) {
            domain = frontPcUrl;
        } else if (StringUtils.isEmpty(h5Domain) && "MOBILE".equals(terminal.toUpperCase())) {
            domain = frontMobileUrl;
        } else if ("H5-1".equals(terminal.toUpperCase())) {
            domain = front_bwh5_url;
        } else {
            domain = frontinH5Url;
        }
        return domain;
    }

    protected MerchantPO getMerchantPO(String merchantCode) {
        String key = MERCHANT_FAMILY + MERCHANT_CODE_PREFIX + TRY + merchantCode;
        MerchantPO merchantPO = (MerchantPO) redisTemp.getObject(key, MerchantPO.class);
        if (merchantPO == null) {
            merchantPO = merchantMapper.getBssMerchant(merchantCode);
            if (merchantPO != null) {
                redisTemp.setObject(key, merchantPO, 3600);
            }
            log.info("database查询商户" + merchantCode + ",merchant具体信息----->" + merchantPO);
        }
        if (merchantPO == null) {
            log.error("查询商户失败:" + merchantCode);
        }
        return merchantPO;
    }

    private String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private void deleteHistoryLogin(Long userId) {
        String token = redisTemp.get(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId);
        if (StringUtils.isNotEmpty(token)) {
            log.info(userId + ",删除用户会话:" + token);
            redisTemp.delete(USERCENTER_FAMILY_KEY + USER_TOKEN_PREFIX + token);
            redisTemp.delete(USERCENTER_FAMILY_KEY + USER_ID_KEY_PREFIX + userId);
        }
    }

    /**
     * @param userId
     * @param username
     * @param phone
     * @param email
     * @return UserVo
     * @description 根据用户号查询对应账户 ,没有查询结果时返回为null，供外部系统或内部调用
     */
    public UserApiVo getUserVoByUserIdOrUsernameOrPhoneOrEmail(Long userId, String username, String phone,
                                                               String email, String password, String merchantCode) throws Exception {
        if (userId == null && username == null && phone == null) {
            throw new Exception("userId,username和phone至少有一个不为空！");
        }
        merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
        log.info(username + "登录商户:" + merchantCode);
        UserPO userPO = userMapper.getUserVoByUserIdOrUsernameOrPhoneOrEmail(userId, username, phone, email, password, merchantCode);
        AccountPO accountPO = tAccountMapper.checkBalance(username, merchantCode);
        if (userPO != null) {
            UserApiVo userVo = build(userPO);
            String token = TokenProccessor.makeToken(userPO.getUserId());
            userVo.setToken(token);
            userVo.setBalance(accountPO.getAmount());
            userVo.setMerchantCode(merchantCode);
            return userVo;
        }
        return null;
    }

    /**
     * @param userId
     * @param username
     * @param phone
     * @param email
     * @return UserVo
     * @description 根据用户号，用户名称，手机号或emial查询对应账户信息是否已经存在
     */
    @Override
    public UserPO getUser(Long userId, String username, String phone, String email, String merchantCode) throws Exception {
        if (userId == null && username == null && phone == null) {
            throw new Exception("userId、username、phone、email至少有一个不为空！");
        }
        merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
        return userMapper.getUser(userId, username, phone, email, merchantCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changeCredit(String userstr, Integer type, Double credit, String merchantCode) {
        try {
            merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;

            String[] userArr = userstr.split(",");
            for (String user : userArr) {
                AccountPO accountPO = tAccountMapper.checkBalance(user, merchantCode);
                Double amount = accountPO.getAmount().doubleValue();
                Long bizType;
                if (type == 1) {
                    amount = amount + credit;
                    bizType = 4L;
                } else {
                    amount = amount - credit;
                    bizType = 5L;

                }
                if (amount < 0) {
                    return false;
                }
                tAccountMapper.updateAccount(accountPO.getUid(), BigDecimal.valueOf(amount));
                TAccountChangeHistoryPO po = new TAccountChangeHistoryPO();
                po.setChangeAmount(BigDecimal.valueOf(credit));
                po.setAccountId(accountPO.getId());
                po.setUid(accountPO.getUid());
                po.setChangeType(type);
                po.setBizType(bizType);
                po.setCreateUser(user);
                po.setModifyUser(user);
                po.setCurrentBalance(accountPO.getAmount());
                po.setMerchantCode(merchantCode);
                po.setCreateTime(System.currentTimeMillis());
                po.setModifyTime(System.currentTimeMillis());
                tAccountMapper.insertAccountChange(po);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2019-01-01 10:00:00
     *
     * @param user
     * @param start
     * @param end
     * @return
     */
    @Override
    public PageVO<UserPO> queryUserList(String user, String start, String end, String merchantCode, Integer page, Integer size) {
        PageVO<UserPO> pageVO;
        try {
            merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;

            Long startTime = null, endTime = null;
            if (StringUtils.isNotEmpty(start)) {
                startTime = DateUtils.parseDate(start, "yyyy-MM-dd HH:mm:ss").getTime();
            }
            if (StringUtils.isNotEmpty(end)) {
                endTime = DateUtils.parseDate(end, "yyyy-MM-dd HH:mm:ss").getTime();
            }
            page = (page == null ? 1 : page);
            size = (size == null ? 20 : size);
            Integer totCnt = userMapper.countUserList(user, merchantCode, startTime, endTime);
            Integer startRow = (page - 1) * size;
            if (totCnt > 0) {
                List<UserPO> userPO = userMapper.queryUserList(user, merchantCode, startTime, endTime, startRow, size);
                pageVO = new PageVO(totCnt, size, page);
                pageVO.setRecords(userPO);
            } else {
                pageVO = new PageVO(0, size, page);
            }
        } catch (Exception e) {
            log.error("BetOrderServiceImpl.queryBetOrderList查询注单异常!", e);
            return null;
        }
        return pageVO;
    }


    @Override
    public PageVO<TAccountChangeHistoryPO> queryAccountChangeList(String userName, String merchantCode, String start, String end, Integer page, Integer size) {
        PageVO<TAccountChangeHistoryPO> pageVO;
        try {
            Long startTime = null, endTime = null;
            if (StringUtils.isNotEmpty(start)) {
                startTime = DateUtils.parseDate(start, "yyyy-MM-dd HH:mm:ss").getTime();
            }
            if (StringUtils.isNotEmpty(end)) {
                endTime = DateUtils.parseDate(end, "yyyy-MM-dd HH:mm:ss").getTime();
            }
            page = (page == null ? 1 : page);
            size = (size == null ? 20 : size);
            Integer totCnt = tAccountMapper.countAccountChangeList(userName, merchantCode, startTime, endTime);
            Integer startRow = (page - 1) * size;
            if (totCnt > 0) {
                List<TAccountChangeHistoryPO> userPO = tAccountMapper.queryAccountChangeList(userName, merchantCode, startTime, endTime, startRow, size);
                pageVO = new PageVO(totCnt, size, page);
                pageVO.setRecords(userPO);
            } else {
                pageVO = new PageVO(0, size, page);
            }
        } catch (Exception e) {
            log.error("BetOrderServiceImpl.queryAccountChangeList异常!", e);
            return null;
        }
        return pageVO;
    }

    @Override
    public PageVO<OrderPO> queryOrderList(String userName, String start, String end, Integer page, Integer size,
                                          String merchantCode) {
        PageVO<OrderPO> pageVO;
        try {
            Long startTime = null, endTime = null;
            if (StringUtils.isNotEmpty(start)) {
                startTime = DateUtils.parseDate(start, "yyyy-MM-dd HH:mm:ss").getTime();
            }
            if (StringUtils.isNotEmpty(end)) {
                endTime = DateUtils.parseDate(end, "yyyy-MM-dd HH:mm:ss").getTime();
            }
            page = (page == null ? 1 : page);
            size = (size == null ? 20 : size);
            Integer totCnt = tAccountMapper.countOrderList(userName, startTime, endTime, merchantCode);
            Integer startRow = (page - 1) * size;
            if (totCnt > 0) {
                List<OrderPO> userPO = tAccountMapper.queryOrderList(userName, startTime, endTime, merchantCode, startRow, size);
                pageVO = new PageVO(totCnt, size, page);
                pageVO.setRecords(userPO);
            } else {
                pageVO = new PageVO(0, size, page);
            }
        } catch (Exception e) {
            log.error("BetOrderServiceImpl.queryAccountChangeList异常!", e);
            return null;
        }
        return pageVO;

    }

    @Override
    public boolean changeOubaoCredit(TransferPO transferPO) {
        String userName = transferPO.getUserName();
        try {
            double betAmount = transferPO.getAmount();
            Integer transferType = transferPO.getTransferType();
            String merchantCode = StringUtils.isEmpty(transferPO.getMerchantCode()) ? DEFAULT_MERCHANT_CODE : transferPO.getMerchantCode();
            AccountPO accountPO = tAccountMapper.checkBalance(userName, merchantCode);
            double amount = accountPO.getAmount().doubleValue();
            Integer bizType = transferPO.getBizType();
            log.info("业务类型:" + bizType);
            long localBizType;
            if (transferType == 1) {
                amount = amount + betAmount;
                localBizType = 3L;
            } else if (transferType == 2) {
                localBizType = 1L;
                amount = amount - betAmount;
            } else {
                localBizType = 0L;
            }
            if (amount < 0) {
                return false;
            }
            tAccountMapper.updateAccount(accountPO.getUid(), BigDecimal.valueOf(amount));
            TAccountChangeHistoryPO po = new TAccountChangeHistoryPO();
            po.setChangeAmount(BigDecimal.valueOf(betAmount));
            po.setAccountId(accountPO.getId());
            po.setUid(accountPO.getUid());
            po.setChangeType(transferType);
            po.setBizType(localBizType);
            po.setCreateUser(userName);
            po.setModifyUser(userName);
            po.setCurrentBalance(accountPO.getAmount());
            po.setMerchantCode(transferPO.getMerchantCode());
            po.setCreateTime(System.currentTimeMillis());
            po.setModifyTime(System.currentTimeMillis());
            po.setRemark("业务:" + bizType);
            tAccountMapper.insertAccountChange(po);
            return true;
        } catch (Exception e) {
            log.error("上加扣款异常!" + transferPO, e);
            return false;
        }
    }

    @Override
    public boolean addAccount(BigDecimal amount, String userName, Long bizType, String merchantCode) {
        try {
            merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
            AccountPO accountPO = tAccountMapper.checkBalance(userName, merchantCode);
            if (bizType == 4L) {
                tAccountMapper.updateAccount(accountPO.getUid(), accountPO.getAmount().subtract(amount));
            } else {
                tAccountMapper.updateAccount(accountPO.getUid(), accountPO.getAmount().add(amount));

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean notifyMerchant(NotifyPO orderPO) {


        System.out.println(orderPO.getTransferId());

        return true;
    }

    @Override
    public boolean notifySafetyMerchant(String merchantCode, String transferId, String userName, String amount, String transferType, Long timestamp, String signature) {
        String key = userMapper.getMerchantKey(merchantCode);
        Boolean bool = MD5Utils.checkMd5(merchantCode + "&" + userName + "&" + transferType + "&" + amount + "&" + transferId + "&" + timestamp, key,signature);
        log.info("signature:" + signature + "   pandaUrl:" + transferUrl);
        return bool;
    }

    @Override
    public BigDecimal checkBalance(String userName, String merchantCode) {
        merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
        return tAccountMapper.checkBalance(userName, merchantCode).getAmount();
    }

    @Override
    public Object loginPanda(String userName, String terminal, String merchantCode) {
        try {
            merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
            String key = userMapper.getMerchantKey(merchantCode);
            if (StringUtils.isEmpty(key)) {
                log.error(merchantCode + " 密钥不存在!");
                return " 密钥不存在!";
            }
            UserPO userPO = userMapper.getUser(null, userName, null, null, merchantCode);
            log.info(merchantCode + ",userName=" + userName + "search result:" + userPO);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("merchantCode", merchantCode);
            params.add("userName", userName);
            params.add("terminal", terminal);
            String currency = userPO.getCurrencyCode();
            if ("RMB".equals(currency)) {
                currency = "1";
            }
            params.add("currency", currency);
            log.info("login currency" + currency);
            BigDecimal balance = tAccountMapper.checkBalance(userName, merchantCode).getAmount();
            params.add("balance", balance);
            params.add("callbackUrl", callbackUrl);
            if (StringUtils.isNotEmpty(jumpSupport)) {
                params.add("jumpsupport", jumpSupport);
            }
            if (StringUtils.isNotEmpty(jumpFrom)) {
                params.add("jumpfrom", jumpFrom);
            }
            if (StringUtils.isNotEmpty(stoken)) {
                params.add("stoken", stoken + System.currentTimeMillis());
            }
            String time = System.currentTimeMillis() + "";
            params.add("timestamp", time);
            String signature = MD5Utils.getMD5(merchantCode + "&" + userName + "&" + terminal + "&" + time, key);
            log.info("signature:" + signature + "   pandaUrl:" + pandaUrl);
            params.add("signature", signature);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response1 = restTemplate.postForEntity(pandaUrl, request, String.class);
            log.info("返回结果:" + response1.getBody());
            JSONObject jsonObject = JSONObject.parseObject(response1.getBody());
            JSONObject object = (JSONObject) jsonObject.get("data");
            object.remove("userId");
            object.remove("url");
            object.remove("createTime");
            object.remove("apiDomain");
            object.remove("username");
            object.remove("imgDomain");

            return object;
        } catch (Exception e) {
            log.error("http 请求panda  异常!", e);
            return null;
        }

    }

    @Override
    public APIResponse checkOubaoCredit(String merchantCode, String userName, String timestamp, String signature) {
        try {
            log.info(merchantCode + "&" + userName + "&" + timestamp + ":" + signature);
            String key = userMapper.getMerchantKey(merchantCode);
            if (StringUtils.isEmpty(key)) {
                return APIResponse.returnFail(merchantCode + " 密钥不存在!");
            }
            boolean flag = MD5Utils.checkMd5(merchantCode + "&" + userName + "&" + timestamp, key, signature);
            if (!flag) {
                return APIResponse.returnFail("验签失败!");
            }
            BigDecimal balance = tAccountMapper.checkBalance(userName, merchantCode).getAmount();
            log.info(userName + "余额:" + balance);
            return APIResponse.returnSuccess(ApiResponseEnum.BlandSUCCESS, balance);
        } catch (Exception e) {
            log.error("http checkOubaoCredit!", e);
            return APIResponse.returnFail("未知错误!");
        }
    }

    /**
     * @param userName
     * @param transferType(1 panda上分(欧宝扣款),2 panda下分(欧宝加款))
     * @param amount
     * @return transferMode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public APIResponse transferPandaCredit(String userName, Integer transferType, BigDecimal amount, String merchantCode) throws Exception {
        merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
        Integer transferMode = merchantMapper.getTransferMode(merchantCode);
        log.info(merchantCode + "转账模式:" + transferMode);
        if (transferMode == null || transferMode != 2) {
            return APIResponse.returnFail("当前商户的转账模式不正确!");
        }
        log.info("上下分开关:" + transferSwitch);
        if ("off".equals(transferSwitch)) {
            return APIResponse.returnFail("当前不支持上下分!");
        }
        String key = userMapper.getMerchantKey(merchantCode);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("merchantCode", merchantCode);
            params.add("userName", userName);
            String transfer = transferType + "";

            String balance = amount + "";

            Random random = new Random();
            int max = 1000;
            int min = 1;
            int s = random.nextInt(max) % (max - min + 1) + min;

            String transferId = s + System.currentTimeMillis() + "";
            params.add("transferType", transfer);
            params.add("transferId", transferId);
            AccountPO accountPO = tAccountMapper.checkBalance(userName, merchantCode);
            if (accountPO == null) {
                return APIResponse.returnFail("用户不存在!");
            }
            log.info(userName + "    hello:" + accountPO);
            Double curBalance = accountPO.getAmount().doubleValue();
            Double newCurbalance;
            if (transferType == 1) {
                if (amount.doubleValue() > curBalance) {
                    return APIResponse.returnFail("余额不足!");
                } else {
                    newCurbalance = curBalance - amount.doubleValue();
                }
            } else {
                newCurbalance = curBalance + amount.doubleValue();
            }
            tAccountMapper.updateAccount(accountPO.getUid(), BigDecimal.valueOf(newCurbalance));
            Long bizType = 0L;
            if (transferType == 1) {
                bizType = 5L;
            } else {
                bizType = 4L;
            }
            TAccountChangeHistoryPO po = new TAccountChangeHistoryPO();
            po.setChangeAmount(amount);
            po.setAccountId(accountPO.getId());
            po.setUid(accountPO.getUid());
            po.setChangeType(transferType == 1 ? 2 : 1);
            po.setBizType(bizType);
            po.setCreateUser(userName);
            po.setModifyUser(userName);
            po.setCurrentBalance(accountPO.getAmount());
            po.setMerchantCode(merchantCode);
            po.setCreateTime(System.currentTimeMillis());
            po.setModifyTime(System.currentTimeMillis());
            tAccountMapper.insertAccountChange(po);
            String time = System.currentTimeMillis() + "";
            params.add("timestamp", time);
            String signature = MD5Utils.getMD5(merchantCode + "&" + userName + "&" + transfer + "&" + balance + "&" + transferId + "&" + time, key);
            log.info("signature:" + signature + "   pandaUrl:" + transferUrl);
            params.add("signature", signature);
            params.add("amount", balance);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response1 = restTemplate.postForEntity(transferUrl, request, String.class);
            log.info("response1:" + response1);
            JSONObject jsonObject = JSONObject.parseObject(response1.getBody());
            log.info("jsonObject:" + jsonObject);
            log.info("code:" + jsonObject.get("code"));
            log.info("msg:" + jsonObject.get("msg"));
            if (!"0000".equals(jsonObject.get("code"))) {
                throw new Exception((String) jsonObject.get("msg"));
            }
            return APIResponse.returnSuccess(jsonObject);
        } catch (Exception e) {
            log.error("http 请求panda  异常!", e);
            if (e.getMessage().contains("玩家余额不足")) {
                throw new Exception("panda玩家余额不足!");
            } else if (e.getMessage().contains("没有此玩家")) {
                throw new Exception("panda没有此玩家!");
            } else {
                throw new Exception("未知异常!");
            }

        }
    }

    /**
     * @param userId
     * @return UserPO
     * @description 根据userId查询用户id是否存在
     */
    @Override
    public UserPO findByUserId(Long userId) {
        UserPO userPO = userMapper.findTUserByCondition(userId);
        return userPO;
    }


    @Override
    public UserApiVo login(String username, String phone, String password, String merchantCode) throws Exception {

        UserApiVo userVo = this.getUserVoByUserIdOrUsernameOrPhoneOrEmail(null, username, phone, null, password,
                merchantCode);
        return userVo;
    }

    /**
     * @param userPO
     * @return accountPo
     * @description 注册用户，返回UserPO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserPO register(UserPO userPO) throws Exception {
        log.info("注册开关:" + registerSwitch);
        if ("off".equals(registerSwitch)) {
            throw new Exception(String.valueOf(ApiResponseEnum.INTERNAL_ERROR));
        }
        String merchantCode = userPO.getMerchantCode();
        merchantCode = StringUtils.isEmpty(merchantCode) ? DEFAULT_MERCHANT_CODE : merchantCode;
        //注册判断商户是否被禁用
        /*MerchantPO merchantPO = this.getMerchant(merchantCode);
        if(merchantPO == null || merchantPO.getId() == null || NUBER_ZERO.equals(merchantPO.getStatus())){
            throw new Exception(String.valueOf(ApiResponseEnum.PROHIBIT_REGISTRATION));
        }*/
        //检查
        //md5处理密码
        userPO.setPassword(MD5Utils.MD5Encode(userPO.getPassword()));
        long nowTime = System.currentTimeMillis();
        userPO.setCreateTime(System.currentTimeMillis());
        //生成userId
        Long newUserId = this.getUserId();
        userPO.setUserId(newUserId);
        userPO.setMerchantCode(merchantCode);
        try {
            this.userMapper.insertTUser(userPO);
            AccountPO accounts = new AccountPO();
            accounts.setUid(newUserId);
            accounts.setAmount(null == userPO.getBalance() ? BigDecimal.ZERO : BigDecimal.valueOf(userPO.getBalance()));
            accounts.setDisabled(0);
            accounts.setCreateUser(newUserId.toString());
            accounts.setModifyUser(newUserId.toString());
            accounts.setMerchantCode(merchantCode);
            this.tAccountMapper.insertAccount(accounts);
            if (TRY_PLAY_MERCHANT_CODE.equals(merchantCode)) {
                createUserAndAccount(userPO);
            }
            //插入登录日志
/*            TLogHistory history = new TLogHistory();
            history.setUid(newUserId);
            history.setUsername(userPO.getUsername());
            history.setLoginTime(nowTime);
            history.setLogDetail("登录");
            history.setLogType(1);
            this.tLogHistoryMapper.insert(history);*/
            //用户注册信息，发送到风控
            //send.sendUserDataMessageToWindControlSystem(userPO);
        } catch (Exception e) {
            log.error("新增用户调用Mapper失败！", e);
            throw new Exception(String.valueOf(ApiResponseEnum.INTERNAL_ERROR));
        }
        return userPO;
    }

    public MerchantPO getMerchant(String merchantCode) throws Exception{
        MerchantPO merchantPO = (MerchantPO) redisTemp.getObject(RedisConstants.MERCHANT_FAMILY +
                RedisConstants.TRY_BUSSINESS + merchantCode, MerchantPO.class);
        if (merchantPO == null) {
            merchantPO = merchantMapper.getMerchant(merchantCode);
            if (merchantPO != null) {
                redisTemp.setObject(RedisConstants.MERCHANT_FAMILY + RedisConstants.TRY_BUSSINESS +
                        merchantCode, merchantPO, RedisConstants.EXPIRE_TIME_ONE_HOUR);
            }
            log.info(new Date() + ",redis缓存商户(0)" + merchantCode + ",merchant具体信息----->" + merchantPO);
        }
        if (merchantPO == null) {
            log.error(new Date() + "," + merchantCode + ApiResponseEnum.MERCHANT_NOT_EXIST.getLabel() + ",rollback now!");
            throw new Exception(String.valueOf(ApiResponseEnum.MERCHANT_NOT_EXIST));
        }
        return merchantPO;
    }

    public long getUserId() {

        int max = 200000;
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        //生成userId
        Long userId = System.currentTimeMillis() + s + random.nextInt(max);
        UserPO userPO = this.findByUserId(userId);
        if (userPO != null) {
            getUserId();
        }
        return userId;
    }

    /**
     * @param userPO
     * @return accountPo
     * @description 根据是否有userId做更新操作，返回UserPO
     */
    @Override
    public UserPO update(UserPO userPO) throws Exception {
        long nowTime = System.currentTimeMillis();
        if (StringUtils.isNotBlank(userPO.getPassword())) {
            //md5处理密码
            userPO.setPassword(MD5Utils.MD5Encode(userPO.getPassword()));
        }
        try {
            userPO.setModifyTime(nowTime);
            userMapper.updateTUser(userPO);
        } catch (Exception e) {
            log.error("更新用户调用Mapper失败！", e);
            throw new Exception(String.valueOf(ApiResponseEnum.INTERNAL_ERROR.getId()));
        }
        return userPO;
    }

    /**
     * @param userPO
     * @return UserVo
     * @description 由UserPO构建UserVo对象
     */
    public static UserApiVo build(UserPO userPO) {
        UserApiVo userVo = new UserApiVo();
        userVo.setUserId(userPO.getUserId().toString());
        //userVo.setDisabled(userPO.getDisabled());
        userVo.setCreateTime(userPO.getCreateTime());
        userVo.setEmail(userPO.getEmail());
        userVo.setIdCard(userPO.getIdCard());
        //userVo.setPassword(userPO.getPassword());
        userVo.setPhone(userPO.getPhone());
        userVo.setRealName(userPO.getRealName());
        userVo.setRemark(userPO.getRemark());
        userVo.setUsername(userPO.getUsername());
        return userVo;
    }

    /**
     * @param userPO
     * @return
     * @throws
     * @deprecated 检查参数accountPO是否合法，当accountPO == null或者（accountPo的属性值uId和id同时为null）时抛出异常，否则返回true
     */
    public static boolean check(UserPO userPO) throws Exception {
        if (userPO.getUserId() == null) {
            log.error("userPO的属性值userId不能为null!");
            throw new Exception(String.valueOf(ApiResponseEnum.INTERNAL_ERROR.getId()));
        }
        return true;

    }
}
