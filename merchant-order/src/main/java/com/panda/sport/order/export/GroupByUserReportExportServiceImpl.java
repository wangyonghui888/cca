package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.order.feign.MerchantReportClient;
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
 * @author :  javier
 * --------  ---------  --------------------------
 */
@Service("groupByUserReportExportServiceImpl")
@Slf4j
public class GroupByUserReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private MerchantReportClient userReportClient;

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

            log.info("开始用户投注报表导出 param = {}",merchantFile.getExportParam());
            UserOrderVO vo  = JSON.parseObject(merchantFile.getExportParam(), UserOrderVO.class);

            final Map<String,Object> result = (Map<String,Object>)userReportClient.listUserBetGroupByUser(vo).getData();

            if (CollectionUtils.isEmpty((List<?>)result.get("list"))) {
                log.info("exportMerchantReport returnDate:is null");
                throw new Exception("未查询到数据");
            }
            inputStream =  new ByteArrayInputStream(groupByExportUsersToCsv((List<?>)result.get("list"), vo));

            super.uploadFile(merchantFile, inputStream);

            log.info("导出结束,花费时间: {}" ,(System.currentTimeMillis() - startTime));
        }catch (Exception e){
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("用户投注统计报表异常!", e);
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
