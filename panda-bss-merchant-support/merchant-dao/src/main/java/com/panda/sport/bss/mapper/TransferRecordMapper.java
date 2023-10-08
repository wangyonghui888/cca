package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.bss.TransferRecordPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRecordMapper {

    List<TransferRecordPO> selectListByUser(@Param(value = "merchantCode") String merchantCode, @Param(value = "userId") Long userId,
                                            @Param(value = "start") Integer start, @Param(value = "end") Integer end,
                                            @Param(value = "startTimeL") Long startTimeL, @Param(value = "endTimeL") Long endTimeL);

    TransferRecordPO selectById(@Param(value = "merchantCode") String merchantCode, @Param(value = "userId") Long userId,
                                @Param(value = "transferId") String transferId);

    Integer countListByUser(@Param(value = "merchantCode") String merchantCode, @Param(value = "userId") Long userId,
                            @Param(value = "startTimeL") Long startTimeL, @Param(value = "endTimeL") Long endTimeL);

    int insert(TransferRecordPO transferRecordPO);


    TransferRecordPO getTransferRecord(@Param(value = "transferId") String transferId);

    void updateTransferRecord(@Param("transferId") String transferId, @Param("status") Integer status, @Param("msg") String msg);

}