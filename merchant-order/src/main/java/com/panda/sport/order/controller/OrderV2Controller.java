package com.panda.sport.order.controller;


import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderV2VO;
import com.panda.sport.order.config.IdempotentUtils;
import com.panda.sport.order.service.OrderV2Service;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/v2/order/user")
public class OrderV2Controller {

    private final OrderV2Service orderV2Service;

    /**
     * 注单查询 - MYSQL - 只查7天表
     */
    @PostMapping(value = "/queryTicketList")
    @AuthRequiredPermission("Merchant:Order:user:list")
    public Response<Object> queryTicketList(HttpServletRequest request, @RequestBody UserOrderV2VO vo) {
        vo.setLanguage(StringUtils.defaultString(request.getHeader("language"), Constant.LANGUAGE_CHINESE_SIMPLIFIED));
        final String traceId = vo.getTraceId();
        log.info("/v2/order/user/queryTicketList:traceId【{}】,param:【{}】", traceId, vo);
        StringBuilder sb = new StringBuilder();
        try {
            vo.setTraceId(null);
            sb.append("/v2/order/user/queryTicketList").append(vo);
            if (IdempotentUtils.repeat(sb.toString(), this.getClass())) {
                return Response.returnFail("请勿重复提交!!!");
            }
            vo.setTraceId(traceId);
            return Response.returnSuccess(orderV2Service.queryTicketList(vo));
        } catch (RuntimeException e) {
            log.error("/v2/order/user/queryTicketList:traceId【{}】,RuntimeException!", vo.getTraceId(), e);
            return Response.returnFail(e.getMessage());
        } catch (Exception e) {
            log.error("/v2/order/user/queryTicketList:traceId【{}】,Exception!", vo.getTraceId(), e);
            return Response.returnFail("查询UTC8自然日期注单列表异常");
        } finally {
            IdempotentUtils.remove(sb.toString(), this.getClass());
        }
    }


    /**
     * 通过商户编码查询用户名称，uid
     */
    @GetMapping(value = "/queryUserIdList")
    public List<Map<String, Object>> queryUserIdList(@RequestParam(value = "merchantCode") String merchantCode) {
        log.info("order/user/queryUserIdList，通过商户编码查询用户名称，uid:" + merchantCode);
        return orderV2Service.queryUserIdList(merchantCode);
    }


}
