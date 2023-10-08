package com.panda.sport.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.OrderTimesSettleInfoPO;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoReqVO;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 订单多次结算账变 服务类
 * </p>
 *
 * @author amos
 * @since 2022-05-22
 */
public interface OrderTimesSettleInfoService extends IService<OrderTimesSettleInfoPO> {

    Response pageList(OrderTimesSettleInfoReqVO pageReqVO);

    Map<String, Object>  orderTimeSettleExport(String language,OrderTimesSettleInfoReqVO pageReqVO, String userName);

    void exportInfomationOf2TimesOrder(HttpServletResponse response, String ymd);
}
