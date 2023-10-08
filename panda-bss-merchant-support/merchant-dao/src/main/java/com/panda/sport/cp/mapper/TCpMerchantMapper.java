package com.panda.sport.cp.mapper;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.cp.mapper
 * @Description :  TODO
 * @Date: 2022-01-07 14:39:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */

import com.panda.sport.merchant.common.po.cp.TMerchant;
import com.panda.sport.merchant.common.vo.ThirdMerchantVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 用户信息
 * @author duwan
 * @date 2022-01-07
 */
@Mapper
@Repository
public interface TCpMerchantMapper {

    List<ThirdMerchantVo> getMerchantList();

    /**
     * 新增
     * @author duwan
     * @date 2022/01/07
     **/
    int insert(TMerchant tMerchant);

    /**
     * 刪除
     * @author duwan
     * @date 2022/01/07
     **/
    int delete(int id);

    /**
     * 更新
     * @author duwan
     * @date 2022/01/07
     **/
    int update(TMerchant tMerchant);

    /**
     * 查询 根据主键 id 查询
     * @author duwan
     * @date 2022/01/07
     **/
    TMerchant load(int id);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/01/07
     **/
    List<TMerchant> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/01/07
     **/
    int pageListCount(int offset,int pagesize);

    List<ThirdMerchantVo> getMerchantByCodes(@Param("merchantCodes")List<String> merchantCodes);

}
