package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panda.sport.merchant.common.po.bss.OperationsBannerSet;
import com.panda.sport.merchant.common.vo.MerchantTreeVO;
import com.panda.sport.merchant.common.vo.OperationsBannerVO;
import com.panda.sport.merchant.common.vo.OperationsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:08:48
 */
public interface OperationsBannerSetMapper extends BaseMapper<OperationsBannerSet> {
    /**
     * 查询所有商户
     *
     * @param param param
     */
    List<MerchantTreeVO> getMerchantList(MerchantTreeVO param);


    OperationsBannerVO  getOldOneInfo(@Param("id") Long id);

    List<OperationsBannerVO> getList(@Param("banner") OperationsVO paramVO);

    /**
     * 根据商户code 查找商户名称
     * */
    List<String> getMerchantName(@Param("merchants") List<String> merchants);
}
