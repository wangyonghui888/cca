package com.panda.sport.merchant.common.utils;

import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/6/2 15:03
 */
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

    public static boolean deleteFile(String host, int port, String username, String password, String filePath, String fileName) throws IOException {
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
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            return ftp.deleteFile(filePath + fileName);
        } finally {
            if (ftp.isConnected()) {
                ftp.logout();
                try {
                    // 20.断开ftp的连接
                    log.info("关闭ftp连接！");
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("上传FTP 文档失败2!", ioe);
                }
            }
        }
    }

    /**
     * 下载时间
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param remotePath
     * @param fileName
     * @param localPath
     * @return
     * @throws IOException
     */
    public static Boolean downFile(String host, int port, String username, String password, String remotePath,
                                   String fileName, String localPath) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {

            //3、定义返回的状态码
            int reply;
            //4、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect(host, port);
            //5、输入账号和密码进行登录
            ftp.login(username, password);
            //6、接受状态码(如果成功，返回230，如果失败返回503)
            reply = ftp.getReplyCode();
            //7、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false

            if (!FTPReply.isPositiveCompletion(reply)) {
                //说明连接失败，需要断开连接
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }
            ftp.logout();
            success = true;
        } catch (IOException e) {
            log.error("下载文件异常1!", e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    // 20.断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    log.error("下载文件异常2!", ioe);
                }
            }
        }
        return success;
    }

    /**
     * 删除旧文件
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param remotePath
     * @return
     * @throws IOException
     */
    public static void deleteOldMerchantFile(String host, int port, String username, String password, String remotePath) throws Exception {
        FTPClient ftp = new FTPClient();
        try {
            log.info("开始删除过期文件" );
            //3、定义返回的状态码
            int reply;
            //4、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect(host, port);
            //5、输入账号和密码进行登录
            ftp.login(username, password);
            //6、接受状态码(如果成功，返回230，如果失败返回503)
            reply = ftp.getReplyCode();
            //7、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
            if (!FTPReply.isPositiveCompletion(reply)) {
                log.error("FTP连接失败，需要断开连接!");
                //说明连接失败，需要断开连接
                ftp.disconnect();
            }
            log.info("FTP连接成功！");
            // 转移到FTP服务器目录
            log.info("开始下载！remotePath = {}", remotePath);
            String curDirectory = ftp.printWorkingDirectory();
            boolean changeWorkingDirectory = ftp.changeWorkingDirectory(remotePath);
            if (!changeWorkingDirectory) {
                log.error("进入文件夹" + remotePath + "失败!");
            }
            //设置为被动模式
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listDirectories("/");
            if (fs == null) {
                log.error(remotePath + ",文件为空!");
                return;
            }
            // 核心逻辑
            deleteIteratorFiles(ftp,curDirectory,fs);

        } catch (IOException e) {
            log.error("删除异常!", e);
            throw e;
        } finally {
            ftp.logout();
            if (ftp.isConnected()) {
                try {
                    // 20.断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        log.info("删除结束");
    }

    /**
     * 迭代删除 panda_order 开头的文件夹  小于1年的删除
     * @param ftp
     * @param curDirectory
     * @param fs
     */
    private static void deleteIteratorFiles(FTPClient ftp, String curDirectory, FTPFile[] fs) {
        log.info("开始删除！！！！" + fs.length);
        for (FTPFile ff1 : fs) {
            try {
                log.info("文件名 ={}" + ff1.getName());
                if (ff1.getName().startsWith("panda_order_")) {
                    FTPFile[] ftpFiles = ftp.listFiles(curDirectory + "/" + ff1.getName());
                    for (FTPFile ff2 : ftpFiles) {
                        if (ff2.getName().startsWith("panda_order_")) {
                            FTPFile[] secondFtpFiles = ftp.listFiles(curDirectory + "/" + ff1.getName()+"/"+ff2.getName());
                            for (FTPFile ff3 : secondFtpFiles) {
                                if (ff3.getName().contains("-")) {
                                    String[] split = ff3.getName().split("-");
                                    // 如果是一年以上的数据就删除
                                    LocalDate ftpTime = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 1);
                                    if (ftpTime.isBefore(LocalDate.now().minusYears(1))) {
                                        ftp.removeDirectory(curDirectory + "/" + ff1.getName() + "/" + ff2.getName() + "/" + ff3.getName());
                                    }
                                }
                            }
                        }else {
                            if (ff2.getName().contains("-")){
                                String[] split = ff2.getName().split("-");
                                // 如果是一年以上的数据就删除
                                LocalDate ftpTime = LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), 1);
                                if (ftpTime.isBefore(LocalDate.now().minusYears(1))) {
                                    ftp.removeDirectory(curDirectory + "/" + ff1.getName() + "/" + ff2.getName());
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error("删除单个文件夹异常:{}",ff1.getName(),e);
            }
        }
    }

    /**
     * 下载时间
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param remotePath
     * @param fileName
     * @return
     * @throws IOException
     */
    public static void downFile(String host, int port, String username, String password, String remotePath,
                                String fileName, String ftpFileName, HttpServletResponse response) throws Exception {
        FTPClient ftp = new FTPClient();
        try {
            log.info("开始进入下载:" + fileName);
            //3、定义返回的状态码
            int reply;
            //4、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftp.connect(host, port);
            //5、输入账号和密码进行登录
            ftp.login(username, password);
            //6、接受状态码(如果成功，返回230，如果失败返回503)
            reply = ftp.getReplyCode();
            //7、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
            if (!FTPReply.isPositiveCompletion(reply)) {
                log.error("FTP连接失败，需要断开连接!");
                //说明连接失败，需要断开连接
                ftp.disconnect();
            }
            log.info("FTP连接成功！ftpFileName = {}", ftpFileName);
            // 转移到FTP服务器目录
            log.info("开始下载！remotePath = {}", remotePath);
            boolean changeWorkingDirectory = ftp.changeWorkingDirectory(remotePath);
            if (!changeWorkingDirectory) {
                log.error("进入文件夹" + remotePath + "失败!");
            }
            //设置为被动模式
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            if (fs == null) {
                log.error(ftpFileName + ",文件为空!");
                return;
            } else {
                log.info("开始下载！查找文件！！！" + fs.length);
            }
            for (FTPFile ff : fs) {
                log.info("文件名 ={}" + ff.getName());
                if (ff.getName().equals(ftpFileName)) {
                    log.info("找到文件开始下载！");
                    String newName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
                    // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
                    response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
                    // 设置文件头：最后一个参数是设置下载的文件名并编码为UTF-8
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"; filename*=utf-8''" + newName);
                    // ftp文件获取文件
                    InputStream is = null;
                    BufferedInputStream bis = null;
                    try {
                        is = ftp.retrieveFileStream(ftpFileName);
                        bis = new BufferedInputStream(is);
                        OutputStream out = response.getOutputStream();
                       // out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                        byte[] buf = new byte[1024];
                        int len = 0;
                        while ((len = bis.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.flush();
                        log.info("下载完成！");
                    } catch (Exception e) {
                        log.error("下载异常!", e);
                        throw e;
                    } finally {
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e) {
                                log.error("关闭下载流异常1!", e);
                            }
                        }
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                log.error("关闭下载流异常2!", e);
                            }
                        }
                    }
                    return;
                }
            }
        } catch (IOException e) {
            log.error("下载异常!", e);
            throw e;
        } finally {
            ftp.logout();
            if (ftp.isConnected()) {
                try {
                    // 20.断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        log.info("下载结束");
    }

/*    public static void main(String[] args) throws Exception {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setFtpFileName("1639540500795.csv.zip");
        merchantFile.setFilePath("/system/20211115/");
        merchantFile.setFileName("注单查询导出_1639540500795.csv.zip");
        downFile("172.18.178.238",21,"admin","g3oL2Y21ZcJkgcMZ",
                "/",merchantFile, null);
    }*/

    public static void downFile(String host, int port, String username, String password, String remotePath,
                                MerchantFile merchantFile, HttpServletResponse response) throws Exception {
        FTPClient ftp = null;
        List<byte[]> listBytes = new ArrayList<>();
        String[] ftpFileNames = merchantFile.getFtpFileName().split(",");
        for (String ftpFileName : ftpFileNames){
            try {
                ftp = new FTPClient();
                log.info("开始进入多文件下载:" + merchantFile.getFileName());
                //3、定义返回的状态码
                int reply;
                //4、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
                ftp.connect(host, port);
                //5、输入账号和密码进行登录
                ftp.login(username, password);
                //6、接受状态码(如果成功，返回230，如果失败返回503)
                reply = ftp.getReplyCode();
                //7、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
                log.info("FTP reply = {}",reply);
                if (!FTPReply.isPositiveCompletion(reply)) {
                    log.error("FTP连接失败，需要断开连接!");
                    //说明连接失败，需要断开连接
                    ftp.disconnect();
                }
                log.info("FTP连接成功！ftpFileName = {}", merchantFile.getFtpFileName());
                // 转移到FTP服务器目录
                log.info("开始下载！remotePath = {}", remotePath);
                boolean changeWorkingDirectory = ftp.changeWorkingDirectory(remotePath);
                if (!changeWorkingDirectory) {
                    log.error("进入文件夹" + remotePath + "失败!");
                }
                //设置为被动模式
                ftp.enterLocalPassiveMode();
                FTPFile[] fs = ftp.listFiles();
                if (fs == null) {
                    log.error(merchantFile.getFtpFileName() + ",文件为空!");
                    return;
                } else {
                    log.info("开始下载！查找文件！！！" + fs.length);
                }
                log.info("文件名称= {}", ftpFileName);
                for (FTPFile ff : fs) {
                    if (ff.getName().equals(ftpFileName)) {
                        InputStream is = ftp.retrieveFileStream(ftpFileName);
                        if (is != null) {
                            log.info("文件大小 = {}", is.available());
                            byte[] down = IOUtils.toByteArray(is);
                            listBytes.add(down);
                            break;
                        }
                    }
                }
            }catch (Exception e){
                log.error("FTP读取文件异常！", e);
            }finally{
                disconnect(ftp);
            }
        }
        log.info("找到文件开始下载！");
        String newName = URLEncoder.encode(merchantFile.getFileName(), StandardCharsets.UTF_8.toString());
        // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        // 设置文件头：最后一个参数是设置下载的文件名并编码为UTF-8
        response.setHeader("Content-Disposition", "attachment; filename=\"" + newName + "\"; filename*=utf-8''" + newName);
        // ftp文件获取文件
        try {
            ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            ZipEntry entry = null;
            for (int i = 0; i < ftpFileNames.length; i++) {
                byte[] tmpBytes = listBytes.get(i);
                String name = ftpFileNames[i];
                entry = new ZipEntry(name);
                // 存储项信息到压缩文件
                out.putNextEntry(entry);
                // 将文件的内容通过字节数组复制到压缩文件中
                out.write(tmpBytes, 0, tmpBytes.length);
                out.closeEntry();
            }
            out.close();
            out.flush();
            fileOutputStream.close();
            log.info("下载完成！");
        } catch (Exception e) {
            log.error("下载异常!", e);
            throw e;
        }
    }

    private static void disconnect(FTPClient ftp) throws IOException {
        if (ftp != null) {
            ftp.logout();
            if (ftp.isConnected()) {
                try {
                    // 20.断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private static InputStream makeInputStream( List<InputStream> list){
        Vector<InputStream> v = new Vector<>();
        for (InputStream i : list){
            v.add(i);
        }

        Enumeration<InputStream> em = v.elements();
        SequenceInputStream sq = new SequenceInputStream(em);
        return sq;
    }
  /*  public static void main(String[] args) throws IOException {
        File file = new File("D:\\work\\63-9279031238.txt");
        InputStream inputStream = new FileInputStream(file);
        log.info("=========>" +inputStream.available());

        byte data[] = new byte[2048];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        BufferedInputStream entryStream = new BufferedInputStream(inputStream, 2048);
        ZipEntry entry = new ZipEntry("63-9279031238.txt");
        zos.putNextEntry(entry);
        int count;
        while ((count = entryStream.read(data, 0, 2048)) != -1)
        {
            zos.write(data, 0, count);
        }
        entryStream.close();
        zos.closeEntry();
        zos.close();
        InputStream in = new ByteArrayInputStream(bos.toByteArray());
        log.info("=========>" +in.available());
        FtpUtil.uploadFile("ftp.sportxxx1ceo.com",21,"admin","IPLEqQZWYM2viq5V","/",
                "zip/","xxxx.txt.zip",in);
        inputStream.close();
        in.close();
    }*/
}