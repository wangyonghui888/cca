package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.merchant.MerchantLevelPO;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.vo.merchant.MerchantLevelVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantLevelMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(MerchantLevelPO record);

    int insertMerchantLog(MerchantLogPO record);

    List<MerchantLevelPO> selectByLevelVO(MerchantLevelVO merchantRateVO);


    MerchantLevelPO selectById(@Param("id") String id);

    int updateByPrimaryKeySelective(MerchantLevelPO record);

    @Select("select r.id,computing_standard computingStandard,range_amount_begin rangeAmountBegin,\n" +
            "range_amount_end rangeAmountEnd,terrace_rate terraceRate,payment_cycle paymentCycle,l.vip_amount vipAmount,\n" +
            "l.vip_payment_cycle vipPaymentCycle,l.technique_amount techniqueAmount,l.technique_payment_cycle techniquePaymentCycle,r.remarks\n" +
            "from t_merchant_rate r left JOIN t_merchant_level l on l.rate_id=r.id where l.`level`=#{level}")
    MerchantLevelPO getMerchantLevel(@Param("level") Integer level);

    @Select("select count(0) from  t_merchant_level l  where l.`level`=#{level}")
    int countMerchantLevel(@Param("level") Integer level);

    @Select("select id,level,level_name levelName,rate_id rateId from t_merchant_level order by level asc")
    List<MerchantLevelPO> queryLevelList();
}