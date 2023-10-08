package com.panda.multiterminalinteractivecenter.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Opt implements Serializable {

    @JSONField(name = "opt")
    private String  opt;

}
