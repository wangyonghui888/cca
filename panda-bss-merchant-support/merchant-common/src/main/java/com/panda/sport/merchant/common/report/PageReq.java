package com.panda.sport.merchant.common.report;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageReq {

    @ApiModelProperty(value = "当前第几页")
    private Integer page;

    @ApiModelProperty(value = "多少条数据")
    private Integer pageSize;

    private Integer rows;

    @ApiModelProperty(name = "offset",value = "偏移总量")
    private Integer offsetCount;

    public Integer getPage(){
        if(page == null){
            page = 1;
        }
        return page;
    }

    public Integer getPageSize(){
        if(pageSize == null){
            pageSize = 10;
        }
        return pageSize;
    }

    public Integer getRows(){
        if(rows == null){
            rows = 0;
        }
        return rows;
    }

    public Integer getOffsetCount(){
        if(offsetCount == null){
            offsetCount = 0;
        }
        return offsetCount;
    }
}