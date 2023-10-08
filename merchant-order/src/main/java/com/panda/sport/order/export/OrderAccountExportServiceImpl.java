package com.panda.sport.order.export;


import com.panda.sport.backup.mapper.BackupOrderMixMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;

import com.panda.sport.order.service.expot.AbstractOrderAccountExportServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  注单账变导出实现类
 * @Date: 2020-12-11 13:49:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Service("orderAccountExportServiceImpl")
@Slf4j
public class OrderAccountExportServiceImpl extends AbstractOrderAccountExportServiceImpl {

    @Autowired
    public BackupOrderMixMapper orderMixMapper;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {
        execute(merchantFile);
    }
}
