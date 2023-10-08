package com.panda.sport.merchant.api.feign;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.dto.CreditConfigDto;
import com.panda.sport.merchant.common.dto.CreditConfigSaveDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.dto.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
public class MultitermialRemoteHystrix implements FallbackFactory<MultiterminalClient> {


    @Override
    public MultiterminalClient create(Throwable cause) {
        return new MultitermialFallBack() {
            @Override
            public List<?> getDomainByMerchantAndArea(Long merchantgroupid, String domaingroupcode) {
                log.error("getDomainByMerchantAndArea RPC调用失败!",cause);
                return null;
            }

            @Override
            public APIResponse getNewH5PcDomain() {
                log.error("getNewH5PcDomain RPC调用失败!", cause);
                return null;
            }
        };
    }
}
