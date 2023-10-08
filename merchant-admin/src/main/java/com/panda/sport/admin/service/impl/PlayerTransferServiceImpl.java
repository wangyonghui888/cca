package com.panda.sport.admin.service.impl;

import com.panda.sport.admin.feign.MerchantApiClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.PlayerTransferService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.backup.mapper.BackupTransferRecordErrorMapper;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.TransferRecordVO;
import com.panda.sport.merchant.common.vo.UserAccountFindVO;
import com.panda.sport.merchant.common.vo.UserTransferVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.manage.service.impl.AbstractUserTransferService;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service("playTransferService")
public class PlayerTransferServiceImpl extends AbstractUserTransferService implements PlayerTransferService {
    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private BackupTransferRecordErrorMapper transferRecordErrorMapper;

    @Override
    public Response<Object> queryUserTransferList(UserTransferVO vo, String language) {
        try {
            assemblyQueryParam(vo);
            return Response.returnSuccess(this.abstractQueryUserTransferList(vo, language));
        } catch (Exception e) {
            log.error("获取账变列表异常", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public void retryTransfer(UserRetryTransferVO vo) {
        UserAccountFindVO findVO = new UserAccountFindVO();
        findVO.setTransferIdList(vo.getTransferIdList());
        List<TransferRecordVO> transferRecordVOList = transferRecordErrorMapper.findTransferRecord(findVO);
        Map<String, List<TransferRecordVO>> transferMap = transferRecordVOList.stream().collect(Collectors.groupingBy(TransferRecordVO::getMerchantCode));
        for (Map.Entry entry : transferMap.entrySet()) {
            String merchantCode = (String) entry.getKey();
            List<TransferRecordVO> tempList = (List<TransferRecordVO>) entry.getValue();
            UserRetryTransferVO newvo = new UserRetryTransferVO();
            List<String> list = tempList.stream().map(TransferRecordVO::getTransferId).collect(Collectors.toList());
            newvo.setTransferIdList(list);
            newvo.setRetryCount(30L);
            newvo.setUserName("System");
            merchantApiClient.retryTransfer(newvo, merchantCode);
        }
    }

    private void assemblyQueryParam(UserTransferVO vo) {
        JwtUser user = SecurityUtils.getUser();
        if (user.getAgentLevel() == 1 || user.getAgentLevel() == 10) {
            List<String> merchantCodeList = localCacheService.getMerchantCodeList(user.getMerchantId(), user.getAgentLevel());
            if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                List<String> result = new ArrayList<>();
                result.addAll(merchantCodeList);
                result.add(user.getMerchantCode());
                vo.setMerchantCodeList(result);
            } else {
                vo.setMerchantCode(user.getMerchantCode());
            }
        } else {
            vo.setMerchantCode(user.getMerchantCode());
        }
    }
}
