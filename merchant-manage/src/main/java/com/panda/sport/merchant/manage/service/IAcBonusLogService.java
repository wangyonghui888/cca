package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.AcBonusLogPO;
import com.panda.sport.merchant.common.vo.AcBonusLogParam;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 优惠券领取日志表 服务类
 * </p>
 *
 * @author baylee
 * @since 2021-08-26
 */
public interface IAcBonusLogService extends IService<AcBonusLogPO> {

    Response<?> pageList(AcBonusLogParam param);

    Response<?> export(AcBonusLogParam param, HttpServletResponse response);
}
