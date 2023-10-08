package com.panda.multiterminalinteractivecenter.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ives
 * @description 维护记录表 DTO
 * @date 2022-03-25
 */
@Data
public class RecordListDTO implements Serializable {

    /**
     * 时间
     */
    private Long time;

    /**
     * 状态 0未有维护 1有维护
     */
    private Integer status;

    public RecordListDTO(){
        this.time = System.currentTimeMillis();
        this.status = 0;
    }

}
