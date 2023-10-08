package com.panda.sport.merchant.common.vo.merchant;

import lombok.Data;

import java.io.Serializable;

@Data
public class AbnormalInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String riskType;

    /**
     * 操作时间
     */
    private Long operateTime;

    /**
     *
     */
    private Integer tagType;

    /**
     *
     */
    private Integer changeTag;

    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String  merchantCode;

    /**
     *
     */
    private String changeTagName;


    private String userName;

    private  String userId;

    private String remark;
}
