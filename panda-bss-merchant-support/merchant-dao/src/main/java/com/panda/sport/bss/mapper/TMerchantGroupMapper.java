package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.MerchantGroupPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description t_domain
 * @author duwan
 * @date 2021-08-19
 */
@Mapper
@Repository
public interface TMerchantGroupMapper extends BaseMapper<MerchantGroupPO> {


    int createMerchantGroup(MerchantGroupPO merchantGroupPO);
    int updateMerchantGroup(MerchantGroupPO merchantGroupPO);
    int deleteMerchantGroup(MerchantGroupPO merchantGroupPO);
    List<MerchantGroupPO> selectMerchantGroup(MerchantGroupPO merchantGroupPO);

    MerchantGroupPO selectMerchantGroupById(@Param("id")Long id);
    /**
     * 查询 分页查询
     * @author duwan
     * @date 2021/08/19
     **/
    List<MerchantGroupPO> pageList(MerchantGroupPO merchantGroupPO);

    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2021/08/19
     **/
    int pageListCount(MerchantGroupPO merchantGroupPO);



}