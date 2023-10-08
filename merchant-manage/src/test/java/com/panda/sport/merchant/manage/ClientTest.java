//package com.panda.sport.merchant.manage;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//import java.math.BigInteger;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.util.Base64;
//import java.util.Date;
//
//@Configuration
//@Slf4j
//public class ClientTest {
//
//    /**
//     * socket连接地址
//     */
//    @Value("${com.17ce.socket.url}")
//    private String webSocketUri;
//
//    @Value("${com.17ce.socket.user:jszr999@gmail.com}")
//    private String webSocketUser;
//
//    @Value("${com.17ce.socket.pwd:eY4Xv7Ue@Z9mNi6}")
//    private String webSocketPwd;
//
//    // $.md5($.base64.encode($.md5(api_pwd).substring(4,23) + $.trim(user) + ut))
//    public static void main(String[] args) {
//        try {
//            String user = "jszr999@gmail.com";
//            String pwd = "XV5VIUNH2P23I2X1";
//            String ut = new Date().getTime() / 1000 + "";
//
//
//            String md5password = getMd5key(pwd).substring(4, 23);
//
//            System.out.println(md5password);
//            String tem1 = md5password + user + ut;
//
//            String baseEncode = Base64.getEncoder().encodeToString(tem1.getBytes(StandardCharsets.UTF_8));
//
//            String finalMd5 = getMd5key(baseEncode);// DigestUtils.md5Hex(baseEncode);
//            System.out.println(finalMd5);
//            String url = "wss://wsapi.17ce.com:8001/socket?user=" + user + "&code=" + finalMd5 + "&ut=" + ut;
//
//            System.out.println(url);
//
//            WsClient wsClient = new WsClient(new URI(url));
//            wsClient.connect();
//            Thread.sleep(10000);
//            JSONObject jsonObject = new JSONObject();
//            Integer txnid = Integer.parseInt((new Date().getTime() / 1000 + "").substring(4));
//            String nodetype = "1,2,3,4";
//            Integer num = 2;
//            String Url = "https://api.gie0ohri.com";
//            String host = Url.replace("https://", "");
//            System.out.println("host:"+host);
//            String TestType = "DNS";
//            Integer TimeOut = 20;
//            String Request = "GET";
//            Boolean NoCache = true;
//            Integer type = 1;
//            String isps = "1,2,6,7,8,17,18,19";
//            String areas = "1,2";
//
//
//            String pro_ids = "190,195,357,236,79";
//            String AutoDecompress = "true";
//            jsonObject.put("txnid", txnid);
//            jsonObject.put("nodetype", nodetype);
//            jsonObject.put("num", num);
//            jsonObject.put("Url", Url);
//            jsonObject.put("Host", host);
//            jsonObject.put("TestType", "DNS");
//            jsonObject.put("TimeOut", TimeOut);
//            jsonObject.put("Request", Request);
//            jsonObject.put("NoCache", NoCache);
//            jsonObject.put("Cookie", "");
//            jsonObject.put("Speed", 0);
//            jsonObject.put("Trace", false);
//            jsonObject.put("UserAgent", "curl/7.47.0");
//            jsonObject.put("type", type);
//            jsonObject.put("GetMD5", true);
//            jsonObject.put("isps", isps);
//            jsonObject.put("FollowLocation", 3);
//            jsonObject.put("GetResponseHeader", true);
//            jsonObject.put("MaxDown", 1048576);
//            jsonObject.put("areas", areas);
//            jsonObject.put("pro_ids", pro_ids);
//            jsonObject.put("AutoDecompress", false);
//            jsonObject.put("PingCount", 10);
//            jsonObject.put("PingSize", 32);
//            System.out.println(jsonObject.toJSONString());
//            wsClient.send(jsonObject.toJSONString());
//            Thread.sleep(100000);
//        } catch (Exception e) {
//            System.out.println("error" + e);
//        }
//    }
//
//    public static String getMd5key(String str) {
//        byte[] digest = null;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("md5");
//            digest = md5.digest(str.getBytes("utf-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //16是表示转换为16进制数
//        String md5Str = new BigInteger(1, digest).toString(16);
//        return md5Str;
//    }
//
//}
