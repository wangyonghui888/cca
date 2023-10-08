package com.panda.multiterminalinteractivecenter.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

@Slf4j
public class IPUtils {
    private final static String LANGUAGE = "CN";
    private static Pattern PATTERN = Pattern.compile("/(\\d+[\\.]){3}\\d+");
    public static final Logger LOGGER = LoggerFactory.getLogger(IPUtils.class);

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("获取本机IP出错", e);
            return "";
        }
    }

    public static String getLocalSystemIp() throws SocketException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        // String ip = "";
        StringBuilder ip = new StringBuilder();
        String addr = "";
        while (enumeration.hasMoreElements()) {
            NetworkInterface networkInterface = enumeration.nextElement();
            if (networkInterface.isUp()) {
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    addr = addressEnumeration.nextElement().toString();
                    if (PATTERN.matcher(addr).matches()) {
                        ip.append(addr);
                    }
                }
            }
        }
        LoggerFactory.getLogger(IPUtils.class).info("GET SYSTEM IP ADDR => " + ip);
        return ip.toString();
    }

    public static String longToIp(long ipLong) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipLong >>> 24);
        sb.append(".");
        sb.append(String.valueOf((ipLong & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipLong & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(ipLong & 0x000000FF));
        return sb.toString();
    }

    public static long ipToLong(String ipString) {
        long result = 0;
        try {
            java.util.StringTokenizer token = new java.util.StringTokenizer(ipString, ".");
            result += Long.parseLong(token.nextToken()) << 24;
            result += Long.parseLong(token.nextToken()) << 16;
            result += Long.parseLong(token.nextToken()) << 8;
            result += Long.parseLong(token.nextToken());
        } catch (Exception e) {

        }
        return result;
    }

    /**
     * @Author: Joken
     * @Description: 获取请求Ip
     * @Date: 2019/10/9 19:20
     * @Param: [request]
     * @Return:
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-connecting-ip");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("x-original-forwarded-for");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("x-real-forwarded-for");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("x-forwarded-for");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            log.error("获取IP地址异常!", e);
            ipAddress = "";
        }
        return ipAddress;
    }


}