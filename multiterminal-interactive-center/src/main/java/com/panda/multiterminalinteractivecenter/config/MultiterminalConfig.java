package com.panda.multiterminalinteractivecenter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class MultiterminalConfig {
    @Value("${http.proxy.switch:false}")
    private Boolean httpProxySwitch;

    @Value("${api.domain.validate:true}")
    private Boolean apiDomainValidate;

    /**17ce默认只支持ty*/
    @Value("${websocket.17ce.enable.list:ty}")
    private String thirdCheckList;

    //17ce检测启用
    @Value("${websocket.17ce.enable:true}")
    private Boolean websocketSwitch;

    //17ce DNS启用
    @Value("${websocket.17ce.dns.enable:true}")
    private Boolean dnsSwitch;

    //自检域名启用
    @Value("${self.domain.check.enable:true}")
    private Boolean selfEnable;

    //域名检测启用
    @Value("${domain.check.enable:true}")
    private Boolean checkDomainEnable;

    //17ce 延迟时间
    @Value("${websocket.17ce.delay:60000}")
    private String dalay;

    //17ce socket路径
    @Value("${com.17ce.socket.url}")
    private String webSocketUri;

    //17ce socket用户名
    @Value("${com.17ce.socket.user:jszr999@gmail.com}")
    private String webSocketUser;

    //17ce socket密码
    @Value("${com.17ce.socket.pwd:XV5VIUNH2P23I2X1}")
    private String webSocketPwd;

    //17ce 省份编号
    @Value("${com.17ce.socket.province:190,195,357}")
    private String provinceId;

    //17ce 城市编号
    @Value("${com.17ce.socket.city:212,217,590,573}")
    private String cityId;

    @Value("${ali_object_name:ali.json:null}")
    private String objectName;

    //oss test.文件路径
    @Value("${spring.json.httpPath:null}")
    private String httpPath;

    //自检域名 url路径
    @Value("${self.domain.url:null}")
    private String selfUrl;


    /**域名自检状态切换开关*/
    @Value("${domain.self.check.ty:false}")
    private Boolean tyDomainSelfCheckSwitch;
    @Value("${domain.self.check.dj:false}")
    private Boolean djdomainSelfCheckSwitch;
    @Value("${domain.self.check.cp:false}")
    private Boolean cpDomainSelfCheckSwitch;
    /**域名组阈值检查发送mongo信息*/
    @Value("${domain.group.threshold.check.ty:false}")
    private Boolean tyDomainGroupThresholdCheckSwitch;
    @Value("${domain.group.threshold.check.dj:false}")
    private Boolean djDomainGroupThresholdCheckSwitch;
    @Value("${domain.group.threshold.check.cp:false}")
    private Boolean cpDomainGroupThresholdCheckSwitch;
    /**域名自检切换开关*/
    @Value("${domain.check.toggle.ty:false}")
    private Boolean tyDomainCheckToggleSwitch;
    @Value("${domain.check.toggle.dj:false}")
    private Boolean djDomainCheckToggleSwitch;
    @Value("${domain.check.toggle.cp:false}")
    private Boolean cpDomainCheckToggleSwitch;




    //aes加密key
    @Value("${ali_aes_ksy:panda1234_1234ob}")
    private String AES_KSY;

    //自检域名阀值
    @Value("${self.domain.check.threshold:3}")
    private Integer selfCheckThreshold;

    @Value("${self.domain.check.nodes:6}")
    private Integer selfNodes;

    @Value("${websocket.17ce.threshold:3}")
    private String threshold;

    @Value("${websocket.17ce.check.nodes:6}")
    private String nodes;

    //自检开关
    @Value("${self.domain.check.switch:0}")
    private String selfCheckSwitch;

    //发送mango 开关
    @Value("${manage.send.mongo.switch:0}")
    private Integer sendMongoSwitch;

    //pc 开关
    @Value("${manage.pc.domain.switch:false}")
    private Boolean pcDomainSwitch;

    //h5 开关
    @Value("${manage.h5.domain.switch:false}")
    private Boolean h5DomainSwitch;

    //app 开关
    @Value("${manage.app.domain.switch:false}")
    private Boolean appDomainSwitch;

    //mango 群id
    @Value("${manage.mongo.targetName}")
    private String targetName;

    //mango 用户id
    @Value("${manage.mongo.userId}")
    private String userId;

    //mango 用户token
    @Value("${manage.mongo.userToken}")
    private String userToken;


}
