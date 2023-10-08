package com.panda.sport.merchant.manage.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.backup.mapper.BackupLuckyboxHisMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.common.utils.ZipFilesUtils;
import com.panda.sport.merchant.common.vo.activity.LuckyboxRecordsVO;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.expot.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 盲盒领取记录接口
 */
@Slf4j
@Service
public class SLuckyboxRecordsServiceImpl{

    @Autowired
    private BackupLuckyboxHisMapper luckyboxHisMapper;

    @Resource
    private MerchantFileService merchantFileService;

    @Resource
    protected FtpProperties ftpProperties;

    /**
     * 盲盒查询记录导出
     * @return
     */
    public List<LuckyboxRecordsVO> queryLuckyBoxHistoryExcel(LuckyboxRecordsVO parmVO) {
        int current =Integer.valueOf(parmVO.getPage().getCurrent()+"");
        int size=Integer.valueOf(parmVO.getPage().getSize()+"");
        Page<LuckyboxRecordsVO> page = PageHelper.startPage(current, size, false);
        //最多导出6万条
        page.setPageSize(60000);
        if (parmVO.getUserName() != null && parmVO.getUserName().length() > 9 && StringUtils.isNumeric(parmVO.getUserName())) {
            //支持一个输人框支持ID 和人名查询
            parmVO.setUid(parmVO.getUserName());
        }
        List<LuckyboxRecordsVO> data = luckyboxHisMapper.queryLuckyBoxHistory(parmVO);

        return data;
    }

    /**
     * 盲盒历史记录查询
     * @return
     */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page  queryLuckyBoxHistory(LuckyboxRecordsVO parmVO) {
        int current =Integer.valueOf(parmVO.getPage().getCurrent()+"");
        int size=Integer.valueOf(parmVO.getPage().getSize()+"");
        Page<LuckyboxRecordsVO> page = PageHelper.startPage(current, size, true);
        if (parmVO.getUserName() != null && parmVO.getUserName().length() > 9 && StringUtils.isNumeric(parmVO.getUserName())) {
            //支持一个输人框支持ID 和人名查询
            parmVO.setUid(parmVO.getUserName());
        }
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page1=new com.baomidou.mybatisplus.extension.plugins.pagination.Page();

        List<LuckyboxRecordsVO> pageData = luckyboxHisMapper.queryLuckyBoxHistory(parmVO);
        log.info("接口返回数据总行数{},data,size{},",page.getTotal(),pageData.size());
        page1.setTotal(page.getTotal());
        page1.setRecords(page.getResult());
        page1.setCurrent(current);
        page1.setSize(size);
        return page1;
    }

    @Async("asyncServiceExecutor")
    public void asyncExport(MerchantFile merchantFile) {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            if (merchantFileService.loadById(merchantFile.getId()) == null) {
                log.info("当前任务被删除！");
                return;
            }
            merchantFileService.updateFileStatus(merchantFile.getId());
            log.info("开始导出盲盒领取记录 param = {}", merchantFile.getExportParam());
            LuckyboxRecordsVO luckyboxRecordsVO = JSONObject.parseObject(merchantFile.getExportParam(),
                    LuckyboxRecordsVO.class);
            List<LuckyboxRecordsVO> dataList = this.queryLuckyBoxHistoryExcel(luckyboxRecordsVO);
            if (CollectionUtils.isEmpty(dataList)) {
                throw new Exception("未查询到数据");
            }
            inputStream = new ByteArrayInputStream(exportToCsv2(dataList));
            uploadFile(merchantFile, inputStream);
            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            merchantFileService.updateFileFail(merchantFile.getId(), e.getMessage());
            log.error("导出盲盒领取记录!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("流关闭异常！");
                }
            }
        }
    }
    private byte[] exportToCsv2( List<LuckyboxRecordsVO> dataList) {
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(dataList) ? 0 : dataList.size());

        //商户名称	商户ID	用户名称	用户ID	盲盒类型	奖品名称	金额	消耗奖卷数	领取状态	领取时间
        int i = 0;
        for (LuckyboxRecordsVO boxRecordsVO : dataList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            int index=0;
            rowData.put(String.valueOf(index++), i);
            rowData.put(String.valueOf(index++), boxRecordsVO.getMerchantName());
            rowData.put(String.valueOf(index++), boxRecordsVO.getMerchantId().concat("\t"));
            rowData.put(String.valueOf(index++), boxRecordsVO.getUserName());
            rowData.put(String.valueOf(index++), boxRecordsVO.getUid().concat("\t"));
            rowData.put(String.valueOf(index++), boxRecordsVO.getBoxName());
            // rowData.put(String.valueOf(index++), boxRecordsVO.getAwardStr());
            rowData.put(String.valueOf(index++), boxRecordsVO.getAward());
            rowData.put(String.valueOf(index++), boxRecordsVO.getUseToken());
            rowData.put(String.valueOf(index++), boxRecordsVO.getStatus());
            rowData.put(String.valueOf(index), boxRecordsVO.getCreateTime());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        int index = 0;
        header.put(String.valueOf(index++), "序号");
        header.put(String.valueOf(index++), "商户名称");
        header.put(String.valueOf(index++), "商户ID");
        header.put(String.valueOf(index++), "用户名称");
        header.put(String.valueOf(index++), "用户ID");
        header.put(String.valueOf(index++), "盲盒类型");
        // header.put(String.valueOf(index++), "奖品名称");
        header.put(String.valueOf(index++), "金额");
        header.put(String.valueOf(index++), "消耗奖卷数");
        header.put(String.valueOf(index++), "领取状态");
        header.put(String.valueOf(index), "领取时间");
        return CsvUtil.exportCSV(header, exportData);
    }
    private void uploadFile(MerchantFile merchantFile, InputStream inputStream) throws IOException {
        try (InputStream zip = ZipFilesUtils.zip(inputStream, merchantFile.getFileName())) {
            merchantFileService.updateFileSizeAndStatus(merchantFile.getId(), zip.available());
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()),
                    ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(),
                    merchantFile.getFtpFileName(), zip);
            merchantFileService.updateFileEnd(merchantFile.getId());
        } catch (Exception e) {
            log.error("导出上传异常！", e);
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}