package com.panda.center.export;


import com.panda.center.entity.MerchantFile;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  商户订单接口
 * @Date: 2020-12-11 10:40:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface OrderFileExportService {

    /**
     * 文件导出接口
     * @param merchantFile
     */
    void export(MerchantFile merchantFile);

}
