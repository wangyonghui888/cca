package com.panda.sport.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.panda.sport.admin.feign.MerchantApiClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.RcsUserConfigLimitService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.backup.mapper.BackupRcsUserSpecialBetLimitConfigMapper;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.UserLimitEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.mapper.MerchantNewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static com.panda.sport.merchant.common.constant.Constant.CASH_MARKET_LEVEL_LIST;
import static com.panda.sport.merchant.common.constant.Constant.CREDIT_MARKET_LEVEL_LIST;

/**
 * @author javier
 * 2021-02-09
 * 服务实现
 */
@Slf4j
@Service("rcsUserConfigLimitService")
class RcsUserConfigLimitServiceImpl implements RcsUserConfigLimitService {

    /**
     * 分库以后 所有用户的级别都要去汇总库（备份库查询）
     *
     * @Autowired private TUserMapper tUserMapper;
     */
    @Autowired
    private BackupTUserMapper tUserMapper;

    @Resource
    private BackupRcsUserSpecialBetLimitConfigMapper backupRcsUserSpecialBetLimitConfigMapper;

    @Resource
    private MerchantNewsMapper merchantNewsMapper;

    @Resource
    private MerchantMapper merchantMapper;
    @Resource
    private MerchantLogService merchantLogService;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Override
    public UserPO getUserLimit(String userId) {
        return tUserMapper.getUserLimitInfo(userId);
    }


    @Override
    public RcsUserConfigDetailVO detail(UserPO userInfo) {
        RcsUserConfigDetailVO result = new RcsUserConfigDetailVO();
        result.setUserId(String.valueOf(userInfo.getUserId()));
        result.setSpecialBettingLimitType(userInfo.getSpecialBettingLimitType());
        result.setSpecialBettingLimitTime(userInfo.getSpecialBettingLimitTime());
        result.setSpecialBettingLimitRemark(userInfo.getSpecialBettingLimitRemark());
        result.setMarketLevel(userInfo.getMarketLevel());
        result.setSpecialBettingLimitDelayTime(userInfo.getSpecialBettingLimitDelayTime());
        if (null == userInfo.getSpecialBettingLimitType() || UserLimitEnum.LIMIT_TYPE_1.getCode().equals(userInfo.getSpecialBettingLimitType())) {
            return result;
        }
        result.setRcsUserSpecialBetLimitConfigList(backupRcsUserSpecialBetLimitConfigMapper.selectListByUserIdAndType(userInfo.getUserId().toString(), userInfo.getSpecialBettingLimitType()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void saveUserLimit(RcsUserConfigParamVO rcsUserConfigParamVo, String language, JwtUser user, String ip) throws
            Exception {
        RcsUserConfigVO rcsUserConfigVo = rcsUserConfigParamVo.getRcsUserConfigVo();
        UserPO userInfo = tUserMapper.getUserInfo(Long.parseLong(rcsUserConfigVo.getUserId()));
        Assert.notNull(userInfo, "用户信息没有找到,参数{}", rcsUserConfigParamVo.toString());
        MerchantPO merchant = merchantMapper.getMerchantByMerchantCode(userInfo.getMerchantCode());
        Assert.notNull(merchant, "商户信息没有找到,参数{}", userInfo.getMerchantCode());
        //更新用户表
        log.info("userInfo.getMerchantCode():" + userInfo.getMerchantCode() + ",tag=" + merchant.getMerchantTag());
        // 更改用户限额

        updateUserLimit(userInfo, rcsUserConfigVo, merchant.getMerchantTag(), language, user, ip);
        //批量添加配置数据
        List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList = rcsUserConfigParamVo.getRcsUserSpecialBetLimitConfigDataVoList();
        /* // 更换微服务调用 源库操作
        if (CollectionUtils.isNotEmpty(rcsUserSpecialBetLimitConfigDataVoList)) {
            backupRcsUserSpecialBetLimitConfigMapper.deleteByUserIdAndLimitType(userInfo.getUserId(), rcsUserConfigVo.getSpecialBettingLimit());
            backupRcsUserSpecialBetLimitConfigMapper.batchInsert(parseRcsUserSpecialBetLimitConfigDataVoList(userInfo.getUserId(), rcsUserConfigVo.getSpecialBettingLimit(), rcsUserSpecialBetLimitConfigDataVoList));
        }*/

        merchantApiClient.updateUserSpecialBettingLimitLog(rcsUserSpecialBetLimitConfigDataVoList, userInfo.getMerchantCode(), userInfo.getUserId(), rcsUserConfigVo.getSpecialBettingLimit());
        //添加商户消息
        addMerchantNews(userInfo, merchant, UserLimitEnum.LIMIT_TYPE_1.getCode().equals(rcsUserConfigVo.getSpecialBettingLimit()) ? "Cancel" : "Confirm", language);
    }


    private void updateUserLimit(UserPO userInfo, RcsUserConfigVO rcsUserConfigVo, Integer merchantTag, String
            language, JwtUser user, String ip) throws Exception {
        userInfo.setSpecialBettingLimitType(rcsUserConfigVo.getSpecialBettingLimit());
        userInfo.setSpecialBettingLimitTime(System.currentTimeMillis());
        userInfo.setSpecialBettingLimitRemark(rcsUserConfigVo.getRemarks());
//        userInfo.setSpecialBettingLimitDelayTime(rcsUserConfigVo.getBetExtraDelay());
        Integer oldMarketLevel = userInfo.getMarketLevel() == null ? 0 : userInfo.getMarketLevel();
        Integer marketLevel = rcsUserConfigVo.getMarketLevel() == null ? 0 : rcsUserConfigVo.getMarketLevel();
        if (marketLevel != null) {
            if ((merchantTag != null && merchantTag == 1 && CREDIT_MARKET_LEVEL_LIST.contains(marketLevel)) ||
                    (CASH_MARKET_LEVEL_LIST.contains(marketLevel) && (merchantTag == null || merchantTag == 0))) {
                userInfo.setMarketLevel(marketLevel);
            } else if ((merchantTag != null && merchantTag == 1 && !CREDIT_MARKET_LEVEL_LIST.contains(marketLevel))
                    || (!CASH_MARKET_LEVEL_LIST.contains(marketLevel) && (merchantTag == null || merchantTag == 0))) {
                throw new Exception("行情等级错误!");
            }
        }

        try {
            MerchantLogFiledVO logvo = new MerchantLogFiledVO();
            StringBuffer fidldName = new StringBuffer();
            StringBuffer beforeValues = new StringBuffer();
            StringBuffer afterValues = new StringBuffer();
            if (marketLevel != null) {
                fidldName.append(MerchantFieldUtil.FIELD_MAPPING.get("marketLevel"));
                beforeValues.append(oldMarketLevel);
                afterValues.append(marketLevel);
            }
            if (rcsUserConfigVo.getBetExtraDelay() != null) {
                fidldName.append(MerchantFieldUtil.FIELD_MAPPING.get("betExtraDelay"));
                beforeValues.append(userInfo.getSpecialBettingLimitDelayTime() == null? "-" : userInfo.getSpecialBettingLimitDelayTime());
                afterValues.append(rcsUserConfigVo.getBetExtraDelay());
            }
            if (rcsUserConfigVo.getPercentage() != null) {
                fidldName.append(MerchantFieldUtil.FIELD_MAPPING.get("percentage"));
                beforeValues.append(userInfo.getSpecialBettingLimitType());
                afterValues.append(rcsUserConfigVo.getPercentage());
            }
            // 获取字段名称
            logvo.getFieldName().add(fidldName.toString());
            // 获取设置前数据
            logvo.getBeforeValues().add(beforeValues.toString());
            // 获取设置后数据
            logvo.getAfterValues().add(afterValues.toString());
            // 保存变更日志
            merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_betting_USER_MANAGER, MerchantLogTypeEnum.EDIT_INFO, logvo,

                    0, user.getId(), user.getUsername(), userInfo.getMerchantCode(), null, String.valueOf(userInfo.getUserId()), language, ip);
        } catch (Exception e) {
            log.info("后台用户{}updateUserLimit{},logo状态{}更新为{}", e);
        }
        log.info("更新用户限额:" + userInfo);
        //tUserMapper.updateUserSpecialBettingLimit(userInfo);
        //fention调用merchantApi服务
        userInfo.setSpecialBettingLimitDelayTime(rcsUserConfigVo.getBetExtraDelay());
        merchantApiClient.updateUserSpecialBettingLimit(userInfo, userInfo.getMerchantCode());
        if (!oldMarketLevel.equals(marketLevel)) {
            this.updateUserCache(userInfo.getUserId(), userInfo.getMerchantCode());
        }
    }
    private void updateUserCache(Long uid, String merchantCode) {
        try {
            merchantApiClient.updateUserCache(uid, merchantCode);
        } catch (Exception e) {
            log.error("kickoutUser踢出用户失败!", e);
        }
    }


    private void addMerchantNews(UserPO userInfo, MerchantPO merchant, String text, String language) {
        String currentTime = DateUtils.getCurrentTime();

        String title = MessageFormat.format("平台对用户{0}{1}了特殊限额", userInfo.getUsername(), text);

        String content = MessageFormat.format("平台于{0}对用户{1}{2}了特殊限额，点击<a href='update'>此处</a>查看详情。",
                currentTime, userInfo.getUsername(), text);
        if (language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
            title = MessageFormat.format("Platform has set bet limit to user {0}{1}", userInfo.getUsername(), text);
            content = MessageFormat.format("Platform{0}to user {1}{2} has set bet limit，click here,to check.<a href='update'>here</a>。",
                    currentTime, userInfo.getUsername(), text);
        }
        Date updateDate = DateUtils.dateStrToDate(currentTime);
        merchantNewsMapper.insert(new MerchantNews()
                .setTitle(title)
                .setContext(content)
                .setIsMerchant(0)
                .setMerchantCode(userInfo.getMerchantCode())
                .setMerchantName(merchant.getMerchantName())
                .setIsRead("0")
                .setSelfIsRead("0")
                .setCreateTime(System.currentTimeMillis())
                .setSendTime(updateDate.getTime())
                .setVisitNumber(0L)
                .setUid(userInfo.getUserId())
                .setType(2));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addChangeRecordHistory(AccountChangeHistoryFindVO vo,HttpServletRequest request) {
        try {
            JwtUser user = SecurityUtils.getUser();
            // 获取系统语言
            String language = request.getHeader("language");
            if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            String userId = null != user.getId()?String.valueOf(user.getId()):null;
            String userName =user.getUsername();
            merchantApiClient.addChangeRecordHistory(vo,vo.getMerchantCode());
            merchantLogService.savePlusDeductionLog(MerchantLogPageEnum.PLUS_DEDUCTION, vo, MerchantLogConstants.MERCHANT_OUT, language, IPUtils.getIpAddr(request),userId,userName);
            return Response.returnSuccess();
        }catch (Exception e) {
            log.error("调用feign===merchantApiClient==api出错!", e);
            return Response.returnFail("调用feign===merchantApiClient==api出错!");
        }
    }

}
