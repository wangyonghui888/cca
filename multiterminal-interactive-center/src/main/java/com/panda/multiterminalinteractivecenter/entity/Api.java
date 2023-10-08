package com.panda.multiterminalinteractivecenter.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Api implements Serializable {

    @JSONField(name = "api")
    private List<String>  api;

}
