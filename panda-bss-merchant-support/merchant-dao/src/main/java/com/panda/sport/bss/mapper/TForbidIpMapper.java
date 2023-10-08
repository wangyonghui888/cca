package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.bss.TForbidIp;
import com.panda.sport.merchant.common.vo.DomainVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.bss.mapper
 * @Description :  TODO
 * @Date: 2021-09-08 11:36:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Mapper
@Repository
public interface TForbidIpMapper {

    /**
     * 新增
     *
     * @author duwan
     * @date 2021/09/08
     **/
    int insert(TForbidIp tForbidIp);

    /**
     * 刪除
     *
     * @author duwan
     * @date 2021/09/08
     **/
    int delete(Long id);

    int deleteByIpName(@Param("ipName") String ipName);
    /**
     * 更新
     *
     * @author duwan
     * @date 2021/09/08
     **/
    int update(TForbidIp tForbidIp);

    /**
     * 查询 根据主键 id 查询
     *
     * @author duwan
     * @date 2021/09/08
     **/
    TForbidIp load(int id);

    /**
     * 查询 分页查询
     *
     * @author duwan
     * @date 2021/09/08
     **/
    List<TForbidIp> pageList(DomainVo domainVo);

    /**
     *
     * @author duwan
     * @date 2021/09/08
     **/
    @Select("select ip_name from t_forbid_ip")
    List<String> ListNames();
    /**
     * 查询 分页查询 count
     *
     * @author duwan
     * @date 2021/09/08
     **/
    int pageListCount(DomainVo domainVo);
}
