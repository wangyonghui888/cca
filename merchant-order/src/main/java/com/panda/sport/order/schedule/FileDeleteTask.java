package com.panda.sport.order.schedule;

import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.MerchantFileVo;
import com.panda.sport.merchant.mapper.MerchantFileMapper;
import com.panda.sport.order.service.MerchantFileService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@JobHandler(value = "FileDeleteTask")
public class FileDeleteTask extends IJobHandler {

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private MerchantFileMapper merchantFileMapper;

    //@Scheduled(cron = "0 0/2 0/1 * * ? ")
    //"0 0 * * *  ?"
    //@Scheduled(cron = "0 0 * * *  ?")
    //@Scheduled(cron = "0 0 12 * * ?")
/*    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() {

    }*/

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("FileDeleteTask开始执行删除文件");
        Calendar calc = Calendar.getInstance();
        try {
            calc.setTime(new Date());
            calc.add(Calendar.DATE, -30);
            Date minDate = calc.getTime();
            MerchantFileVo requestPageVO = new MerchantFileVo();
            requestPageVO.setCreatTime(minDate.getTime());
            List<MerchantFile> list = merchantFileMapper.queryListAll(requestPageVO);
            log.info("删除文件数量 {}", list.size());
            for (MerchantFile m : list) {
                merchantFileService.deleteFileById(m.getId());
            }
            calc.setTime(new Date());
            calc.add(Calendar.HOUR_OF_DAY, -3);
            Date failDate = calc.getTime();
            MerchantFileVo failParam = new MerchantFileVo();
            failParam.setStatus(0);
            failParam.setCreatTime(failDate.getTime());
            List<MerchantFile> failList = merchantFileMapper.queryListAll(failParam);
            for (MerchantFile m : failList) {
                merchantFileService.deleteFileById(m.getId());
            }
        } catch (Exception e) {
            log.error("FileDeleteTask文件删除异常！", e);
        }
        log.info("FileDeleteTask结束执行删除文件");
        return SUCCESS;
    }
}

