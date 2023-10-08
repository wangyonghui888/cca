package com.panda.multiterminalinteractivecenter.feign;


import com.panda.multiterminalinteractivecenter.feign.dto.ConfigPO;
import com.panda.multiterminalinteractivecenter.feign.dto.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BackendApiHystrix implements FallbackFactory<BackendApiClient>   {

    @Override
    public BackendApiClient create(Throwable cause) {

        log.error("BssRemoteHystrix error",cause);
        return new PandBssFallBack() {
            @Override
            public Result getSportConfig() {
                log.error("获取配置,RPC接口异常");
                return null;
            }

            @Override
            public Result updateSportConfig(ConfigPO configPO) {
                log.error("更新,RPC接口异常");
                return null;
            }
        };
    }
}

