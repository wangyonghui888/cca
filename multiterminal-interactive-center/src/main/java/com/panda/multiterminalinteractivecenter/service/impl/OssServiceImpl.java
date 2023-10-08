package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.multiterminalinteractivecenter.cashe.LocalCacheService;
import com.panda.multiterminalinteractivecenter.config.MultiterminalConfig;
import com.panda.multiterminalinteractivecenter.constant.DomainConstants;
import com.panda.multiterminalinteractivecenter.entity.Oss;
import com.panda.multiterminalinteractivecenter.enums.DomainTypeEnum;
import com.panda.multiterminalinteractivecenter.service.OssService;
import com.panda.multiterminalinteractivecenter.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.enums.ReadyState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * Oss 服务实现类
 * </p>
 *
 * @author ifan
 * @since 2022-08-29
 */
@Service
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OssServiceImpl implements OssService {

    private final MultiterminalConfig config;

    private final FtpProperties ftpProperties;

    private final OssDomainProcessor ossDomainProcessor;

    @Autowired
    private OssDomainWebSocketServiceImpl ossDomainWebSocketService;

    @Override
    public boolean ossUpload(JSONObject obj) {
        try {
            // aliOssUpload(obj);
            // txOssUpload(obj);
            //上传传到自己服务器
            FileUtil.writeBytes(obj.toString().getBytes(StandardCharsets.UTF_8), new File("/opt/oss/" + config.getObjectName()));
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()),
                    ftpProperties.getUsername(), ftpProperties.getPassword(), "",
                    "oss-file", config.getObjectName(), new ByteArrayInputStream(obj.toString().getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (Exception e) {
            log.error("域名oss文件上传失败", e);
            return false;
        }
    }

    @Override
    public void checkOssDomain(String ossText) {

        String objectStr = HttpClientUtil.doGet(config.getHttpPath());
        if (StringUtils.isEmpty(objectStr)) {
            return;
        }


        JSONObject jsonObject = JSONObject.parseObject(objectStr);

        if (StringUtils.isNotEmpty(ossText)) {
            jsonObject = JSONObject.parseObject(ossText);
        }

        log.info("旧oss动态域名:>>>{}", jsonObject);

        //解析并组装oss
        List<Oss> ossList = OssUtil.parseBuildOss(jsonObject);
        if (CollectionUtils.isEmpty(ossList)) {
            log.info("暂无oss域名");
        }

        log.info("解析后oss动态域名:>>>{}", JSON.toJSONString(ossList));

        Set<Oss> checkSelfFailedOssDomain = new LinkedHashSet<>();
        Set<Oss> checkSelfSuccessOssDomain = new LinkedHashSet<>();
        Set<Oss> check17CeFailedOssDomain = new LinkedHashSet<>();
        for (Oss oss : ossList) {
            DomainTypeEnum domainTypeEnum = DomainTypeEnum.getByCode(oss.getDomainType());
            //自检域名
            if (config.getSelfEnable()) {
                boolean checkPass = ossDomainProcessor.selfTestDomain(oss.getDomainName(), "ty", domainTypeEnum.getValue());
                if (checkPass) {
                    checkSelfSuccessOssDomain.add(oss);
                    continue;
                }
                log.info("checkInOssDomainTask:域名:【{}】,加密后域名:【{}】,商户分组类型:【{}】结果:【{}】",
                        oss.getDomainName(), oss.getEncryptDomainName(), oss.getGroupType(), checkPass);

                checkSelfFailedOssDomain.add(oss);
            }
        }

        //自检未通过的oss域名
        if(CollectionUtils.isNotEmpty(checkSelfFailedOssDomain)) {
            log.info("自检未通过的oss动态域名:>>>{}", JSON.toJSONString(checkSelfFailedOssDomain));
            //针对不可用域名,到域名池里面找对应商户分组的域名(domainGroupCode) 替换
            ossDomainProcessor.replaceUnavailableDomain(checkSelfFailedOssDomain, jsonObject);
        }


        //17ce检测域名
        if (config.getWebsocketSwitch() && CollectionUtils.isNotEmpty(checkSelfSuccessOssDomain)) {
                OssDomainWebSocket webSocket = null;
                if (config.getWebsocketSwitch() || config.getDnsSwitch()) {
                    webSocket = this.initOssWebSocketClient();
                }
                //自检失败的域名17ce检测 DomainConstants.OSS_DOMAIN区分域名池域名还是oss文件域名
                for(Oss successOss : checkSelfSuccessOssDomain){
                    ossDomainProcessor.check17ceDomain(webSocket, successOss, null,null, DomainConstants.OSS_DOMAIN);
                    continue;
            }
        }


        //获取缓存自检未通过的所有oss域名
        Set<String> keySet = LocalCacheService.FAILED_17CE_OSS_DOMAIN_MAP.asMap().keySet();
        if(CollectionUtils.isNotEmpty(keySet)){
            Iterator<String> it = keySet.iterator();
            while(it.hasNext()){
                String key = it.next();
                Oss value = LocalCacheService.FAILED_17CE_OSS_DOMAIN_MAP.getIfPresent(key);
                check17CeFailedOssDomain.add(value);
            }
        }

        //17ce检测未通过的oss动态域名
        if(CollectionUtils.isNotEmpty(check17CeFailedOssDomain)) {
            JSONObject newJsonObject = LocalCacheService.NEW_JSON_OBJECT.getIfPresent("newJsonObject");
            if(null == newJsonObject){
                return;
            }

            log.info("17ce检测未通过的oss动态域名:>>>{}", JSON.toJSONString(check17CeFailedOssDomain));
            //针对不可用域名,到域名池里面找对应商户分组的域名(domainGroupCode) 替换
            ossDomainProcessor.replaceUnavailableDomain(check17CeFailedOssDomain, newJsonObject);
        }
    }

    /**
     * 注入Socket客户端
     * <p>
     * let code = $.md5($.base64.encode($.md5(api_pwd).substring(4,23) + $.trim(user) + timestamp.substring(0,10)));
     *
     * @return
     */
    public OssDomainWebSocket initOssWebSocketClient() {
        URI uri = null;
        try {
            String ut = new Date().getTime() / 1000 + "";

            String md5password = OssUtil.getMd5key(config.getWebSocketPwd()).substring(4, 23);

            String tem1 = md5password + config.getWebSocketUser() + ut;

            String baseEncode = Base64.getEncoder().encodeToString(tem1.getBytes(StandardCharsets.UTF_8));
            String finalMd5 = OssUtil.getMd5key(baseEncode);// DigestUtils.md5Hex(baseEncode);
            String url = config.getWebSocketUri() + "?user=" + config.getWebSocketUser() + "&code=" + finalMd5 + "&ut=" + ut;
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.error("初始化WS连接失败!", e);
        }
        OssDomainWebSocket webSocketClient = new OssDomainWebSocket(uri, ossDomainWebSocketService);
        //启动时创建客户端连接
        webSocketClient.connect();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!webSocketClient.getReadyState().equals(ReadyState.OPEN)) {
            webSocketClient.close();
            log.info("webSocket连接没有打开!");
        }
        log.info("webSocket连接打开了");
        return webSocketClient;
    }
}
