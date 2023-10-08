package com.panda.sport.backup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.vo.SMatchInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackUpSMatchInfoMapper extends BaseMapper<SMatchInfo> {

    Long getSportIdByMatchIdSMatch(@Param("matchId") String matchId, @Param("tableName") String tableName);

    Long getSportIdByOutrightIdSMatch(@Param("outrightId") String outrightId);

    Long getSportIdById(@Param("id") String id);

    List<Long> getStandardMatchIds(@Param("tableName") String tableName, @Param("matchManageIdName") String matchManageIdName, @Param("matchManageId") String matchManageId);

    /**
     * 根据比赛ID查询赛事信息
     *
     * @param mid
     * @return
     */
    SMatchInfo getMatchInfoByMid(@Param("mid") Long mid);
}
