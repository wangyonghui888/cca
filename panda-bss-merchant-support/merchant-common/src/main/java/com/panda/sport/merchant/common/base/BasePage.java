package com.panda.sport.merchant.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.panda.sport.merchant.common.constant.DatabaseCommonColumnToStr;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author :  ives
 * @Description :  分页基本类
 * @Date: 2022-02-13 14:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePage implements Serializable {

    private static final long serialVersionUID = -6498592228454284015L;

    @ApiModelProperty(value = "第几页", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示的记录数", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "总记录数", example = "0")
    private Integer total;

    @ApiModelProperty(value = "总页数", example = "0")
    @JsonIgnore
    private Integer totalPage;

    @ApiModelProperty(value = "起始记录数", example = "0")
    @JsonIgnore
    private Integer start;

    @ApiModelProperty(value = "排序字段", example = "id")
    @JsonIgnore
    private String orderField;

    @ApiModelProperty(value = "排序类型", example = "DESC")
    @JsonIgnore
    private String orderType;

    public int getPageNum() {
        if (pageNum == null || pageNum <= 0){
            pageNum = 1;
        }
        return pageNum;
    }

    public int getPageSize() {
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        return pageSize;
    }

    public Integer getTotal() {
        if (total == null || total <= 0){
            total = 0;
        }
        return total;
    }

    public Integer getTotalPage() {
        totalPage = (getTotal() / getPageSize()) + ((getTotal() % getPageSize()) > 0 ? 1 : 0);
        return totalPage;
    }

    public int getStart() {
        start = ( getPageNum() - 1 ) * getPageSize();
        return start;
    }

    public String getOrderField() {
        if (StringUtils.isBlank(orderField)){
            orderField = DatabaseCommonColumnToStr.ID;
        }
        return orderField;
    }

    public String getOrderType() {
        if (StringUtils.isBlank(orderType)){
            orderType = DatabaseCommonColumnToStr.DESC;
        }
        return orderType;
    }
}
