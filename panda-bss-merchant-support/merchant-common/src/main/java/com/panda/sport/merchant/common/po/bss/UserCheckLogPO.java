package com.panda.sport.merchant.common.po.bss;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCheckLogPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 提交商户
     */
    private String merchantCode;

    private String uid;

    private String userName;

    /**
     * 协查操作人
     */
    private String checkUser;

    /**
     * 协查时间
     */
    private Long checkTime;

    /**
     * 协查原因
     */
    private String checkReason;
    /**
     * 协查补充说明
     */
    private String checkExplain;

    /**
     * 提交人
     */
    private String submitUser;
    /**
     * 0是待协查 1是已完成
     */
    private Integer status;

    /**
     * 协查结果
     */
    private String checkResult;
    /**
     * 协查结果详情
     */
    private String resultExplain;


    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 创建时间
     */
    private Long createTime;

}