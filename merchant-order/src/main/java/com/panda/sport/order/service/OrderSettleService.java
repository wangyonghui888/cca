package com.panda.sport.order.service;


import java.io.IOException;

public interface OrderSettleService {

    void uploadSettleOrder(long startTime, long endTime, String codeParam) throws IOException;


    void uploadSettleOrder(long startTime, long endTime, String codeParam,Integer vipLevel) throws IOException;


    void uploadSettleOrderVip(long startTime, long endTime, String codeParam) throws IOException;


    void uploadSettleOrderAllVip(long startTime, long endTime, String codeParam) throws IOException;
}
