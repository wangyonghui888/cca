package com.panda.sport.order.controller;

import cn.hutool.core.date.DateUtil;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.order.service.OrderSettleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/order/schedule")
@Slf4j
public class ScheduleController {
    @Autowired
    private OrderSettleService tSettleService;

/*
    @Autowired
    private VipOrderSettleTask vipOrderSettleTask;
*/

    @GetMapping(value = "/execute")
    public Response<Object> execute(HttpServletRequest request, @RequestParam(value = "date") String date,
                                    @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("/task/schedule/execute:" + date + ",merchantCode=" + merchantCode);
        try {
            Date executeDate = DateUtils.parseDate(date, "yyyy-MM-dd");
            long startTime = (DateUtil.beginOfDay(executeDate)).getTime();
            long endTime = (DateUtil.endOfDay(executeDate)).getTime();
            tSettleService.uploadSettleOrder(startTime, endTime, merchantCode);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("手动执行上传文件服务器任务失败", e);
            return Response.returnFail("手动执行上传文件服务器任务失败!");
        }
    }

    @GetMapping(value = "/executePeriod")
    public Response<Object> executePeriod(HttpServletRequest request, @RequestParam(value = "startDate") String startDate,
                                          @RequestParam(value = "endDate") String endDate, @RequestParam(value = "merchantCode", required = false) String merchantCode) {
        log.info("/task/schedule/executePeriod:" + startDate + "," + endDate + ",merchantCode=" + merchantCode);
        try {
            if (StringUtils.isAnyEmpty(startDate, endDate)) {
                return Response.returnFail("参数错误");
            }
            Date start = DateUtils.parseDate(startDate, "yyyy-MM-dd");
            Date end = DateUtils.parseDate(endDate, "yyyy-MM-dd");
            if (start.after(end)) {
                return Response.returnFail("参数错误");
            }
            long diffDays = (end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000);
            log.info("diffDays:" + diffDays);
            for (int i = 0; i < diffDays; i++) {
                Date tempDate = DateUtils.addDays(start, i);
                String dateStr = DateFormatUtils.format(tempDate, "yyyy-MM-dd");
                log.info("执行日期:" + dateStr);
                Date executeDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
                long startTime = (DateUtil.beginOfDay(executeDate)).getTime();
                long endTime = (DateUtil.endOfDay(executeDate)).getTime();
                tSettleService.uploadSettleOrder(startTime, endTime, merchantCode);
            }
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("手动执行上传文件服务器任务失败", e);
            return Response.returnFail("手动执行上传文件服务器任务失败!");
        }
    }
/*
    @GetMapping(value = "/vipExportExcel")
    public Response<Object> vipExportExcel(@RequestParam(value = "params", required = false,defaultValue = "")String params) {
        log.info("/task/schedule/vipExportExcel");
        try {
            vipOrderSettleTask.execute(params);
            return Response.returnSuccess();
        } catch (Exception e) {

            return Response.returnFail("手动执行上传vipExportExcel文件服务器任务失败!");
        }
    }*/
}
