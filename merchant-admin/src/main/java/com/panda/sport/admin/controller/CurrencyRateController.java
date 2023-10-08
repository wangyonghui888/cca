package com.panda.sport.admin.controller;

import com.panda.sport.admin.feign.BssBackendClient;
import com.panda.sport.admin.vo.RateVo;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 汇率的列表
 */
@RestController
@RequestMapping("/admin/rate")
@Slf4j
public class CurrencyRateController {


    @Autowired
    private BssBackendClient bssOrderClient;

    /**
     * 查询列表
     * @param rateVo
     * @return
     */
    @PostMapping("/queryRateList")
    public Object queryRateList(@RequestBody RateVo rateVo) {

        try {
            return bssOrderClient.queryRateList(rateVo);
        } catch (Exception e) {
            log.error("汇率查询列表报错:{}",e);
            return Response.returnFail("汇率查询失败");
        }
    }

    /**
     * 查询汇率log列表
     * @param rateVo
     * @return
     */
    @PostMapping("/queryRateLogList")
    public Object queryRateLogList(@RequestBody RateVo rateVo) {

        try {
            return bssOrderClient.queryRateLogList(rateVo);
        } catch (Exception e) {
            log.error("汇率查询列表报错:{}",e);
            return Response.returnFail("汇率查询失败");
        }
    }
}
