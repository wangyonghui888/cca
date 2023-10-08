package com.panda.sport.merchant.api.service;

public interface IMongoMsgService {
    void send(String text ,String targetName,String userId,String userToken);
}
