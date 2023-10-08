package com.panda.multiterminalinteractivecenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.mapper
 * @Description :  TODO
 * @Date: 2022-03-18 14:13:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Mapper
public interface MaintenanceRecordMapper extends BaseMapper<MaintenanceRecord> {
    /**
     * 新增
     * @author duwan
     * @date 2022/03/18
     **/
    int insert(MaintenanceRecord maintenanceRecord);

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
    int update(MaintenanceRecord maintenanceRecord);

    /**
     * 查询 根据主键 id 查询
     * @author duwan
     * @date 2022/03/18
     **/
    MaintenanceRecord load(int id);

    /**
     * 查询 分页查询
     * @author duwan
     * @date 2022/03/18
     **/
    List<MaintenanceRecord> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author duwan
     * @date 2022/03/18
     **/
    int pageListCount(int offset,int pagesize);

    List<MaintenanceRecord> checkListByServerCode(List<String> serverCodes);

    int starMaintenancePlatform(@Param("starTime") long starTime);

    List<MaintenanceRecord> getRuningPlatform(@Param("dataCode") String dataCode);

    List<MaintenanceRecord>  getStarMaintenanceRecord(@Param("starTime") long starTime);

    List<MaintenanceRecord> getStarMaintenanceRecordIsRemind(@Param("starTime") long starTime);

    int updateStarMaintenanceRecordIsRemind(@Param("id") Long id);
}
