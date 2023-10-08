package com.panda.sport.merchant.manage.controller;

import com.panda.sport.merchant.common.enums.ResponseEnum;

import com.panda.sport.merchant.common.utils.StringUtil;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.DataSyncVO;
import com.panda.sport.merchant.manage.service.DataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/manage/dataSync")
@Slf4j
public class DataSyncController {

    @Autowired
    private DataSyncService dataSyncService;

    /**
     * 对账单数据同步
     */
    @PostMapping("/callExecuteFinance")
    public Response callExecuteFinance(HttpServletRequest request,@RequestBody DataSyncVO dataSyncVO) {
        try {
            log.info("/manage/dataSync/callExecuteFinance:" + dataSyncVO.getStartDate() + '，' + dataSyncVO.getEndDate());
            if (StringUtil.isBlankOrNull(dataSyncVO.getStartDate()) && StringUtil.isBlankOrNull( dataSyncVO.getEndDate())) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            return dataSyncService.callExecuteFinance(request,dataSyncVO.getStartDate(), dataSyncVO.getEndDate());
        } catch (Exception e) {
            log.error("DataSyncController.callExecuteFinance,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 用户投注统计数据同步
     */
    @PostMapping("/callExecuteUserBet")
    public Response callExecuteUserBet(HttpServletRequest request,@RequestBody DataSyncVO dataSyncVO) {
        try {
            log.info("/manage/dataSync/callExecuteUserBet:" + dataSyncVO.getStartDate() + '，' + dataSyncVO.getEndDate());
            if (StringUtil.isBlankOrNull(dataSyncVO.getStartDate()) && StringUtil.isBlankOrNull(dataSyncVO.getEndDate())) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            return dataSyncService.callExecuteUserBet(request,dataSyncVO.getStartDate(),dataSyncVO.getEndDate());
        } catch (Exception e) {
            log.error("DataSyncController.callExecuteUserBet,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 赛事投注统计数据同步
     */
    @PostMapping("/callExecuteMatchBet")
    public Response callExecuteMatchBet(HttpServletRequest request,@RequestBody DataSyncVO dataSyncVO) {
        try {
            log.info("/manage/dataSync/callExecuteMatchBet:" + dataSyncVO.getStartDate() + '，' + dataSyncVO.getEndDate());
            if (StringUtil.isBlankOrNull(dataSyncVO.getStartDate()) && StringUtil.isBlankOrNull(dataSyncVO.getEndDate())) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            return dataSyncService.callExecuteMatchBet(request, dataSyncVO.getStartDate(),dataSyncVO.getEndDate());
        } catch (Exception e) {
            log.error("DataSyncController.callExecuteMatchBet,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

}
