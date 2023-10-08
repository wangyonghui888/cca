package com.panda.sport.merchant.mapper;

import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.vo.MerchantLogFindVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.mapper
 * @Description :  商户日志orm接口
 * @Date: 2020-09-01 15:30
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Repository
public interface MerchantLogMapper {

    /**
     * [新增]
     * @author duwan
     * @date 2020/09/01
     **/
    int insert(MerchantLogPO merchantLog);

    /**
     * [刪除]
     * @author duwan
     * @date 2020/09/01
     **/
    int delete(int id);

    /**
     * [更新]
     * @author duwan
     * @date 2020/09/01
     **/
    int update(MerchantLogPO merchantLog);

    /**
     * [查询] 根据主键 id 查询
     * @author duwan
     * @date 2020/09/01
     **/
    MerchantLogPO load(int id);

    /**
     * [查询] 分页查询
     * @author duwan
     * @date 2020/09/01
     **/
    List<MerchantLogPO> pageList(MerchantLogFindVO findVO);

    /**
     * [查询] 分页查询 count
     * @author duwan
     * @date 2020/09/01
     **/
    int pageListCount(MerchantLogFindVO findVO);

    @Update("UPDATE merchant_log set merchant_name =#{merchantName} where merchant_code = #{merchantCode}")
    int updateMerchantName(@Param("merchantCode")String  merchantCode, @Param("merchantName")String  merchantName);

}
