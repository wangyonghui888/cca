package com.panda.sport.order.schedule;

import com.panda.sport.backup.mapper.BackupOrderMapper;
import com.panda.sport.merchant.common.dto.InfomationOf2TimesOrderDTO;
import com.panda.sport.merchant.common.po.bss.OrderTimesSettleInfoPO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 二次结算负帐数据统计 amos
 */
@Slf4j
@Component
@JobHandler(value = "infomationOf2TimesOrderTask")
public class InfomationOf2TimesOrderTask extends IJobHandler {

    @Autowired
    private BackupOrderMapper backupOrderMapper;

    /**
     * 执行主体
     * @param s "1,-1"  start : 开始天数    end: 结束天数
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            log.info("infomationOf2TimesOrderTask:{}", s);
            int start = 1, end = 0;
            if (StringUtils.isNotEmpty(s)) {
                String[] ss = s.split(",");
                start = Integer.parseInt(ss[0]);
                end = Integer.parseInt(ss[1]);
            }
            List<InfomationOf2TimesOrderDTO> infomationOf2TimesOrderDTOList = backupOrderMapper.select2TimesOrderInfo(start, end);
            //数据持久化到数据库
            List<OrderTimesSettleInfoPO> tOrderTimesSettleInfoList = new ArrayList<>();
            int count = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (InfomationOf2TimesOrderDTO infomationOf2TimesOrderDTO : infomationOf2TimesOrderDTOList) {
                count++;
                OrderTimesSettleInfoPO tOrderTimesSettleInfo = new OrderTimesSettleInfoPO();
                BeanUtils.copyProperties(infomationOf2TimesOrderDTO, tOrderTimesSettleInfo);
                if (infomationOf2TimesOrderDTO.getDiffNegativeAmount() != null) {
                    tOrderTimesSettleInfo.setNegativeAmount(new Double(Double.parseDouble(infomationOf2TimesOrderDTO.getDiffNegativeAmount()) * 100).longValue());
                } else {
                    tOrderTimesSettleInfo.setNegativeAmount(0L);
                }
                if (infomationOf2TimesOrderDTO.getAmount() != null) {
                    tOrderTimesSettleInfo.setAmount(new Double(Double.parseDouble(infomationOf2TimesOrderDTO.getAmount()) * 100).longValue());
                } else {
                    tOrderTimesSettleInfo.setAmount(0L);
                }
                if (infomationOf2TimesOrderDTO.getLastChangeAmount() != null) {
                    tOrderTimesSettleInfo.setLastChangeAmount(new Double(Double.parseDouble(infomationOf2TimesOrderDTO.getLastChangeAmount()) * 100).longValue());
                } else {
                    tOrderTimesSettleInfo.setLastChangeAmount(0L);
                }
                if (infomationOf2TimesOrderDTO.getLastAfterChangeAmount() != null) {
                    tOrderTimesSettleInfo.setLastAfterChangeAmount(new Double(Double.parseDouble(infomationOf2TimesOrderDTO.getLastAfterChangeAmount()) * 100).longValue());
                } else {
                    tOrderTimesSettleInfo.setLastAfterChangeAmount(0L);
                }
                if (infomationOf2TimesOrderDTO.getFirstChangeAmount() != null) {
                    tOrderTimesSettleInfo.setFirstChangeAmount(new Double(Double.parseDouble(infomationOf2TimesOrderDTO.getFirstChangeAmount()) * 100).longValue());
                } else {
                    tOrderTimesSettleInfo.setFirstChangeAmount(0L);
                }
                if (infomationOf2TimesOrderDTO.getFirstChangeBeforeAmount() != null) {
                    tOrderTimesSettleInfo.setFirstChangeBeforeAmount(new Double(Double.parseDouble(infomationOf2TimesOrderDTO.getFirstChangeBeforeAmount()) * 100).longValue());
                } else {
                    tOrderTimesSettleInfo.setFirstChangeBeforeAmount(0L);
                }
                if (infomationOf2TimesOrderDTO.getBeginTime() != null) {
                    tOrderTimesSettleInfo.setBeginTime(simpleDateFormat.parse(infomationOf2TimesOrderDTO.getBeginTime()).getTime());
                } else {
                    tOrderTimesSettleInfo.setBeginTime(0L);
                }
                if (infomationOf2TimesOrderDTO.getLastChangeTime() != null) {
                    tOrderTimesSettleInfo.setLastChangeTime(simpleDateFormat.parse(infomationOf2TimesOrderDTO.getLastChangeTime()).getTime());
                } else {
                    tOrderTimesSettleInfo.setLastChangeTime(0L);
                }
                if (infomationOf2TimesOrderDTO.getFirstChangeTime() != null) {
                    tOrderTimesSettleInfo.setFirstChangeTime(simpleDateFormat.parse(infomationOf2TimesOrderDTO.getFirstChangeTime()).getTime());
                } else {
                    tOrderTimesSettleInfo.setFirstChangeTime(0L);
                }
                tOrderTimesSettleInfo.setRemark(infomationOf2TimesOrderDTO.getChangeReason());
                if (infomationOf2TimesOrderDTO.getUid() != null) {
                    tOrderTimesSettleInfo.setUid(Long.parseLong(infomationOf2TimesOrderDTO.getUid()));
                } else {
                    tOrderTimesSettleInfo.setUid(0L);
                }
                if (infomationOf2TimesOrderDTO.getMatchId() != null) {
                    tOrderTimesSettleInfo.setMatchId(Long.parseLong(infomationOf2TimesOrderDTO.getMatchId()));
                } else {
                    tOrderTimesSettleInfo.setMatchId(0L);
                }
                tOrderTimesSettleInfo.setCreateTime(System.currentTimeMillis());
                tOrderTimesSettleInfo.setModifyTime(System.currentTimeMillis());
                tOrderTimesSettleInfoList.add(tOrderTimesSettleInfo);
                if (count % 10 == 0) {
                    backupOrderMapper.batchInfomationOf2TimesOrderDTO(tOrderTimesSettleInfoList);
                    tOrderTimesSettleInfoList = new ArrayList<>();
                }
            }
            if (CollectionUtils.isNotEmpty(tOrderTimesSettleInfoList)) {
                backupOrderMapper.batchInfomationOf2TimesOrderDTO(tOrderTimesSettleInfoList);
            }
        }catch (Exception e){
            log.error("infomationOf2TimesOrderTask: args:" + s ,e);
            throw e;
        }
        return ReturnT.SUCCESS;
    }
}