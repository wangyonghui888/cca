package com.panda.multiterminalinteractivecenter.vo;

import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-03-19 13:10:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class KickUserVo {

    private List<String> dataCodes;

    /**
     * 踢出用户类型
     * 1 按设备 2按用户 3按商户 4踢所有
     */
    private Integer kickType;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 数据权限编码
     */
    private String dataCode;

    /**
     * 商户编号
     */
    private String merchantCode;

    /**
     * 商户集合号
     */
    private String merchantCodes;

    /**
     * 参数
     */
    private String kickParam;

    /**
     * 位置
     */
    private Integer index;

}
