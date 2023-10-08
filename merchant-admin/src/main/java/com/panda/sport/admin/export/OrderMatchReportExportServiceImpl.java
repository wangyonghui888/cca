package com.panda.sport.admin.export;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.merchant.MerchantMatchBetVo;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  赛事报表导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("orderMatchReportExportServiceImpl")
@Slf4j
public class OrderMatchReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient rpcClient;

    @Override
    @Async()
    public void export(MerchantFile merchantFile){

        long startTime = System.currentTimeMillis();
        InputStream inputStream = null;
        try {
            if (super.checkTask(merchantFile.getId())){
                log.info("当前任务被删除！");
                return;
            }
            super.updateFileStart(merchantFile.getId());

            log.info("开始赛事投注报表导出 param = {}",merchantFile.getExportParam());

            MerchantMatchBetVo sportVO = JSON.parseObject(merchantFile.getExportParam(), MerchantMatchBetVo.class);
            List<?> resultList = null;
            if (CollectionUtil.isEmpty(sportVO.getMerchantCodeList())){
                Map<String,Object> result = rpcClient.queryMatchStatisticListNew(sportVO);
                if (result != null) {
                    resultList = (List<?>) result.get("list");
                }
            }else {
                Map<String,Object> result = rpcClient.queryMatchStatisticListNew(sportVO);
                if (result != null) {
                    resultList = (List<?>) result.get("list");
                }
            }
            if (CollectionUtils.isEmpty(resultList)) {
                throw new Exception("未查询到数据");
            }

            inputStream =  new ByteArrayInputStream(exportMatchToCsv(resultList, sportVO.getLanguage()));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}" ,(System.currentTimeMillis() - startTime));
        }catch (Exception e){
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("赛事投注统计报表异常!", e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    log.error("流关闭异常！");
                }
            }
        }
    }
}
