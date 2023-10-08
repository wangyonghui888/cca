package com.panda.sport.order.export;

import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.order.service.expot.AbstractOrderExportServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  注单导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service("orderMaxExportServiceImpl")
public class OrderMaxExportServiceImpl extends AbstractOrderExportServiceImpl {

    @Override
    @Async("asyncServiceExecutor")
    public void export(MerchantFile merchantFile) {
        this.execute(merchantFile);
    }

}
