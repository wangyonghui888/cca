package com.panda.sport.merchant.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.TDomainMapper;
import com.panda.sport.bss.mapper.TMerchantGroupMapper;
import com.panda.sport.merchant.common.po.bss.MerchantGroupPO;
import com.panda.sport.merchant.common.po.bss.TDomain;
import com.panda.sport.merchant.common.utils.HttpConnectionPool;
import com.panda.sport.merchant.common.vo.CheckApiDomainVo;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.TDomainVo;
import com.panda.sport.merchant.manage.entity.form.DomainCheckPost;
import com.panda.sport.merchant.manage.service.IMongoService;
import com.panda.sport.merchant.manage.service.IOssDomainService;
import com.panda.sport.merchant.manage.util.DomainEnableWebSocket;
import com.panda.sport.merchant.manage.util.DomainWebSocket;
import com.panda.sport.merchant.mapper.DomainMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.manage.service.impl.LocalCacheService.DOMAIN;

@RefreshScope
@Service("webSocketService")
@Slf4j
public class WebSocketService {
    /**
     * socket连接地址
     */
    @Value("${com.17ce.socket.url}")
    private String webSocketUri;

    @Value("${com.17ce.socket.user:jszr999@gmail.com}")
    private String webSocketUser;

    @Value("${com.17ce.socket.pwd:XV5VIUNH2P23I2X1}")
    private String webSocketPwd;

    @Value("${com.17ce.socket.province:190,195,357}")
    private String provinceId;

    @Value("${com.17ce.socket.city:212,217,590,573}")
    private String cityId;

    @Value("${domain.check.enable:false}")
    private String checkDomainEnable;

    @Value("${websocket.17ce.enable:false}")
    private String websocketSwitch;

    @Value("${websocket.17ce.dns.enable:false}")
    private String dnsSwitch;


    @Value("${websocket.17ce.delay:60000}")
    private String dalay;

    @Value("${self.domain.url:null}")
    private String selfUrl;

    @Value("${self.domain.check.enable:false}")
    private String selfEnable;

    @Value("${self.domain.check.enable.third:false}")
    private String selfEnableThird;
    @Value("${domain.check.enable.third:false}")
    private String checkDomainEnableThird;

    @Value("${self.domain.check.threshold:1}")
    private String selfCheckThreshold;

    @Value("${self.domain.check.switch:0}")
    private String selfCheckSwitch;

    @Autowired
    public MerchantMapper merchantMapper;
    @Autowired
    private TDomainMapper tDomainMapper;
    @Autowired
    public IOssDomainService ossDomainService;

    @Autowired
    TMerchantGroupMapper tMerchantGroupMapper;

    @Autowired
    private IMongoService mongoService;

    @Autowired
    private DomainMapper domainMapper;

    /*    var postdata = {
        txnid: txnid,
        nodetype: [ 1, 2 ],
        num: 1,
        Url: $('#tx_url').val(),
        TestType: 'HTTP',
        Host: 'baidu.com',
        TimeOut: 20,
        Request: 'GET',
        NoCache: true,
        Speed: 0,
        Cookie: '',
        Trace: false,
        Referer: '',
        UserAgent: 'curl/7.47.0',
        FollowLocation: 2,
        GetMD5: true,
        GetResponseHeader: true,
        MaxDown: 1048576,
        AutoDecompress: true,
        type: 1,
        isps: [ 1, 2 ],
        pro_ids: '221,49',
        areas: [ 1 ],
        PingSize: 32,
        PingCount: 10
    }*/
    public void checkDomain() throws InterruptedException {
        if (checkDomainEnable.equalsIgnoreCase("true")) {
            log.info("checkDomain商户API域名检测开始!");
            long start = System.currentTimeMillis();
            List<CheckApiDomainVo> domainList = merchantMapper.getCheckApiDomain();
            DomainWebSocket webSocket = null;
            if (websocketSwitch.equalsIgnoreCase("true") || dnsSwitch.equalsIgnoreCase("true")) {
                webSocket = this.initWebSocketClient();
                Thread.sleep(1000);
            }
            try {
                for (CheckApiDomainVo domain : domainList) {
                    if (selfEnable.equalsIgnoreCase("true")) {
                        if (selfTestDomain(domain.getAppDomain(), "ty", "api", null) && websocketSwitch.equalsIgnoreCase("true")) {
                            sendWebSocketMessage(webSocket, domain.getAppDomain(), "HTTP");
                            Thread.sleep(500);
                        }
                    } else {
                        if (websocketSwitch.equalsIgnoreCase("true")) {
                            sendWebSocketMessage(webSocket, domain.getAppDomain(), "HTTP");
                            Thread.sleep(500);
                        }
                    }
                }
                if (dnsSwitch.equalsIgnoreCase("true")) {
                    Date now = new Date();
                    String tempDate = DateFormatUtils.format(now, "yyyy-MM-dd HH:mm");
                    log.info("DNS时间:" + tempDate);
                    if (tempDate.endsWith("0") || tempDate.endsWith("4") || tempDate.endsWith("7")) {
                        for (CheckApiDomainVo domain : domainList) {
                            sendWebSocketMessage(webSocket, domain.getAppDomain(), "DNS");
                            Thread.sleep(400);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("checkDomain:", e);
            }
            if (websocketSwitch.equalsIgnoreCase("true") || dnsSwitch.equalsIgnoreCase("true")) {
                int waitWs = Integer.parseInt(dalay);
                Thread.sleep(waitWs);
                webSocket.close();
            }
            log.info("checkDomain商户API域名检测结束:" + (System.currentTimeMillis() - start));
        }
    }

    public void checkDomainThird() {
        if ("true".equalsIgnoreCase(checkDomainEnableThird)) {
            log.info("checkDomain商户API域名检测开始!");
            long start = System.currentTimeMillis();
            List<TDomainVo> list = domainMapper.getTypeByEnable(1);
            try {
                for (TDomainVo domain : list) {
                    if (domain.getDomainName().contains("|")) {
                        continue;
                    }
                    String code = "cp";
                    if (domain.getGroupCode() == 1) {
                        code = "dj";
                    }
                    String domainType = "h5";
                    if (domain.getDomainType() == 3) {
                        domainType = "api";
                    }
                    if ("true".equalsIgnoreCase(selfEnableThird)) {
                        selfTestDomain(domain.getDomainName(), code, domainType, domain.getMerchantGroupId());
                    }
                }
            } catch (Exception e) {
                log.error("checkDomain:", e);
            }
            log.info("checkDomain商户API域名检测结束:" + (System.currentTimeMillis() - start));
        }
    }

    private boolean selfTestDomain(String domain, String code, String domainType, Long groupId) {
        try {
            String type;
            if ("ty".equalsIgnoreCase(code)) {
                type = code;
            } else {
                type = "cpdj";
            }
            DomainCheckPost domainCheckPost = new DomainCheckPost();
            domainCheckPost.setDomain(domain);
            domainCheckPost.setType(type);
            domainCheckPost.setDomaintype(domainType);
/*            Mono<String> result = WebClient.create().post()
                    .uri(selfUrl)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject("{" +
                            "  \"type\" : \"" + type + "\"," +
                            "  \"domain\" : \"" + domain + "\"," +
                            "  \"domaintype\" : \"" + domainType + "\"" +
                            "}"))
                    .retrieve().bodyToMono(String.class);*/
            ResponseEntity<String> responseEntity = HttpConnectionPool.restTemplate.postForEntity(selfUrl, domainCheckPost, String.class);
            log.info("selfTestDomain.result:{}", domain + responseEntity);
            String body = responseEntity.getBody();
            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONObject data = jsonObject.getJSONObject("data");
            Object obj = data.get("nohealthnodecount");
            log.info("selfTestDomain.nohealthnodecount=" + obj);
            int threshold = Integer.parseInt(selfCheckThreshold);
            if (obj != null) {
                int count = (int) obj;
                if (count > threshold) {
                    log.info(domain + "：selfTestDomain自检失败!threshold=" + threshold);
                    if ("0".equals(selfCheckSwitch) && !"ty".equalsIgnoreCase(code)) {
                        String text = "0000,selfTestDomain自检失败域名测速检测失败,threshold=" + threshold + "，第三方域名不自动切换!";
                        mongoService.send(text);
                    } else {
                        String text = String.format("域名自检发现此域名可能被攻击,请及时检查：domain:【%s】", domain);
                        mongoService.send(text);

//                        String newDomain = ossDomainService.switchDomain(0000, domain, "selfTest", code, groupId);
//                        if (StringUtils.isNotEmpty(newDomain)) {
//                            String text = "0000,selfTestDomain自检失败域名测速检测失败，threshold=" + threshold + "域名开始切换：\n" +
//                                    "旧域名【" + domain + "】,\n新域名【" + newDomain + "】!";
//                            mongoService.send(text);
//                        }
                    }
                    return false;
                } else {
                    log.info(domain + "selfTestDomain自检success!");
                    return true;
                }
            }
            return true;
        } catch (Exception e) {
            log.error(domain + "selfTestDomain:", e);
            return true;
        }
    }

    public void checkDNSDomain() throws InterruptedException {
        if (dnsSwitch.equalsIgnoreCase("true")) {
            List<String> domainList = merchantMapper.getAllApiDomain(null);
            DomainWebSocket webSocket = this.initWebSocketClient();
            Thread.sleep(1000);
            try {
                for (String domain : domainList) {
                    sendWebSocketMessage(webSocket, domain, "DNS");
                    Thread.sleep(600);
                }
            } catch (Exception e) {
                log.error("checkDNSDomain:", e);
            }
            int waitWs = Integer.parseInt(dalay);
            Thread.sleep(waitWs);
            webSocket.close();
        }
    }


    @Async
    public void checkSingleDomain(String domain) {
        try {
            if (websocketSwitch.equalsIgnoreCase("true")) {
                DomainWebSocket webSocket = this.initWebSocketClient();
                Thread.sleep(1000);
                try {
                    sendWebSocketMessage(webSocket, domain, "HTTP");
                } catch (Exception e) {
                    log.error("innercheckDomain:", e);
                }
                int waitWs = Integer.parseInt(dalay);
                Thread.sleep(waitWs);
                webSocket.close();
            }
        } catch (Exception e) {
            log.error("checkSingleDomain:", e);
        }

    }

    private void sendWebSocketMessage(WebSocketClient webSocket, String domain, String testType) {
        try {
            JSONObject jsonObject = new JSONObject();
            Integer txnid = Integer.parseInt((System.currentTimeMillis() / 10 + "").substring(6));
            LocalCacheService.domainMap.put(DOMAIN + txnid, domain + ";" + testType);
            log.info("checkDomain:" + domain + ",LocalCacheService.domainMap=" + LocalCacheService.domainMap.estimatedSize());
            String nodetype = "1,2,3,4";
            Integer num = 1;
            Integer TimeOut = 20;
            String Request = "GET";
            Boolean NoCache = true;
            Integer type = 1;
            String isps = "1,2,6,7,8,17,18,19";
            String areas = "1,2";
            String pro_ids = provinceId;
            String city_ids = cityId;
            jsonObject.put("txnid", txnid);
            jsonObject.put("nodetype", nodetype);
            jsonObject.put("num", num);
            jsonObject.put("Url", domain);
            String host = "";
            if (testType.equalsIgnoreCase("DNS")) {
                host = domain.replace("https://", "");
            }
            jsonObject.put("Host", host);
            jsonObject.put("TestType", testType);
            jsonObject.put("TimeOut", TimeOut);
            jsonObject.put("Request", Request);
            jsonObject.put("NoCache", NoCache);
            jsonObject.put("Cookie", "");
            jsonObject.put("Speed", 0);
            jsonObject.put("Trace", false);
            jsonObject.put("UserAgent", "curl/7.47.0");
            jsonObject.put("type", type);
            jsonObject.put("GetMD5", true);
            jsonObject.put("isps", isps);
            jsonObject.put("FollowLocation", 3);
            jsonObject.put("GetResponseHeader", true);
            jsonObject.put("MaxDown", 1048576);
            jsonObject.put("areas", areas);
            jsonObject.put("pro_ids", pro_ids);
            //jsonObject.put("city_ids", city_ids);
            jsonObject.put("AutoDecompress", false);
            jsonObject.put("PingCount", 10);
            jsonObject.put("PingSize", 32);
            log.info("sendWebSocketMessage:" + jsonObject);
            webSocket.send(jsonObject.toJSONString());
        } catch (Exception e) {
            log.error("sendWebSocketMessage:", e);
        }
    }

    /**
     * 注入Socket客户端
     * <p>
     * let code = $.md5($.base64.encode($.md5(api_pwd).substring(4,23) + $.trim(user) + timestamp.substring(0,10)));
     *
     * @return
     */
    public DomainWebSocket initWebSocketClient() {
        URI uri = null;
        try {
            String ut = new Date().getTime() / 1000 + "";

            String md5password = getMd5key(webSocketPwd).substring(4, 23);

            String tem1 = md5password + webSocketUser + ut;

            String baseEncode = Base64.getEncoder().encodeToString(tem1.getBytes(StandardCharsets.UTF_8));
            String finalMd5 = getMd5key(baseEncode);// DigestUtils.md5Hex(baseEncode);
            String url = webSocketUri + "?user=" + webSocketUser + "&code=" + finalMd5 + "&ut=" + ut;
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.error("初始化WS连接失败!", e);
        }
        DomainWebSocket webSocketClient = new DomainWebSocket(uri, ossDomainService);
        //启动时创建客户端连接
        webSocketClient.connect();
        return webSocketClient;
    }

    public DomainEnableWebSocket initWebSocketClient2() {
        URI uri = null;
        try {
            String ut = new Date().getTime() / 1000 + "";

            String md5password = getMd5key(webSocketPwd).substring(4, 23);

            String tem1 = md5password + webSocketUser + ut;

            String baseEncode = Base64.getEncoder().encodeToString(tem1.getBytes(StandardCharsets.UTF_8));

            String finalMd5 = getMd5key(baseEncode);// DigestUtils.md5Hex(baseEncode);
            String url = webSocketUri + "?user=" + webSocketUser + "&code=" + finalMd5 + "&ut=" + ut;

            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.error("初始化WS连接失败!", e);
        }
        DomainEnableWebSocket webSocketClient = new DomainEnableWebSocket(uri, ossDomainService);
        //启动时创建客户端连接
        webSocketClient.connect();
        return webSocketClient;
    }

    public String getMd5key(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

    public void checkUnuseDomain() {
        try {
            if (websocketSwitch.equalsIgnoreCase("true")) {
                String unuseDomain = tDomainMapper.getUnuseDomain();
                if (StringUtils.isNotEmpty(unuseDomain)) {
                    DomainWebSocket webSocket = this.initWebSocketClient();
                    Thread.sleep(1000);
                    try {
                        sendWebSocketMessage(webSocket, unuseDomain, "HTTP");
                    } catch (Exception e) {
                        log.error("1checkUnuseDomain:", e);
                    }
                    int waitWs = Integer.parseInt(dalay);
                    Thread.sleep(waitWs);
                    webSocket.close();
                }
            }
        } catch (Exception e) {
            log.error("2checkUnuseDomain:", e);
        }
    }

    public void checkInDomainEnable() throws InterruptedException {
        if (!websocketSwitch.equalsIgnoreCase("true")) {
            return;
        }
        try {
            //1、每天定时检测一次已使用的域名，如果没有商户在使用则改为未使用；
            DomainVo domainVo = new DomainVo();
            domainVo.setEnable(1);
            domainVo.setDomainType(2);
            List<TDomain> list = tDomainMapper.selectAll(domainVo);
            for (TDomain domain : list) {
                int count = merchantMapper.checkMerchantDomainExist(domain.getDomainName());
                if (count < 1) {
                    tDomainMapper.updateDomainEnable(0, domain.getDomainName());
                }
            }
        } catch (Exception e) {
            log.error("1、每天定时检测一次已使用的域名，如果没有商户在使用则改为未使用；异常！", e);
        }
        try {
            //2、每天定时检测一次被攻击，被劫持的域名，如果是可用的则自动恢复成未使用；
            long start = System.currentTimeMillis();
            List<TDomain> domains = tDomainMapper.selectAllByEnable34();
            DomainEnableWebSocket webSocket = this.initWebSocketClient2();
            Thread.sleep(1000);
            try {
                for (TDomain domain : domains) {
                    String testType = "HTTP";
                    if (domain.getEnable() == 4) {
                        testType = "DNS";
                    }
                    sendWebSocketMessage(webSocket, domain.getDomainName(), testType);
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                log.error("checkDomain:", e);
            }
            int waitWs = Integer.parseInt(dalay);
            Thread.sleep(waitWs);
            webSocket.close();
            log.info("checkDomain商户API域名检测结束" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            log.error("2、每天定时检测一次被攻击，被劫持的域名，如果是可用的则自动恢复成未使用；异常！", e);
        }
        try {
            //3、芒果无效域名提醒需将无效的域名显示出来，每个商户组发一次提醒，多个无效域名换行显示：
            List<TDomain> domainsFail = tDomainMapper.selectAllByEnable34();
            if (domainsFail.size() > 0) {
                //开始提醒
                Map<Long, List<TDomain>> map = domainsFail.stream().collect(Collectors.groupingBy(TDomain::getMerchantGroupId));
                for (Map.Entry<Long, List<TDomain>> entry : map.entrySet()) {
                    Long groupId = entry.getKey();
                    MerchantGroupPO merchantGroupPO = tMerchantGroupMapper.selectMerchantGroupById(groupId);
                    if (merchantGroupPO != null) {
                        String tx = "";
                        for (TDomain domain : entry.getValue()) {
                            String text = "商户组 ：" + merchantGroupPO.getGroupName() + "中存在无效域名: " + domain.getDomainName();
                            mongoService.send(text);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("3、芒果无效域名提醒需将无效的域名显示出来，每个商户组发一次提醒，多个无效域名换行显示：；异常！", e);
        }
    }
}
