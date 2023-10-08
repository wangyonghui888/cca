package com.panda.sport.order.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "panda-bss-backend-order", fallback = RemoteHystrix.class)
public interface BssBackendClient {

    @PostMapping(value = "/v1/transfer/retryTransfer")
    Object retryTransfer(@RequestBody UserRetryTransferVO vo);

    @GetMapping(value = "/v1/betOrder/getSportIdByMatchManageId")
    Object getSportIdByMatchManageId(@RequestParam(value = "matchManageId") String matchManageId);

     @GetMapping(value = "/v1/betOrder/tPayoutLimit/listAlbPayoutLimit")
     Object queryListAlbPayoutLimit(@RequestParam(value = "uid") Long uid);
}
