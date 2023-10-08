package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.bss.CurrencyRatePO;
import com.panda.sport.merchant.common.po.merchant.MerchantRatePO;
import com.panda.sport.merchant.common.vo.merchant.MerchantRateVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRateMapper {

    int deleteByPrimaryKey(String id);

    int insert(MerchantRatePO record);

    Long selectMaxId();

    int insertSelective(MerchantRatePO record);

    List<MerchantRatePO> selectByRateVO(MerchantRateVO merchantRateVO);


    MerchantRatePO selectById(Long id);

    int updateByPrimaryKeySelective(MerchantRatePO record);

    int updateByPrimaryKey(MerchantRatePO record);

    @Select("select r.id,computing_standard computingStandard,range_amount_begin rangeAmountBegin,\n" +
            "range_amount_end rangeAmountEnd,terrace_rate terraceRate,payment_cycle paymentCycle,r.remarks\n" +
            "from t_merchant_rate r ")
    List<MerchantRatePO> queryRateList();

}