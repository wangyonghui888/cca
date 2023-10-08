package com.panda.sport.merchant.common.po.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * merchant_file
 *
 * @author duwan 2020-12-08
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class MerchantFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 逻辑id
     */
    private Long id;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 操作人
     */
    private String operatName;

    /**
     * 来源页面
     */
    private String pageName;


    /**
     * 导出进度
     */
    private Long exportRate;
    /**
     * 导出参数
     */
    private String exportParam;

    /**
     * 导出来源类型
     */
    private String exportBean;

    /**
     * 导出状态|0 未开始 |1 处理中 |2 文件已准备 |3 失败
     */
    private Long status;

    /**
     * 导出备注
     */
    private String remark;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * ftp文件名
     */
    private String ftpFileName;

    /**
     * 数据总数
     */
    private Integer dataSize;

    public MerchantFile() {
    }

}
