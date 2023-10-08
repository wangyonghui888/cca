package com.panda.sport.merchant.manage.feign;


import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.vo.MerchantInfoVo;
import com.panda.sport.merchant.common.vo.SystemSwitchVO;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "merchant-api", fallback = RemoteMerchantApiHystrix.class)
public interface MerchantApiClient {
    @GetMapping(value = "/api/merchant/kickoutMerchant")
    Object kickoutMerchant(@RequestParam(value = "merchantCode") String merchantCode);

    @PostMapping(value = "/api/merchant/findMerchantInfo")
    APIResponse<Object> findMerchantInfo(@RequestBody MerchantInfoVo merchantInfoVo);

    @GetMapping(value = "/api/user/kickoutUserInternal")
    Object kickoutUserInternal(@RequestParam(value = "uid") Long uid, @RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping("/api/merchant/clearMerchantActivityCache")
    Object clearMerchantActivityCache(@RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping("/api/user/kickoutMerchantUser")
    Object kickoutMerchantUser(@RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping("/api/user/updateMerchantUserCache")
    void updateMerchantUserCache(@RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping(value = "/api/avtivity/clearTicketsOfMardigraTask")
    void clearTicketsOfMardigraTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "conditionId") Integer conditionId);

    /**
     * @see  com.panda.sport.merchant.api.controller.MerchantController
     * @function kickoutMerchants
     */
    @GetMapping(value = "/api/merchant/kickoutMerchants")
    Object kickoutMerchants(@RequestParam(value = "merchantCodes") List<String> merchantCodes);

    @PostMapping("/api/merchant/updateMerchantChatSwitch")
    void updateMerchantChatSwitch(@RequestBody SystemSwitchVO systemSwitchVO);

    @GetMapping("/api/merchant/updateVideoDomain")
    APIResponse<Object> updateVideoDomain(@RequestParam(value = "newDomain") String newDomain, @RequestParam(value = "oldDomain") String oldDomain,
                                          @RequestParam(value = "merchantCode") String merchantCode);

    @PostMapping("/api/merchant/updateMerchantCache")
    APIResponse updateMerchantCache(@RequestBody MerchantConfig merchantConfig);
}
