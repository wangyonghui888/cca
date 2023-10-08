package com.panda.sport.order.feign;

import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.dto.ActivityBetStatDTO;
import com.panda.sport.merchant.common.vo.finance.FinanceDayVO;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceDayVo;
import com.panda.sport.merchant.common.vo.finance.MerchantFinanceMonthVo;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.merchant.SportVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RemoteHystrix implements BssBackendClient, MerchantApiClient, PandaBSSUsercenterClient,MerchantApiNewClient {


    @Override
    public Object retryTransfer(UserRetryTransferVO vo) {
        log.error("!!!RPC retryTransfer:" + vo);
        return null;
    }

    @Override
    public Object getSportIdByMatchManageId(String matchManageId) {
        log.error("!!!RPC getSportIdByMatchManageId:" + matchManageId);
        return null;
    }

    @Override
    public Object queryListAlbPayoutLimit(Long uid) {
        log.error("!!!RPC queryListAlbPayoutLimit:" + uid);
        return null;
    }


    @Override
    public Object preLogin(String userName, String terminal, String merchantCode, String currency, Long timestamp) {
        log.error("preLogin 异常!");
        return null;
    }

    @Override
    public Object retryTransfer(@RequestBody UserRetryTransferVO vo, @RequestParam(value = "merchantCode") String merchantCode) {
        log.error("retryTransfer 调用异常");
        return null;
    }

    @Override
    public void upsertUserBonus(String merchantCode, List<AcBonusPO> bonusList) {
        log.error("upsertUserBonus 调用异常" + bonusList);
    }

    @Override
    public void clearTicketsOfTask(String code) {
        log.error("!!!!!!!clearTicketsOfTask 调用异常" + code);
    }

    @Override
    public void clearTicketsOfMardigraTask(String merchantCode, Integer conditionId) {
        log.error("!!!!!!!clearTicketsOfMardigraTask 调用异常" + merchantCode);
    }

    @Override
    public Object kickoutMerchant(String merchantCode) {
        log.error("!!!!!!!kickoutMerchant 调用异常" + merchantCode);
        return  null;
    }

    @Override
    public void addChangeRecordHistory(AccountChangeHistoryFindVO accountChangeHistoryFindVO, String merchantCode) {
        log.error("!!!!!!!addChangeRecordHistory 调用异常" + merchantCode);
    }

    @Override
    public int upsertUserVip(String merchantCode, List<UserVipVO> vipList) {
        log.error("upsertUserVip 调用异常" + merchantCode + ":" + vipList);
        return 0;
    }

    @Override
    public int upsertUserDisabled(String merchantCode, List<UserVipVO> vipList) {
        log.error("upsertUserDisabled 调用异常" + merchantCode + ":" + vipList);
        return 0;
    }

    @Override
    public int upsertUserDisabled2Allow(String merchantCode, Integer disabled, List<UserVipVO> vipList) {
        log.error("upsertUserDisabled2Allow 调用异常" + merchantCode + ":" + vipList);
        return 0;
    }


    @Override
    public int updateDisabled(String merchantCode, UserVipVO userVipVO) {
        log.error("updateDisabled 调用异常" + merchantCode + ":" + userVipVO);
        return 0;
    }

    @Override
    public void updateIsVipDomain(String merchantCode, String userId, Integer isVipDomain) {
        log.error("updateIsVipDomain 调用异常" + merchantCode + ":" + isVipDomain);
    }

    @Override
    public void userAccountClearDataTask(String merchantCode, Integer num) {
        log.error("userAccountClearDataTask 调用异常" + merchantCode + ":" + num);
    }

    @Override
    public void transferRecordClearDataTask(String merchantCode, Integer num) {
        log.error("transferRecordClearDataTask 调用异常" + merchantCode + ":" + num);
    }

/*
    @Override
    public void clearStressTestData(String merchantCode, Integer num) {
        log.error("clearStressTestData error,RPC接口异常");
    }*/

    @Override
    public void executeDailyTask(String merchantCode, String dateStr) {
        log.error(merchantCode + ",executeDailyTask error,RPC接口异常");
    }

    @Override
    public void upsertUserToDisabled(String merchantCode, List<Long> uidList, Integer disabled) {
        log.error(merchantCode + ",upsertUserToDisabled error,RPC接口异常");
    }

    @Override
    public void executeSumTask(String merchantCode) {
        log.error("merchantCode:" + merchantCode);
    }


    @Override
    public int initCurrencyList() {
        log.error(",initCurrencyList error,RPC接口异常");
        return 0;
    }
}

