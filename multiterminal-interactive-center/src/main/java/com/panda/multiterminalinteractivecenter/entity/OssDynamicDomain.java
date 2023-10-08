package com.panda.multiterminalinteractivecenter.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;


@Data
public class OssDynamicDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    @JSONField(name = "img")
    private List<String> img;

    @JSONField(name = "chatroomUrl")
    private String chatroomUrl;

    @JSONField(name = "chatroomHttpUrl")
    private String chatroomHttpUrl;

    @JSONField(name = "static")
    private List<String> quiescent;

    @JSONField(name = "file_name")
    private String fileName;

    @JSONField(name = "GAB")
    private Api gab;

    @JSONField(name = "type")
    private String type;

    @JSONField(name = "testFileUpload")
    private Opt testFileUpload;


    @JSONField(name = "update_time")
    private String updateTime;

    @JSONField(name = "live_domains")
    private LiveDomains liveDomains;

    @JSONField(name = "GAS")
    private Api gas;

    @JSONField(name = "api")
    private List<String> api;

    @JSONField(name = "update_by")
    private String updateBy;

    @JSONField(name = "GAY")
    private Api gay;


    @JSONField(name = "GACOMMON")
    private Api common;
}
