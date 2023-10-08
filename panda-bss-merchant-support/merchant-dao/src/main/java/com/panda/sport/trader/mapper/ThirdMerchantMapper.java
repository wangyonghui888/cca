package com.panda.sport.trader.mapper;

import com.panda.sport.merchant.common.po.trader.Merchant;
import com.panda.sport.merchant.common.vo.ThirdMerchantVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-27
 */
@Mapper
@Repository
public interface ThirdMerchantMapper {

    List<Merchant> getMerchant(@Param("tag") Integer tag, @Param("account") String account);

    List<ThirdMerchantVo> getMerchantList();

    List<ThirdMerchantVo> getTblMerchantByCodes(@Param("merchantCodes") List<String> merchantCodes);
}
