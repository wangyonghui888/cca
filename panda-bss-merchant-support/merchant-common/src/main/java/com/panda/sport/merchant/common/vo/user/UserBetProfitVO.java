package com.panda.sport.merchant.common.vo.user;


import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderMonthPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderWeekPO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class UserBetProfitVO implements Serializable {

    private Map<String,Object> fourteenDay;

    private Map<String,Object> threeMonth;

    private Map<String,Object> sixMonth;
}
