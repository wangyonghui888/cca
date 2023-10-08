package com.panda.sport.order.service.expot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/6/2 14:57
 */
@Data
@Component
@RefreshScope
public class FtpProperties {
    @Value("${spring.ftp.host:null}")
    private String host;

    @Value("${spring.ftp.port:null}")
    private String port;

    @Value("${spring.ftp.username:null}")
    private String username;

    @Value("${spring.ftp.password:null}")
    private String password;

    @Value("${spring.ftp.basePath:null}")
    private String basePath;

    @Value("${spring.ftp.httpPath:null}")
    private String httpPath;
}
