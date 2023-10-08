package com.panda.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.center.entity.AcBonusLog;
import com.panda.center.result.Response;
import com.panda.center.param.AcBonusLogParam;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 优惠券领取日志表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
public interface IAcBonusLogService extends IService<AcBonusLog> {
    Response<?> pageList(AcBonusLogParam param);

    Response<?> export(AcBonusLogParam param, HttpServletResponse response);
}
