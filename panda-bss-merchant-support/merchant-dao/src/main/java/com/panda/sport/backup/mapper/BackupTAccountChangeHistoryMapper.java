package com.panda.sport.backup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.AccountChangeHistoryPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.merchant.common.vo.UserTransferVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户变更历史记录表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2020-01-08
 */
@Repository
public interface BackupTAccountChangeHistoryMapper extends BaseMapper<AccountChangeHistoryPO> {

    int countChangeHistoryList(UserTransferVO vo);

    List<AccountChangeHistoryPO> queryChangeHistoryList(UserTransferVO vo);

    List<AccountChangeHistoryFindVO> queryChangeHistoryListNew(UserAccountFindVO vo);

    List<Map<String, Object>> queryChangeHistoryListNewExportList(UserAccountFindVO vo);

    int queryChangeHistoryListNewExportListCount(UserAccountFindVO vo);

    int countChangeHistoryListNew(UserAccountFindVO vo);

    int countChangeHistory(@Param("merchantCode") String merchantCode, @Param("startTime") long startTime, @Param("endTime") long endTime);

    List<Map<String, Object>> queryAccountChangeList(@Param("merchantCode") String merchantCode, @Param("startTime") long startTime, @Param("endTime") long endTime,
                                                     @Param("start") int start, @Param("size") int size);
}