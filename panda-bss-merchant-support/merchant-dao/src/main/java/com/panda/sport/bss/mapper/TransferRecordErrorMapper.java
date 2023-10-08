package com.panda.sport.bss.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.panda.sport.merchant.common.po.bss.TransferRecordErrorPO;
import com.panda.sport.merchant.common.po.bss.TransferRecordPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRecordErrorMapper extends BaseMapper<TransferRecordErrorPO> {

    List<TransferRecordPO> getTransferRecordList(@Param("transferIdList") List<String> transferIdList, @Param("retryCount") Long retryCount);

    void updateTransferRecord(@Param("transferId") String transferId, @Param("status") Integer status, @Param("msg") String msg);

    int countByTransferId(@Param("transferId") String transferId);

}
