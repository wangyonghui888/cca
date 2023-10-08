package com.panda.sport.merchant.mapper;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.mapper
 * @Description :  TODO
 * @Date: 2022-01-01 10:51:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */

import com.panda.sport.merchant.common.po.merchant.TMerchantGroup;
import com.panda.sport.merchant.common.vo.TMerchantGroupInfoVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description b端商户分组表
 * @author duwan
 * @date 2022-01-01
 */
@Mapper
@Repository
public interface MerchantGroupMapper {

    /**
     * 新增
     * @author duwan
     * @date 2022/01/01
     **/
    int insert(TMerchantGroup tMerchantGroup);

    /**
     * 刪除
     * @author duwan
     * @date 2022/01/01
     **/
    int delete(int id);

    /**
     * 更新
     * @author duwan
     * @date 2022/01/01
     **/
    int update(TMerchantGroup tMerchantGroup);

    /**
     * 查询 根据主键 id 查询
     * @author duwan
     * @date 2022/01/01
     **/
    TMerchantGroup load(int id);

    TMerchantGroup loadIdByGroupName(@Param("groupName") String groupName,@Param("groupCode") Integer groupCode);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/01/01
     **/
    List<TMerchantGroup> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/01/01
     **/
    int pageListCount(int offset,int pagesize);

    List<TMerchantGroup> selectAll(@Param("groupCode")Integer groupCode);

    List<TMerchantGroupInfoVo> getMerchantGroupInfoByThirdCode(@Param("groupCode")Integer groupCode,@Param("account")String account);

    List<TMerchantGroup> selectAllByStatus(@Param("status")Integer status);
}