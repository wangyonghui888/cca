package com.panda.multiterminalinteractivecenter.feign;


import com.panda.multiterminalinteractivecenter.feign.dto.ConfigPO;
import com.panda.multiterminalinteractivecenter.feign.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "panda-bss-backend-order", fallbackFactory = BackendApiHystrix.class)
public interface BackendApiClient {
    @GetMapping(value = "/backend/game/getSportConfig")
    Result<ConfigPO> getSportConfig();

    @PostMapping(value = "/backend/game/updateSportConfig")
    Result updateSportConfig(@RequestBody ConfigPO configPO);

}
