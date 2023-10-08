package com.panda.multiterminalinteractivecenter.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FtpUtil {

    public static Boolean uploadFile(String host, int port, String username, String password, String basePath,
                                     String filePath, String fileName, InputStream inputStream) throws IOException {
        //1、创建临时路径
        StringBuilder tempPath = new StringBuilder();
        //2、创建FTPClient对象（对于连接ftp服务器，以及上传和上传都必须要用到一个对象）
        FTPClient ftp = new FTPClient();
        try {
            //3、定义返回的状态码
            int reply;
            //4、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect(host, port);
            //5、输入账号和密码进行登录
            ftp.login(username, password);
            log.info("FTP登陆成功!");
            //6、接受状态码(如果成功，返回230，如果失败返回503)
            reply = ftp.getReplyCode();
            //7、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
            if (!FTPReply.isPositiveCompletion(reply)) {
                //说明连接失败，需要断开连接
                ftp.disconnect();
                return false;
            }
            //16.把文件转换为二进制字符流的形式进行上传
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            log.info("FTP开始上传!dir=" + basePath + filePath);
            File path = new File(basePath + filePath);
            if (!path.exists()) {
                log.info(basePath + filePath + "不存在,开始创建");
                path.mkdirs();
                log.info(basePath + filePath + path.exists());
            }
            boolean changeWorkingDirectory = ftp.changeWorkingDirectory(filePath);
            if (changeWorkingDirectory) {
                log.info("进入文件" + filePath + "夹成功.");
            } else {
                log.info("进入文件" + filePath + "夹失败.开始创建文件夹");
                String[] dirs = filePath.split("/");
                for (String dir : dirs) {
                    //13、再次检测路径是否存在(/home/ftp/www/2019)-->返回false，说明路径不存在
                    log.info("2开始创建" + filePath + "," + tempPath + dir);
                    if (ftp.makeDirectory(tempPath + dir)) {
                        log.info("创建文件夹" + tempPath + dir + "成功");
                    } else {
                        log.info("创建文件夹" + tempPath + dir + "失败");
                    }
                    //12、更换临时路径：/home/ftp/www/2019
                    tempPath.append(dir).append("/");
                }
                boolean changeWorkingDirectory2 = ftp.changeWorkingDirectory(filePath);
                if (changeWorkingDirectory2) {
                    log.info("进入文件" + filePath + "夹成功.");
                } else {
                    log.info("进入文件" + filePath + "夹失败.");
                }
            }
            //17、设置为被动模式 这才是真正上传方法storeFile(filename,input),返回Boolean雷类型，上传成功返回true
            ftp.enterLocalPassiveMode();
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            if (!ftp.storeFile(fileName, inputStream)) {
                log.error("!!!!!!!上传失败啊," + fileName);
                return false;
            }
            // 18.关闭输入流
            inputStream.close();
            // 19.退出ftp
            ftp.logout();
        } catch (IOException e) {
            log.error("上传FTP 文档失败1!", e);
            throw new IOException(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    // 20.断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("上传FTP 文档失败2!", ioe);
                }
            }
        }
        return true;
    }

}