package com.panda.sport.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author YK
 * @Description: 公告栏
 * @date 2020/3/12 16:42
 */
@Repository
public interface MerchantNoticeMapper extends BaseMapper<MerchantNotice> {

    List<MerchantNotice> homePopNotice(@Param("releaseRange") Integer releaseRange, @Param("abnormalUserId") String abnormalUserId);


    List<MerchantNotice> getNoticeByParam(@Param("standardMatchId") String standardMatchId,@Param("playId") Long playId);

    MerchantNotice getNoticeIn12Hours(@Param("noticeTypeId") Integer noticeTypeId,
                                      @Param("matchId") Long matchId,
                                      @Param("noticeResultType") Integer noticeResultType,
                                      @Param("sportId") Integer sportId,
                                      @Param("fixStartTime") Long fixStartTime);
}
