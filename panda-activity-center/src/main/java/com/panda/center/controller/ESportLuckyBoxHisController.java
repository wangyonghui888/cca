package com.panda.center.controller;

import com.panda.center.param.LuckyBoxParam;
import com.panda.center.result.Response;
import com.panda.center.service.IMerchantLogService;
import com.panda.center.service.IOlympicLuckyboxRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/26 17:08:32
 */
@Slf4j
@RestController
@RequestMapping("/manage/dj/box")
public class ESportLuckyBoxHisController {

    @Resource
    private IOlympicLuckyboxRecordsService luckyboxRecordsService;
    @Resource
    private IMerchantLogService merchantLogService;

    /**
     * 盲盒领取记录查询
     *
     * @param luckyBoxParam com.panda.center.param.LuckyBoxParam
     * @return com.panda.center.result.Response
     */
    @PostMapping("/queryLuckyBoxHistory")
    public Response<?> queryLuckyBoxHistory(@RequestBody LuckyBoxParam luckyBoxParam) {
        return luckyboxRecordsService.queryLuckyBoxHistory(luckyBoxParam);
    }

    @PostMapping("/queryLuckyBoxHistoryExcel")
    public Response<?> queryLuckyBoxHistoryExcel(@RequestBody LuckyBoxParam luckyBoxParam, HttpServletResponse response) {
        return luckyboxRecordsService.queryLuckyBoxHistoryExcel(luckyBoxParam, response);
    }
}
