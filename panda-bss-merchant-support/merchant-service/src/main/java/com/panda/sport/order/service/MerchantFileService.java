package com.panda.sport.order.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.DateTimeUtils;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantFileVo;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.mapper.MerchantFileMapper;
import com.panda.sport.order.service.expot.OrderFileExportService;
import com.panda.sport.order.service.expot.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.service.service
 * @Description :  商户文件服务类
 * @Date: 2020-12-08 12:18:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@RefreshScope
@Service
@Slf4j
public class MerchantFileService {

    @Autowired
    private MerchantFileMapper merchantFileMapper;


    @Autowired
    private MerchantLogService merchantLogService;

    @Value("${spring.ftp.host:null}")
    private String host;

    @Value("${spring.ftp.port:21}")
    private Integer port;

    @Value("${spring.ftp.username:null}")
    private String username;

    @Value("${spring.ftp.password:null}")
    private String password;

    @Value("${spring.ftp.basePath:/}")
    private String basePath;

    @Value("${spring.ftp.dataSize:20000}")
    private String ftpDataSize;

    @Value("${exportDataSize:1000000}")
    private Integer exportDataSize;

    public static final int SPLIT_FILE_DATE_SIZE = 100000;

    public static final int SPLIT_FILE_DATE_SIZE_2 = 200000;
    /**
     * 查询列表
     *
     * @param merchantFileVo
     * @return
     */
    public PageVO<MerchantFile> queryList(MerchantFileVo merchantFileVo) {
        try {
            if (StringUtils.isNotBlank(merchantFileVo.getMerchantCode())) {
                merchantFileVo.setMerchantCodes(Arrays.asList(merchantFileVo.getMerchantCode()));
            }
            Integer count = merchantFileMapper.queryListCount(merchantFileVo);
            PageVO<MerchantFile> poPageVO = new PageVO<MerchantFile>(count, merchantFileVo.getPageSize(), merchantFileVo.getPageNum());
            if (count == 0) {
                return poPageVO;
            }
            merchantFileVo.setStart((merchantFileVo.getPageNum() - 1) * merchantFileVo.getPageSize());
            if (merchantFileVo.getStart() > count) {
                return poPageVO;
            }
            poPageVO.setRecords(merchantFileMapper.queryList(merchantFileVo));
            return poPageVO;
        } catch (Exception e) {
            log.error("查询任务异常!", e);
            return null;
        }
    }

    /**
     * 返回执行中的任务
     *
     * @return
     */
    public List<MerchantFile> queryExecuteFile(MerchantFileVo merchantFileVo) {
        if (merchantFileVo == null) {
            merchantFileVo = new MerchantFileVo();
        }
        if (StringUtils.isNotBlank(merchantFileVo.getMerchantCode())) {
            merchantFileVo.setMerchantCodes(Arrays.asList(merchantFileVo.getMerchantCode()));
        }
        return merchantFileMapper.queryExecuteFile(merchantFileVo);
    }


    /**
     * 返回执行中的任务
     *
     * @return
     */
    public List<MerchantFile> queryFileByFileName(String fileName) {
        return merchantFileMapper.queryFileByName(fileName);
    }

    /**
     * 删除文件
     *
     * @param fileId
     */
    @Transactional(transactionManager = "transactionManager")
    public Response deleteNewFileById(HttpServletRequest request,Long fileId) {
        try {
            MerchantFile merchantFile = merchantFileMapper.load(fileId);
            if (merchantFile == null) {
                return Response.returnFail(ResponseEnum.DATA_NOT_EXIST);
            }
            if (merchantFile.getStatus() == 2L) {
                String[] filename = merchantFile.getFtpFileName().split(",");
                for (String f : filename) {
                    FtpUtil.deleteFile(host, port, username, password, merchantFile.getFilePath(), f);
                }
            }
            merchantFileMapper.delete(fileId);
            /**
             *  添加系统日志
             * */
            String userId = request.getHeader("user-id");
            String username = request.getHeader("merchantName");
            String ip = IPUtils.getIpAddr(request);
            String language = request.getHeader("language");
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add("-");
            vo.getBeforeValues().add(merchantFile.getFileName());
            vo.getAfterValues().add("-");
            merchantLogService.saveLog(MerchantLogPageEnum.SET_TASK_MANAGER_EXPORT_58, MerchantLogTypeEnum.DEL_INFO, vo,
                    MerchantLogConstants.MERCHANT_IN, userId, username, userId, username, userId, language, ip);
        } catch (Exception e) {
            log.error("删除文件异常！" + fileId, e);
            return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 删除文件
     *
     * @param fileId
     */
    @Transactional(transactionManager = "transactionManager")
    public Response deleteFileById(Long fileId) {
        try {
            MerchantFile merchantFile = merchantFileMapper.load(fileId);
            if (merchantFile == null) {
                return Response.returnFail(ResponseEnum.DATA_NOT_EXIST);
            }
            if (merchantFile.getStatus() == 2L) {
                String[] filename = merchantFile.getFtpFileName().split(",");
                for (String f : filename) {
                    FtpUtil.deleteFile(host, port, username, password, merchantFile.getFilePath(), f);
                }
            }
            merchantFileMapper.delete(fileId);
        } catch (Exception e) {
            log.error("删除文件异常！" + fileId, e);
            return Response.returnFail(ResponseEnum.SYSTEM_ERROR);
        }
        return Response.returnSuccess();
    }

    /**
     * 查询进度
     *
     * @param fileId
     * @return
     */
    public Response queryFileRate(Long fileId) {
        MerchantFile merchantFile = merchantFileMapper.load(fileId);
        if (merchantFile == null) {
            return Response.returnFail(ResponseEnum.DATA_NOT_EXIST);
        }
        return Response.returnSuccess(merchantFile);
    }

    /**
     * 查询进度
     *
     * @return
     */
    public Response queryFileRate(MerchantFileVo merchantFileVo) {
        List<MerchantFile> merchantFile = merchantFileMapper.queryFileRate(merchantFileVo);
        if (merchantFile == null) {
            return Response.returnFail(ResponseEnum.DATA_NOT_EXIST);
        }
        return Response.returnSuccess(merchantFile);
    }

    /**
     * 重置状态
     *
     * @return
     */
    public void resetFileStatus() {
        merchantFileMapper.resetFileStatus();
    }

    /**
     * 下载文件
     *
     * @param fileId
     * @param response
     */
    public void downLoadFile(Long fileId, HttpServletResponse response) {
        try {
            MerchantFile merchantFile = merchantFileMapper.load(fileId);
            log.info("查询可下载文件:" + merchantFile);
            if (merchantFile == null) {
                return;
            }
            log.info("下载参数 host ={} param = {}", host, JSON.toJSONString(merchantFile));
            if (merchantFile.getFtpFileName().indexOf(",") > 0) {
                FtpUtil.downFile(host, port, username, password, merchantFile.getFilePath(), merchantFile, response);
            }else {
                FtpUtil.downFile(host, port, username, password, merchantFile.getFilePath(), merchantFile.getFileName(), merchantFile.getFtpFileName(), response);
            }
        } catch (Exception e) {
            log.error("文件导出异常 fileId=" + fileId, e);
        }
    }

    /**
     * 提交创建文件任务
     *
     * @param fileVo
     */
    @Transactional(transactionManager = "transactionManager")
    public MerchantFile saveFile(MerchantFile fileVo) {
        if (merchantFileMapper.insert(fileVo) > 0) {
            return fileVo;
        }
        return null;
    }

    /**
     *  导出文件任务
     * @param tagName  提示下载信息
     * @param merchantCode  商户编码
     * @param operateName  操作人
     * @param param  请求参数
     * @param pageName  下载文件名
     * @param exportBean  导出bean名称实际操作类
     * @param dataSize  导出文件大小  数量太大会分成多个文件
     * @return
     */
    public MerchantFile saveFileTask(String tagName, String merchantCode, String operateName, String param, String pageName, String exportBean, Integer dataSize) {
        if (merchantFileMapper.queryListInitCount(operateName) > 0) {
            throw new RuntimeException("请等待其他任务处理完成后再点击导出！");
        }
        List<MerchantFile> taskList;
        if (StringUtils.isNotEmpty(merchantCode)) {
            taskList = merchantFileMapper.queryExistTask(merchantCode);
        } else {
            taskList = merchantFileMapper.queryInnerExistTask();
        }
        log.info(merchantCode + "查询是否存在相同条件的任务:" + taskList);
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (MerchantFile task : taskList) {
                String tempParam = task.getExportParam();
                if (tempParam.equals(param)) {
                    log.error(merchantCode + "已存在相同条件的任务!");
                    throw new RuntimeException("贵方有相同条件的任务在执行中,请等待其他任务处理完成后再点击导出！");
                }
            }
        }
        MerchantFile merchantFile = new MerchantFile();
        //创建导出任务
        String ftpFileName = "";
        String fileName = "";
        if (dataSize != null && dataSize > SPLIT_FILE_DATE_SIZE){
            Integer fileLine = SPLIT_FILE_DATE_SIZE;
            int size = (dataSize % fileLine == 0) ? (dataSize / fileLine) : (dataSize / fileLine + 1);
            for (int i = 0;i < size ; i++){
                if (StringUtils.isEmpty(fileName)){
                    fileName = tagName  + new Date().getTime() + i + ".csv.zip";
                }
                if (StringUtils.isNotEmpty(ftpFileName)){
                    ftpFileName = ftpFileName + "," +new Date().getTime() + i + ".csv.zip";
                }else {
                    ftpFileName =  new Date().getTime() + i + ".csv.zip";
                }

            }
        }else {
            ftpFileName = new Date().getTime()  + ".csv.zip";
            fileName = tagName + ftpFileName;
        }
        merchantFile.setMerchantCode(merchantCode);
        merchantFile.setFileName(fileName);
        merchantFile.setFtpFileName(ftpFileName);
        merchantFile.setDataSize(dataSize == null ? 0 : dataSize);
        merchantFile.setExportParam(param);
        merchantFile.setPageName(pageName);
        merchantFile.setExportBean(exportBean);
        merchantFile.setOperatName(operateName);
        merchantFile.setFilePath(basePath +
                (StringUtils.isEmpty(merchantCode) ? "system" : merchantCode) + "/" + DateTimeUtils.getBeforeDateYYMMDD() + "/");
        merchantFile.setCreateTime(System.currentTimeMillis());
        saveFile(merchantFile);
        //小文件 异步并发执行
        OrderFileExportService exportService = SpringUtils.getBean(merchantFile.getExportBean());
        log.info("OrderFileExportService"+exportService);
        if (exportService != null) {
            log.info("OrderFileExportService:"+merchantFile);
            exportService.export(merchantFile);
        }
        return merchantFile;
    }

    public MerchantFile saveFileTask2(String tagName, String merchantCode, String operateName, String param, String pageName, String exportBean, Integer dataSize) {
        List<MerchantFile> taskList;
        if (StringUtils.isNotEmpty(merchantCode)) {
            taskList = merchantFileMapper.queryExistTask(merchantCode);
        } else {
            taskList = merchantFileMapper.queryInnerExistTask();
        }
        log.info(merchantCode + "查询是否存在相同条件的任务:" + taskList);
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (MerchantFile task : taskList) {
                String tempParam = task.getExportParam();
                if (tempParam.equals(param)) {
                    log.error(merchantCode + "已存在相同条件的任务!");
                    throw new RuntimeException("贵方有相同条件的任务在执行中,请等待其他任务处理完成后再点击导出！");
                }
            }
        }
        MerchantFile merchantFile = new MerchantFile();
        //创建导出任务
        String ftpFileName = "";
        String fileName = "";
        if (dataSize != null && dataSize > SPLIT_FILE_DATE_SIZE_2){
            Integer fileLine = SPLIT_FILE_DATE_SIZE_2;
            int size = (dataSize % fileLine == 0) ? (dataSize / fileLine) : (dataSize / fileLine + 1);
            for (int i = 0;i < size ; i++){
                if (StringUtils.isEmpty(fileName)){
                    fileName = tagName  +"_"+ i + ".csv.zip";
                }
                if (StringUtils.isNotEmpty(ftpFileName)){
                    ftpFileName = ftpFileName + "," +new Date().getTime() + i + ".csv.zip";
                }else {
                    ftpFileName =  new Date().getTime() + i + ".csv.zip";
                }

            }
        }else {
            ftpFileName = new Date().getTime()  + ".csv.zip";
            fileName = tagName + ".csv.zip";
        }
        merchantFile.setMerchantCode(merchantCode);
        merchantFile.setFileName(fileName);
        merchantFile.setFtpFileName(ftpFileName);
        merchantFile.setDataSize(dataSize == null ? 0 : dataSize);
        merchantFile.setExportParam(param);
        merchantFile.setPageName(pageName);
        merchantFile.setExportBean(exportBean);
        merchantFile.setOperatName(operateName);
        merchantFile.setFilePath(basePath +
                (StringUtils.isEmpty(merchantCode) ? "system" : merchantCode) + "/" + DateTimeUtils.getBeforeDateYYMMDD() + "/");
        merchantFile.setCreateTime(System.currentTimeMillis());
        saveFile(merchantFile);
        //小文件 异步并发执行
        OrderFileExportService exportService = SpringUtils.getBean(merchantFile.getExportBean());
        if (exportService != null) {
            exportService.export(merchantFile);
        }
        return merchantFile;
    }

    /**
     * 提交创建文件任务
     *
     * @param
     */
    public MerchantFile saveFileTask(String tagName, String merchantCode, String operateName, String param, String pageName, String exportBean) {
        return saveFileTask(tagName, merchantCode, operateName, param, pageName, exportBean, null);
    }

    /**
     * 更新文件进度
     *
     * @param fileId
     * @param rate
     */
    @Transactional(transactionManager = "transactionManager")
    public void updateFileRate(Long fileId, Long rate) {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setId(fileId);
        if (rate > 100L) {
            rate = 99L;
        }
        merchantFile.setExportRate(rate);
        merchantFileMapper.update(merchantFile);
    }

    /**
     * 更新文件状态与大小
     *
     * @param fileId
     * @param size
     */
    @Transactional(transactionManager = "transactionManager")
    public void updateFileSizeAndStatus(Long fileId, Integer size) {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setId(fileId);
        long s = (size / 1024);
        if (s < 1) {
            s = 1;
        }
        merchantFile.setFileSize(s);
        merchantFile.setEndTime(System.currentTimeMillis());
        merchantFileMapper.update(merchantFile);
    }

    /**
     * 更新文件状态与大小
     *
     * @param fileId
     */
    @Transactional(transactionManager = "transactionManager")
    public void updateFileEnd(Long fileId) {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setId(fileId);
        merchantFile.setExportRate(100L);
        merchantFile.setStatus(2L);
        merchantFile.setEndTime(System.currentTimeMillis());
        merchantFileMapper.update(merchantFile);
    }

    /**
     * 更新文件状态与大小
     *
     * @param fileId
     */
    @Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
    public void updateFileFail(Long fileId, String remark) {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setId(fileId);
        merchantFile.setFileSize(0L);
        merchantFile.setExportRate(0L);
        merchantFile.setStatus(3L);
        if (StringUtils.isNotEmpty(remark)) {
            if (remark.length() > 200) {
                remark = remark.substring(0, 200);
            }
            merchantFile.setRemark(remark);
        }
        merchantFile.setEndTime(System.currentTimeMillis());
        merchantFileMapper.update(merchantFile);
    }

    /**
     * 更新文件状态与大小
     *
     * @param fileId
     */
    @Transactional(transactionManager = "transactionManager")
    public void updateFileStatus(Long fileId) {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setId(fileId);
        merchantFile.setStatus(1L);
        merchantFileMapper.update(merchantFile);
    }

    /**
     * 更新文件状态与大小
     *
     * @param fileId
     */
    @Transactional(transactionManager = "transactionManager")
    public void updateFileStatusInit(Long fileId) {
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setId(fileId);
        merchantFile.setStatus(0L);
        merchantFileMapper.update(merchantFile);
    }

    public Long getLastNum() {
        return merchantFileMapper.getLastNum() + 1;
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public MerchantFile loadById(Long id) {
        return merchantFileMapper.load(id);
    }


    /**
     * 根据Id查询
     *
     * @return
     */
    public MerchantFile loadExecuteById() {
        return merchantFileMapper.loadExecuteById();
    }

    /**
     * 根据Id查询
     *
     * @return
     */
    public MerchantFile loadInitMaxTaskById() {
        return merchantFileMapper.loadInitMaxTaskById();
    }

    /**
     * 根据Id查询
     *
     * @return
     */
    public List<MerchantFile> loadInitTaskById() {
        return merchantFileMapper.loadInitTaskById();
    }

    public Integer getExportDataSize(){
        return this.exportDataSize;
    }

    public MerchantFile saveExportTask(String operator, String fileName, String params,
                                       String exportBean) {
        Integer curCount = merchantFileMapper.queryListInitCount(operator);
        if (curCount > 0) {
            throw new RuntimeException("请等待其他任务处理完成后再点击导出!");
        }
        MerchantFile merchantFile = new MerchantFile();
        merchantFile.setFileName(fileName);
        merchantFile.setFileSize(1L);
        merchantFile.setCreateTime(System.currentTimeMillis());
        merchantFile.setOperatName(operator);
        merchantFile.setPageName("盲盒领取记录-导出数据");
        merchantFile.setExportRate(0L);
        merchantFile.setExportParam(params);
        merchantFile.setExportBean(exportBean);
        merchantFile.setStatus(0L);
        merchantFile.setRemark("");
        merchantFile.setFilePath(basePath + "system/" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        merchantFile.setFtpFileName(fileName);
        merchantFileMapper.insert(merchantFile);
        return merchantFile;
    }
}
