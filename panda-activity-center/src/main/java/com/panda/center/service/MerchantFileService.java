package com.panda.center.service;

import com.alibaba.fastjson.JSON;
import com.panda.center.enums.ResponseEnum;
import com.panda.center.export.OrderFileExportService;
import com.panda.center.result.Response;
import com.panda.center.utils.DateTimeUtils;
import com.panda.center.utils.FtpUtil;
import com.panda.center.utils.SpringUtils;
import com.panda.center.vo.PageVO;
import com.panda.center.mapper.merchant.MerchantFileMapper;
import com.panda.center.entity.MerchantFile;
import com.panda.center.vo.MerchantFileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    @Transactional
    public MerchantFile saveFile(MerchantFile fileVo) {
        if (merchantFileMapper.insert(fileVo) > 0) {
            return fileVo;
        }
        return null;
    }

    /**
     * 提交创建文件任务
     *
     * @param
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
    @Transactional
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
    @Transactional
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
    @Transactional
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional
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
    @Transactional
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
}
