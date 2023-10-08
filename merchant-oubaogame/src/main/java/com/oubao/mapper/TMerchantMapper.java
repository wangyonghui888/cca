package com.oubao.mapper;

import com.oubao.po.MerchantPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TMerchantMapper {

    void insertMerchant(@Param("merchantCode") String merchantCode, @Param("merchantKey") String merchantKey, @Param("transferMode") Integer transferMode);

    void updateMerchant(@Param("merchantCode") String merchantCode, @Param("merchantKey") String merchantKey, @Param("transferMode") Integer transferMode);

    @Select("select transfer_mode from t_merchant where merchant_code=#{merchantCode}")
    Integer getTransferMode(@Param("merchantCode") String merchantCode);

    MerchantPO getBssMerchant(@Param(value = "merchantCode") String merchantCode);

    MerchantPO getMerchant(@Param(value = "merchantCode") String merchantCode);
}
