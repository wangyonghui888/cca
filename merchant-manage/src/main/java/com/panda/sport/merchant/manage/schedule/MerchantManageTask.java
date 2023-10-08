package com.panda.sport.merchant.manage.schedule;

import com.panda.sport.backup.mapper.BackupTransferRecordErrorMapper;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.TransferRecordVO;
import com.panda.sport.merchant.manage.service.IMongoService;
import com.panda.sport.merchant.manage.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class MerchantManageTask {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private IMongoService mongoService;
    @Autowired
    private BackupTransferRecordErrorMapper errorMapper;

    @Value("${manage.mongo.tranfer.targetname}")
    private String targetname;

    @Value("${manage.mongo.tranfer.userid}")
    private String userId;

    @Value("${manage.mongo.tranfer.usertoken}")
    private String userToken;

    //@Scheduled(cron = "0 0/5 0/1 * * ? ")
    //@Scheduled(cron = "0 0 12 * * ? ")
    public void execute() {
        log.info("开始执行变更商户状态!");
        try {
            merchantService.updateMerchantStatus();
            log.info("执行变更商户状态end!");
        } catch (Exception e) {
            log.error("变更商户状态异常!", e);
        }
    }


    @Scheduled(cron = "0 10/30 * * * ? ")
    public void tranferErrorExecute() {
        log.info("开始执行异常加款派彩通知!");
        try {
            List<Map<String, Object>> list = errorMapper.countTransferGroup();
            if (CollectionUtils.isNotEmpty(list)) {
                log.info("开始执行异常加款派彩通知!list:" + list.size());
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_YYYY_MM_DD_HH_MM_SS);
                String str = sdf.format(new Date());
                for (Map<String, Object> map : list) {
                    List<TransferRecordVO> transferRecordVOS = errorMapper.findRecordError(String.valueOf(map.get("merchantCode")));
                    StringBuilder text = new StringBuilder("【Alert】 " + str + " \n【Merchant】 " + map.get("merchantCode") + " \n【Payout】 " + map.get("sumNum") + " \n【Details】");
                    for (TransferRecordVO transferRecordVO : transferRecordVOS) {
                        text.append(" ").append(transferRecordVO.getTransferId()).append(" ").append(transferRecordVO.getMag()).append("\n");
                    }
                    mongoService.send(text.toString(), targetname, userId, userToken);
                    log.info("开始执行异常加款派彩通知!text:" + text);
                }
            }
            log.info("开始执行异常加款派彩通知end!");
        } catch (Exception e) {
            log.error("执行异常加款派彩通知异常!", e);
        }
    }
}