package com.panda.sport.merchant.manage.service;


import com.panda.sport.merchant.common.bo.DiffNoticeBO;

import java.util.List;

/**
 * 異常數據發送芒果通知
 *  * @author ifan
 *  * @since 2022-06-28
 */
public interface SendMangoNoticeService {

    /**
     * 发送异常通知
     * @param diffList
     * @param title
     */
    void sendDiffNotice(List<DiffNoticeBO> diffList,String title);
}
