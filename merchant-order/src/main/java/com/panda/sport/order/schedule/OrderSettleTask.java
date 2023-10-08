package com.panda.sport.order.schedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.order.service.OrderSettleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@JobHandler(value = "OrderSettleTask")
public class OrderSettleTask extends IJobHandler {
    @Autowired
    private OrderSettleService tSettleService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        if (StringUtil.isNotBlank(param)) {
            //   分割字符串
            String[] split = param.split(",");
            // 时间
            String date = split[0];

            if (split.length == 1) {
                excuteWithParam(date, null);
            } else {
                String merchantCode = split[1];
                excuteWithParam(date, merchantCode);
            }
        } else {
            excuteNoParam();
        }
        return SUCCESS;
    }

    private void excuteNoParam() {
        try {
            long start = System.currentTimeMillis();
            log.info("OrderSettleTask.UPLOAD_开始执行注单上传服务器任务:" + start);

            Date now = new Date();
            DateTime beforeYesterday = DateUtil.offsetDay(now, -2);
            long startTime = DateUtil.beginOfDay(beforeYesterday).getTime();
            long endTime = DateUtil.endOfDay(beforeYesterday).getTime();
            log.info("OrderSettleTask.UPLOAD_three days ago startTime=" + startTime + ",endTime=" + endTime);
            tSettleService.uploadSettleOrder(startTime, endTime, null);


            DateTime sixdaysBefore = DateUtil.offsetDay(now, -6);
            startTime = DateUtil.beginOfDay(sixdaysBefore).getTime();
            endTime = DateUtil.endOfDay(sixdaysBefore).getTime();
            log.info("OrderSettleTask.UPLOAD_five days ago startTime=" + startTime + ",endTime=" + endTime);
            tSettleService.uploadSettleOrder(startTime, endTime, null);
            log.info("OrderSettleTask.UPLOAD_注单上传服务器任务执行结束:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("OrderSettleTask.UPLOAD_执行注单上传服务器ERROR!", e);
        }
    }

    private void excuteWithParam(String date, String merchantCode) {
        try {

            long start = System.currentTimeMillis();
            log.info("UPLOAD_带参开始执行注单上传服务器任务:{} ，参数:  时间:{},商户编号:{}", start, date, merchantCode);

            // 开始时间
            DateTime startDateTime = DateUtil.parse(date);

            // 结束时间
            DateTime endDateTime = DateUtil.offsetDay(startDateTime, 1);

            long startTime = startDateTime.getTime();
            long endTime = endDateTime.getTime();

            log.info("UPLOAD startTime=" + startTime + ",endTime=" + endTime);

            tSettleService.uploadSettleOrder(startTime, endTime, merchantCode);

        } catch (Exception e) {
            log.error("UPLOAD_执行注单上传服务器ERROR!", e);
        }
    }


}