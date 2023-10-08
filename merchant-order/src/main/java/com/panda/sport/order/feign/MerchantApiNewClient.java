package com.panda.sport.order.feign;


import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@FeignClient(value = "merchant-api-new", fallback = RemoteHystrix.class)
public interface MerchantApiNewClient {

    @PostMapping(value = "/api/merchant/initCurrencyList")
    int initCurrencyList();
}
