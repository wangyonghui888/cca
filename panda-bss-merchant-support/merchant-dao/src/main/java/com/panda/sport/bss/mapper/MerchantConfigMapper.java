package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.MerchantCodeConfig;
import com.panda.sport.merchant.common.po.bss.MerchantCodeConfigLog;
import com.panda.sport.merchant.common.po.bss.MerchantConfig;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * t_merchant_config
 *
 * @author duwan
 * @date 2021/02/03
 */
@Mapper
@Repository
public interface MerchantConfigMapper extends BaseMapper<MerchantConfig> {

    /**
     * [新增]
     *
     * @author duwan
     * @date 2021/02/03
     **/
    int insert(MerchantConfig tMerchantConfig);

    /**
     * [刪除]
     *
     * @author duwan
     * @date 2021/02/03
     **/
    int delete(int id);

    /**
     * [更新]
     *
     * @author duwan
     * @date 2021/02/03
     **/
    int update(MerchantConfig tMerchantConfig);

    int updateMerchantConfig(MerchantConfig tMerchantConfig);

    int updateIsAppMerchantConfig(MerchantConfig tMerchantConfig);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author duwan
     * @date 2021/02/03
     **/
    MerchantConfig load(int id);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author duwan
     * @date 2021/02/03
     **/
    MerchantConfig queryByMerchantCode(String merchantCode);

    /**
     * [查询] 分页查询
     *
     * @author duwan
     * @date 2021/02/03
     **/
    List<MerchantConfig> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author duwan
     * @date 2021/02/03
     **/
    int pageListCount(int offset, int pagesize);


    @Update("UPDATE t_merchant_config set settle_switch_advance=#{settleSwitchAdvance},cash_out_update=unix_timestamp(now())*1000 where merchant_code=#{merchantCode} ")
    int updateMerchantSettleSwitchAdvance(MerchantConfig updateMerchant);

    @Update("UPDATE t_merchant_config set settle_switch_basket=#{settleSwitchBasket},cash_out_update=unix_timestamp(now())*1000 where merchant_code=#{merchantCode} ")
    int updateMerchantSettleSwitchBasket(MerchantConfig updateMerchant);


    String getCreditMerchant(String merchantCode);

    @Select("select * from t_merchant_config m where m.merchant_code=#{merchantCode}")
    MerchantConfig getConfigByMerchantCode(@Param("merchantCode") String merchantCode);

    List<MerchantConfig> queryConfigList();

    List<MerchantConfig> queryMerchantRiskList();

    List<MerchantCodeConfig> queryCodeConfigList();

    MerchantCodeConfig queryCodeConfigById(MerchantCodeConfig merchantCodeConfig);

    List<MerchantCodeConfigLog> queryCodeConfigLogList(@Param("merchantCode")String merchantCode);

    int updateCodeConfig(MerchantCodeConfig config);

    int insertMerchantCodeConfigLog(MerchantCodeConfigLog configLog);

    String getSystemConfig(@Param("key") String tagMarketLevelStatus);

    int updateDefaultVideoDomain(MerchantConfig merchantConfig);

    String queryDefaultVideoDomain(@Param("merchantCodeList") List<String> merchantCodeList);

    List<MerchantPO> queryMerchantInfo(@Param("merchantCodeList") List<String> merchantCodeList);

    List<MerchantPO> selectChildrenMerchant(@Param("merchantId") String merchantId);

    int updateConfigFilter(MerchantConfig config);

    MerchantConfig selectMerchantByCode(@Param("merchantCode") String merchantCode);
}
