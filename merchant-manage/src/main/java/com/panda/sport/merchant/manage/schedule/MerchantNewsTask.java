package com.panda.sport.merchant.manage.schedule;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.mapper.MerchantNewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author YK
 * @Description: 商户的消息的发送
 * @date 2020/3/14 13:39
 */
@Component
@Slf4j
public class MerchantNewsTask {

    @Resource
    private MerchantMapper merchantMapper;

    @Resource
    private MerchantNewsMapper merchantNewsMapper;

    /**
     * 商户KEY 30后 即将到期
     * 每3天发送一次
     */
    @Scheduled(cron = "0 0 10 */3 * ?")
    public void keyWillExpire() {
        log.info("商户key失效JOB即将到期,发送公告开始");
        String willDay = DateUtil.format(DateUtil.offsetDay(new Date(), 30), "yyyy-MM-dd");
        String isReason = "will";
        List<TMerchantKey> tMerchantKeyList = tMerchantKeyList(willDay, isReason);
        if (tMerchantKeyList.size() > 0) {
            sendNews(tMerchantKeyList, "willToday");
        }
    }

    /**
     * 商户KEY今天过期
     */
    @Scheduled(cron = "0 0 11 * * ?")
    public void keyTodayExpire() {
        log.info("商户key失效JOB今天过期,发送公告开始");

        String today = DateUtil.today();
        String isReason = "eq";
        List<TMerchantKey> tMerchantKeyList = tMerchantKeyList(today, isReason);
        if (tMerchantKeyList.size() > 0) {
            sendNews(tMerchantKeyList, "eqToday");
        }
    }

    /**
     * 商户KEY对比今天已经过期
     */
    @Scheduled(cron = "0 0 12 * * ?")
    //@Scheduled(cron = "*/10 * * * * ?")
    public void keyAlreadyExpire() {
        log.info("商户key失效JOB已经过期,发送公告开始");
        String today = DateUtil.today();
        String isReason = "lt";
        List<TMerchantKey> tMerchantKeyList = tMerchantKeyList(today, isReason);
        if (tMerchantKeyList.size() > 0) {
            sendNews(tMerchantKeyList, "ltToday");
        }
    }

    /**
     * 查询条件
     *
     * @param dateTime
     * @param isReason
     * @return
     */
    public List<TMerchantKey> tMerchantKeyList(String dateTime, String isReason) {


        String startTime = null;
        String endTime = null;
        switch (isReason) {
            case "eq":
                endTime = dateTime;
                break;
            case "lt":
                endTime = dateTime;
                break;
            case "will":
                String today = DateUtil.today();
                startTime = today;
                endTime = dateTime;
                break;
        }
        List<MerchantPO> tMerchantKeyList = merchantMapper.queryMerchantKeyList(isReason,startTime,endTime);

        List<TMerchantKey> tMerchantKeys = new ArrayList<>();
        if (tMerchantKeyList.size() > 0) {
            for (MerchantPO tMerchantKey : tMerchantKeyList) {
                    // 判断下 商户状态有效的状态才可以发消息
                    if (tMerchantKey.getStatus() == 1) {
                        TMerchantKey t = new TMerchantKey();
                        BeanUtils.copyProperties(tMerchantKey, t);
                        tMerchantKeys.add(t);
                    }
                }
            }
        return tMerchantKeys;
    }

    /**
     * 发送消息
     *
     * @param tMerchantKeyList
     */
    public void sendNews(List<TMerchantKey> tMerchantKeyList, String islast) {

        List<String> merchantCodeList = new ArrayList<>();
        for (TMerchantKey tMerchantKey : tMerchantKeyList) {
            String endTime = tMerchantKey.getEndTime();
            String title = "体育的证书已经到期";
            String context = "体育的证书已经过期了,点击<a ref='update'>此处</a>此处更新证书";
            if ("eqToday".equals(islast)) {
                title = tMerchantKey.getMerchantName() + "的证书今天过期";
                context = tMerchantKey.getMerchantName() + ",体育的证书今天过期了,点击<a ref='update'>此处</a>更新证书。";
            } else if ("ltToday".equals(islast)) {
                String today = DateUtil.today();
                long betweenDay = DateUtil.between(DateUtil.parse(endTime), DateUtil.parse(today), DateUnit.DAY);
                title = tMerchantKey.getMerchantName() + "的证书已经过期了" + betweenDay + "天";
                context = tMerchantKey.getMerchantName() + ",体育的证书已经过期了" + betweenDay + "天,点击<a ref='update'>此处</a>更新证书。";
            } else if ("willToday".equals(islast)) {
                String today = DateUtil.today();
                long betweenDay = DateUtil.between(DateUtil.parse(today), DateUtil.parse(endTime), DateUnit.DAY);
                title = tMerchantKey.getMerchantName() + "的证书还有" + betweenDay + "天到期";
                context = tMerchantKey.getMerchantName() + ",体育的证书还有" + betweenDay + "天要过期了,点击<a ref='update'>此处</a>更新证书。";
            }
            MerchantNews merchantNewsPo = new MerchantNews();
            merchantNewsPo.setTitle(title).setContext(context)
                    .setMerchantCode(tMerchantKey.getMerchantCode()).setMerchantName(tMerchantKey.getMerchantName()).setIsMerchant(1)
                    .setSendTime(System.currentTimeMillis()).setCreateTime(System.currentTimeMillis());
            merchantNewsMapper.insert(merchantNewsPo);
            merchantCodeList.add(tMerchantKey.getMerchantCode());
        }
    }
}
