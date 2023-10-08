package com.panda.multiterminalinteractivecenter.dto;

import lombok.Data;

/**
 * @author :  duwan
 * @Project Name :  test
 * @Package Name :  com.panda.multiterminalinteractivecenter.dto
 * @Description :  踢出用户输入参数类 , 实现用户踢出时优先异步实现，及时返回成功。
 * @Date: 2022-04-16 11:29:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class KickUserDto {

    /**
     * 踢出用户类型 非空
     * 1 按设备 2按用户 3按商户 4踢所有
     * 系统如果不支持某种踢出类型支持配置
     */
    private Integer type;

    /**
     * 对应彩票各个服务的唯一标识，需沟通确认
     */
    private String systemCode;

    /**manage/merchantDomainGroup/dj/findApiDomainLog
     * 踢出商户参数，支持多个按逗号 ,隔开。 踢全部时为空
     */
    private String merchantCodes;

    /**
     * 踢出用户参数，支持多个按逗号 ,隔开 可为空
     */
    private String userIds;

}
