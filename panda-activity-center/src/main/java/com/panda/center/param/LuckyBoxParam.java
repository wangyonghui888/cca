package com.panda.center.param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panda.center.vo.LuckyBoxRecordsVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/12/26 17:17:56
 */
@Getter
@Setter
@ToString
public class LuckyBoxParam implements Serializable {
    /**
     * 商户ID
     */
    private String merchantId;
    /**
     * 用户名/ID 模糊
     */
    private String userName;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;

    private Page<LuckyBoxRecordsVO> page = new Page<>();
}

