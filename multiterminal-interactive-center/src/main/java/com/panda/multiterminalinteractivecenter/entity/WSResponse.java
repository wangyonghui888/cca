package com.panda.multiterminalinteractivecenter.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class WSResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    //状态码
    private Integer rt;
    //错误信息
    private String error;

    //调用ID
    private Integer txnid;

    private String type;

    private Result data;

    private String msg;

    @Data
    public static class Result implements Serializable {
        private static final long serialVersionUID = 1L;
        private String TaskId;

        private Integer NodeID;

        private Integer HttpCode;

        private Double TotalTime;

        private Integer FileSize;

        private Integer RealSize;

        private Double TTFBTime;

        private Float DownTime;

        private Double ConnectTime;

        private Float NsLookup;

        private String HttpHead;

        private String SrcIP;

        private Integer TotalCount;
        private Integer TotalCountOK;
        private Integer TotalCountErr;
        private Integer TotalCountTimeout;
    }
}
