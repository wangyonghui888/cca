package com.panda.multiterminalinteractivecenter.service;



import com.alibaba.fastjson.JSONObject;

public interface OssService {

    boolean ossUpload(JSONObject obj);


    void checkOssDomain(String ossText);

}
