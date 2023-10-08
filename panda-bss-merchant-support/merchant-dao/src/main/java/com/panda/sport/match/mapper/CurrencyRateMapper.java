package com.panda.sport.match.mapper;



import com.panda.sport.merchant.common.po.bss.CurrencyRatePO;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CurrencyRateMapper {
    /**
     * 汇率的
     * @return
     */
    List<CurrencyRatePO> queryCurrencyRateList();
}
