package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author : ives
 * @Description :  对外商户配置服务类
 * @Date: 2022-01-24
 */
@Repository
public interface ExternalMerchantConfigMapper extends BaseMapper<MerchantConfig> {

}
