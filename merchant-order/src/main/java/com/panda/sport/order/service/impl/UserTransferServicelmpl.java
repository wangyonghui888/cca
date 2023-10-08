package com.panda.sport.order.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.panda.sport.backup.mapper.BackupTransferRecordErrorMapper;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.impl.AbstractUserTransferService;
import com.panda.sport.order.feign.MerchantApiClient;
import com.panda.sport.order.service.UserTransferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserTransferServicelmpl extends AbstractUserTransferService implements UserTransferService {

    @Autowired
    private MerchantApiClient merchantApiClient;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private BackupTransferRecordErrorMapper transferRecordMapper;

    /**
     * 查询玩家账变记录
     *
     * @Param: [vo]
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:26
     */
    @Override

    public Response<Object> queryUserTransferList(UserTransferVO vo, String language) {
        try {
            return Response.returnSuccess(this.abstractQueryUserTransferList(vo, language));
        } catch (Exception e) {
            log.error("queryUserTransferList查询用户账变异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response retryTransfer(UserRetryTransferVO vo) {
        if (CollectionUtils.isEmpty(vo.getTransferId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        vo.setTransferIdList(vo.getTransferId());
        //  bssBackendClient.retryTransfer(vo);
        UserAccountFindVO findVO = new UserAccountFindVO();
        findVO.setTransferIdList(vo.getTransferIdList());
        List<TransferRecordVO> transferRecordVOList = transferRecordMapper.findTransferRecord(findVO);
        callMerchantService(transferRecordVOList);
        return Response.returnSuccess();
    }

    @Override
    @Async
    public void executeRetry() {
        try {
            UserAccountFindVO findVO = new UserAccountFindVO();
            findVO.setSort("desc");
            /*查询七天内的免转失败记录 交易类弄为 结算、退款、礼金、拒单（目前下注先扣钱）、结算回滚、下注取消、下注取消回滚；*/
            findVO.setStatus(0);
            findVO.setTransferMode(1);
            List<Integer> bizTypeList = Arrays.asList(2, 3, 4, 5, 6, 9, 10, 11, 12);
            findVO.setBizTypeList(bizTypeList);
            findVO.setRetryCount(30);
            findVO.setStart(0);
            findVO.setPageSize(1000);
            log.info("executeRetry:" + findVO);
            Integer count = transferRecordMapper.findTransferRecordCount(findVO);
            if (count == 0) {
                log.info("查询派彩失败数据executeRetry为空!");
                return;
            }
            log.info("查询派彩失败数据count="+count);
            if(count>1000){
                int pageC = (count % 1000 == 0) ? (count / 1000) : (count / 1000 + 1);
                log.info("查询派彩失败数据pageC="+pageC);
                for( int i=0;i<=pageC;i++){
                    findVO.setStart(i*1000);
                    log.info("查询派彩失败数据pageC="+i*1000);
                    List<TransferRecordVO> transferRecordVOList = transferRecordMapper.findTransferRecord(findVO);
                    callMerchantService(transferRecordVOList);
                }
            }else {
                log.info("查询派彩失败数据pageC=2");
                List<TransferRecordVO> transferRecordVOList = transferRecordMapper.findTransferRecord(findVO);
                callMerchantService(transferRecordVOList);
            }
        } catch (Exception e) {
            log.error("执行重试免转钱包ERROR!", e);
        }
    }

    private void callMerchantService(List<TransferRecordVO> transferRecordVOList) {
        Map<String, List<TransferRecordVO>> transferMap = transferRecordVOList.stream().collect(Collectors.groupingBy(TransferRecordVO::getMerchantCode));
        for (Map.Entry entry : transferMap.entrySet()) {
            String merchantCode = (String) entry.getKey();
            List<TransferRecordVO> tempList = (List<TransferRecordVO>) entry.getValue();
            UserRetryTransferVO vo = new UserRetryTransferVO();
            List<String> list = tempList.stream().map(TransferRecordVO::getTransferId).collect(Collectors.toList());
            vo.setTransferIdList(list);
            vo.setUserName("System");
            merchantApiClient.retryTransfer(vo, merchantCode);
        }
    }


    @Override
    @Async
    public void executeRetry2() {
        try {
            UserAccountFindVO findVO = new UserAccountFindVO();
            findVO.setSort("desc");
            /*查询七天内的免转失败记录 交易类弄为 结算、退款、礼金、拒单（目前下注先扣钱）、结算回滚、下注取消、下注取消回滚；*/
            findVO.setStatus(0);
            findVO.setTransferMode(1);
            List<Integer> bizTypeList = Arrays.asList(2,3, 4, 5, 6,9,10,11,12);
            findVO.setBizTypeList(bizTypeList);
            findVO.setRetryCount(102);
            findVO.setMinRetryCount(30);
            findVO.setStart(0);
            findVO.setPageSize(1000);
            log.info("executeRetry:" + findVO);
            Integer count = transferRecordMapper.findTransferRecordCount(findVO);
            if (count == 0) {
                log.info("executeRetry为空!");
                return;
            }
            List<TransferRecordVO> transferRecordVOList = transferRecordMapper.findTransferRecord(findVO);
            callMerchantService(transferRecordVOList);
        } catch (Exception e) {
            log.error("执行重试免转钱包ERROR!", e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addChangeRecordHistory(AccountChangeHistoryFindVO vo,HttpServletRequest request) {
        try {
            String language = request.getHeader("language");
            String userId = request.getHeader("user-id");
            String userName = request.getHeader("merchantName");
            language= StringUtils.isEmpty(language) ? LanguageEnum.EN.getCode() : language;
            vo.setModifyUser(userName);
            merchantApiClient.addChangeRecordHistory(vo,vo.getMerchantCode());
            MerchantLogPageEnum pageEnum =null;
            if(null!=vo.getType()) {
                if ("0".equals(vo.getType())) {
                    pageEnum = MerchantLogPageEnum.PLUS_DEDUCTION;
                } else {
                    pageEnum = MerchantLogPageEnum.PLUS_DATA_DEDUCTION;
                }
            }else{
                pageEnum = MerchantLogPageEnum.PLUS_DEDUCTION;
            }
            merchantLogService.savePlusDeductionLog(pageEnum, vo, MerchantLogConstants.MERCHANT_IN, language, IPUtils.getIpAddr(request),userId,userName);
            return Response.returnSuccess();
        }catch (Exception e) {
            log.error("调用feign===merchantApiClient==api出错!", e);
            return Response.returnFail("调用feign===merchantApiClient==api出错!");
        }
    }
}
