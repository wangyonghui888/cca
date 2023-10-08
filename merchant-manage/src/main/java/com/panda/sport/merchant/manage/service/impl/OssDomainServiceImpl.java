package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.OssDomainMapper;
import com.panda.sport.bss.mapper.TDomainMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.OssDomain;
import com.panda.sport.merchant.common.po.merchant.TMerchantGroupInfo;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.common.utils.SpringUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.TDomainVo;
import com.panda.sport.merchant.manage.entity.form.WSResponse;
import com.panda.sport.merchant.manage.feign.MerchantApiClient;
import com.panda.sport.merchant.manage.service.IMongoService;
import com.panda.sport.merchant.manage.service.IOssDomainService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.mapper.DomainMapper;
import com.panda.sport.merchant.mapper.MerchantGroupMapper;
import com.panda.sport.merchant.mapper.TMerchantGroupInfoMapper;
import com.panda.sport.order.service.expot.FtpProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.manage.service.impl.LocalCacheService.*;

/**
 * <p>
 * oss域名表 服务实现类
 * </p>
 *
 * @author Baylee
 * @since 2021-05-28
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
@RefreshScope
public class OssDomainServiceImpl extends ServiceImpl<OssDomainMapper, OssDomain> implements IOssDomainService {

    //aes加密key
    @Value("${ali_aes_ksy:panda1234_1234ob}")
    private String AES_KSY;
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    @Value("${ali_end_point:oss-cn-hongkong.aliyuncs.com}")
    private String END_POINT;
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    @Value("${ali_access_key_id:LTAI5tF2THuSqiw3yEEE1fKu}")
    private String ACCESS_KEY_ID;
    @Value("${ali_access_key_secret:76YaCiqt0kRO9ZN1EU60rvQ2fYPbBM}")
    private String ACCESS_KEY_SECRET;
    @Value("${ali_bucket_name:aly-json-oss}")
    private String BUCKET_NAME;
    @Value("${ali_object_name:ali.json}")
    private String OBJECT_NAME;
    // --------------------------tx cloud oss--------------------------------
    // 1 初始化用户身份信息（secretId, secretKey）。
    @Value("${tx_secret_id:AKIDZwKVrqnJk9OXfcHn0xsZGiw65rbsjL6B}")
    private String SECRET_ID;
    @Value("${tx_secret_key:xjHf0e8AyXrywKAvb7OTSJwsiPn8G9x4}")
    private String SECRET_KEY;
    // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
    @Value("${tx_region_name:ap-hongkong}")
    private String REGION_NAME;
    // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
    @Value("${tx_bucket_name:tx-json-oss-1305515509}")
    private String TX_BUCKET_NAME;
    // 指定文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示下载的文件 picture.jpg 在 folder 路径下
    @Value("${tx_name:tx.json}")
    private String TX_NAME;

    @Value("${websocket.17ce.threshold:2}")
    private String threshold;

    private MerchantLogService merchantLogService;
    @Autowired
    private TDomainMapper tDomainMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private MerchantApiClient merchantApiClient;
    @Autowired
    private IMongoService mongoService;
    @Resource
    private FtpProperties ftpProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAndUploadOss(List<OssDomain> ossDomain) {
        this.saveOrUpdateBatch(ossDomain);
        List<OssDomain> domainList = ossDomain.stream().
                filter(v -> Constant.INT_0.equals(v.getStatus())).collect(Collectors.toList());
        try {
            if (CollUtil.isNotEmpty(domainList)) {
                JSONObject obj = new JSONObject();
                obj.put("update_by", "123");
                obj.put("update_time", DateUtil.formatDateTime(new Date()));
                List<String> list = Lists.newArrayList();
                for (OssDomain domain : domainList) {
                    list.add(encryptAES(domain.getUrl(), AES_KSY));
                }
                obj.put("api", list);
                //upload ali cloud oss
                aliOssUpload(obj);
                //upload tx cloud oss
                txOssUpload(obj);
            }
        } catch (Exception e) {
            log.error("域名oss文件上传失败", e);
            throw new RuntimeException("api oss upload error");
        }
        return true;
    }

    @Override
    public boolean ossUpload(JSONObject obj) {
        try {
            // aliOssUpload(obj);
            // txOssUpload(obj);
            //上传传到自己服务器
            FileUtil.writeBytes(obj.toString().getBytes(StandardCharsets.UTF_8), new File("/opt/oss/" + OBJECT_NAME));
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()),
                    ftpProperties.getUsername(), ftpProperties.getPassword(), "",
                    "oss-file", OBJECT_NAME, new ByteArrayInputStream(obj.toString().getBytes(StandardCharsets.UTF_8)));
            return true;
        } catch (Exception e) {
            log.error("域名oss文件上传失败", e);
            return false;
        }
    }

    @Override
    public void getDomainConfig(HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        FTPClient client = new FTPClient();
        InputStream input = null;
        try {
            // 创建输出流对象
            File file = new File("/opt/oss/" + OBJECT_NAME);
            byte[] byteArray;
            if (!file.exists()) {
                log.error("domain file is not exists!");
                //读取FTP文件
                client.connect(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()));
                boolean isLogin = client.login(ftpProperties.getUsername(), ftpProperties.getPassword());
                if (!isLogin) {
                    log.error("remote ftp login error!");
                    return; //Response.returnFail("remote ftp login error!");
                }
                client.enterLocalPassiveMode();
                client.changeWorkingDirectory("oss-file");
                input = client.retrieveFileStream(OBJECT_NAME);
                if (input == null) {
                    log.error("remote ftp file is not exists!");
                    return;
                }
                byteArray = IOUtils.toByteArray(input);
            } else {
                byteArray = FileUtil.readBytes(file);
            }
            outputStream = response.getOutputStream();
            // 设置返回内容格式
            response.setContentType("application/octet-stream");
            // 把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码
            // 中文不要太多，最多支持17个中文，因为header有150个字节限制。
            // 这一步一定要在读取文件之后进行，否则文件名会乱码，找不到文件
            String fileName = new String(OBJECT_NAME.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            // 设置下载弹窗的文件名和格式（文件名要包括名字和文件格式）
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            // 返回数据到输出流对象中
            outputStream.write(byteArray);
        } catch (IOException e) {
            log.error("download domain file error", e);
        } finally {
            // 关闭流对象
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (input != null) {
                    input.close();
                }
                if (client.isConnected()) {
                    client.disconnect();
                }
            } catch (IOException e) {
                log.error("close stream error", e);
            }
        }
    }

    @Override
    @Async
    public void processWSMessage(WSResponse wsResponse) {
        Integer rt = wsResponse.getRt();
        Integer txnid = wsResponse.getTxnid();
        String domainType = (String) LocalCacheService.domainMap.getIfPresent(DOMAIN + txnid);
        log.info(txnid + ",domainType=" + domainType);
        if (rt != 1 && rt != 10010) {
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            List<String> fieldName = new ArrayList<>();
            fieldName.add("domain check " + txnid);
            vo.setFieldName(fieldName);
            List<String> beforeValues = new ArrayList<>();
            beforeValues.add(wsResponse.getRt() + "");
            beforeValues.add(wsResponse.getError());
            beforeValues.add(wsResponse.getMsg());
            vo.setBeforeValues(beforeValues);
            List<String> afterValues = new ArrayList<>();
            if (wsResponse.getData() != null) {
                afterValues.add(wsResponse.getData().toString());
            }
            vo.setAfterValues(afterValues);
            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHECK_MERCHANT_Domian, vo,
                    MerchantLogConstants.MERCHANT_IN, "999999999999999", "System", "SYSTEM",
                    "SYSTEM", "9999999", "en",null,"正常", "正常", 3);
        } else if (rt == 10010) {
            String text = "存在无效域名,请检查!";
            mongoService.send(text, null);
            if (StringUtils.isEmpty(domainType)) {
                log.error("此次检测域名为空!!!");
                return;
            }
            String domain = domainType.split(";")[0];
            String testType = domainType.split(";")[1];
//            tDomainMapper.updateDomainEnable(3, domain);
        } else {
            if (StringUtils.isEmpty(domainType)) {
                log.error("此次检测域名为空!!!");
                return;
            }
            String domain = domainType.split(";")[0];
            String testType = domainType.split(";")[1];
            log.info(txnid + ",domain=" + domain);
            WSResponse.Result data = wsResponse.getData();
            String type = wsResponse.getType();
            CopyOnWriteArrayList<Double> timeCostList = (CopyOnWriteArrayList<Double>) domainCountMap.getIfPresent(DOMAIN + txnid);
            List<String> ipPool = this.getIpPool();
            if (type != null && type.equalsIgnoreCase("NewData") && data != null && data.getTotalTime() != null && testType.equalsIgnoreCase("HTTP")) {
                Double totalTime = data.getTotalTime();
                if (timeCostList != null) {
                    timeCostList.add(totalTime);
                } else {
                    timeCostList = new CopyOnWriteArrayList<>();
                }
                domainCountMap.put(DOMAIN + txnid, timeCostList);
            } else if (type != null && type.equalsIgnoreCase("NewData") && testType.equalsIgnoreCase("DNS") && CollectionUtils.isNotEmpty(ipPool) && data != null && data.getSrcIP() != null) {
                String scrIp = data.getSrcIP().replaceAll(" ", "").replace(";", "").replace(";", "");
                log.info("DNS:" + txnid + ",domain=" + domain + ",scrIp=" + scrIp);
                if (ipPool.contains(scrIp)) {
                    LocalCacheService.domainMap.invalidate(DOMAIN + txnid);
//                        tDomainMapper.updateDomainEnable(4, domain);
                        log.info(txnid + "," + domain + "DNS失效域名池域名池成功!");
                        Map<String, String> newDomainMap = tDomainMapper.getAvailableDomainByGroup(domain);
                        String newDomain = newDomainMap == null ? null : newDomainMap.get("domainName");
                        String groupName = newDomainMap == null ? "SYSTEM" : newDomainMap.get("groupName");
                        if (StringUtils.isEmpty(newDomain)) {
                            mongoService.send(txnid + ",商户组域名池没有可用域名!!!无效域名:" + domain, null);
                            return;
                            //newDomain = tDomainMapper.getAvailableDomain();
                        }
                        log.info(txnid + "DNS查询可切换域名:" + domain + "---->newDomain:" + newDomain);
                        if (StringUtils.isNotEmpty(newDomain)) {
                            log.info(txnid + "," + domain + "DNS切换域名开始,newDomain:" + newDomain);
//                            merchantMapper.updateApiDomain(domain, newDomain);
                            log.info(DOMAIN + txnid + "失效成功!");
//                            merchantApiClient.kickoutMerchant(null);
                            MerchantLogFiledVO vo = new MerchantLogFiledVO();
                            List<String> fieldName = new ArrayList<>();
                            fieldName.add("domain check" + txnid);
                            vo.setFieldName(fieldName);
                            List<String> beforeValues = new ArrayList<>();
                            beforeValues.add(domain);
                            vo.setBeforeValues(beforeValues);
                            List<String> afterValues = new ArrayList<>();
                            afterValues.add(newDomain);
                            vo.setAfterValues(afterValues);
//                            merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHANGE_MERCHANT_Domian, vo,
//                                    MerchantLogConstants.MERCHANT_IN, "999999999999999", groupName, groupName, groupName,
//                                    "9999999", "en",null,"正常", "被劫持", 3);
                            String text = txnid + ",DNS检测失败:" + scrIp + ",域名开始切换:" + domain + "---->" + newDomain;
                            mongoService.send(text, null);
//                            tDomainMapper.updateDomainEnable(4, domain);
                    } else {
                        log.error("域名池已空,请立即添加域名!!!!!!!!!!!");
                    }
                }
            }
            if (StringUtils.isNotEmpty(type) && type.equalsIgnoreCase("TaskEnd") && testType.equalsIgnoreCase("HTTP")) {
                log.info(txnid + "," + domain + "," + wsResponse.getData());
                double limit = Double.parseDouble(threshold);

                double f1 = 0;
                if (CollectionUtils.isNotEmpty(timeCostList)) {
                    List<Double> timeoutList = new ArrayList<>();
                    for (Double cost : timeCostList) {
                        if (cost > 3) {
                            timeoutList.add(cost);
                        }
                    }
                    int timeOutSize = timeoutList.size(), timeCostSize = timeCostList.size();
                    log.info(testType + "," + domain + ",timeOutSize=" + timeOutSize + ",timeCostSize=" + timeCostSize);
                    f1 = new BigDecimal((float) timeOutSize / timeCostSize).setScale(2, RoundingMode.HALF_UP).doubleValue();
                }
                if ((CollectionUtils.isEmpty(timeCostList) || f1 >= limit)) {
                    log.info(txnid + "," + domain + "失效域名开始,超时PING率:" + f1);
                    String newDomain = switchDomain(txnid, domain, "17ce HTTP","ty",null);
                    if (newDomain != null) {
                        String text = txnid + ",域名测速检测失败,无效率=" + f1 + ",阈值=" + limit + ",域名开始切换,老域名" + domain + ",新域名" + newDomain + ".共" +
                                (CollectionUtils.isEmpty(timeCostList) ? 0 : timeCostList.size()) + "个测试点返回数据";
                        mongoService.send(text);
                    }
                } else {
                    String domainType2 = (String) LocalCacheService.domainMap.getIfPresent(DOMAIN + txnid);
                    if (StringUtils.isNotEmpty(domainType2)) {
                        int count = merchantMapper.checkMerchantDomainExist(domain);
                        log.info(txnid + ",checkMerchantDomainExist:" + count);
//                        if (count > 0) {
//                            tDomainMapper.updateDomainEnable(1, domain);
//                        } else {
//                            tDomainMapper.updateDomainEnable(2, domain);
//                        }
                    }
                }
            }
        }
    }

    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private TMerchantGroupInfoMapper merchantGroupInfoMapper;
    @Autowired
    private MerchantGroupMapper merchantGroupMapper;

    @Override
    public String switchDomain(Integer txnid, String domain, String type,String code,Long groupIdNew ) {
        if ("ty".equalsIgnoreCase(code)){
//            tDomainMapper.updateDomainEnable(3, domain);
            Map<String, String> newDomainMap = tDomainMapper.getAvailableDomainByGroup(domain);
            String newDomain = newDomainMap == null ? null : newDomainMap.get("domainName");
            String groupName = newDomainMap == null ? "SYSTEM" : newDomainMap.get("groupName");
            if (StringUtils.isEmpty(newDomain)) {
                mongoService.send(txnid + type + ",商户组域名池没有可用域名!!!无效域名:" + domain, null);
                return null;
            }
            log.info(txnid + type + "可切换域名:" + domain + "---->newDomain:" + newDomain);
            if (StringUtils.isNotEmpty(newDomain)) {
                log.info(txnid + "," + domain + "切换域名开始,newDomain:" + newDomain);
//                merchantMapper.updateApiDomain(domain, newDomain);
//                tDomainMapper.updateDomainEnable(1, newDomain);
//                merchantApiClient.kickoutMerchant(null);
                MerchantLogFiledVO vo = new MerchantLogFiledVO();
                List<String> fieldName = new ArrayList<>();
                fieldName.add("domain check" + txnid);
                vo.setFieldName(fieldName);
                List<String> beforeValues = new ArrayList<>();
                beforeValues.add(domain);
                vo.setBeforeValues(beforeValues);
                List<String> afterValues = new ArrayList<>();
                afterValues.add(newDomain);
                vo.setAfterValues(afterValues);
//                if ("selfTest".equals(type)) {
//                    merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHANGE_MERCHANT_Domian, vo,
//                            MerchantLogConstants.MERCHANT_IN, "999999999999999", groupName, groupName, groupName,
//                             "9999999", "en",null,"被攻击", "-",3);
//                } else {
//                    merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, MerchantLogTypeEnum.CHANGE_MERCHANT_Domian, vo,
//                            MerchantLogConstants.MERCHANT_IN, "999999999999999", groupName, groupName, groupName,
//                            "9999999", "en",null,"-", "被攻击", 3);
//                }
    /*            String text = txnid + ",域名测速检测失败,无效率:" + f1 + ",阈值" + limit + "域名开始切换:" + domain + "---->" + newDomain + ".共" +
                        (CollectionUtils.isEmpty(timeCostList) ? 0 : timeCostList.size()) + "个测试点返回数据";
                mongoService.send(text);*/
                return newDomain;
            }else {
                log.error("域名池已空,请立即添加域名!!!!!!!!!!!");
            }
        }else {
                DomainAbstractService domainAbstractService = null;
                MerchantLogTypeEnum merchantLogTypeEnum = null;
                if ("dj".equalsIgnoreCase(code)){
                    //电竞
                    merchantLogTypeEnum = MerchantLogTypeEnum.CHANGE_MERCHANT_DJ_Domian;
                    domainAbstractService = (DomainAbstractService) SpringUtil.getBean("DjDomainServiceImpl");
                }else if ("cp".equalsIgnoreCase(code)){
                    merchantLogTypeEnum = MerchantLogTypeEnum.CHANGE_MERCHANT_CP_Domian;
                    domainAbstractService = (DomainAbstractService) SpringUtil.getBean("CpDomainServiceImpl");
                }else {
                    log.info("未配置！");
                }
                if (domainAbstractService == null){
                    log.error("数据配置异常！");
                    return null;
                }
                TDomainVo domainVoOld = domainMapper.getTypeByDomainName(domain,groupIdNew);
                Integer domainType = domainVoOld.getDomainType();
                Long groupId = domainVoOld.getMerchantGroupId();
                TDomainVo domainVo = domainMapper.getDomainByTypeAndGroupId(domainType, groupId);
                if (domainVo == null){
                    mongoService.send(txnid + type + ","+code+"商户组域名池没有可用域名!!!无效域名:" + domain, null);
                    return null;
                }
//                domainMapper.updateByDomainTypeAndName(3, domainType,domain,groupId,System.currentTimeMillis());
//                domainMapper.updateByDomainTypeAndName(1, domainType,domainVo.getDomainName(),groupId,System.currentTimeMillis());
                List<TMerchantGroupInfo> merchantList = merchantGroupInfoMapper.tMerchantGroupInfoByGroupId(groupId);
                for (TMerchantGroupInfo merchant : merchantList) {
                    domainAbstractService.sendMsg(merchant.getMerchantName(), domainType,domainVo.getDomainName(),1);
                }
            String groupName =  merchantGroupMapper.load(groupId.intValue()).getGroupName();
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
                List<String> fieldName = new ArrayList<>();
                fieldName.add("domain check" + txnid);
                vo.setFieldName(fieldName);
                List<String> beforeValues = new ArrayList<>();
                beforeValues.add(domain);
                vo.setBeforeValues(beforeValues);
                List<String> afterValues = new ArrayList<>();
                afterValues.add(domainVo.getDomainName());
                vo.setAfterValues(afterValues);
//                if ("selfTest".equals(type)) {
//                    merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, merchantLogTypeEnum, vo,
//                            MerchantLogConstants.MERCHANT_IN, "999999999999999", "system", groupName, groupName,
//                            "9999999", "en",null,"被攻击", "-",domainType );
//                } else {
//                    merchantLogService.saveLog(MerchantLogPageEnum.DOMAIN_CENTER, merchantLogTypeEnum, vo,
//                            MerchantLogConstants.MERCHANT_IN, "999999999999999", "system", groupName, groupName,
//                             "9999999", "en",null,"-", "被攻击",domainType);
//                }
                return domainVo.getDomainName();
            }
        return null;
    }


    @Override
    @Async
    public void processWSMessage2(WSResponse wsResponse) {
        Integer rt = wsResponse.getRt();
        Integer txnid = wsResponse.getTxnid();
        String domainType = (String) LocalCacheService.domainMap.getIfPresent(DOMAIN + txnid);
        log.info(txnid + ",domainType=" + domainType);
        if (StringUtils.isEmpty(domainType)) {
            log.error("此次检测域名为空!!!");
            return;
        }
        String domain = domainType.split(";")[0];
        String testType = domainType.split(";")[1];
        log.info(txnid + ",domain=" + domain);
        WSResponse.Result data = wsResponse.getData();
        String type = wsResponse.getType();
        List<Double> timeCostList = (List<Double>) domainCountMap.getIfPresent(DOMAIN + txnid);
        List<String> ipPool = this.getIpPool();
        if (type != null && type.equalsIgnoreCase("NewData") && data != null && data.getTotalTime() != null && testType.equalsIgnoreCase("HTTP")) {
            Double totalTime = data.getTotalTime();
            if (timeCostList != null) {
                timeCostList.add(totalTime);
            } else {
                timeCostList = new ArrayList<>();
            }
            domainCountMap.put(DOMAIN + txnid, timeCostList);
        } else if (type != null && type.equalsIgnoreCase("NewData") && testType.equalsIgnoreCase("DNS") && CollectionUtils.isNotEmpty(ipPool) && data != null && data.getSrcIP() != null) {
            String scrIp = data.getSrcIP().replaceAll(" ", "").replace(";", "").replace(";", "");
            log.info("DNS:" + txnid + ",domain=" + domain + ",scrIp=" + scrIp);
            if (!ipPool.contains(scrIp)) {
//                tDomainMapper.updateDomainEnable(0, domain);
                log.info(txnid + "," + domain + "DNS失效域名恢复池域名池成功!");
            }
        }
        if (StringUtils.isNotEmpty(type) && type.equalsIgnoreCase("TaskEnd") && testType.equalsIgnoreCase("HTTP")) {
            log.info(txnid + "," + domain + "," + wsResponse.getData());

            MathContext mc = new MathContext(2, RoundingMode.HALF_DOWN); //必须设置精度
            double limit = Double.parseDouble(threshold);
            
            double f1 = 0;
            if (CollectionUtils.isNotEmpty(timeCostList)) {
                List<Double> timeoutList = new ArrayList<>();
                for (Double cost : timeCostList) {
                    if (cost > 3) {
                        timeoutList.add(cost);
                    }
                }
                int timeOutSize = timeoutList.size(), timeCostSize = timeCostList.size();
                log.info(testType + "," + domain + ",timeOutSize=" + timeOutSize + ",timeCostSize=" + timeCostSize);
                f1 = new BigDecimal((float) timeOutSize / timeCostSize).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
            if ((CollectionUtils.isEmpty(timeCostList) || f1 >= limit)) {
                log.info(txnid + "," + domain + "失效域名开始,超时PING率:" + f1);
            } else {
//                tDomainMapper.updateDomainEnable(0, domain);
            }
        }
    }

    private List<String> getIpPool() {
        List<String> ipList = (List<String>) dnsIpPoolMap.getIfPresent(DOMAIN_DNS);
        if (CollectionUtils.isEmpty(ipList)) {
            ipList = tDomainMapper.getIpPool();
            dnsIpPoolMap.put(DOMAIN_DNS, ipList);
        }
        return ipList;
    }

    private void aliOssUpload(JSONObject obj) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        // 创建PutObjectRequest对象。
        // 填写Bucket名称、Object完整路径和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        //上传文件
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, OBJECT_NAME, new ByteArrayInputStream(obj.toString().getBytes()));
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);
        // 上传文件。
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    private void txOssUpload(JSONObject obj) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(REGION_NAME);
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        // 指定文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示下载的文件 picture.jpg 在 folder 路径下
        //上传文件
        ByteArrayInputStream input = new ByteArrayInputStream(obj.toString().getBytes());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度
        objectMetadata.setContentLength(input.available());
        com.qcloud.cos.model.PutObjectRequest putObjectRequest = new com.qcloud.cos.model.PutObjectRequest(TX_BUCKET_NAME, TX_NAME, input, objectMetadata);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
    }

    /**
     * PKCS5Padding -- Pkcs7 两种padding方法都可以
     *
     * @param content content
     * @param key     key
     */
    public static String decryptAES(String content, String key) {
        try {
            byte[] decode = Base64.getDecoder().decode(content);
            SecretKeySpec secretKe = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKe);
            //解密后是16进制
            return new String(cipher.doFinal(decode));
        } catch (Exception e) {
            log.error(String.format("解密失败:，content：%s, key: %s", content, key));
            throw new RuntimeException("decrypt error");
        }

    }

    /**
     * 加密
     *
     * @param content content
     * @param key     key
     */
    public static String encryptAES(String content, String key) {
        try {
            SecretKeySpec secretKe = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            // "算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKe);
            byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error(String.format("加密失败:，content：%s, key: %s", content, key));
            throw new RuntimeException("encrypt error");
        }

    }
}
