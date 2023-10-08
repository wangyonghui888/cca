package com.panda.sport.order.controller;


import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoReqVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.order.service.OrderTimesSettleInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 订单多次结算账变 前端控制器
 *
 * @author amos
 * @since 2022-05-22
 */
@Slf4j
@RestController
@RequestMapping("/order/orderTimeSettle")
public class OrderTimesSettleInfoController {

    @Resource
    private OrderTimesSettleInfoService orderTimesSettleInfoService;

    @PostMapping(value = "/pageList")
    @ApiOperation(value = "/pageList", notes = "二次结算查询")
    public Response addESportsNotice(HttpServletRequest request, @RequestBody @Valid OrderTimesSettleInfoReqVO pageReqVO){

        log.info("二次结算查询分页统计:{}",JSON.toJSONString(pageReqVO));
        return orderTimesSettleInfoService.pageList(pageReqVO);
    }

    @RequestMapping(value = "/export")
    @ApiOperation(value = "/export", notes = "二次结算导出")
    public Response orderTimeSettleExport(HttpServletRequest request,
                                          @RequestHeader(defaultValue = Constant.LANGUAGE_CHINESE_SIMPLIFIED,name = "language") String language,
                                          @RequestBody @Valid OrderTimesSettleInfoReqVO pageReqVO) {

        log.info("orderTimeSettleExport param = {}", JSON.toJSONString(pageReqVO));
        String merchantName = request.getHeader("merchantName");
        // 查询当前操作人
        return Response.returnSuccess(orderTimesSettleInfoService.orderTimeSettleExport(language,pageReqVO,merchantName));
    }

    @RequestMapping(value = "/exportInfomationOf2TimesOrder")
    public void exportInfomationOf2TimesOrder(HttpServletResponse response, String ymd) {
        orderTimesSettleInfoService.exportInfomationOf2TimesOrder(response, ymd);
    }
}
