package com.panda.sport.backup83.mapper;

import com.panda.sport.merchant.common.vo.TransferRecordVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Backup83TransferRecordMapper {


    List<TransferRecordVO> findTransferRecord(UserAccountFindVO vo);

    List<TransferRecordVO> findTransferRecordExportList(UserAccountFindVO vo);

    int findTransferRecordExportListCount(UserAccountFindVO vo);

    Integer findTransferRecordCount(UserAccountFindVO vo);
}