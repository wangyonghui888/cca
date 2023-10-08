package com.panda.multiterminalinteractivecenter.controller;

import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogQueryVO;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Z9-velpro
 */
@Slf4j
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

    @Autowired
    private MerchantLogService merchantLogService;

    @PostMapping("/queryLog")
    public Response queryLog(@RequestBody MerchantLogQueryVO queryVO) {
        return merchantLogService.queryLog(queryVO);
    }

    /**
     * 查询栏目
     *
     * @return
     */
    @GetMapping("/getLogPages")
    public Response getLogPages() {
        return Response.returnSuccess(merchantLogService.loadLogPages());
    }
    /**
     * 查询日志操作类型
     *
     * @return
     */
    @GetMapping("/getLogTypes")
    public Response getLogTypes() {
        return Response.returnSuccess(merchantLogService.loadLogType());
    }

}
