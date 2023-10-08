package com.panda.sport.order.service;

import com.panda.sport.merchant.common.vo.Response;

public interface OrderSearchService {


    Response<?> getSportIdByMatchManageId(String matchManageId);

}