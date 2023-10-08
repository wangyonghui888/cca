package com.panda.sport.bss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.bss.AccountChangeHistoryPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.TournamentVo;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.merchant.common.vo.UserTransferVO;
import com.panda.sport.merchant.common.vo.api.BetApiVo;
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
public interface TAccountChangeHistoryMapper extends BaseMapper<AccountChangeHistoryPO> {

}