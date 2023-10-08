package com.panda.sport.merchant.mapper;

import com.panda.sport.merchant.common.po.merchant.TMerchantGroupInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description t_merchant_group_info
 * @author duwan
 * @date 2022-01-01
 */
@Mapper
@Repository
public interface TMerchantGroupInfoMapper {

    /**
     * 新增
     * @author duwan
     * @date 2022/01/01
     **/
    int insert(TMerchantGroupInfo tMerchantGroupInfo);

    /**
     * 刪除
     * @author duwan
     * @date 2022/01/01
     **/
    int delete(int id);

    int deleteByGroupId(int merchantGroupId);
    /**
     * 更新
     * @author duwan
     * @date 2022/01/01
     **/
    int update(TMerchantGroupInfo tMerchantGroupInfo);

    /**
     * 查询 根据主键 id 查询
     * @author duwan
     * @date 2022/01/01
     **/
    TMerchantGroupInfo load(int id);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/01/01
     **/
    List<TMerchantGroupInfo> pageList(int offset, int pagesize);

    List<TMerchantGroupInfo> tMerchantGroupInfoByGroupId(@Param("merchantGroupId")Long merchantGroupId);

    List<TMerchantGroupInfo> getAllByCode(@Param("code")Integer  code);
    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/01/01
     **/
    int pageListCount(int offset,int pagesize);


}