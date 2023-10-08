package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.Response;

import java.io.IOException;

public interface AccountRecordService {


    /**
     * 执行按日上传账号交易记录与账变记录
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @throws IOException
     */
    void accountRecordUpload(long startTime, long endTime) throws IOException;

}
