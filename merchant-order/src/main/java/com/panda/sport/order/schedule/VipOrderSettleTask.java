/*
package com.panda.sport.order.schedule;

import cn.hutool.core.date.DateUtil;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.order.service.OrderSettleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@JobHandler(value = "vipOrderSettleTask")
public class VipOrderSettleTask extends IJobHandler {

    @Autowired
    private OrderSettleService tSettleService;


    */
/**
     * @param param  格式为时间+商户编码   例如：202205,237593
     * @return
     * @throws Exception
     *//*

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        try {
            long start = System.currentTimeMillis();
            log.info("UPLOAD_开始执行vip注单上传服务器任务:" + start);

            String[] split = param.split(",");

            // 时间
            String date = split[0] ;

            String merchantCode = null;
            if (split.length > 1){
                 merchantCode = split[1];
            }

            Date now  = new Date();

            if (StringUtil.isNotBlank(date)){
                now =DateUtil.offsetMonth(DateUtil.parse(date+"01"),1);
            }

            Date now1 = DateUtil.offsetMonth(now, -1);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01 00:00:00");
            String dateString = formatter.format(now1);

            long startTime = DateUtil.parse(dateString).getTime();
            // 结束时间加一个月
            long endTime = DateUtil.offsetMonth(new Date(startTime),1).getTime();

            if (StringUtil.isNotBlank(merchantCode)){
                log.info("uploadSettleOrderVip.VIP_UPLOAD_three days ago startTime=" + startTime + ",endTime=" + endTime +",merchantCode="+ merchantCode);
                // 注单上传到vip
                tSettleService.uploadSettleOrderVip(startTime, endTime, merchantCode);
                log.info("VIP_UPLOAD_vip注单上传服务器任务执行结束uploadSettleOrderVip:" + (System.currentTimeMillis() - start) +",merchantCode="+ merchantCode);
            }else{
                log.info("uploadSettleOrderVip.VIP_UPLOAD_three days ago startTime=" + startTime + ",endTime=" + endTime);
                // 注单上传到vip
                tSettleService.uploadSettleOrderVip(startTime, endTime, null);
                log.info("VIP_UPLOAD_vip注单上传服务器任务执行结束uploadSettleOrderVip:" + (System.currentTimeMillis() - start));

                log.info("uploadSettleOrderAllVip.VIP_UPLOAD_three days ago startTime=" + startTime + ",endTime=" + endTime);
                // 计算所有的vip
                tSettleService.uploadSettleOrderAllVip(startTime, endTime, null);
                log.info("VIP_UPLOAD_vip注单上传服务器任务执行结束uploadSettleOrderAllVip:" + (System.currentTimeMillis() - start));
            }
        } catch (Exception e) {
            log.error("VIP_UPLOAD_执行vip-admin注单上传服务器ERROR!", e);
        }
        return SUCCESS;
    }
}
*/
