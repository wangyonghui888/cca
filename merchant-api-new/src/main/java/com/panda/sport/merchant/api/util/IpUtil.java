package com.panda.sport.merchant.api.util;


import com.panda.sport.merchant.common.utils.City;
import com.panda.sport.merchant.common.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class IpUtil {
    private static City cityDb;
    private static Map<String, City> cacheMap = new HashMap<>();
    private final static String CACHE_KEY = "localCache";
    private final static String LANGUAGE = "CN";
    // 缓存时间：单位毫秒
    private static long cacheTime;
    // 创建时间：单位毫秒
    private static long createTime;
    @Value("${pullIpPackageJob.switches}")
    private String switches;
    @Value("${pullIpPackageJob.folderPath}")
    private String folderPath;
    @Value("${pullIpPackageJob.fileName}")
    private String fileName;
    @Value("${pullIpPackageJob.fileNameTemp}")
    private String fileNameTemp;
    @Value("${pullIpPackageJob.ipDatabaseUrl}")
    private String ipDatabaseUrl;
    private static final String browser_property = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.";
    private static final String SWITCH_CONSTANT = "off";
    private static final int TIME_OUT = 5000;

    public void doRefresh() {
        cacheMap.clear();
        cityDb = null;
        setCacheTime(24, TimeUnit.HOURS);
    }

    @PostConstruct
    public void loadIPToCache() {
        ExecutorInstance.executorService.submit(() -> {
            try {
                log.info("加载IP库到内存中开始!");
                pull();
                File file = new File(folderPath + File.separator + fileName);
                File fileTemp = new File(folderPath + File.separator + fileNameTemp);
                log.info("加载IP库到内存中file："+folderPath + File.separator + fileName);
                log.info("加载IP库到内存中fileTemp："+folderPath + File.separator + fileNameTemp);
                if (file.exists()) {
                    log.info("加载IP库到内存中file.exists()："+folderPath + File.separator + fileName);
                    cityDb = new City(folderPath + File.separator + fileName);
                } else if (fileTemp.exists()) {
                    log.info("加载IP库到内存中fileTemp.exists()："+folderPath + File.separator + fileNameTemp);
                    cityDb = new City(folderPath + File.separator + fileNameTemp);
                }
                cacheMap.put(CACHE_KEY, cityDb);
                createTime = System.currentTimeMillis();
                setCacheTime(24, TimeUnit.HOURS);
            } catch (Exception ex) {
                log.error("系统启动时刷新IP库到本地缓存异常!", ex);
            }
        });
    }

    /**重载域名解析，length可以截取省市区*/
    public String findCity(String ip ,int length){
        if(StringUtils.isEmpty(ip) || IPUtils.isInnerIp(ip)){
            return "默认";
        }
        String ipArea = this.findCity(ip);
        if(StringUtils.isBlank(ipArea)) ipArea = "默认";
        if(length <= 0 || length >= 3){
            return ipArea;
        }
        String [] areaArray = ipArea.split(",");
        if(areaArray.length <= length){
            return ipArea;
        }
        if(length == 1){
            ipArea = areaArray[0];
        }
        if(length == 2){
            ipArea = areaArray[0] + "," + areaArray[1];
        }
        return ipArea;
    }

    /**
     * 每次读取都刷新一次缓存.如果没有数据就读取本地文件到缓存
     * 2019年10月20日
     *
     * @param ip
     * @return
     */
    public String findCity(String ip) {
        try {
            if (StringUtils.isBlank(ip)) return "";
            ip = ip.trim();
            this.refresh();
            if (cacheMap.get(CACHE_KEY) == null) {
                File file = new File(folderPath + File.separator + fileName);
                File fileTemp = new File(folderPath + File.separator + fileNameTemp);
                if (file.exists()) {
                    cityDb = new City(folderPath + File.separator + fileName);
                    cacheMap.put(CACHE_KEY, cityDb);
                } else if (fileTemp.exists()) {
                    cityDb = new City(folderPath + File.separator + fileNameTemp);
                    cacheMap.put(CACHE_KEY, cityDb);
                } else {
                    return null;
                }
            }
            City city = cacheMap.get(CACHE_KEY);
            if (city == null) return "";
            String[] result = city.find(ip, LANGUAGE);
            if (result == null) return "";
            if (result.length > 3) {
                return result[0] + "," + result[1] + "," + result[2];
            }
            return Arrays.toString(result).replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");
        } catch (Exception ex) {
            log.error("从IP库读取物理地址异常!", ex);
        }
        return "";
    }

    /**
     * 过期了刷新缓存
     */
    public void refresh() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - createTime > cacheTime) {
            ExecutorInstance.executorService.submit(() -> {
                try {
                    if (pull())
                        doRefresh();
                } catch (Exception e) {
                    log.error("PULL IP DATABASE ERROR!", e);
                }
            });
            createTime = System.currentTimeMillis();
        }
    }

    /**
     * 指定单位
     *
     * @param cacheTimeTemp
     * @param unit
     * @author:
     * @date: 2019年10月20日 下午12:09:10
     */
    public static void setCacheTime(long cacheTimeTemp, TimeUnit unit) {
        cacheTime = unit.toMillis(cacheTimeTemp);
    }

    /**
     * 毫秒
     *
     * @param cacheTimeTemp
     * @author:
     * @date: 2019年10月20日 下午12:07:46
     */
    public static void setCacheTime(long cacheTimeTemp) {
        cacheTime = cacheTimeTemp;
    }

    /**
     * 从IPNET拉取IP数据库(每天一次)
     *
     * @param
     * @return
     */
    public boolean pull() throws Exception {
        if (StringUtils.isEmpty(switches) || switches.toLowerCase().equals(SWITCH_CONSTANT)) {
            log.info("IP拉取开关已关闭!");
            return false;
        }
        log.info("IP拉取开关已打开");
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            File dir = new File(folderPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            URL url = new URL(ipDatabaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT);
            conn.setRequestProperty("User-Agent", browser_property);
            inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);
            if (getData != null) {
                File oldFile = new File(folderPath + File.separator + fileName);
                if (oldFile.exists()) {
                    File tempFile = new File(folderPath + File.separator + fileNameTemp);
                    if (tempFile.exists()) {
                        tempFile.delete();
                    }
                    if (oldFile.renameTo(tempFile)) {
                        oldFile.delete();
                    }
                }
            }
            File saveDir = new File(folderPath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(folderPath + File.separator + fileName);
            fos = new FileOutputStream(file);
            fos.write(getData);
            log.info("IP拉取成功,写入本地成功!");
            return true;
        } catch (Exception e) {
            if (!e.toString().contains("429")) {
                log.error("拉取IP库失败!", e);
            } else {
                log.error("IP拉取失败!", e);
            }
            return false;
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 从输入流中获取字节数组
     * 2019年10月20日
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * @Author: Joken
     * @Description: 获取当前机器ip
     * @Date: 2020/2/14 12:12
     * @Return: java.lang.String
     */
    public static String getLocalIp(int reTryLimitCount) {
        String localIp = "127.0.0.1";
        try {
            InetAddress address = InetAddress.getLocalHost();
            localIp = address.getHostAddress();
            if (StringUtils.isEmpty(localIp)) {
                if (reTryLimitCount > 0) {
                    log.info("结算获取本机ip,重试获取第{}次！", reTryLimitCount);
                    reTryLimitCount--;
                    getLocalIp(reTryLimitCount);
                } else {
                    log.info("结算获取本机ip,重试获取失败！");
                    return localIp;
                }
            } else {
                return localIp;
            }
        } catch (Exception e) {
            log.error("获取当前机器Ip异常！", e);
            return localIp;
        }
        return localIp;
    }
}