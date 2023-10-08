package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:23:21
 */
@Getter
@Setter
@ToString
public class OperationsVO implements Serializable {

    private static final long serialVersionUID = -6595547821561215863L;

    /**
     * id
     */
    private Long id;

    /**
     * 类型：1-,baner  2-图标
     */
    private Integer type;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 状态：0,-关，1-开启-预览, 2-开启-线上
     */
    private Integer status;
    /**
     * 测试用户
     */
    private String testUser;

    /**
     * 开始
     */
    private Integer start;

    /**
     * 页面大小
     */
    private Integer size;

    /**
     * 图片名称
     */
    private String imgName;

    private List<Long> ids;

    /**
     * 连接类型(0:无连接,1:内部导航,2:弹窗连接)
     */
    private String urlType;
}
