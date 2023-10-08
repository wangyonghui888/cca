package com.panda.multiterminalinteractivecenter.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class LiveDomains implements Serializable {

    @JSONField(name = "pc")
    private String  pc;

    @JSONField(name = "end")
    private String  end;

    @JSONField(name = "h5")
    private String  h5;

}
