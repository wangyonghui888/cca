package com.panda.center.mapper.trader;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.center.entity.Merchant;
import com.panda.center.vo.MerchantTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-27
 */
public interface MerchantMapper extends BaseMapper<Merchant> {

    List<MerchantTree> getMerchantTree(@Param("tag") Integer tag, @Param("account") String account);
}
