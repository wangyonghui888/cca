package com.panda.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.center.entity.OlympicLuckyboxRecords;
import com.panda.center.param.LuckyBoxParam;
import com.panda.center.result.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 奥运拆盒历史记录 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-26
 */
public interface IOlympicLuckyboxRecordsService extends IService<OlympicLuckyboxRecords> {

    Response<?> queryLuckyBoxHistory(LuckyBoxParam luckyBoxParam);

    Response<?> queryLuckyBoxHistoryExcel(LuckyBoxParam luckyBoxParam, HttpServletResponse response);
}
