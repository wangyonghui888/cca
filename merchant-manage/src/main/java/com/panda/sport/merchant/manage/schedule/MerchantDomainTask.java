package com.panda.sport.merchant.manage.schedule;

import com.alibaba.druid.util.StringUtils;
import com.panda.sport.merchant.manage.service.MerchantGroupService;
import com.panda.sport.merchant.manage.service.impl.WebSocketService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName MerchantDomainTask
 * @Description TODO
 * @Author Administrator
 * @Date 2021/8/20 10:54
 */
@Slf4j
@Component
@RefreshScope
public class MerchantDomainTask {

    @Value("${self.domain.check.switch:on}")
    private String checkSwitch;

    @Autowired
    private MerchantGroupService merchantGroupService;

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 域名自动切换（根据配置时间，单位TY）
     */
    // @Scheduled(cron = "0 0/2 * * * ?")
    public void changDomain() {
        try {
            if(StringUtils.equals(checkSwitch,"off")){
                log.info("第三方自动任务切换域名不用！");
                return;
            }
            merchantGroupService.changDomainEnable();

            log.info("商户API域名自动切换开始");

            merchantGroupService.autoChangeDomain();

            log.info("商户API域名自动切换结束");
        } catch (Exception e) {
            log.error("changDomain", e);
        }
    }

    /**
     * 域名自动切换（根据配置时间，单位 DJ，TY）
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void changThirdDomain() {
        try {
            if(StringUtils.equals(checkSwitch,"off")){
                log.info("第三方自动任务切换域名不用！");
                return;
            }
            log.info("第三方自动任务切换域名跑起来了！");
            merchantGroupService.changDomainEnableThird();

            log.info("第三方商户API域名自动切换开始");

            merchantGroupService.autoChangeDomainThird();

            log.info("商户API域名自动切换结束");
        } catch (Exception e) {
            log.error("changDomain", e);
        }
    }

    /**
     * 三方API自动检测,自动切换。TY
     */
    //@Scheduled(cron = "1 2/5 * * * ?")
    //@Scheduled(cron = "*/100 * * * * ?")
    @Scheduled(cron = "0/15 */1 * * * ?")
    public void checkDomain() {
        try {
            if(StringUtils.equals(checkSwitch,"off")){
                log.info("第三方自动任务切换域名不用！");
                return;
            }
            webSocketService.checkDomain();
        } catch (Exception e) {
            log.error("checkDomain异常!", e);
        }
    }

    /**
     * 三方API自动检测,自动切换。DJ|CP
     */
    //@Scheduled(cron = "0/15 */1 * * * ?")
    public void checkDomainThird() {
        try {
            if(StringUtils.equals(checkSwitch,"off")){
                log.info("第三方自动任务切换域名不用！");
                return;
            }
            webSocketService.checkDomainThird();
        } catch (Exception e) {
            log.error("checkDomain异常!", e);
        }
    }

    /**
     * 每天定时扫描一次已使用的域名 如果没有商户在使用则改为未使用
     */
//    @Scheduled(cron = "0 0 12 * * ?")
    public void checkInDomainEnable() {
        try {
            if(StringUtils.equals(checkSwitch,"off")){
                log.info("第三方自动任务切换域名不用！");
                return;
            }
            webSocketService.checkInDomainEnable();
        } catch (Exception e) {
            log.error("checkInDomainEnable 异常!", e);
        }
    }
}
