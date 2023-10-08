package com.panda.sport.backup83.mapper;

import com.panda.sport.merchant.common.vo.BetOrderV2VO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface Backup83OrderMixV2Mapper {
    List<OrderSettle> queryBetOrderList(BetOrderV2VO betOrderVO);

    List<OrderSettle> querySettledOrderList(BetOrderV2VO betOrderVO);

    List<OrderSettle> queryLiveOrderList(BetOrderV2VO betOrderVO);

    int countBetOrderList(BetOrderV2VO betOrderVO);

    int countSettledOrderList(BetOrderV2VO betOrderVO);

    int countLiveOrderList(BetOrderV2VO betOrderVO);

    String getNameByNameCode(@Param("language") String language,
                             @Param("nameCode") String nameCode);

    List<OrderSettle> queryUserMerchantPOList(Set<Long> uidList);
}
