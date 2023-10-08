package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import com.panda.multiterminalinteractivecenter.vo.MaintenanceRecordOutDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.mapper
 * @Description :  TODO
 * @Date: 2022-03-18 14:03:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Mapper
public interface MaintenancePlatformMapper extends BaseMapper<MaintenancePlatform> {

    /**
     * 新增
     * @author duwan
     * @date 2022/03/18
     **/
    int insert(MaintenancePlatform maintenancePlatform);

    /**
     * 刪除
     * @author duwan
     * @date 2022/03/18
     **/
    int delete(int id);

    /**
     * 更新
     * @author duwan
     * @date 2022/03/18
     **/
    int update(MaintenancePlatform maintenancePlatform);

    /**
     * 查询 根据主键 id 查询
     * @author duwan
     * @date 2022/03/18
     **/
    MaintenancePlatform load(int id);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/03/18
     **/
    List<MaintenancePlatform> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/03/18
     **/
    int pageListCount(int offset,int pagesize);

    /**
     * 根据数据ID获取系统集合
     * @param dataCodes
     * @return
     */
    List<MaintenancePlatform> findListByDataCode(List<String> dataCodes);

    MaintenanceRecordOutDto querySystemInfo(@Param("dataCode") String dataCode);


}
