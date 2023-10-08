package com.panda.sport.merchant.manage.mq.vo;
/**
 * @author Administrator
 * @date 2021/4/17
 * @TIME 12:08
 */

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TagMarketMsg
 * @Description TODO
 * @Author Administrator
 * @Date 2021/4/17 12:08
 */
@Data
public class TagMarketMsg implements Serializable {
    String businessId;

    Integer tagMarketStatus;

    Integer tagMarketLevelId;

    Integer tagMarketLevelIdPc;

}
