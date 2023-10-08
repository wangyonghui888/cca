package com.panda.sport.merchant.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :  joken
 * @version 1.0
 * @Project Name :
 * @Package Name :
 * @Description : 多线程处理统一对象返回
 * @Date: 2019/9/26 22:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseThreadDTO {

    private String businessName;
    private String startTime;
    private String endTime;
    private String elapsedTime;

}
