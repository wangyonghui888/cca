package com.panda.multiterminalinteractivecenter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author istio
 * @Description Nacos配置17ce
 * @createTime 2022年02月22日 13:20:00
 */
@RefreshScope
@Configuration
@Component
@Data
public class NacosThirdEnableConfig {
    /**
     * 17ce开关
     * true  false
     */

    @Value("${websocket.17ce.enable:null}")
    public  String thirdEnable;

    @Value("${websocket.17ce.threshold:2}")
    public  Integer threshold;

    @Value("${self.domain.check.threshold:2}")
    public  Integer selfThreshold;

    @Value("${self.domain.check.nodes:11}")
    public  Integer selfNodes;

    @Value("${websocket.17ce.check.nodes:11}")
    public  Integer nodes;

}
