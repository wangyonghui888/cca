package com.panda.sport.merchant.manage.service.impl;

import com.panda.sport.merchant.common.bo.DiffNoticeBO;
import com.panda.sport.merchant.manage.service.IMongoService;
import com.panda.sport.merchant.manage.service.SendMangoNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@RefreshScope
@Service
public class SendMangoNoticeServiceImpl implements SendMangoNoticeService {
    @Autowired
    private IMongoService mongoService;

    @Value("${manage.mongo.tranfer.targetname}")
    private String targetname;

    @Value("${manage.mongo.tranfer.userid}")
    private String userId;

    @Value("${manage.mongo.tranfer.usertoken}")
    private String userToken;

    @Override
    public void sendDiffNotice(List<DiffNoticeBO> diffList,String title) {
        try {
            for (DiffNoticeBO diffNoticeBO : diffList) {
                // 千位分隔符，保留两位小数，不足补零
                String correctBetAmount = new DecimalFormat("#,###.00").format(diffNoticeBO.getCorrectBetAmountSum());
                String correctProfitAmount = new DecimalFormat("#,###.00").format(diffNoticeBO.getCorrectProfitAmountSum());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(title).append("\n")
                        .append("异常商户：").append(diffNoticeBO.getMerchantName()).append("\n")
                        .append("异常日期：").append(diffNoticeBO.getFinanceDate()).append("\n")
                        .append("正确注单数：").append(diffNoticeBO.getCorrectOrderSum()).append("单\n")
                        .append("正确投注用户数：").append(diffNoticeBO.getCorrectBetUserSum()).append("人\n")
                        .append("正确投注金额：").append(correctBetAmount)
                        .append("RMB\n")
                        .append("正确盈利金额：").append(correctProfitAmount)
                        .append("RMB\n")
                        .append("请及时处理！")
                ;
                mongoService.send(stringBuilder.toString(),targetname, userId, userToken);
            }
        } catch (Exception e) {
            log.error("{}异常,原因：",title, e);
        }
    }
}
