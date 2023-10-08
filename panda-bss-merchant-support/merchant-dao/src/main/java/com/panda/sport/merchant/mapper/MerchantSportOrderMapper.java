package com.panda.sport.merchant.mapper;

import com.panda.sport.merchant.common.po.merchant.MerchantSportOrderPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantSportOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MerchantSportOrderPO record);

    int insertSelective(MerchantSportOrderPO record);

    MerchantSportOrderPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MerchantSportOrderPO record);

    int updateByPrimaryKey(MerchantSportOrderPO record);

    List<MerchantSportOrderPO> selectByPageInfo(MerchantSportOrderPO record);

    /**
     * 报表导出下载
     * @param record
     * @return
     */
    List<String > reportDownload(MerchantSportOrderPO record );

}