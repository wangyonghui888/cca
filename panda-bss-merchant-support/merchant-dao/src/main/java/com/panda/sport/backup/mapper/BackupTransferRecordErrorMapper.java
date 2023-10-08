package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.vo.TransferRecordVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BackupTransferRecordErrorMapper {


    List<TransferRecordVO> findTransferRecord(UserAccountFindVO vo);

    List<TransferRecordVO> findRecordError(@Param("merchantCode") String merchantCode);

    Integer findTransferRecordCount(UserAccountFindVO vo);

    List<Map<String,Object>> countTransferGroup();
}